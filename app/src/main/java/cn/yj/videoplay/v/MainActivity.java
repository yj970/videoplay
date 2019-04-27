package cn.yj.videoplay.v;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.yj.videoplay.Contract;
import cn.yj.videoplay.R;
import cn.yj.videoplay.fragment.MineFragment;
import cn.yj.videoplay.fragment.VideoFragment;

public class MainActivity extends AppCompatActivity implements Contract.MainView {
    FragmentPagerAdapter adapter;
    List<Fragment> mList;
    @BindView(R.id.id_viewpager)
    ViewPager mViewPager;
    @BindView(R.id.tv_main)
    TextView tvMain;
    @BindView(R.id.tv_me)
    TextView tvMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        String testPath = Environment.getExternalStorageDirectory().getPath() + File.separator + "1.mp4";
//        FragmentManager manager = getSupportFragmentManager();
//        FragmentTransaction transaction = manager.beginTransaction();
//        transaction.replace(R.id.video, VideoFragment.newInstance(testPath));
//        transaction.commit();

        mList = new ArrayList<Fragment>();
        mList.add(VideoFragment.newInstance(testPath));
        mList.add(MineFragment.newInstance());
        adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return mList.size();
            }

            @Override
            public Fragment getItem(int position) {
                return mList.get(position);
            }
        };

        mViewPager.setAdapter(adapter);
    }

    @OnClick({R.id.tv_main, R.id.tv_me})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_main:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.tv_me:
                mViewPager.setCurrentItem(1);
                break;
        }
    }
}
