package com.example.ivyli.security.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ivyli.security.R;
import com.example.ivyli.security.adapter.StatusAdapter;
import com.example.ivyli.security.core.SecurityApplication;
import com.example.ivyli.security.presenters.MainViewPresenter;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST = 5555;

    private Button mButton;
    private Button mStartFacialRecognition;
    private TextView mTextHelp;
    private RecyclerView mProcessStatus;
    private StatusAdapter mAdapter;

    @Inject MainViewPresenter mPresenter;

    boolean mPermission = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((SecurityApplication) getApplication()).getAppComponent().inject(this);

        mButton = (Button) findViewById(R.id.take_photo);
        mStartFacialRecognition = (Button) findViewById(R.id.facial_recognition);
        mTextHelp = (TextView) findViewById(R.id.take_photo_help);
        mProcessStatus = (RecyclerView) findViewById(R.id.process_status);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST);
        mPresenter.takeTarget(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.initView();
    }

    public void initFacialRecognition() {
        mStartFacialRecognition.setVisibility(View.VISIBLE);
        mStartFacialRecognition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdapter.clear();
                mPresenter.startFacialRecognition();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mProcessStatus.setLayoutManager(linearLayoutManager);
        mAdapter = new StatusAdapter(null, this);
        mProcessStatus.setAdapter(mAdapter);

        String text = getResources().getString(R.string.retake_photo);
        mButton.setText(text);
        mTextHelp.setText(getResources().getString(R.string.retake_photo_hint));
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdapter.clear();
                mPresenter.ratakePhoto();
            }
        });
        mButton.setVisibility(View.VISIBLE);
    }

    public void addStatus(int value, int arg) {
        if (arg == -1) {
            mAdapter.addStatus(getResources().getString(value));
        } else {
            mAdapter.addStatus(getResources().getString(value, arg));
        }
    }

    public void initTakePhoto() {
        mStartFacialRecognition.setVisibility(View.GONE);

        String text = getResources().getString(R.string.take_photo);
        mButton.setText(text);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPermission) {
                    startPhotoActivity();
                }else{
                    toast(R.string.permission_error);
                }
            }
        });

        mTextHelp.setText(getResources().getString(R.string.start_hint));
    }

    public void startPhotoActivity() {
        Intent intent = CameraActivity.getCameraActivityIntent(MainActivity.this);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    mPermission = true;
                } else {
                    mPermission = false;
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.dropTarget();
    }

    public void toast(int id) {
        Toast.makeText(this, getResources().getString(id), Toast.LENGTH_SHORT).show();
    }
}


