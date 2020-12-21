package com.example.mylog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;


public class MainActivity extends AppCompatActivity {
    SignInButton btnsignIn;
    private static final String EMAIL = "email";
CallbackManager callbackManager;
FirebaseAuth firebaseAuth;
GoogleSignInClient googleSignInClient;
    LoginButton loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginButton=(LoginButton)findViewById(R.id.login_button);
        setContentView(R.layout.activity_main);
        firebaseAuth=FirebaseAuth.getInstance();
        callbackManager = CallbackManager.Factory.create();
        btnsignIn=(SignInButton)findViewById(R.id.bt_signin);
        final GoogleSignInOptions googleSignInOptions=new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN
        ).requestIdToken("89091479853-5jn8ilsqr7kjqfpokcq8e83qcc14gqq2.apps.googleusercontent.com")
                .requestEmail()
                .build();
        googleSignInClient= GoogleSignIn.getClient(MainActivity.this
        ,googleSignInOptions);
        btnsignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=googleSignInClient.getSignInIntent();
                startActivityForResult(intent,100);
            }
        });

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(EMAIL));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
              facebookhandle(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
    }
    @Override
    protected void onStart()
    {
        super.onStart();
        FirebaseUser user=firebaseAuth.getCurrentUser();
        if (user!=null)
        {
            startActivity(new Intent(MainActivity.this,Logout.class));
            finish();
        }
    }
    private void facebookhandle(AccessToken token)
    {
        AuthCredential credential= FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
           if (task.isSuccessful())
           {
               startActivity(new Intent(MainActivity.this,Logout.class));
               finish();
           }
           else {
               Toast.makeText(getApplicationContext(),"unsuccessful",Toast.LENGTH_SHORT).show();

           }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==100)
        {
            Task<GoogleSignInAccount> signInAccountTask=GoogleSignIn
                    .getSignedInAccountFromIntent(data);
            if (signInAccountTask.isSuccessful())
            {
                String s="Google Sign in successful";
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
                try {
                    GoogleSignInAccount googleSignInAccount=signInAccountTask
                            .getResult(ApiException.class);
                    if (googleSignInAccount != null)
                    {
                        AuthCredential authCredential= GoogleAuthProvider
                                .getCredential(googleSignInAccount.getIdToken()
                                ,null);
                        firebaseAuth.signInWithCredential(authCredential)
                                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {

                                        if (task.isSuccessful())
                                        {

                                            startActivity(new Intent(MainActivity.this,Logout.class)
                                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                            );
                                            Toast.makeText(getApplicationContext(),"Log in is successful",Toast.LENGTH_SHORT).show();
                                        }
                                        else
                                        {
                                            Toast.makeText(getApplicationContext(),"Failed"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                    }
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
//x5w0TKfR9doqx2/Dn4HREoRiPBc=