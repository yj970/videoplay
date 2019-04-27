package cn.yj.videoplay.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class MySeekBar extends LinearLayout{
    private Context context;
    TextView textView;
    SeekBar seekBar;
    int width;
    ISeekBarChangeListener iSeekBarChangeListenerImpl;
    String text="00:00:00~00:00:15";

    public MySeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        textView = new TextView(context);
        textView.setText(text);
        seekBar = new SeekBar(context);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (iSeekBarChangeListenerImpl != null) {
                    iSeekBarChangeListenerImpl.onProgressChanged(seekBar, progress, fromUser);
                }
                // 绘制TextView
                LayoutParams params = (LayoutParams) textView.getLayoutParams();
                int left = progress*(width-260)/100;// todo 这里不精准，260为预判值。
                params.setMargins(left,0,0,0);
                textView.setLayoutParams(params);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        addView(textView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        addView(seekBar, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
    }

    public void setText(String text) {
        this.text = text;
        textView.setText(text);
    }

    public interface ISeekBarChangeListener {
        void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser);
    }

    public void setSeekBarChangeListenerImpl(ISeekBarChangeListener iSeekBarChangeListenerImpl) {
        this.iSeekBarChangeListenerImpl = iSeekBarChangeListenerImpl;
    }
}
