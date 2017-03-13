package com.waibao.team.tuyou.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.adapter.VoteListAdapter;
import com.waibao.team.tuyou.config.Config;
import com.waibao.team.tuyou.dto.VoteDto;
import com.waibao.team.tuyou.utils.DateUtil;
import com.waibao.team.tuyou.utils.JsonUtils;
import com.waibao.team.tuyou.utils.ToastUtil;
import com.waibao.team.tuyou.utils.UserUtil;
import com.waibao.team.tuyou.widget.ToolBarBuilder;
import com.waibao.team.tuyou.widget.WrapHeightListView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import okhttp3.Call;
import okhttp3.Request;

public class VoteActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.img_head)
    ImageView imgHead;
    @Bind(R.id.nick_name)
    TextView nickName;
    @Bind(R.id.time)
    TextView time;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.vote_list)
    WrapHeightListView voteList;
    @Bind(R.id.bt_vote)
    Button bt_vote;


    private List<Map<String, Object>> datas = new ArrayList<>();
    private VoteListAdapter adapter = new VoteListAdapter(datas);
    private VoteDto voteDto;
    private int pre_choose_pos = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        voteDto = JsonUtils.getObjectfromString(getIntent().getStringExtra("VoteDto"), VoteDto.class);
        initView();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_vote;
    }

    private void initData() {
        String[] content = voteDto.getContent().split(";");
        String[] total = voteDto.getTotal().split(";");
        for (int i = 0; i < content.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("is_check", false);
            map.put("title", content[i]);
            map.put("sum", total[i]);
            datas.add(map);
        }
    }

    private void initView() {
        ToolBarBuilder builder = new ToolBarBuilder(this, toolbar);
        builder.setTitle("投票详情").build();
        initData();
        voteList.setAdapter(adapter);
        voteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ToastUtil.showToast("" + position);
                if (pre_choose_pos != -1) {
                    View itemview = voteList.getChildAt(pre_choose_pos);
                    ((VoteListAdapter.ViewHolder) itemview.getTag()).voteCb.setChecked(false);
                    datas.get(pre_choose_pos).put("is_check", false);
                }
                ((VoteListAdapter.ViewHolder) view.getTag()).voteCb.setChecked(true);
                datas.get(position).put("is_check", true);
                pre_choose_pos = position;
            }
        });
        Glide.with(this).load(voteDto.getImgUrl()).bitmapTransform(new CropCircleTransformation(this))
                .into(imgHead);
        nickName.setText(voteDto.getNickname());
        time.setText(DateUtil.getStringByFormat(voteDto.getPostDate(), DateUtil.dateFormatYMDHMS));
        title.setText(voteDto.getTitle());

    }

    @OnClick(R.id.bt_vote)
    public void onClick() {
        final int check_pos = getCheckedPositon();
        if (check_pos == -1) {
            ToastUtil.showToast("请选择一项投票吧！");
            return;
        }
        voteDto.setTotal(getTotalString(check_pos));
        OkHttpUtils.get().url(Config.IP + "/group_voteForGroup")
                .addParams("uid", UserUtil.user.getId())
                .addParams("voteDto", JsonUtils.getJsonStringformat(voteDto))
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                ToastUtil.showToast("网络访问出错");
            }

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                Log.e("LLLLL", "onBefore: " + request.url().toString());
            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String messageInfo = jsonObject.get("messageInfo").toString();
                    if ("成功".equals(messageInfo)) {
                        adapter.setIsChoose(check_pos);
                        bt_vote.setVisibility(View.GONE);
                    }
                    ToastUtil.showToast(messageInfo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 获得选中的项
     *
     * @return
     */
    private int getCheckedPositon() {
        for (int i = 0; i < datas.size(); i++) {
            if ((Boolean) datas.get(i).get("is_check")) {
                return i;
            }
        }
        return -1;
    }

    private String getTotalString(int pos) {
        String s = "";
        for (int i = 0; i < datas.size(); i++) {
            if (i == pos) {
                s += (Integer.parseInt(datas.get(i).get("sum").toString()) + 1) + ";";
            } else {
                s += datas.get(i).get("sum") + ";";
            }
        }
        Log.e("LLLLL", s);
        return s;
    }
}
