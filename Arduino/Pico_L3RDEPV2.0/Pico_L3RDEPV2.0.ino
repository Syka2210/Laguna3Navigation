//_______________________________________________________________________________________
//  TO DO's : - Implement a timer for exiting the loops when there are no 0x81 bit's at 
//              the end of a pack of frames that forms a text message.
//            - 
//_______________________________________________________________________________________

#include <ACAN2515.h>
#include <SPI.h>
#include <Keyboard.h>

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
//  Pins for the 4x1 keypad that will be used for controlling the navigation bar of the 
//  Android device, due to the fact that it will not be accesible (frame)
//——————————————————————————————————————————————————————————————————————————————



const int backButton = 7;      // 4x1 keyboard -- button assigned for back 
int homeButton = 8;      // 4x1 keyboard -- button assigned for home
int appSwitchButton = 9; // 4x1 keyboard -- button assigned for app switch button

//——————————————————————————————————————————————————————————————————————————————
//  MCP2515 Driver object
//——————————————————————————————————————————————————————————————————————————————

ACAN2515 can (MCP2515_CS, SPI, MCP2515_INT) ;

CANMessage canMsg;

//——————————————————————————————————————————————————————————————————————————————
//  MCP2515 Quartz: adapt to your design
//——————————————————————————————————————————————————————————————————————————————

static const uint32_t QUARTZ_FREQUENCY = 8UL * 1000UL * 1000UL ; // 8 MHz

int high_box = 0;

String frequency_box;
String function_name;
String endString = ":end_string";

String incomingMessage;
long int incomingMessageTime;
String lastSource;
String lastMessage;
String lastVolume;

//——————————————————————————————————————————————————————————————————————————————
//  Enable or disable the Log function for debugging. All the frames that is important
//  for us are sent to the Android device and saved in a .txt file along with the methods 
//  used by the app, that can be read afterwards and search for bugs.
//  This functions needs a more powerfull microcontroller than the ATmega32U4, hence
//  the use of the PI Pico Board
//——————————————————————————————————————————————————————————————————————————————

bool logFileActive = true;

unsigned long delayTime = 250;

