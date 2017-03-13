package com.waibao.team.tuyou.activity;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.config.Config;
import com.waibao.team.tuyou.utils.FilesUtil;
import com.waibao.team.tuyou.utils.StringUtil;
import com.waibao.team.tuyou.utils.ToastUtil;
import com.waibao.team.tuyou.utils.UserUtil;
import com.waibao.team.tuyou.widget.MyDialog;
import com.waibao.team.tuyou.widget.ToolBarBuilder;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class IdentityActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.img_zheng)
    ImageView imgZheng;
    @Bind(R.id.img_fan)
    ImageView imgFan;
    @Bind(R.id.et_name)
    EditText et_name;
    @Bind(R.id.et_code)
    EditText et_code;
    private boolean isPic1Ok = false;
    private boolean isPic2Ok = false;
    private String filepath1;
    private String filepath2;
    private Bitmap bm;
    private String name;
    private String code;
    String TAG = "IdentityActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        ToolBarBuilder builder = new ToolBarBuilder(this, toolbar);
        builder.setTitle("身份验证").build();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_identity;
    }

    @OnClick({R.id.img_zheng, R.id.img_fan, R.id.ensure})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.img_zheng:
                intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(intent, "选择图片"), 1);
                break;
            case R.id.img_fan:
                intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(intent, "选择图片"), 2);
                break;
            case R.id.ensure:
                name = et_name.getText().toString();
                code = et_code.getText().toString();
                if (StringUtil.isEmpty(name) || StringUtil.isEmpty(code)) {
                    ToastUtil.showToast("姓名或身份证号码为空");
                    return;
                }
                if (code.length() != 18) {
                    ToastUtil.showToast("身份证号码为18位");
                    return;
                }
                if (!isPic1Ok) {
                    ToastUtil.showToast("请点击第一张图片进行拍摄或选择");
                    return;
                }
                if (!isPic2Ok) {
                    ToastUtil.showToast("请点击第二张图片进行拍摄或选择");
                    return;
                }
                sendIdentity();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        Uri selectedImageUri = data.getData();
        bm = null;
//        ContentResolver resolver = getContentResolver();
        try {
//            bm = MediaStore.Images.Media.getBitmap(resolver, selectedImageUri);
//            bm = getBitmapFormUri(this, selectedImageUri);
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = managedQuery(selectedImageUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = FilesUtil.scal(cursor.getString(column_index));
            bm = BitmapFactory.decodeFile(path);
            if (requestCode == 1) {
                isPic1Ok = true;
                imgZheng.setImageBitmap(bm);
                filepath1 = path;
            } else if (requestCode == 2) {
                isPic2Ok = true;
                filepath2 = path;
                imgFan.setImageBitmap(bm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void sendIdentity() {
        final Dialog loadingdialog = new MyDialog().showLodingDialog(this);
        OkHttpUtils.post().url(Config.IP + "/user_verifyIdentity")
                .addParams("userId", UserUtil.user.getId())
                .addParams("IDCardNum", code)
                .addParams("trueName", name)
                .addFile("files", "img.jpg", new File(filepath1))
                .addFile("files", "img.jpg", new File(filepath2))
                .tag(TAG)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                ToastUtil.showNetError();
                if (null != loadingdialog) {
                    loadingdialog.dismiss();
                }
            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String messageinfo = jsonObject.get("messageInfo").toString();
                    if (null != loadingdialog) {
                        loadingdialog.dismiss();
                    }
                    if ("success".equals(messageinfo)) {
                        ToastUtil.showToast("发送成功，等待工作人员审核");
                        setResult(RESULT_OK);
                        IdentityActivity.this.finish();

                    } else if ("null".equals(messageinfo)) {
                        ToastUtil.showToast("身份证号码不能为空");
                    } else if ("error".equals(messageinfo)) {
                        ToastUtil.showToast("身份证号码有误");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag(TAG);
    }
}
