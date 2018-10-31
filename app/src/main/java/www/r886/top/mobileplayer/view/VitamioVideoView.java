package www.r886.top.mobileplayer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

import io.vov.vitamio.widget.*;

/**
 * Created by Shiyue on 2018/10/7.
 */

public class VitamioVideoView extends io.vov.vitamio.widget.VideoView{
    public VitamioVideoView(Context context) {
        this(context,null);
    }

    public VitamioVideoView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public VitamioVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec,heightMeasureSpec);
    }

    public void setVideoSize(int videoWidth,int videoHeight)
    {
        ViewGroup.LayoutParams params=getLayoutParams();
        params.width=videoWidth;
        params.height=videoHeight;
        setLayoutParams(params);
    }
}
