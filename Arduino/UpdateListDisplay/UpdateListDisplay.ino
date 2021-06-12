#include <SerialCommand.h>
#include <SPI.h>
#include <mcp2515.h>

SerialCommand sCmd;

MCP2515 mcp2515(10);
struct can_frame canMsg;

struct can_frame messageRecvMsg;

struct can_frame fr991check;
struct can_frame fr1466check;

bool radiostart     = false;
bool frame1407check = false;
bool frame792check  = false;
bool frame1496check = false;
bool frame449check  = false;
unsigned long fr1407time = 0;
unsigned long fr792time  = 0;
unsigned long fr1496time = 0;

void setup() {
  Serial.begin(2000000);
  mcp2515.reset();
  mcp2515.setBitrate(CAN_500KBPS, MCP_8MHZ);
  mcp2515.setNormalMode();

  //sCmd.addCommand("ss", startSync);

  //struct can_frame frcheck;
  fr991check.can_id  = 991;
  fr991check.can_dlc = 8;
  fr991check.data[0] = 0x28;
  fr991check.data[1] = 0x00;
  fr991check.data[2] = 0x81;
  fr991check.data[3] = 0x81;
  fr991check.data[4] = 0x81;
  fr991check.data[5] = 0x81;
  fr991check.data[6] = 0x81;
  fr991check.data[7] = 0x81;

  //FRAME 1466 CHECK
  fr1466check.can_id  = 1466;
  fr1466check.can_dlc = 8;
  fr1466check.data[0] = 0x74;
  fr1466check.data[1] = 0x81;
  fr1466check.data[2] = 0x81;
  fr1466check.data[3] = 0x81;
  fr1466check.data[4] = 0x81;
  fr1466check.data[5] = 0x81;
  fr1466check.data[6] = 0x81;
  fr1466check.data[7] = 0x81;
}

void startSync(){
  struct can_frame startSyncMsg;
  startSyncMsg.can_id  = 0x3CF;
  startSyncMsg.can_dlc = 8;
  startSyncMsg.data[0] = 0x22;
  startSyncMsg.data[1] = 0x00;
  startSyncMsg.data[2] = 0xFF;
  startSyncMsg.data[3] = 0xFF;
  startSyncMsg.data[4] = 0xFF;
  startSyncMsg.data[5] = 0xFF;
  startSyncMsg.data[6] = 0xFF;
  startSyncMsg.data[7] = 0xFF;

  mcp2515.sendMessage(&startSyncMsg);
  Serial.println("sent");
}

void fr1313Recv(){
  struct can_frame fr1313Msg;
  fr1313Msg.can_id  = 1313;
  fr1313Msg.can_dlc = 8;
  fr1313Msg.data[0] = 0x30;
  fr1313Msg.data[1] = 0x01;
  fr1313Msg.data[2] = 0x00;
  fr1313Msg.data[3] = 0xFF;
  fr1313Msg.data[4] = 0xFF;
  fr1313Msg.data[5] = 0xFF;
  fr1313Msg.data[6] = 0xFF;
  fr1313Msg.data[7] = 0xFF;

  mcp2515.sendMessage(&fr1313Msg);
  Serial.print(".");
}

void fr1313End(int frame){
  struct can_frame fr1313EndMsg;
  fr1313EndMsg.can_id  = 1313;
  fr1313EndMsg.can_dlc = 8;
  fr1313EndMsg.data[0] = 0x74;
  fr1313EndMsg.data[1] = 0xFF;
  fr1313EndMsg.data[2] = 0xFF;
  fr1313EndMsg.data[3] = 0xFF;
  fr1313EndMsg.data[4] = 0xFF;
  fr1313EndMsg.data[5] = 0xFF;
  fr1313EndMsg.data[6] = 0xFF;
  fr1313EndMsg.data[7] = 0xFF;

  mcp2515.sendMessage(&fr1313EndMsg);
  Serial.print(frame);
  Serial.println("  Stop");
}

void fr1407(){
  struct can_frame fr1407Msg;
  fr1407Msg.can_id   = 1407;
  fr1407Msg.can_dlc  = 8;
  fr1407Msg.data[0]  = 0x00;
  fr1407Msg.data[1]  = 0xFF;
  fr1407Msg.data[2]  = 0xFF;
  fr1407Msg.data[3]  = 0xFF;
  fr1407Msg.data[4]  = 0xFF;
  fr1407Msg.data[5]  = 0xFF;
  fr1407Msg.data[6]  = 0xFF;
  fr1407Msg.data[7]  = 0xFF;

  mcp2515.sendMessage(&fr1407Msg);
  Serial.println("1407");
}

