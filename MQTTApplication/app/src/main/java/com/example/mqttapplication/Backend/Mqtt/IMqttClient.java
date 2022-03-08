package com.example.mqttapplication.Backend.Mqtt;
import com.example.mqttapplication.Backend.MqttThread.IMqttCallbacks;

public interface IMqttClient {

    public void ConnectMQTT();
    public void BuildClient(String brokerURL,int port);
    public void Disconnect();
    public void Subscribe(String topic,int Qos);
    public void Publish(String topic,String payload,int Qos);
    public void setListener(IMqttCallbacks listener);
    }


