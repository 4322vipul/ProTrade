package com.aimheadshot.buynsell.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.aimheadshot.buynsell.MainActivity;
import com.aimheadshot.buynsell.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class BuyNSellActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser firebaseUser;
    private ImageView bigBuyButton,bigSellbutton,bigExchangeButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_nsell);

        firebaseAuth=FirebaseAuth.getInstance();
        bigBuyButton=(ImageView) findViewById(R.id.bigBuyButtonId);
        bigSellbutton=(ImageView) findViewById(R.id.bigSellButtonId);
        bigExchangeButton=(ImageView) findViewById(R.id.bigExchangeButtonId);

        bigSellbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BuyNSellActivity.this,SellingActivity.class));
                finish();
            }
        });
        bigBuyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BuyNSellActivity.this,PostListBuyActivity.class));
                finish();
            }
        });
        bigExchangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BuyNSellActivity.this,ExchangeListActivity.class));
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_signout){

            firebaseAuth.signOut();
            startActivity(new Intent(BuyNSellActivity.this,MainActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
}
