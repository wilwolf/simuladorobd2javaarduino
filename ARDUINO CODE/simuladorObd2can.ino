/*  Simulador de OBDII con Java y Arduino
*   Instituto de Investigacion de Ingenieria de Sistemas
*   Team X El Alto - La Paz Bolivia 2021
*
*/  
/***********  LIBRERIAS *********************/
#include <Wire.h>    //Libreria que permite la comunicacion I2C
#include <LiquidCrystal_I2C.h> //Libreria controladora del LCD 
#include "mcp2515_can.h" //Libreria para el uso de BUSCANSHIELD
#include <SPI.h>
/********* VARIABLES Y SET *****************/
LiquidCrystal_I2C lcd(0x27, 16, 2);// Creamos la clase display de 16x2
#define CAN_2515
const int SPI_CS_PIN = 9;
const int CAN_INT_PIN = 2;
mcp2515_can CAN(SPI_CS_PIN); // Set CS pin
uint32_t  canId = 0x000;

unsigned char len = 0;
unsigned char buf[8];
char str[20];

String BuildMessage="";
int MSGIdentifier=0;
/*********** VARIABLES PID ********************/
char vDistanceTraveled =  0;
char vVehiculeSpeed = 0;
char vCoolTemperatura = 0;
char vEngineRunTime = 0;
char vThrottlePosition = 0;
char vIAT = 0;
char vMAF = 0;
char vMAP = 0;
char vFuelTankLevel = 0;
char vEngineSpeed = 0;
/*********** SETUP ********************/
void setup() {
    lcd.begin();   // iniciamos el display
    lcd.backlight(); //encendemos el display
    imprimirMensaje("Iniciando...");  
    delay(2000);
    SERIAL_PORT_MONITOR.begin(115200); //Iniciamos y esperamos a la apertura del puerto
    
    while(!Serial){
     imprimirMensaje("Conecte USB...");
    }
    imprimirMensaje(" USB conectado!");  
    delay(2000);
    START_INIT:
      if(CAN_OK == CAN.begin(CAN_500KBPS)){ //bit de frecuencia y seteamos crystal frecuencia
        SERIAL_PORT_MONITOR.println("CAN BUS Shield iniciado ok!");
        imprimirMensaje("CBS iniciado ok!");
      }else{
         imprimirMensaje("CBS fallo init");
         SERIAL_PORT_MONITOR.println("CAN BUS Shield fallo al iniciar");
         SERIAL_PORT_MONITOR.println("inicie de nuevo CAN BUS Shield");
         delay(100);
         goto START_INIT;
       }   
}

