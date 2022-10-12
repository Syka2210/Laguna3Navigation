#include <ACAN2515.h>
#include <SPI.h>
#include <Keyboard.h>


Add jpystick and buttons commands!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

//——————————————————————————————————————————————————————————————————————————————
// The Pico has two SPI peripherals, SPI and SPI1. Either (or both) can be used.
// The are no default pin assignments to these must be set explicitly.
//——————————————————————————————————————————————————————————————————————————————
static const byte MCP2515_SCK  = 18 ; // SCK input of MCP2515
static const byte MCP2515_MOSI = 19 ; // SDI input of MCP2515
static const byte MCP2515_MISO = 16 ; // SDO output of MCP2517

static const byte MCP2515_CS  = 17 ;  // CS input of MCP2515 (adapt to your design)
static const byte MCP2515_INT = 12 ;  // INT output of MCP2515 (adapt to your design)
//——————————————————————————————————————————————————————————————————————————————
//  MCP2515 Driver object
//——————————————————————————————————————————————————————————————————————————————
ACAN2515 can (MCP2515_CS, SPI, MCP2515_INT) ;
CANMessage canMsg;
//——————————————————————————————————————————————————————————————————————————————
//  MCP2515 Quartz: adapt to your design
//——————————————————————————————————————————————————————————————————————————————
static const uint32_t QUARTZ_FREQUENCY = 8UL * 1000UL * 1000UL ; // 8 MHz
//——————————————————————————————————————————————————————————————————————————————
//  Pins for the 4x1 keypad that will be used for controlling the navigation bar of the 
//  Android device, due to the fact that it will not be accesible (frame)
//——————————————————————————————————————————————————————————————————————————————
const int backButton = 7;      // 4x1 keyboard -- button assigned for back 
const int homeButton = 8;      // 4x1 keyboard -- button assigned for home
const int appSwitchButton = 9; // 4x1 keyboard -- button assigned for app switch button
//——————————————————————————————————————————————————————————————————————————————
//  Disable or enable the log Function and the second UART data line (for debuging)
//  on pins GP0 and GP1.
//——————————————————————————————————————————————————————————————————————————————
bool logFileActive = true;
bool secondSerialActive = true;

unsigned char dataPackage[200];
char dataPackegePos;
int dataPackageLength;

char high_box;
String freq_box;
String last_menu;

String endString = "end_string";

String receivedMessage;
//--- Save the last main messages received in order to resend them if the app request it
String lastSource;
String lastMessage;
String lastVolume;

void sendToSerial(String viewType="zz", String frequencyType="zz", char highBox='z', String box_1x1="zz", String box_1x2="zz", String box_1x3="zz", String box_2x1="zz", String box_2x2="zz", String box_2x3="zz", String box_3x1="zz", String box_3x2="zz", String box_3x3="zz", char bass_sign='_', char bass_val='_', char treble_sign='_', char treble_val='_');

void setup() {
  //--- Start serial at highes baud rate for Android comunication
  Serial.begin(250000);
  Serial1.begin(250000);
  //--- There are no default SPI pins so they must be explicitly assigned
  SPI.setSCK(MCP2515_SCK);
  SPI.setTX(MCP2515_MOSI);
  SPI.setRX(MCP2515_MISO);
  SPI.setCS(MCP2515_CS);
  //--- Begin SPI
  SPI.begin () ;  
  //--- Configure ACAN2515
  Serial.println ("Configure ACAN2515") ;
  Serial1.println ("Configure ACAN2515") ;
  ACAN2515Settings settings (QUARTZ_FREQUENCY, 500UL * 1000UL) ; // CAN bit rate 500 kb/s - autovehicle frequency
  settings.mReceiveBufferSize = 100; // Buffer size set to 100 from 32
  settings.mRequestedMode = ACAN2515Settings::NormalMode ; // Select normal mode
  const uint16_t errorCode = can.begin (settings, [] { can.isr () ; }) ;
  //--- Turn the board LED on to check for functionality
  pinMode(LED_BUILTIN, OUTPUT);
  digitalWrite(LED_BUILTIN, HIGH);

  pinMode(backButton, INPUT_PULLUP);
  pinMode(homeButton, INPUT_PULLUP);
  pinMode(appSwitchButton, INPUT_PULLUP);
}

void logFunction(){
  //if (canMsg.id == 289){
      Serial.print("LOG-FILE:FRAME ID=");
      //Serial1.print("LOG-FILE:FRAME ID="); 
      Serial.print(canMsg.id);
      //Serial1.print(canMsg.id);
      for (int i=0; i<=7; i++){
        Serial.print(":");
        //Serial1.print(":");
        if (canMsg.data[i] < 16){
          Serial.print('0');
          //Serial1.print('0');
        } 
        Serial.print(canMsg.data[i], HEX);
        //Serial1.print(canMsg.data[i], HEX);
      }
      Serial.print(":");
      //Serial1.print(":);
      Serial.println(endString);
      //Serial1.println(endString);
  //}
}

