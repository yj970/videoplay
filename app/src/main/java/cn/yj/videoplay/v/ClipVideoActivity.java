package cn.yj.videoplay.v;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.yj.videoplay.R;
import cn.yj.videoplay.event.VideoPrepareEvent;
import cn.yj.videoplay.fragment.VideoFragment;
import cn.yj.videoplay.util.TimeUtil;
import cn.yj.videoplay.view.MySeekBar;

/**
 * Created by yangjie on 2019/3/30.
 */

public class ClipVideoActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_back)
    ImageView ivBack;

    @BindView(R.id.sb)
    MySeekBar sb;
    VideoFragment videoFragment;
    boolean seekBarCanTouch = false;
    int totalVideoTime;
    final int clipDuration = 1000*15;

    public static void startClipVideoActivity(Activity activity) {
        activity.startActivity(new Intent(activity, ClipVideoActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clip_video);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);


        tvTitle.setText("剪辑音视频");

        init();
        setListener();

    }

    private void setListener() {
        sb.setSeekBarChangeListenerImpl(new MySeekBar.ISeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (!seekBarCanTouch) {
                    return;
                }
                int startTime = progress*(totalVideoTime-clipDuration)/100;
                sb.setText(TimeUtil.getTime(startTime)+"~"+TimeUtil.getTime(startTime+clipDuration));
                videoFragment.seekTo(startTime);
            }
        });
    }

    private void init() {
        String testPath = Environment.getExternalStorageDirectory().getPath() + File.separator + "1.mp4";
        videoFragment = VideoFragment.newInstance(testPath, clipDuration);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.content, videoFragment);
        transaction.commit();
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }

    @Subscribe
    public void acceptVideoPreparedEvent(VideoPrepareEvent event) {
        totalVideoTime = event.getTotalVideoTime();
        seekBarCanTouch = true;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
