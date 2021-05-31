/*  Simulador de OBDII con Java y Arduino
*   Instituto de Investigacion de Ingenieria de Sistemas
*   Team X El Alto - La Paz Bolivia 2021
*   By: Ing. Grover Wilson Quisbert Ibañez
*/  
/***********  LIBRERIAS *********************/
#include <Wire.h>                 //Libreria que permite la comunicacion I2C
#include <LiquidCrystal_I2C.h>    //Libreria controladora del LCD 
#include "mcp2515_can.h"          //Libreria para el uso de CAN
#include <SPI.h>

 
/********* VARIABLES Y SET *****************/
LiquidCrystal_I2C lcd(0x27, 16, 2);// Creamos la clase display de 16x2
#define CAN_2515
const int SPI_CS_PIN = 9;
const int CAN_INT_PIN = 2;
mcp2515_can CAN(SPI_CS_PIN);   // Set CS pin
uint32_t  canId = 0x000;       // Set ID CAN 
unsigned char len = 0;         // Size array  
unsigned char buf[8];          // Array Buffer
uint8_t DegreeBitmap[]= { 0x6, 0x9, 0x9, 0x6, 0x0, 0, 0, 0 };      // create a dot character pattern for degree symbol 


char str[20];

String BuildMessage="";
int MSGIdentifier=0;
/*********** VARIABLES PARA EL TRATAMIENTO DE CADENAS *********/
String valor = "";
String opcion = "";
String texto = "";
/*********** VARIABLES PID SOPORTADAS ********************/
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
/********** DECLARACION DE VARIABLES PIDS GLOBALES *******/
const char PID_NAME_0x00[] PROGMEM = "PIDs supported [01 - 20]";
const char PID_NAME_0x01[] PROGMEM = "Monitor status since DTCs cleared";
const char PID_NAME_0x02[] PROGMEM = "Freeze DTC";
const char PID_NAME_0x03[] PROGMEM = "Fuel system status";
const char PID_NAME_0x04[] PROGMEM = "Calculated engine load";
const char PID_NAME_0x05[] PROGMEM = "Engine coolant temperature";
const char PID_NAME_0x06[] PROGMEM = "Short term fuel trim — Bank 1";
const char PID_NAME_0x07[] PROGMEM = "Long term fuel trim — Bank 1";
const char PID_NAME_0x08[] PROGMEM = "Short term fuel trim — Bank 2";
const char PID_NAME_0x09[] PROGMEM = "Long term fuel trim — Bank 2";
const char PID_NAME_0x0a[] PROGMEM = "Fuel pressure";
const char PID_NAME_0x0b[] PROGMEM = "Intake manifold absolute pressure";
const char PID_NAME_0x0c[] PROGMEM = "Engine RPM";
const char PID_NAME_0x0d[] PROGMEM = "Vehicle speed";
const char PID_NAME_0x0e[] PROGMEM = "Timing advance";
const char PID_NAME_0x0f[] PROGMEM = "Intake air temperature";
const char PID_NAME_0x10[] PROGMEM = "MAF air flow rate";
const char PID_NAME_0x11[] PROGMEM = "Throttle position";
const char PID_NAME_0x12[] PROGMEM = "Commanded secondary air status";
const char PID_NAME_0x13[] PROGMEM = "Oxygen sensors present (in 2 banks)";
const char PID_NAME_0x14[] PROGMEM = "Oxygen Sensor 1 - Short term fuel trim";
const char PID_NAME_0x15[] PROGMEM = "Oxygen Sensor 2 - Short term fuel trim";
const char PID_NAME_0x16[] PROGMEM = "Oxygen Sensor 3 - Short term fuel trim";
const char PID_NAME_0x17[] PROGMEM = "Oxygen Sensor 4 - Short term fuel trim";
const char PID_NAME_0x18[] PROGMEM = "Oxygen Sensor 5 - Short term fuel trim";
const char PID_NAME_0x19[] PROGMEM = "Oxygen Sensor 6 - Short term fuel trim";
const char PID_NAME_0x1a[] PROGMEM = "Oxygen Sensor 7 - Short term fuel trim";
const char PID_NAME_0x1b[] PROGMEM = "Oxygen Sensor 8 - Short term fuel trim";
const char PID_NAME_0x1c[] PROGMEM = "OBD standards this vehicle conforms to";
const char PID_NAME_0x1d[] PROGMEM = "Oxygen sensors present (in 4 banks)";
const char PID_NAME_0x1e[] PROGMEM = "Auxiliary input status";
const char PID_NAME_0x1f[] PROGMEM = "Run time since engine start";
const char PID_NAME_0x20[] PROGMEM = "PIDs supported [21 - 40]";
const char PID_NAME_0x21[] PROGMEM = "Distance traveled with malfunction indicator lamp (MIL) on";
const char PID_NAME_0x22[] PROGMEM = "Fuel Rail Pressure (relative to manifold vacuum)";
const char PID_NAME_0x23[] PROGMEM = "Fuel Rail Gauge Pressure (diesel, or gasoline direct injection)";
const char PID_NAME_0x24[] PROGMEM = "Oxygen Sensor 1 - Fuel–Air Equivalence Ratio";
const char PID_NAME_0x25[] PROGMEM = "Oxygen Sensor 2 - Fuel–Air Equivalence Ratio";
const char PID_NAME_0x26[] PROGMEM = "Oxygen Sensor 3 - Fuel–Air Equivalence Ratio";
const char PID_NAME_0x27[] PROGMEM = "Oxygen Sensor 4 - Fuel–Air Equivalence Ratio";
const char PID_NAME_0x28[] PROGMEM = "Oxygen Sensor 5 - Fuel–Air Equivalence Ratio";
const char PID_NAME_0x29[] PROGMEM = "Oxygen Sensor 6 - Fuel–Air Equivalence Ratio";
const char PID_NAME_0x2a[] PROGMEM = "Oxygen Sensor 7 - Fuel–Air Equivalence Ratio";
const char PID_NAME_0x2b[] PROGMEM = "Oxygen Sensor 8 - Fuel–Air Equivalence Ratio";
const char PID_NAME_0x2c[] PROGMEM = "Commanded EGR";
const char PID_NAME_0x2d[] PROGMEM = "EGR Error";
const char PID_NAME_0x2e[] PROGMEM = "Commanded evaporative purge";
const char PID_NAME_0x2f[] PROGMEM = "Fuel Tank Level Input";
const char PID_NAME_0x30[] PROGMEM = "Warm-ups since codes cleared";
const char PID_NAME_0x31[] PROGMEM = "Distance traveled since codes cleared";
const char PID_NAME_0x32[] PROGMEM = "Evap. System Vapor Pressure";
const char PID_NAME_0x33[] PROGMEM = "Absolute Barometric Pressure";
const char PID_NAME_0x34[] PROGMEM = "Oxygen Sensor 1 - Fuel–Air Equivalence Ratio";
const char PID_NAME_0x35[] PROGMEM = "Oxygen Sensor 2 - Fuel–Air Equivalence Ratio";
const char PID_NAME_0x36[] PROGMEM = "Oxygen Sensor 3 - Fuel–Air Equivalence Ratio";
const char PID_NAME_0x37[] PROGMEM = "Oxygen Sensor 4 - Fuel–Air Equivalence Ratio";
const char PID_NAME_0x38[] PROGMEM = "Oxygen Sensor 5 - Fuel–Air Equivalence Ratio";
const char PID_NAME_0x39[] PROGMEM = "Oxygen Sensor 6 - Fuel–Air Equivalence Ratio";
const char PID_NAME_0x3a[] PROGMEM = "Oxygen Sensor 7 - Fuel–Air Equivalence Ratio";
const char PID_NAME_0x3b[] PROGMEM = "Oxygen Sensor 8 - Fuel–Air Equivalence Ratio";
const char PID_NAME_0x3c[] PROGMEM = "Catalyst Temperature: Bank 1, Sensor 1";
const char PID_NAME_0x3d[] PROGMEM = "Catalyst Temperature: Bank 2, Sensor 1";
const char PID_NAME_0x3e[] PROGMEM = "Catalyst Temperature: Bank 1, Sensor 2";
const char PID_NAME_0x3f[] PROGMEM = "Catalyst Temperature: Bank 2, Sensor 2";
const char PID_NAME_0x40[] PROGMEM = "PIDs supported [41 - 60]";
const char PID_NAME_0x41[] PROGMEM = "Monitor status this drive cycle";
const char PID_NAME_0x42[] PROGMEM = "Control module voltage";
const char PID_NAME_0x43[] PROGMEM = "Absolute load value";
const char PID_NAME_0x44[] PROGMEM = "Fuel–Air commanded equivalence ratio";
const char PID_NAME_0x45[] PROGMEM = "Relative throttle position";
const char PID_NAME_0x46[] PROGMEM = "Ambient air temperature";
const char PID_NAME_0x47[] PROGMEM = "Absolute throttle position B";
const char PID_NAME_0x48[] PROGMEM = "Absolute throttle position C";
const char PID_NAME_0x49[] PROGMEM = "Absolute throttle position D";
const char PID_NAME_0x4a[] PROGMEM = "Absolute throttle position E";
const char PID_NAME_0x4b[] PROGMEM = "Absolute throttle position F";
const char PID_NAME_0x4c[] PROGMEM = "Commanded throttle actuator";
const char PID_NAME_0x4d[] PROGMEM = "Time run with MIL on";
const char PID_NAME_0x4e[] PROGMEM = "Time since trouble codes cleared";
const char PID_NAME_0x4f[] PROGMEM = "Maximum value for Fuel–Air equivalence ratio, oxygen sensor voltage, oxygen sensor current, and intake manifold absolute pressure";
const char PID_NAME_0x50[] PROGMEM = "Maximum value for air flow rate from mass air flow sensor";
const char PID_NAME_0x51[] PROGMEM = "Fuel Type";
const char PID_NAME_0x52[] PROGMEM = "Ethanol fuel percentage";
const char PID_NAME_0x53[] PROGMEM = "Absolute Evap system Vapor Pressure";
const char PID_NAME_0x54[] PROGMEM = "Evap system vapor pressure";
const char PID_NAME_0x55[] PROGMEM = "Short term secondary oxygen sensor trim";
const char PID_NAME_0x56[] PROGMEM = "Long term secondary oxygen sensor trim";
const char PID_NAME_0x57[] PROGMEM = "Short term secondary oxygen sensor trim";
const char PID_NAME_0x58[] PROGMEM = "Long term secondary oxygen sensor trim";
const char PID_NAME_0x59[] PROGMEM = "Fuel rail absolute pressure";
const char PID_NAME_0x5a[] PROGMEM = "Relative accelerator pedal position";
const char PID_NAME_0x5b[] PROGMEM = "Hybrid battery pack remaining life";
const char PID_NAME_0x5c[] PROGMEM = "Engine oil temperature";
const char PID_NAME_0x5d[] PROGMEM = "Fuel injection timing";
const char PID_NAME_0x5e[] PROGMEM = "Engine fuel rate";
const char PID_NAME_0x5f[] PROGMEM = "Emission requirements to which vehicle is designed";

