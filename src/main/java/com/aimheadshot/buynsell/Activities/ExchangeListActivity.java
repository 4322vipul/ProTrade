package com.aimheadshot.buynsell.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.aimheadshot.buynsell.Data.exchangeRecyclerViewAdapter;
import com.aimheadshot.buynsell.Model.ExchangeItemInformation;
import com.aimheadshot.buynsell.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ExchangeListActivity extends AppCompatActivity {

    private ImageButton exchange_image_button;
    private EditText exchange_price_edit_text;
    private Button exchange_submit_button;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private ListView exchange_list_view;
    private Uri image_uri;
    private RecyclerView recyclerView;
    private exchangeRecyclerViewAdapter exchangeAdapter;
    private List<ExchangeItemInformation> exchangeListItems;
    private static final int GALLERY_CODE=1;

    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_list);

        exchange_image_button=(ImageButton)findViewById(R.id.exchangeItemImageButtonId);
        exchange_price_edit_text=(EditText)findViewById(R.id.exchangeItemPriceId);
        exchange_submit_button=(Button)findViewById(R.id.exchangeSubmitButtonId);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();

        databaseReference=FirebaseDatabase.getInstance().getReference().child("Mitems");

        databaseReference.keepSynced(true);

        exchangeListItems=new ArrayList<>();
        recyclerView=(RecyclerView)findViewById(R.id.exchangeItemRecyclerViewId);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        userId=firebaseUser.getUid();

        exchange_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent=new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_CODE);
            }
        });


        exchange_submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                exchangeOperations();

            }
        });

    }

    private void exchangeOperations() {

        final String exchangeitemprice=exchange_price_edit_text.getText().toString().trim();


        Query query=databaseReference.orderByChild("item_price").equalTo(exchangeitemprice);

       // Log.d("Tag","%%%%Value is%%%%%"+exchangeitemprice);

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ExchangeItemInformation exchangeItemInformation=dataSnapshot.getValue(ExchangeItemInformation.class);


                exchangeListItems.add(exchangeItemInformation);

                Collections.reverse(exchangeListItems);

                exchangeAdapter=new exchangeRecyclerViewAdapter(ExchangeListActivity.this,exchangeListItems);
                recyclerView.setAdapter(exchangeAdapter);
                exchangeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_CODE&& resultCode==RESULT_OK){
            image_uri=data.getData();
            CropImage.activity(image_uri).setAspectRatio(1,1).setGuidelines(CropImageView.Guidelines.ON).start(this);
        }
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            if(resultCode==RESULT_OK){
                image_uri=result.getUri();
                exchange_image_button.setImageURI(image_uri);
            }
            else if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error =result.getError();
            }
        }



    }
}
