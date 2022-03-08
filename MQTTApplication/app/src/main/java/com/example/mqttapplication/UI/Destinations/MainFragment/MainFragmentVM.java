package com.example.mqttapplication.UI.Destinations.MainFragment;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

;


import com.example.mqttapplication.Application.MqttApplication;

import com.example.mqttapplication.Helpers.MessagesEnums;
import com.example.mqttapplication.UI.Destinations.VmBase;

import javax.inject.Inject;

public class MainFragmentVM extends VmBase {
    private static String TAG="MainFragmentVM";

    @Inject
    public MainFragmentVM()
    {

        this.repoListener = this;

        MqttApplication.getAppComponent().inject(this);

        setRepoListener();
    }

    public void setRepoListener()
    {
        sensorRepository.setVmActionListener(repoListener);
    }
    //used to push events into the view

    //passed to TemperaturesFragment

    //State Holders
    private MutableLiveData<String> brokerUrl;
    public MutableLiveData<String> getUrl(){
        if (brokerUrl==null)
        {
            brokerUrl = new MutableLiveData<String>();
        }
        return brokerUrl;
    }
    //

    //Executes after user presses Connect
    @Override
    public void ConnectAction(String url) {
        Log.d(TAG,"ConnectAction called from Fragment");
        Bundle bundle = new Bundle();
        bundle.putString("url",url);
        Message msg = new Message();
        msg.setData(bundle);
        msg.what= MessagesEnums.MQTT_CONN_ACTION;
        sensorRepository.onMessageFromView(msg);
    }

    @Override
    public void DisconnectAction() { }

    @Override
    public void PublishAction(String topic, String payload) { }

    @Override
    public void onMessageFromRepo(Message msg)
    {
        Log.d(TAG,"onMessageFromRepo called from Repo");
        if(msg.what==MessagesEnums.MQTT_CONN_SUCCESS)
        {
            getStatus().setValue(MessagesEnums.MQTT_CONN_SUCCESS);
        }

    }


}
