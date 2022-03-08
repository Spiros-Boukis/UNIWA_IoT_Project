package com.example.mqttapplication.Repository;

import android.os.Message;

import com.example.mqttapplication.UI.Interfaces.RepoEventListener;

public interface ISensorRepository {

    public void setVmActionListener(RepoEventListener _listener);

    public void onMessageFromView(Message message);
}