void sendToSerial(String viewType, String frequencyType, char highBox, String box_1x1, String box_1x2, String box_1x3, String box_2x1, String box_2x2, String box_2x3, String box_3x1, String box_3x2, String box_3x3, char bass_sign, char bass_val, char treble_sign, char treble_val){
  String messagePack = "";
  if (viewType == "Error"){
    messagePack = viewType + ":" + frequencyType + ":" + endString;
    Serial.println(messagePack);
    //Serial1.println(messagePack);
  }else if (viewType == "requestMessage"){
    if (lastSource.length() > 0){
      Serial.println(lastSource);
      //Serial1.println(lastSource);
    }else{
      Serial.println("Error:last source string is empty!:end_string");
      Serial1.println("Error:last source string is empty!:end_string");
    }
    if (lastVolume.length() > 0){
      Serial.println(lastVolume);
      //Serial1.println(lastVolume);  
    }else{
      Serial.println("Error:last volume string is empty!:end_string");
      //Serial.println("Error:last volume string is empty!:end_string");
    }
    if (lastMessage.length() > 0){
      Serial.println(lastMessage);
      //Serial1.println(lastMessage);
    }else{
      Serial.println("Error:last message string is empty!:end_string");
      //Serial.println("Error:last message string is empty!:end_string");
    }
  }else if (viewType == "HighlightedBox"){
    messagePack = viewType + ":" + String(highBox) + ":" + endString;
    Serial.println(messagePack);
    //Serial1.println(messagePack);
  }else if (viewType == "view_c1"){
    messagePack = viewType + ":" + box_1x1 + ":" + box_1x2 + ":" + endString;
    lastMessage = messagePack;
    Serial.println(messagePack);
    //Serial1.println(messagePack);
  }else if (viewType == "freq_type"){
    messagePack = viewType + ":" + frequencyType + ":" + endString;
    Serial.println(messagePack);
    //Serial1.println(messagePack);
  }else if (viewType == "view_c3"){
    messagePack = viewType + ":" + box_1x1 + ":" + box_1x2 + ":" + box_1x3 + ":" + box_2x1 + ":" + box_2x2 + ":" + box_2x3 + ":" + endString;
    lastMessage = messagePack;
    Serial.println(messagePack);
    //Serial1.println(messagePack);
  }else if (viewType == "source"){
    messagePack = viewType + ":" + frequencyType + ":" + endString;
    Serial.println(messagePack);
    //Serial1.println(messagePack);
  }else if (viewType == "view_41"){
    messagePack = viewType + ":" + frequencyType + ":" + String(highBox) + ":" + box_1x1 + ":" + box_1x2 + ":" + box_1x3 + ":" + endString;
    lastMessage = messagePack;
    Serial.println(messagePack);
    //Serial1.println(messagePack);
  }else if (viewType == "device_volume"){
    messagePack = viewType + ":" + box_1x1 + ":" + endString;
    Serial.println(messagePack);
    //Serial1.println(messagePack);
  }else if (viewType == "view_43"){
    messagePack = viewType + ":" + frequencyType + ":" + String(highBox) + ":" + box_1x1 + ":" + box_1x2 + ":" + box_1x3 + ":" + box_2x1 + ":" + box_2x2 + ":" + box_2x3 + ":" + box_3x1 + ":" + box_3x2 + ":" + box_3x3 + ":" + endString;
    lastMessage = messagePack;
    Serial.println(messagePack);
    //Serial1.println(messagePack);
  }else if (viewType == "view_52"){
    messagePack = viewType + ":" + frequencyType + ":" + endString;
    lastMessage = messagePack;
    Serial.println(messagePack);
    //Serial1.println(messagePack);
  }else if (viewType == "view_63"){
    messagePack = viewType + ":" + box_1x1 + ":" + box_1x2 + ":" + box_1x3 + ":" + box_2x1 + ":" + box_2x2 + ":" + box_2x3 + ":" + endString;
    lastMessage = messagePack;
    Serial.println(messagePack);
    //Serial1.println(messagePack);
  }else if (viewType == "view_70"){
    messagePack = viewType + ":" + frequencyType + ":" + String(highBox) + ":" + endString;
    Serial.println(messagePack);
    //Serial1.println(messagePack);
  }else if (viewType == "view_73"){
    messagePack = viewType + ":" + String(highBox) + ":" + box_1x1 + ":" + box_1x2 + ":" + box_1x3 + ":" + box_2x1 + ":" + box_2x2 + ":" + box_2x3 + ":" + bass_sign + ":" + bass_val + ":" + treble_sign + ":" + treble_val + ":" + ":" + endString;
    lastMessage = messagePack;
    Serial.println(messagePack);
    //Serial1.println(messagePack);
  }
  else{
    Serial.print("viewType ");
    //Serial1.print("viewType ");
    Serial.print(viewType);
    //Serial1.print(viewType);
    Serial.println(" not found in sendToSerial method");
    //Serial1.println(" not found in sendToSerial method");
  }
}