const char* const PID_NAME_MAPPER[] PROGMEM = {
  PID_NAME_0x00,
  PID_NAME_0x01,
  PID_NAME_0x02,
  PID_NAME_0x03,
  PID_NAME_0x04,
  PID_NAME_0x05,
  PID_NAME_0x06,
  PID_NAME_0x07,
  PID_NAME_0x08,
  PID_NAME_0x09,
  PID_NAME_0x0a,
  PID_NAME_0x0b,
  PID_NAME_0x0c,
  PID_NAME_0x0d,
  PID_NAME_0x0e,
  PID_NAME_0x0f,
  PID_NAME_0x10,
  PID_NAME_0x11,
  PID_NAME_0x12,
  PID_NAME_0x13,
  PID_NAME_0x14,
  PID_NAME_0x15,
  PID_NAME_0x16,
  PID_NAME_0x17,
  PID_NAME_0x18,
  PID_NAME_0x19,
  PID_NAME_0x1a,
  PID_NAME_0x1b,
  PID_NAME_0x1c,
  PID_NAME_0x1d,
  PID_NAME_0x1e,
  PID_NAME_0x1f,
  PID_NAME_0x20,
  PID_NAME_0x21,
  PID_NAME_0x22,
  PID_NAME_0x23,
  PID_NAME_0x24,
  PID_NAME_0x25,
  PID_NAME_0x26,
  PID_NAME_0x27,
  PID_NAME_0x28,
  PID_NAME_0x29,
  PID_NAME_0x2a,
  PID_NAME_0x2b,
  PID_NAME_0x2c,
  PID_NAME_0x2d,
  PID_NAME_0x2e,
  PID_NAME_0x2f,
  PID_NAME_0x30,
  PID_NAME_0x31,
  PID_NAME_0x32,
  PID_NAME_0x33,
  PID_NAME_0x34,
  PID_NAME_0x35,
  PID_NAME_0x36,
  PID_NAME_0x37,
  PID_NAME_0x38,
  PID_NAME_0x39,
  PID_NAME_0x3a,
  PID_NAME_0x3b,
  PID_NAME_0x3c,
  PID_NAME_0x3d,
  PID_NAME_0x3e,
  PID_NAME_0x3f,
  PID_NAME_0x40,
  PID_NAME_0x41,
  PID_NAME_0x42,
  PID_NAME_0x43,
  PID_NAME_0x44,
  PID_NAME_0x45,
  PID_NAME_0x46,
  PID_NAME_0x47,
  PID_NAME_0x48,
  PID_NAME_0x49,
  PID_NAME_0x4a,
  PID_NAME_0x4b,
  PID_NAME_0x4c,
  PID_NAME_0x4d,
  PID_NAME_0x4e,
  PID_NAME_0x4f,
  PID_NAME_0x50,
  PID_NAME_0x51,
  PID_NAME_0x52,
  PID_NAME_0x53,
  PID_NAME_0x54,
  PID_NAME_0x55,
  PID_NAME_0x56,
  PID_NAME_0x57,
  PID_NAME_0x58,
  PID_NAME_0x59,
  PID_NAME_0x5a,
  PID_NAME_0x5b,
  PID_NAME_0x5c,
  PID_NAME_0x5d,
  PID_NAME_0x5e,
  PID_NAME_0x5f,
};

