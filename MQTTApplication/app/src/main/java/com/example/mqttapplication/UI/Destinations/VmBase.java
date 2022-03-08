package com.example.mqttapplication.UI.Destinations;

import android.os.Message;

import androidx.lifecycle.ViewModel;

import com.example.mqttapplication.Repository.SensorRepositoryImpl;
import com.example.mqttapplication.Helpers.SingleLiveEvent;
import com.example.mqttapplication.UI.Interfaces.RepoEventListener;
import com.example.mqttapplication.UI.Interfaces.UserActions;

import javax.inject.Inject;

//Base Class For The App View Model
//
public abstract class VmBase extends ViewModel implements UserActions, RepoEventListener {

    //Static Repository Instance. Shared across all ViewModels
    @Inject
    public  SensorRepositoryImpl sensorRepository;

    //Listener for Events Happening into Repository

    //ViewModels implement the reactions
    protected RepoEventListener repoListener;

    //Integer Observed by the view. Contains MessageEnums values
    //Its a way of control flow from Viewmodel to the View
    protected SingleLiveEvent<Integer> Status;
    public SingleLiveEvent<Integer> getStatus()
    {
        if(this.Status==null)
            Status = new SingleLiveEvent<Integer>();
        return Status;
    }

    public abstract void setRepoListener();

    @Override
    public abstract void onMessageFromRepo(Message msg);

    @Override
    public abstract void ConnectAction(String url);

    @Override
    public abstract void DisconnectAction();

    @Override
    public abstract void PublishAction(String topic, String payload);
}
