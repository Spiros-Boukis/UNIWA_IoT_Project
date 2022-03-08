package com.example.mqttapplication.Application.DI.Modules;

import com.example.mqttapplication.UI.Destinations.MainFragment.MainFragmentVM;
import com.example.mqttapplication.UI.Destinations.TemperaturesFragment.MeasurementsFragmentVM;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

//Provides ViewModels to destinations
@Module
public class ViewModelModule {


    @Singleton
    @Provides
    public MainFragmentVM provideMainViewModel()
    {
        return new MainFragmentVM();
    }

    @Singleton
    @Provides
    public MeasurementsFragmentVM provideTempViewModel()
    {
        return new MeasurementsFragmentVM();
    }

}
