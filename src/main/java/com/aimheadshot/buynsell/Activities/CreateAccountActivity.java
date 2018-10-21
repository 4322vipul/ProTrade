package com.aimheadshot.buynsell.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.aimheadshot.buynsell.MainActivity;
import com.aimheadshot.buynsell.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class CreateAccountActivity extends AppCompatActivity {

    private ImageButton profile_pic_button;
    private EditText email_id_edit_text;
    private EditText pwd_edit_text;
    private EditText first_name_text;
    private EditText last_name_text;
    private Button create_account_btn;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private Uri resultUri;
    private final static int GALLERY_CODE=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child("Users");
        firebaseAuth=FirebaseAuth.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference().child("Profile_pics");
        progressDialog=new ProgressDialog(this);

        profile_pic_button=(ImageButton)findViewById(R.id.profilePicId);
        email_id_edit_text=(EditText)findViewById(R.id.emailIdEditTextId);
        pwd_edit_text=(EditText)findViewById(R.id.passwordEditTextId);
        first_name_text=(EditText)findViewById(R.id.firstNameEditTextId);
        last_name_text=(EditText)findViewById(R.id.lastNameEditTextId);
        create_account_btn=(Button)findViewById(R.id.createAccountButtonId);

       // profile_pic_button.setImageDrawable(getDrawable(R.drawable.profilepic));


        profile_pic_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_CODE);
            }
        });
        create_account_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewAccount();
            }
        });


    }

    private void createNewAccount() {

        final String name = first_name_text.getText().toString().trim();
        final String lname = last_name_text.getText().toString().trim();
        String em = email_id_edit_text.getText().toString().trim();
        String pwd = pwd_edit_text.getText().toString().trim();

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(lname)
                && !TextUtils.isEmpty(em) && !TextUtils.isEmpty(pwd)&& resultUri!=null) {

            progressDialog.setMessage("Creating Account...");
            progressDialog.show();

            firebaseAuth.createUserWithEmailAndPassword(em, pwd)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            if (authResult != null) {

                                StorageReference imagePath = storageReference.child("Profile_pics")
                                        .child(resultUri.getLastPathSegment());


                             //   !!!! Fix the profile picture


                                imagePath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                        String userid =firebaseAuth.getCurrentUser().getUid();

                                        DatabaseReference currenUserDb = databaseReference.child(userid);
                                        currenUserDb.child("firstname").setValue(name);
                                        currenUserDb.child("lastname").setValue(lname);
                                        currenUserDb.child("image").setValue(resultUri.toString());


                                        progressDialog.dismiss();

                                        //send users to postList
                                        Intent intent = new Intent(CreateAccountActivity.this, BuyNSellActivity.class );
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();


                                    }
                                });

                            }

                        }
                    });


        }else{

            Toast.makeText(getApplicationContext(),"Select any picture!!",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode== GALLERY_CODE && resultCode == RESULT_OK){
            Uri imageUri=data.getData();

            CropImage.activity(imageUri).setAspectRatio(1,1).setGuidelines(CropImageView.Guidelines.ON).start(this);
        }

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            if(resultCode==RESULT_OK){
                resultUri=result.getUri();
                profile_pic_button.setImageURI(resultUri);
            }else if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error =result.getError();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(CreateAccountActivity.this, MainActivity.class));
        finish();
    }
}
