package www.r886.top.mobileplayer;

import android.app.Application;

import org.xutils.x;

/**
 * Created by Shiyue on 2018/10/27.
 */

public class MyApplication  extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);
    }
}
