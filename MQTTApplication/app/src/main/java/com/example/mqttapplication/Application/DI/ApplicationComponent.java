package com.example.mqttapplication.Application.DI;

import com.example.mqttapplication.Application.DI.Modules.BackEndModule;
import com.example.mqttapplication.Application.DI.Modules.ViewModelModule;
import com.example.mqttapplication.Backend.MqttThread.MqttThread;
import com.example.mqttapplication.Repository.SensorRepositoryImpl;
import com.example.mqttapplication.UI.Destinations.MainFragment.MainFragment;
import com.example.mqttapplication.UI.Destinations.MainFragment.MainFragmentVM;
import com.example.mqttapplication.UI.Destinations.TemperaturesFragment.MeasurementsFragment;
import com.example.mqttapplication.UI.Destinations.TemperaturesFragment.MeasurementsFragmentVM;
import com.example.mqttapplication.Application.DI.Modules.RepositoryModule;

import javax.inject.Singleton;

import dagger.Component;


//Global Application Dagger2 Component
//It provides Dependancies to each Application Component(View , Repository , Backend))
@Singleton
//Includes Following Modules, each Module Provides dependancies
@Component(modules = {RepositoryModule.class, ViewModelModule.class, BackEndModule.class})
public interface ApplicationComponent {


    void inject (MainFragmentVM vm);
    void inject (MeasurementsFragmentVM vm);
    void inject (MainFragment fragment);
    void inject (MeasurementsFragment fragment);
    void inject (SensorRepositoryImpl repoImpl);
    void inject (MqttThread thread);
}