void setup() {
  //--- Start serial at highes baud rate for Android comunication
  Serial.begin (250000) ;  
  //--- There are no default SPI pins so they must be explicitly assigned
  SPI.setSCK(MCP2515_SCK);
  SPI.setTX(MCP2515_MOSI);
  SPI.setRX(MCP2515_MISO);
  SPI.setCS(MCP2515_CS);
  //--- Begin SPI
  SPI.begin () ;  
  //--- Configure ACAN2515
  Serial.println ("Configure ACAN2515") ;
  ACAN2515Settings settings (QUARTZ_FREQUENCY, 500UL * 1000UL) ; // CAN bit rate 500 kb/s - autovehicle frequency
  settings.mRequestedMode = ACAN2515Settings::NormalMode ; // Select normal mode
  const uint16_t errorCode = can.begin (settings, [] { can.isr () ; }) ;
  /*
  if (errorCode == 0) {
    Serial.print ("Bit Rate prescaler: ") ;
    Serial.println (settings.mBitRatePrescaler) ;
    Serial.print ("Propagation Segment: ") ;
    Serial.println (settings.mPropagationSegment) ;
    Serial.print ("Phase segment 1: ") ;
    Serial.println (settings.mPhaseSegment1) ;
    Serial.print ("Phase segment 2: ") ;
    Serial.println (settings.mPhaseSegment2) ;
    Serial.print ("SJW: ") ;
    Serial.println (settings.mSJW) ;
    Serial.print ("Triple Sampling: ") ;
    Serial.println (settings.mTripleSampling ? "yes" : "no") ;
    Serial.print ("Actual bit rate: ") ;
    Serial.print (settings.actualBitRate ()) ;
    Serial.println (" bit/s") ;
    Serial.print ("Exact bit rate ? ") ;
    Serial.println (settings.exactBitRate () ? "yes" : "no") ;
    Serial.print ("Sample point: ") ;
    Serial.print (settings.samplePointFromBitStart ()) ;
    Serial.println ("%") ;
  } else {
    Serial.print ("Configuration error 0x") ;
    Serial.println (errorCode, HEX) ;
  }
  */
  //Turn the board LED on to check for functionality
  pinMode(LED_BUILTIN, OUTPUT);
  digitalWrite(LED_BUILTIN, HIGH);

  pinMode(backButton, INPUT_PULLUP);
  pinMode(homeButton, INPUT_PULLUP);
  pinMode(appSwitchButton, INPUT_PULLUP);
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
//  The log function
//——————————————————————————————————————————————————————————————————————————————
void logFunction(){
  if (canMsg.id == 289){
     Serial.print("LOG-FILE:FRAME ID=");
      Serial.print(canMsg.id);
      for (int i=0; i<=7; i++){
        Serial.print(":");
        if (canMsg.data[i] < 16) Serial.print('0');
        Serial.print(canMsg.data[i], HEX);
      }
      Serial.println(endString);
  }
}

//——————————————————————————————————————————————————————————————————————————————
//  Returns the main volume in text format (or Pause text) 
//——————————————————————————————————————————————————————————————————————————————
void volume_text(bool counter, bool unpause){
  bool endFunction = false;
  String volume = String();
  char character;
  if (unpause == true){
    Serial.print("device_volume:");
    Serial.print("return");
    Serial.println(endString);
    return;
  }
  while (counter == false && unpause == false){
    if (can.receive (canMsg)){
      //--- Send frame to ANDROID for debugging LOG
      if (logFileActive == true) logFunction();

      if (endFunction == true){
        //--- Serial transmit the volume
        Serial.print("device_volume:");
        Serial.print(volume);
        Serial.println(endString);
        lastVolume = String("device_volume:") + volume + endString;
        return;        
      }

      if (canMsg.id == 289){
        for (int i=1; i<=7; i++){
          if (canMsg.data[i] == 0x81) endFunction = true;
          //--- Saving the string
          if (endFunction == false){
            if (canMsg.data[i] >= 0x20 && canMsg.data[i] <= 0x7E){
            character = canMsg.data[i];
            volume = volume + character;
            }
          }
        }
      }
    }
  }
}

//——————————————————————————————————————————————————————————————————————————————
//  Returns the source text by the second byt of the frame
//——————————————————————————————————————————————————————————————————————————————
void sources(){
  if (canMsg.data[1] == 0x1F) Serial.print("Source:Radio"); 
  else if (canMsg.data[1] == 0x27) Serial.print("Source:cd_player");
  else if (canMsg.data[1] == 0x43) Serial.print("Source:Auxiliary audio sources");
  
  Serial.println(endString);
} 

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

String frequency_type_box(bool counter){
  String title;
  char character;
  bool counter_0x00 = false;
  while (counter == false){
    if (can.receive (canMsg)){
      //Send frame to ANDROID for debugging LOG
      if (logFileActive == true) logFunction(); 
      if (canMsg.data[0] >= 0x23){
        for (int i=1; i<=7; i++){
          if (canMsg.data[i] == 0x00 && i != 0){
            if (counter_0x00 == true){
              return title;
            } else counter_0x00 = true;
          } else counter_0x00 = false;
          
          if (canMsg.data[i] >= 0x20 && canMsg.data[i] <= 0x7E){
            character = canMsg.data[i];
            title.concat(character);
          }
        }        
      }
    }
  }
  return "FUCK";
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

//Highlighted box
//TODO: Refactor?
//Counter never changes, infinite loop?
//What does this return if it skips the while?
int high_box_function(bool counter){
  int cbh = 0;
  while (counter == false){ 
    if (can.receive (canMsg)){
      //Send frame to ANDROID for debugging LOG
      if (logFileActive == true) logFunction();

      if (canMsg.id == 289 && canMsg.data[0] == 0x21){
        if (canMsg.data[6] == 0x20 && high_box != 1){
          Serial.print("HighlightedBox:1");
          Serial.println(endString);
          return 1;
        } 
        else if (canMsg.data[6] == 0x21 && high_box != 2){
          Serial.print("HighlightedBox:2");
          Serial.println(endString);
          return 2;
        } 
        else if (canMsg.data[6] == 0x22 && high_box != 3){
          Serial.print("HighlightedBox:3");
          Serial.println(endString);
          return 3;
        } 
        else if (canMsg.data[6] == 0x23 && high_box != 4){
          Serial.print("HighlightedBox:4");
          Serial.println(endString);
          return 4;
        }
        else break; 
      }
    }
  }
  return 0;
}

//——————————————————————————————————————————————————————————————————————————————
//  This function reads the value of a volume level in the settings menu
//——————————————————————————————————————————————————————————————————————————————
//Menu settings volume function
//Counter never changes, infinite loop?
void menu_volume(bool counter, String function){
  String menu_volume;
  bool endFunction = false;
  while (counter == false){
    if  (can.receive (canMsg)){
      //Send frame to ANDROID for debugging LOG
      if (logFileActive == true) logFunction();

      if (endFunction == true){
        //Serial transmit the titles
        /*
        menu_volume = String("menu_volume:") + function + String(":") + value + endString;
        lastMessage = menu_volume;
        Serial.println(menu_volume);
        */
        ///////////////////////////
        ///*
        Serial.print("menu_volume:");
        Serial.print(function);
        Serial.print(":");
        Serial.print(menu_volume);
        Serial.println(endString);
        //*/
        // end function
        return;
      }
      if (canMsg.id == 289 && canMsg.data[0] == 0x22){
        menu_volume = canMsg.data[6];
      }
      if (canMsg.data[7] == 0x81){
        endFunction = true;
      }
    }
  }
}

//——————————————————————————————————————————————————————————————————————————————
//  This function displays either a list of audio styles settings, or two volume levels
//  that can be customized in the settings menu
//——————————————————————————————————————————————————————————————————————————————
void musical_atmosphere(bool counter, char signs){
  int arraySize = 11;
  String musical_atmosphere = String("");
  String array3x2[arraySize];
  bool start_titles = false;
  bool curselect = false;
  bool counter_set = false;
  bool endFunction = false;
  int rowNr = 1;
  int columnNr = 1;
  int arrayPos = 0;
  int curent_selected = 0;
  bool counter_0x00 = false;
  bool iconBit = false;
  char character;
  while (counter == false){
    if (can.receive (canMsg)){
      //Send frame to ANDROID for debugging LOG
      if (logFileActive == true) logFunction();

      if (endFunction == true){
        //Serial transmit the titles
        // String ex: musical_atmosphere : 2 : off : on :   : title_1 : title_2 :   : + : 5 : - : 2 : end_string
        // Highlighted portion 21/1=text box ; 22/2=bass ; 23/3=treble
        // function name
        /*
        //musical_atmosphere = String("musical_atmosphere:") + curent_selected;
        musical_atmosphere = String("musical_atmosphere:");
        Serial.print("DEBUG PRINT =>");
        for (int i = 1; i <= 10; i++){
          //musical_atmosphere = String(musical_atmosphere) + String(":") + array3x2[i];
          musical_atmosphere += ":";
          musical_atmosphere += array3x2[i];
          Serial.print(array3x2[i]);
          Serial.print(":");
        }
        Serial.println();
        musical_atmosphere = musical_atmosphere + endString;
        lastMessage = musical_atmosphere;
        Serial.println(musical_atmosphere);
        */
        ///////////////////////////////////////////
        ///*
        Serial.print("musical_atmosphere:");
        //Highlighted portion 21/1=text box ; 22/2=bass ; 23/3=treble
        Serial.print(curent_selected);
        for (int i = 1; i <= 10; i++){
          Serial.print(":");
          Serial.print(array3x2[i]);
        }
        Serial.println(endString);
        //*/
        return;
      }

      if (canMsg.id == 289 && canMsg.data[0] == 0x22 && curselect == false){
        if (canMsg.data[4] == 0x21) curent_selected = 2;
        if (canMsg.data[4] == 0x22) curent_selected = 3;
        if (canMsg.data[4] == 0x23) curent_selected = 4;
        curselect = true;
      }

      if (canMsg.id == 289 && canMsg.data[0] >=0x23){ 
        start_titles = true;
      }

      if (canMsg.id == 289 && start_titles == true){

        ////////// Counter for separating the titles //////////

        for (int i=1; i<=7; i++){          
          if (canMsg.data[i] == 0x0D){
            rowNr++;
            columnNr = 1;
          }

          //Finish with the grid, check treble and bass values
          if (canMsg.data[i] == 0x00 && i != 0 && counter_set == false){
            if (counter_0x00 == true){
              arrayPos = 7;
              //Serial.println(arrayPos);
              counter_0x00 = false;
              counter_set = true;
              continue;
            } else counter_0x00 = true;
          } else counter_0x00 = false;
          //Finish with the grid, check treble and bass values

          //Checking where on the 3x2 array whe are to save the string
          if (arrayPos < 7) 
          {
            arrayPos = get_array_position(columnNr, rowNr);
          }
          //Checking where on the 3x2 array whe are to save the string

          if (canMsg.data[i] == 0xF1 && i < 7) {
            array3x2[arrayPos] = icons(canMsg.data[i+1]);
            columnNr++;
            continue;
          }
          if (canMsg.data[i] == 0XF1 && i == 7){
            iconBit = true;
            continue;
          }
          if (iconBit == true && i == 1 && i < 7){
            array3x2[arrayPos] = icons(canMsg.data[i]);
            iconBit = false;
            columnNr++;
            continue;
          }

          if (columnNr == 2 && arrayPos < 7 && canMsg.data[i] != 0x26 && canMsg.data[i] != 0x27){
            if (canMsg.data[i] >= 0x20 && canMsg.data[i] <= 0x7E){
              character = canMsg.data[i];
              array3x2[arrayPos].concat(character);
            }
          }

          if (signs == 0x5A && canMsg.data[0] == 0x2B){
            if (canMsg.data[5] <= 0x09) array3x2[7] = "+";
            if (canMsg.data[5] >= 0xF7) array3x2[7] = "-";
            //-------------------------------------------
            if (canMsg.data[6] <= 0x09) array3x2[9] = "+";
            if (canMsg.data[6] >= 0xF7) array3x2[9] = "-";

            if (canMsg.data[5] <= 0x09) array3x2[8] = canMsg.data[5];
            if (canMsg.data[5] >= 0xF7) array3x2[8] = 256 - canMsg.data[5];
            //------------------------------------------------------------
            if (canMsg.data[6] <= 0x09) array3x2[10] = canMsg.data[6];
            if (canMsg.data[6] >= 0xF7) array3x2[10] = 256 - canMsg.data[6];            
          }

          if (canMsg.data[0] == 0x2C || canMsg.data[7] == 0x81){
            endFunction = true;
          }    
        } 
      }     
    }
  }
}

//——————————————————————————————————————————————————————————————————————————————
//  This function displays the radio grid that has a 3x3 matrix box with stations name, 
//  frequency and number. Also it adds a box in the front that shows us what type of frequency
//  or what program list are we using.
//  we might have a 3x3 matrix data sent, containg the curent, previous and next station
//  or a 3x1 line with only the curent station selected
//——————————————————————————————————————————————————————————————————————————————
//TODO: Tidy up the Serial.prints
//TODO: Refactor
void gridSortRadio(bool counter) {
  //Serial.println("Entered gridSortRadio");
  int arraySize = 10;
  String array3x3[arraySize];
  String gridSortRadio = String();
  bool start_titles = false;
  bool counter_0x00 = false;
  bool highPass = false;
  int rowNr = 1;
  int columnNr = 1;
  int arrayPos = 0;
  int arrayElements = 0;
  char character;
  while (counter == false) {
    if (can.receive (canMsg)) {
      //Send frame to ANDROID for debugging LOG
      if (logFileActive == true) logFunction();

      if (columnNr == 4) {
        //Check how many strings are in the 3x3 array
        for (int i = 0; i < arraySize; i++) {
          if (array3x3[i].length() > 0) arrayElements++;
        }
        //Check how many strings are in the 3x3 array

        //Serial transmit the titles depending of number
        if (arrayElements == 3) {
          //Insert below the 3 items identifier
          /*
          gridSortRadio = String("radio_grid1x3:") + high_box + String(":") + frequency_box + String(":") + array3x3[1] + String(":") + array3x3[4] + String(":") + array3x3[7] + endString;
          lastMessage = gridSortRadio;
          Serial.println(gridSortRadio);
          */
          //////////////////////////////////
          ///*
          Serial.print("radio_grid1x3:");
          Serial.print(high_box);
          Serial.print(":");
          Serial.print(frequency_box);
          Serial.print(":");          
          Serial.print(array3x3[1]);
          Serial.print(":");
          Serial.print(array3x3[4]);
          Serial.print(":");
          Serial.print(array3x3[7]);
          Serial.println(endString);
          //*/
          return;
        }
        if (arrayElements >= 3) {
          /*
          gridSortRadio = String("radio_grid3x3:") + high_box + String(":") + frequency_box;
          for (int i=1; i<=9; i++){
            gridSortRadio = gridSortRadio + char('z') + array3x3[i];
          }
          gridSortRadio = gridSortRadio + endString;
          lastMessage = gridSortRadio;
          Serial.println(gridSortRadio);
          */
          //////////////////////////////////////
          ///*
          Serial.print("radio_grid3x3:");
          Serial.print(high_box);
          Serial.print(":");
          Serial.print(frequency_box);
          Serial.print(":");
          Serial.print(array3x3[1]);
          Serial.print(":");
          Serial.print(array3x3[2]);
          Serial.print(":");
          Serial.print(array3x3[3]);
          Serial.print(":");
          Serial.print(array3x3[4]);
          Serial.print(":");
          Serial.print(array3x3[5]);
          Serial.print(":");
          Serial.print(array3x3[6]);
          Serial.print(":");
          Serial.print(array3x3[7]);
          Serial.print(":");
          Serial.print(array3x3[8]);
          Serial.print(":");
          Serial.print(array3x3[9]);
          Serial.println(endString);
          //*/
          return;
        }
      }

      if (canMsg.data[0] == 0x22 && highPass == false) {

        //It might be the bit 4, it might be the bit ??6??
        if (canMsg.data[4] == 0x20) high_box = 1;
        if (canMsg.data[4] == 0x21) high_box = 2;
        if (canMsg.data[4] == 0x22) high_box = 3;
        if (canMsg.data[4] == 0x23) high_box = 4;
        highPass = true;
      }

      if (canMsg.id == 289 && canMsg.data[0] >= 0x23) {
        start_titles = true;
      }

      if (canMsg.id == 289 && start_titles == true) {
        for (int i = 1; i <= 7; i++) {
          if (canMsg.data[i] == 0x0D) {
            if (rowNr == 3) {
              //rowNr = 1;
            } else rowNr++;
          }
          if (canMsg.data[i] == 0x00 && i != 0) {
            if (counter_0x00 == true) {
              columnNr++;
              rowNr = 1;
              counter_0x00 = false;
            } else counter_0x00 = true;
          } else counter_0x00 = false;

          //Checking where on the 3x3 array whe are to save the string
          arrayPos = get_array_position(columnNr, rowNr);
          //Checking where on the 3x3 array whe are to save the string

          //Saving the string on the coresponding 3x3 array position
          if (columnNr <= 2 && canMsg.data[i] >= 0x20 && canMsg.data[i] <= 0x7E) {
            character = canMsg.data[i];
            array3x3[arrayPos].concat(character);
          }
          //Saving the string on the coresponding 3x3 array position
          
          //Saving the channel number using the chan_number function
          if (columnNr == 3) {
            if (canMsg.data[i] >= 0x10 && canMsg.data[i] <= 0x15) {
              character = canMsg.data[i];
              array3x3[arrayPos] = chan_number(character);
            }
            if (canMsg.data[i] >= 0x46 && canMsg.data[i] <= 0x4B) {
              character = canMsg.data[i];
              array3x3[arrayPos] = chan_number(character);
            }
            if (canMsg.data[i] == 0x2D){
              array3x3[arrayPos] = '-';
            }
          }
          //Saving the channel number using the chan_number function
        }
      }
    }
  }
}

//——————————————————————————————————————————————————————————————————————————————
//  This function displays a grid that has a 3x2 matrix box with types of radio programs, 
//  (news, pop, rock)
//  we might have a 3x2 matrix data sent, containg the curent, previous and next program
//  or a 1x2 line with only the curent station selected
//——————————————————————————————————————————————————————————————————————————————
void gridPTY3x2(bool counter, char type){
  int arraySize = 7;
  String array3x2[arraySize];
  String gridPTY3x2 = String();
  bool start_titles = false;
  bool counter_0x00 = false;
  bool icon = false;
  int rowNr = 1;
  int columnNr = 2;
  int arrayPos = 0;
  int arrayElements = 0;
  char character;
  while (counter == false){
    if (can.receive (canMsg)){
      //Send frame to ANDROID for debugging LOG
      if (logFileActive == true) logFunction();

      if (columnNr == 3){
        //Check how many strings are in the 3x3 array
        for (int i = 0; i < arraySize; i++){
          if (array3x2[i].length() > 0) arrayElements++;
        }
        //Check how many strings are in the 3x3 array
        
        //Serial transmit the titles depending of number
        if (arrayElements == 1 && type == 0x41){
          //Insert below the 3 items identifier
          gridPTY3x2 = String("complex_grid1x2:") + array3x2[1] + String(":") + array3x2[4] + endString;
          lastMessage = gridPTY3x2;
          Serial.println(gridPTY3x2);
          /*
          Serial.print("complex_grid1x2:");
          Serial.print(array3x2[1]);
          Serial.print(":");
          Serial.print(array3x2[4]);
          Serial.println(endString);
          */
          return;
        }
        if (arrayElements >= 1 && type == 0x43){
          //Insert below the 3 items identifier
          /*
          gridPTY3x2 = String("complex_grid3x2");
          for (int i=1; i<=6; i++){
            gridPTY3x2 = gridPTY3x2 + String(":") + array3x2[i];
          }
          gridPTY3x2 = gridPTY3x2 + endString;
          lastMessage = gridPTY3x2;
          Serial.println(gridPTY3x2);
          */
          ///////////////////////////////////////////
          ///*
          Serial.print("complex_grid3x2:");
          Serial.print(array3x2[1]);
          Serial.print(":");
          Serial.print(array3x2[2]);
          Serial.print(":");
          Serial.print(array3x2[3]);
          Serial.print(":");
          Serial.print(array3x2[4]);
          Serial.print(":");
          Serial.print(array3x2[5]);
          Serial.print(":");
          Serial.print(array3x2[6]);
          Serial.println(endString);
          //*/
          return;
        }
      }

      if (canMsg.id == 289 && (canMsg.data[7] == 0xF0 || canMsg.data[7] == 0xF1 || canMsg.data[7] == 0xF2)){
        icon = true;
      }

      if (canMsg.id == 289 && canMsg.data[0] == 0x23){ 
        start_titles = true;
      }

      if (canMsg.id == 289 && start_titles == true){
        //Serial.println(arrayElements);
        for (int i=1; i<=7; i++){
          if (canMsg.data[i] == 0x0D){
            rowNr++;
          }

          //Checking where on the 3x2 array whe are to save the string
          if (rowNr == 1 && columnNr == 1) arrayPos = 1;
          if (rowNr == 2 && columnNr == 1) arrayPos = 2;
          if (rowNr == 3 && columnNr == 1) arrayPos = 3;
          if (rowNr == 1 && columnNr == 2) arrayPos = 4;
          if (rowNr == 2 && columnNr == 2) arrayPos = 5;
          if (rowNr == 3 && columnNr == 2) arrayPos = 6;
          //Checking where on the 3x2 array whe are to save the string

          //Saving the string on the coresponding 3x3 array position
            if (columnNr == 2){
              if (canMsg.data[i] >= 0x20 && canMsg.data[i] <= 0x7E){
              character = canMsg.data[i];
              array3x2[arrayPos].concat(character);
              }
            }
          //Saving the string on the coresponding 3x2 array position
        }
      }
    }
  }
}

//——————————————————————————————————————————————————————————————————————————————
//  This function displays a grid that has a 3x2 matrix box used for menu settings 
//——————————————————————————————————————————————————————————————————————————————
//TODO: Tidy up the Serial.prints
//TODO: Refactor
void gridSort3x2(bool counter){
  int arraySize = 7;
  String array3x2[arraySize];
  bool start_titles = false;
  bool counter_0x00 = false;
  bool icon = false;
  int rowNr = 1;
  int columnNr = 1;
  int arrayPos = 0;
  int arrayElements = 0;
  char character;
  while (counter == false){
    if (can.receive (canMsg)){
      //Send frame to ANDROID for debugging LOG
      if (logFileActive == true) logFunction();

      if (columnNr == 3){
        //Check how many strings are in the 3x3 array
        for (int i = 0; i < arraySize; i++){
          if (array3x2[i].length() > 0) arrayElements++;
        }
        //Check how many strings are in the 3x3 array

        //Save the curent selection in the function_name variable
        function_name = array3x2[5];
        //Save the curent selection in the function_name variable
        
        //Serial transmit the titles depending of number
        if (arrayElements <= 2){
          //Insert below the 3 items identifier
          Serial.print("complex_grid1x2:");
          Serial.print(array3x2[1]);
          Serial.print(":");
          Serial.print(array3x2[4]);
          Serial.println(endString);
          return;
        }
        if (arrayElements >= 3){
          //Insert below the 3 items identifier
          Serial.print("complex_grid3x2:");
          Serial.print(array3x2[1]);
          Serial.print(":");
          Serial.print(array3x2[2]);
          Serial.print(":");
          Serial.print(array3x2[3]);
          Serial.print(":");
          Serial.print(array3x2[4]);
          Serial.print(":");
          Serial.print(array3x2[5]);
          Serial.print(":");
          Serial.print(array3x2[6]);
          Serial.println(endString);
          return;
        }
      }

      if (canMsg.id == 289 && (canMsg.data[7] == 0xF0 || canMsg.data[7] == 0xF1 || canMsg.data[7] == 0xF2)){
        icon = true;
      }

      if (canMsg.id == 289 && canMsg.data[0] == 0x23){ 
        start_titles = true;
      }

      if (canMsg.id == 289 && start_titles == true){
        //Serial.println(arrayElements);
        for (int i=1; i<=7; i++){
          if (canMsg.data[i] == 0x0D &&(canMsg.data[i+1] == 0xF1 || canMsg.data[i+1] == 0xF2 || canMsg.data[i+1] == 0x00 || canMsg.data[i-1] == 0xF0)){
            if (rowNr == 3){
              //rowNr = 1;
            }else rowNr++;
          }
          if (canMsg.data[i] == 0x00 && i != 0){
            if (counter_0x00 == true){
              columnNr++;
              rowNr = 1;
              counter_0x00 = false;
            } else counter_0x00 = true;
          } else counter_0x00 = false;

          //Checking where on the 3x2 array whe are to save the string
          arrayPos = get_array_position(columnNr, rowNr);
          //Checking where on the 3x2 array whe are to save the string

          //Saving the string on the coresponding 3x3 array position
          if (columnNr == 1){
            if (icon == true && canMsg.data[i] <= 0x7F && canMsg.data[i] != 0x0D && canMsg.data[i] != 0x00){
              //Save the icon name in the first array column
              array3x2[arrayPos] = icons(canMsg.data[i]);
            }
            if (icon == true && canMsg.data[i] == 0x0D && (canMsg.data[i+1] == 0xF0 || canMsg.data[i+1] == 0x00 || canMsg.data[i-1] == 0xF1 || canMsg.data[i-1] == 0xF2)){
              //Save the icon name in the first array column
              array3x2[arrayPos] = icons(canMsg.data[i]);
            }
            if (icon == false){
              if (canMsg.data[i] >= 0x20 && canMsg.data[i] <= 0x7E){
              character = canMsg.data[i];
              array3x2[arrayPos].concat(character);
              }
            }
          }
            if (columnNr == 2){
              if (canMsg.data[i] >= 0x20 && canMsg.data[i] <= 0x7E){
              character = canMsg.data[i];
              array3x2[arrayPos].concat(character);
              }
            }
          //Saving the string on the coresponding 3x2 array position
        }
      }
    }
  }
}

//——————————————————————————————————————————————————————————————————————————————
//  This function displays a grid that has a 3x3 matrix box used for the PTY radio menu
//  where you can change the frequency of the AM band. 
//——————————————————————————————————————————————————————————————————————————————
//TODO: Tidy up the Serial.prints
//TODO: Refactor
void gridSort3x3(bool counter){  
  int arraySize = 7;
  String array3x3[arraySize];
  String gridSort3x3 = String();
  int arrayElements = 0;
  bool start_titles = false;
  bool counter_0x00 = false;
  bool highPass = false;
  int rowNr = 1;
  int columnNr = 1;
  int arrayPos = 0;
  char character;
  while (counter == false){
    if (can.receive (canMsg)){
      //Send frame to ANDROID for debugging LOG
      if (logFileActive == true) logFunction();

      if (columnNr == 3){
        //Check how many strings are in the 3x3 array
        for (int i = 0; i < arraySize; i++){
          if (array3x3[i].length() > 0) arrayElements++;
        }
        //Check how many strings are in the 3x3 array

        //Serial transmit the titles depending of number
        if (arrayElements <= 2){
          //Insert below the 3 items identifier
          /*
          gridSort3x3 = String("freq_grid1x3:") + high_box + String(":") + frequency_box + String(":") + array3x3[1] + String(":") + array3x3[4] + endString;
          lastMessage = gridSort3x3;
          Serial.println(gridSort3x3);
          */
          /////////////////////
          ///*
          Serial.print("freq_grid1x3:");
          Serial.print(high_box);
          Serial.print(":");
          Serial.print(frequency_box);
          Serial.print(":");
          Serial.print(array3x3[1]);
          Serial.print(":");
          Serial.print(array3x3[4]);
          Serial.println(endString);
          //*/
          return;
        }
        if (arrayElements >= 3){
          /*
          //Insert below the 3 items identifier
          gridSort3x3 = String("freq_grid3x3:") + high_box + String(":") + frequency_box;
          for (int i=1; i<=6; i++){
            gridSort3x3 = gridSort3x3 + String(":") + array3x3[i];
          }
          gridSort3x3 = gridSort3x3 + endString;
          lastMessage = gridSort3x3;
          Serial.println(gridSort3x3);
          */
          //////////////////////////////////
          ///*
          Serial.print("freq_grid3x3:");
          Serial.print(high_box);
          Serial.print(":");
          Serial.print(frequency_box);
          Serial.print(":");
          Serial.print(array3x3[1]);
          Serial.print(":");
          Serial.print(array3x3[2]);
          Serial.print(":");
          Serial.print(array3x3[3]);
          Serial.print(":");
          Serial.print(array3x3[4]);
          Serial.print(":");
          Serial.print(array3x3[5]);
          Serial.print(":");
          Serial.print(array3x3[6]);
          Serial.println(endString);
          //*/
          return;
        }
      }

      if (canMsg.data[0] == 0x22 && highPass == false) {

        //It might be the bit 4, it might be the bit ??6??
        if (canMsg.data[4] == 0x20) high_box = 1;
        if (canMsg.data[4] == 0x21) high_box = 2;
        if (canMsg.data[4] == 0x22) high_box = 3;
        if (canMsg.data[4] == 0x23) high_box = 4;
        highPass = true;
      }

    if (canMsg.id == 289 && canMsg.data[0] ==0x23){ 
      start_titles = true;
    }

    if (canMsg.id == 289 && start_titles == true){
      //Serial.println(arrayElements);
      for (int i=1; i<=7; i++){
          if (canMsg.data[i] == 0x0D){
            if (rowNr == 3){
              //rowNr = 1;
            }else rowNr++;
          }
          if (canMsg.data[i] == 0x00 && i != 0){
            if (counter_0x00 == true){
              columnNr++;
              rowNr = 1;
              counter_0x00 = false;
            } else counter_0x00 = true;
          } else counter_0x00 = false;

          //Checking where on the 3x2 array whe are to save the string
          arrayPos = get_array_position(columnNr, rowNr);
          //Checking where on the 3x2 array whe are to save the string

          //Saving the string on the coresponding 3x3 array position
          if (columnNr == 1){
              if (canMsg.data[i] >= 0x20 && canMsg.data[i] <= 0x7E){
              character = canMsg.data[i];
              array3x3[arrayPos].concat(character);
              }
          }
          if (columnNr == 2){
            if (canMsg.data[i] >= 0x10 && canMsg.data[i] <= 0x15) {
              character = canMsg.data[i];
              array3x3[arrayPos] = chan_number(character);
            }
            if (canMsg.data[i] == 0x2D){
              array3x3[arrayPos] = '-';
            }
          }
        }
          //Saving the string on the coresponding 3x2 array position
      }
    }
  }
}

//——————————————————————————————————————————————————————————————————————————————
//  Displays a information box with a 3 vertical boxes on the left for confirming, canceling or
//  acknowledging the information received.
//——————————————————————————————————————————————————————————————————————————————
//TODO: Tidy up the Serial.prints
//TODO: Refactor
void confirm_cancel_function(bool counter){
  int arraySize = 7;
  String array3x2[arraySize];
  String confirm_cancel = String();
  bool start_titles = false;
  bool counter_0x00 = false;
  int rowNr = 1;
  int columnNr = 1;
  int arrayPos = 0;
  int arrayElements = 0;
  char character;
  while (counter == false){
    if (can.receive (canMsg)){
      //Send frame to ANDROID for debugging LOG
      if (logFileActive == true) logFunction();

      if (columnNr == 3){
        //Check how many strings are in the array
        for (int i = 0; i < arraySize; i++){
          if (array3x2[i].length() > 0) arrayElements++;
        }
        //Check how many strings are in the array
        if (arrayElements >= 3){
          //Insert below the 3 items identifier
          /*
          confirm_cancel = "confirm_cancel_function";
          Serial.print(confirm_cancel);   Serial.print("          ");
          for (int i=1; i<=6; i++){
            Serial.print(array3x2[i]);  Serial.print("     ");
            confirm_cancel = confirm_cancel + String(":") + array3x2[i];
          }
          Serial.println();
          confirm_cancel = confirm_cancel + endString;
          lastMessage = confirm_cancel;
          Serial.println(confirm_cancel);
          Serial.println(lastMessage);
          */
          ////////////////////////
          ///*
          Serial.print("confirm_cancel_function:");
          Serial.print(array3x2[1]);
          Serial.print(":");
          Serial.print(array3x2[2]);
          Serial.print(":");
          Serial.print(array3x2[3]);
          Serial.print(":");
          Serial.print(array3x2[4]);
          Serial.print(":");
          Serial.print(array3x2[5]);
          Serial.print(":");
          Serial.print(array3x2[6]);
          Serial.println(endString);
          //*/
          return;
        }
      }

      if (canMsg.id == 289 && canMsg.data[0] ==0x23){ 
        start_titles = true;
      }

      if (canMsg.id == 289 && start_titles == true){
        //Serial.println(arrayElements);
        for (int i=1; i<=7; i++){
            if (canMsg.data[i] == 0x0D){
              if (rowNr == 3){
                //rowNr = 1;
              }else rowNr++;
            }
            if (canMsg.data[i] == 0x00 && i != 0){
              if (counter_0x00 == true){
                columnNr++;
                rowNr = 1;
                counter_0x00 = false;
              } else counter_0x00 = true;
            } else counter_0x00 = false;

          //Checking where on the 3x2 array whe are to save the string
          arrayPos = get_array_position(columnNr, rowNr);
          //Checking where on the 3x2 array whe are to save the string

          //Saving the string on the coresponding 3x3 array position
          if (canMsg.data[i] >= 0x20 && canMsg.data[i] <= 0x7E){
              character = canMsg.data[i];
              array3x2[arrayPos].concat(character);
          }
      }
          //Saving the string on the coresponding 3x2 array position
      }
    }
  }
}

//——————————————————————————————————————————————————————————————————————————————
//  Displays a information box that is displayed a couple of seconds.
//——————————————————————————————————————————————————————————————————————————————

void info_box(bool counter){
  int arraySize = 2;
  String arrayInfoBox[arraySize];
  String info_box = String();
  bool start_titles = false;
  char character;
  bool counter_0x00 = false;
  bool endFunction = false;
  while (counter == false) {
    if (can.receive (canMsg)){
      //Send frame to ANDROID for debugging LOG
      if (logFileActive == true) logFunction();
      
      if (endFunction == true) {
        /*
        info_box = String("info_box:") + arrayInfoBox[1] + endString;
        lastMessage = info_box;
        Serial.println(info_box);
        */
        //////////////////////////////////
        ///*
        Serial.print("info_box:");
        Serial.print(arrayInfoBox[1]);
        Serial.println(endString);
        //*/
        return;
      }

      if (canMsg.id == 289 && canMsg.data[0] == 0x22){
        start_titles = true;
      }

      if (canMsg.id == 289 && start_titles == true){
        for (int i = 1; i <= 7; i++){
          //Check for the end of the message package
          if (canMsg.data[i] == 0x00 && i != 0){
              if (counter_0x00 == true){
                endFunction = true;
                counter_0x00 = false;
              } else counter_0x00 = true;
            } else counter_0x00 = false;

          if (canMsg.data[i] >= 0x20 && canMsg.data[i] <= 0x7E){
            character = canMsg.data[i];
            arrayInfoBox[1].concat(character);
          }
        }
      }      
    }
  }
}

//——————————————————————————————————————————————————————————————————————————————
//  Takes the frames from the navigation control unit (the joystick and the buttons)
//  and converts them in a Keyboard emulated data, in order to navigate through the Android
//  device. 
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
  //  Check for message request from Android device on app startup to send
  //  the last messages
  if (Serial.available()){
    char c = Serial.read();
    incomingMessage.concat(c);
    Serial.println(incomingMessage);
    incomingMessageTime = millis();
    if (incomingMessage == "reqMsg"){
      incomingMessage = "";
      //Serial.println(incomingByte);
      Serial.println(lastSource);
      delay(20);
      Serial.println(lastVolume);
      delay(20);
      Serial.println(lastMessage);
    }
  }

  // BE ADVISED TO DELETE THIS VERIFICATION PART BELLOW   !!!!!!!!!!!!!
  if (millis() - incomingMessageTime > 300 && incomingMessage.length() > 0){
    Serial.println(millis() - incomingMessageTime);
    incomingMessage = "";
  }
  //End of message request

  //--- Search for 4x1 keypad button press
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

  
  if (can.receive (canMsg)){
    if(canMsg.id == 289){
      //Send frame to ANDROID for debugging LOG
      if (logFileActive == true) logFunction();      

      //Volume function
      if (canMsg.data[0] == 0x10 && canMsg.data[2] == 0x35){
        volume_text(false, false);
      }
      else if (canMsg.data[0] == 0x03 && canMsg.data[2] == 0x08){
        volume_text(false, true);
      } 

      //Highlighted box
      else if(canMsg.data[0] == 0x10 && canMsg.data[1] == 0x0E && canMsg.data[2] == 0x20 && canMsg.data[3] == 0x00){
          //In the frame with the first bit=21 the 7'th bit represents the position of the highlighted
          //box. The first box has the value 20, the second 21 and so on.
          //Serial.println("ENTER HIGHLIGHTED BOX");
          high_box = high_box_function(false);
          //Serial.println("EXIT");
          //Serial.println();
      }
                                       
      else if (canMsg.data[0] == 0x10 && canMsg.data[2] == 0x25){
        if ((canMsg.data[3] == 0xC1 || canMsg.data[3] == 0xC3) && canMsg.data[4] == 0x09 ){
          if (canMsg.data[7] == 0x00){
            frequency_box = frequency_type_box(false);
          }
          else gridSort3x2(false); // CHECKED
        }

        else if (canMsg.data[4] == 0x17){
          gridSortRadio(false); // CHECKED
        }

        else if (canMsg.data[4] == 0x13 && canMsg.data[5] == 0x03){
          gridPTY3x2(false, canMsg.data[3]);
        }

        else if (canMsg.data[4] == 0x07 && canMsg.data[6] == 0x20){
          gridSort3x3(false); // CHECKED        
        }

        else if (canMsg.data[3] == 0x70 && canMsg.data[4] == 0x00 && canMsg.data[5] == 0x04 && canMsg.data[6] == 0x40){
          menu_volume(false, function_name);
        }
        
        else if (canMsg.data[3] == 0x73 && canMsg.data[4] == 0x02 && canMsg.data[6] == 0x40){
          musical_atmosphere(false, canMsg.data[1]);
        }
        else if (canMsg.data[3] == 0x63 && canMsg.data[4] == 0x09){
          confirm_cancel_function(false);
        }
        else if (canMsg.data[3] == 0x52 && canMsg.data[4] == 0x09){
          info_box(false);
        }
        else if (canMsg.data[3] == 0x41 && canMsg.data[4] == 0x13 && canMsg.data[5] == 0x01 && canMsg.data[6] == 0x20 && canMsg.data[7] == 0x80){
          sources();
        }
      }                                       
    }

    else if (canMsg.id == 1597){
      keyboardButtons(canMsg.data[0]);   
    }

    else if (canMsg.id == 1598){
      keyboardJoystick(canMsg.data[0], canMsg.data[1]);      
    }
  }
}
