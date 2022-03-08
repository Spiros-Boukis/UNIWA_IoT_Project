package com.example.mqttapplication.Application.DI.Modules;

import com.example.mqttapplication.Backend.Mqtt.IMqttClient;
import com.example.mqttapplication.Backend.Mqtt.myHiveMQClient;
import com.example.mqttapplication.Backend.MqttThread.MqttThread;
import com.hivemq.client.mqtt.MqttClient;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;


@Module
public  class BackEndModule {

    //Provides The MqttService to the Repository
    @Provides
    @Singleton
    public MqttThread provideMqttService()
    {
        return new MqttThread();
    }

    @Provides
    @Singleton
    public IMqttClient provideHiveMQClient()
    {
        return new myHiveMQClient();
    }

}
