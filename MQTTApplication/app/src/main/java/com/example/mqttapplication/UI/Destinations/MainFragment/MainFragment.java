package com.example.mqttapplication.UI.Destinations.MainFragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;
import androidx.preference.PreferenceManager;
import com.example.mqttapplication.Application.MqttApplication;
import com.example.mqttapplication.Helpers.MessagesEnums;
import com.example.mqttapplication.R;
import javax.inject.Inject;

public class MainFragment extends Fragment {

    private static String TAG="MainFragment";
    //UI Elements
    private TextView urlTextView;
    private Button connButton;
    private ImageButton prefButton;
    //
    //VM
    @Inject
    public MainFragmentVM viewModel;
    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        setUrlText(preferences.getString("urlPref","default"));
        viewModel.setRepoListener();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        init(view);
        return view;
    }
    private void init(View view)
    {
        PerformDI();
        setupLiveDataObservers();
        initUI(view);
    }

    private void setupLiveDataObservers()
    {
        //LiveData Binding
        final Observer<String> urlObserver = new Observer<String>() {
            @Override
            public void onChanged(String s) {
                urlTextView.setText(s);
            }
        };
        viewModel.getUrl().observe(getViewLifecycleOwner(),urlObserver);

        final Observer<Integer> vmEventObserver = new Observer<Integer>() {
            @Override
            public void onChanged(Integer i) {
                if(i.equals(MessagesEnums.MQTT_CONN_SUCCESS))
                {
                    Bundle data = new Bundle();
                    Navigation.findNavController(getView()).navigate(R.id.temperaturesFragment,data);
                }
            }
        };
        viewModel.getStatus().observe(getViewLifecycleOwner(),vmEventObserver);
    }

    private void setUrlText(String s)
    {
        viewModel.getUrl().setValue(s);
    }

    private void initUI(View view){
        urlTextView = view.findViewById(R.id.urlTextview);

        connButton = view.findViewById(R.id.connBtn);
        prefButton = view.findViewById(R.id.prefButton);
        prefButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Navigating to settingsFragment");
                Navigation.findNavController(view).navigate(R.id.settingsFragment);
            }
        });

        connButton.setOnClickListener(new View.OnClickListener() {
            //on connButton Click
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Connect Button clicked");
                SharedPreferences prefs =  PreferenceManager.getDefaultSharedPreferences(getContext());
                String url=prefs.getString("urlPref","");
                System.out.println(url);

                viewModel.ConnectAction(url);
            }
        });

        setupLiveDataObservers();
    }

    private void PerformDI()
    {
        MqttApplication.getAppComponent().inject(this);
    }

}


















