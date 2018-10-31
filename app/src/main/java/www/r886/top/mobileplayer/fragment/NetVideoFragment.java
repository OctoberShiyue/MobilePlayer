package www.r886.top.mobileplayer.fragment;


import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.drm.ProcessedData;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import www.r886.top.mobileplayer.R;
import www.r886.top.mobileplayer.activity.SystemVideoPlayer;
import www.r886.top.mobileplayer.adapter.NetVideoPagerAdapte;
import www.r886.top.mobileplayer.domain.MediaItem;
import www.r886.top.mobileplayer.utils.CacheUtils;
import www.r886.top.mobileplayer.utils.Constants;
import www.r886.top.mobileplayer.utils.Utils;
import www.r886.top.mobileplayer.view.XListView;


public class NetVideoFragment extends Fragment {
    @ViewInject(R.id.lv_listview)
    private XListView mListView;

    @ViewInject(R.id.tv_nonet)
    private TextView mTvnonet;

    @ViewInject(R.id.pb_loading)
    private ProgressBar mPb_loading;


    private ArrayList<MediaItem> mediaItems;

    private NetVideoPagerAdapte netVideoPagerAdapte;

    private boolean isLoadMore=false;

    private void onLoad() {
        mListView.stopRefresh();
        mListView.stopLoadMore();
        mListView.setRefreshTime(Utils.getSystemTime());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_net_video, container, false);
        x.view().inject(this,view);
        mListView.setPullLoadEnable(true);
        mListView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                getDataFromNet();
            }

            @Override
            public void onLoadMore() {
                getMoreDataFromNet();
            }
        });
        return  view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String saveJson=CacheUtils.getString(NetVideoFragment.this.getActivity(),Constants.NET_URL);
        if (!TextUtils.isEmpty(saveJson))
        {
            processData(saveJson);
        }
        LogUtil.e("网络视频数据被初始化了。。。");
        refresh();



    }

    private void refresh()
    {
        Constants.setRequTime();
        String sUrl=Constants.NET_URL;
//        if (isLoadMore)
//            sUrl=Constants.getNet_Url_Load(mediaItems.get(mediaItems.size()-1).getData());
        x.http().get(new RequestParams(sUrl), new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                LogUtil.e("联网成功=="+result);
                CacheUtils.putString(NetVideoFragment.this.getActivity(),Constants.NET_URL,result);
                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("联网失败=="+ex.getMessage());
                Toast.makeText(NetVideoFragment.this.getActivity(),"联网失败..",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("onCancelled=="+cex.getMessage());
            }

            @Override
            public void onFinished() {
                LogUtil.e("onFinished==");
                onLoad();
                showNetUi();
            }
        });
    }

    private void processData(String json) {
        if (!isLoadMore)
        {
            mediaItems=parseJson(json);

        }else
        {
            ArrayList<MediaItem> mediaItem=parseJson(json);
            mediaItems.addAll(mediaItem);
            netVideoPagerAdapte.notifyDataSetChanged();
        }


    }

    private void showNetUi()
    {
        if (mediaItems!=null&& mediaItems.size()>0)
        {
            netVideoPagerAdapte=new NetVideoPagerAdapte(NetVideoFragment.this.getActivity(),mediaItems);
            mListView.setAdapter(netVideoPagerAdapte);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (mediaItems.get(position-1).getData().equals("NoN")||mediaItems.get(position-1).getData().equals(""))
                    {
                        Toast.makeText(NetVideoFragment.this.getActivity(),"该视频暂时没有地址",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Intent intent=new Intent(NetVideoFragment.this.getActivity(),SystemVideoPlayer.class);
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("videolist",mediaItems);
                    intent.putExtras(bundle);
                    intent.putExtra("position",position-1);
                    NetVideoFragment.this.getActivity().startActivity(intent);
                }
            });
            mTvnonet.setVisibility(View.GONE);
        }
        else
        {
            mTvnonet.setVisibility(View.VISIBLE);
        }
        mPb_loading.setVisibility(View.GONE);
    }


    private ArrayList<MediaItem> parseJson(String json) {
        final ArrayList<MediaItem> mediaItems=new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray data=jsonObject.optJSONArray("data");
            if (data != null && data.length() > 0)
          {
              for (int i = 0; i < data.length(); i++) {
                  JSONObject obj=(JSONObject) data.get(i);
                  String title=  obj.optString("videoName");
                  String description="正在获取地址...";
                  String imageUrl=obj.optString("videoBigPic");
                  String mp4_url="NoN";
                  String vid=obj.optString("vid");
                  MediaItem mediaItem=new MediaItem();
                  mediaItem.setName(title);
                  mediaItem.setDesc(description);
                  mediaItem.setImageUrl(imageUrl);
                  mediaItem.setData(mp4_url);
                  mediaItems.add(mediaItem);

                  final int finalI = i;
                  x.http().get(new RequestParams(Constants.NET_URL_VIDEO+vid), new Callback.CommonCallback<String>() {
                      @Override
                      public void onSuccess(String result) {
                          MediaItem mediaItem1=mediaItems.get(finalI);
                          String videodown=parseVideoJson(result);
                          if (videodown.equals(""))
                          {
                              mediaItem1.setDesc("解析地址失败....");
                          }else
                          {
                              mediaItem1.setDesc("正在获取视频...");
                              x.http().get(new RequestParams(Constants.NET_URL_VIDEO_DOWN+videodown), new CommonCallback<String>() {
                                  @Override
                                  public void onSuccess(String result) {
                                      MediaItem mediaItem1=mediaItems.get(finalI);
                                      String url=parseVideoDownJson(result);
                                      if (url.equals(""))
                                      {
                                          mediaItem1.setDesc("解析视频失败....");
                                      }else
                                      {
                                          mediaItem1.setDesc("");
                                          mediaItem1.setData(url);
                                      }
                                  }

                                  @Override
                                  public void onError(Throwable ex, boolean isOnCallback) {
                                      mediaItems.get(finalI).setDesc("解析视频失败....");
                                  }

                                  @Override
                                  public void onCancelled(CancelledException cex) {

                                  }

                                  @Override
                                  public void onFinished() {
                                      netVideoPagerAdapte.notifyDataSetChanged();

                                  }
                              });
                          }
                      }

                      @Override
                      public void onError(Throwable ex, boolean isOnCallback) {
                          mediaItems.get(finalI).setDesc("解析地址失败....");
                      }

                      @Override
                      public void onCancelled(CancelledException cex) {

                      }

                      @Override
                      public void onFinished() {
                            netVideoPagerAdapte.notifyDataSetChanged();
                      }
                  });
              }
          }





//            JSONObject recomVideos = jsonObject.optJSONObject("RecomVideos");
//            JSONArray data = recomVideos.getJSONArray("data");
//            if (data != null && data.length() > 0)
//          {
//              for (int i = 0; i < data.length(); i++) {
//                  JSONObject obj=(JSONObject) data.get(i);
//                  JSONObject client_param= obj.getJSONObject("client_param");
//                  JSONObject page_params=client_param.getJSONObject("page_params");
//                  String title=  page_params.optString("title");
//                  String description=page_params.optString("source");
//                  String imageUrl=obj.optString("poster");
//                  String mp4_url=page_params.optString("src");
//                  MediaItem mediaItem=new MediaItem();
//                  mediaItem.setName(title);
//                  mediaItem.setDesc(description);
//                  mediaItem.setImageUrl(imageUrl);
//                  mediaItem.setData(mp4_url);
//                  mediaItems.add(mediaItem);
//              }
//          }

//            JSONObject data=jsonObject.optJSONObject("data");
//            JSONArray videoList=data.optJSONArray("videoList");
//            if (videoList!=null&&videoList.length()>0)
//            {
//                for (int i = 0; i <videoList.length() ; i++) {
//                    JSONObject  jsonObjectItem = (JSONObject) videoList.get(i);
//                    if (jsonObjectItem!=null)
//                    {
//                        MediaItem mediaItem=new MediaItem();
//                        String title=  jsonObjectItem.optString("title");
//                        String description=jsonObjectItem.optString("description");
//                        String imageUrl=jsonObjectItem.optString("cover");
//                        String mp4_url=jsonObjectItem.optString("mp4_url");
//                        mediaItem.setName(title);
//                        mediaItem.setDesc(description);
//                        mediaItem.setImageUrl(imageUrl);
//                        mediaItem.setData(mp4_url);
//                        mediaItems.add(mediaItem);
//                    }
//                }
//            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mediaItems;
    }

    private String parseVideoJson(String json)
    {
      String su="";
        try {
            JSONObject jsonObject=new JSONObject(json);
            su=jsonObject.optJSONObject("data").optJSONArray("su").getString(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return su;
    }

    private String parseVideoDownJson(String json)
    {
        String url="";
        try {
            JSONObject jsonObject=new JSONObject(json);
            url=((JSONObject)jsonObject.optJSONArray("servers").get(0)).optString("url");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return url;
    }


    public void getDataFromNet() {
        //  Toast.makeText(NetVideoFragment.this.getActivity(),"刷新  onRefresh",Toast.LENGTH_SHORT).show();
        isLoadMore=false;
        refresh();
        LogUtil.e(Thread.currentThread().toString());
    }
    public void getMoreDataFromNet() {
        isLoadMore=true;
        refresh();
      // Toast.makeText(NetVideoFragment.this.getActivity(),"加载更多  onLoadMore",Toast.LENGTH_SHORT).show();
        LogUtil.e(Thread.currentThread().toString());
    }

}
