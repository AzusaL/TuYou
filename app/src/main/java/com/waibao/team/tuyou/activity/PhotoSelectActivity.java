package com.waibao.team.tuyou.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.adapter.PhotoFloderLvAdapter;
import com.waibao.team.tuyou.adapter.PhotoSelectRvAdapter;
import com.waibao.team.tuyou.dto.ImageFloder;
import com.waibao.team.tuyou.event.AddPhotoEvent;
import com.waibao.team.tuyou.listener.PhotoRvItemClickLitener;
import com.waibao.team.tuyou.utils.ConstanceUtils;
import com.waibao.team.tuyou.utils.ToastUtil;
import com.waibao.team.tuyou.widget.MyDialog;
import com.waibao.team.tuyou.widget.ToolBarBuilder;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PhotoSelectActivity extends BaseActivity {

    @Bind(R.id.tv_foload)
    TextView tvFoload;
    @Bind(R.id.raw)
    View raw;
    @Bind(R.id.rl_category)
    RelativeLayout rlCategory;
    @Bind(R.id.btn_nest)
    Button btnNest;
    @Bind(R.id.photo_rv)
    RecyclerView img_rview;
    @Bind(R.id.titlerl)
    RelativeLayout titlerl;
    @Bind(R.id.img_count)
    TextView tv_imgcount;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.btn_back)
    Button mBtnBack;

    private PhotoSelectRvAdapter adapter; //图片显示的适配器
    private AlertDialog loding_dialog; //加载中dialog
    private File mImgDir;
    private HashSet<String> mDirPaths = new HashSet<String>();// 临时的辅助类，用于防止同一个文件夹的多次扫描
    private ArrayList<ImageFloder> mImageFloders = new ArrayList<ImageFloder>();// 扫描拿到所有的图片文件夹
    private int totallimgcount = 0;// 所有图片的数量
    private ArrayList<String> mImgs = new ArrayList<String>();// 各个文件夹中的所有图片路径
    private ArrayList<String> allimgs = new ArrayList<>(); //存所有图片路径的集合

    private int mScreenHeight;  //屏幕高度，用于调节popupwindow高度
    private PopupWindow popupWindow; //选择不同文件夹的对话框
    private View dirView; //popupwindow中显示的View
    private ListView dirLv; //popupwindow中显示文件夹的LV
    private PhotoFloderLvAdapter dir_adapter;  //文件夹的适配器
    private ArrayList<String> upload_urls = new ArrayList<>(); //被选中的所有图片URL集合
    private int what_count; //最多能选中的照片张数
    private File tempDir; //存放处理后的图片的临时文件夹
    private Uri cutimageUri, cameraimageUri;  //调用系统截图和相机处理图片后返回的Uri
    private File cameraimgforSD; //拍照后存储在sd卡上的临时图片文件

    private boolean isneedcutimg = false; //是否需要对图片进行裁剪

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (ConstanceUtils.MESSAGE_OK == msg.what) {
                loding_dialog.dismiss();
                GridLayoutManager gridLayoutManager = new GridLayoutManager(ConstanceUtils.CONTEXT, 3);
                img_rview.setLayoutManager(gridLayoutManager);
                img_rview.setHasFixedSize(true);
                mImgs.addAll(allimgs);
                adapter = new PhotoSelectRvAdapter(mImgs, upload_urls);
                img_rview.setAdapter(adapter);
                setListener();
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        new ToolBarBuilder(this, toolbar).setCanBack(false).setTitle("").build();
        initData();
        getImgUrl();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_photo_select;
    }

    private void initData() {
        Intent intent = getIntent();
        what_count = intent.getIntExtra("pic_count", 0);
        isneedcutimg = intent.getBooleanExtra("isneedcutimg", false);
        if (!isneedcutimg) {
            upload_urls.addAll(intent.getStringArrayListExtra("selectimglist"));
        }
        mImgs.add(""); //第一张图的位置放照相机按钮
        tempDir = new File(Environment.getExternalStorageDirectory() + "/tuyou");
        if (!tempDir.exists()) {
            tempDir.mkdir();
        }
        cutimageUri = Uri.parse("file://" + tempDir.getAbsolutePath() + "/tempimg" + System.currentTimeMillis() + ".jpg");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ConstanceUtils.TAKEPHOTO_REQUESTCODE:
                //如果返回的uri不为空，且照片已经存在sd上，则调用系统截图
                if (cameraimageUri != null && cameraimgforSD.exists()) {
                    if (isneedcutimg) {
                        cutimg(cameraimageUri);
                    } else {
                        upload_urls.add("file://" + cameraimageUri.getPath());
                        EventBus.getDefault().post(new AddPhotoEvent(upload_urls));
                        finish();
                    }
                } else {
                    //如果拍完照片后没点击确认，而是点击返回，照片已经存在sd卡上，将其删除
                    if (cameraimgforSD.exists()) {
                        cameraimgforSD.delete();
                    }
                }
                break;
            case ConstanceUtils.CUT_PHOTO_REQUESTCODE:
                if (cutimageUri != null && data != null) {
                    upload_urls.clear();
                    upload_urls.add("file://" + cutimageUri.getPath());
                    EventBus.getDefault().post(new AddPhotoEvent(upload_urls));
                    finish();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //调用系统截图，返回截图处理后的图片Uri
    private void cutimg(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", true);
        intent.putExtra("aspectX", 1);  //截图框的长宽比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300); //生成的图片分辨率
        intent.putExtra("outputY", 300);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false); //不返回bitmap数据
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cutimageUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, ConstanceUtils.CUT_PHOTO_REQUESTCODE);
    }

    private void setListener() {
        adapter.setOnItemClickLitener(new PhotoRvItemClickLitener() {
            @Override
            public void onItemClick(View view, int position, String url, Boolean bool) {
                //position为0是打开相机拍照
                if (0 == position) {
                    //在点击相机按钮时，图片需有至少一个可选个数
                    if (what_count <= upload_urls.size()) {
                        ToastUtil.showToast_center("已超过限制张数");
                        return;
                    }
                    //相机拍照完存储图片的路径
                    String filedir = tempDir.getAbsolutePath() + "/camera"
                            + System.currentTimeMillis() + ".jpg";
                    //相机拍照完返回图片uri
                    cameraimageUri = Uri.parse("file://" + filedir);
                    cameraimgforSD = new File(filedir);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra("return-data", false);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraimageUri);
                    startActivityForResult(intent, ConstanceUtils.TAKEPHOTO_REQUESTCODE);
                } else {
                    //bool：图片是否被选中
                    if (bool) {
                        //如果图片被选中，且集合中未添加该url，则添加到集合中
                        if (!upload_urls.contains(url)) {
                            upload_urls.add(url);
                        }
                    } else {
                        //如果图片不是被选中，且集合中已经添加了该url，则将该URL从集合中删除
                        if (upload_urls.contains(url)) {
                            upload_urls.remove(url);
                        }
                    }

                    //判断文件选中个数
                    if (upload_urls.size() > what_count) {
                        tv_imgcount.setText("" + (what_count - upload_urls.size()));
                        tv_imgcount.setTextColor(Color.parseColor("#ff3300"));
                    } else {
                        tv_imgcount.setText(upload_urls.size() + "/" + what_count);
                        tv_imgcount.setTextColor(Color.parseColor("#ffffff"));
                    }
                    tv_imgcount.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    //扫描图片前发送广播更新图片数据(安卓5.0之后这个方法无效。。。)
    private void updateGallery() {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        this.sendBroadcast(intent);
    }

    //获取手机中所有图片的url
    private void getImgUrl() {
        updateGallery();
        loding_dialog = new MyDialog().showLodingDialog(PhotoSelectActivity.this);
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            loding_dialog.dismiss();
            ToastUtil.showToast_center("没有SD卡");
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                String firstImage = null;
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = PhotoSelectActivity.this.getContentResolver();
                // 只查询jpeg和png的图片
                Cursor mCursor = mContentResolver.query(mImageUri, null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[]{"image/jpeg", "image/png"}, MediaStore.Images.Media.DATE_MODIFIED);
                mCursor.moveToLast();
                while (mCursor.moveToPrevious()) {
                    // 获取图片的路径
                    String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    allimgs.add(path);
                    totallimgcount++;
                    // 拿到第一张图片的路径
                    if (firstImage == null)
                        firstImage = path;
                    // 获取该图片的父路径名
                    File parentFile = new File(path).getParentFile();
                    if (parentFile == null)
                        continue;
                    String dirPath = parentFile.getAbsolutePath();
                    ImageFloder imageFloder = null;
                    // 利用一个HashSet防止多次扫描同一个文件夹（不加这个判断，图片多起来还是相当恐怖的~~）
                    if (mDirPaths.contains(dirPath)) {
                        continue;
                    } else {
                        mDirPaths.add(dirPath);
                        // 初始化imageFloder
                        imageFloder = new ImageFloder();
                        imageFloder.setDir(dirPath);
                        imageFloder.setFirstImagePath(path);
                    }
                    if (parentFile.list() == null)
                        continue;
                    int picSize = parentFile.list(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String filename) {
                            if (filename.endsWith(".jpg") || filename.endsWith(".png") || filename.endsWith(".jpeg"))
                                return true;
                            return false;
                        }
                    }).length;
                    imageFloder.setCount(picSize);
                    mImageFloders.add(imageFloder);

                }
                mCursor.close();
                if (totallimgcount > 0) {
                    ImageFloder firstimageFloder = new ImageFloder();
                    firstimageFloder.setName("最近图片");
                    firstimageFloder.setCount(totallimgcount);
                    firstimageFloder.setFirstImagePath(mImageFloders.get(0).getFirstImagePath());
                    firstimageFloder.setSelected(true);
                    mImageFloders.add(0, firstimageFloder);
                }
                // 扫描完成，辅助的HashSet设为null,释放内存
                mDirPaths = null;
                // 通知Handler扫描图片完成
                handler.sendEmptyMessage(ConstanceUtils.MESSAGE_OK);
            }
        }).start();

    }

    //初始化展示文件夹的popupWindw
    private void showPopupWindw() {
        if (popupWindow == null) {
            //获取屏幕高度
            mScreenHeight = ConstanceUtils.screenHight;

            dirView = View.inflate(this, R.layout.photo_floder_layout, null);
            popupWindow = new PopupWindow(dirView, ViewGroup.LayoutParams.MATCH_PARENT, mScreenHeight * 3 / 5);

            dirLv = (ListView) dirView.findViewById(R.id.floder_photo_lv);
            dir_adapter = new PhotoFloderLvAdapter(mImageFloders);
            dirLv.setAdapter(dir_adapter);
        }
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
        popupWindow.showAsDropDown(toolbar);
        popupWindow.update();
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                raw.setRotation(0);
            }
        });
        dirLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (0 == position) {
                    tvFoload.setText("最近图片");
                    mImgs.clear();
                    mImgs.addAll(allimgs);
                    mImgs.add(0, ""); //第一张图的位置放照相机按钮
                    adapter.notifyDataSetChanged();
                    popupWindow.dismiss();
                    return;
                }
                tvFoload.setText(mImageFloders.get(position).getName().replace("/", ""));
                mImgDir = new File(mImageFloders.get(position).getDir());
                List<String> list = Arrays.asList(mImgDir.list(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String filename) {
                        if (filename.endsWith(".jpg") || filename.endsWith(".png") || filename.endsWith(".jpeg"))
                            return true;
                        return false;
                    }
                }));
                mImgs.clear();
                for (int i = 0; i < list.size(); i++) {
                    mImgs.add(mImageFloders.get(position).getDir() + "/" + list.get(i));
                }
                Collections.reverse(mImgs); //倒序操作，让最新添加的图片显示在前面
                mImgs.add(0, ""); //第一张图的位置放照相机按钮
                popupWindow.dismiss();
                adapter.notifyDataSetChanged();
            }
        });
    }


    @OnClick({R.id.btn_back, R.id.rl_category, R.id.btn_nest})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.rl_category:
                showPopupWindw();
                //切换分类箭头向下或向上
                raw.setRotation(180);
                break;
            case R.id.btn_nest:
                if (upload_urls.size() == 0) {
                    ToastUtil.showToast_center("你还没有选择任何图片" + what_count + "张图片！");
                    return;
                }
                if (upload_urls.size() > what_count) {
                    ToastUtil.showToast_center("最多只能选择" + what_count + "张图片！");
                    return;
                } else {
                    if (isneedcutimg) {
                        cutimg(Uri.parse(upload_urls.get(0)));
                        break;
                    }
                    EventBus.getDefault().post(new AddPhotoEvent(upload_urls));
                    finish();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }
}
