package com.example.ivyli.security.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ivyli.security.R;
import com.example.ivyli.security.adapter.PhotoAdapter;
import com.example.ivyli.security.core.SecurityApplication;
import com.example.ivyli.security.db.ImagesTable;
import com.example.ivyli.security.presenters.PhotoPreviewPresenter;

import java.util.List;

import javax.inject.Inject;

public class PhotoPreviewActivity extends AppCompatActivity {
    private RecyclerView mPhotos;
    private PhotoAdapter mAdapter;
    private EditText mPassword1;
    private EditText mPassword2;
    private Button mSave;

    private View mProgress;

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

        mPassword1 = (EditText) findViewById(R.id.passowrd_1);
        mPassword2 = (EditText) findViewById(R.id.passowrd_2);
        mSave = (Button) findViewById(R.id.save_button);
        mProgress = findViewById(R.id.progress_container);

        mPhotos = (RecyclerView) findViewById(R.id.photo_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mPhotos.setLayoutManager(linearLayoutManager);
        mAdapter = new PhotoAdapter(null, this);
        mPhotos.setAdapter(mAdapter);

        mPresenter.takeTarget(this);
        mPresenter.initView();
    }

    public void initView(List<ImagesTable> images) {
        mAdapter.setPhotos(images);
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mPassword1.getWindowToken(), 0);
                mProgress.setVisibility(View.VISIBLE);
                mPresenter.savePhotos(mPassword1.getText().toString(), mPassword2.getText().toString());
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.clearCache();
    }

    @Override
    public void onBackPressed(){
        mPresenter.clearCache();
        this.finish();
    }

    public void passwordError() {
        Toast.makeText(this, getResources().getString(R.string.password_error), Toast.LENGTH_SHORT).show();
        mPassword1.setText(null);
        mPassword2.setText(null);
    }

    public void dismissProgress(){
        mProgress.setVisibility(View.GONE);
    }
}