bool bytesToArray(int arrayLength){
  int arrayPos = 0;
  bool loop = true;
  logFunction();
  for (int i = 2; i <= 7; i++){
    dataPackage[arrayPos] = canMsg.data[i];
    arrayPos++;
  }
  while (loop == true){
    if (can.receive (canMsg)){
      if (canMsg.id == 289){
        if (canMsg.data[0] == 0x10){
          Serial.println("Info:FAILED:Received new data package before ariving to the end of the previous one");
          break;
        }
        logFunction();
        for (int i = 1; i <= 7; i++){    
          dataPackage[arrayPos] = canMsg.data[i];
          if (arrayPos == arrayLength){
            loop = false;
            break;
          }else{
            arrayPos++;        
          }
        }
      }
    }
  }
  
  for (int i = 0; i <= arrayPos; i++){
    Serial.print(dataPackage[i], HEX);
    Serial.print(":");
  }
  Serial.println();

  if (arrayPos != arrayLength){
    Serial.print("Info:Error: data package length not identical to array => ");
    Serial.print(arrayPos);
    Serial.println(endString);
  }else{
    Serial.println("Info:Package length correct!!!");
  }

  Serial.println("------------------------------------------------------------");

  if (dataPackage[1] == 0x00 && dataPackage[0] == 0x20){
    highlighted_box(dataPackage[11]);
    return true;
  }
    
  if (dataPackage[1] == 0xC1 && dataPackage[5] != 0x00){
    Serial.println("entering view_C1");
    view_C1(arrayLength);
    return true;
  }
  if (dataPackage[1] == 0xC1 && dataPackage[5] == 0x00){
    freq_type(arrayLength);
    return true;
  }
  if (dataPackage[1] == 0xC3){
    Serial.println("entering view_C3");
    view_C3(arrayLength);
    return true;
  }
  
  if (dataPackage[1] == 0x41 && dataPackage[0] == 0x25){
    view_41(arrayLength);
    return true;
  }
  if (dataPackage[1] == 0x41 && dataPackage[0] == 0x35){
    view_41_main_volume(arrayLength, false);
    return true;
  }
  if (dataPackage[1] == 0x00 && dataPackage[0] == 0x08){
    view_41_main_volume(arrayLength, true);
    return true;
  }
  if (dataPackage[1] == 0x43){
    // --- Check to see if the data package is the Memory menu, or the normal radio display
    // and send the second parameter true for memory or false.
    if (dataPackage[2 == 0x7A]){
      view_43(arrayLength, true);      
    }else view_43(arrayLength, false);
    return true;
  }
  if (dataPackage[1] == 0x52){
    view_52(arrayLength);
    return true;
  }
  if (dataPackage[1] == 0x63){
    view_63(arrayLength);
    return true;
  }
  if (dataPackage[1] == 0x73){
    view_73(arrayLength);
    return true;
  }

  return false;
}

//——————————————————————————————————————————————————————————————————————————————
//  Get the array position for the text matrix (3x3)
//——————————————————————————————————————————————————————————————————————————————
int get_array_position(int columnNr, int rowNr) {
  int arrayPos = 0;
  if (columnNr == 1)
  {
    if (rowNr == 1) arrayPos = 1;
    else if (rowNr == 2) arrayPos = 2;
    else if (rowNr == 3) arrayPos = 3;
  }
  else if (columnNr == 2)
  {
    if (rowNr == 1) arrayPos = 4;
    else if (rowNr == 2) arrayPos = 5;
    else if (rowNr == 3) arrayPos = 6;
  }
  else if (columnNr == 3)
  {
    if (rowNr == 1) arrayPos = 7;
    else if (rowNr == 2) arrayPos = 8;
    else if (rowNr == 3) arrayPos = 9;
  }
  return arrayPos;
}

//——————————————————————————————————————————————————————————————————————————————
//  Find and save the highlighted box
//——————————————————————————————————————————————————————————————————————————————
void highlighted_box(char value){
  if (value == 0x20){
    sendToSerial("HighlightedBox", "", '1');
    high_box = '1';
  }else if (value == 0x21){
    sendToSerial("HighlightedBox", "", '2');
    high_box = '2';
  }else if (value == 0x22){
    sendToSerial("HighlightedBox", "", '3');
    high_box = '3';    
  }else if (value == 0x23){
    sendToSerial("HighlightedBox", "", '4');
    high_box = '4';
  }else sendToSerial("Error", "Highlighted_box function received wrong data");
}

//Channel number function -- CHECKED
String chan_number(char recv_char){
  String ch_nr;
  switch(recv_char){
    case 0x10:
      ch_nr = "1";
      break;
    case 0x11:
      ch_nr = "2";
      break;
    case 0x12:
      ch_nr = "3";
      break;
    case 0x13:
      ch_nr = "4";
      break;
    case 0x14:
      ch_nr = "5";
      break;
    case 0x15:
      ch_nr = "6";
      break;
    case 0x46:
      ch_nr = "7";
      break;
    case 0x47:
      ch_nr = "8";
      break;
    case 0x48:
      ch_nr = "9";
      break;
    case 0x49:
      ch_nr = "10";
      break;
    case 0x4A:
      ch_nr = "11";
      break;
    case 0x4B:
      ch_nr = "12";
      break;
    default:
      ch_nr = "0";
      break;
    }

  return ch_nr;
}

