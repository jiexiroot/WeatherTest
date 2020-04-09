package com.example.gson;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.alibaba.fastjson.JSON;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.android.material.navigation.NavigationView;
import com.makeramen.roundedimageview.RoundedImageView;
import org.jetbrains.annotations.NotNull;
import org.litepal.LitePal;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private DrawerLayout drawerLayout;
    private List<NextWeatherBean> messageList=new ArrayList<>();
    private   NavigationView navigationView;
    private  TextView responseText,cityText;
    private EditText etText;
    private WeatherBean weatherBean;
    private LinearLayoutManager layoutManager;
    private int random_id;
    private String result;
    private BDLocationUtils bdLocationUtils ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Stetho.initializeWithDefaults(this);
        new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();

        initIntert();

        //开启定位
        bdLocationUtils = new BDLocationUtils(MainActivity.this);
        bdLocationUtils.doLocation();
        bdLocationUtils.mLocationClient.start();//初始化定位

        Button sendRequest = (Button) findViewById(R.id.send_request);
        responseText = (TextView) findViewById(R.id.response_text);
        cityText=(TextView) findViewById(R.id.response_city);
        etText=(EditText) findViewById(R.id.et_text) ;
        sendRequest.setOnClickListener(this);
        layoutManager = new LinearLayoutManager(this);
        initNav_view();

    }
    //获取字符串并将起转换为资源id
    public static int getResId(String variableName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(variableName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private void initIntert() {
        Intent intent = getIntent();
        String action = intent.getAction();
        if (action.equals("two")) {
            Bundle bundle = intent.getExtras();
            result = bundle.getString("name");
            random_id = bundle.getInt("user_id");
            //将获取的随机头像id转换为图片在安卓的id

            String img_id="img_user"+random_id;
            Log.d("tag",img_id);//测试img_id
            random_id=getResId(img_id,R.drawable.class);
        }
    }
    //对抽屉控件增加事件和初始化
    private void initNav_view() {
        drawerLayout=(DrawerLayout) findViewById(R.id.drawer_layout) ;
        navigationView = findViewById(R.id.nav_view);
        SharedPreferences pref = getSharedPreferences("city",MODE_PRIVATE);
        Map<String,?> map = pref.getAll();
        boolean flag = false;
        for (final Map.Entry<String,?> entry:map.entrySet()){
            navigationView.getMenu().add(1,1,1,entry.getValue().toString()).setIcon(R.drawable.city).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    drawerLayout.closeDrawers();
                    sendRequestWithOkHttp("http://v.juhe.cn/weather/index?format=2&cityname="+entry.getValue().toString()+"&key=c995e708e85722b617f23e068f053a20");
                    return false;
                }
            });
        }

        choose_user();
        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_dingwei:
                        //启动
//                        drawerLayout.closeDrawers();
                        List<String> permissionList = new ArrayList<>();
                        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
                        {
                            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
                        }
                        if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED)
                        {
                            permissionList.add(Manifest.permission.READ_PHONE_STATE);
                        }
                        if (ContextCompat.checkSelfPermission(MainActivity.this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        }
                        if (!permissionList.isEmpty()) {
                            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
                            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
                        }
                        else {

                        }


                        bdLocationUtils.mLocationClient.start();//开始定位
                        Log.d("tag",Const.LATITUDE+"");
                        Log.d("tag",Const.LONGITUDE+"");
                        //动态获取地址
                        sendRequestWithOkHttp("http://v.juhe.cn/weather/geo?format=2&key=c995e708e85722b617f23e068f053a20&lon="+Const.LONGITUDE+"&lat="+Const.LATITUDE);
                        Toast.makeText(MainActivity.this,Const.describe+"！",Toast.LENGTH_SHORT).show();
                        Toast.makeText(MainActivity.this,"网络获取成功！",Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.nav_exit:
                        Toast.makeText(MainActivity.this,"成功退出！",Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        finish();
//                        Intent aboutIntent = new Intent(MainActivity.this, aboutActivity.class);
//                        startActivity(aboutIntent);
//                        drawerLayout.closeDrawers();
                        break;
                }
                return false;
            }
        });

    }
    private static final int LOCATION_CODE = 1;
    private LocationManager lm;//【位置管理】


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 权限被用户同意。

                } else {
                    // 权限被用户拒绝了。
                    Toast.makeText(MainActivity.this, "定位权限被禁止，使用网络定位！",Toast.LENGTH_LONG).show();
                }

            }
        }
    }


    private void choose_user() {
        View headerView=navigationView.getHeaderView(0);
        TextView user_text=(TextView)headerView.findViewById(R.id.user_text);
        user_text.setText(String.format(getString(R.string.nav_header_title), result));//动态设置欢迎名
        RoundedImageView roundedImageView=(RoundedImageView) headerView.findViewById(R.id.imageView);
        roundedImageView.setImageResource(random_id);//设置头像
        navigationView.setItemIconTintList(null);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.send_request) {
            sendRequestWithOkHttp("http://v.juhe.cn/weather/index?format=2&cityname="+etText.getText()+"&key=c995e708e85722b617f23e068f053a20");
        }
    }


    private void sendRequestWithOkHttp(String url) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    OkHttpClient client = new OkHttpClient();
