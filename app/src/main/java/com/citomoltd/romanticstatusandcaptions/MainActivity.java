package com.citomoltd.romanticstatusandcaptions;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView mainRecyclerView;
    DrawerLayout drawerLayout;
    MaterialToolbar materialToolbar;
    NavigationView navigationView;
    private long backPressedTime;
    private Toast backToast;

    ArrayList<HashMap<String,String>> arrayList=new ArrayList<>();
    HashMap<String,String>hashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        drawerLayout=findViewById(R.id.drawLayout);
        materialToolbar=findViewById(R.id.toolbar);
        navigationView=findViewById(R.id.navigationView);

        mainRecyclerView=findViewById(R.id.mainRecyclerView);


        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle
                (MainActivity.this,drawerLayout,materialToolbar,R.string.drawerLayout_close,R.string.drawerLayout_open);
        drawerLayout.addDrawerListener(toggle);



        //========recycler work=============
        setHashMap();
        LRecyclerView lRecyclerView=new LRecyclerView();
        mainRecyclerView.setAdapter(lRecyclerView);
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //nav intro====================================
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId()==R.id.homeId){
                    Toast.makeText(MainActivity.this, "home............", Toast.LENGTH_SHORT).show();
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else if (item.getItemId()==R.id.privacyIdMenu) {
                    //code here-------------------------------------------
                    Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://sites.google.com/view/romanticstatus-caption-privacy"));
                    startActivity(intent);
                    drawerLayout.closeDrawer(GravityCompat.START);

                } else if (item.getItemId()==R.id.shareMenu) {
                    // code here
                    ShareApp(MainActivity.this);
                    drawerLayout.closeDrawer(GravityCompat.START);

                } else if (item.getItemId()==R.id.moreApp) {
                    // code here==============
                    String developerName="https://play.google.com/store/apps/developer?id=citomoltd";
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(developerName)));

                    }catch (ActivityNotFoundException e){
                        startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(developerName)));

                    }
                    drawerLayout.closeDrawer(GravityCompat.START);

                } else if (item.getItemId()==R.id.ratingMenu) {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+getPackageName())));
                    }catch (ActivityNotFoundException e){
                        startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://play.google.com/store/apps/details?id="+getPackageName())));
                    }
                    drawerLayout.closeDrawer(GravityCompat.START);


                }
                return true;
            }
        });


        // Handle the back press using OnBackPressedCallback
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (backPressedTime + 2000 > System.currentTimeMillis()) {
                    if (backToast != null) {
                        backToast.cancel();
                    }
                    finish(); // Exit the app
                } else {
                    backToast = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
                    backToast.show();
                }
                backPressedTime = System.currentTimeMillis();
            }
        };

        // Add the callback to the OnBackPressedDispatcher
        getOnBackPressedDispatcher().addCallback(this, callback);


    }

    private class LRecyclerView extends RecyclerView.Adapter<LRecyclerView.LViewHolder> {

        class LViewHolder extends RecyclerView.ViewHolder {
            TextView itemTextTittle;
            LinearLayout next_activity;

            public LViewHolder(@NonNull View itemView) {
                super(itemView);
                itemTextTittle = itemView.findViewById(R.id.itemTextTittle);
                next_activity = itemView.findViewById(R.id.next_activity);
            }
        }

        @NonNull
        @Override
        public LViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View lview = inflater.inflate(R.layout.item_list_file, parent, false);
            return new LViewHolder(lview);
        }

        @Override
        public void onBindViewHolder(@NonNull LViewHolder holder, int position) {
            HashMap<String, String> item = arrayList.get(position);
            String title = item.get("tittle");
            holder.itemTextTittle.setText(title);

            holder.next_activity.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, Status_View_activity.class);
                intent.putExtra("TITTLE", title);
                intent.putExtra("val", position);
                startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }


    //setH--+=========================
    private void setHashMap() {
        String[] titles = {
                "বাংলা ক্যাপশন", "রোমান্টিক প্রেমের ছন্দ", "রোমান্টিক প্রেমের sms",
                "প্রেমিকা খুশি হওয়ার এসএমএস", "প্রেমিকার মন ভালো করার এসএমএস",
                "মিস করার এসএমএস", "ভালোবাসার এসএমএস", "ভালোবাসার স্ট্যাটাস",
                "রোমান্টিক FB ক্যাপশন", "প্রেমের শুভ সকাল এসএমএস",
                "প্রেমের শুভ নববর্ষ এসএমএস", "প্রেমের ঈদ মোবারক sms"
        };
        for (String title : titles) {
            HashMap<String, String> map = new HashMap<>();
            map.put("tittle", title);
            arrayList.add(map);
        }
    }







    //Storage permission code=========================
    //Storage permission code=========================



    private void ShareApp(Context context) {
        final String appPackageName = context.getPackageName();
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Download now: https://play.google.com/store/apps/details?id=" + appPackageName);
        sendIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(sendIntent, "Share via"));
    }



}