String icons(char input){
  String iconOutput;

  switch (input){
    case 0x01:
      iconOutput = "icon_Audio_settings";
      break;
    case 0x02:
      iconOutput = "icon_Phone_settings";
      break;
    case 0x03:
      iconOutput = "icon_System_settings";
      break;
    case 0x04:
      iconOutput = "icon_Bluetooth";
      break;
    case 0x05:
      iconOutput = "icon_Language";
      break;
    case 0x06:
      iconOutput = "icon_Sound_optimization";
      break;
    case 0x07:
      iconOutput = "icon_Musical_atmosphere";
      break;
    case 0x08:
      iconOutput = "icon_Display_CD_time";
      break;
    case 0x09:
      iconOutput = "icon_Radio_functions";
      break;
    case 0x0A:
      iconOutput = "icon_Serious_classics";
      break;
    case 0x0B:
      iconOutput = "icon_Varied_speech";
      break;
    case 0x0C:
      iconOutput = "icon_News";
      break;
    case 0x0D:
      iconOutput = "icon_Sport";
      break;
    case 0x0E:
      iconOutput = "icon_Pop_music";
      break;
    case 0x1A:
      iconOutput = "icon_Pair_phone";
      break;
    case 0x12:
      iconOutput = "icon_Music_note";
      break;
    case 0x13:
      iconOutput = "icon_Vehicle_phonebook";
      break;
    case 0x14:
      iconOutput = "icon_Mobile_phonebook";
      break;
    case 0x15:
      iconOutput = "icon_Call_history";
      break;
    case 0x16:
      iconOutput = "icon_Mail_box";
      break;
    case 0x17:
      iconOutput = "icon_Directory_management";
      break;
    case 0x18:
      iconOutput = "icon_Other_audio_settings";
      break;
    case 0x1D:
      iconOutput = "icon_Missed_calls";
      break;
    case 0x1E:
      iconOutput = "icon_Received_calls";
      break;
    case 0x1F:
      iconOutput = "icon_Dialed_numbers";
      break;
    case 0x20:
      iconOutput = "icon_Put_on_hold";
      break;
    case 0x21:
      iconOutput = "icon_USB";
      break;
    case 0x24:
      iconOutput = "icon_Volume";
      break;
    case 0x25:
      iconOutput = "icon_Ringtone";
      break;
    case 0x26:
      iconOutput = "icon_Cercle_full";
      break;
    case 0x27:
      iconOutput = "icon_Cercle_empty";
      break;
    case 0x28:
      iconOutput = "icon_Checked_box";
      break;
    case 0x29:
      iconOutput = "icon_Unchecked_box";
      break;
    case 0x2A:
      iconOutput = "icon_Folder";
      break;
    case 0x2B:
      iconOutput = "icon_Voice_prompt_volume";
      break;
    case 0x40:
      iconOutput = "icon_1";
      break;
    case 0x41:
      iconOutput = "icon_2";
      break;
    case 0x42:
      iconOutput = "icon_3";
      break;
    case 0x43:
      iconOutput = "icon_4";
      break;
    case 0x44:
      iconOutput = "icon_5";
      break;
    case 0x60:
      iconOutput = "icon_Adaptation_volume";
      break;
    case 0x62:
      iconOutput = "icon_Emergency";
      break;
    default:
      break;
  }
  
  return iconOutput;
}
//—————————————————————————————————————————————————————————————————————————————————————————————————————————————————
//  A one row / two columns view, usually with an icon in the first column, and text on the second
//  There may be instances where in the second column we have additional icons (mainly in the settings - bluetooth)
//  ||icon||TEXT||
//—————————————————————————————————————————————————————————————————————————————————————————————————————————————————
// ADD A TEXT CONCATENATION FOR FREQUENCY TYPE VISIBLE INSTEAD OF THE ICONS
//—————————————————————————————————————————————————————————————————————————————————————————————————————————————————
void view_C1(int arrayLength){
  String viewC1[3];
  int arrayPos = 1;
  bool start_titles = false;
  char character;
  String iconText;
  for (int i = 19; i <= arrayLength; i++){
    if (dataPackage[i] == 0xF1){
      viewC1[arrayPos] = icons(dataPackage[i+1]);
      continue;
    }
    // Check for column change
    if (dataPackage[i] == 0x00 && dataPackage[i+1] == 0x00 && i < arrayLength-3){
      arrayPos = 2;
      start_titles = true;
      // Skip the 0x00 column separator bits (2 or 3)      
      if (dataPackage[i+2] == 0x00) i = i+2;
      else i = i+1;      
      continue;
    }
    if (start_titles == true && dataPackage[i] >= 0x20 && dataPackage[i] <= 0x7E){
      character = dataPackage[i];
      viewC1[arrayPos].concat(character);
    }
  }
  sendToSerial("view_c1", "", '_', viewC1[1], viewC1[2]);
}
//—————————————————————————————————————————————————————————————————————————————————————————————————————————————————
//  A HIDDEN one row / two columns view, with text on both columns for the frequency type box on the view_41 and
//  view_43 functions. We will save the first box text in a global variable to be used in those functions.
//  || text ||TEXT||
//—————————————————————————————————————————————————————————————————————————————————————————————————————————————————
void freq_type(int arrayLength){
  String freq[3];
  int arrayPos = 1;
  bool start_titles = false;
  char character;
  String iconText;
  for (int i = 20; i <= arrayLength; i++){
    // Check for column change
    if (dataPackage[i] == 0x00 && dataPackage[i+1] == 0x00 && i < arrayLength-3){
      arrayPos = 2;
      // Skip the 0x00 column separator bits (2 or 3)      
      if (dataPackage[i+2] == 0x00) i = i+2;
      else i = i+1;      
      continue;
    }
    if (dataPackage[i] >= 0x20 && dataPackage[i] <= 0x7E){
      character = dataPackage[i];
      freq[arrayPos].concat(character);
    }
  }
  freq_box = freq[1];
  sendToSerial("freq_type", freq[1]);
}
//—————————————————————————————————————————————————————————————————————————————————————————————————————————————————
//  A three row / three columns view, usually with icons in the first column, and text on the second
//  There may be instances where in the second column we have additional icons (mainly in the settings - bluetooth)
//  ||icon||TEXT||
//  ||icon||TEXT||
//  ||icon||TEXT||
//—————————————————————————————————————————————————————————————————————————————————————————————————————————————————
void view_C3(int arrayLength){
  String viewC3[7];
  String iconText;
  int arrayPos = 1;
  int rowNr = 1;
  int columnNr = 1;
  bool start_titles = false;
  char character;

  for (int i = 19; i <= arrayLength; i++){
    // Set the String array possition 
    arrayPos = get_array_position(columnNr, rowNr);
    // Save the icons in the first column
    if (dataPackage[i] == 0xF1){
      //iconText = icons(dataPackage[i+1]);
      viewC3[arrayPos] = icons(dataPackage[i+1]);
      i = i+1;
      continue;
    }
    // Check for row change and skip 2 bits (0xF0 && 0x0D)
    if (dataPackage[i] == 0xF0 && dataPackage[i+1] == 0x0D){
      rowNr++;
      i = i+1;      
      continue;
    }
    // Check for column change
    if (dataPackage[i] == 0x00 && dataPackage[i+1] == 0x00 && i < arrayLength-3){
      columnNr = 2;
      rowNr = 1;
      start_titles = true;
      // Skip the 0x00 column separator bits (2 or 3)      
      if (dataPackage[i+2] == 0x00) i = i+2;
      else i = i+1;
      continue;
    }
    // Save the text in the second column DISREGARDING the aditional icons !!!!!!!!
    if (start_titles == true && dataPackage[i] >= 0x20 && dataPackage[i] <= 0x7E){
      character = dataPackage[i];
      viewC3[arrayPos].concat(character);
    }
  }
  // --- Saving the middle text from the menu so if we enter view_70 we know what we had selected.
  last_menu = viewC3[5];
  sendToSerial("view_c3", "", '_', viewC3[1], viewC3[2], viewC3[3], viewC3[4], viewC3[5], viewC3[6]);
}
//—————————————————————————————————————————————————————————————————————————————————————————————————————————————————
//  A one row / four columns view. The first column is one box with the frequency type used (FM, LW, PTY), and it is
//  received on a different data pack, that's why it will be saved on a global variable string. The second column is
//  for the frequency (text), as well as the third one is for the name of the channel. The last one is either a icon
//  identified with the 0xF0 preliminary bit, or a '-' char, with no preliminary identifier.
//  IT MIGHT also contain the source selected (radio, CD player, Auxiliary audio sources). That package comes without
//  column separator, so we will only search for text match after the end of the dataPackage interogation.
//  || FM || FREQUENCY || CHANELL NAME || CHANELL NR ||
//—————————————————————————————————————————————————————————————————————————————————————————————————————————————————
void view_41(int arrayLength){
  String view41[4];
  int arrayPos = 1;
  char character;

  //Search for the curent highlighted (selected) Box and save it in a global variable
  highlighted_box(dataPackage[16]);
  
  //Concatenate text and icons (one row)S
  for (int i = 20; i <= arrayLength; i++){
    // Check for column change
    if (dataPackage[i] == 0x00 && dataPackage[i+1] == 0x00 && i < arrayLength-3){
      arrayPos++;
      // Skip the 0x00 column separator bits (2 or 3)      
      if (dataPackage[i+2] == 0x00) i = i+2;
      else i = i+1;      
      continue;
    }
    // Save the channel number and skip the used bits (0xF2; 0x--)
    if (dataPackage[i] == 0xF2){
      character = dataPackage[i+1];
      view41[arrayPos] = chan_number(character);
      i = i+1;
      continue;
    }
    // Concatenate the text in the coresponding array position
    if (dataPackage[i] >= 0x20 &&  dataPackage[i] <= 0x7E){
      character = dataPackage[i];
      view41[arrayPos].concat(character);
    }
  }
  //Search if the String has the Source menu
  // Save the last instance of source in a global variable in case of a Android app restart and request of info
  if (view41[1].equals("Radio")){
    sendToSerial("source", "Radio");
    lastSource = String("source:Radio:") + endString;
    return;
  }
  if (view41[1].equals("CD player")){
    sendToSerial("source", "cd_player");
    lastSource = String("source:cd_player:") + endString;
    return;
  }
  if (view41[1].equals("Auxiliary audio sources")){
    sendToSerial("source", "auxiliary audio sources");  
    lastSource = String("source:auxiliary audio sources:") + endString;
    return;
  }
  //Send the String
  sendToSerial("view_41", freq_box, high_box, view41[1], view41[2], view41[3]);
}