void fr792(){
  struct can_frame fr792Msg;
  fr792Msg.can_id  = 792;
  fr792Msg.can_dlc = 8;
  fr792Msg.data[0] = 0x00;
  fr792Msg.data[1] = 0x00;
  fr792Msg.data[2] = 0x00;
  fr792Msg.data[3] = 0x00;
  fr792Msg.data[4] = 0x00;
  fr792Msg.data[5] = 0x00;
  fr792Msg.data[6] = 0x00;
  fr792Msg.data[7] = 0x00;

  mcp2515.sendMessage(&fr792Msg);
  Serial.println("792");
}

void fr1496(){
  struct can_frame fr1496Msg;
  fr1496Msg.can_id  = 1496;
  fr1496Msg.can_dlc = 5;
  fr1496Msg.data[0] = 0x00;
  fr1496Msg.data[1] = 0x07;
  fr1496Msg.data[2] = 0xFF;
  fr1496Msg.data[3] = 0x03;
  fr1496Msg.data[4] = 0xFF;

  mcp2515.sendMessage(&fr1496Msg);
  Serial.println("1496");
}

void fr449(){
  struct can_frame fr449Msg;
  fr449Msg.can_id  = 449;
  fr449Msg.can_dlc = 8;
  fr449Msg.data[0] = 0x70;
  fr449Msg.data[1] = 0xFF;
  fr449Msg.data[2] = 0xFF;
  fr449Msg.data[3] = 0xFF;
  fr449Msg.data[4] = 0xFF;
  fr449Msg.data[5] = 0xFF;
  fr449Msg.data[6] = 0xFF;
  fr449Msg.data[7] = 0xFF;

  mcp2515.sendMessage(&fr449Msg);
  Serial.println("449");
}

void loop() {
   if (mcp2515.readMessage(&canMsg) == MCP2515::ERROR_OK){
     //SEND FRAME 975 WHEN FRAME 991 IS RECEIVED AT RADIO START
     if (canMsg.can_id == 991){
       radiostart = true;
       int passed = 0;
       for (int i = 0; i < canMsg.can_dlc; i++){
        if (canMsg.data[i] == fr991check.data[i]){
          passed ++; 
        }
      }
      if (passed == 8){
        startSync();      
      }
     }
     
     //SEND FRAME 1313 WHEN TEXT IS SENT (FRAME 289)
     if(canMsg.can_id == 289){       
      if (canMsg.data[7] == 0x81){
        fr1313End(canMsg.can_id);         
       }
       else fr1313Recv();
     }

     //SEND FRAME 1407 REGULARELY AT 500 ms WHEN RADIO STARTS 
     //if (canMsg.can_id == 1423 && frame1407check == false){
     if (radiostart == true && frame1407check == false){  
      //if (canMsg.data[1] == 0x02){
        frame1407check = true;
        fr1407time = millis();
        fr1407();
      //}
     }else if (frame1407check == true && millis() - fr1407time >= 500){
       fr1407time = millis();
       fr1407();
     }

    //SEND FRAME 792 REGULARELY AT --- ms WHEN RADIO STARTS
    if (radiostart == true && frame792check == false){
      frame792check = true;
      fr792time = millis();
      fr792();
    } else if (frame792check == true && millis() - fr792time >= 3000){
      fr792time = millis();
      fr792();
    }

    //SEND FRAME 1496 REGULARELY AT --- ms WHEN RADIO STARTS
    if (radiostart == true && frame1496check == false){
      frame1496check = true;
      fr1496time = millis();
      fr1496();
    }else if (frame1496check == true && millis() - fr1496time >= 500){
      fr1496time = millis();
      fr1496();
    }

    //SEND FRAME 449 AFTER THE FRAME 1466 AND CHECK TO RECEIVE FRAME 1473
    if (canMsg.can_id == 1466){
      fr449();
      frame449check = true;
    }

    //Check if frame 1473 is received
    if (canMsg.can_id == 1473){
      Serial.print("1473 and 449sent is: ");
      Serial.println(frame449check);
    }     
   }
}
