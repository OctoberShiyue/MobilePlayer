package www.r886.top.mobileplayer.adapter;

import android.content.Context;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import www.r886.top.mobileplayer.R;
import www.r886.top.mobileplayer.domain.MediaItem;
import www.r886.top.mobileplayer.utils.Utils;

/**
 * Created by Shiyue on 2018/10/4.
 */

public class VideoPagerAdapter extends BaseAdapter
{
    Context context;
    ArrayList<MediaItem> mediaItems;
    private Utils utils;
    public VideoPagerAdapter (Context context, ArrayList<MediaItem> mediaItems)
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
        ViewHoder viewHoder;
        if (convertView==null)
        {
            convertView=View.inflate(context, R.layout.video_pager,null);
            viewHoder=new ViewHoder();
            viewHoder.iv_icon=convertView.findViewById(R.id.iv_icon);
            viewHoder.tv_name=convertView.findViewById(R.id.tv_name);
            viewHoder.tv_time=convertView.findViewById(R.id.tv_time);
            viewHoder.tv_size=convertView.findViewById(R.id.tv_size);
            convertView.setTag(viewHoder);
        }
        else
            viewHoder=(ViewHoder)convertView.getTag();

        MediaItem mediaItem= mediaItems.get(position);
        viewHoder.tv_name.setText(mediaItem.getName());
        viewHoder.tv_size.setText(Formatter.formatFileSize(context,mediaItem.getSize()));
        viewHoder.tv_time.setText(utils.stringForTime((int) mediaItem.getDuration()));
        return convertView;
    }


}

 class ViewHoder{
    ImageView iv_icon;
    TextView tv_name;
    TextView tv_time;
    TextView tv_size;
}