//——————————————————————————————————————————————————————————————————————————————
//  Returns the main volume in text format (or Pause text) 
//——————————————————————————————————————————————————————————————————————————————
void view_41_main_volume(int arrayLength, bool unpause){
  String volume = String();
  char character;
  if (unpause == true){
    sendToSerial("device_volume", "", '_', "return");
    return;
  }
  for (int i = 7; i <= arrayLength; i++){
    if (dataPackage[i] >= 0x20 &&  dataPackage[i] <= 0x7E){
      character = dataPackage[i];
      volume = volume + character;
    }
  }
  //--- Serial transmit the volume
  sendToSerial("device_volume", "", '_', volume);
  // Save the last volume value in a global variable in case of a Android app restart and request of info
  lastVolume = "device_volume" + ":" + volume + ":" + endString;
  return;
}

//—————————————————————————————————————————————————————————————————————————————————————————————————————————————————
//  A three rows / four columns view. The first column is one box with the frequency type used (FM, LW, PTY), and it is
//  received on a different data pack, that's why it will be saved on a global variable string. The second column is
//  for the frequency (text), as well as the third one is for the name of the channel. The last one is either a icon
//  identified with the 0xF0 preliminary bit, or a '-' char, with no preliminary identifier.
//        || FREQUENCY || CHANELL NAME || CHANELL NR ||
//  || FM || FREQUENCY || CHANELL NAME || CHANELL NR ||
//        || FREQUENCY || CHANELL NAME || CHANELL NR ||
//—————————————————————————————————————————————————————————————————————————————————————————————————————————————————
void view_43(int arrayLength, bool memory){
  String view43[10];
  char character;
  int startByt = 0;
  int colNum = 1;
  int rowNum = 1;
  int arrayPos;

  //Search for the curent highlighted (selected) Box and save it in a global variable
  highlighted_box(dataPackage[16]);

  if (memory == true){
    startByt = 31;
  }else startByt = 20;

  //Concatenate text (three rows)
  for (int i = startByt; i <= arrayLength; i++){
    // Set the string array position
    arrayPos = get_array_position(colNum, rowNum);
    // Check for column change
    if (dataPackage[i] == 0x00 && dataPackage[i+1] == 0x00 && i < arrayLength-3){
      colNum++;
      rowNum = 1;
      // Skip the 0x00 column separator bits (2 or 3)      
      if (dataPackage[i+2] == 0x00) i = i+2;
      else i = i+1;      
      continue;
    }
    // Check for row change and skip 2 bits (0xF0 && 0x0D)
    if (dataPackage[i] == 0xF0 && dataPackage[i+1] == 0x0D){
      rowNum++;
      i = i+1;      
      continue;
    }
    // Save the channel number and skip the used bits (0xF2; 0x--)
    if (dataPackage[i] == 0xF2){
      character = dataPackage[i+1];
      view43[arrayPos] = chan_number(character);
      i = i+1;
      continue;
    }
    // Concatenate the text in the coresponding array position
    if (dataPackage[i] >= 0x20 &&  dataPackage[i] <= 0x7E){
      character = dataPackage[i];
      view43[arrayPos].concat(character);
    }
  }
  //Send the string
  if (memory == false){
    sendToSerial("view_43", freq_box, high_box, view43[1], view43[2], view43[3], view43[4], view43[5], view43[6], view43[7], view43[8], view43[9]);
  }else sendToSerial("view_43", freq_box, high_box, view43[4], view43[5], view43[6], view43[7], view43[8], view43[9], view43[1], view43[2], view43[3]);
}
//—————————————————————————————————————————————————————————————————————————————————————————————————————————————————
//  A INFO view. Only one box that receives characters. It might receive some icons, have to investigate.
//        || TEXT          ||
//        || TEXT          ||
//        || TEXT          ||
//—————————————————————————————————————————————————————————————————————————————————————————————————————————————————
void view_52(int arrayLength){
  String info_box = String();
  char character;
  bool end_text = false;
  for (int i = 17; i <= arrayLength; i++){
    // Check for column change
    if (dataPackage[i] == 0x00 && dataPackage[i+1] == 0x00 && i < arrayLength-3){
      end_text = true;
      // Skip the 0x00 column separator bits (2 or 3)      
      if (dataPackage[i+2] == 0x00) i = i+2;
      else i = i+1;      
      // We break the for loop because we saved all the characters we need
      break;
    }
    if (dataPackage[i] >= 0x20 &&  dataPackage[i] <= 0x7E){
      character = dataPackage[i];
      info_box.concat(character);
    }
  }
  sendToSerial("view_52", info_box);
}
//—————————————————————————————————————————————————————————————————————————————————————————————————————————————————
//  A three rows on the first column with a only one box on the second column view. 
//        || TEXT ||                 ||
//        ||------||                 ||
//        || TEXT ||                 ||
//        ||------||                 ||
//        || TEXT ||                 ||
//—————————————————————————————————————————————————————————————————————————————————————————————————————————————————
void view_63(int arrayLength){
  String view63[10];
  int arrayPos;
  int colNum = 1;
  int rowNum = 1;
  char character;
  for (int i = 19; i <= arrayLength; i++){
    // Set the string array position
    arrayPos = get_array_position(colNum, rowNum);
    // Check for column change
    if (dataPackage[i] == 0x00 && dataPackage[i+1] == 0x00 && i < arrayLength-3){
      colNum++;
      rowNum = 1;
      // Skip the 0x00 column separator bits (2 or 3)      
      if (dataPackage[i+2] == 0x00) i = i+2;
      else i = i+1;      
      continue;
    }
    // Check for row change and skip 2 bits (0xF0 && 0x0D)
    if (dataPackage[i] == 0xF0 && dataPackage[i+1] == 0x0D){
      rowNum++;
      i = i+1;      
      continue;
    }
    // On the information text box, because we don't have multiple rows, the text lines are separated only with the 0x0D bit.
    if ( colNum == 2 && dataPackage[i] == 0x0D){
      rowNum++;
      continue;
    }
    // Concatenate the text in the coresponding array position
    if (dataPackage[i] >= 0x20 &&  dataPackage[i] <= 0x7E){
      character = dataPackage[i];
      view63[arrayPos].concat(character);
    }
  }
  sendToSerial("view_63", "", '_', view63[1], view63[2], view63[3], view63[4], view63[5], view63[6]);
}
//—————————————————————————————————————————————————————————————————————————————————————————————————————————————————
//  A progress bar that displays the level of volume for different setting of the audio. 
//        || Bluetooth volume      || +20 ||
//        ||   ---------------            ||
//—————————————————————————————————————————————————————————————————————————————————————————————————————————————————
void view_70(int arrayLength){
  char value;
  value = dataPackage[18];
  sendToSerial("view_70", last_menu, value);
}
//—————————————————————————————————————————————————————————————————————————————————————————————————————————————————
//  The musical atmosphere menu, which has a selection circle (empty or full), and on the Bass/treble option there
//  are two level indicators for those settings.  
//        || ICON ||  "Rock"         ||  --   --
//        || ICON ||  Bass/treble    ||  --   --
//        || ICON ||  "Voice"        ||  --   --
//                                        B   T
//—————————————————————————————————————————————————————————————————————————————————————————————————————————————————
void view_73(int arrayLength){
  String view73[9];
  char bass_sign = '_';
  char treble_sign = '_';
  char bass = 0;
  char treble = 0;
  char character;
  int arrayPos = 4;

  //Search for the curent highlighted (selected) Box and save it in a global variable
  highlighted_box(dataPackage[16]);

  for (int i = 20; i <= arrayLength; i++){
    // Check for row change and skip 2 bits (0xF0 && 0x0D)
    if (dataPackage[i] == 0xF0 && dataPackage[i+1] == 0x0D){
      arrayPos++;
      i = i+1;      
      continue;
    }
    // Check for arriving at the bass/treble values and save the values or skip the data package if there are none
    if (dataPackage[i] == 0x00 && dataPackage[i+1] == 0x00 && i < arrayLength-3){ 
      if (dataPackage[i+2] != 0x00 && dataPackage[i+3] != 0x00){
        //here we save the values in the variables
        if (dataPackage[i+2] <= 0x09){
          bass_sign = '+';
          bass = dataPackage[i+2];
        }else if (dataPackage[i+2] >= 0xF7){
          bass_sign = '-';
          bass = dataPackage[i+2];
        }
        if (dataPackage[i+3] <= 0x09){
          treble_sign = '+';
          treble = dataPackage[i+3];
        }else if (dataPackage[i+3] >= 0xF7){
          treble_sign = '-';
          treble = dataPackage[i+3];
        }
      }
      else break;
    }

    // Save the icons in the first column
    if (dataPackage[i] == 0xF1){
      //iconText = icons(dataPackage[i+1]);
      view73[arrayPos - 3] = icons(dataPackage[i+1]);
      i = i+1;
      continue;
    }
    // Concatenate the text in the coresponding array position
    if (dataPackage[i] >= 0x20 &&  dataPackage[i] <= 0x7E){
      character = dataPackage[i];
      view73[arrayPos].concat(character);
    }
  }

  sendToSerial("view_73", "", high_box, view73[1], view73[2], view73[3], view73[4], view73[5], view73[6], "", "", "", bass_sign, bass, treble_sign, treble);
}

