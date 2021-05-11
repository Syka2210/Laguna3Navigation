#include <mcp2515.h>

struct can_frame canMsg;

MCP2515 mcp2515(10);

int high_box = 0;

String frequency_box;
String function_name;
String endString = ":end_string";

void setup() {
  Serial.begin(250000);
  mcp2515.reset();
  mcp2515.setBitrate(CAN_500KBPS, MCP_8MHZ);
  mcp2515.setNormalMode();
}

//Volume function
void volume_text(bool counter){
  bool next_digit = false;
  while (counter == false){
    if (mcp2515.readMessage(&canMsg) == MCP2515::ERROR_OK){
      if (canMsg.can_id == 1313 && canMsg.data[0] == 0x74){
        return;
      }
      if (canMsg.can_id == 289 && canMsg.data[0] == 0x23){
        char digit1 = canMsg.data[6];
        Serial.print(digit1);
        next_digit = true;
      }
      if (canMsg.can_id == 289 && canMsg.data[0] == 0x24 && next_digit == true){
        char digit2 = canMsg.data[1];
        Serial.print(digit2);
        Serial.println(endString);
        //Serial.println();
      }
    }
  }
}

//Sources function
void sources(){
  if (canMsg.data[1] == 0x1F){
    Serial.print("Source:Radio");
    Serial.println(endString);
  } 
  else if (canMsg.data[1] == 0x27){
    Serial.print("Source:cd_player");
    Serial.println(endString);
  } 
  else if (canMsg.data[1] == 0x43){
    Serial.print("Source:Auxiliary audio sources");
    Serial.println(endString);
  } 
} 

//Channel number function -- CHECKED
String chan_number(char recv_char){
  String ch_nr = "0";
  if (recv_char == 0x10) ch_nr = "1";
  if (recv_char == 0x11) ch_nr = "2";
  if (recv_char == 0x12) ch_nr = "3";
  if (recv_char == 0x13) ch_nr = "4";
  if (recv_char == 0x14) ch_nr = "5";
  if (recv_char == 0x15) ch_nr = "6";
  if (recv_char == 0x46) ch_nr = "7";
  if (recv_char == 0x47) ch_nr = "8";
  if (recv_char == 0x48) ch_nr = "9";
  if (recv_char == 0x49) ch_nr = "10";
  if (recv_char == 0x4A) ch_nr = "11";
  if (recv_char == 0x4B) ch_nr = "12";
  return ch_nr;
}

