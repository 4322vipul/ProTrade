package com.aimheadshot.buynsell.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.aimheadshot.buynsell.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

public class SellingActivity extends AppCompatActivity {

    private ImageButton item_image_button;
    private EditText item_name_edit_text;
    private EditText item_price_edit_text;
    private EditText item_description_edit_text;
    private EditText seller_contact_num_edit_text;
    private Button submit_button;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private ProgressDialog progressDialog;
    private Uri image_uri;
    private static final int GALLERY_CODE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressDialog=new ProgressDialog(this);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        storageReference= FirebaseStorage.getInstance().getReference();

        databaseReference= FirebaseDatabase.getInstance().getReference().child("Mitems");


        item_image_button=(ImageButton)findViewById(R.id.itemImageButtonId);
        item_name_edit_text=(EditText)findViewById(R.id.itemNameEditTextId);
        item_price_edit_text=(EditText)findViewById(R.id.itemPriceEditTextId);
        submit_button=(Button)findViewById(R.id.submitToPostListButtonId);
        item_description_edit_text=(EditText)findViewById(R.id.itemDescriptionEditTextId);
        seller_contact_num_edit_text=(EditText)findViewById(R.id.sellerPhoneNumberEditTextId);

        item_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent=new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_CODE);
            }
        });


        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPosting();
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
                item_image_button.setImageURI(image_uri);
            }
            else if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error =result.getError();
            }
        }
    }

    private void startPosting() {
        progressDialog.setMessage("Adding to the list...");
        progressDialog.show();
        final String item_name=item_name_edit_text.getText().toString().trim();
        final String item_price=item_price_edit_text.getText().toString().trim();
        final String item_desc=item_description_edit_text.getText().toString().trim();
        final String seller_num=seller_contact_num_edit_text.getText().toString().trim();

        if(!TextUtils.isEmpty(item_name)&&!TextUtils.isEmpty(item_price)&&!TextUtils.isEmpty(item_desc)&&
                image_uri !=null&&!TextUtils.isEmpty(seller_num)){

            final StorageReference filepath=storageReference.child("List_Images").
                    child(image_uri.getLastPathSegment());
            filepath.putFile(image_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Uri downloadurl= taskSnapshot.getDownloadUrl();
                    DatabaseReference newPost=databaseReference.push();

                    Map<String,String> dataToSave=new HashMap<>();
                    dataToSave.put("item_name",item_name);
                    dataToSave.put("item_price",item_price);
                    dataToSave.put("item_desc",item_desc);
                    dataToSave.put("seller_number",seller_num);
                    dataToSave.put("image",downloadurl.toString());

                    newPost.setValue(dataToSave);

                    progressDialog.dismiss();

                    Toast.makeText(SellingActivity.this,"Item Added Successfully",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(SellingActivity.this,BuyNSellActivity.class));
                    finish();
                }
            });
        }else {

            Toast.makeText(getApplicationContext(),"Fill all the fields",Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SellingActivity.this,BuyNSellActivity.class));
        finish();
    }
}
