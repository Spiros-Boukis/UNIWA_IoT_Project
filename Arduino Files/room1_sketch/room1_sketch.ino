//Απαραίτητες βιβλιοθήκες
#include <WiFi.h> 
//Βιβλιοθήκη MQTT Client
#include <PubSubClient.h>
//Βιβλιοθήκη για την σύνδεση με τον αισθήτηρα με το πρωτόκολλο I2C
#include <Wire.h>
//Βιβλιοθήκες σχετικές με τον αισθητήρα
#include "Adafruit_SHT4x.h"
#include <Adafruit_Sensor.h>
//

//---Αρχικοποίηση Μεταβλητών---//

//Wi-Fi credentials
const char* ssid = "Inkjetstores.gr";
const char* password = "2299025593";
//Domain Name του MQTT Broker
const char* mqtt_server = "spyrosboukis.ddns.net";
//ID του MQTT Client
const char* mqttClientID = "room1_ESP";
//LWT
const char* willTopic = "room1/sensor/status";
const char* willMessage = "offline";
//
//Αντικείμενο του αισθητήρα
Adafruit_SHT4x sht4 = Adafruit_SHT4x();
//Wi-Fi Client
WiFiClient espClient;
//MQTT Client
PubSubClient client(espClient);

//χρησιμοποείται για τον υπολογισμό του χρόνου μεταξύ των μετρήσεων.
//eτσι μπορούμε να ορίσουμε συχνότητα για το κάθε πότε θέλουμε να γίνεται νέα μέτρηση
long lastMsgTime = 0;
//Συχνότητα Μετρήσης σε ms
int measurement_interval = 5000;

//Κατάσταση LED
char ledStatus[8];
//Μεταβλητές μετρήσεων
float temperature = 0;
float humidity = 0;
//Ορισμός Pin που ελέγχει το LED, σύμφωνα με την συνδεσμολογία  
const int ledPin = 4;


//1 Αρχικοποίηση 
void setup() {
  //---1.2
  setupSensor();
  //--1.3
  setup_wifi();
  //--1.4, 1.5, 1.6, 1.7
  setup_mqtt();

  //set led pin to output mode
  pinMode(ledPin, OUTPUT);
  //LED OFF
  digitalWrite(ledPin, LOW);
  //ledStatus = false
  strncpy(ledStatus, "false", sizeof((ledStatus)));
}

//2 
void loop() {

 //μεταβλητές
  sensors_event_t humidity, temp;
  char tempStr[8];
  char humStr[8];

  //---2.1
  if (!client.connected()) {
    reconnect();
  }

  //2.6
  client.loop();

  
  long now = millis();
  if (now - lastMsgTime > measurement_interval) {
    lastMsgTime = now;
    
    uint32_t timestamp = millis();
    //Διάζουμε τις μετρήσεις του αισθητήρα
    sht4.getEvent(&humidity, &temp);
    timestamp = millis() - timestamp;
    Serial.print("Read duration (ms): ");
    Serial.println(timestamp);
    Serial.print("Temperature: ");  
    Serial.print(temp.temperature);  
    Serial.println(" degrees C");
    Serial.print("Humidity: ");
    Serial.print(humidity.relative_humidity);
    Serial.println("% rH");
    Serial.println("-----------------------------------");
    //formatting
    dtostrf(temp.temperature, 5, 2, tempStr);
    dtostrf(humidity.relative_humidity, 5, 2, humStr);
    //
    client.publish("room1/temperature", tempStr,true);
    client.publish("room1/humidity", humStr,true);

    delay(1000);
  }
}


