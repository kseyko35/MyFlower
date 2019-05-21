package com.denes.myflower.Fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.denes.myflower.AddFlower;
import com.denes.myflower.AlarmMainActivity;
import com.denes.myflower.Antoryum;
import com.denes.myflower.Orkides;
import com.denes.myflower.PostFlower;
import com.denes.myflower.PostRecycleAdapter;
import com.denes.myflower.R;

import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyFlowersFragment extends Fragment {

    FloatingActionMenu materialDesignFAM;
    com.github.clans.fab.FloatingActionButton share, reminder;
    private String [] cicekler={"Orkide", "Antoryum","Karanfil","Zakkum","Hatmi Çiçeği","Menekşe","Bonsai","Spatifilyum","Siklamen","Ponsetya"};
    private ArrayAdapter<String > dataCicekler;
    private FirebaseAuth mAuth;
    private StorageReference mStorageReference;
    private FirebaseFirestore mFirebaseFirestore;
    private String currenUserId,mCicekler;
    private RecyclerView flower_list_view;
    private List<PostFlower> flower_list;
    private PostRecycleAdapter postRecycleAdapter;
    private DocumentSnapshot lastVisible;
    private Boolean isFirstPageFirstLoad = true;
    private Spinner mSpinner;
    private RadioButton mSuRBut,mToprakRBut,mGubreRBut;
    private Button mAddAlarm;
    public MyFlowersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_my_flowers, container, false);


        flower_list=new ArrayList<>();

        mAuth=FirebaseAuth.getInstance();
        currenUserId=mAuth.getCurrentUser().getUid();
        mStorageReference= FirebaseStorage.getInstance().getReference();
        flower_list_view=view.findViewById(R.id.flowerListView);
        materialDesignFAM = view.findViewById(R.id.floating_action_menu);
        share = view.findViewById(R.id.share);
        reminder = view.findViewById(R.id.reminder);

        postRecycleAdapter=new PostRecycleAdapter(flower_list);

        flower_list_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        flower_list_view.setAdapter(postRecycleAdapter);





        if(mAuth.getCurrentUser()!=null) {
            mFirebaseFirestore = FirebaseFirestore.getInstance();

            flower_list_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    Boolean reachedBottom = !recyclerView.canScrollVertically(1);

                    if (reachedBottom) {
                        LoadMoreFlower();
                    }
                }
            });

            Query firstQuery = mFirebaseFirestore
                    .collection("Flowers")
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .limit(5);
            firstQuery.addSnapshotListener(getActivity(),new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                    if(!documentSnapshots.isEmpty()) {
                        if (isFirstPageFirstLoad) {

                            lastVisible = documentSnapshots.getDocuments().get(documentSnapshots.size() - 1);
                            flower_list.clear();
                        }

                        for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                            if (doc.getType() == DocumentChange.Type.ADDED) {
                                String flowerPostId = doc.getDocument().getId();
                                PostFlower postFlower = doc.getDocument().toObject(PostFlower.class).withId(flowerPostId);

                                if (isFirstPageFirstLoad) {
                                    flower_list.add(postFlower);
                                } else {
                                    flower_list.add(0, postFlower);
                                }
                                postRecycleAdapter.notifyDataSetChanged();

                            }
                        }
                        isFirstPageFirstLoad = false;
                    }
                }
            });


            share.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent i=new Intent(MyFlowersFragment.this.getActivity(),AddFlower.class);
                    startActivity(i);
                }
            });
            reminder.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    final AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                    View mView=getLayoutInflater().inflate(R.layout.alert_dialog,null);
                    mSpinner=mView.findViewById(R.id.spinner);
                    mSuRBut=mView.findViewById(R.id.suRButton);
                    mToprakRBut=mView.findViewById(R.id.toprakRButton);
                    mGubreRBut=mView.findViewById(R.id.gubreRButton);
                    mAddAlarm=mView.findViewById(R.id.AddAlarm);
                    dataCicekler = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,cicekler);
                    dataCicekler.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mSpinner.setAdapter(dataCicekler);
                    mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view,
                                                   int position, long id) {
                            mCicekler=cicekler[position];
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });


                    mAddAlarm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder builder1=new AlertDialog.Builder(getActivity());
                            if(mSuRBut.isChecked()||mGubreRBut.isChecked()||mToprakRBut.isChecked()){
                                switch (mCicekler){
                                    case "Orkide":
                                        if(mSuRBut.isChecked()){
                                            builder1.setTitle("Alert !!")
                                                    .setMessage("   *Orkide çiçeği, yazın haftada iki kez, saksı üstden sulanmalı\n   *Sulama esnasında kullanılan su kabı ölçeği hep aynı olmalı ve çiçeğe dökülen su saksıın dibinde en fazla akşam sulamış isek, sabahleyin tabakda biriken su dökülmelidir. Böylelikle bitkinin toprağında koku ve haşerelerin oluşmasınada engel olunacaktır.\n   *Sulamada temel kural bitkinin toprağı kurudukça su verilmeli, toprağının kuru yada sulu olduğunu anlamamız için, toprağına parmağımıza batırarak kuru veya ıslak olduğunu anlayabiliriz.\n   *Islak ise bitkimiz topraktaki suyu henüz bitirememiş olduğundan sulamamızı bir sonraki sulama gününe bırakmalıyız.")
                                                    .setIcon(R.drawable.ic_notification_alert)
                                                    .setCancelable(false);
                                        }
                                        else if(mGubreRBut.isChecked()){
                                            builder1.setTitle("Alert !!")
                                                    .setMessage("   *Orkidemize vitamin kullanırken dikkat edilmesi gereken husus, çiçeğimiz yaprakları ve çiçekleri sağlıklı ise bu dönem içerisinde vitamin kullanmak zorunda değiliz.\n   *Çiçeğimizin çiçekleri solmuş ve budamasını yapmış isek bu dönemde bitkimizin hem çiçek açması hemde gelişmesi için mağazalarda satılan çiçek vitaminlerini kullanma koşullarına uyarak bitkimize besin desteği verebiliriz.\n   *rnek: Genelde besinler 1LT suya bir kapak ölçeğinde vitamin dökülür, iyice karıştırılan suyla vitamin bulunduğu ortamdaki çiçeklere sulamadaki standart ölçeğimizle ilaçlı suyu bitkilerimize onbeş günde bir yada ayda bir uygulayabiliriz. ")
                                                    .setIcon(R.drawable.ic_notification_alert)
                                                    .setCancelable(false);
                                        }
                                        else if(mToprakRBut.isChecked()){
                                            builder1.setTitle(" Alert !!").setMessage("   *Sulanmalar esnasında doğal olarak toprakta azalmalar olacağından, azaldığı dönemlerde saksıya üst taraftan toprak ilave etmek orkidemizin daha sağlıklı yetişmesine katkı sağlayacaktır.\n   *Orkide toprağı diğer bitkilere göre ağaç kabuklarından oluşmaktadır. Bu tarz toprak köklerinin ışık almasını sağlar ve orkide çiçeği köklerinden ışık alarak beslenir.\n   *Toprak değiştirirken dikkat edilmesi gereken husus, değiştirmek istediğiniz çiçeğin saksısının en fazla bir numara büyüğüne dikilmelidir.")
                                                    .setIcon(R.drawable.ic_notification_alert)
                                                    .setCancelable(false);
                                        }
                                        builder1.setNegativeButton("More Details !", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Intent is = new Intent(getActivity(), Orkides.class);
                                                startActivity(is);

                                                dialogInterface.dismiss();
                                            }
                                        });
                                    case "Antoryum":
                                        if(mSuRBut.isChecked()){
                                            builder1.setTitle(" Alert !!").setMessage("   Antoryum çiçeği, yazın haftada iki ile üç kere, saksı üstten sulanmalıdır.\n   *Sulama tekniğinde Pazartesi ve Perşembe günleri tercih edilebilir.\n   *Sulama esnasında kullanılan su kabı ölçeği hep aynı olmalı ve çiçeğe dökülen su saksının dibinde en fazla akşam sulamış isek, sabahleyin tabakda biriken su dökülmelidir. Böylelikle bitkinin toprağında koku ve haşerelerin oluşmasında engel olunacaktır.\n   *Sulamada temel kural bitkinin toprağa kurudukça su verilmeli, toprağının kuru yada sulu olduğunu anlamamız için, toprağına parmağımıza batırarak kuru veya ıslak olduğunu anlayabiliriz. Islak ise bitkimiz topraktaki suyu henüz bitirememiş olduğundan sulamamızı bir sonraki sulama gününe bırakmalıyız.")
                                                    .setIcon(R.drawable.ic_notification_alert)
                                                    .setCancelable(false);
                                        }
                                        else if(mGubreRBut.isChecked()){
                                            builder1.setTitle(" Alert !!").setMessage("   *Antoryum çiçeğimize, vitamin kullanırken dikkat edilmesi gereken husus, çiçeğimiz yaprakları ve çiçekleri sağlıklı ise bu dönem içerisinde vitamin kullanmak zorunda değiliğiz.\n   *Çiçeğimizin çiçekleri solmuş ve budamasını yapmış isek bu dönemde bitkimizin hem çiçek açması hemde gelişmesi için mağazalarda satılan çiçek vitaminlerini kullanma koşullarına uyarak bitkimize besin desteği verebiliriz.\n   *Örnek: Genelde besinler 1LT suya bir kapak ölçeğinde vitamin dökülür, iyice karıştırılan suyla vitamin bulunduğu ortamdaki çiçeklere sulamadaki standart ölçeğimizle ilaçlı suyu bitkilerimize onbeş günde bir yada ayda bir uygulayabiliriz.")
                                                    .setIcon(R.drawable.ic_notification_alert)
                                                    .setCancelable(false);
                                        }
                                        else if(mToprakRBut.isChecked()){
                                            builder1.setTitle(" Alert !!").setMessage("   *Antoryum çiçeğinin toprak değişimi Nisan-Mayıs aylarından yapılmalıdır.\n   *Toprak değiştirirken dikkat edilmesi gereken husus, değiştirmek istediğiniz çiçeğin saksısının en fazla bir numara büyüğüne dikilmelidir.")
                                                    .setIcon(R.drawable.ic_notification_alert)
                                                    .setCancelable(false);
                                        }

                                        builder1.setPositiveButton("OK!", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Intent is = new Intent(getActivity(), AlarmMainActivity.class);
                                                startActivity(is);

                                                dialogInterface.dismiss();
                                            }
                                        });
                                        builder1.setNegativeButton("More Details !", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Intent is = new Intent(getActivity(), Antoryum.class);
                                                startActivity(is);

                                                dialogInterface.dismiss();
                                            }
                                        });
                                        builder1.create().show();

                                }}
                            else{
                                Intent is2 = new Intent(getActivity(), AlarmMainActivity.class);
                                startActivity(is2);

                            }
                        }
                    });
                    builder.setView(mView).show();
                }
            });
        }
        return view;
    }
    public void LoadMoreFlower(){
        if(mAuth.getCurrentUser()!=null) {
            Query nextQuery = mFirebaseFirestore.collection("Flowers")
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .startAfter(lastVisible)
                    .limit(5);

            nextQuery.addSnapshotListener(getActivity(),new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                    if (!documentSnapshots.isEmpty()) {
                        lastVisible = documentSnapshots.getDocuments().get(documentSnapshots.size() - 1);
                        for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                            if (doc.getType() == DocumentChange.Type.ADDED) {
                                String flowerPostId=doc.getDocument().getId();
                                PostFlower postFlower = doc.getDocument().toObject(PostFlower.class).withId(flowerPostId);
                                flower_list.add(postFlower);
                                postRecycleAdapter.notifyDataSetChanged();

                            }
                        }
                    }
                }
            });
        }

    }

}



