package com.waibao.team.tuyou.tripgrounp_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.activity.PersonPageActivity;
import com.waibao.team.tuyou.callback.UserCountsCallback;
import com.waibao.team.tuyou.config.Config;
import com.waibao.team.tuyou.dto.UserCountDto;
import com.waibao.team.tuyou.utils.ConstanceUtils;
import com.waibao.team.tuyou.utils.StringUtil;
import com.waibao.team.tuyou.utils.ToastUtil;
import com.waibao.team.tuyou.widget.MyDialog;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.PieChartView;
import okhttp3.Call;

/**
 * Created by Azusa on 2016/5/6.
 * 团数据统计页面
 */
public class StatisticsFragment extends Fragment {
    @Bind(R.id.age_statistics)
    PieChartView mAgeStatistics;
    @Bind(R.id.reputation_statistics)
    ColumnChartView mReputationStatistics;
    @Bind(R.id.img_head)
    ImageView mImgHead;
    @Bind(R.id.tv_name)
    TextView mTvName;
    @Bind(R.id.tv_statistics)
    TextView mTvStatistics;
    @Bind(R.id.sex_statistics)
    PieChartView mSexStatistics;
    private View view;
    private List<UserCountDto> datas = new ArrayList<>();  //统计数据
    private AlertDialog dialog;
    private boolean isInited = false;  //是否初始化过
    private String gId; //团id
    private int currentPosition = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tripgroup_statistics, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !isInited) {
            initView();
            isInited = true;
        }
    }

    private void initView() {
        gId = getArguments().getString("gId");
        dialog = new MyDialog().showLodingDialog(getActivity());
        mImgHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PersonPageActivity.class);
                intent.putExtra("uId", datas.get(currentPosition).getUid());
                startActivity(intent);
            }
        });
        getData();
    }

    private void getData() {

        OkHttpUtils.get().url(Config.IP + "/group_countGroupDetails")
                .addParams("gid", gId)
                .build().execute(new UserCountsCallback() {
            @Override
            public void onError(Call call, Exception e) {
                ToastUtil.showToast("网络链接出错");
                dialog.cancel();
            }

            @Override
            public void onResponse(List<UserCountDto> response) {
                datas.addAll(response);
                generateReputationData();
                generateAgeData();
                generateSexData();
                dialog.cancel();
                initHead();
            }
        });

    }

    private void initHead() {
        Glide.with(this)
                .load(Config.Pic + datas.get(0).getImgUrl())
                .bitmapTransform(new CropCircleTransformation(ConstanceUtils.CONTEXT))
                .into(mImgHead);
        mTvName.setText(datas.get(0).getNickname() + "：");
    }

    private void generateSexData() {

        int boy = 0, girl = 0;
        for (int i = 0; i < datas.size(); i++) {
            if (StringUtil.isEmpty(datas.get(i).getSex())) {
                boy++;
                continue;
            }
            if (datas.get(i).getSex().equals("2")) {
                boy++;
            } else {
                girl++;
            }
        }

        float a = boy;
        float b = datas.size();
        List<SliceValue> values = new ArrayList<>();
        SliceValue sliceValue = new SliceValue((a / b) * 100, 0xff66cccc);

        float boyF = (a / b) * 100;
        sliceValue.setLabel("男 " + String.format("%.2f", boyF) + "%");
        values.add(sliceValue);

        float c = girl;
        SliceValue sliceValue2 = new SliceValue((c / b) * 100, 0xfff8bbd0);
        float girlF = (c / b) * 100;
        sliceValue2.setLabel("女 " + String.format("%.2f", girlF) + "%");
        values.add(sliceValue2);

        PieChartData data = new PieChartData(values);
        data.setHasLabels(true);
        data.setHasLabelsOnlyForSelected(false);
        data.setHasLabelsOutside(true);
        data.setHasCenterCircle(false);
        mSexStatistics.setPieChartData(data);
        mSexStatistics.setCircleFillRatio(0.7f);
        mSexStatistics.setChartRotationEnabled(false);
    }

    private void generateReputationData() {
        final List<UserCountDto> creditData = new ArrayList<>();
        creditData.addAll(datas);
        if (creditData.size() < 3) {
            creditData.add(new UserCountDto("", 0, "", "", "", 0));
            creditData.add(new UserCountDto("", 0, "", "", "", 0));
        }
        int numColumns = creditData.size();
        List<Column> columns = new ArrayList<>();
        List<SubcolumnValue> values;
        for (int i = 0; i < numColumns; ++i) {
            values = new ArrayList<>();
            SubcolumnValue value = new SubcolumnValue((float) creditData.get(i).getCredit(), ChartUtils.pickColor());
            value.setLabel(creditData.get(i).getNickname());
            values.add(value);

            Column column = new Column(values);
            column.setHasLabels(true);
            column.setHasLabelsOnlyForSelected(false);
            columns.add(column);
        }

        ColumnChartData data = new ColumnChartData(columns);

        Axis axisY = new Axis().setHasLines(true);
        data.setAxisYLeft(axisY);

        mReputationStatistics.setColumnChartData(data);
        mReputationStatistics.setValueSelectionEnabled(true);
        mReputationStatistics.setZoomEnabled(false);
        mReputationStatistics.setOnValueTouchListener(new ColumnChartOnValueSelectListener() {
            @Override
            public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {
                Glide.with(StatisticsFragment.this)
                        .load(Config.Pic + creditData.get(columnIndex).getImgUrl())
                        .bitmapTransform(new CropCircleTransformation(ConstanceUtils.CONTEXT))
                        .into(mImgHead);
                String name = new String(value.getLabelAsChars());
                mTvName.setText(name + "：");
                mTvStatistics.setText((int) value.getValue() + "");
                currentPosition = columnIndex;
            }

            @Override
            public void onValueDeselected() {
            }
        });

    }

    private void generateAgeData() {
        int numValues = 4;

        int ages[] = new int[4];

        for (int i = 0; i < datas.size(); i++) {
            int age = datas.get(i).getAge();
            if (age >= 18 && age <= 22) {
                ages[0]++;
            } else if (age > 22 && age <= 26) {
                ages[1]++;
            } else if (age > 26 && age <= 35) {
                ages[2]++;
            } else {
                ages[3]++;
            }
        }

        String[] labes = {"18~22", "23~26", "27~35", "35+"};

        List<SliceValue> values = new ArrayList<>();
        for (int i = 0; i < numValues; ++i) {
            float a = ages[i];
            float b = datas.size();
            SliceValue sliceValue = new SliceValue((a / b) * 100, ChartUtils.pickColor());
            sliceValue.setLabel(labes[i] + " " + String.format("%.2f", (a / b) * 100) + "%");
            values.add(sliceValue);
        }

        PieChartData data = new PieChartData(values);
        data.setHasLabels(true);
        data.setHasLabelsOnlyForSelected(false);
        data.setHasLabelsOutside(true);
        data.setHasCenterCircle(false);
        mAgeStatistics.setPieChartData(data);
        mAgeStatistics.setCircleFillRatio(0.7f);
        mAgeStatistics.setChartRotationEnabled(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
