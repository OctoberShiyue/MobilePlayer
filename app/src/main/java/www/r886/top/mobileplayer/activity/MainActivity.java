package www.r886.top.mobileplayer.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;

import www.r886.top.mobileplayer.fragment.AudioFragment;
import www.r886.top.mobileplayer.fragment.NetAuidoFragment;
import www.r886.top.mobileplayer.fragment.NetVideoFragment;
import www.r886.top.mobileplayer.R;
import www.r886.top.mobileplayer.fragment.VideoFragment;

/**
 * Created by Shiyue on 2018/10/2.
 */

public class MainActivity  extends Activity{


    private RadioGroup  rg_bottom_tag;
    private ArrayList<Fragment> baseFragment;
    private Fragment dFragment;
    private int position;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rg_bottom_tag=findViewById(R.id.rg_bottom_tag);
        baseFragment=new ArrayList<>();
        baseFragment.add(new VideoFragment());
        baseFragment.add(new AudioFragment());
        baseFragment.add(new NetVideoFragment());
        baseFragment.add(new NetAuidoFragment());
        rg_bottom_tag.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId)
                {
                    case R.id.rb_video:
                        position=0;
                        break;
                    case R.id.rb_audio:
                        position=1;
                        break;
                    case R.id.rb_net_video:
                        position=2;
                        break;
                    case R.id.rb_net_audio:
                        position=3;
                        break;
                }
                setFragment();
            }
        });
        rg_bottom_tag.check(R.id.rb_video);

    }

    private void setFragment() {
        FragmentManager fm= getFragmentManager();
        FragmentTransaction ft= fm.beginTransaction();
        Fragment f=baseFragment.get(position);
        if (f.equals(dFragment)) return;
        if (!f.isAdded())
        {
            Log.e("Mess", "setFragment: "+position+"-该fragment没有显示,正在初始化..." );
            ft.add(R.id.fl_main_content,f);
            ft.hide(f);
            ft.show(f);
        }
        if (dFragment!=null)
        {
            ft.show(f);
            ft.hide(dFragment);
        }
        dFragment=f;
        ft.commit();
    }

    public void onRecord(View view) {
        Toast.makeText(MainActivity.this,"记录",Toast.LENGTH_SHORT).show();
    }
    public void onGame(View view) {
        Toast.makeText(MainActivity.this,"游戏",Toast.LENGTH_SHORT).show();
    }
    public void onSearch(View view) {
        Toast.makeText(MainActivity.this,"搜索",Toast.LENGTH_SHORT).show();
    }

}
