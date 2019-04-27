package cn.yj.videoplay.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.yj.videoplay.R;

/**
 * Created by yangjie on 2019/3/30.
 */

public class UtilAdapter extends RecyclerView.Adapter<UtilAdapter.ViewHolder>{
    List<String> data;
    IClick iClick;

    public UtilAdapter(List<String> data) {
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_util, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tv.setText(data.get(position));
        holder.tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iClick != null) {
                    iClick.onClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv)
        TextView tv;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface IClick {
        void onClick(int position);
    }

    public void setiClick(IClick iClick) {
        this.iClick = iClick;
    }
}
