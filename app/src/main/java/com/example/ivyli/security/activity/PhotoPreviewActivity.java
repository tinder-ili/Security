package com.example.ivyli.security.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.ivyli.security.R;
import com.example.ivyli.security.adapter.PhotoAdapter;
import com.example.ivyli.security.core.SecurityApplication;
import com.example.ivyli.security.db.ImagesTable;
import com.example.ivyli.security.presenters.PhotoPreviewPresenter;

import java.util.List;

import javax.inject.Inject;

public class PhotoPreviewActivity extends AppCompatActivity {
    RecyclerView mPhotos;
    PhotoAdapter mAdapter;

    @Inject PhotoPreviewPresenter mPresenter;

    public static Intent getPhotoPreviewActivityIntent(Context context) {
        Intent intent = new Intent(context, PhotoPreviewActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_preview);
        ((SecurityApplication) getApplication()).getAppComponent().inject(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mPhotos = (RecyclerView) findViewById(R.id.photo_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mPhotos.setLayoutManager(linearLayoutManager);
        mAdapter = new PhotoAdapter(null);
        mPhotos.setAdapter(mAdapter);

        mPresenter.takeTarget(this);
        mPresenter.initView();
    }

    public void initView(List<ImagesTable> images) {
        mAdapter.setPhotos(images);
    }
}