//——————————————————————————————————————————————————————————————————————————————
//  Takes the frames from the navigation control unit (the joystick and the buttons)
//  and converts them in a Keyboard emulated data, in order to navigate through the Android
//  device.
//  The android device uses a third party app (Xposed Edge Pro) that converts some of these
//  commands to specific events (open app, open voice input for destination, etc.)
//     /‾‾‾‾‾‾‾|‾‾‾‾‾‾‾‾‾‾‾|‾‾‾‾‾‾‾\           //     /‾‾‾‾‾‾‾|‾‾‾‾‾‾‾‾‾‾‾|‾‾‾‾‾‾‾\ 
//    /  |     |    BACK   |     |  \          //    /   1    |     2     |    3   \ 
//   /   |_   /_____________\   _|   \         //   /        /_____________\        \ 
//  |________/               \________|        //  |________/  *    12      \________|
//  |       |       |||       |       |        //  |       |                 |       |  * 10 - rotate joystick right
//  |  MAP  |   __       __   | INFO  |        //  |   4   | 14    (16)      |   5   | ** 11 - rotate joystick left
//  | 2D/3D |   ‾‾       ‾‾   | ROUTE |        //  |       |     (CLIK)   15 |       |
//  |       |       |||       |       |        //  |       |                 |       |
//  |‾‾‾‾‾‾‾‾\               /‾‾‾‾‾‾‾‾|        //  |‾‾‾‾‾‾‾‾\       13   ** /‾‾‾‾‾‾‾‾|
//   \  DEST  \‾‾‾‾‾‾|‾‾‾‾‾‾/  MENU  /         //   \        \‾‾‾‾‾‾|‾‾‾‾‾‾/        /
//    \  *HOME | REP | LIG |  *SET  /          //    \    6   |  7  |  8  |    9   /
//     \_______|_____|_____|_______/           //     \_______|_____|_____|_______/
//——————————————————————————————————————————————————————————————————————————————
//  1 -                                          9 - "keypad:menu" (string)
//  2 - ESCPAE (keyboard)                       10 - "keypad:right" (string) *
//  3 -                                         11 - "keypad:left"  (string) * 
//  4 - *press once  - Google Maps              12 -    UP ARROW (keyboard)
//      *press twice - Waze              
//  5 - IF (Google Maps) THEN voice input       13 -  DOWN ARROW (keyboard)
//  6 -                                         14 -  LEFT ARROW (keyboard)
//  7 -                                         15 - RIGHT ARROW (keyboard)
//  8 - SCREEN BRIGHTNESS ????                  16 -       ENTER (keyboard)
//  
//  * We use this to select specific apps from the interface

