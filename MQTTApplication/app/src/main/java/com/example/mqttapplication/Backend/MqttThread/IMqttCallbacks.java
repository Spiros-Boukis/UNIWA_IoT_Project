package com.example.mqttapplication.Backend.MqttThread;
import android.os.Message;
//Callbacks implemented by the MqttThread
//They are called by the mqttClient implementations when the events described by the methods below occur
public interface IMqttCallbacks {
    public void onConnectCallback();
    public void onDisconnectCallback();
    public void onIncomingMessageCallback(String topic, String payload);
}
