package com.example.lazykitchen.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.example.lazykitchen.R;
import com.example.lazykitchen.util.ActivityUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeActivity extends AppCompatActivity {

    String userDataUrlPrefix="http://47.100.4.109:8080/user/info";
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        intent=getIntent();
        syncLocalUserData();
        this.getSupportActionBar().hide();
        ActivityUtils.add(this.getClass().getSimpleName(),this);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavController navController;
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        navController = navHostFragment.getNavController();
        AppBarConfiguration configuration = new AppBarConfiguration.Builder(R.id.searchFragment,R.id.shareFragment,R.id.personFragment).build();
        NavigationUI.setupActionBarWithNavController(this,navController,configuration);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }

    public void syncLocalUserData(){
        OkHttpClient client = new OkHttpClient();
        //String phone=intent.getStringExtra("phone");
        String phone="17321050252";
        String userDataUrl=userDataUrlPrefix+"?phone="+phone;
        Request request = new Request.Builder()
                .url(userDataUrl)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Toast toast = Toast.makeText(HomeActivity.this, "????????????????????????", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    Gson gson=new Gson();
                    Map map=gson.fromJson(response.body().string(),Map.class);
                    // code=0->?????? code=-1->??????
                    String name=((Map)map.get("userInfo")).get("name").toString();
                    String sex=((Map)map.get("userInfo")).get("gender").toString();
                    String id=(int)(double)(
                            (
                                    (Map)map.get("userInfo")
                            ).get("id"))+"";
                    //??????UI???????????????UI????????????
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            // ????????????????????????
                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
                            prefs.edit().putString("ID",id).apply();
                            prefs.edit().putString("name",name).apply();
                            String sexCode="2";
                            if(sex.equals("???")) {
                                sexCode = "0";
                            }
                            else if (sex.equals("???")){
                                sexCode= "1";
                            }
                            prefs.edit().putString("sex",sexCode).apply();
                        }
                    });
                }
            }
        });
    }
}