package com.upc.help_system.model;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.upc.help_system.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/4/6.
 */

public class HelpInfoAdapter extends RecyclerView.Adapter {
    @BindView(R.id.content_show)
    TextView contentShow;
    @BindView(R.id.category_show)
    TextView categoryShow;
    @BindView(R.id.tips_show)
    TextView tipsShow;
    @BindView(R.id.deadline_show)
    TextView deadlineShow;
    @BindView(R.id.time)
    TextView time;
    private List<HelpInfo> helpInfoList;

    public HelpInfoAdapter(List<HelpInfo> list) {
        helpInfoList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.helpinfo_layout, null);
        ButterKnife.bind(parent);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new HelpInfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        HelpInfoViewHolder holder1 = (HelpInfoViewHolder) holder;
        holder1.position = position;
        HelpInfo helpInfo = helpInfoList.get(position);
        contentShow.setText(helpInfo.getContent());
        tipsShow.setText((int) helpInfo.getTip());
        categoryShow.setText(helpInfo.getCategory());
        deadlineShow.setText(helpInfo.getDeadline());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class HelpInfoViewHolder extends RecyclerView.ViewHolder {
        public View rootView;
        public TextView content;
        public int position;
        public HelpInfoViewHolder(View itemView) {
            super(itemView);

        }
    }
}
