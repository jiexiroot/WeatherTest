package com.example.gson.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.gson.CustomVideoView;
import com.example.gson.EditTextClearTools;
import com.example.gson.MainActivity;
import com.example.gson.R;

import java.util.Random;

public class LoginActivity extends AppCompatActivity {
    EditText e1;
    ImageView m1;
    Button btn1;
    private void init() {
        // TODO Auto-generated method stub
        e1 = (EditText) findViewById(R.id.text_1);

        m1 = (ImageView) findViewById(R.id.del_button);

        // 添加清除监听器
        EditTextClearTools.addclerListener(e1, m1);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionbar=getSupportActionBar();
        if(actionbar !=null){
            actionbar.hide();
        }
        init();
        initView();
        btn1=(Button) findViewById(R.id.btn_open);
        opennextlayout();
        quanxian();
    }
    private static final int LOCATION_CODE = 1;
    private LocationManager lm;//【位置管理】

    public void quanxian(){
        lm = (LocationManager) LoginActivity.this.getSystemService(LoginActivity.this.LOCATION_SERVICE);
        boolean ok = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (ok) {//开了定位服务
            if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                Log.e("BRG","没有权限");
                // 没有权限，申请权限。
                // 申请授权。
                ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_CODE);
//                        Toast.makeText(getActivity(), "没有权限", Toast.LENGTH_SHORT).show();

            } else {

                // 有权限了，去放肆吧。
//                        Toast.makeText(getActivity(), "有权限", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.e("BRG","系统检测到未开启GPS定位服务");
            Toast.makeText(LoginActivity.this, "系统检测到未开启GPS定位服务", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, 1315);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 权限被用户同意。

                } else {
                    // 权限被用户拒绝了。
                    Toast.makeText(LoginActivity.this, "定位权限被禁止，相关地图功能无法使用！",Toast.LENGTH_LONG).show();
                }

            }
        }
    }

    //切换按钮
    private  void opennextlayout(){
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String qwe=e1.getText().toString();
                int flag=0;
                Intent intent2 = new Intent(LoginActivity.this, MainActivity.class);
                intent2.setAction("two");
                Bundle bundle1 = new Bundle();
                Random random=new Random();
                bundle1.putInt("user_id",random.nextInt(8));
                bundle1.putString("name", qwe);
                intent2.putExtras(bundle1);     //将bundle传入intent中。
                Toast.makeText(LoginActivity.this,"欢迎您，"+qwe+"用户",Toast.LENGTH_SHORT).show();
                startActivity(intent2);

            }
        });
    }
    //创建播放视频的控件对象
    private CustomVideoView videoview;


    private void initView() {
        //加载视频资源控件
        videoview = (CustomVideoView) findViewById(R.id.videoview);
        //设置播放加载路径
        videoview.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video));
        //播放
        videoview.start();
        //循环播放
        videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                videoview.start();
            }
        });
    }

    //返回重启加载
    @Override
    protected void onRestart() {
        initView();
        super.onRestart();
    }

    //防止锁屏或者切出的时候，音乐在播放
    @Override
    protected void onStop() {
        videoview.stopPlayback();
        super.onStop();
    }
}
