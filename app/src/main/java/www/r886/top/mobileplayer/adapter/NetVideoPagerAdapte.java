package www.r886.top.mobileplayer.adapter;

import android.content.Context;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;

import org.xutils.x;

import java.util.ArrayList;

import www.r886.top.mobileplayer.R;
import www.r886.top.mobileplayer.domain.MediaItem;
import www.r886.top.mobileplayer.utils.Utils;

/**
 * Created by Shiyue on 2018/10/27.
 */

public class NetVideoPagerAdapte extends BaseAdapter {
    Context context;
    ArrayList<MediaItem> mediaItems;
    private Utils utils;
    public NetVideoPagerAdapte (Context context, ArrayList<MediaItem> mediaItems)
    {
        this.context=context;
        this.mediaItems=mediaItems;
        utils=new Utils();
    }

    @Override
    public int getCount() {
        return mediaItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mediaItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NetViewHoder netViewHoder;
        if (convertView==null)
        {
            convertView=View.inflate(context, R.layout.item_net_video_pager,null);
            netViewHoder=new NetViewHoder();
            netViewHoder.iv_icon=convertView.findViewById(R.id.iv_icon);
            netViewHoder.tv_name=convertView.findViewById(R.id.tv_name);
            netViewHoder.tv_desc=convertView.findViewById(R.id.tv_desc);
            convertView.setTag(netViewHoder);
        }
        else
            netViewHoder=(NetViewHoder)convertView.getTag();

        MediaItem mediaItem= mediaItems.get(position);
        netViewHoder.tv_name.setText(mediaItem.getName());
        netViewHoder.tv_desc.setText(mediaItem.getDesc());

        //x.image().bind(netViewHoder.iv_icon,mediaItem.getImageUrl());
        Glide.with(context)
                .load(mediaItem.getImageUrl())
                .into(netViewHoder.iv_icon);
        return convertView;
    }


}

class NetViewHoder{
    ImageView iv_icon;
    TextView tv_name;
    TextView tv_desc;
}
