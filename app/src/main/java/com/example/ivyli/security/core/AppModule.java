package com.example.ivyli.security.core;

import android.app.Application;

import com.example.ivyli.security.repository.PhotoRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    SecurityApplication mApplication;

    public AppModule(SecurityApplication application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return mApplication;
    }

    @Provides
    @Singleton
    PhotoRepository providePhotoReposiroty(Application context) {
        return new PhotoRepository(context);
    }
}
