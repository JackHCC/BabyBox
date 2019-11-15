package com.jack.carebaby.utils;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jack.carebaby.R;

import java.util.List;

public class WHeightAdapter extends RecyclerView.Adapter<WHeightAdapter.ViewHolder>{
    private Context mContext;
    private List<String> id;
    private List<String> weight;
    private List<String> height;
    private List<String> time;


    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView babyweight;
        TextView babyheight;
        TextView babytime;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            babyweight = (TextView) itemView.findViewById(R.id.weight);
            babyheight = (TextView) itemView.findViewById(R.id.height);
            babytime = (TextView) itemView.findViewById(R.id.time);
        }
    }

    public WHeightAdapter(List<String> idList,List<String> weightList,List<String> heightList,List<String> timeList){
        id = idList;
        weight = weightList;
        height = heightList;
        time = timeList;
    }

    @Override
    public WHeightAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_wheight_item,parent,false);
        return new WHeightAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final WHeightAdapter.ViewHolder holder, final int position) {
        final String sid = id.get(position);
        String sweight = weight.get(position);
        String sheight = height.get(position);
        String stime = time.get(position);
        holder.babyweight.setText(sweight+"kg");
        holder.babyheight.setText(sheight+"cm");
        holder.babytime.setText(stime);
    }

    @Override
    public int getItemCount() {
        if (id == null)
            return 0;
        else
            return id.size();
    }
}
