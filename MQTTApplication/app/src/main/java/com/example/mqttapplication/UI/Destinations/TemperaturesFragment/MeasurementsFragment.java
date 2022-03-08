package com.example.mqttapplication.UI.Destinations.TemperaturesFragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import com.example.mqttapplication.Application.MqttApplication;
import com.example.mqttapplication.Helpers.MessagesEnums;
import com.example.mqttapplication.R;


import javax.inject.Inject;

public class MeasurementsFragment extends Fragment {

    private static String TAG ="MeasurementsFragment";

    @Inject
    public MeasurementsFragmentVM viewModel;
    //UI ELEMENTS
    private TextView room1Status;
    private TextView room2Status;
    private TextView room1Temp;
    private TextView room2Temp;
    private TextView room1Hum;
    private Button disconnectBtn;
    private TextView room2Hum;
    private Switch room1Switch;
    private Switch room2Switch;
    private Button room1toogle;
    private Button room2toogle;
    //
    private void setupLiveDataObservers()
    {
        //ROOM1 STATUS OBSERVER
        //Declare an observer that tracks changes to a String variable
        final Observer<String> room1StatusObserver = new Observer<String>() {
            @Override
            public void onChanged(String s)
            {
                //Runs when vm Variable value changes
                if(s.equals("online"))
                {
                    room1Status.setTextColor(Color.GREEN);
                }
                else
                {
                    room1Status.setTextColor(Color.RED);
                }
                room1Status.setText(s);
            }
        };
        //Set the observer to track changes in VM variable
        viewModel.getRoom1Status().observe(getViewLifecycleOwner(),room1StatusObserver);

        //ROOM2 STATUS
        final Observer<String> room2StatusObserver = new Observer<String>() {
            @Override
            public void onChanged(String s) {

                if(s.equals("online"))
                {
                    room2Status.setTextColor(Color.GREEN);
                }
                else
                {
                    room2Status.setTextColor(Color.RED);
                }
                room2Status.setText(s);
            }
        };
        viewModel.getRoom2Status().observe(getViewLifecycleOwner(),room2StatusObserver);

        //ROOM1 LED STATUS
        final Observer<String> room1LedStatusObserver = new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(s.equals("false"))
                    room1Switch.setChecked(false);
                else if (s.equals("true"))
                    room1Switch.setChecked(true);
            }
        };
        viewModel.getRoom1LedStatus().observe(getViewLifecycleOwner(),room1LedStatusObserver);

        //ROOM2 LED STATUS
        final Observer<String> room2LedStatusObserver = new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(s.equals("false"))
                    room2Switch.setChecked(false);
                else if (s.equals("true"))
                    room2Switch.setChecked(true);
            }
        };
        viewModel.getRoom2LedStatus().observe(getViewLifecycleOwner(),room2LedStatusObserver);

        //ROOM1 TEMP
        final Observer<String> room1TempObserver = new Observer<String>() {
            @Override
            public void onChanged(String s) {
                room1Temp.setText(s);
            }
        };
        viewModel.getRoom1Temp().observe(getViewLifecycleOwner(),room1TempObserver);

        //ROOM2 TEMP
        final Observer<String> room2TempObserver = new Observer<String>() {
            @Override
            public void onChanged(String s) {
                room2Temp.setText(s);
            }
        };
        viewModel.getRoom2Temp().observe(getViewLifecycleOwner(),room2TempObserver);

        //ROOM1 TEMP
        final Observer<String> room1HumObserver = new Observer<String>() {
            @Override
            public void onChanged(String s) {
                room1Hum.setText(s);
            }
        };
        viewModel.getRoom1Hum().observe(getViewLifecycleOwner(),room1HumObserver);

        //ROOM2 TEMP
        final Observer<String> room2HumObserver = new Observer<String>() {
            @Override
            public void onChanged(String s) {
                room2Hum.setText(s);
            }
        };
        viewModel.getRoom2Hum().observe(getViewLifecycleOwner(),room2HumObserver);


        //Connection Status
        final Observer<Integer> vmEventObserver = new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                switch(integer)
                {
                    case MessagesEnums.MQTT_DISCONNECTED:
                    {
                        Log.d(TAG,"Navigating to MainFragment");
                        Navigation.findNavController(getView()).navigate(R.id.mainFragment);
                        break;
                    }
                }
            }
        } ;
        viewModel.getStatus().observe(getViewLifecycleOwner(),vmEventObserver);


    }



    public MeasurementsFragment() {
        // Required empty public constructor
    }

    public static MeasurementsFragment newInstance(String param1, String param2) {
        MeasurementsFragment fragment = new MeasurementsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_temperatures, container, false);
        init(view);
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        viewModel.setRepoListener();
        Message msg = new Message();
        Bundle bundle = new Bundle();
        msg.what= MessagesEnums.MQTT_SUBSCRIBE_ACTION;
        msg.setData(bundle);
        viewModel.sensorRepository.onMessageFromView(msg);
    }

    private void init(View view)
    {
        performDI();
        initUI(view);
        setupListeners();
        setupLiveDataObservers();
    }

    private void initUI(View view)
    {
        room1Status = view.findViewById(R.id.room1Status);
        room2Status = view.findViewById(R.id.room2Status);

        room1Temp = view.findViewById(R.id.room1Temp);



        room2Temp = view.findViewById(R.id.room2Temp);

        room1Hum = view.findViewById(R.id.room1Hum);

        room2Hum = view.findViewById(R.id.room2Hum);



        room1Switch = view.findViewById(R.id.room1switch);

        room1Switch.setClickable(false);
        room1toogle = view.findViewById(R.id.room1toogle);

        room2Switch = view.findViewById(R.id.room2switch);
        room2Switch.setClickable(false);
        room2toogle = view.findViewById(R.id.room2toogle);

        setupLiveDataObservers();

        disconnectBtn = view.findViewById(R.id.disconnectBtn);
    }


    private void setupListeners()
    {
        room1toogle.setOnClickListener(new CompoundButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG," Room1 Switch Pressed");
                if(room1Switch.isChecked()== true)
                {

                    viewModel.PublishAction("room1/led/output","false");
                }
                else {
                    viewModel.PublishAction("room1/led/output","true");
                }
            }

        });

        disconnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Disconnect Button pressed");
                viewModel.DisconnectAction();
            }
        });

        room2toogle.setOnClickListener(new CompoundButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG," Room2 Switch Pressed");
                if(room2Switch.isChecked())
                {
                    viewModel.PublishAction("room2/led/output","false");
                }
                else {
                    viewModel.PublishAction("room2/led/output","true");
                }
            }

        });


    }

    private void performDI()
    {
        MqttApplication.getAppComponent().inject(this);
    }

}