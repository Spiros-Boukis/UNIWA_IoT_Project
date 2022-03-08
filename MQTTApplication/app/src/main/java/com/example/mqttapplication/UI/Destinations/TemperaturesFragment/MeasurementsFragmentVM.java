package com.example.mqttapplication.UI.Destinations.TemperaturesFragment;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;


import com.example.mqttapplication.Application.MqttApplication;

import com.example.mqttapplication.Helpers.MessagesEnums;
import com.example.mqttapplication.UI.Destinations.VmBase;

import javax.inject.Inject;

public class MeasurementsFragmentVM extends VmBase{

    private static String TAG="MeasurementsFragmentVM";

    public void setRepoListener()
    {
        sensorRepository.setVmActionListener(repoListener);
    }



    //STATE HOLDERS
    private MutableLiveData<String> room1Hum;
    //Fragment Accesses via getter
    public MutableLiveData<String> getRoom1Hum()
    {
        if(room1Hum==null)
        {
            room1Hum= new MutableLiveData<String>();
        }
        return room1Hum;
    }

    private MutableLiveData<String> room2Hum;
    public MutableLiveData<String> getRoom2Hum()
    {
        if(room2Hum==null)
        {
            room2Hum= new MutableLiveData<String>();
        }
        return room2Hum;
    }
    private MutableLiveData<String> room1Status;

    public MutableLiveData<String> getRoom1Status()
    {
        if(room1Status==null)
        {
            room1Status = new MutableLiveData<String>();
        }
        return room1Status;
    }
    private MutableLiveData<String> room2Status;
    public MutableLiveData<String> getRoom2Status()
    {
        if(room2Status==null)
        {
            room2Status = new MutableLiveData<String>();
        }
        return room2Status;
    }
    private MutableLiveData<String> room1Temp;
    public MutableLiveData<String> getRoom1Temp()
    {
        if(room1Temp==null)
        {
            room1Temp = new MutableLiveData<String>();
        }
        return room1Temp;
    }
    private MutableLiveData<String> room2Temp;
    public MutableLiveData<String> getRoom2Temp()
    {
        if(room2Temp==null)
        {
            room2Temp = new MutableLiveData<String>();
        }
        return room2Temp;
    }
    private MutableLiveData<String> room1LedStatus;
    public MutableLiveData<String> getRoom1LedStatus()
    {
        if(room1LedStatus==null)
        {
            room1LedStatus = new MutableLiveData<String>();
        }
        return room1LedStatus;
    }
    private MutableLiveData<String> room2LedStatus;
    public MutableLiveData<String> getRoom2LedStatus()
    {
        if(room2LedStatus==null)
        {
            room2LedStatus = new MutableLiveData<String>();
        }
        return room2LedStatus;
    }

    @Inject
    public MeasurementsFragmentVM()
    {
        MqttApplication.getAppComponent().inject(this);
        this.repoListener = this;
        sensorRepository.setVmActionListener(repoListener);
    }

    @Override
    public void ConnectAction(String url) {

    }

    @Override
    public void DisconnectAction() {
        Log.d(TAG,"DisconnectAction Called from Fragment");
        Message msg = new Message();
        msg.what= MessagesEnums.MQTT_DISCONNECT_ACTION;
        sensorRepository.onMessageFromView(msg);
    }

    @Override
    public void PublishAction(String topic, String payload) {
        Log.d(TAG,"PublishAction Called from Fragment");
        Message msg = new Message();
        Bundle bundle = new Bundle();
        msg.what= MessagesEnums.MQTT_ACTION;
        bundle.putString("topic",topic);
        bundle.putString("payload",payload);
        msg.setData(bundle);
        sensorRepository.onMessageFromView(msg);
    }

    @Override
    public void onMessageFromRepo(Message msg) {
        Log.d(TAG,"onMessageFromRepo called from Repo");
        System.out.println(msg.what);

        if(msg.what==MessagesEnums.MQTT_ACTION)
        {
            String topic = msg.getData().getString("topic");
            String payload = msg.getData().getString("payload");

            switch(topic)
            {

                case "room1/temperature":
                {
                    getRoom1Temp().setValue(payload+"°C");
                    break;
                }
                case "room2/temperature":
                {
                    getRoom2Temp().setValue(payload+"°C");
                    break;
                }
                case "room1/sensor/status":
                {
                    getRoom1Status().setValue(payload);
                    break;
                }
                case "room2/sensor/status":
                {
                    getRoom2Status().setValue(payload);
                    break;
                }
                case "room1/led/status":
                {
                    Log.d(TAG,"Updating LiveData");
                    getRoom1LedStatus().setValue(payload);
                    break;
                }
                case "room2/led/status":
                {
                    Log.d(TAG,"Updating LiveData");
                    getRoom2LedStatus().setValue(payload);
                    break;
                }
                case "room1/humidity":
                {
                    getRoom1Hum().setValue(payload + "rH");
                    break;
                }
                case "room2/humidity":
                {
                    getRoom2Hum().setValue(payload + "rH");
                    break;
                }
            }
        }
        else if(msg.what == MessagesEnums.MQTT_CONN_SUCCESS)
        {

        }
        else if(msg.what == MessagesEnums.MQTT_DISCONNECTED)
        {
            getStatus().setValue(MessagesEnums.MQTT_DISCONNECTED);
        }
    }

}
