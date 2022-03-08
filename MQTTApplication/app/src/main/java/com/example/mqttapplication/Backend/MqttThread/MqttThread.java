package com.example.mqttapplication.Backend.MqttThread;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;


import com.example.mqttapplication.Application.MqttApplication;

import com.example.mqttapplication.Backend.Mqtt.IMqttClient;
import com.example.mqttapplication.Helpers.*;

import androidx.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;
//Service Thread managing the MQTT client and connection
@Singleton
public class MqttThread extends HandlerThread implements IMqttCallbacks {
    //Interface providing MQTT actions API
    @Inject
    public IMqttClient mqttClient;
    //Handles Messages From The Repository
    public Handler incomingFromRepoHandler;
    //Handles Sending Messages To Repository
    public Handler RepoMsgHandler;
    //Implementations of Callbacks Called By out MQTT Client when certain events occur
    public static String TAG="MqttThread";
    @Inject
    public  MqttThread()
    {
        super("MQTT_Thread");
        //Inject Dependencies
        MqttApplication.getAppComponent().inject(this);
        //Get MQTT Implementation Instance
        //set it as listener
        mqttClient.setListener(this);
        //build mqttClient
    }

    //Setter for the handler we use to send messages back to the Main Thread(Repo)
    public void setRepoHandler(Handler handler)
    {
        RepoMsgHandler = handler;
    }
    @Override
    protected void onLooperPrepared() {
        incomingFromRepoHandler = new Handler(RepoMsgCallback);
    }
    //Exposes msg handler to Repository so it can send messages
    public Handler getIncomingFromRepoHandler()
    {
        return this.incomingFromRepoHandler;
    }
    //set Message From Repo Handler

    //Handling Messages from Repository
    private Handler.Callback RepoMsgCallback = new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            Log.d(TAG,"Incoming Message From Repository");
            if (msg.what==MessagesEnums.MQTT_CONN_ACTION)
            {
                mqttClient.BuildClient(msg.getData().getString("url"),1883);
                mqttClient.ConnectMQTT();
            }
            else if(msg.what==MessagesEnums.MQTT_DISCONNECT_ACTION)
                mqttClient.Disconnect();
            else if(msg.what==MessagesEnums.MQTT_ACTION)
                mqttClient.Publish(msg.getData().getString("topic"),msg.getData()
                        .getString("payload"),1);
            else if(msg.what==MessagesEnums.MQTT_SUBSCRIBE_ACTION){
                mqttClient.Subscribe("room1/temperature",2);
                mqttClient.Subscribe("room2/temperature",2);
                mqttClient.Subscribe("room1/humidity",2);
                mqttClient.Subscribe("room2/humidity",2);
                mqttClient.Subscribe("room1/sensor/status",2);
                mqttClient.Subscribe("room2/sensor/status",2);
                mqttClient.Subscribe("room1/led/status",2);
                mqttClient.Subscribe("room2/led/status",2);
            }
            return false;
        }
    };



    @Override
    public void run() {
        super.run();
    }




    @Override
    public void onConnectCallback() {

        Log.d(TAG,"onConnectCallback called from Client");
        Message msg = Message.obtain();
        msg.what = MessagesEnums.MQTT_CONN_SUCCESS;
        //Inform repo
        RepoMsgHandler.sendMessage(msg);
    }

    @Override
    public void onDisconnectCallback() {

        Log.d(TAG,"onDisconnectCallback called from Client");
        Message msg = Message.obtain();
        msg.what = MessagesEnums.MQTT_DISCONNECTED;
        //Inform repo
        RepoMsgHandler.sendMessage(msg);
    }

    @Override
    public void onIncomingMessageCallback(String topic, String payload) {
        Log.d(TAG,"onIncomingMessageCallback called from Client");
        Message msg = Message.obtain();
        msg.what = MessagesEnums.MQTT_ACTION;
        Bundle b = new Bundle();
        b.putString("topic",topic);
        b.putString("payload",payload);
        msg.setData(b);
        //Inform repo
        RepoMsgHandler.sendMessage(msg);
    }
}
