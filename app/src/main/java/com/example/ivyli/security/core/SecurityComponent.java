package com.example.ivyli.security.core;

import com.example.ivyli.security.activity.CameraActivity;
import com.example.ivyli.security.activity.MainActivity;
import com.example.ivyli.security.activity.PhotoPreviewActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface SecurityComponent {
    void inject(SecurityApplication application);

    void inject(MainActivity activity);

    void inject(CameraActivity activity);

    void inject(PhotoPreviewActivity activity);
}
