//package com.nova.cstorage;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.android.gms.auth.api.Auth;
//import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
//import com.google.android.gms.auth.api.signin.GoogleSignInResult;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.common.api.internal.OnConnectionFailedListener;
//import com.nova.cstorage.map.Activity_map;
//
//
//public class Activity_Login extends AppCompatActivity implements View.OnClickListener, OnConnectionFailedListener {
//
//    EditText inputMail;
//    public static final int RC_SIGN_IN = 1;
//    private GoogleApiClient mGoogleApiClient;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.login);
//
//        inputMail = (EditText) findViewById(R.id.login_email);
//
//
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .build();
//
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
//                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                .build();
//
//        Button btn_login = (Button) findViewById(R.id.btn_login);
//        Button btn_join = (Button) findViewById(R.id.btn_join);
//        Button btn_back = (Button) findViewById(R.id.btn_back);
//
//        Button btn_ggLogin = (Button) findViewById(R.id.sign_in_button);
//
//        btn_login.setOnClickListener(this);
//        btn_join.setOnClickListener(this);
//        btn_back.setOnClickListener(this);
//        btn_ggLogin.setOnClickListener(this);
//    }
//
//    @Override
//    public void onClick(View v) {
//        if (v.getId() == R.id.btn_login) {
//            String mail = inputMail.getText().toString();
//            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//            intent.putExtra("1", mail);
//            startActivity(intent);
//        }
//        if (v.getId() == R.id.btn_join) {
//            Intent intent = new Intent(getApplicationContext(), Activity_Join.class);
//            startActivity(intent);
//
//        }
//        if (v.getId() == R.id.sign_in_button) {
//            signIn();
//
//
//        }
//        if (v.getId() == R.id.btn_back) {
//            Log.d("뒤로가기", "1");
//            Intent intent_back = new Intent(getApplicationContext(), MainActivity.class);
//            startActivity(intent_back);
//            finish();
//        }
//    }
//
//    private void signIn() {
//        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
//        startActivityForResult(signInIntent, RC_SIGN_IN);
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
//        if (requestCode == RC_SIGN_IN) {
//            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//            handleSignInResult(result);
//        }
//    }
//
//    private void handleSignInResult(GoogleSignInResult result) {
//        Log.d("로그인", "handleSignInResult:" + result.isSuccess());
////        if (result.isSuccess()) {
////            // Signed in successfully, show authenticated UI.
////            GoogleSignInAccount acct = result.getSignInAccount();
////            mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
////            updateUI(true);
////        } else {
////            // Signed out, show unauthenticated UI.
////            updateUI(false);
////        }
//    }
//}
