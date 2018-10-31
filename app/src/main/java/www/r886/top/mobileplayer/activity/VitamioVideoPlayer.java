package www.r886.top.mobileplayer.activity;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.xutils.common.util.LogUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.VideoView;
import www.r886.top.mobileplayer.R;
import www.r886.top.mobileplayer.domain.MediaItem;
import www.r886.top.mobileplayer.utils.Utils;

import www.r886.top.mobileplayer.view.VitamioVideoView;

public class VitamioVideoPlayer extends Activity  implements View.OnClickListener {
    private static final int PROGRESS = 1;
    private static final int HIDE_MEDIACONTROLLER = 2;
    private static final int DEFAULT_SCREEN = 1;
    private static final int FULL_SCREEN = 2;
    private static final int SHOW_SPED = 3;
    private VitamioVideoView videoView;
    private Uri uri = null;
    private LinearLayout llTop;
    private TextView tvName;
    private ImageView ivBattery;
    private TextView tvSystemItem;
    private Button btnVoice;
    private Button btnSwichPlayer;
    private LinearLayout llBottom;
    private TextView tvCurrentItem;
    private TextView tvDuration;
    private Button btnExit;
    private Button btnPre;
    private Button btnVideoStartPause;
    private Button btnVideoNext;
    private Button btnVideoVideoSwitchScree;
    private SeekBar seekbarVideo;
    private SeekBar seekbarAudio;
    private RelativeLayout media_controller;
    private Utils utils;
    private MyReceiver myReceiver;
    private ArrayList<MediaItem> mediaItems;
    private int position;
    private GestureDetector detector;
    private boolean isshowMediaController = false;
    private boolean isFullScreen = false;

    private AudioManager audioManager;
    private LinearLayout ll_buffer;
    private TextView tv_netspeed;
    private TextView tv_loading_netspeed;
    private LinearLayout ll_loading;
    /**
     * 当前声音
     */
    private int currentVoice;
    /**
     * 最大声音
     */
    private int maxVoice;


    private int screenWidth = 0;
    private int screenHeight = 0;

    private int mVideoWidth = 0;
    private int mVideoHeight = 0;
    private int distanceY;
    private int startY;
    private int touchRang;
    private int mVol;