//---1.2---
void setupSensor()
{
  //Εκκίνηση σηριακής επικοινωνίας με τον αισθητήρα
  Serial.begin(115200);

  
  while (!Serial){
    Serial.println("Serial connection Error...");
    delay(10);
    }
  Serial.println("Adafruit SHT4x test");
  if (! sht4.begin()) {
    Serial.println("Couldn't find SHT4x Sensor");
    while (1) delay(1);
  }
  Serial.println("Found SHT4x sensor");
  Serial.print("Serial number 0x");
  Serial.println(sht4.readSerial(), HEX);

  //Ορισμός Ακρίβειας αισθητήρα, HIGH-MED-LOW
  sht4.setPrecision(SHT4X_LOW_PRECISION);
  //Εμφάνιση ακρίβειας στην σηριακή κονσόλα
  switch (sht4.getPrecision()) {
    case SHT4X_HIGH_PRECISION:
      Serial.println("High precision"); break;
    case SHT4X_MED_PRECISION:
      Serial.println("Med precision"); break;
    case SHT4X_LOW_PRECISION:
      Serial.println("Low precision"); break;
  }

  //

}

//---1.3---
void setup_wifi() {
  delay(10);

  Serial.println();
  Serial.print("Connecting to Wi-Fi");
 

  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  Serial.println("");
  Serial.println("WiFi connected");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
  Serial.println("-----------------------------------");

}

//--1.4 1.5 1.6 1.7 --
void setup_mqtt(){
  //1.4 1.5
  client.setServer(mqtt_server, 1883);
  Serial.println("Trying To Connect To MQTT server on port 1883");
  //Σύνδεση με ορισμό LWT
  if(client.connect(mqttClientID, willTopic, 1, true, willMessage))
  {
  Serial.println("---Connected to MQTT Broker---");
  
  //Ορισμός συνάρτησης που διαχειρίζεται τα μηνύματα από τον Broker
  client.setCallback(mqtt_message_callback);
   //--1.6--
  client.subscribe("room1/led/output",1);
  //--1.7
  //client.publish
  client.publish("room1/led/status", "false", true);
  client.publish("room1/sensor/status", "online", true);
  Serial.println("Published: room1/sensor/status,true ");
  Serial.println("Published: room1/led/status,false ");
  }
}

//2.6 Η Συνάρτηση που διαχειρίζεται τα νέα μηνύματα από τον broker
//length = μέγεθος μηνύματος σε byte
void mqtt_message_callback(char* topic,byte* message,unsigned int length){
  //Show in console
  Serial.print("Message arrived on topic: ");
  Serial.print(topic);
  Serial.print(". Message: ");

  //Μετατροπή του μηνύματος σε string
  String arrivedMsg;
  for (int i = 0; i < length; i++) {
    Serial.print((char)message[i]);
    arrivedMsg += (char)message[i];
  }
  Serial.println();
  

  //Έλεγχος topic
  if (String(topic) == "room1/led/output") {
    Serial.print("--New Led Status:");
    Serial.println(arrivedMsg);

    if (arrivedMsg == "true") {
     
      
      //Αλλαγή Κατάστασης LED σε ON
      digitalWrite(ledPin, HIGH);

      //Publish new status
      strncpy(ledStatus, "true", sizeof((ledStatus)));
      client.publish("room1/led/status", ledStatus, true);
      
      //
    }
    else if (arrivedMsg == "false") {
      
      digitalWrite(ledPin, LOW);
      strncpy(ledStatus, "false", sizeof((ledStatus)));
      client.publish("room1/led/status", ledStatus, true);
    }
    Serial.print("Published to room1/led/status payload:");
    Serial.println(arrivedMsg);
    Serial.println("-----------------------------------");
  }
}

void reconnect() {
  // Loop until we're reconnected
  while (!client.connected()) {
    Serial.print("...Attempting MQTT connection...");
    // Attempt to connect
    if (client.connect(mqttClientID, willTopic, 1, true, willMessage)) {
      Serial.println("---Connected to MQTT Broker---");
      // Subscribe
      client.subscribe("room1/led/output",1);
      client.publish("room1/led/status", "false", true);
      client.publish("room1/sensor/status", "online", true);
    } else {
      Serial.print("failed, rc=");
      Serial.print(client.state());
      Serial.println(" try again in 5 seconds");
      // Wait 5 seconds before retrying
      delay(5000);
    }
  }
}
