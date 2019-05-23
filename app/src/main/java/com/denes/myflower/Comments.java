package com.denes.myflower;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.denes.myflower.Fragment.MyFlowersFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Comments extends AppCompatActivity {


    private EditText comment_field;
    private ImageView comment_post_btn;

    private Toolbar mToolbar;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private RecyclerView comment_list;
    private CommentsRecyclerAdapter commentsRecyclerAdapter;
    private List<CommentsActivity> commentsList;
    private Boolean firstList = true;
    private String flower_post_id;
    private String current_user_id;
    DocumentSnapshot lastVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        current_user_id = firebaseAuth.getCurrentUser().getUid();
        flower_post_id = getIntent().getStringExtra("flower_post_id");

        comment_field = findViewById(R.id.comment_field);
        comment_post_btn = findViewById(R.id.comment_post_btn);

        comment_list = findViewById(R.id.comment_list);
        //RecyclerView Firebase List
        commentsList = new ArrayList<>();
        commentsRecyclerAdapter = new CommentsRecyclerAdapter(commentsList);
        comment_list.setHasFixedSize(true);
        comment_list.setLayoutManager(new LinearLayoutManager(this));
        comment_list.setAdapter(commentsRecyclerAdapter);

        mToolbar = findViewById(R.id.toolbarcomment);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Comments:");


        Query query = firebaseFirestore
                .collection("Flowers/" + flower_post_id + "/Comments").orderBy("timestamp", Query.Direction.DESCENDING);

        query.addSnapshotListener(Comments.this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (!documentSnapshots.isEmpty()) {
                    if (firstList) {
                        lastVisible = documentSnapshots.getDocuments().get(documentSnapshots.size() - 1);
                        commentsList.clear();
                    }
                    for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                        if (doc.getType() == DocumentChange.Type.ADDED) {
                            CommentsActivity comments = doc.getDocument().toObject(CommentsActivity.class);
                            if(comments.getTimestamp()==null){
                                comments.setTimestamp(null);
                            }else{
                                if (firstList) {
                                    commentsList.add(comments);
                                } else {
                                    commentsList.add(0, comments);
                                }

                                commentsRecyclerAdapter.notifyDataSetChanged();
                            }



                        }

                    }
                    firstList = false;
                }
            }
        });


        comment_post_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String comment_message = comment_field.getText().toString();


                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String currentDateandTime = sdf.format(new Date());

                Map<String, Object> commentsMap = new HashMap<>();
                commentsMap.put("message", comment_message);
                commentsMap.put("user_id", current_user_id);
                commentsMap.put("timestamp", currentDateandTime);//Sorun burada


                firebaseFirestore.collection("Flowers/" + flower_post_id + "/Comments").add(commentsMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {

                        if (!task.isSuccessful()) {

                            Toast.makeText(Comments.this, "Error Posting Comment : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        } else {

                            comment_field.setText("");

                        }
                    }
                });
            }
        });

        comment_field.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().equals(""))
                    comment_post_btn.setImageResource(R.mipmap.send_icon);
                else comment_post_btn.setImageResource(R.mipmap.ic_send_icon_red);


            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
