#include <DHT.h>
#include <ESP8266WiFi.h>

//DHT Pin
#define DHTPIN D4
#define DHTTYPE DHT11

//API Key
String apiKeyTemp = "******";
String apiKeyMotion = "******";
String apiKeyWater = "******";

//WiFi Conf
const char* ssid = "******";
const char* password = "******";
const char* server = "api.thingspeak.com";

//Pin Acquire
const int waterSensor = A0;
const int lampRed = D3;
const int lampGreen = D7;
const int lampMotion = D2;
const int lampRoom1 = D0;
const int lampRoom2 = D5;
const int lampRoom3 = D6;
const int motionSensor = D1;
const int buzzerPin = D8;

//Buzzer Conf
const int songLength = 7;
char notes[] = "ccggaa ";
int beats[] = { 1, 1, 1, 1, 1, 1, 2 };
int tempo = 300;

//Additional Var
int waterValue;
int motionValue;
int pirState = LOW;
int systemStatus = 0;

DHT dht(DHTPIN, DHTTYPE);
WiFiClient client;
 
void setup()
{
  Serial.begin(115200);
  dht.begin();
 
  pinMode(lampRed, OUTPUT);
  pinMode(lampGreen, OUTPUT);
  pinMode(lampMotion, OUTPUT);
  pinMode(lampRoom1, OUTPUT);
  pinMode(lampRoom2, OUTPUT);
  pinMode(lampRoom3, OUTPUT);
  pinMode(buzzerPin, OUTPUT);
  pinMode(motionSensor, INPUT);
  
  WiFi.begin(ssid, password);

  Serial.print("Connecting to ");
  Serial.println(ssid);
   
  while (WiFi.status() != WL_CONNECTED)
  {
    delay(500);
    Serial.print(".");
  }
  
  Serial.println("");
  Serial.println("WiFi connected");
}
 
void loop()
{
  getSystemStatus();
  if(systemStatus == 1) {
    postTempHumd();
    motionSensorDetection();
    waterLevelPercentage();
    
    getLamp(1);
    getLamp(2);
    getLamp(3);
  } else {
    Serial.println("System Not Active");
    digitalWrite(lampRoom1, LOW);
    digitalWrite(lampRoom2, LOW);
    digitalWrite(lampRoom3, LOW);
  }
}

void postTempHumd() {
  float kelembapan = dht.readHumidity();
  float temperatur = dht.readTemperature();
  
  if (isnan(kelembapan) || isnan(temperatur))
  {
    Serial.println("Failed to read Sensor DHT");
    return;
  }
  
  if (client.connect(server,80)) {
    String postStr = apiKeyTemp;
    postStr +="&field1=";
    postStr += String(temperatur);
    postStr +="&field2=";
    postStr += String(kelembapan);
     
    client.print("POST /update HTTP/1.1\n");
    client.print("Host: api.thingspeak.com\n");
    client.print("Connection: close\n");
    client.print("X-THINGSPEAKAPIKEY: "+apiKeyTemp+"\n");
    client.print("Content-Type: application/x-www-form-urlencoded\n");
    client.print("Content-Length: ");
    client.print(postStr.length());
    client.print("\n\n");
    client.print(postStr);
     
    Serial.print("\nTemperature: ");
    Serial.print(temperatur);
    Serial.print("\nHumadity: ");
    Serial.println(kelembapan);
  }
  
  client.stop();
}

void postMotion(int status) {
  if (client.connect(server,80)) {
    String postStr = apiKeyMotion;
    postStr +="&field1=";
    postStr += String(status);
     
    client.print("POST /update HTTP/1.1\n");
    client.print("Host: api.thingspeak.com\n");
    client.print("Connection: close\n");
    client.print("X-THINGSPEAKAPIKEY: "+apiKeyMotion+"\n");
    client.print("Content-Type: application/x-www-form-urlencoded\n");
    client.print("Content-Length: ");
    client.print(postStr.length());
    client.print("\n\n");
    client.print(postStr);
  }
  
  client.stop();
}

void postWaterLevel(int level) {
  if (client.connect(server,80)) {
    String postStr = apiKeyWater;
    postStr +="&field1=";
    postStr += String(level);
     
    client.print("POST /update HTTP/1.1\n");
    client.print("Host: api.thingspeak.com\n");
    client.print("Connection: close\n");
    client.print("X-THINGSPEAKAPIKEY: "+apiKeyWater+"\n");
    client.print("Content-Type: application/x-www-form-urlencoded\n");
    client.print("Content-Length: ");
    client.print(postStr.length());
    client.print("\n\n");
    client.print(postStr);
  }
  
  client.stop();
}

