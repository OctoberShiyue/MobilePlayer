package www.r886.top.mobileplayer.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import www.r886.top.mobileplayer.R;
import www.r886.top.mobileplayer.activity.MainActivity;

public class SplashActivity extends Activity {
    private  final  int MY_READ_EXTERNAL_STORAGE=1000;
    private  Handler handler=  new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getPermission();
            }
        },2000);

    }

    private void startMainActivity() {
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                getPermission();
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();

    }


    private void getPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_READ_EXTERNAL_STORAGE);
        }else
        {
            startMainActivity();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissions.length>0&&requestCode==MY_READ_EXTERNAL_STORAGE&&permissions[0].equals(Manifest.permission.READ_EXTERNAL_STORAGE))
        {
            Toast.makeText(this,"授权成功",Toast.LENGTH_SHORT).show();
            startMainActivity();
        }else
        {
            Toast.makeText(this,"授权失败,不然无法打开视频播放器!",Toast.LENGTH_SHORT).show();
        }
    }
}
