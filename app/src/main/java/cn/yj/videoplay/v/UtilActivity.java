package cn.yj.videoplay.v;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.yj.videoplay.Contract;
import cn.yj.videoplay.R;
import cn.yj.videoplay.adapter.UtilAdapter;

/**
 * Created by yangjie on 2019/3/30.
 */

public class UtilActivity extends Activity implements Contract.UtilView {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.rv)
    RecyclerView rv;
    UtilAdapter adapter;

    public static void startUtilActivity(Activity activity) {
        activity.startActivity(new Intent(activity, UtilActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_util);
        ButterKnife.bind(this);


        initView();
        setListener();

    }

    private void initView() {
        tvTitle.setText("工具");

        List<String> data = new ArrayList<>();
        data.add("剪切音视频");
        data.add("抽出视频");
        data.add("抽出音频");
        data.add("音视频合成");
        data.add("混合音频");
        data.add("音视频制作");
        adapter = new UtilAdapter(data);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
    }

    public void setListener() {
        adapter.setiClick(new UtilAdapter.IClick() {
            @Override
            public void onClick(int position) {
                switch (position) {
                    case 0:
                        ClipVideoActivity.startClipVideoActivity(UtilActivity.this);
                        break;
                }
            }
        });
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
