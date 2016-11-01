package com.example.ivyli.security.presenters;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.ivyli.security.R;
import com.example.ivyli.security.activity.CameraActivity;
import com.example.ivyli.security.activity.PhotoPreviewActivity;
import com.example.ivyli.security.repository.PhotoRepository;

import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

public class CameraViewPresenter extends BasePresenter<CameraActivity> {
    private final static int DELAY = 500;
    private final Handler handler = new Handler();
    private final Timer timer = new Timer();
    private boolean mSaveFileReady = false;
    private Camera mCamera;

    @Inject
    PhotoRepository mRepository;

    @Inject
    public CameraViewPresenter() {
        mCamera = getCameraInstance();
    }

    public void setPhotoData(@NonNull byte[] photo) {
        mRepository.addPhoto(photo);
    }

    public void takeTarget(CameraActivity activity) {
        super.takeTarget(activity);
        try {
            timer.schedule(task, DELAY, DELAY);
        } catch (Exception e) {
            Log.e("", e.getLocalizedMessage());
        }
    }

    public void dropTarget() {
        super.dropTarget();
        mCamera.release();
    }

    public Camera getCamera() {
        return mCamera;
    }

    private Camera.PictureCallback getPictureCallback(final int counter) {
        return new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                setPhotoData(data);
                Log.v("", "picture taken");

                mSaveFileReady = true;
            }
        };
    }


    public Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(1); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    private final TimerTask task = new TimerTask() {
        private int counter = 0;

        public void run() {
            handler.post(new Runnable() {
                public void run() {
                    if (counter == 11) {
                        return;
                    }

                    CameraActivity target = getTarget();
                    try {
                        mCamera.stopPreview();
                        mCamera.startPreview();
                        mCamera.takePicture(null, null, getPictureCallback(counter));
                        target.toastWithArg(R.string.take_photo_n, counter);
                        counter++;
                    } catch (RuntimeException e) {
                        Log.e("", e.getLocalizedMessage());

                        if (target == null) {
                            return;
                        }
                        target.toast(R.string.taking_photo_error_retry);
                        target.finish();
                        return;
                    }

                }
            });

            if (counter == 11 && mSaveFileReady) {
                timer.cancel();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        CameraActivity target = getTarget();
                        if (target != null) {
                            Intent intent = PhotoPreviewActivity.getPhotoPreviewActivityIntent(target);
                            target.startActivity(intent);
                            target.finish();
                        }
                    }
                });
            }
        }
    };
}


