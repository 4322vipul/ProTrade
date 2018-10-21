package com.aimheadshot.buynsell;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aimheadshot.buynsell.Activities.BuyNSellActivity;
import com.aimheadshot.buynsell.Activities.CreateAccountActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private EditText email_edit_text;
    private EditText password_edit_text;
    private Button login_button;
    private Button createAccountButton;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth=FirebaseAuth.getInstance();
        email_edit_text=(EditText)findViewById(R.id.emailIdEditTextId1);
        password_edit_text=(EditText)findViewById(R.id.passwordEditTextId1);
        login_button=(Button)findViewById(R.id.loginButtonId1);
        createAccountButton=(Button)findViewById(R.id.createAccountButton1);

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,CreateAccountActivity.class));
                finish();
            }
        });

        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                firebaseUser=firebaseAuth.getCurrentUser();

                if(firebaseUser!=null){
                    Toast.makeText(MainActivity.this,"Signed In",Toast.LENGTH_LONG);
                    startActivity(new Intent(MainActivity.this,BuyNSellActivity.class));
                    finish();
                }else{

                    Toast.makeText(MainActivity.this,"Not Signed In",Toast.LENGTH_LONG).show();

                }

            }
        };

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if(!TextUtils.isEmpty(email_edit_text.getText().toString())
                      &&!TextUtils.isEmpty(password_edit_text.getText().toString())){
                  String email=email_edit_text.getText().toString();
                  String pwd=password_edit_text.getText().toString();
                  login(email,pwd);

              }  else{


              }
            }
        });

    }

    private void login(String email, String pwd) {
       firebaseAuth.signInWithEmailAndPassword(email,pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
           @Override
           public void onComplete(@NonNull Task<AuthResult> task) {
               if(task.isSuccessful()){
                   Toast.makeText(MainActivity.this,"Signed In",Toast.LENGTH_LONG).show();
                   startActivity(new Intent(MainActivity.this,BuyNSellActivity.class));
                   finish();
               }else{

               }
           }
       });

    }


    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(authStateListener!=null){
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }
}
