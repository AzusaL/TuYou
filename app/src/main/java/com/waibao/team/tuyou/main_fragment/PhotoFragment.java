package com.waibao.team.tuyou.main_fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.config.Config;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;

public class PhotoFragment extends Fragment {

    private View view;
    private PhotoView mphotoview;
    private PhotoViewAttacher mattacher;
    private String photourl;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = View.inflate(getActivity(), R.layout.photo_view_fg, null);
        photourl = getArguments().getString("photourl");
        initView();
        setlistener();
        return view;
    }

    private void setlistener() {
        mattacher.setOnPhotoTapListener(new OnPhotoTapListener() {

            @Override
            public void onPhotoTap(View arg0, float arg1, float arg2) {
                getActivity().finish();
            }

            @Override
            public void onOutsidePhotoTap() {

            }
        });
    }

    private void initView() {
        mphotoview = (PhotoView) view.findViewById(R.id.photo_view_img);
        mattacher = new PhotoViewAttacher(mphotoview);

        Glide.with(this)
                .load(Config.Pic + photourl)
                .into(mphotoview);
    }
}
