package com.nova.cstorage.map;

import static android.content.ContentValues.TAG;
import static androidx.core.content.PackageManagerCompat.LOG_TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.DnsResolver;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.Response;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.nova.cstorage.R;
import com.nova.cstorage.book.bookSearchAdapter;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapReverseGeoCoder;
import net.daum.mf.map.api.MapView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;


public class Activity_map extends AppCompatActivity implements View.OnClickListener, MapView.MapViewEventListener, MapView.CurrentLocationEventListener {
    String BASE_URL = "https://dapi.kakao.com/";
    String API_KEY = "KakaoAK 06d38d1906a9183c233b697d84226a39"; //카카오 개발자 사이트에서 발급 받은 키를 REST API 키를 입력합니다 KakaoAK와 함께 전송하세요
    TextView searchResult;
    EditText searchMap, searchRegion;
    ArrayList<MapPlaceData> mapSearchDataArrayList;
    MapPlaceData mapPlaceData;
    MapSearchAdapter mapSearchAdapter;
    private RecyclerView placeSearchview;
    private ViewGroup mapViewContainer;
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION};
    Context context;
    private int pageNumber = 1;
    MapPoint mapPoint;
    MapView mapView;
    MapPOIItem marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchplace);

        mapView = new MapView(this);

        mapViewContainer = (ViewGroup) findViewById(R.id.mapView);
        mapViewContainer.addView(mapView);


        searchMap = (EditText) findViewById(R.id.mapSearch_result);
        searchRegion = (EditText) findViewById(R.id.mapSearch_region);


        placeSearchview = (RecyclerView) findViewById(R.id.placeSearch_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        placeSearchview.setLayoutManager(linearLayoutManager);

        Button mapSearch = (Button) findViewById(R.id.btn_map_search);



        mapView.setMapViewEventListener(this);

        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting();
        } else {
            checkRunTimePermission();
        }
        mapPoint = MapPoint.mapPointWithGeoCoord(37.485624, 126.972105);
        mapView.setMapCenterPoint(mapPoint, true);
        //        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
        marker = new MapPOIItem();

        //맵 포인트 위도경도 설정

        marker.setItemName("Default Marker");
        marker.setTag(0);
        marker.setMapPoint(mapPoint);
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

        mapView.addPOIItem(marker);
        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting();
        } else {
            checkRunTimePermission();
        }


        mapSearch.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapViewContainer.removeAllViews();
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint currentLocation, float accuracyInMeters) {
        MapPoint.GeoCoordinate mapPointGeo = currentLocation.getMapPointGeoCoord();
        Log.i(LOG_TAG, String.format("MapView onCurrentLocationUpdate (%f,%f) accuracy (%f)", mapPointGeo.latitude, mapPointGeo.longitude, accuracyInMeters));
        MapPoint currentMapPoint = MapPoint.mapPointWithGeoCoord(mapPointGeo.latitude, mapPointGeo.longitude);
        //이 좌표로 지도 중심 이동
        mapView.setMapCenterPoint(currentMapPoint, true);
        //전역변수로 현재 좌표 저장
        double mCurrentLat = mapPointGeo.latitude;
        double mCurrentLng = mapPointGeo.longitude;
        Log.d(TAG, "현재위치 => " + mCurrentLat + "  " + mCurrentLng);
        mapViewContainer.setVisibility(View.GONE);
        //트래킹 모드가 아닌 단순 현재위치 업데이트일 경우, 한번만 위치 업데이트하고 트래킹을 중단시키기 위한 로직
//        if (!isTrackingMode) {
//            mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {
    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {
    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {
    }


    private void onFinishReverseGeoCoding(String result) {
//        Toast.makeText(LocationDemoActivity.this, "Reverse Geo-coding : " + result, Toast.LENGTH_SHORT).show();
    }

    // ActivityCompat.requestPermissions를 사용한 퍼미션 요청의 결과를 리턴받는 메소드
    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        super.onRequestPermissionsResult(permsRequestCode, permissions, grandResults);
        if (permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면
            boolean check_result = true;

            // 모든 퍼미션을 허용했는지 체크합니다.
            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }

            if (check_result) {
                Log.d("@@@", "start");
                //위치 값을 가져올 수 있음

            } else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있다
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])) {
                    Toast.makeText(Activity_map.this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(Activity_map.this, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    void checkRunTimePermission() {

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(Activity_map.this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED) {
            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)
            // 3.  위치 값을 가져올 수 있음

        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.
            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(Activity_map.this, REQUIRED_PERMISSIONS[0])) {
                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(Activity_map.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(Activity_map.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(Activity_map.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }
        }
    }

    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_map.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하시겠습니까?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case GPS_ENABLE_REQUEST_CODE:
                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {
                        Log.d("@@@", "onActivityResult : GPS 활성화 되있음");
                        checkRunTimePermission();
                        return;
                    }
                }
                break;
        }
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_map_search) {
            pageNumber = 1;
            String culture = searchMap.getText().toString();
            String region = searchRegion.getText().toString();
            if (region.equals("")) {
                Toast.makeText(getApplicationContext(), "지역명을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            } else {
                KakaoAPIInterface spotInterface = ApiClient.getApiClient().create(KakaoAPIInterface.class);
                Call<MapSearchData> call = spotInterface.getSearchKeyword(API_KEY, culture + " " + region, pageNumber);

                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchMap.getWindowToken(), 0);

                call.enqueue(new Callback<MapSearchData>() {
                    @Override  //연결 성공 시에 실행되는 부분
                    public void onResponse(retrofit2.Call<MapSearchData> call, retrofit2.Response<MapSearchData> response) {
                        Log.e("onSuccess", response.toString());

                        mapSearchDataArrayList = new ArrayList<MapPlaceData>();
                        for (int i = 0; i < response.body().documents.size(); i++) {
                            String mapPlace = response.body().documents.get(i).place_name;
                            String mapCategory = response.body().documents.get(i).category_name;
                            String mapAddress = response.body().documents.get(i).address_name;
                            String mapPhone = response.body().getDocuments().get(i).phone;
                            String mapURL = response.body().getDocuments().get(i).place_url;
                            String x = response.body().getDocuments().get(i).x;
                            String y = response.body().getDocuments().get(i).y;

                            mapPlaceData = new MapPlaceData();
                            mapPlaceData.setPhone(mapPhone);
                            mapPlaceData.setPlaceName(mapPlace);
                            mapPlaceData.setCategory(mapCategory);
                            mapPlaceData.setAddress(mapAddress);
                            mapPlaceData.setUrl(mapURL);
                            mapPlaceData.setx(x);
                            mapPlaceData.sety(y);

                            mapSearchDataArrayList.add(mapPlaceData);
                        }
                        mapSearchAdapter = new MapSearchAdapter(context, mapSearchDataArrayList);

                        placeSearchview.setAdapter(mapSearchAdapter);
                        mapPoint = MapPoint.mapPointWithGeoCoord(Double.parseDouble(response.body().documents.get(0).y), Double.parseDouble(response.body().documents.get(0).x));
                              marker = new MapPOIItem();
                            marker.setItemName(response.body().documents.get(0).place_name);
                            marker.setTag(0);
                            marker.setMapPoint(mapPoint);
                            marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
                            marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

                            mapView.addPOIItem(marker);
                        if (!checkLocationServicesStatus()) {
                            showDialogForLocationServiceSetting();
                        } else {
                            checkRunTimePermission();
                        }
                        mapView.setMapCenterPoint(mapPoint, true);

//                    String status = response.body().getStatus();
//
//                    System.out.println("안녕" + response.body().getMessage());
                    }

                    @Override
                    public void onFailure(retrofit2.Call<MapSearchData> call, Throwable t) {
                        Log.e("onfail", "에러 = " + t.getMessage());
                    }
                });
            }
        }
    }

    @Override
    public void onMapViewInitialized(MapView mapView) {

    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }
}