void playTone(int tone, int duration) {
  for (long i = 0; i < duration * 1000L; i += tone * 2) {
    digitalWrite(buzzerPin, HIGH);
    delayMicroseconds(tone);
    digitalWrite(buzzerPin, LOW);
    delayMicroseconds(tone);
  }
}

void playNote(char note, int duration) {
  char names[] = { 'c', 'd', 'e', 'f', 'g', 'a', 'b', 'C' };
  int tones[] = { 1915, 1700, 1519, 1432, 1275, 1136, 1014, 956 };

  for (int i = 0; i < 8; i++) {
    if (names[i] == note) {
      playTone(tones[i], duration);
    }
  }
}

void motionSensorDetection() {
  motionValue = digitalRead(motionSensor);
  if (motionValue == HIGH) {
    digitalWrite(lampMotion, HIGH); 
    if (pirState == LOW) {
      Serial.println("Motion detected!");
      pirState = HIGH;
      postMotion(1);
      for (int i = 0; i < songLength; i++) {
        if (notes[i] == ' ') {
          delay(beats[i] * tempo);
        } else {
          playNote(notes[i], beats[i] * tempo);
        }
        delay(tempo / 1);
      }
    }
  } else {
    digitalWrite(lampMotion, LOW); 
    if (pirState == HIGH){
      delay(2000);
      postMotion(0);
      Serial.println("Motion ended!");
      pirState = LOW;
    }
  }
}

void waterLevelPercentage() {
  waterValue = analogRead(waterSensor);
  if (waterValue<=480){ 
    Serial.println("Water level: 0mm - Empty"); 
  }
  else if (waterValue>480 && waterValue<=530){ 
    Serial.println("Water level: 0mm to 5mm"); 
  }
  else if (waterValue>530 && waterValue<=615){ 
    Serial.println("Water level: 5mm to 10mm"); 
  }
  else if (waterValue>615 && waterValue<=660){ 
    Serial.println("Water level: 10mm to 15mm"); 
  } 
  else if (waterValue>660 && waterValue<=680){ 
    Serial.println("Water level: 15mm to 20mm"); 
  }
  else if (waterValue>680 && waterValue<=690){ 
    Serial.println("Water level: 20mm to 25mm"); 
  }
  else if (waterValue>690 && waterValue<=700){ 
    Serial.println("Water level: 25mm to 30mm"); 
  }
  else if (waterValue>700 && waterValue<=705){ 
    Serial.println("Water level: 30mm to 35mm"); 
  }
  else if (waterValue>705){ 
    Serial.println("Water level: 35mm to 40mm"); 
  }
  postWaterLevel(waterValue);
}

void getSystemStatus()
{
    if(client.connect(server, 80))
    {
        client.println(String("GET ") + "/channels/201328/fields/1/last HTTP/1.1\r\n"+
                       "Host: api.thingspeak.com\r\n" +
                       "Connection: close\r\n\r\n");

        int i=0;
        while((!client.available()) && (i<1000)){
          delay(10);
          i++;
        }
    }
    else {
        Serial.println("Unable to connect to Thingspeak.");
    }

    while(client.available()){
      systemStatus = client.readStringUntil('\r').toInt();
    }

    if(systemStatus == 1) {
      digitalWrite(lampRed, LOW);
      digitalWrite(lampGreen, HIGH);
    } else {
      digitalWrite(lampRed, HIGH);
      digitalWrite(lampGreen, LOW);
    }
}

void getLamp(int lampNum)
{
    if(client.connect(server, 80))
    {
        client.println(String("GET ") + "/channels/199304/fields/"+lampNum+"/last HTTP/1.1\r\n"+
                       "Host: api.thingspeak.com\r\n" +
                       "Connection: close\r\n\r\n");

        int i=0;
        while((!client.available()) && (i<1000)){
          delay(10);
          i++;
        }
    }
    else {
        Serial.println("Unable to connect to Thingspeak.");
    }

    int status = 0;
    while(client.available()){
      status = client.readStringUntil('\r').toInt();
    }

    if(status == 1) {
        if(lampNum == 1) {
          digitalWrite(lampRoom1, HIGH);
        } else if(lampNum == 2) {
          digitalWrite(lampRoom2, HIGH);
        } else if(lampNum == 3) {
          digitalWrite(lampRoom3, HIGH);
        }
      } else {
        if(lampNum == 1) {
          digitalWrite(lampRoom1, LOW);
        } else if(lampNum == 2) {
          digitalWrite(lampRoom2, LOW);
        } else if(lampNum == 3) {
          digitalWrite(lampRoom3, LOW);
        }
      }
}