//——————————————————————————————————————————————————————————————————————————————
void keyboardButtons(char firstBit){
      //KEY 1
      if(firstBit == 0x00){
        Keyboard.press('1');
        //Serial.println("1");
        delay(delayTime);
        Keyboard.releaseAll();
      }
      //KEY 2
      if(firstBit == 0x01){
        Keyboard.press(KEY_ESC);
        //Serial.println("KEY_ESC");
        delay(delayTime);
        Keyboard.releaseAll();
      }
      //KEY 3
      if(firstBit == 0x02){
        Keyboard.press('3');
        //Serial.println("3");
        delay(delayTime);
        Keyboard.releaseAll();
      }
      //KEY 5
      if(firstBit == 0x03){
        Keyboard.press('5');
        //Serial.println("5");
        delay(delayTime);
        Keyboard.releaseAll();
      }
      //KEY 9  -- APP PopUp menu
      if(firstBit == 0x04){
        //Keyboard.press('9');
        Serial.print("keypad:menu");
        Serial.println(endString);
        //delay(delayTime);
        //Keyboard.releaseAll();
      }
      //KEY 8
      if(firstBit == 0x05){
        Keyboard.press('8');
        //Serial.println("8");
        delay(delayTime);
        Keyboard.releaseAll();
      }
      //KEY 7
      if(firstBit == 0x06){
        Keyboard.press('7');
        //Serial.println("7");
        delay(delayTime);
        Keyboard.releaseAll();
      }
      //KEY 6
      if(firstBit == 0x07){
        Keyboard.press('6');
        //Serial.println("6");
        delay(delayTime);
        Keyboard.releaseAll();
      }
      //KEY 4
      if(firstBit == 0x08){
        Keyboard.press('4');
        //Serial.println("4");
        delay(delayTime);
        Keyboard.releaseAll();
      }  
}

