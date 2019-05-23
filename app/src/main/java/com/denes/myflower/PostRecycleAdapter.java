package com.denes.myflower;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;


import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by seyfi on 17.3.2018.
 */

public class PostRecycleAdapter extends RecyclerView.Adapter<PostRecycleAdapter.ViewHolder> {

    public List<PostFlower> flowers_list;
    public Context context;
    private FirebaseFirestore mFirebaseFirestore;
    private FirebaseAuth mFirebaseAuth;



    public PostRecycleAdapter(List<PostFlower> flower_list){

        this.flowers_list=flower_list;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.flower_list_item,parent,false);
        context =parent.getContext();
        mFirebaseFirestore=FirebaseFirestore.getInstance();
        mFirebaseAuth=FirebaseAuth.getInstance();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.setIsRecyclable(false);
        final String flowerPostId=flowers_list.get(position).flower_PostId;
        final String currentUserID=mFirebaseAuth.getCurrentUser().getUid();

        String name_data=flowers_list.get(position).getFlower_name();
        holder.setNameText(name_data);

        String image_url=flowers_list.get(position).getImage_url();
        holder.setPostImage(image_url);


        String user_id=flowers_list.get(position).getUser_id();
        if(user_id.equals(currentUserID)){
            holder.mDeleteLinearLayout.setEnabled(true);
            holder.mDeleteLinearLayout.setVisibility(View.VISIBLE);
        }
        //User Data will be retrieved here...

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
        try{
            long millisecond = flowers_list.get(position).getTimestamp().getTime();
            String dateString = DateFormat.format("dd/MM/yyyy", new Date(millisecond)).toString();
            holder.setTime(dateString);
        }
        catch (Exception e){
            Toast.makeText(context, "Exception : " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        //get like
        mFirebaseFirestore.collection("Flowers/" + flowerPostId + "/Likes").document(currentUserID).addSnapshotListener(((MainActivity) context),new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if(documentSnapshot.exists()) {
                    holder.mLikeImage.setImageDrawable(context.getDrawable(R.mipmap.action_like_accent));
                }
                else{
                    holder.mLikeImage.setImageDrawable(context.getDrawable(R.mipmap.action_like_gray));
                }
            }
        });
        //get like counter
        mFirebaseFirestore.collection("Flowers/" + flowerPostId + "/Likes").addSnapshotListener(((MainActivity) context),new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                if(!queryDocumentSnapshots.isEmpty()){
                    int count=queryDocumentSnapshots.size();
                    holder.setLikesCounter(count);
                }
                else{
                    holder.setLikesCounter(0);
                }
            }
        });
        mFirebaseFirestore.collection("Flowers/" + flowerPostId + "/Comments").addSnapshotListener(((MainActivity) context),new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                if(!queryDocumentSnapshots.isEmpty()){
                    int count=queryDocumentSnapshots.size();
                    holder.mCommentImage.setImageDrawable(context.getDrawable(R.mipmap.ic_send_icon_yellow));
                    holder.setCommentsCounter(count);
                }
                else{
                    holder.mCommentImage.setImageDrawable(context.getDrawable(R.mipmap.comment_icon));
                    holder.setCommentsCounter(0);
                }
            }
        });


        //likeFeatures
        holder.mLikeLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mFirebaseFirestore.collection("Flowers/" + flowerPostId + "/Likes").document(currentUserID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(!task.getResult().exists()){
                            Map<String ,Object> likesMap=new HashMap<>();
                            likesMap.put("timestamp", FieldValue.serverTimestamp());

                            mFirebaseFirestore.collection("Flowers/" + flowerPostId + "/Likes").document(currentUserID).set(likesMap);
                        }
                        else{
                            mFirebaseFirestore.collection("Flowers/" + flowerPostId + "/Likes").document(currentUserID).delete();

                        }
                    }
                });

            }
        });

        holder.mCommentLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent commentIntent = new Intent(context, Comments.class);
                commentIntent.putExtra("flower_post_id", flowerPostId);
                context.startActivity(commentIntent);
            }
        });

        holder.mDeleteLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFirebaseFirestore.collection("Flowers").document(flowerPostId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        flowers_list.remove(position);
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {

        return flowers_list.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private View mView;
        private TextView nameView;
        private ImageView postImageView,mCommentImage,mLikeImage;

        private TextView postDate;
        private TextView postUserName;
        private CircleImageView postUserImage;
        private TextView mLikeCounter,mTextDelete;
        private TextView mCommentsCounter;
        private LinearLayout mCommentLinearLayout,mLikeLinearLayout,mDeleteLinearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            mView=itemView;

            mCommentLinearLayout=mView.findViewById(R.id.commentLinearLayout);
            mDeleteLinearLayout=mView.findViewById(R.id.deleteLinearLayout);
            mLikeLinearLayout=mView.findViewById(R.id.likeLinearLayout);
            mTextDelete=mView.findViewById(R.id.text_delete);
            mLikeImage=mView.findViewById(R.id.post_like);
            mCommentImage=mView.findViewById(R.id.comment_img);

        }
        public void setNameText (String nameText){
            nameView=mView.findViewById(R.id.post_flower_name);
            nameView.setText(nameText);
        }

        public void setPostImage(String downloadUri){

            postImageView=mView.findViewById(R.id.post_flower_image);
            RequestOptions requestOptions=new RequestOptions();
            requestOptions.placeholder(R.drawable.placeholder);
            Glide.with(context).applyDefaultRequestOptions(requestOptions).load(downloadUri)
                    //.thumbnail(Glide.with(context).load(thumbUri))
                    .into(postImageView);
        }
        public void setTime(String date){
            postDate=mView.findViewById(R.id.post_date);
            postDate.setText(date);
        }
        public void setUserData(String name,String image){
            postUserImage=mView.findViewById(R.id.post_user_image);
            postUserName=mView.findViewById(R.id.post_user_name);

            postUserName.setText(name);

            RequestOptions placeholderOption =new RequestOptions();
            placeholderOption.placeholder(R.drawable.boy_img);
            Glide.with(context).applyDefaultRequestOptions(placeholderOption).load(image).into(postUserImage);
        }
        public void setLikesCounter(int count){

            mLikeCounter=mView.findViewById(R.id.post_like_counter);
            mLikeCounter.setText(count + " Likes");

        }
        public void setCommentsCounter(int count){

            mCommentsCounter=mView.findViewById(R.id.blog_comment_count);
            mCommentsCounter.setText(count + " Comments");

        }

    }


}
