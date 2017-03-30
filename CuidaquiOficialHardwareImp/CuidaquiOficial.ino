//
// Copyright 2015 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

// FirebaseDemo_ESP8266 is a sample that demo the different functions
// of the FirebaseArduino API.

#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>
#include <ESP8266HTTPClient.h>
#include <FirebaseCloudMessaging.h>

#include <DNSServer.h>            //Local DNS Server used for redirecting all requests to the configuration portal
#include <ESP8266WebServer.h>     //Local WebServer used to serve the configuration portal
#include <WiFiManager.h>          //https://github.com/tzapu/WiFiManager WiFi Configuration Magic

// Set these to run example.
#define FIREBASE_HOST "cuidaqui-8c292.firebaseio.com"
#define FIREBASE_AUTH "9uetUBkDvmmoYnNgFiAII6oKTfC2tOuZj95Z69lI"
String server_key = "AAAAlWZLk_Y:APA91bEjB_vUWEEbbhBcOGGep2L9JIm1FcTok04nW4qOQoDZb-LNv6uMcNYdMAZBAbbVuEL7boy_DGgHP3T0GUEbsrXSGRigzpvtpPriZhYjashzh-r-51eRxtCfyWTmfLWjInl4Yck2";
//#define WIFI_SSID "Mello"
//#define WIFI_PASSWORD "39452119"

#define WATER_CALL "WATER_CALL"
#define BATHROOM_CALL "BATHROOM_CALL"
#define DISCOMFORT_CALL "DISCOMFORT_CALL"
#define REGISTRATION_ID_LABEL "REGISTRATION_ID"

#define WAITING_FOR_ATTENDENCE "Paciente aguardando atendimento"

#define RED_LED 16
#define GREEN_LED 5
#define FIRST_BUTTON 4
#define SECOND_BUTTON 2
#define THIRD_BUTTON 14

String app_registration_id;

WiFiClient client;

void setup() {
  Serial.begin(115200);
  pinMode(RED_LED,OUTPUT);
  pinMode(GREEN_LED,OUTPUT);
  pinMode(FIRST_BUTTON,INPUT);
  pinMode(SECOND_BUTTON,INPUT);
  pinMode(THIRD_BUTTON,INPUT);

  WiFiManager wifiManager;

  //Begin as STA and AP
  WiFi.mode(WIFI_AP);
  delay(500);
  
  //wifiManager.resetSettings();
  digitalWrite(RED_LED,HIGH);
  delay(200);
  wifiManager.autoConnect("CuidAqui");
  delay(200);
  digitalWrite(RED_LED,LOW);
  Serial.println("connected...yeey :)");

  delay(500);
  WiFi.mode(WIFI_STA);  
/*
  WiFi.beginSmartConfig();

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
    Serial.println(WiFi.smartConfigDone());
    digitalWrite(RED_LED,HIGH);
    delay(200);
    digitalWrite(RED_LED,LOW);
    delay(200);
  }
*/
  Serial.println("");
  Serial.println("WiFi connected");  
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());

  // connect to wifi.
 /* WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("connecting");
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(500);
  }
  Serial.println();
  Serial.print("connected: ");
  Serial.println(WiFi.localIP());*/
  
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);

  //get single app registration id
  app_registration_id = Firebase.getString(REGISTRATION_ID_LABEL);
  Serial.print("REGISTRATION_LABEL: ");
  Serial.println(app_registration_id);

  //Inicializando o controle
  Firebase.setInt(BATHROOM_CALL,0);
  Firebase.setInt(DISCOMFORT_CALL,0);
  Firebase.setInt(WATER_CALL,0);
}

//0 : initialization code for remote control
//1 : waiting to be served
//2 : Someone is o the way
//3 : already served

void loop() {
  //Serial.println(".");
  if(digitalRead(FIRST_BUTTON)==HIGH){
    Serial.println(BATHROOM_CALL);
    int number = Firebase.getInt(BATHROOM_CALL);
    if(number != 2 ){
      Firebase.setInt(BATHROOM_CALL,1);
      sendNotification(BATHROOM_CALL,WAITING_FOR_ATTENDENCE); 
    }
    blinkGreenLed();
  }
  if(digitalRead(SECOND_BUTTON) == HIGH){
    Serial.println(DISCOMFORT_CALL);
    int number = Firebase.getInt(DISCOMFORT_CALL);
    if(number != 2){
      Firebase.setInt(DISCOMFORT_CALL,1);
      sendNotification(DISCOMFORT_CALL,WAITING_FOR_ATTENDENCE); 
    }
    blinkGreenLed();
  }
  if(digitalRead(THIRD_BUTTON) == HIGH){
    Serial.println(WATER_CALL);
    int number = Firebase.getInt(WATER_CALL);
    if(number != 2){
      Firebase.setInt(WATER_CALL,1);  
      sendNotification(WATER_CALL,WAITING_FOR_ATTENDENCE);     
    }
    blinkGreenLed();
  }
  delay(100);
  /*
  // set value
  Firebase.setFloat("number", 42.0);
  // handle error
  if (Firebase.failed()) {
      Serial.print("setting /number failed:");
      Serial.println(Firebase.error());  
      return;
  }
  delay(1000);
  
  // update value
  Firebase.setFloat("number", 43.0);
  // handle error
  if (Firebase.failed()) {
      Serial.print("setting /number failed:");
      Serial.println(Firebase.error());  
      return;
  }
  delay(1000);

  // get value 
  Serial.print("number: ");
  Serial.println(Firebase.getFloat("number"));
  delay(1000);

  // remove value
  Firebase.remove("number");
  delay(1000);

  // set string value
  Firebase.setString("message", "hello world");
  // handle error
  if (Firebase.failed()) {
      Serial.print("setting /message failed:");
      Serial.println(Firebase.error());  
      return;
  }
  delay(1000);
  
  // set bool value
  Firebase.setBool("truth", false);
  // handle error
  if (Firebase.failed()) {
      Serial.print("setting /truth failed:");
      Serial.println(Firebase.error());  
      return;
  }
  delay(1000);

  // append a new value to /logs
  String name = Firebase.pushInt("logs", n++);
  // handle error
  if (Firebase.failed()) {
      Serial.print("pushing /logs failed:");
      Serial.println(Firebase.error());  
      return;
  }
  Serial.print("pushed: /logs/");
  Serial.println(name);
  delay(1000);
  */
}

HTTPClient http;
void sendNotification(String paytitle, String pay) {
  //more info @ https://firebase.google.com/docs/cloud-messaging/http-server-ref

  String data = "{";
  data = data + "\"to\": \"" + app_registration_id + "\",";
  data = data + "\"notification\": {";
  data = data + "\"body\": \"" + pay + "\",";
  data = data + "\"title\" : \"" + paytitle + "\" ";
  data = data + "} }";

  http.begin("http://fcm.googleapis.com/fcm/send");
  http.addHeader("Authorization", "key="+ server_key);
  http.addHeader("Content-Type", "application/json");
  http.addHeader("Host", "fcm.googleapis.com");
  http.addHeader("Content-Length", String(pay.length()));
  http.POST(data);
  http.writeToStream(&Serial);
  http.end();
  Serial.println();
}

void blinkGreenLed(){
    digitalWrite(GREEN_LED,HIGH);
    delay(200);
    digitalWrite(GREEN_LED,LOW);
    delay(200);
}