const char PERCENTAGE[] PROGMEM = "%";
const char KPA[] PROGMEM = "kPa";
const char PA[] PROGMEM = "Pa";
const char RPM[] PROGMEM = "rpm";
const char KPH[] PROGMEM = "km/h";
const char DEGREES_BEFORE_TDC[] PROGMEM = "° before TDC";
const char GRAMS_PER_SECOND[] PROGMEM = "grams/sec";
const char SECONDS[] PROGMEM = "seconds";
const char RATIO[] PROGMEM = "ratio";
const char COUNT[] PROGMEM = "count";
const char KM[] PROGMEM = "km";
const char VOLTS[] PROGMEM = "V";
const char MINUTES[] PROGMEM = "minutes";
const char GPS[] PROGMEM = "g/s";
const char DEGREES[] PROGMEM = "°";
const char DEGREES_CELCIUS[] PROGMEM = "\001C";
const char LPH[] PROGMEM = "L/h";

const char* const PID_UNIT_MAPPER[] PROGMEM = {
  NULL,
  NULL,
  NULL,
  NULL,
  PERCENTAGE,
  DEGREES_CELCIUS,
  PERCENTAGE,
  PERCENTAGE,
  PERCENTAGE,
  PERCENTAGE,
  KPA,
  KPA,
  RPM,
  KPH,
  DEGREES_BEFORE_TDC,
  DEGREES_CELCIUS,
  GRAMS_PER_SECOND,
  PERCENTAGE,
  NULL,
  NULL,
  PERCENTAGE,
  PERCENTAGE,
  PERCENTAGE,
  PERCENTAGE,
  PERCENTAGE,
  PERCENTAGE,
  PERCENTAGE,
  PERCENTAGE,
  NULL,
  NULL,
  NULL,
  SECONDS,
  NULL,
  KM,
  KPA,
  KPA,
  RATIO,
  RATIO,
  RATIO,
  RATIO,
  RATIO,
  RATIO,
  RATIO,
  RATIO,
  PERCENTAGE,
  PERCENTAGE,
  PERCENTAGE,
  PERCENTAGE,
  COUNT,
  KM,
  PA,
  KPA,
  RATIO,
  RATIO,
  RATIO,
  RATIO,
  RATIO,
  RATIO,
  RATIO,
  RATIO,
  DEGREES_CELCIUS,
  DEGREES_CELCIUS,
  DEGREES_CELCIUS,
  DEGREES_CELCIUS,
  NULL,
  NULL,
  VOLTS,
  PERCENTAGE,
  RATIO,
  PERCENTAGE,
  DEGREES_CELCIUS,
  PERCENTAGE,
  PERCENTAGE,
  PERCENTAGE,
  PERCENTAGE,
  PERCENTAGE,
  PERCENTAGE,
  MINUTES,
  MINUTES,
  NULL,
  GPS,
  NULL,
  PERCENTAGE,
  KPA,
  PA,
  PERCENTAGE,
  PERCENTAGE,
  PERCENTAGE,
  PERCENTAGE,
  KPA,
  PERCENTAGE,
  PERCENTAGE,
  DEGREES_CELCIUS,
  DEGREES,
  LPH,
  NULL,
};

