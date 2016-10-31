package com.example.ivyli.security.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ivyli.security.R;
import com.example.ivyli.security.core.SecurityApplication;
import com.example.ivyli.security.presenters.MainViewPresenter;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    private Button mButton;
    private TextView mTextHelp;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 5555;
    @Inject MainViewPresenter mPresenter;

    boolean permission = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((SecurityApplication) getApplication()).getAppComponent().inject(this);

        mButton = (Button) findViewById(R.id.take_photo);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
        mPresenter.takeTarget(this);
        mPresenter.initView();
    }

    public void setRetakePhotoButton() {
        String text = getResources().getString(R.string.retake_photo);
        mButton.setText(text);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mTextHelp.setText(getResources().getString(R.string.retake_photo_hint));
    }

    public void setTakePhotoText() {
        String text = getResources().getString(R.string.take_photo);
        mButton.setText(text);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = CameraActivity.getCameraActivityIntent(MainActivity.this);
                startActivity(intent);
            }
        });

        mTextHelp.setText(getResources().getString(R.string.start_hint));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permission = true;
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.dropTarget();
    }
}


