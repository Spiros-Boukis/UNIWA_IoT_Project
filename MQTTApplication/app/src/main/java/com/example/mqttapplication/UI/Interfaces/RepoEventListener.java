package com.example.mqttapplication.UI.Interfaces;

import android.os.Message;

import java.util.HashMap;

//implemented by Viewmodels
//Called into Repo on certain Events
public interface RepoEventListener {
    public void onMessageFromRepo(Message msg);
}
