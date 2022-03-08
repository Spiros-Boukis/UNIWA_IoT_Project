package com.example.mqttapplication.UI.Interfaces;

//Implemented By ViewModels
//Called by Fragments after Certain UserActions
public interface UserActions {

    public void ConnectAction(String url);
    public void DisconnectAction();
    public void PublishAction(String topic, String payload);
}