//                    Request request = new Request.Builder()
//                            // 指定访问的服务器地址是电脑本机
//                            .url("http://v.juhe.cn/weather/index?format=2&cityname=%E6%88%90%E9%83%BD&key=c995e708e85722b617f23e068f053a20")
//                            .build();
//                    Response response = client.newCall(request).execute();
//                    String responseData = response.body().string();
//                    parseJSONWithGSON(responseData);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();

        HttpUtil.sendOkHttpRequest(url,new okhttp3.Callback(){

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseData=response.body().string();
                parseJSONWithGSON(responseData);
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }
        });

    }

    private void parseJSONWithGSON(String jsonData) {

        Log.d("MainActivity", jsonData );
        String all="";
        boolean uiflag=false;

        weatherBean = JSON.parseObject(jsonData, WeatherBean.class);
        //判断成功返回数据
        String resultcode=weatherBean.getResultcode();
        if (resultcode.equals("200")) {
        //200为正常返回数据

            String city = weatherBean.getResult().getToday().getCity(); //城市
            String wind = weatherBean.getResult().getToday().getWind();  //风向
            String tem = weatherBean.getResult().getToday().getTemperature(); //温度
            String dressing_advice = weatherBean.getResult().getToday().getDressing_advice();
            String dressing_index = weatherBean.getResult().getToday().getDressing_index();


            String today = ("温度：" + tem + "\n风向：" + wind + "\n穿衣：" + dressing_advice + dressing_index + "\n\n");


            //后面几天
            String connectString = "";
            List<WeatherBean.ResultBean.FutureBean> futureBeanList = weatherBean.getResult().getFuture();
            for (int i = 1; i < futureBeanList.size(); i++) {
                String week2 = futureBeanList.get(i).getWeek();
                if (i == 1) week2 = "明    天";
                String weather2 = futureBeanList.get(i).getWeather();
                String date = futureBeanList.get(i).getDate();
                String tem2 = futureBeanList.get(i).getTemperature();
                String wind2 = futureBeanList.get(i).getWind();
                connectString = connectString + week2 + "        " + weather2 + "      " + tem2 + "    " + wind2 + "\n\n";
                String[] sArray = tem2.split("~");
                if (weather2.contains("转") == true) {
                    String[] sArray2 = weather2.split("转");
                    weather2 = sArray2[1];
                }
                int picId = 0;
                if (weather2.equals("晴")) picId = R.drawable.qingtian;
                if (weather2.endsWith("雨")) picId = R.drawable.xiayu;
                if (weather2.equals("多云")) picId = R.drawable.duoyun2;
                if (weather2.equals("阴")) picId = R.drawable.yintian;
                String[] sArray3 = new String[8];

                for (int j=4;j<date.length();j++){
                    char he=date.charAt(j);
                    if (he=='0' && j/2==0) he=' ';
                    sArray3[j-4]=he+"";
                }

                date=sArray3[0]+sArray3[1]+"月"+sArray3[2]+sArray3[3]+"日";
                NextWeatherBean nextWeatherBean = new NextWeatherBean(picId, date, week2, sArray[0], sArray[1]);
                messageList.add(nextWeatherBean);
            }
            if (all != null) uiflag = true;
            all = today + "\n" + connectString;
            showResponse(all, uiflag, city);
        }//失败提示
        else{
          uiflag=false;
          showResponse(all, uiflag, null);
        }
    }

    private void showResponse(final String all,final boolean flag,final String city){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (flag==true){
                    cityText.setText("城市:"+city);
                    responseText.setText(all);
                    TextView t1=(TextView) findViewById(R.id.city_id);
                    t1.setText(city);
                    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
                    recyclerView.setLayoutManager(layoutManager);
                    MsgAdapter adapter = new MsgAdapter(messageList);
                    recyclerView.setAdapter(adapter);
                    SnCal snCal=new SnCal();
                    snCal.setCityname(city);
                    snCal.setUsername(result);
                    snCal.save();
                    List<SnCal> messages= LitePal.select("cityname","username").order("id desc").limit(10).find(SnCal.class);
                    SharedPreferences.Editor editor= getSharedPreferences("city",MODE_PRIVATE).edit();
                    editor.apply();
                    SharedPreferences pref = getSharedPreferences("city",MODE_PRIVATE);
                    Map<String,?> map = pref.getAll();
                    if(map.isEmpty()){
                        editor.putString(String.valueOf(map.size()+1), city);
                        editor.apply();
                    }
                    for (Map.Entry<String, ?> entry : map.entrySet()) {
                        if (!entry.getValue().equals(city)) {
                            editor.putString(String.valueOf(map.size()+1), city);
                            editor.apply();
                        }
                    }
                }
                else
                    responseText.setText("连接超时！请检查网络连接或城市名称是否输入正确！");
            }
        });
    }

}
