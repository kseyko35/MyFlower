package com.denes.myflower.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.denes.myflower.About;
import com.denes.myflower.CustomItemClickListener;
import com.denes.myflower.LoginActivity;
import com.denes.myflower.Portuca;
import com.denes.myflower.Vanilya;
import com.denes.myflower.Plants;
import com.denes.myflower.R;
import com.denes.myflower.SetupActivity;
import com.denes.myflower.PlantsAdapter;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlantsFragment extends Fragment {

    private RecyclerView recycler_view;
    private List<Plants> plants_list;
    FloatingActionMenu materialDesignFAM;
    FloatingActionButton floatingActionButton1, floatingActionButton2, floatingActionButton3;
    private FirebaseAuth mAuth;

    public PlantsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_plants, container, false);
        recycler_view = view.findViewById(R.id.recycler_view2);
        materialDesignFAM = view.findViewById(R.id.material_design_android_floating_action_menu);
        floatingActionButton1 = view.findViewById(R.id.material_design_floating_action_menu_item1);
        floatingActionButton2 = view.findViewById(R.id.material_design_floating_action_menu_item2);
        floatingActionButton3 = view.findViewById(R.id.material_design_floating_action_menu_item3);
        mAuth=FirebaseAuth.getInstance();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());

        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);

        recycler_view.setLayoutManager(layoutManager);

        plants_list = new ArrayList<Plants>();
        plants_list.add(new Plants("Vanilya", "İsmi Latince kökenli Vanilla planifolia’dan gelen vanilya, genellikle tropikal ülkelerin bir ürünü olup hoş kokusu ve...", R.drawable.vanilya5));
        plants_list.add(new Plants("Semizotu", "Halk arasında semizebe ismi ile bilinen semizotu, hemen her yerde rahatlıkla görebileceğiniz bitkilerden...", R.drawable.semizotu1));
        plants_list.add(new Plants("Isırgan Otu", "Bilimsel alanda Urtica membranacea olarak bilinen ısırgan otu, Isırgangiller familyasından bir bitkidir. Mayıs ve Ağustos ayları...", R.drawable.isirgan));
        plants_list.add(new Plants("Cibes Otu", "Doğanın bize sunduğu nimetler saymakla bitmiyor. Özellikle şifalı otlar, yüzyıllardır hastalıkların tedavisinde, yaraların...", R.drawable.cibes));
        plants_list.add(new Plants("Hatmi Çiçeği", "Bilimse aldı Althaea officinalis olan hatmi çiçeği; pembe ve mor renklerde çiçeklere sahip olan, şifalı bitkiler...", R.drawable.hatmi));
        plants_list.add(new Plants("Çoban Çantasi", "Çoban çantası Latince adı Capsella bursa pastoris olarak bilinen bir bitkidir. Anadolu’da cıngıldak otu, çoban torbası, kuskus otu olarak da bilinen...", R.drawable.coban));

        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mAuth.signOut();
                Intent i=new Intent(getActivity(),LoginActivity.class);
                startActivity(i);
            }
        });
        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i=new Intent(getActivity(),SetupActivity.class);
                startActivity(i);

            }
        });
        floatingActionButton3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i=new Intent(getActivity(),About.class);
                startActivity(i);

            }
        });

        PlantsAdapter adapter_items1 = new PlantsAdapter(plants_list, new CustomItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Log.d("position", "Tıklanan Pozisyon:" + position);
                Plants plants = plants_list.get(position);
                if(position==0){
                    Intent i=new Intent(PlantsFragment.this.getActivity(),Vanilya.class);
                    startActivity(i);}
                else if (position==1){
                    Intent i=new Intent(PlantsFragment.this.getActivity(),Portuca.class);
                    startActivity(i);
                }

            }
        });
        recycler_view.setHasFixedSize(true);

        recycler_view.setAdapter(adapter_items1);

        recycler_view.setItemAnimator(new DefaultItemAnimator());


        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

}
