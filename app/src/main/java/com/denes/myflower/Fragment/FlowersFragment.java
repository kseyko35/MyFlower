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
import com.denes.myflower.Antoryum;
import com.denes.myflower.Bonsai;
import com.denes.myflower.CustomItemClickListener;
import com.denes.myflower.Flowers;
import com.denes.myflower.Portuca;
import com.denes.myflower.LoginActivity;
import com.denes.myflower.Orkides;
import com.denes.myflower.R;
import com.denes.myflower.SetupActivity;
import com.denes.myflower.FlowersAdapter;

import java.util.ArrayList;
import java.util.List;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 */
public class FlowersFragment extends Fragment {
    private RecyclerView recycler_view;
    private List<Flowers> flowers_list;
    FloatingActionMenu materialDesignFAM;
    FloatingActionButton floatingActionButton1, floatingActionButton2, floatingActionButton3;
    private FirebaseAuth mAuth;

    public FlowersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_flowers_fragment, container, false);

        mAuth=FirebaseAuth.getInstance();
        recycler_view = view.findViewById(R.id.recycler_view);
        materialDesignFAM = view.findViewById(R.id.material_design_android_floating_action_menu);
        floatingActionButton1 = view.findViewById(R.id.material_design_floating_action_menu_item1);
        floatingActionButton2 = view.findViewById(R.id.material_design_floating_action_menu_item2);
        floatingActionButton3 = view.findViewById(R.id.material_design_floating_action_menu_item3);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());

        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);

        recycler_view.setLayoutManager(layoutManager);

        flowers_list = new ArrayList<Flowers>();

        flowers_list.add(new Flowers("Orkide", "   Salepgiller familyasına ait çiçekli bir bitkidir. Karacıl bir çiçek türü olup, küçük yaprakları ... ", R.drawable.ork));
        flowers_list.add(new Flowers("Antoryum", "   Diğer adı ile flamingo çiçeği yetiştirilmesi oldukça kolay, uzun ömürlü ve ... ", R.drawable.antoryum4));
        flowers_list.add(new Flowers("Bonsai", "   Saksı çiçeği ev, ofis ve işyerlerimizi renklendiren ve bulunduğu ortama sadelik katan ihtişamlı gövde yapısı ile yaşam alanlarınızın vazgeçilmezi...", R.drawable.bonsai));
        flowers_list.add(new Flowers("Karanfil", "Çok sayıda türü ve bu türlerin altında 80’den fazla çeşidi bulunan karanfil çiçeğinin bilinenleri belirli sayıdadır. Yetişme alanlarına...", R.drawable.karanfi));
        flowers_list.add(new Flowers("Zakkum", "Zakkum bitkisi, genel özellikleri ile şık bir bitki özelliğinde olsa da, zehirli olması diğer süs bitkilerinden ayrılmasına neden...", R.drawable.zakkum));
        flowers_list.add(new Flowers("Hatmi Çiçeği", "Bilimsel adı Althaea officinalis olan hatmi çiçeği; pembe ve mor renklerde çiçeklere sahip olan...", R.drawable.hatmi));
        flowers_list.add(new Flowers("Menekşe", "Menekşegiller familyasından olan Viola cinsini meydana getiren bitki türlerinin ortak ismine...", R.drawable.menekse));
        flowers_list.add(new Flowers("Spatifilyum", "Spatifilyum saksı çiçeği ev, ofis ve işyerlerimizi renklendiren ve bulunduğu ortama saflık ve sadelik katan beyaz ve güzel çiçekleriyle spatifilyum en sevdiğiniz saksı bitkilerinin arasına...", R.drawable.spatifilyum));
        flowers_list.add(new Flowers("Siklamen", "Siklamen saksı çiçeği, ev, ofis, işyerleri gibi yaşam alanlarımızı renklendiren pembe, fuşya, beyaz gibi renkleri bulunan bitkilerdir. Doğada en çok pembe, fuşya, pembe-beyaz, beyaz ve kırmızı renk çeşitleri...", R.drawable.siklamen));
        flowers_list.add(new Flowers("Ponsetya", "Ponsetya çiçeği diğer adıyla 'Atatürk Çiçeği' adını Atatürk'ün çabalarıyla ülkemizde yetiştirilmeye başlandığı için almıştır. Ponsetya saksı çiçeği ev, ofis ve iş yerleri gibi yaşam alanlarını renklendiren kırmızı..", R.drawable.ponsetya));



        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mAuth.signOut();
                Intent i=new Intent(FlowersFragment.this.getActivity(),LoginActivity.class);
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


        FlowersAdapter adapter_items = new FlowersAdapter(flowers_list, new CustomItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Log.d("position", "Tıklanan Pozisyon:" + position);
                Flowers flowers = flowers_list.get(position);
                if(position==0){
                    Intent i=new Intent(FlowersFragment.this.getActivity(),Orkides.class);
                    startActivity(i);}
                else if (position==1){
                    Intent i=new Intent(FlowersFragment.this.getActivity(),Antoryum.class);
                    startActivity(i);
                }
                else if (position==2){
                    Intent i=new Intent(FlowersFragment.this.getActivity(),Bonsai.class);
                    startActivity(i);
                }
            }
        });
        recycler_view.setHasFixedSize(true);

        recycler_view.setAdapter(adapter_items);

        recycler_view.setItemAnimator(new DefaultItemAnimator());
        return view;
    }

}
