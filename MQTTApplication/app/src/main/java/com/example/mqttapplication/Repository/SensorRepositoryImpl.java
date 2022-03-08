package com.example.mqttapplication.Repository;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.mqttapplication.Application.MqttApplication;
import com.example.mqttapplication.Helpers.MessagesEnums;
import com.example.mqttapplication.Backend.MqttThread.MqttThread;
import com.example.mqttapplication.UI.Interfaces.RepoEventListener;

import javax.inject.Inject;
import javax.inject.Singleton;
@Singleton
public class
SensorRepositoryImpl implements ISensorRepository{
    private static String TAG = "SensorRepositoryImpl";

    @Inject
    MqttThread threadService;
    //Handles Incoming Messages From MQTTService Thread
    private Handler messageFromThreadHandler;
    //Callback listener implemented in the ViewModel, Used for informing ViewModel about new Messages
    public RepoEventListener VMlistener;
    @Inject
    public SensorRepositoryImpl()
    {
        MqttApplication.getAppComponent().inject(this);
        //Handler for Handling Messages FROM the MQTT Thread
        messageFromThreadHandler = new Handler(Looper.getMainLooper(), MqttThread_Msg_CallBack);
        //Set this handler on the Thread so it was be used to send messages back to Repository
        threadService.setRepoHandler(this.messageFromThreadHandler);
        threadService.start();
    }
    //Callback called from ViewModel
    @Override
    public void onMessageFromView(Message message) {
        //Access the handler used by MQTT thread to listen to messages from the main thred
        Log.d(TAG,"onMessageFromView called from ViewModel");
        threadService.getIncomingFromRepoHandler().sendMessage(message);
    }
    //callback for messages coming from MQTT Client
    private Handler.Callback MqttThread_Msg_CallBack = new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            Log.d(TAG,"Message from MQTT Thread Arrived");
                if(msg.what==MessagesEnums.MQTT_ACTION)
                {
                    Bundle b = msg.getData();
                    String topic = b.getString("topic");
                    String payload = b.getString("payload");
                }
                //Callback to Viewmodel, with the message from MQTT thread
                VMlistener.onMessageFromRepo(msg);
            return false;
        }
    };

    @Override
    public void setVmActionListener(RepoEventListener _listener) {
        VMlistener = _listener;
    }
}