    private boolean isMute = false;
    private boolean isNetUri = false;
    private boolean isUseSystem = false;
    private int precurrentPosition = 0;


    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2018-10-04 20:10:03 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        tv_loading_netspeed = findViewById(R.id.tv_loading_netspeed);
        ll_loading = findViewById(R.id.ll_loading);
        media_controller = findViewById(R.id.media_controller);
        llTop = (LinearLayout) findViewById(R.id.ll_top);
        tvName = (TextView) findViewById(R.id.tv_name);
        ivBattery = (ImageView) findViewById(R.id.iv_battery);
        tvSystemItem = (TextView) findViewById(R.id.tv_system_item);
        btnVoice = (Button) findViewById(R.id.btn_voice);
        btnSwichPlayer = (Button) findViewById(R.id.btn_swich_player);
        llBottom = (LinearLayout) findViewById(R.id.ll_bottom);
        tvCurrentItem = (TextView) findViewById(R.id.tv_current_item);
        tvDuration = (TextView) findViewById(R.id.tv_duration);
        btnExit = (Button) findViewById(R.id.btn_exit);
        btnPre = (Button) findViewById(R.id.btn_pre);
        btnVideoStartPause = (Button) findViewById(R.id.btn_video_start_pause);
        btnVideoNext = (Button) findViewById(R.id.btn_video_next);
        btnVideoVideoSwitchScree = (Button) findViewById(R.id.btn_video_video_switch_scree);
        seekbarVideo = findViewById(R.id.seekbarVideo);
        seekbarAudio = findViewById(R.id.seekbarAudio);
        ll_buffer = findViewById(R.id.ll_buffer);
        tv_netspeed = findViewById(R.id.tv_netspeed);
        btnVoice.setOnClickListener(this);
        btnSwichPlayer.setOnClickListener(this);
        btnExit.setOnClickListener(this);
        btnPre.setOnClickListener(this);
        btnVideoStartPause.setOnClickListener(this);
        btnVideoNext.setOnClickListener(this);
        btnVideoVideoSwitchScree.setOnClickListener(this);
        seekbarVideo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    videoView.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                handler.removeMessages(HIDE_MEDIACONTROLLER);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER, 5000);
            }


        });


    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2018-10-04 20:10:03 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        Log.e("Mess", "onClick: ");
        if (v == btnVoice) {
            isMute = !isMute;
            // Handle clicks for btnVoice
            updataVoice(currentVoice, isMute);

        } else if (v == btnSwichPlayer) {
            // Handle clicks for btnSwichPlayer
            showSwichPlayerDialog();
        } else if (v == btnExit) {
            // Handle clicks for btnExit
            finish();
        } else if (v == btnPre) {
            // Handle clicks for btnPre
            playPreVideo();
        } else if (v == btnVideoStartPause) {
            // Handle clicks for btnVideoStartPause
            startAndPause();
        } else if (v == btnVideoNext) {
            // Handle clicks for btnVideoNext
            playNextVideo();
        } else if (v == btnVideoVideoSwitchScree) {
            // Handle clicks for btnVideoVideoSwitchScree
            setFullScreenAndDefault();
        }
        handler.removeMessages(HIDE_MEDIACONTROLLER);
        handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER, 5000);
    }

    private void showSwichPlayerDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("当你播放视频有花屏的时候请尝试切换播放器。");
        builder.setPositiveButton("确定切换", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startVitamioPlayer();
            }
        });
        builder.setNegativeButton("取消",null);
        builder.show();
    }

    private void startVitamioPlayer() {
        if (videoView!=null)
        {
            videoView.stopPlayback();
        }
        Intent intent=new Intent(VitamioVideoPlayer.this,SystemVideoPlayer.class);
        if (mediaItems!=null&& mediaItems.size()>0)
        {

            Bundle bundle=new Bundle();
            bundle.putSerializable("videolist",mediaItems);
            intent.putExtras(bundle);
            intent.putExtra("position",position);

        }else if (uri!=null)
        {
            intent.setData(uri);
        }
        startActivity(intent);
        finish();
    }

    private void startAndPause() {
        if (videoView.isPlaying()) {
            videoView.pause();
            btnVideoStartPause.setBackgroundResource(R.drawable.btn_video_start_real_selector);
        } else {
            videoView.start();
            btnVideoStartPause.setBackgroundResource(R.drawable.btn_video_start_pause_selector);
        }
    }

    private void playPreVideo() {
        LogUtil.e("playPreVideo"+position);
        if (mediaItems != null && mediaItems.size() > 0) {
            position--;
            if (position >= 0) {
                ll_loading.setVisibility(View.VISIBLE);
                MediaItem mediaItem = mediaItems.get(position);
                tvName.setText(mediaItem.getName());
                isNetUri = utils.isNetUri(mediaItem.getData());
                videoView.setVideoPath(mediaItem.getData());
                setButtonState();
                LogUtil.e("   ll_loading.setVisibility(View.VISIBLE);");
            }

        } else if (uri != null) {
            setButtonState();
        }
    }

    private void playNextVideo() {
        LogUtil.e("playNextVideo"+position);
        if (mediaItems != null && mediaItems.size() > 0) {
            position++;
            if (position < mediaItems.size()) {
                ll_loading.setVisibility(View.VISIBLE);
                MediaItem mediaItem = mediaItems.get(position);
                tvName.setText(mediaItem.getName());
                isNetUri = utils.isNetUri(mediaItem.getData());
                videoView.setVideoPath(mediaItem.getData());
                setButtonState();
                LogUtil.e("   ll_loading.setVisibility(View.VISIBLE);");
            }

        } else if (uri != null) {
            setButtonState();
        }
    }

    private void setButtonState() {
        if (mediaItems != null && mediaItems.size() > 0) {
            if (mediaItems.size() == 1) {
                btnPre.setBackgroundResource(R.drawable.btn_pre_gray);
                btnPre.setEnabled(false);
                btnVideoNext.setBackgroundResource(R.drawable.btn_next_gray);
                btnVideoNext.setEnabled(false);
            } else if (position == 0) {
                btnPre.setBackgroundResource(R.drawable.btn_pre_gray);
                btnPre.setEnabled(false);
            } else if (position == mediaItems.size() - 1) {
                btnVideoNext.setBackgroundResource(R.drawable.btn_next_gray);
                btnVideoNext.setEnabled(false);
            } else {
                btnPre.setBackgroundResource(R.drawable.btn_pre_selector);
                btnPre.setEnabled(true);
                btnVideoNext.setBackgroundResource(R.drawable.btn_video_next_selector);
                btnVideoNext.setEnabled(true);
            }
        } else if (uri != null) {
            btnPre.setBackgroundResource(R.drawable.btn_pre_gray);
            btnPre.setEnabled(false);
            btnVideoNext.setBackgroundResource(R.drawable.btn_next_gray);
            btnVideoNext.setEnabled(false);
        }
    }

    private Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Vitamio.isInitialized(this);

        setContentView(R.layout.activity_vitamio_video_player);
        videoView = findViewById(R.id.videoview);
        findViews();
        utils = new Utils();
        initData();
        detector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
                startAndPause();
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                setFullScreenAndDefault();
                return super.onDoubleTap(e);
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (isshowMediaController) {
                    hideMediaController();
                    handler.removeMessages(HIDE_MEDIACONTROLLER);
                } else {
                    showMediaController();
                    handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER, 5000);
                }
                return super.onSingleTapConfirmed(e);
            }
        });
        handler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {

                    case SHOW_SPED:
                        String netSpeed = utils.getNetSpeed(VitamioVideoPlayer.this);
                        tv_loading_netspeed.setText("玩命加载中.." + netSpeed);
                        tv_netspeed.setText("缓冲加载中..." + netSpeed);
                        handler.removeMessages(SHOW_SPED);
                        handler.sendEmptyMessageDelayed(SHOW_SPED, 2000);
                        break;

                    case PROGRESS:
                        int currentPostion = (int) videoView.getCurrentPosition();
                        seekbarVideo.setProgress(currentPostion);
                        tvCurrentItem.setText(utils.stringForTime(currentPostion));

                        tvSystemItem.setText(getSystemTime());

                        if (isNetUri) {
                            int buffer = videoView.getBufferPercentage();
                            int totaBuffer = buffer * seekbarVideo.getMax();
                            int secondaryProgress = totaBuffer / 100;
                            seekbarVideo.setSecondaryProgress(secondaryProgress);
                        } else {
                            seekbarVideo.setSecondaryProgress(0);
                        }


                        if (!isUseSystem && videoView.isPlaying()) {

                            if (videoView.isPlaying()) {
                                int buffer = currentPostion - precurrentPosition;
                                if (buffer < 500) {
                                    ll_buffer.setVisibility(View.VISIBLE);
                                } else {
                                    ll_buffer.setVisibility(View.GONE);
                                }
                            } else {
                                ll_buffer.setVisibility(View.GONE);
                            }

                        }
                        precurrentPosition = currentPostion;

                        removeMessages(PROGRESS);
                        sendEmptyMessageDelayed(PROGRESS, 1000);
                        if (currentPostion >= videoView.getDuration())
                            btnVideoStartPause.setBackgroundResource(R.drawable.btn_video_start_real_selector);
                        break;
                    case HIDE_MEDIACONTROLLER:
                        hideMediaController();
                        break;
                }
            }
        };
        handler.sendEmptyMessageDelayed(SHOW_SPED, 2000);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoView.start();
                ll_loading.setVisibility(View.GONE);
                int duration = (int) videoView.getDuration();
                seekbarVideo.setMax(duration);
                tvDuration.setText(utils.stringForTime(duration));
                handler.sendEmptyMessage(PROGRESS);
                hideMediaController();
                mVideoWidth = mp.getVideoWidth();
                mVideoHeight = mp.getVideoHeight();
                //    videoView.setVideoSize(mp.getVideoWidth(),mp.getVideoHeight());

            }
        });
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                AlertDialog.Builder builder=new AlertDialog.Builder(VitamioVideoPlayer.this);
                builder.setTitle("提示");
                builder.setMessage("抱歉,无法播放该视频");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.show();
                return true;
            }
        });
        // videoView.setMediaController(new MediaController(this));
        uri = getIntent().getData();
        mediaItems = (ArrayList<MediaItem>) getIntent().getSerializableExtra("videolist");
        position = getIntent().getIntExtra("position", 0);
        if (mediaItems != null && mediaItems.size() > 0) {
            MediaItem mediaItem = mediaItems.get(position);
            tvName.setText(mediaItem.getName());
            isNetUri = utils.isNetUri(mediaItem.getData());
            videoView.setVideoPath(mediaItem.getData());
        } else if (uri != null) {
            tvName.setText(uri.toString());
            isNetUri = utils.isNetUri(uri.toString());
            videoView.setVideoURI(uri);
        } else {
            Toast.makeText(VitamioVideoPlayer.this, "你没有传递数据", Toast.LENGTH_SHORT).show();
        }
        setButtonState();

        seekbarAudio.setMax(maxVoice);
        seekbarAudio.setProgress(currentVoice);
        seekbarAudio.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    if (progress > 0) {
                        isMute = false;
                    } else {
                        isMute = true;
                    }

                    updataVoice(progress, isMute);

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                handler.removeMessages(HIDE_MEDIACONTROLLER);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER, 4000);
            }
        });
        ll_loading.setVisibility(View.GONE);
        if (isUseSystem) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                    @Override
                    public boolean onInfo(MediaPlayer mp, int what, int extra) {

                        switch (what) {
                            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                                ll_buffer.setVisibility(View.VISIBLE);
                                break;
                            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                                ll_buffer.setVisibility(View.INVISIBLE);
                                break;
                        }
                        return true;
                    }
                });
            }
        }



    }



    /**
     * 设置音量的大小
     *
     * @param progress
     */
    private void updataVoice(int progress, boolean isMute) {
        if (isMute) {
            seekbarAudio.setProgress(0);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
        } else {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            seekbarAudio.setProgress(progress);
            currentVoice = progress;

        }
    }

    private void setFullScreenAndDefault() {
        if (isFullScreen) {
            videoView.setVideoSize(screenWidth, screenHeight);
            btnVideoVideoSwitchScree.setBackgroundResource(R.drawable.btn_video_switch_scree_default_selector);
            setVideoType(DEFAULT_SCREEN);

        } else {
            // for compatibility, we adjust size based on aspect ratio
            //    int mVideoHecight=0;
            //   int mVideoWidth=0;
            int height = screenHeight;
            int width = screenWidth;
            if (mVideoWidth * height < width * mVideoHeight) {
                //Log.i("@@@", "image too wide, correcting");
                width = height * mVideoWidth / mVideoHeight;
            } else if (mVideoWidth * height > width * mVideoHeight) {
                //Log.i("@@@", "image too tall, correcting");
                height = width * mVideoHeight / mVideoWidth;
            }
            videoView.setVideoSize(width, height);
            btnVideoVideoSwitchScree.setBackgroundResource(R.drawable.btn_video_switch_scree_full_selector);
            setVideoType(FULL_SCREEN);
        }
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        if (myReceiver != null)
            unregisterReceiver(myReceiver);
        myReceiver = null;
        super.onDestroy();

    }

    private void initData() {
        myReceiver = new MyReceiver();
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        currentVoice = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        maxVoice = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(myReceiver, intentFilter);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
    }

    /**
     * Sets battery.
     *
     * @param battery the battery
     */
    public void setBattery(int battery) {
        if (battery <= 0) {
            ivBattery.setImageResource(R.drawable.ic_battery_0);
        } else if (battery <= 10) {
            ivBattery.setImageResource(R.drawable.ic_battery_10);
        } else if (battery <= 20) {
            ivBattery.setImageResource(R.drawable.ic_battery_20);
        } else if (battery <= 40) {
            ivBattery.setImageResource(R.drawable.ic_battery_40);
        } else if (battery <= 60) {
            ivBattery.setImageResource(R.drawable.ic_battery_60);
        } else if (battery <= 80) {
            ivBattery.setImageResource(R.drawable.ic_battery_80);
        } else if (battery <= 100) {
            ivBattery.setImageResource(R.drawable.ic_battery_100);
        } else {
            ivBattery.setImageResource(R.drawable.ic_battery_100);
        }


    }

    /**
     * Gets system time.
     *
     * @return the system time
     */
    public String getSystemTime() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.format(new Date());
    }

    /**
     * Sets video type.
     *
     * @param videoType the video type
     */
    public void setVideoType(int videoType) {
        switch (videoType) {
            case FULL_SCREEN:
                isFullScreen = true;
                break;
            case DEFAULT_SCREEN:
                isFullScreen = false;
                break;
        }
    }


    /**
     * The type My receiver.
     */
    class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra("level", 0);
            Log.e("Mess", "onReceive: 电量=" + level);
            setBattery(level);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                handler.removeMessages(HIDE_MEDIACONTROLLER);
                mVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                touchRang = Math.min(screenHeight, screenWidth);
                startY = (int) event.getY();
             //   Log.e("Mess", "onTouchEvent:  DOWN");
                break;
            case MotionEvent.ACTION_MOVE:

                distanceY = startY - (int) event.getY();
                int delta = (int) (((float) distanceY / (float) touchRang) * (float) maxVoice);
                int voice = Math.min(Math.max(mVol + delta, 0), maxVoice);
                if (delta != 0) {
                    isMute = false;
                    updataVoice(voice, isMute);
                }
            /*    Log.e("Mess", "onTouchEvent:  MOVE distanceY" + distanceY);
                Log.e("Mess", "onTouchEvent:  MOVE touchRang" + touchRang);
                Log.e("Mess", "onTouchEvent:  MOVE maxVoice" + maxVoice);
                Log.e("Mess", "onTouchEvent:  MOVE dalta" + delta);*/
                break;
            case MotionEvent.ACTION_UP:
             //   Log.e("Mess", "onTouchEvent:  UP");
                distanceY = 0;
                handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER, 4000);
                break;
        }


        return super.onTouchEvent(event);
    }

    private void showMediaController() {
        media_controller.setVisibility(View.VISIBLE);
        isshowMediaController = true;
    }

    private void hideMediaController() {
        media_controller.setVisibility(View.GONE);
        isshowMediaController = false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            currentVoice--;
            isMute = false;
            updataVoice(currentVoice, isMute);
            handler.removeMessages(HIDE_MEDIACONTROLLER);
            handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER, 4000);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            currentVoice++;
            isMute = false;
            updataVoice(currentVoice, isMute);
            handler.removeMessages(HIDE_MEDIACONTROLLER);
            handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER, 4000);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