/*********** LOOP ********************/
void loop() {
   //GENERAL ROUTINE
   unsigned char SupportedPID[10] = {1,2,3,4,5,6,7,8,9,10};
   unsigned char MilCleared[7] = {4,65,63,34,224,185,147};
    
  while(1){
    if(SERIAL_PORT_MONITOR.available()){
        
        
        String valor = SERIAL_PORT_MONITOR.readString();
        String opcion = valor.substring(0,4);
        String texto = valor.substring(4);
        
        if(opcion=="disp"){
            if(texto=="apagarDisplay"){apagarDisplay();imprimirMensaje("Apagando...");} //Apagar Display
            else if(texto=="encederDisplay"){encenderDisplay();imprimirMensaje("Encendiendo...");} //Encendemos Display
            else if(texto=="limpiarDisplay"){limpiarDisplay();}//Limpiamos el Display
            else { imprimirMensaje(texto);}    
        }else if(opcion=="0x31"){
              vDistanceTraveled = texto.toDouble();
              imprimirMensaje("x31-DT:"+texto + " Km");
              SERIAL_PORT_MONITOR.println("x31-DT:"+texto + " Km");
              
        }else if(opcion=="0x0D"){
              vVehiculeSpeed = texto.toDouble();
              imprimirMensaje("x0D-VS:"+texto + " Km/h");
              SERIAL_PORT_MONITOR.println("x0D-VS:"+texto + " Km/h");
              
        }else if(opcion=="0x05"){
              vCoolTemperatura = texto.toDouble();
              imprimirMensaje("x05-CT:"+texto + " C");
              SERIAL_PORT_MONITOR.println("x05-CT:"+texto + " C");
              
        }else if(opcion=="0x1F"){
              vEngineRunTime = texto.toDouble();
              imprimirMensaje("x1F-ERT:"+texto + " SEC");
              SERIAL_PORT_MONITOR.println("x1F-ERT:"+texto + " SEC");
              
        }else if(opcion=="0x11"){
              vThrottlePosition = texto.toDouble();
              imprimirMensaje("x11-TP:"+texto + " %");
              SERIAL_PORT_MONITOR.println("x11-TP:"+texto + " %");
              
        }else if(opcion=="0x0F"){
              vIAT = texto.toDouble();
              imprimirMensaje("x0F-IAT:"+texto + " C");
              SERIAL_PORT_MONITOR.println("x0F-IAT:"+texto + " C");
              
        }else if(opcion=="0x10"){
              vMAF = texto.toDouble();
              imprimirMensaje("x10-MAF:"+texto + " gr/sec");
              SERIAL_PORT_MONITOR.println("x10-MAF:"+texto + " gr/sec");
              
        }else if(opcion=="0x0B"){
              vMAP = texto.toDouble();
              imprimirMensaje("x0B-MAP:"+texto + " Kpa");
              SERIAL_PORT_MONITOR.println("x0B-MAP:"+texto + " Kpa");
              
        }else if(opcion=="0x2F"){
              vFuelTankLevel = texto.toDouble();
              imprimirMensaje("x2F-FTL:"+texto + " %");
              SERIAL_PORT_MONITOR.println("x2F-FTL:"+texto + " %");
              
        }else if(opcion=="0x0C"){
              vEngineSpeed = texto.toDouble();
              imprimirMensaje("x0C-RPM:"+texto + " rpm");
              SERIAL_PORT_MONITOR.println("x0C-RPM:"+texto + " rpm");
              
        }else{
          SERIAL_PORT_MONITOR.println(" Sin datos");
        }
      }      
       delay(1000);
       //SENSORES
      unsigned char DistanceTraveled[7]    = {4, 65, 49, vDistanceTraveled, 0, 185, 147};
      unsigned char VehiculeSpeed[7]       = {4, 65, 13, vVehiculeSpeed, 0, 185, 147};
      unsigned char CoolTemperatura[7]     = {4, 65, 5,  vCoolTemperatura, 0, 185, 147};
      unsigned char EngineRunTime[7]       = {4, 65, 31, vEngineRunTime, 0, 185, 147};
      unsigned char ThrottlePosition[7]    = {4, 65, 17, vThrottlePosition, 0, 185, 147};
      unsigned char IAT[7]                 = {4, 65, 15, vIAT, 0, 185, 147};
      unsigned char MAF[7]                 = {4, 65, 16, vMAF, 0, 185, 147};
      unsigned char MAP[7]                 = {4, 65, 11, vMAP, 0, 185, 147};
      unsigned char FuelTankLevel[7]       = {4, 65, 47, vFuelTankLevel, 0, 185, 147};
      unsigned char EngineSpeed[7]         = {4, 65, 12, vEngineSpeed, 0, 185, 147};

      if(CAN_MSGAVAIL == CAN.checkReceive()){
        CAN.readMsgBuf(&len, buf);
         canId = CAN.getCanId();
         SERIAL_PORT_MONITOR.print("<"); SERIAL_PORT_MONITOR.print(canId);SERIAL_PORT_MONITOR.print(",");
          for(int i =0; i<len; i++){
            BuildMessage = BuildMessage + buf[i] + ",";
          }
         SERIAL_PORT_MONITOR.println(BuildMessage);
         //Check wich message was received
         if(BuildMessage=="2,1,0,0,0,0,0,0,") {CAN.sendMsgBuf(0x7E8, 0, 8, SupportedPID);}
         if(BuildMessage=="2,1,1,0,0,0,0,0,") {CAN.sendMsgBuf(0x7E8, 0, 7, MilCleared);}

         //SEND SENSOR STATUS
         if(BuildMessage=="2,1,49,0,0,0,0,0,"){CAN.sendMsgBuf(0x7E8, 0, 7, vDistanceTraveled);}
         if(BuildMessage=="2,1,13,0,0,0,0,0,"){CAN.sendMsgBuf(0x7E8, 0, 7, vVehiculeSpeed);}
         if(BuildMessage=="2,1,5,0,0,0,0,0,"){CAN.sendMsgBuf(0x7E8, 0, 7, vCoolTemperatura);}
         if(BuildMessage=="2,1,31,0,0,0,0,0,"){CAN.sendMsgBuf(0x7E8, 0, 7, vEngineRunTime);}
         if(BuildMessage=="2,1,17,0,0,0,0,0,"){CAN.sendMsgBuf(0x7E8, 0, 7, vThrottlePosition);}
         if(BuildMessage=="2,1,15,0,0,0,0,0,"){CAN.sendMsgBuf(0x7E8, 0, 7, vIAT);}
         if(BuildMessage=="2,1,16,0,0,0,0,0,"){CAN.sendMsgBuf(0x7E8, 0, 7, vMAF);}
         if(BuildMessage=="2,1,11,0,0,0,0,0,"){CAN.sendMsgBuf(0x7E8, 0, 7, vMAP);}
         if(BuildMessage=="2,1,47,0,0,0,0,0,"){CAN.sendMsgBuf(0x7E8, 0, 7, vFuelTankLevel);}
         if(BuildMessage=="2,1,12,0,0,0,0,0,"){CAN.sendMsgBuf(0x7E8, 0, 7, vEngineSpeed);}
         BuildMessage="";
      }
      
     }
  }
 
/*********** FUNCTIONES ****************/
void imprimirMensaje(String texto){
    lcd.clear();
    lcd.setCursor(0,0);
    lcd.print(">SimOBD2-IIIS<");
    lcd.setCursor(0,1);
    lcd.print(texto);
}
/**/
void apagarDisplay(){
  lcd.noBacklight();
  SERIAL_PORT_MONITOR.println("Display apagado");
}
/**/
void encenderDisplay(){
  lcd.backlight(); 
  SERIAL_PORT_MONITOR.println("Display encendido");
}
/**/
void limpiarDisplay(){
  lcd.clear();
  SERIAL_PORT_MONITOR.println("Display clear");
}
/**/
void float2Bytes(unsigned char* bytes_temp[4], float float_variable){
  union {
    float a;
    unsigned char bytes[4]; 
  } thing;
  thing.a = float_variable;
  memcpy(bytes_temp, thing.bytes,4);
}