//Mode 1 PID

unsigned char SupportedPID1[8] = {0x02, 0x41, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
unsigned char SupportedPID2[8] = {0x02, 0x41, 0x20, 0x00, 0x00, 0x00, 0x00, 0x00};
unsigned char SupportedPID3[8] = {0x02, 0x41, 0x40, 0x00, 0x00, 0x00, 0x00, 0x00};
unsigned char SupportedPID4[8] = {0x02, 0x41, 0x60, 0x00, 0x00, 0x00, 0x00, 0x00};
unsigned char SupportedPID5[8] = {0x02, 0x41, 0x80, 0x00, 0x00, 0x00, 0x00, 0x00};
unsigned char SupportedPID6[8] = {0x02, 0x41, 0xA0, 0x00, 0x00, 0x00, 0x00, 0x00};
unsigned char SupportedPID7[8] = {0x02, 0x41, 0xC0, 0x00, 0x00, 0x00, 0x00, 0x00};

//Mode 2
unsigned char DistTravelled[8] = {0x03, 0x41, 0x31, 0x50, 0x00, 0x00, 0x00, 0x00};
unsigned char Throttle[8] = {0x03, 0x41, 0x11, 0x02, 0x00, 0x00, 0x00, 0x00};

unsigned char EngineSpeed[8] =  {0x04, 0x41, 0x0C, 0x4e, 0x20, 0x00, 0x00, 0x00};
unsigned char VehiculeSpeed[8] = {0x03, 0x41, 0x0D, 0x50, 0x00, 0x00, 0x00, 0x00};
unsigned char CoolantTemp[8] = {0x03, 0x41, 0x05, 0x46, 0x00, 0x00, 0x00, 0x00};

unsigned char EngRunTime[8] = {0x04, 0x41, 0x1F, 0x00, 0x78, 0x00, 0x00, 0x00};
unsigned char IAT[8] = {0x03, 0x41, 0x0F, 0x60, 0x00, 0x00, 0x00, 0x00};
unsigned char MAF[8] = {0x04, 0x41, 0x10, 0x00, 0xE5, 0x00, 0x00, 0x00};
unsigned char MAP[8] = {0x03, 0x41, 0x0B, 0x22, 0x00, 0x00, 0x00, 0x00};

unsigned char FuelTankLevel[8] = {0x03, 0x41, 0x2f, 0x22, 0x00, 0x00, 0x00, 0x00};
unsigned char DistTraveledWithMIL[8] = {0x03, 0x41, 0x21, 0x22, 0x00, 0x00, 0x00, 0x00};

//mode 3 Frame
unsigned char DTCFrame[8] = {0x04, 0x43, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00};

//mode 4 Frame
unsigned char DTCClearFrame[8] = {0x01, 0x44, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};

//mode 9 PID
unsigned char CalibID[8] = {0x03, 0x49, 0x04, 0x22, 0x00, 0x00, 0x00, 0x00};
unsigned char CVN[8] = {0x03, 0x49, 0x06, 0x22, 0x00, 0x00, 0x00, 0x00};
unsigned char ECU_NAME[8] = {0x03, 0x49, 0x0A, 0x22, 0x00, 0x00, 0x00, 0x00};
/*********** SETUP **************************************/
void setup() {
    lcd.begin();   // iniciamos el display
    lcd.createChar ( 1, DegreeBitmap );
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
        imprimirMensaje("CAN iniciado!");
      }else{
         imprimirMensaje("CAN fallo!");
         SERIAL_PORT_MONITOR.println("CAN BUS Shield fallo al iniciar");
         SERIAL_PORT_MONITOR.println("inicie de nuevo CAN BUS Shield");
         delay(100);
         goto START_INIT;
       }  
     delay(2000);
     imprimirMensaje("..."); 
}

