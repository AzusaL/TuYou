package com.waibao.team.tuyou.adapter;

import android.animation.ObjectAnimator;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.listener.OnItemClickListener;
import com.waibao.team.tuyou.utils.ConstanceUtils;
import com.waibao.team.tuyou.utils.DisplayUtil;

import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Delete_exe on 2016/6/6.
 */
public class VoteListAdapter extends BaseAdapter {
    private List<Map<String, Object>> list;
    private Boolean isChoose = false;

    public VoteListAdapter(List<Map<String, Object>> list) {
        this.list = list;
    }

    public void setIsChoose(int pos) {
        isChoose = true;
        list.get(pos).put("is_check", true);
        list.get(pos).put("sum", Integer.parseInt(list.get(pos).get("sum").toString()) + 1);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (null == convertView) {
            convertView = View.inflate(parent.getContext(), R.layout.item_vote, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (isChoose) {
            viewHolder.voteCb.setVisibility(View.GONE);
            int width = getVoteBackground(Integer.parseInt(list.get(position).get("sum").toString()));
            viewHolder.color_background.setLayoutParams(new RelativeLayout.LayoutParams(
                    width, RelativeLayout.LayoutParams.MATCH_PARENT));
            ObjectAnimator.ofFloat(viewHolder.color_background, "translationX", -width, 0).setDuration(200).start();


        } else {
            viewHolder.voteCb.setChecked((boolean) list.get(position).get("is_check"));
        }
        viewHolder.voteMenu.setText((String) list.get(position).get("title"));
        viewHolder.voteSum.setText(list.get(position).get("sum") + "票");
        return convertView;
    }

    /**
     * 计算设置背景颜色宽度
     */
    public int getVoteBackground(int vote_sum) {
        float allVoteSum = 0;
        for (int i = 0; i < list.size(); i++) {
            allVoteSum += Integer.parseInt(list.get(i).get("sum").toString());
        }
        float scale = vote_sum / allVoteSum;
        int width = (int) (scale * ConstanceUtils.screenWidth - DisplayUtil.dip2px(32));
        return width;
    }

    public static class ViewHolder {
        @Bind(R.id.vote_cb)
        public CheckBox voteCb;
        @Bind(R.id.vote_menu)
        TextView voteMenu;
        @Bind(R.id.vote_sum)
        TextView voteSum;
        @Bind(R.id.color_background)
        View color_background;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
