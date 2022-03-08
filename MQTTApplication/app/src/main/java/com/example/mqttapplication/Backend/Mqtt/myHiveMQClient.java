package com.example.mqttapplication.Backend.Mqtt;
import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;
import com.example.mqttapplication.Backend.MqttThread.IMqttCallbacks;
import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.lifecycle.MqttClientConnectedContext;
import com.hivemq.client.mqtt.lifecycle.MqttClientConnectedListener;
import com.hivemq.client.mqtt.lifecycle.MqttClientDisconnectedContext;
import com.hivemq.client.mqtt.lifecycle.MqttClientDisconnectedListener;
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient;
import java.util.UUID;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class myHiveMQClient implements IMqttClient {
    //HiveMQ
    private static String TAG = "--myHiveMQClient--" ;
    private Mqtt3AsyncClient mqttClient;
    private IMqttCallbacks listener;

    public void setListener(IMqttCallbacks listener) {
        this.listener = listener;
    }

    @Inject
    public myHiveMQClient() {
    }

    @Override
    public void BuildClient(String brokerURL, int port) {
        //Build the client with given arguments
        mqttClient = MqttClient.builder()
                .useMqttVersion3()
                .identifier(UUID.randomUUID().toString())
                .serverHost(brokerURL)
                .serverPort(port)
                //
                .automaticReconnectWithDefaultConfig().addConnectedListener(new MqttClientConnectedListener() {
                    @Override
                    public void onConnected(MqttClientConnectedContext context) {
                        Log.d(TAG,"Connected to Broker : "+brokerURL+" on Port: "+port);
                        listener.onConnectCallback();
                    }
                })
                .addDisconnectedListener(new MqttClientDisconnectedListener() {
                    @Override
                    public void onDisconnected(MqttClientDisconnectedContext context) {
                        //inform repo
                        Log.d(TAG,"Disconnected from Broker");
                        listener.onDisconnectCallback();
                    }
                })
                .buildAsync();
    }

    @Override
    public void ConnectMQTT() {
        Log.d(TAG,"ConnectMQTT called from Thread");
        mqttClient.connect();

    }

    @Override
    public void Disconnect() {
        Log.d(TAG,"Disconnect called from Thread");
        mqttClient.disconnect();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void Subscribe(String topic, int Qos) {
        Log.d(TAG,"Subscribe called from Thread");
        mqttClient.subscribeWith().topicFilter(topic).
                callback(mqtt3Publish -> {
                    String _topic = mqtt3Publish.getTopic().toString();
                    String _payload = new String(mqtt3Publish.getPayloadAsBytes());
                    if(topic=="room1/led/status" || topic=="room2/led/status")
                    Log.d(TAG,"received at topic "+topic+" Payload: "+_payload);

                    //inform repo
                    listener.onIncomingMessageCallback(topic,_payload);


                })
                .send().whenComplete((mqtt3SubAck, throwable) -> {
            if(throwable!= null){
                Log.d(TAG,"Failed to sub to topic "+topic);
            }
            else
            {
                Log.d(TAG,"Subscribed to "+topic);
            }
                });
    }

    @Override
    public void Publish(String topic,String payload,int Qos) {
        Log.d(TAG,"Publish called from Thread");
            mqttClient.publishWith().topic(topic).payload(payload.getBytes()).send();
    }
}
