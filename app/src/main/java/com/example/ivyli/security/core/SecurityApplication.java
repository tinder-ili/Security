package com.example.ivyli.security.core;

import android.app.Application;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;


public class SecurityApplication extends Application {
    private static SecurityComponent sSecurityComponent;

    public void onCreate() {
        super.onCreate();
        setupDb();

        sSecurityComponent = DaggerSecurityComponent.builder().appModule(new AppModule(this)).build();
        sSecurityComponent.inject(this);
    }

    private void setupDb() {
        Configuration dbConfiguration = new Configuration.Builder(this).setDatabaseName("Security.db").create();
        ActiveAndroid.initialize(dbConfiguration);
    }

    public SecurityComponent getAppComponent() {
        return sSecurityComponent;
    }
}
