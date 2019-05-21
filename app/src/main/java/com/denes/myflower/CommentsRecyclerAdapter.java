package com.denes.myflower;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentsRecyclerAdapter extends RecyclerView.Adapter<CommentsRecyclerAdapter.ViewHolder> {

    public List<CommentsActivity> commentsList;
    public Context context;
    private FirebaseFirestore mFirebaseFirestore;
    private FirebaseAuth mFirebaseAuth;



    public CommentsRecyclerAdapter(List<CommentsActivity> commentsList){

        this.commentsList = commentsList;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_list_item, parent, false);
        context = parent.getContext();
        mFirebaseFirestore=FirebaseFirestore.getInstance();
        mFirebaseAuth=FirebaseAuth.getInstance();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CommentsRecyclerAdapter.ViewHolder holder, int position) {

        holder.setIsRecyclable(false);

        String commentMessage = commentsList.get(position).getMessage();
        holder.setComment_message(commentMessage);


        String user_id=commentsList.get(position).getUser_id();

        try{
            long millisecond = commentsList.get(position).getTimestamp().getTime();
            String dateString = DateFormat.format("dd/MM/yyyy", new Date(millisecond)).toString();
            holder.setTime(dateString);
        }
        catch (Exception e){

        }
        mFirebaseFirestore.collection("users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    String userName=task.getResult().getString("name");
                    String userImage=task.getResult().getString("image");

                    holder.setUserData(userName,userImage);
                }
                else{
                    //firebase exceptions
                }
            }
        });





    }


    @Override
    public int getItemCount() {

        if(commentsList != null) {

            return commentsList.size();

        } else {

            return 0;

        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;
        private CircleImageView postUserImage;
        private TextView postUserName;
        private TextView comment_message;
        private TextView commentDate;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void  setTime(String date){
            commentDate=mView.findViewById(R.id.CommentDate);
            commentDate.setText(date);

        }
        public void setComment_message(String message){

            comment_message = mView.findViewById(R.id.comment_message);
            comment_message.setText(message);

        }
        public void setUserData(String name,String image){
            postUserImage=mView.findViewById(R.id.comment_image);
            postUserName=mView.findViewById(R.id.comment_username);

            postUserName.setText(name);

            RequestOptions placeholderOption =new RequestOptions();
            placeholderOption.placeholder(R.drawable.boy_img);
            Glide.with(context).applyDefaultRequestOptions(placeholderOption).load(image).into(postUserImage);
        }



    }

}