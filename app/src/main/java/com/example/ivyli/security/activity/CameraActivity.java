package com.example.ivyli.security.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.ivyli.security.R;
import com.example.ivyli.security.core.SecurityApplication;
import com.example.ivyli.security.presenters.CameraViewPresenter;
import com.example.ivyli.security.view.CameraPreview;

import javax.inject.Inject;

public class CameraActivity extends Activity {

    private CameraPreview mPreview;

    @Inject
    CameraViewPresenter mPresenter;

    public static Intent getCameraActivityIntent(Context context) {
        Intent intent = new Intent(context, CameraActivity.class);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        ((SecurityApplication) getApplication()).getAppComponent().inject(this);

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mPresenter.getCamera());
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
        mPresenter.takeTarget(this);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.dropTarget();
    }

    public void toast(int id) {
        Toast.makeText(this, getResources().getString(id), Toast.LENGTH_SHORT).show();
    }

    public void toastWithArg(int id, int arg) {
        Toast.makeText(this, getResources().getString(id, arg), Toast.LENGTH_SHORT).show();
    }
}