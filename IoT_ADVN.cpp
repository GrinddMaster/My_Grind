#include <WiFi.h>
#include <PubSubClient.h>
#include<WiFiClientSecure.h>
#include<cert.h>

/*
    _    _ _   _   _                    _       
   / \  | (_) | | | |_   _ ___ ___  ___(_)_ __  
  / _ \ | | | | |_| | | | / __/ __|/ _ \ | '_ \ 
 / ___ \| | | |  _  | |_| \__ \__ \  __/ | | | |
/_/   \_\_|_| |_| |_|\__,_|___/___/\___|_|_| |_|

 ____   ___  _  ___   ___  _ __________ 
|___ \ / _ \/ |/ _ \ / _ \/ |___ /___ / 
  __) | | | | | | | | (_) | | |_ \ |_ \ 
 / __/| |_| | | |_| |\__, | |___) |__) |
|_____|\___/|_|\___/   /_/|_|____/____/                                                                                         


*/


const char* ssid = "Name";
const char* pass = "Password";

const int trig= 33;
const int echo= 25;
const char* ID = "xxxxxx";
const char* username = "AliHussein";
const char* password = "HelloWorld123";
const char* mqtt_server = "xxxx.s1.eu.hivemq.cloud";
const int mqtt_port = xxxxx;
const char* mqtt_topic = "test/1";

WiFiClientSecure wClient;
PubSubClient client(wClient);

long setUpSensor(){
	long distance,duration;
	digitalWrite(trig,LOW);
	delay(900);
	digitalWrite(trig,HIGH);
	delay(900);
	digitalWrite(trig,LOW);
	duration = pulseIn(echo,HIGH);
	distance = duration*0.034/2;
	Serial.println("Distance:");
	Serial.println(distance);
	Serial.println("cm");
	delay(1500);
	return distance;
}

void callback(char* topic,byte* payload, unsigned int length){
	Serial.print("Message: ");
	Serial.print(topic);
	String level = "";
	for(int i=0;i<length;i++){
		Serial.print(char(payload[i]));
		level.concat(payload[i]);
	}
	if(level == "level1"){
		Serial.println(level);
		level = "";
	}else if(level == "level2"){
		Serial.println(level);
		level = "";
	}else if (level == "level3"){
		Serial.println(level);
		level = "";
	}
	level ="";
}

void mqtt_connect(){
	Serial.println("Starting connection to mqtt server!");
	while(!client.connected()){
		if(client.connect(ID,username,password)){
			Serial.println("Connected to mqtt server~!");
			client.subscribe(mqtt_topic);
		}
		else{
			Serial.println("Connection Error:");
			Serial.println(client.state());
			delay(5000);
		}
	}
}


void setup(){
	Serial.begin(115200);
	pinMode(trig, OUTPUT);
	pinMode(echo,INPUT);
	WiFi.begin(ssid,pass);
	while(WiFi.status() != WL_CONNECTED){
		delay(1500);
		Serial.println("Connecting...\n");
	}
	Serial.println("Connected~!");


	wClient.setCACert(cert);
	client.setServer(mqtt_server,mqtt_port);
	client.setCallback(callback);
}

void loop(){
	if(!client.connected()){
		mqtt_connect();
	}
	else{
		client.loop();
	//String payload = String(setUpSensor());
	//client.publish(mqtt_topic,payload.c_str());
	delay(1000);
	}
}