//Radio frequency_type_box
String frequency_type_box(bool counter){
  String title;
  char character;
  bool counter_0x00 = false;
  while (counter == false){
    if (mcp2515.readMessage(&canMsg) == MCP2515::ERROR_OK){ 
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
}

//Icons function - CHECKED
String icons(char input){
  String iconOutput;
  if (input == 0x01) iconOutput = "icon_Audio_settings";
  if (input == 0x02) iconOutput = "icon_Phone_settings";
  if (input == 0x03) iconOutput = "icon_System_settings";
  if (input == 0x04) iconOutput = "icon_Bluetooth";
  if (input == 0x05) iconOutput = "icon_Language";
  if (input == 0x06) iconOutput = "icon_Sound_optimization";
  if (input == 0x07) iconOutput = "icon_Musical_atmosphere";
  if (input == 0x08) iconOutput = "icon_Display_CD_time";
  if (input == 0x09) iconOutput = "icon_Radio_functions";
  if (input == 0x1A) iconOutput = "icon_Pair_phone";
  if (input == 0x12) iconOutput = "icon_Music_note";
  if (input == 0x13) iconOutput = "icon_Vehicle_phonebook";
  if (input == 0x14) iconOutput = "icon_Mobile_phonebook";
  if (input == 0x15) iconOutput = "icon_Call_history";
  if (input == 0x16) iconOutput = "icon_Mail_box";
  if (input == 0x17) iconOutput = "icon_Directory_management";
  if (input == 0x18) iconOutput = "icon_Other_audio_settings";
  if (input == 0x1D) iconOutput = "icon_Missed_calls";
  if (input == 0x1E) iconOutput = "icon_Received_calls";
  if (input == 0x1F) iconOutput = "icon_Dialed_numbers";
  if (input == 0x20) iconOutput = "icon_Put_on_hold";
  if (input == 0x21) iconOutput = "icon_USB";
  if (input == 0x24) iconOutput = "icon_Volume";
  if (input == 0x25) iconOutput = "icon_Ringtone";
  if (input == 0x26) iconOutput = "icon_Cercle_full";
  if (input == 0x27) iconOutput = "icon_Cercle_empty";
  if (input == 0x28) iconOutput = "icon_Checked_box";
  if (input == 0x29) iconOutput = "icon_Unchecked_box";
  if (input == 0x2B) iconOutput = "icon_Voice_prompt_volume";
  if (input == 0x40) iconOutput = "icon_1";
  if (input == 0x41) iconOutput = "icon_2";
  if (input == 0x42) iconOutput = "icon_3";
  if (input == 0x43) iconOutput = "icon_4";
  if (input == 0x44) iconOutput = "icon_5";
  if (input == 0x60) iconOutput = "icon_Adaptation_volume";
  if (input == 0x62) iconOutput = "icon_Emergency";
  
  return iconOutput;
}

//Highlighted box
int high_box_function(bool counter){
  int cbh = 0;
  while (counter == false){
    if (mcp2515.readMessage(&canMsg) == MCP2515::ERROR_OK){
      if (canMsg.can_id == 289 && canMsg.data[0] == 0x21){
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
  
}

//Menu settings volume function 
void menu_volume(bool counter, String function){
  String menu_volume;
  while (counter == false){
    if  (mcp2515.readMessage(&canMsg) == MCP2515::ERROR_OK){
      if (canMsg.can_id == 1313 && canMsg.data[0] == 0x74){
        //Serial transmit the titles
        Serial.print("menu_volume:");
        Serial.print(function);
        Serial.print(":");
        Serial.print(menu_volume);
        Serial.println(endString);
        // end function
        return;
      }
      if (canMsg.can_id == 289 && canMsg.data[0] == 0x22){
        menu_volume = canMsg.data[6];
      }      
    }
  }

}

//
void musical_atmosphere(bool counter){
  String title_1;
  String title_2;
  String title_3;
  bool start_titles = false;
  int counter_0D = 1;
  int checked_cercle = 0;
  int curent_selected = 0;
  int bass_volume;
  int treble_volume;
  char bass_sign[1];
  char treble_sign[1];
  bool counter_F1 = false;
  bool counter_0x00 = false;
  char character;
  while (counter == false){
    if (mcp2515.readMessage(&canMsg) == MCP2515::ERROR_OK){
      if (canMsg.can_id == 1313 && canMsg.data[0] == 0x74){
        //Serial transmit the titles
        // String ex: musical_atmosphere : 2 : off : on :   : title_1 : title_2 :   : + : 5 : - : 2 : end_string
        //function name
        Serial.print("musical_atmosphere:");
        //Highlighted portion 21/1=text box ; 22/2=bass ; 23/3=treble
        Serial.print(curent_selected);
        Serial.print(":");
        //Checked circle
        if (title_1.length() > 0){
          if ((checked_cercle & 1) == 1){
            Serial.print("on");
          }else Serial.print("off");
        }
        Serial.print(":");
        if (title_2.length() > 0){
          if ((checked_cercle & 2) == 2){
            Serial.print("on");
          }else Serial.print("off");
        }
        Serial.print(":");
        if (title_3.length() > 0){
          if ((checked_cercle & 4) == 4){
            Serial.print("on");
          }else Serial.print("off");
        }
        Serial.print(":");
        //Titles
        Serial.print(title_1);
        Serial.print(":");
        Serial.print(title_2);
        Serial.print(":");
        Serial.print(title_3);
        Serial.print(":");
        //Volume bars values
        Serial.print(bass_sign);
        Serial.print(":");
        Serial.print(bass_volume);
        Serial.print(":");
        Serial.print(treble_sign);
        Serial.print(":");
        Serial.print(treble_volume);
        Serial.println(endString);

        return;
      }

      if (canMsg.can_id == 289 && canMsg.data[0] >=0x23){ 
        start_titles = true;
      }

      if (canMsg.can_id == 289 && canMsg.data[0] == 0x22){
        curent_selected = canMsg.data[4] - 32;
      }

      if (canMsg.can_id == 289 && start_titles == true){

        ////////// Counter for separating the titles //////////

        for (int i=1; i<=7; i++){
          if (canMsg.data[i] == 0x0D){
            counter_0D++;
          }
          if (canMsg.data[i] == 0x00 && i != 0){
            if (counter_0x00 == true){
              counter_0D++;
              counter_0x00 = false;
              continue;
            } else counter_0x00 = true;
          } else counter_0x00 = false;

          if (canMsg.data[i] == 0xF1) counter_F1 = true;
          if (counter_F1 == true && canMsg.data[i] == 0x26){
            if (counter_0D == 1) checked_cercle = checked_cercle | 1;
            if (counter_0D == 2) checked_cercle = checked_cercle | 2;
            if (counter_0D == 3) checked_cercle = checked_cercle | 4;
            counter_F1 = false;
            continue;
          }

          if (counter_0D == 1 && canMsg.data[i] >= 0x20 && canMsg.data[i] <= 0x7E){
            character = canMsg.data[i];
            title_1.concat(character);
          }

          if (counter_0D == 2 && canMsg.data[i] >= 0x20 && canMsg.data[i] <= 0x7E){
            character = canMsg.data[i];
            title_2.concat(character);
          }

          if (counter_0D == 3 && canMsg.data[i] >= 0x20 && canMsg.data[i] <= 0x7E){
            character = canMsg.data[i];
            title_3.concat(character);
          }

          if (counter_0D == 4){
            if (i == 5){
              if (canMsg.data[i] <= 0x09){
                bass_sign[0] = '+';
                bass_volume = canMsg.data[i];
              }
              if (canMsg.data[i] >= 0xF7 && canMsg.data[i] <= 0xFF){
                bass_sign[0] = '-';
                bass_volume = 256 - canMsg.data[i];
              }
            }
            if (i == 6){
              if (canMsg.data[i] <= 0x09){
                treble_sign[0] = '+';
                treble_volume = canMsg.data[i];
              } 
              if (canMsg.data[i] >= 0xF7 && canMsg.data[i] <= 0xFF){
                treble_sign[0] = '-';
                treble_volume = 256 - canMsg.data[i];
              }
            }
          }
        } 
      }     
    }
  }
}

//Radio function - CHECKED
void gridSortRadio(bool counter) {
  //Serial.println("Entered gridSortRadio");
  int arraySize = 10;
  String array3x3[arraySize];
  bool start_titles = false;
  bool counter_0x00 = false;
  bool highPass = false;
  int rowNr = 1;
  int columnNr = 1;
  int arrayPos = 0;
  int arrayElements = 0;
  char character;
  while (counter == false) {
    if (mcp2515.readMessage(&canMsg) == MCP2515::ERROR_OK) {
      if (canMsg.can_id == 1313 && canMsg.data[0] == 0x74) {
        //Check how many strings are in the 3x3 array
        for (int i = 0; i < arraySize; i++) {
          if (array3x3[i].length() > 0) arrayElements++;
        }
        //Check how many strings are in the 3x3 array

        //Serial transmit the titles depending of number
        if (arrayElements == 3) {
          //Insert below the 3 items identifier
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
          return;
        }
        if (arrayElements >= 3) {
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

      if (canMsg.can_id == 289 && canMsg.data[0] >= 0x23) {
        start_titles = true;
      }

      if (canMsg.can_id == 289 && start_titles == true) {
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
          if (rowNr == 1 && columnNr == 1) arrayPos = 1;
          if (rowNr == 2 && columnNr == 1) arrayPos = 2;
          if (rowNr == 3 && columnNr == 1) arrayPos = 3;
          if (rowNr == 1 && columnNr == 2) arrayPos = 4;
          if (rowNr == 2 && columnNr == 2) arrayPos = 5;
          if (rowNr == 3 && columnNr == 2) arrayPos = 6;
          if (rowNr == 1 && columnNr == 3) arrayPos = 7;
          if (rowNr == 2 && columnNr == 3) arrayPos = 8;
          if (rowNr == 3 && columnNr == 3) arrayPos = 9;
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

//Universal 3x2 grid - Checked
void gridSort3x2(bool counter){
  int arraySize = 7;
  String array3x2[arraySize];
  bool start_titles = false;
  bool counter_0x00 = false;
  int rowNr = 1;
  int columnNr = 1;
  int arrayPos = 0;
  int arrayElements = 0;
  char character;
  while (counter == false){
    if (mcp2515.readMessage(&canMsg) == MCP2515::ERROR_OK){
      if (canMsg.can_id == 1313 && canMsg.data[0] == 0x74){
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

    if (canMsg.can_id == 289 && canMsg.data[0] ==0x23){ 
      start_titles = true;
    }

    if (canMsg.can_id == 289 && start_titles == true){
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
          if (rowNr == 1 && columnNr == 1) arrayPos = 1;
          if (rowNr == 2 && columnNr == 1) arrayPos = 2;
          if (rowNr == 3 && columnNr == 1) arrayPos = 3;
          if (rowNr == 1 && columnNr == 2) arrayPos = 4;
          if (rowNr == 2 && columnNr == 2) arrayPos = 5;
          if (rowNr == 3 && columnNr == 2) arrayPos = 6;
          //Checking where on the 3x2 array whe are to save the string

          //Saving the string on the coresponding 3x3 array position
          if (columnNr == 1){
              if (canMsg.data[i] <= 0x7F && canMsg.data[i] != 0x0D && canMsg.data[i] != 0x00){
                //Save the icon name in the first array column
                array3x2[arrayPos] = icons(canMsg.data[i]);
              }
            }
            if (columnNr == 2){
              if (canMsg.data[i] >= 0x20 && canMsg.data[i] <= 0x7E){
              character = canMsg.data[i];
              array3x2[arrayPos].concat(character);
              }
            }
          }
          //Saving the string on the coresponding 3x2 array position
      }
    }
  }
}

//Grid 3x3 
void gridSort3x3(bool counter){
  int arraySize = 7;
  String array3x3[arraySize];
  int arrayElements = 0;
  bool start_titles = false;
  bool counter_0x00 = false;
  bool highPass = false;
  int rowNr = 1;
  int columnNr = 1;
  int arrayPos = 0;
  char character;
  while (counter == false){
    if (mcp2515.readMessage(&canMsg) == MCP2515::ERROR_OK){
      if (canMsg.can_id == 1313 && canMsg.data[0] == 0x74){
        //Check how many strings are in the 3x3 array
        for (int i = 0; i < arraySize; i++){
          if (array3x3[i].length() > 0) arrayElements++;
        }
        //Check how many strings are in the 3x3 array

        //Save the curent selection in the function_name variable
        //function_name = array3x3[5];
        //Save the curent selection in the function_name variable

        //Serial transmit the titles depending of number
        if (arrayElements <= 2){
          //Insert below the 3 items identifier
          Serial.print("freq_grid1x3:");
          Serial.print(high_box);
          Serial.print(":");
          Serial.print(frequency_box);
          Serial.print(":");
          Serial.print(array3x3[1]);
          Serial.print(":");
          Serial.print(array3x3[4]);
          Serial.println(endString);
          return;
        }
        if (arrayElements >= 3){
          //Insert below the 3 items identifier
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

    if (canMsg.can_id == 289 && canMsg.data[0] ==0x23){ 
      start_titles = true;
    }

    if (canMsg.can_id == 289 && start_titles == true){
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
          if (rowNr == 1 && columnNr == 1) arrayPos = 1;
          if (rowNr == 2 && columnNr == 1) arrayPos = 2;
          if (rowNr == 3 && columnNr == 1) arrayPos = 3;
          if (rowNr == 1 && columnNr == 2) arrayPos = 4;
          if (rowNr == 2 && columnNr == 2) arrayPos = 5;
          if (rowNr == 3 && columnNr == 2) arrayPos = 6;
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

void loop(){
  if (mcp2515.readMessage(&canMsg) == MCP2515::ERROR_OK){
    if(canMsg.can_id == 289){
      //Volume function
      if(canMsg.data[0] == 0x10 && canMsg.data[2] == 0x35 && canMsg.data[6] == 0x00){
        Serial.print("device_volume:");
        volume_text(false);
      }

      //Highlighted box
      if(canMsg.data[0] == 0x10 &&  canMsg.data[2] == 0x20 && canMsg.data[3] == 0x00 && canMsg.data[6] == 0x00){
          //In the frame with the first bit=21 the 7'th bit represents the position of the highlighted
          //box. The first box has the value 20, the second 21 and so on.
          //Serial.println("ENTER HIGHLIGHTED BOX");
          high_box = high_box_function(false);
          //Serial.println("EXIT");
          //Serial.println();
      }

                                       
      if (canMsg.data[0] == 0x10 && canMsg.data[2] == 0x25){
        if ((canMsg.data[1] != 0x43 && canMsg.data[1] != 0x31 && canMsg.data[1] != 0x2D && canMsg.data[1] != 0x3D) && 
             canMsg.data[4] == 0x09 && canMsg.data[7] != 0x00){
          //Serial.println("ENTER gridSort3x2");
          gridSort3x2(false); // CHECKED
          //Serial.println("EXIT");
          //Serial.println();
        }
        if (canMsg.data[4] == 0x17 && canMsg.data[5] == 0x02){
          //Serial.println("ENTER gridSortRadio");
          gridSortRadio(false); // CHECKED
          //Serial.println("EXIT");
          //Serial.println();
        }
        if ((canMsg.data[1] == 0x37 || canMsg.data[1] == 0x1F) && (canMsg.data[3] == 0x41 || canMsg.data[3] == 0x43) && 
             canMsg.data[4] == 0x07 && canMsg.data[5] == 0x02 && canMsg.data[6] == 0x20){
          gridSort3x3(false); // CHECKED        
        }
        if ((canMsg.data[1] == 0x43 || canMsg.data[1] == 0x31 || canMsg.data[1] == 0x2D || canMsg.data[1] == 0x3D) && 
             canMsg.data[3] == 0xC1 && canMsg.data[4] == 0x09 && canMsg.data[5] == 0x01 && canMsg.data[6] == 0x20){
          //Serial.println("ENTER frequency_box");
          frequency_box = frequency_type_box(false);
          //Serial.println("EXIT");
          //Serial.println();
        }
        if (canMsg.data[3] == 0x70 && canMsg.data[4] == 0x00 && canMsg.data[5] == 0x04 && canMsg.data[6] == 0x40){
          //Serial.println("ENTER menu_volume");
          menu_volume(false, function_name);
          //Serial.println("EXIT");
          //Serial.println();
        }
        
        if (canMsg.data[3] == 0x73 && canMsg.data[4] == 0x02 && canMsg.data[6] == 0x40){
          //Serial.println("ENTER musical_atmosphere");
          musical_atmosphere(false);
          //Serial.println("EXIT");
          //Serial.println();
        }
        if (canMsg.data[3] == 0x41 && canMsg.data[4] == 0x13 && canMsg.data[5] == 0x01 && 
            canMsg.data[6] == 0x20 && canMsg.data[7] == 0x80){
          //Serial.println("ENTER sources");
          sources();
          //Serial.println("EXIT");
          //Serial.println();
        }
      }                                       
    }
  }
}
