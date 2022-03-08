package com.example.mqttapplication.Application.DI.Modules;

import com.example.mqttapplication.Repository.SensorRepositoryImpl;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class RepositoryModule {




    public RepositoryModule()
    {

    }

    //provides the Repository Implementation to The ViewModels
    @Provides
    @Singleton
    public SensorRepositoryImpl provideSensorRepo() {
        return new SensorRepositoryImpl();
    }

}


