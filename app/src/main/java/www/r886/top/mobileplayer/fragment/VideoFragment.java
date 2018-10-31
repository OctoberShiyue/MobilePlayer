package www.r886.top.mobileplayer.fragment;


import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import www.r886.top.mobileplayer.R;
import www.r886.top.mobileplayer.activity.SystemVideoPlayer;
import www.r886.top.mobileplayer.domain.MediaItem;
import  www.r886.top.mobileplayer.adapter.VideoPagerAdapter;
import www.r886.top.mobileplayer.utils.Utils;


public class VideoFragment extends Fragment {
    private ListView listView;
    private TextView tv_nomedia;
    private ProgressBar pb_loading;
    private ArrayList<MediaItem> mediaItems;
    private Handler handler;
    private Button btn_opennetvideo;
    private EditText et_opennetvideo;

    private VideoPagerAdapter videoPagerAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_video, container, false);
        listView=view.findViewById(R.id.lv_listview);
        tv_nomedia=view.findViewById(R.id.tv_nomedia);
        pb_loading=view.findViewById(R.id.pb_loading);
        et_opennetvideo=view.findViewById(R.id.et_opennetvideo);
        et_opennetvideo.clearFocus();
        btn_opennetvideo=view.findViewById(R.id.btn_opennetvideo);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // Toast.makeText(VideoFragment.this.getActivity(),mediaItems.get(position).toString(),Toast.LENGTH_SHORT).show();

             //   Intent intent=new Intent();
           //     intent.setDataAndType(Uri.parse(mediaItems.get(position).getData()),"video*/");
         //       VideoFragment.this.getActivity().startActivity(intent);
//                Intent intent=new Intent(VideoFragment.this.getActivity(),SystemVideoPlayer.class);
//                intent.setDataAndType(Uri.parse(mediaItems.get(position).getData()),"video*/");
//                VideoFragment.this.getActivity().startActivity(intent);intent
                Intent intent=new Intent(VideoFragment.this.getActivity(),SystemVideoPlayer.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("videolist",mediaItems);
                intent.putExtras(bundle);
                intent.putExtra("position",position);
                VideoFragment.this.getActivity().startActivity(intent);


            }
        });
        handler=new Handler(getActivity().getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what)
                {
                    case 0:
                        tv_nomedia.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        videoPagerAdapter=new VideoPagerAdapter(VideoFragment.this.getActivity(),mediaItems);
                        listView.setAdapter(videoPagerAdapter);
                        break;
                }

                pb_loading.setVisibility(View.GONE);
            }
        };

        //----
        btn_opennetvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(VideoFragment.this.getActivity(),SystemVideoPlayer.class);
                intent.setData(Uri.parse(et_opennetvideo.getText().toString()));
             /*   intent.setDataAndType(Uri.parse(et_opennetvideo.getText().toString()),"video/*");
                Log.e("Mess", "et_opennetvideo.getText().toString()=: "+et_opennetvideo.getText().toString() );*/
                VideoFragment.this.getActivity().startActivity(intent);
            }
        });

        //Log.e("Mess", "VideoFragment -onCreateView: " );
        getDataFromLocal();
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) return;

    }

    /**
     * 从本地sd卡得到数据
     */
    private void getDataFromLocal() {
        mediaItems=new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
           /*    try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
                ContentResolver contentResolver= getActivity().getContentResolver();
                Uri uri= MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                String[] objs={
                        MediaStore.Video.Media.DISPLAY_NAME,
                        MediaStore.Video.Media.DURATION,
                        MediaStore.Video.Media.SIZE,
                        MediaStore.Video.Media.DATA,
                        MediaStore.Video.Media.ARTIST

                };
                Cursor cursor= contentResolver.query(uri,objs,null,null,null);
                if (cursor!=null)
                {
                    while (cursor.moveToNext())
                    {
                        MediaItem mediaItem=new MediaItem();
                        String name=cursor.getString(0);
                        mediaItem.setName(name);
                        long duration=cursor.getLong(1);
                        mediaItem.setDuration(duration);
                        long size=cursor.getLong(2);
                        mediaItem.setSize(size);
                        String data=cursor.getString(3);
                        mediaItem.setData(data);
                        String artist=cursor.getString(4);
                        mediaItem.setArtist(artist);
                        mediaItems.add(mediaItem);
                    }
                    cursor.close();

                }
                if (mediaItems !=null && mediaItems.size()>0)
                    handler.sendEmptyMessage(1);
                else
                    handler.sendEmptyMessage(0);

            }
        }).start();
    }



}
