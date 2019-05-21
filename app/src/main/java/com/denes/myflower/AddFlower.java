package com.denes.myflower;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;



public class AddFlower extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private StorageReference mStorageReference;
    private FirebaseFirestore mFirebaseFirestore;
    private static final int PICK_IMAGE=1;
    private Toolbar mToolbar;
    private Calendar mCurrentDay;
    int day,month,year;

    String mCicekler,date;
    private EditText Name;
    private ImageView mFlowerImage;
    private Button  mAdd;
    private String currenUserId;



    private Uri imageUri=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_flower);
        mAuth=FirebaseAuth.getInstance();
        currenUserId=mAuth.getCurrentUser().getUid();
        mStorageReference= FirebaseStorage.getInstance().getReference();
        mFirebaseFirestore=FirebaseFirestore.getInstance();
        mAdd=findViewById(R.id.Add);
        Name=findViewById(R.id.editComment);
        mFlowerImage=findViewById(R.id.flower_image);
        mCurrentDay =Calendar.getInstance();
        day=mCurrentDay.get(Calendar.DAY_OF_MONTH);
        month=mCurrentDay.get(Calendar.MONTH);
        year=mCurrentDay.get(Calendar.YEAR);

        mToolbar =findViewById(R.id.toolbar3);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Share");



        mFlowerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(AddFlower.this);

            }
        });

        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final String name=Name.getText().toString();
                if(!TextUtils.isEmpty(name)&&imageUri!=null){
                    final ProgressDialog progressDialog=new ProgressDialog(AddFlower.this);
                    progressDialog.setTitle("Sharing...");
                    progressDialog.show();

                    String randomName= FieldValue.serverTimestamp().toString();
                    StorageReference filePath=mStorageReference.child("Flower_image").child(randomName + ".jpg");
                    filePath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            final String downloadUri = task.getResult().getDownloadUrl().toString();
                            if(task.isSuccessful()){


                                Map<String,Object> postMap=new HashMap<>();
                                postMap.put("image_url",downloadUri);
                                postMap.put("flower_name", name);
                                postMap.put("type",mCicekler);
                                postMap.put("date",date);
                                postMap.put("user_id",currenUserId);
                                postMap.put("timestamp", FieldValue.serverTimestamp());

                                mFirebaseFirestore.collection("Flowers").add(postMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(getApplicationContext(),"Flower was added",Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();

                                            Intent i = new Intent(AddFlower.this, MainActivity.class);
                                            startActivity(i);
                                            finish();


                                        }else{

                                        }
                                    }
                                });
                            }else{
                                Toast.makeText(getApplicationContext(),"There is a problem",Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

                }else{
                    Toast.makeText(getApplicationContext(),"Please fill all of them",Toast.LENGTH_SHORT).show();
                }

            }

        });
    }





    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                imageUri = result.getUri();
                mFlowerImage.setImageURI(imageUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();

            }
        }
    }
}