/*********** LOOP ********************/
void loop() {
   //GENERAL ROUTINE
   unsigned char SupportedPID[10] = {1,2,3,4,5,6,7,8,9,10};
   unsigned char MilCleared[7] = {4,65,63,34,224,185,147};
    
  while(1){
    if(SERIAL_PORT_MONITOR.available()){
        
        char _valor;
        valor = SERIAL_PORT_MONITOR.readString();
        int str_len = valor.length()+1;
        char array_str[str_len];
        valor.toCharArray(array_str,str_len);
        const char s[2]="|";
        char *token;

        token = strtok(array_str,s);
        while(token!=NULL){
          valor = token;
          opcion = valor.substring(0,4);
          texto = valor.substring(4);
            if(opcion=="disp"){
            if(texto=="apagarDisplay"){apagarDisplay();imprimirMensaje("Apagando...");} //Apagar Display
            else if(texto=="encederDisplay"){encenderDisplay();imprimirMensaje("Encendiendo...");} //Encendemos Display
            else if(texto=="limpiarDisplay"){limpiarDisplay();}//Limpiamos el Display
            else { imprimirMensaje(texto);}    
            }else if(opcion=="0x31"){
                  vDistanceTraveled = texto.toDouble();
                  imprimirPid(pidName(0x31),pidUnits(0x31), texto);
                  SERIAL_PORT_MONITOR.println(pidName(0x31)+"-"+pidUnits(0x31)+":"+texto);
                  DistTravelled[4] = texto.toDouble();
                  DistTravelled[3] = texto.toDouble();
                  sendPid(canId, DistTravelled);
                  delay(100); 
            }else if(opcion=="0x0D"){
                  vVehiculeSpeed = texto.toDouble();
                  imprimirPid(pidName(0x0D),pidUnits(0x0D), texto);
                  SERIAL_PORT_MONITOR.println(pidName(0x0D)+"-"+pidUnits(0x0D)+":"+texto);
                  VehiculeSpeed[3]  = texto.toDouble();
                  sendPid(canId, VehiculeSpeed); 
                  delay(100);
            }else if(opcion=="0x05"){
                  vCoolTemperatura = texto.toDouble();
                  imprimirPid(pidName(0x05),pidUnits(0x05), texto);
                  SERIAL_PORT_MONITOR.println(pidName(0x05)+"-"+pidUnits(0x05)+":"+texto);
                  CoolantTemp[3] = texto.toDouble();
                  sendPid(canId, CoolantTemp); 
                  delay(100);
            }else if(opcion=="0x1F"){
                  vEngineRunTime = texto.toDouble();
                  imprimirPid(pidName(0x1F),pidUnits(0x1F), texto);
                  SERIAL_PORT_MONITOR.println(pidName(0x1F)+"-"+pidUnits(0x1F)+":"+texto);
                  EngRunTime[4] = texto.toDouble();
                  EngRunTime[3] = texto.toDouble();
                  sendPid(canId, EngRunTime); 
                  delay(100);
            }else if(opcion=="0x11"){
                  vThrottlePosition = texto.toDouble();
                  imprimirPid(pidName(0x11),pidUnits(0x11), texto);
                  SERIAL_PORT_MONITOR.println(pidName(0x11)+"-"+pidUnits(0x11)+":"+texto);
                  Throttle[3] = texto.toDouble();
                  sendPid(canId, Throttle); 
                  delay(100);
            }else if(opcion=="0x0F"){
                  vIAT = texto.toDouble();
                  imprimirPid(pidName(0x0F),pidUnits(0x0F), texto);
                  SERIAL_PORT_MONITOR.println(pidName(0x0F)+"-"+pidUnits(0x0F)+":"+texto);
                  IAT[3] = texto.toDouble();
                  sendPid(canId, IAT); 
                  delay(100);
            }else if(opcion=="0x10"){
                  vMAF = texto.toDouble();
                  imprimirPid(pidName(0x10),pidUnits(0x10), texto);
                  SERIAL_PORT_MONITOR.println(pidName(0x10)+"-"+pidUnits(0x10)+":"+texto);
                  MAF[4] = texto.toDouble();
                  MAF[3] = texto.toDouble();
                  sendPid(canId, MAF); 
                  delay(100);
            }else if(opcion=="0x0B"){
                  vMAP = texto.toDouble();
                  imprimirPid(pidName(0x0B),pidUnits(0x0B), texto);
                  SERIAL_PORT_MONITOR.println(pidName(0x0B)+"-"+pidUnits(0x0B)+":"+texto);
                  MAP[3] = texto.toDouble();
                  sendPid(canId, MAP); 
                  delay(100);
            }else if(opcion=="0x2F"){
                  vFuelTankLevel = texto.toDouble();
                  imprimirPid(pidName(0x2F),pidUnits(0x2F), texto);
                  SERIAL_PORT_MONITOR.println(pidName(0x2F)+"-"+pidUnits(0x2F)+":"+texto);
                  FuelTankLevel[3] = texto.toDouble();
                  sendPid(canId, FuelTankLevel); 
                  delay(100);
            }else if(opcion=="0x0C"){
                  vEngineSpeed = texto.toDouble();
                  imprimirPid(pidName(0x0C),pidUnits(0x0C), texto);
                  SERIAL_PORT_MONITOR.println(pidName(0x0C)+"-"+pidUnits(0x0C)+":"+texto);
                  EngineSpeed[4] = texto.toDouble();
                  EngineSpeed[3] = texto.toDouble();
                  sendPid(canId, EngineSpeed); 
                  delay(100);
            }else{
                  imprimirMensaje("Pid desconocido");
                  SERIAL_PORT_MONITOR.println(" Sin datos");
            }  
         
          token = strtok(NULL,s);
        }
        
      }      

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
/* =========================================
 * Function imprimirMensaje(String)
 * line2 
   ========================================= */
void imprimirMensaje(String texto){
    lcd.clear();
    lcd.setCursor(0,0);
    lcd.print(">SimOBD2-IIIS<");
    lcd.setCursor(0,1);
    lcd.print(texto);
}
/* =========================================
 * Function imprimirPid(String,String, String)
 * line2 
   ========================================= */
void imprimirPid(String NamePid, String UnidadPid, String valorPid){
    lcd.clear();
    lcd.setCursor(0,0);
    lcd.print(NamePid);
    lcd.setCursor(0,1);
    lcd.print(UnidadPid+" "+valorPid);
}

/* =========================================
 * Function apagarDisplay()
 * Apaga el LCD / luz de fondo off 
   ========================================= */
void apagarDisplay(){
  lcd.noBacklight();
  SERIAL_PORT_MONITOR.println("Display apagado");
}

/* =========================================
 * Function encederDisplay()
 * enciende el LCD / luz de fondo on 
   ========================================= */
void encenderDisplay(){
  lcd.backlight(); 
  SERIAL_PORT_MONITOR.println("Display encendido");
}

/* =========================================
 * Function limpiarDisplay()
 * Clear el LCD / Display none
   ========================================= */
void limpiarDisplay(){
  lcd.clear();
  SERIAL_PORT_MONITOR.println("Display clear");
}

/* =========================================
 * Function pidName(pid HEX)
 * retorna el nombre del PID
   ========================================= */
String pidName(uint8_t pid)
{
  if (pid > 0x5f) {
    return "Unknown";
  }

  #ifdef __AVR__
    const char* pgmName = pgm_read_ptr(&PID_NAME_MAPPER[pid]);
    String name;
  
    if (pgmName != NULL) {
      while (char c = pgm_read_byte(pgmName++)) {
        name += c;
      }
    }
  
    return name;
  #else
    return PID_NAME_MAPPER[pid];
  #endif
}
/* =========================================
 * Function pidUnits(pid HEX)
 * retorna la unidad del PID
   ========================================= */
String pidUnits(uint8_t pid)
{
  if (pid > 0x5f) {
    return "";
  }

#ifdef __AVR__
  const char* pgmUnits = pgm_read_ptr(&PID_UNIT_MAPPER[pid]);
  String units;

  if (pgmUnits != NULL) {
    while (char c = pgm_read_byte(pgmUnits++)) {
      units += c;
    }
  }

  return units;
#else
  return PID_UNIT_MAPPER[pid];
#endif
}

/* =========================================
 * Function sendPid(0x16, __pid[X]) 
 * Envia el Pid por el puerto CAN
   ========================================= */
void sendPid(char canId,  unsigned char __pid[]) {
    SERIAL_PORT_MONITOR.print(".::Send Pid: "+canId);
    for(int i=0; i< sizeof(__pid); i++){
        SERIAL_PORT_MONITOR.print(__pid[i]);
    }
    SERIAL_PORT_MONITOR.println();
    CAN.sendMsgBuf(canId, 0, 8, __pid);
}
   
