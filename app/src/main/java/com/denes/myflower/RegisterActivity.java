package com.denes.myflower;

import android.content.Intent;
import android.net.Uri;
import android.os.storage.StorageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.test.mock.MockApplication;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {

    private CircleImageView mImageButton;
    private EditText mEmailField;
    private EditText mNameField;
    private EditText mPasswordField;
    private Button mRegBtn;
    private Button mBackBtn;
    private static final int PICK_IMAGE=1;
    private Uri imageUri;
    private StorageReference mStorage;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFireStore;
    private ProgressBar  mProgBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mImageButton=findViewById(R.id.imageViewLogo);
        mEmailField=findViewById(R.id.register_email);
        mNameField=findViewById(R.id.register_name);
        mPasswordField=findViewById(R.id.register_Password);
        mRegBtn=findViewById(R.id.register_button);
        mBackBtn=findViewById(R.id.back_to_register);
        mProgBar=findViewById(R.id.RegisterProgressBar);
        imageUri=null;
        mStorage= FirebaseStorage.getInstance().getReference().child("images");
        mAuth= FirebaseAuth.getInstance();
        mFireStore=FirebaseFirestore.getInstance();


        mRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(imageUri!=null){
                    mProgBar.setVisibility(View.VISIBLE);


                final String name=mNameField.getText().toString();
                String email=mEmailField.getText().toString();
                String password=mPasswordField.getText().toString();

                if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
                    mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull final Task<AuthResult> task) {

                            if(task.isSuccessful()){

                                final String user_id=mAuth.getCurrentUser().getUid();
                                StorageReference user_profile=mStorage.child(user_id + " .jpg");

                                user_profile.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> uploadTask) {
                                        if (uploadTask.isSuccessful()) {
                                            String download_url = uploadTask.getResult().getDownloadUrl().toString();

                                            Map<String, Object> userMap = new HashMap<>();
                                            userMap.put("name", name);
                                            userMap.put("image", download_url);

                                            mFireStore.collection("users").document(user_id).set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    mProgBar.setVisibility(View.INVISIBLE);
                                                    sendToMain();
                                                }
                                            });

                                        } else {
                                            Toast.makeText(RegisterActivity.this, "Error : " + uploadTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            mProgBar.setVisibility(View.INVISIBLE);
                                        }
                                    }

                                });
                            }
                            else
                            {
                                Toast.makeText(RegisterActivity.this,"Error : " +task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                mProgBar.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                }
            }
            else{
                    Toast.makeText(RegisterActivity.this, "Lütfen Resim seçiniz ", Toast.LENGTH_SHORT).show();
                }
            }
        });



        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i,"select Picture"),PICK_IMAGE);
            }
        });


    }

    private void sendToMain() {
        Intent mainintent=new Intent(RegisterActivity.this,MainActivity.class);
        startActivity(mainintent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE){
            imageUri =data.getData();
            mImageButton.setImageURI(imageUri);
        }
    }
}