void keyboardJoystick(char firstBit, char secondBit){
      //KEY 10
      if(firstBit == 0x00 && secondBit == 0x06){
       // Keyboard.press(KEY_UP_ARROW);
       // Serial.println("KEY_UP_ARROW");
       //delay(delayTime);
       //Keyboard.releaseAll();
       Serial.print("keypad:right");
       Serial.println(endString);
      }
      //KEY 11
      if(firstBit == 0x10 && secondBit == 0x05){
       // Keyboard.press(KEY_UP_ARROW);
       // Serial.println("KEY_UP_ARROW");
       // delay(delayTime);
       // Keyboard.releaseAll();
       Serial.print("keypad:left");
       Serial.println(endString);
      }
      //KEY 12
      if(firstBit == 0x10 && secondBit == 0x00){
        Keyboard.press(KEY_UP_ARROW);
        //Serial.println("KEY_UP_ARROW");
        delay(delayTime);
        Keyboard.releaseAll();
      }
      //KEY 13
      if(firstBit == 0x20 && secondBit == 0x00){
        Keyboard.press(KEY_DOWN_ARROW);
        //Serial.println("KEY_DOWN_ARROW");
        delay(delayTime);
        Keyboard.releaseAll();
      }
      //KEY 14
      if(firstBit == 0x30 && secondBit == 0x00){
        Keyboard.press(KEY_LEFT_ARROW);
        //Serial.println("KEY_LEFT_ARROW");
        delay(delayTime);
        Keyboard.releaseAll();
      }
      //KEY 15
      if(firstBit == 0x40 && secondBit == 0x00){
        Keyboard.press(KEY_RIGHT_ARROW);
        //Serial.println("KEY_RIGHT_ARROW");
        delay(delayTime);
        Keyboard.releaseAll();
      }
      //KEY 16
      if(firstBit == 0x01 && secondBit == 0x00){
        Keyboard.press(0xE0);
        //Serial.println("ENTER_KEY");
        delay(delayTime);
        Keyboard.releaseAll();
        Serial.print("keypad:enter");
        Serial.println(endString);
      }
}



void loop() {
  // --- Check for message request from Android device on app startup to send the last messages memorized
  if (Serial.available() > 0){
    while (Serial.available() > 0){
      char s = Serial.read();
      receivedMessage.concat(s);
      if (receivedMessage == "reqMsg"){
        // ---- Here we send all the last messages memorized.
        sendToSerial("requestMessage");
        receivedMessage = "";
      }
    }
    receivedMessage = "";
  }
  // --- Search for 4x1 keypad button press
  if (!digitalRead(backButton)){
        Keyboard.press(KEY_ESC);
        Serial.println("KEY_ESC");
        delay(delayTime);
        Keyboard.releaseAll();
  }
  if (!digitalRead(homeButton)){
        Keyboard.press('h');
        Serial.println("h");
        delay(delayTime);
        Keyboard.releaseAll();
  }
  if (!digitalRead(appSwitchButton)){
        Keyboard.press('s');
        Serial.println("s");
        delay(delayTime);
        Keyboard.releaseAll();
  }
  // --- Scan for ISO-TP format message package and send it to be saved in an array
  // !!! TO DO - Remove the debugging lines !!!
  if (can.receive (canMsg)){
    if (canMsg.id == 289){
      int packageLength = 0;
      Serial.println("RETURNED TO LOOP");
      if (canMsg.data[0] == 0x10){
        packageLength = canMsg.data[1];
        Serial.print("package length is: ");
        Serial.println(packageLength);
      
        if (bytesToArray(packageLength - 1) == false){
          Serial.print("Info:Error: data package not recognized:");
          Serial.println(endString);
        }else {
          Serial.print("Info:Succes: data package recognized:");
          Serial.println(endString);
          Serial.println("######################################################");
          Serial.println();
          Serial.println();
          Serial.println();
        }
      }
    }else if (canMsg.id == 1597){
      keyboardButtons(canMsg.data[0]);
    }else if (canMsg.id == 1598){
      keyboardJoystick(canMsg.data[0], canMsg.data[1]);      
    }
  }
}
