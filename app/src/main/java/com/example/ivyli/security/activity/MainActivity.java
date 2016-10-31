package com.example.ivyli.security.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ivyli.security.R;
import com.example.ivyli.security.core.SecurityApplication;
import com.example.ivyli.security.presenters.MainViewPresenter;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 5555;

    private Button mButton;
    private TextView mTextHelp;
    private EditText mPassword1;
    private EditText mPassword2;

    @Inject MainViewPresenter mPresenter;

    boolean permission = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((SecurityApplication) getApplication()).getAppComponent().inject(this);

        mButton = (Button) findViewById(R.id.take_photo);
        mTextHelp = (TextView) findViewById(R.id.take_photo_help);
        mPassword1 = (EditText) findViewById(R.id.passowrd_1);
        mPassword2 = (EditText) findViewById(R.id.passowrd_2);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
        mPresenter.takeTarget(this);
        mPresenter.initView();
    }

    public void initRetakePhoto() {
        String text = getResources().getString(R.string.retake_photo);
        mButton.setText(text);
        mTextHelp.setText(getResources().getString(R.string.retake_photo_hint));
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.retakePhoto(mPassword1.getText().toString(), mPassword2.getText().toString());
            }
        });
    }

    public void initTakePhoto() {
        String text = getResources().getString(R.string.take_photo);
        mButton.setText(text);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPhotoActivity();
            }
        });

        mTextHelp.setText(getResources().getString(R.string.start_hint));
    }

    public void startPhotoActivity() {
        Intent intent = CameraActivity.getCameraActivityIntent(MainActivity.this);
        startActivity(intent);
    }

    public void clearPasswordFields(){
        mPassword1.setText(null);
        mPassword2.setText(null);
    }

    public void retakePhotoPasswordError() {
        Toast.makeText(this, getResources().getString(R.string.password_error), Toast.LENGTH_SHORT).show();
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


