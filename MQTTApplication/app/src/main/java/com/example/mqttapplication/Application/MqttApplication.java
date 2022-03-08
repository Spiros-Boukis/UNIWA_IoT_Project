package com.example.mqttapplication.Application;

import android.app.Application;

import com.example.mqttapplication.Application.DI.ApplicationComponent;
import com.example.mqttapplication.Application.DI.DaggerApplicationComponent;
import com.example.mqttapplication.Application.DI.Modules.BackEndModule;
import com.example.mqttapplication.Application.DI.Modules.ViewModelModule;

import com.example.mqttapplication.Application.DI.Modules.RepositoryModule;

import javax.inject.Inject;


//Application Context Class
//Lives Throughout the entire app, lifecycle
public class MqttApplication extends Application {

    //Dagger2 DI Application Component
    private static ApplicationComponent AppComponent;


    @Override
    public void onCreate() {


        super.onCreate();
        //Instantiate DI Component
        AppComponent = DaggerApplicationComponent.builder().repositoryModule(new RepositoryModule()).
                        viewModelModule(new ViewModelModule()).
                        backEndModule(new BackEndModule()).build();
    }

    @Inject
    public MqttApplication()
    {

    }

    //DI Component static getter
    public static ApplicationComponent getAppComponent()
    {
        return AppComponent;
    }
}
