package com.example.mylog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Logout extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    Button logout;
    TextView email1;
    ImageView image;
    GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);
image=(ImageView)findViewById(R.id.image);
email1=(TextView)findViewById(R.id.textbtn);
logout=(Button)findViewById(R.id.log_out);
        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser user=firebaseAuth.getCurrentUser();
        if (user!=null)
        {
            Glide.with(this).load(user.getPhotoUrl()).into(image);
            email1.setText(user.getEmail());

        }
        googleSignInClient = GoogleSignIn.getClient(Logout.this
        , GoogleSignInOptions.DEFAULT_SIGN_IN);
        if(user.getProviderId()=="facebook.com")
        {
logout.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        firebaseAuth.signOut();
        LoginManager.getInstance().logOut();
        startActivity(new Intent(Logout.this,MainActivity.class));
        finish();
    }
});}
        else
        {
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                       if (task.isSuccessful())
                       {
                           firebaseAuth.signOut();
                           Toast.makeText(getApplicationContext(),"Log out is successful",Toast.LENGTH_SHORT).show();
                           finish();
                       }
                        }
                    });
                }
            });
        }


    }
}
