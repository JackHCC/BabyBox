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

public class OlderAdapter extends RecyclerView.Adapter<OlderAdapter.ViewHolder>{
    private Context mContext;
    private List<String> id;
    private List<String> name;
    private List<String> birthday;
    private List<String> sex;
    private List<String> emephone;


    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView oldername;
        TextView olderbirthday;
        TextView oldersex;
        TextView olderemephone;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            oldername = (TextView) itemView.findViewById(R.id.name);
            olderbirthday = (TextView) itemView.findViewById(R.id.birthday);
            oldersex = (TextView) itemView.findViewById(R.id.sex);
            olderemephone = (TextView) itemView.findViewById(R.id.emephone);
        }
    }

    public OlderAdapter(List<String> idList,List<String> nameList,List<String> birthdayList,List<String> sexList,List<String> emephoneList){
        id = idList;
        name = nameList;
        birthday = birthdayList;
        sex = sexList;
        emephone = emephoneList;
        System.out.println(emephone);
    }

    @Override
    public OlderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_older_item,parent,false);
        return new OlderAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final OlderAdapter.ViewHolder holder, final int position) {
        final String sid = id.get(position);
        String sname = name.get(position);
        String sbirthday = birthday.get(position);
        String ssex = sex.get(position);
        String semephone = emephone.get(position);
        holder.oldername.setText(sname);
        holder.olderbirthday.setText(sbirthday);
        holder.oldersex.setText(ssex);
        holder.olderemephone.setText(semephone);
    }

    @Override
    public int getItemCount() {
        if (name == null)
            return 0;
        else
            return name.size();
    }
}
