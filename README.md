# Laguna3Navigation
   A custom navigation module for Renault Laguna III, replacing the OE screen with an Android device that comunicates with the multimedia CAN BUS network. 
Back view             |  Front view
:-------------------------:|:-------------------------:
![IMG_20221002_092058](https://user-images.githubusercontent.com/73030948/193441050-f1efbb43-76cb-4566-a6fb-61b67fe674e8.jpg) |  ![IMG_20221002_092205](https://user-images.githubusercontent.com/73030948/193441074-121e753d-78a7-4cbe-b709-bd1acdd1360a.jpg)

   This project started as an alternative to the limited navigation unit that the Laguna III is equipped with (those until the R-link variant). The only difference from the aftermarket versions is the use of the OE radio. I've decided to keep the radio unit and find a way to display the info received from it to the Android device implemented, along with other useful info such as: weather info, ETA to the destination.
Also, the use of the Android navigation (Google Maps, Waze), and other apps (Spotify, YouTube, etc) was one of the main reasons for the implementation of this new multimedia interface.

   This new unit was planned as a plug in module. So no cutting of car wiring, screen fixture plastics or other trims of the car, only adding a few additional wires for a microphone and permanent power supply. 
Beeing a plug in module, the steering wheel satellite commands and the console joystick were mandatory. The satellite commands comunicate only with the radio, so that was no issue. On the other hand, the joystick for the navigation module was linked to that through CAN BUS, and that allowed me to remap the controls to my needs easily. More on that later. 

   For the android device, I had different options, with ups and downs:
- an Android tablet (Nexus 7). I have choosed that mainly for the fact that there is a kernel that allows the tablet to run in a deep sleep mode with very low curent consumption, and open in 2 seconds (no boot, since there is no complete shut down). I haven't found other tablets that have this kind of kernel, so this model was the only option. The cons - the tablet is old, runs slow, overheats quickly, hard to come by. 
- a Raspberry pi running LineageOS. This came with a few disadvantages. Big booting time, need to aquire additional modules (GPS, display, Bluetooth in some cases), high cost. On the other hand, it is more accessible (well not lately, the reason being the shortage of semiconductors), easier to work with, easier to upgrade with newer pi models, GPIO pins. 

   I've decided to use the nexus 7, at least for the first project. 

   After that being settled, the next issue at hand was acquiring the message packages sent from the radio to the navigation module ( well a part of it which is the display, so will refer to it on the rest of the presentation as the "display"). This multimedia unit uses a bitrate of 500 kbps, so accessing it was the easy part. "Decoding" all the frames received... now that was a little tricky. With the help of other who have tried something similar, but opposed to what I was trying to accomplish, I have managed to identify the following:
- each unit sends a message to announce that it is present and ready to work to the other units that may need the data from it. At the same time, this initial unit is expecting another messages from the other units to check for acknowledgement. Like pinging a server... Sort of. 
- the text messages with the icons and alignment are send using the ISO-TO (ISO 15765-2) standard. After each frame, the display sends acknowledgement frames to the radio, so I had to address that issue, or the radio would stop sending the next frames.

For reading the CAN Buss network, I started from an Arduino Uno with a MCP2515 module, and worked my way up to a Raspberry Pi Pico (due to the higher capabilities of processing data, higher Ram and flash memory, castellated holes for slim mounting, and let's be onest, for the slick look of it). 
This data needed to be sent to the Android device, and processed to show an elegant interface, similar to the original one, in order to navigate through the menu easily. That meant an app was required. So with the help of Google (literally), I've developed an app that receives the data from the Pico through the wired serial connection, and display our messages and icons same as the OE display.
OE display menu             |  New module display menu
:-------------------------:|:-------------------------:
![IMG_20210511_163811](https://user-images.githubusercontent.com/73030948/193441588-5d84a813-81a6-4cae-b3fa-1438c31c8d6d.jpg) | ![IMG_20221002_094015](https://user-images.githubusercontent.com/73030948/193441556-13d58a33-d28d-4645-b7f6-0681a14f08fb.jpg)

The message sent to the android device from Pico consist of a string with identifiers at the beginning and at the end, and with a separator for spliting the string easily. The Pico .ino file have a detailed info for explaining how each of the function works.
Message identifier  |  Data to be displayed |  end of message identifier
:-------------------------:|:-------------------------:|:-----------------:
view_43 | FM : 3 (highlighted box) : 92.90 : 100.00 : 120.20 : KISS FM : PRO FM : DIGI FM : 1 : 2 : 3 | end_string
