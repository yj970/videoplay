package cn.yj.videoplay.fragment;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.yj.videoplay.Constant;
import cn.yj.videoplay.R;
import cn.yj.videoplay.event.VideoPrepareEvent;

/**
 * Created by yangjie on 2019/3/30.
 */

public class VideoFragment extends Fragment implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnInfoListener,
        MediaPlayer.OnSeekCompleteListener,
        MediaPlayer.OnVideoSizeChangedListener {

    @BindView(R.id.sv)
    SurfaceView surfaceView;
    @BindView(R.id.iv)
    ImageView iv;
    Unbinder unbinder;
    private MediaPlayer mediaPlayer;
    private MyHandler handler;// 用于更新时间
    private String filePath; // 视频路径
    private boolean init = true;// 初始化
    private int currTime;
    private int videoDuration=-1;// 影片持续播放时间
    private int startTime = 0;// 开始播放时间
    private boolean isPlayComplete;// 是否播放完成

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, null, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    public static VideoFragment newInstance(String filePath) {
        Bundle args = new Bundle();
        VideoFragment fragment = new VideoFragment();
        args.putString(Constant.FILE_PATH, filePath);
        fragment.setArguments(args);
        return fragment;
    }

    /***
     *
     * @param filePath
     * @param duration 影片播放持续时间
     * @return
     */
    public static VideoFragment newInstance(String filePath, int duration) {
        Bundle args = new Bundle();
        VideoFragment fragment = new VideoFragment();
        args.putString(Constant.FILE_PATH, filePath);
        args.putInt(Constant.FILE_DURATION, duration);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        filePath = (String) getArguments().get(Constant.FILE_PATH);
        if (getArguments().get(Constant.FILE_DURATION) != null) {
            videoDuration = (int) getArguments().get(Constant.FILE_DURATION);
        }
        File file = new File(filePath);
        if (filePath == null || !file.exists()) {
            // 无法播放视频
            Toast.makeText(getActivity(), "播放视频失败", Toast.LENGTH_LONG).show();
            return;
        }

        initHandler();
        initSurfaceView();
        initMediaPlayer();
    }

    public void seekTo(int startTime) {
        this.startTime = startTime;
        mediaPlayer.seekTo(startTime);
    }


    class MyHandler extends Handler {
        WeakReference<VideoFragment> weakReference;

        public MyHandler(VideoFragment fragment) {
            this.weakReference = new WeakReference<VideoFragment>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            this.sendEmptyMessageDelayed(0, 500);
            if (weakReference.get() != null) {
                if (weakReference.get().mediaPlayer != null && weakReference.get().mediaPlayer.isPlaying()) {
                    weakReference.get().updateTime();
                }
            }
        }
    }

    private void initHandler() {
        handler = new MyHandler(this);
    }

    private void initMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnInfoListener(this);
        mediaPlayer.setOnSeekCompleteListener(this);
        mediaPlayer.setOnVideoSizeChangedListener(this);
//
//        String basePath = Environment.getExternalStorageDirectory().getPath() + File.separator + "testVideo" + File.separator;
//        String path = basePath + "test.mp4";// 本地视频


        try {
            mediaPlayer.setDataSource(filePath);// 本地视频
            //mediaPlayer.setDataSource(path);// 网络视频
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void initSurfaceView() {
        surfaceView.setZOrderOnTop(false);// surfaceView是否置于最顶层，默认是false；若设为true，则会挡住其他View，即使在布局中其他View在Surface之上。
        surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);// 表明该Surface不包含原生数据，Surface用到的数据由其他对象提供 （https://blog.csdn.net/ShanYu1198124123/article/details/52448586）
        surfaceView.getHolder().addCallback(this);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.sv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sv:
                if (isPlayComplete && !mediaPlayer.isPlaying()) {
                    rePlay();
                } else {
                    playOrPause();
                }
                break;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (init) {
            mediaPlayer.prepareAsync();
        }
        mediaPlayer.setDisplay(holder);
        if (!init) {
            mediaPlayer.seekTo(currTime);
        }
        init = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        isPlayComplete = true;
        iv.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        int totalVideoTime = mediaPlayer.getDuration();// 片长，毫秒级
        EventBus.getDefault().post(new VideoPrepareEvent(totalVideoTime));
        updateTime();
        mp.start();// 开始播放视频
        startRecordTime();// 开始获取播放时间
    }

    private void startRecordTime() {
        handler.sendEmptyMessage(0);
    }


    private void updateTime() {
        currTime = mediaPlayer.getCurrentPosition();//当前播放的时间
        checkDuration();
//        tvLength.setText("播放进度：" + TimeUtil.getTime(currTime) + "/" + TimeUtil.getTime(endTime));
    }

    /**
     * 判断是否已达最大播放时长
     */
    private void checkDuration() {
        if (videoDuration!=-1) {
            if (currTime - startTime >= videoDuration) {
                mediaPlayer.pause();
                isPlayComplete = true;
                iv.setVisibility(View.VISIBLE);
            } else {
                isPlayComplete = false;
            }
        }
    }

    // 播放or暂停
    // 如果视频处于播放完成状态，调用此方法会进行重播
    private void playOrPause() {
        if (!mediaPlayer.isPlaying()) {
            iv.setVisibility(View.GONE);
            mediaPlayer.start();
        } else {
            mediaPlayer.pause();
            iv.setVisibility(View.VISIBLE);
        }
    }

    // 重播
    private void rePlay() {
        mediaPlayer.seekTo(startTime);
        playOrPause();
    }


    @Override
    public void onSeekComplete(MediaPlayer mp) {

    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {

    }

    @Override

    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // 相当于onResume()方法

        } else {
            // 相当于onpause()方法
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                iv.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        mediaPlayer.release();
        mediaPlayer = null;
    }
}
