package com.example.multimediainterfacev12;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.hardware.usb.UsbDevice;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import me.aflak.arduino.Arduino;
import me.aflak.arduino.ArduinoListener;

public class MainActivity extends AppCompatActivity implements ArduinoListener {
    private Arduino arduino;

    //Debug textView
    private TextView debugTextbox;


    //Declaring the volume layout and the volume text box
    private TextView volume;


    //Declaring the Radio Layout and the textviews
    private LinearLayout radio3x4grid;
    private TextView radio1x2text, radio1x3text, radio1x4text;
    //----------------------------
    CardView radio2x1card;
    private TextView radio2x1text;
    CardView radio2x2card;
    private TextView radio2x2text;
    CardView radio2x3card;
    private TextView radio2x3text;
    CardView radio2x4card;
    private TextView radio2x4text;
    //----------------------------
    private TextView radio3x2text, radio3x3text, radio3x4text;

    //Declaring the 3x3 grid Layout
    private LinearLayout radio3x3grid;
    private TextView radio3x3grid1x1text;
    private TextView radio3x3grid1x2text;
    private TextView radio3x3grid1x3text;
    CardView radio3x3grid2x1card;
    CardView radio3x3grid2x2card;
    CardView radio3x3grid2x3card;
    private TextView radio3x3grid2x1text;
    private TextView radio3x3grid2x2text;
    private TextView radio3x3grid2x3text;
    private TextView radio3x3grid3x1text;
    private TextView radio3x3grid3x2text;
    private TextView radio3x3grid3x3text;



    //Declaring the 3x2 menu Layout
    private LinearLayout complex3x2grid;
    private TextView complex1x2text, complex2x2text, complex3x2text;
    private TextView complex1x1text, complex2x1text, complex3x1text;
    //-----------------------------
    private ImageView iconAudioSettings1x1, iconAudioSettings2x1, iconAudioSettings3x1;
    private ImageView iconPhoneSettings1x1, iconPhoneSettings2x1, iconPhoneSettings3x1;
    private ImageView iconSystemSettings1x1, iconSystemSettings2x1, iconSystemSettings3x1;
    private ImageView iconBluetooth1x1, iconBluetooth2x1, iconBluetooth3x1;
    private ImageView iconLanguage1x1, iconLanguage2x1, iconLanguage3x1;
    private ImageView iconSoundOptimisation1x1, iconSoundOptimisation2x1, iconSoundOptimisation3x1;
    private ImageView iconMusicalAtmosphere1x1, iconMusicalAtmosphere2x1, iconMusicalAtmosphere3x1;
    private ImageView iconDisplayCDtime1x1, iconDisplayCDtime2x1, iconDisplayCDtime3x1;
    private ImageView iconRadioFunctions1x1, iconRadioFunctions2x1, iconRadioFunctions3x1;
    private ImageView iconSeriousClassics1x1, iconSeriousClassics2x1, iconSeriousClassics3x1;
    private ImageView iconVariedSpeech1x1, iconVariedSpeech2x1, iconVariedSpeech3x1;
    private ImageView iconNews1x1, iconNews2x1, iconNews3x1;
    private ImageView iconSport1x1, iconSport2x1, iconSport3x1;
    private ImageView iconPopMusic1x1, iconPopMusic2x1, iconPopMusic3x1;
    private ImageView iconPairPhone1x1, iconPairPhone2x1, iconPairPhone3x1;
    private ImageView iconMusicNote1x1, iconMusicNote2x1, iconMusicNote3x1;
    private ImageView iconPhonebook1x1, iconPhonebook2x1, iconPhonebook3x1;
    private ImageView iconCallHistory1x1, iconCallHistory2x1, iconCallHistory3x1;
    private ImageView iconMailbox1x1, iconMailbox2x1, iconMailbox3x1;
    private ImageView iconDirectoryManagement1x1, iconDirectoryManagement2x1, iconDirectoryManagement3x1;
    private ImageView iconOtherAudioSettings1x1, iconOtherAudioSettings2x1, iconOtherAudioSettings3x1;
    private ImageView iconMissedCalls1x1, iconMissedCalls2x1, iconMissedCalls3x1;
    private ImageView iconReceivedCalls1x1, iconReceivedCalls2x1, iconReceivedCalls3x1;
    private ImageView iconDialedNumbers1x1, iconDialedNumbers2x1, iconDialedNumbers3x1;
    private ImageView iconPutOnHold1x1, iconPutOnHold2x1, iconPutOnHold3x1;
    private ImageView iconUSB1x1, iconUSB2x1, iconUSB3x1;
    private ImageView iconVolume1x1, iconVolume2x1, iconVolume3x1;
    private ImageView iconRingtone1x1, iconRingtone2x1, iconRingtone3x1;
    private ImageView iconCercle1x1empty, iconCercle2x1empty, iconCercle3x1empty;
    private ImageView iconCercle1x1full, iconCercle2x1full, iconCercle3x1full;
    private ImageView iconCheckedBox1x1, iconCheckedBox2x1, iconCheckedBox3x1;
    private ImageView iconUncheckedBox1x1, iconUncheckedBox2x1, iconUncheckedBox3x1;
    private ImageView iconFolder1x1, iconFolder2x1, iconFolder3x1;
    private ImageView iconVoicePromptVolume1x1, iconVoicePromptVolume2x1, iconVoicePromptVolume3x1;
    private ImageView iconNumber_1_1x1, iconNumber_1_2x1, iconNumber_1_3x1;
    private ImageView iconNumber_2_1x1, iconNumber_2_2x1, iconNumber_2_3x1;
    private ImageView iconNumber_3_1x1, iconNumber_3_2x1, iconNumber_3_3x1;
    private ImageView iconNumber_4_1x1, iconNumber_4_2x1, iconNumber_4_3x1;
    private ImageView iconNumber_5_1x1, iconNumber_5_2x1, iconNumber_5_3x1;
    private ImageView iconAdaptationVolume1x1, iconAdaptationVolume2x1, iconAdaptationVolume3x1;
    private ImageView iconEmergency1x1, iconEmergency2x1, iconEmergency3x1;




    //Declaring the volume settings Layout
    private LinearLayout settigsMenuProgressBar;
    private ProgressBar menuVolumeProgressBar;
    private TextView functionName;
    private TextView currentValue;


    //Declaring the musical atmosphere menu Layout
    private LinearLayout musicalAtmosphere;
    private ImageView emptyCercleRow1, checkedCercleRow1, emptyCercleRow2, checkedCercleRow2, emptyCercleRow3, checkedCercleRow3;
    private TextView musicalAtmosphereMainText1, musicalAtmosphereMainText2, musicalAtmosphereMainText3;
    CardView cardRow2;
    CardView cardText2;
    private LinearLayout bassTrebleLayout;
    CardView bassTextCard;
    CardView trebleTextCard;
    private ProgressBar bassProgressBar;
    private ProgressBar trebleProgressBar;


    //Declaring the Source Tab Layout
    CardView radioCard;
    CardView cdplayerCard;
    CardView auxCard;

    //Declaring the grid 4 Layout
    private LinearLayout grid4;
    private TextView grid4text1x1;
    private TextView grid4text2x1;
    private TextView grid4text3x1;
    private TextView grid4text1x2;

    //Declaring the Information one box Layout
    private LinearLayout infoGrid;
    private TextView infoGridText;

    String messageReceived = "";
    int a = 0;
    int menuVolumeCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        arduino = new Arduino(this, 250000);

        debugTextbox = findViewById(R.id.debugText);
        debugTextbox.setMovementMethod(new ScrollingMovementMethod());


        volume = findViewById(R.id.volume_text);

        //Declaring radio 3x4 grid and it's cards and text views
        radio3x4grid = findViewById(R.id.radio3x4grid);
        //--------------------------------------------
        radio1x2text = findViewById(R.id.radio1x2text);
        radio1x3text = findViewById(R.id.radio1x3text);
        radio1x4text = findViewById(R.id.radio1x4text);
        //--------------------------------------------
        radio2x1card = findViewById(R.id.radio2x1card);
        radio2x1text = findViewById(R.id.radio2x1text);
        radio2x2card = findViewById(R.id.radio2x2card);
        radio2x2text = findViewById(R.id.radio2x2text);
        radio2x3card = findViewById(R.id.radio2x3card);
        radio2x3text = findViewById(R.id.radio2x3text);
        radio2x4card = findViewById(R.id.radio2x4card);
        radio2x4text = findViewById(R.id.radio2x4text);
        //--------------------------------------------
        radio3x2text = findViewById(R.id.radio3x2text);
        radio3x3text = findViewById(R.id.radio3x3text);
        radio3x4text = findViewById(R.id.radio3x4text);

        //Declaring 3x3 radio grid
        radio3x3grid = findViewById(R.id.radio3x3grid);
        radio3x3grid2x1card = findViewById(R.id.radio3x3grid2x1card);
        radio3x3grid2x2card = findViewById(R.id.radio3x3grid2x2card);
        radio3x3grid2x3card = findViewById(R.id.radio3x3grid2x3card);
        //--------------------------------------------
        radio3x3grid1x1text = findViewById(R.id.radio3x3grid1x1text);
        radio3x3grid1x2text = findViewById(R.id.radio3x3grid1x2text);
        radio3x3grid1x3text = findViewById(R.id.radio3x3grid1x3text);
        radio3x3grid2x1text = findViewById(R.id.radio3x3grid2x1text);
        radio3x3grid2x2text = findViewById(R.id.radio3x3grid2x2text);
        radio3x3grid2x3text = findViewById(R.id.radio3x3grid2x3text);
        radio3x3grid3x1text = findViewById(R.id.radio3x3grid3x1text);
        radio3x3grid3x2text = findViewById(R.id.radio3x3grid3x2text);
        radio3x3grid3x3text = findViewById(R.id.radio3x3grid3x3text);



        //Declaring 3x2 grid with the first column for icons and second column for text
        complex3x2grid = findViewById(R.id.complex3x2grid);
        complex1x2text = findViewById(R.id.complex1x2text);
        complex2x2text = findViewById(R.id.complex2x2text);
        complex3x2text = findViewById(R.id.complex3x2text);
        complex1x1text = findViewById(R.id.complex1x1text);
        complex2x1text = findViewById(R.id.complex2x1text);
        complex3x1text = findViewById(R.id.complex3x1text);
        //------------------------------------------------
        iconAudioSettings1x1 = findViewById(R.id.complex1x1audioSettings);
        iconAudioSettings2x1 = findViewById(R.id.complex2x1audioSettings);
        iconAudioSettings3x1 = findViewById(R.id.complex3x1audioSettings);
        //++++++++++++++++++++++++++++++++++++++++++++++++
        iconPhoneSettings1x1 = findViewById(R.id.complex1x1phoneSettings);
        iconPhoneSettings2x1 = findViewById(R.id.complex2x1phoneSettings);
        iconPhoneSettings3x1 = findViewById(R.id.complex3x1phoneSettings);
        //++++++++++++++++++++++++++++++++++++++++++++++++
        iconSystemSettings1x1 = findViewById(R.id.complex1x1systemSettings);
        iconSystemSettings2x1 = findViewById(R.id.complex2x1systemSettings);
        iconSystemSettings3x1 = findViewById(R.id.complex3x1systemSettings);
        //++++++++++++++++++++++++++++++++++++++++++++++++
        iconBluetooth1x1 = findViewById(R.id.complex1x1bluetooth);
        iconBluetooth2x1 = findViewById(R.id.complex2x1bluetooth);
        iconBluetooth3x1 = findViewById(R.id.complex3x1bluetooth);
        //++++++++++++++++++++++++++++++++++++++++++++++++
        iconLanguage1x1 = findViewById(R.id.complex1x1language);
        iconLanguage2x1 = findViewById(R.id.complex2x1language);
        iconLanguage3x1 = findViewById(R.id.complex3x1language);
        //++++++++++++++++++++++++++++++++++++++++++++++++
        iconSoundOptimisation1x1 = findViewById(R.id.complex1x1soundOptimization);
        iconSoundOptimisation2x1 = findViewById(R.id.complex2x1soundOptimization);
        iconSoundOptimisation3x1 = findViewById(R.id.complex3x1soundOptimization);
        //++++++++++++++++++++++++++++++++++++++++++++++++
        iconMusicalAtmosphere1x1 = findViewById(R.id.complex1x1musicalAtmosphere);
        iconMusicalAtmosphere2x1 = findViewById(R.id.complex2x1musicalAtmosphere);
        iconMusicalAtmosphere3x1 = findViewById(R.id.complex3x1musicalAtmosphere);
        //++++++++++++++++++++++++++++++++++++++++++++++++
        iconDisplayCDtime1x1 = findViewById(R.id.complex1x1displayCDtime);
        iconDisplayCDtime2x1 = findViewById(R.id.complex2x1displayCDtime);
        iconDisplayCDtime3x1 = findViewById(R.id.complex3x1displayCDtime);
        //++++++++++++++++++++++++++++++++++++++++++++++++
        iconRadioFunctions1x1 = findViewById(R.id.complex1x1radioFunctions);
        iconRadioFunctions2x1 = findViewById(R.id.complex2x1radioFunctions);
        iconRadioFunctions3x1 = findViewById(R.id.complex3x1radioFunctions);
        //++++++++++++++++++++++++++++++++++++++++++++++++
        iconSeriousClassics1x1 = findViewById(R.id.complex1x1seriousClassics);
        iconSeriousClassics2x1 = findViewById(R.id.complex2x1seriousClassics);
        iconSeriousClassics3x1 = findViewById(R.id.complex3x1seriousClassics);
        //++++++++++++++++++++++++++++++++++++++++++++++++
        iconVariedSpeech1x1 = findViewById(R.id.complex1x1variedSpeech);
        iconVariedSpeech2x1 = findViewById(R.id.complex2x1variedSpeech);
        iconVariedSpeech3x1 = findViewById(R.id.complex3x1variedSpeech);
        //++++++++++++++++++++++++++++++++++++++++++++++++
        iconNews1x1 = findViewById(R.id.complex1x1news);
        iconNews2x1 = findViewById(R.id.complex2x1news);
        iconNews3x1 = findViewById(R.id.complex3x1news);
        //++++++++++++++++++++++++++++++++++++++++++++++++
        iconSport1x1 = findViewById(R.id.complex1x1sport);
        iconSport2x1 = findViewById(R.id.complex2x1sport);
        iconSport3x1 = findViewById(R.id.complex3x1sport);
        //++++++++++++++++++++++++++++++++++++++++++++++++
        iconPopMusic1x1 = findViewById(R.id.complex1x1popMusic);
        iconPopMusic2x1 = findViewById(R.id.complex2x1popMusic);
        iconPopMusic3x1 = findViewById(R.id.complex3x1popMusic);
        //++++++++++++++++++++++++++++++++++++++++++++++++
        iconPairPhone1x1 = findViewById(R.id.complex1x1pairPhone);
        iconPairPhone2x1 = findViewById(R.id.complex2x1pairPhone);
        iconPairPhone3x1 = findViewById(R.id.complex3x1pairPhone);
        //++++++++++++++++++++++++++++++++++++++++++++++++
        iconMusicNote1x1 = findViewById(R.id.complex1x1musicNote);
        iconMusicNote2x1 = findViewById(R.id.complex2x1musicNote);
        iconMusicNote3x1 = findViewById(R.id.complex3x1musicNote);
        //++++++++++++++++++++++++++++++++++++++++++++++++
        iconPhonebook1x1 = findViewById(R.id.complex1x1phonebook);
        iconPhonebook2x1 = findViewById(R.id.complex2x1phonebook);
        iconPhonebook3x1 = findViewById(R.id.complex3x1phonebook);
        //++++++++++++++++++++++++++++++++++++++++++++++++
        iconCallHistory1x1 = findViewById(R.id.complex1x1callHistory);
        iconCallHistory2x1 = findViewById(R.id.complex2x1callHistory);
        iconCallHistory3x1 = findViewById(R.id.complex3x1callHistory);
        //++++++++++++++++++++++++++++++++++++++++++++++++
        iconMailbox1x1 = findViewById(R.id.complex1x1mailbox);
        iconMailbox2x1 = findViewById(R.id.complex2x1mailbox);
        iconMailbox3x1 = findViewById(R.id.complex3x1mailbox);
        //++++++++++++++++++++++++++++++++++++++++++++++++
        iconDirectoryManagement1x1 = findViewById(R.id.complex1x1directoryManagement);
        iconDirectoryManagement2x1 = findViewById(R.id.complex2x1directoryManagement);
        iconDirectoryManagement3x1 = findViewById(R.id.complex3x1directoryManagement);
        //++++++++++++++++++++++++++++++++++++++++++++++++
        iconOtherAudioSettings1x1 = findViewById(R.id.complex1x1otherAudioSettings);
        iconOtherAudioSettings2x1 = findViewById(R.id.complex2x1otherAudioSettings);
        iconOtherAudioSettings3x1 = findViewById(R.id.complex3x1otherAudioSettings);
        //++++++++++++++++++++++++++++++++++++++++++++++++
        iconMissedCalls1x1 = findViewById(R.id.complex1x1missedCalls);
        iconMissedCalls2x1 = findViewById(R.id.complex2x1missedCalls);
        iconMissedCalls3x1 = findViewById(R.id.complex3x1missedCalls);
        //++++++++++++++++++++++++++++++++++++++++++++++++
        iconReceivedCalls1x1 = findViewById(R.id.complex1x1receivedCalls);
        iconReceivedCalls2x1 = findViewById(R.id.complex2x1receivedCalls);
        iconReceivedCalls3x1 = findViewById(R.id.complex3x1receivedCalls);
        //++++++++++++++++++++++++++++++++++++++++++++++++
        iconDialedNumbers1x1 = findViewById(R.id.complex1x1dialedNumbers);
        iconDialedNumbers2x1 = findViewById(R.id.complex2x1dialedNumbers);
        iconDialedNumbers3x1 = findViewById(R.id.complex3x1dialedNumbers);
        //++++++++++++++++++++++++++++++++++++++++++++++++
        iconPutOnHold1x1 = findViewById(R.id.complex1x1putOnHold);
        iconPutOnHold2x1 = findViewById(R.id.complex2x1putOnHold);
        iconPutOnHold3x1 = findViewById(R.id.complex3x1putOnHold);
        //++++++++++++++++++++++++++++++++++++++++++++++++
        iconUSB1x1 = findViewById(R.id.complex1x1usb);
        iconUSB2x1 = findViewById(R.id.complex2x1usb);
        iconUSB3x1 = findViewById(R.id.complex3x1usb);
        //++++++++++++++++++++++++++++++++++++++++++++++++
        iconVolume1x1 = findViewById(R.id.complex1x1volume);
        iconVolume2x1 = findViewById(R.id.complex2x1volume);
        iconVolume3x1 = findViewById(R.id.complex3x1volume);
        //++++++++++++++++++++++++++++++++++++++++++++++++
        iconRingtone1x1 = findViewById(R.id.complex1x1ringtone);
        iconRingtone2x1 = findViewById(R.id.complex2x1ringtone);
        iconRingtone3x1 = findViewById(R.id.complex3x1ringtone);
        //++++++++++++++++++++++++++++++++++++++++++++++++
        iconCercle1x1empty = findViewById(R.id.complex1x1emptyCircle);
        iconCercle2x1empty = findViewById(R.id.complex2x1emptyCircle);
        iconCercle3x1empty = findViewById(R.id.complex3x1emptyCircle);
        //++++++++++++++++++++++++++++++++++++++++++++++++
        iconCercle1x1full = findViewById(R.id.complex1x1fullCircle);
        iconCercle2x1full = findViewById(R.id.complex2x1fullCircle);
        iconCercle3x1full = findViewById(R.id.complex3x1fullcircle);
        //++++++++++++++++++++++++++++++++++++++++++++++++
        iconCheckedBox1x1 = findViewById(R.id.complex1x1checkedCheckbox);
        iconCheckedBox2x1 = findViewById(R.id.complex2x1checkedCheckbox);
        iconCheckedBox3x1 = findViewById(R.id.complex3x1checkedCheckbox);
        //++++++++++++++++++++++++++++++++++++++++++++++++
        iconUncheckedBox1x1 = findViewById(R.id.complex1x1uncheckedCheckbox);
        iconUncheckedBox2x1 = findViewById(R.id.complex2x1uncheckedCheckbox);
        iconUncheckedBox3x1 = findViewById(R.id.complex3x1uncheckedCheckbox);
        //++++++++++++++++++++++++++++++++++++++++++++++++
        iconFolder1x1 = findViewById(R.id.complex1x1folder);
        iconFolder2x1 = findViewById(R.id.complex2x1folder);
        iconFolder3x1 = findViewById(R.id.complex3x1folder);
        //++++++++++++++++++++++++++++++++++++++++++++++++
        iconVoicePromptVolume1x1 = findViewById(R.id.complex1x1voicePromptVolume);
        iconVoicePromptVolume2x1 = findViewById(R.id.complex2x1voicePromptVolume);
        iconVoicePromptVolume3x1 = findViewById(R.id.complex3x2voicePromptVolume);
        //++++++++++++++++++++++++++++++++++++++++++++++++
        iconNumber_1_1x1 = findViewById(R.id.complex1x1number_1);
        iconNumber_1_2x1 = findViewById(R.id.complex2x1number_1);
        iconNumber_1_3x1 = findViewById(R.id.complex3x1number_1);
        //++++++++++++++++++++++++++++++++++++++++++++++++
        iconNumber_2_1x1 = findViewById(R.id.complex1x1number_2);
        iconNumber_2_2x1 = findViewById(R.id.complex2x1number_2);
        iconNumber_2_3x1 = findViewById(R.id.complex3x1number_2);
        //++++++++++++++++++++++++++++++++++++++++++++++++
        iconNumber_3_1x1 = findViewById(R.id.complex1x1number_3);
        iconNumber_3_2x1 = findViewById(R.id.complex2x1number_3);
        iconNumber_3_3x1 = findViewById(R.id.complex3x1number_3);
        //++++++++++++++++++++++++++++++++++++++++++++++++
        iconNumber_4_1x1 = findViewById(R.id.complex1x1number_4);
        iconNumber_4_2x1 = findViewById(R.id.complex2x1number_4);
        iconNumber_4_3x1 = findViewById(R.id.complex3x1number_4);
        //++++++++++++++++++++++++++++++++++++++++++++++++
        iconNumber_5_1x1 =findViewById(R.id.complex1x1number_5);
        iconNumber_5_2x1 =findViewById(R.id.complex2x1number_5);
        iconNumber_5_3x1 =findViewById(R.id.complex3x1number_5);
        //++++++++++++++++++++++++++++++++++++++++++++++++
        iconAdaptationVolume1x1 = findViewById(R.id.complex1x1adaptationVolume);
        iconAdaptationVolume2x1 = findViewById(R.id.complex2x1adaptationVolume);
        iconAdaptationVolume3x1 = findViewById(R.id.complex3x1adaptationVolume);
        //++++++++++++++++++++++++++++++++++++++++++++++++
        iconEmergency1x1 = findViewById(R.id.complex1x1emergency);
        iconEmergency2x1 = findViewById(R.id.complex2x1emergency);
        iconEmergency3x1 = findViewById(R.id.complex3x1emergency);




        //Declaring the menu Volume progress bar
        settigsMenuProgressBar = findViewById(R.id.settingMenuProgressBarr);
        menuVolumeProgressBar = (ProgressBar)findViewById(R.id.menuVolumeProgressBar);
        functionName = findViewById(R.id.functionName);
        currentValue = findViewById(R.id.currentValue);


        //Declaring the muscial atmosphere grid with two progress bars
        musicalAtmosphere = findViewById(R.id.musicalAtmosphere);
        emptyCercleRow1 = findViewById(R.id.musicAtRow1EmptyCerc);
        checkedCercleRow1 = findViewById(R.id.musicAtRow1FullCerc);
        emptyCercleRow2 = findViewById(R.id.musicAtRow2EmptyCerc);
        checkedCercleRow2 = findViewById(R.id.musicAtRow2FullCerc);
        emptyCercleRow3 = findViewById(R.id.musicAtRow3EmptyCerc);
        checkedCercleRow3 = findViewById(R.id.musicAtRow3FullCerc);
        //--------------------------------------------------------
        musicalAtmosphereMainText1 = findViewById(R.id.musicalAtmosphereMainText1);
        musicalAtmosphereMainText2 = findViewById(R.id.musicalAtmosphereMainText2);
        musicalAtmosphereMainText3 = findViewById(R.id.musicalAtmosphereMainText3);
        cardRow2 = findViewById(R.id.cardRow2);
        cardText2 = findViewById(R.id.cardText2);
        bassTrebleLayout = findViewById(R.id.bassTrebleLayout);
        bassTextCard = findViewById(R.id.bassTextCard);
        trebleTextCard = findViewById(R.id.trebleTextCard);
        bassProgressBar = (ProgressBar)findViewById(R.id.bassProgressBar);
        trebleProgressBar = (ProgressBar)findViewById(R.id.trebleProgressBar);


        //Declaring the source tab with the three card options
        radioCard = findViewById(R.id.sourceRadioCard);
        cdplayerCard = findViewById(R.id.sourceCdplayerCard);
        auxCard = findViewById(R.id.sourceAuxCard);

        //Declaring the grid 4 layout
        grid4 = findViewById(R.id.grid4);
        grid4text1x1 = findViewById(R.id.grid4text1x1);
        grid4text2x1 = findViewById(R.id.grid4text2x1);
        grid4text3x1 = findViewById(R.id.grid4text3x1);
        grid4text1x2 = findViewById(R.id.grid4text1x2);

        //Declaring the Information one box
        infoGrid = findViewById(R.id.infoGrid);
        infoGridText = findViewById(R.id.infoGridText);

    }

    @Override
    protected void onStart(){
        super.onStart();
        arduino.setArduinoListener((ArduinoListener) this);
    }

    @Override
    public void onArduinoAttached(UsbDevice device){
        showToast("Arduino attached!");
        arduino.open(device);
    }

    @Override
    public void onArduinoDetached() {
        showToast("Arduino detached!");
        arduino.close();
    }

    @Override
    public void onArduinoMessage(byte[] bytes) {
        String str = new String(bytes);
        messageReceived = messageReceived + str;


        if (messageReceived.contains("end_string")) debugTextbox(messageReceived);
        


        if (messageReceived.contains("end_string")){
            // SOURCE
            if (messageReceived.toLowerCase().contains("source")){
                source(messageReceived);
                messageReceived = "";
            }

            //VOLUME
            else if (messageReceived.toLowerCase().contains("device_volume")){
                volume(messageReceived);
                messageReceived = "";
            }

            //HIGHLIGHTED BOX
            else if (messageReceived.toLowerCase().contains("highlightedbox")){
                highBox(messageReceived);
                messageReceived = "";
            }


            //////////////////////////RADIO/////////////////////////////
            //Radio 1x3 grid ---> SHORT
            else if (messageReceived.toLowerCase().contains("radio_grid1x3")){
                // string ex: radio_grid3x3 : 3 (highlighted box) : FM : 92.90 : KISS FM : 1 : end_string
                radioDisplayShort(messageReceived);
                messageReceived = "";
            }

            //Radio 3x3 grid ---> LONG
            else if (messageReceived.toLowerCase().contains("radio_grid3x3")){
                // string ex: radio_grid3x3 : 3 (highlighted box) : FM : 92.90 : KISS FM : 1 : 100.00 : PRO FM : 2 : 120.20 : DIGI FM : 3 : end_string
                radioDisplay(messageReceived);
                messageReceived = "";
            }

            //Frequency 3x3 grid  ---> LONG
            else if (messageReceived.toLowerCase().contains("freq_grid3x3")){
                // string ex: freq_grid3x3 : 3 (highlighted box) : PTY : 162 : 162 : 162 : 1 : 2 : - : end_string
                frequency3x3Display(messageReceived);
                messageReceived = "";
            }

            //Frequency 1x3 grid  ---> SHORT
            else if (messageReceived.toLowerCase().contains("freq_grid1x3")){
                // string ex: freq_grid3x3 : 3 (highlighted box) : PTY : 162 : 2 : end_string
                frequency3x3DisplayShort(messageReceived);
                messageReceived = "";
            }

            /*
            //Frequency type show
            else if (messageReceived.toLowerCase().contains("frequency_detailed")){
                // string ex: frequency_detailed : FM : Frequency Modulation : end_string
                frequency_detailed(messageReceived);
                messageReceived = "";
            }

             */

            /*
            //Frequency list
            else if (messageReceived.toLowerCase().contains("frequency_list")){
                // string ex: frequency_list : FM : PTY : LW : Frequency modulation : FM program type : Long wave : end_string
                frequency_list(messageReceived);
                messageReceived = "";
            }

             */
            //////////////////////////RADIO/////////////////////////////


            //////////////////////////MENU 3x2/////////////////////////////
            //Menu 3x2 grid ---> LONG
            else if (messageReceived.toLowerCase().contains("complex_grid3x2")){
                // STRING EX ---> complex_grid3x2 : icon1 : icon2 : icon3 : track_name1 : track_name2 : track_name3 : end_string
                menuDisplay(messageReceived);
                messageReceived = "";
            }

            //Menu 1x2 grid ---> SHORT
            else if (messageReceived.toLowerCase().contains("complex_grid1x2")){
                // STRING EX ---> complex_grid1x2 : icon1 : track_name1 : end_string
                menuDisplayShort(messageReceived);
                messageReceived = "";
            }

            //////////////////////////MENU 3x2/////////////////////////////


            ////////////////////VOLUME PROGRESS BAR////////////////////////
            else if (messageReceived.toLowerCase().contains("menu_volume")){
                // string ex: menu_volume : function_name : value : end_string
                menuVolumeProgressBar(messageReceived);
                messageReceived = "";
            }
            ////////////////////VOLUME PROGRESS BAR////////////////////////


            //////////////////////MUSICAL ATMOSPHERE//////////////////////
            else if (messageReceived.toLowerCase().contains("musical_atmosphere")){
                // String ex: musical_atmosphere : 2 : off : on : _ : title_1 : title_2 : _ : + : 5 : - : 2 : end_string
                menuMusicalAtmosphere(messageReceived);
                messageReceived = "";
            }
            //////////////////////MUSICAL ATMOSPHERE//////////////////////

            // 3+1 selection
            else if (messageReceived.toLowerCase().contains("confirm_cancel_function")){
                // string ex: confirm_cancel_function : Cancel : Confirm : : Do you want to reset : these parameters? : end_string
                confirm_cancel(messageReceived);
                messageReceived = "";
            }


            //CLEAR STRING IF NOT RECOGNIZED
            else messageReceived = "";

        }

    }

    @Override
    public void onArduinoOpened() {

    }

    @Override
    public void onUsbPermissionDenied() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        arduino.unsetArduinoListener();
        arduino.close();
    }

    public void debugTextbox(final String message){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                debugTextbox.append("\r\n" + ">" + message);
                //debugTextbox.setText(message);
            }
        });
    }

    public void source(final String message){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Set all sources back to initial color
                radioCard.setCardBackgroundColor(getResources().getColor(R.color.sourceMainBackground));
                cdplayerCard.setCardBackgroundColor(getResources().getColor(R.color.sourceMainBackground));
                auxCard.setCardBackgroundColor(getResources().getColor(R.color.sourceMainBackground));
                closeAllDisplays();

                // string ex: source : radio : end_string
                String[] source = message.split(":");
                // RADIO
                if (source[1].toLowerCase().contains("radio")){
                    radioCard.setCardBackgroundColor(getResources().getColor(R.color.sourceSelected));
                }
                // CD PLAYER
                if (source[1].toLowerCase().contains("cd_player")){
                    cdplayerCard.setCardBackgroundColor(getResources().getColor(R.color.sourceSelected));
                }
                // AUX
                if (source[1].toLowerCase().contains("auxiliary audio sources")){
                    auxCard.setCardBackgroundColor(getResources().getColor(R.color.sourceSelected));
                }
            }
        });
    }

    public void volume(final String message){
        runOnUiThread(new Runnable() {
            @SuppressLint("StringFormatInvalid")
            @Override
            public void run() {
                // string ex: volume : 22 : end_string
                String[] messageIds = message.split(":");
                volume.setText(getString(R.string.volume, messageIds[1]));
                //volume.setText("Volume: " + messageIds[1] + " ");
            }
        });
    }

    public void highBox(final String message){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Set all card colours to main colour
                radio2x2card.setCardBackgroundColor(getResources().getColor(R.color.sourceMainBackground));
                radio2x3card.setCardBackgroundColor(getResources().getColor(R.color.sourceMainBackground));
                radio2x4card.setCardBackgroundColor(getResources().getColor(R.color.sourceMainBackground));

                radio3x3grid2x2card.setCardBackgroundColor(getResources().getColor(R.color.sourceMainBackground));
                radio3x3grid2x3card.setCardBackgroundColor(getResources().getColor(R.color.sourceMainBackground));
                //Check for highlighted box nr
                String[] messageIds = message.split(":");
                if (messageIds[1].contains("2")){
                    radio2x2card.setCardBackgroundColor(getResources().getColor(R.color.sourceSelected));
                    radio3x3grid2x2card.setCardBackgroundColor(getResources().getColor(R.color.sourceSelected));
                }else if (messageIds[1].contains("3")){
                    radio2x3card.setCardBackgroundColor(getResources().getColor(R.color.sourceSelected));
                    radio3x3grid2x3card.setCardBackgroundColor(getResources().getColor(R.color.sourceSelected));
                }else if (messageIds[1].contains("4")){
                    radio2x4card.setCardBackgroundColor(getResources().getColor(R.color.sourceSelected));
                }
            }
        });
    }

    public void radioDisplayShort(final String message){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                closeAllDisplays();
                clearTextBoxes();
                //Set all card colours to main colour
                radio2x2card.setCardBackgroundColor(getResources().getColor(R.color.sourceMainBackground));
                radio2x3card.setCardBackgroundColor(getResources().getColor(R.color.sourceMainBackground));
                radio2x4card.setCardBackgroundColor(getResources().getColor(R.color.sourceMainBackground));
                // STRING EX -->  radio_grid3x3 : 3 (highlighted box) : FM : 92.90 : KISS FM : 1 : 100.00 : PRO FM : 2 : 120.20 : DIGI FM : 3 : end_string
                String[] messageIds = message.split(":");
                radio2x1text.setText(messageIds[2]);
                //---------------------------------
                radio2x2text.setText(messageIds[3]);
                radio2x3text.setText(messageIds[4]);
                radio2x4text.setText(messageIds[5]);
                //---------------------------------
                if (messageIds[1].contains("2")) radio2x2card.setCardBackgroundColor(getResources().getColor(R.color.sourceSelected));
                if (messageIds[1].contains("3")) radio2x3card.setCardBackgroundColor(getResources().getColor(R.color.sourceSelected));
                if (messageIds[1].contains("4")) radio2x4card.setCardBackgroundColor(getResources().getColor(R.color.sourceSelected));
                //Bring front the Radio layout.
                radio3x4grid.setVisibility(View.VISIBLE);
            }
        });
    }

    public void radioDisplay(final String message){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                closeAllDisplays();
                clearTextBoxes();
                //Set all card colours to main colour
                radio2x2card.setCardBackgroundColor(getResources().getColor(R.color.sourceMainBackground));
                radio2x3card.setCardBackgroundColor(getResources().getColor(R.color.sourceMainBackground));
                radio2x4card.setCardBackgroundColor(getResources().getColor(R.color.sourceMainBackground));
                // STRING EX -->  radio_grid3x3 : 3 (highlighted box) : FM : 92.90 :  100.00 : 120.20 : KISS FM : PRO FM : DIGI FM : 1 : 2 : 3 : end_string
                String[] messageIds = message.split(":");
                radio2x1text.setText(messageIds[2]);
                //---------------------------------
                radio1x2text.setText(messageIds[3]);
                radio2x2text.setText(messageIds[4]);
                radio3x2text.setText(messageIds[5]);
                //---------------------------------
                radio1x3text.setText(messageIds[6]);
                radio2x3text.setText(messageIds[7]);
                radio3x3text.setText(messageIds[8]);
                //---------------------------------
                radio1x4text.setText(messageIds[9]);
                radio2x4text.setText(messageIds[10]);
                radio3x4text.setText(messageIds[11]);
                //---------------------------------
                if (messageIds[1].contains("2")) radio2x2card.setCardBackgroundColor(getResources().getColor(R.color.sourceSelected));
                if (messageIds[1].contains("3")) radio2x3card.setCardBackgroundColor(getResources().getColor(R.color.sourceSelected));
                if (messageIds[1].contains("4")) radio2x4card.setCardBackgroundColor(getResources().getColor(R.color.sourceSelected));
                //Bring front the Radio layout.
                radio3x4grid.setVisibility(View.VISIBLE);


            }
        });
    }

    public void frequency3x3Display(final String message){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                closeAllDisplays();
                clearTextBoxes();
                //set all card colours to main colour
                radio3x3grid2x1card.setCardBackgroundColor(getResources().getColor(R.color.sourceMainBackground));
                radio3x3grid2x2card.setCardBackgroundColor(getResources().getColor(R.color.sourceMainBackground));
                radio3x3grid2x3card.setCardBackgroundColor(getResources().getColor(R.color.sourceMainBackground));
                // string ex: freq_grid3x3 : 3 (highlighted box) : PTY : 162 : 162 : 162 : 1 : 2 : - : end_string
                String[] messageIds = message.split(":");
                radio3x3grid2x1text.setText(messageIds[2]);
                //----------------------------------------
                radio3x3grid1x2text.setText(messageIds[3]);
                radio3x3grid2x2text.setText(messageIds[4]);
                radio3x3grid3x2text.setText(messageIds[5]);
                //----------------------------------------
                radio3x3grid1x3text.setText(messageIds[6]);
                radio3x3grid2x3text.setText(messageIds[7]);
                radio3x3grid3x3text.setText(messageIds[8]);
                //----------------------------------------
                if (messageIds[1].contains("2")) radio3x3grid2x2card.setCardBackgroundColor(getResources().getColor(R.color.sourceSelected));
                if (messageIds[1].contains("3")) radio3x3grid2x3card.setCardBackgroundColor(getResources().getColor(R.color.sourceSelected));
                //Bring front the layout
                radio3x3grid.setVisibility(View.VISIBLE);
            }
        });
    }

    public void frequency3x3DisplayShort(final String message){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                closeAllDisplays();
                clearTextBoxes();
                //set all card colours to main colour
                radio3x3grid2x1card.setCardBackgroundColor(getResources().getColor(R.color.sourceMainBackground));
                radio3x3grid2x2card.setCardBackgroundColor(getResources().getColor(R.color.sourceMainBackground));
                radio3x3grid2x3card.setCardBackgroundColor(getResources().getColor(R.color.sourceMainBackground));
                // string ex: freq_grid3x3 : 3 (highlighted box) : PTY : 162 : - : end_string
                String[] messageIds = message.split(":");
                radio3x3grid2x1text.setText(messageIds[2]);
                //----------------------------------------
                radio3x3grid2x2text.setText(messageIds[3]);
                //----------------------------------------
                radio3x3grid2x3text.setText(messageIds[4]);
                //----------------------------------------
                if (messageIds[1].contains("2")) radio3x3grid2x2card.setCardBackgroundColor(getResources().getColor(R.color.sourceSelected));
                if (messageIds[1].contains("3")) radio3x3grid2x3card.setCardBackgroundColor(getResources().getColor(R.color.sourceSelected));
                //Bring front the layout
                radio3x3grid.setVisibility(View.VISIBLE);
            }
        });
    }

    /*
    public void frequency_detailed(final String message){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                closeAllDisplays();
                clearTextBoxes();
                //set all card colours to main colour
                radio3x3grid2x1card.setCardBackgroundColor(getResources().getColor(R.color.sourceMainBackground));
                radio3x3grid2x2card.setCardBackgroundColor(getResources().getColor(R.color.sourceMainBackground));
                radio3x3grid2x3card.setCardBackgroundColor(getResources().getColor(R.color.sourceMainBackground));
                // string ex: frequency_detailed : FM : Frequency Modulation : end_string
                String[] messageIds = message.split(":");
                radio3x3grid2x1text.setText(messageIds[1]);
                radio3x3grid2x2text.setText(messageIds[2]);
                //----------------------------------------
                radio3x3grid2x2card.setCardBackgroundColor(getResources().getColor(R.color.sourceSelected));
                //Bring front the layout
                radio3x3grid.setVisibility(View.VISIBLE);
            }
        });
    }
     */

    /*
    public void frequency_list(final String message){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                closeAllDisplays();
                clearTextBoxes();
                // string ex: frequency_list : FM : PTY : LW : Frequency modulation : FM program type : Long wave : end_string
                String[] messageIds = message.split(":");
                radio3x3grid1x1text.setText(messageIds[1]);
                radio3x3grid2x1text.setText(messageIds[2]);
                radio3x3grid3x1text.setText(messageIds[3]);
                //---------------------------------------
                radio3x3grid1x2text.setText(messageIds[4]);
                radio3x3grid2x2text.setText(messageIds[5]);
                radio3x3grid3x2text.setText(messageIds[6]);
                //---------------------------------------
                radio3x3grid2x2card.setCardBackgroundColor(getResources().getColor(R.color.sourceSelected));
                //Bring front the layout
                radio3x3grid.setVisibility(View.VISIBLE);
            }
        });
    }
     */

    public void menuDisplay(final String message){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                closeAllDisplays();
                clearTextBoxes();
                setIconsToInvisible();
                // STRING EX ---> complex_grid3x2 : icon1 : icon2 : icon3 : track_name1 : track_name2 : track_name3 : end_string
                String[] messageIds = message.split(":");
                // Set first icon visible
                setIconsVisible(messageIds[1], messageIds[2], messageIds[3]);
                complex1x2text.setText(messageIds[4]);
                complex2x2text.setText(messageIds[5]);
                complex3x2text.setText(messageIds[6]);
                complex3x2grid.setVisibility(View.VISIBLE);
            }
        });
    }

    public void menuDisplayShort(final String message){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                closeAllDisplays();
                clearTextBoxes();
                setIconsToInvisible();
                // STRING EX ---> complex_grid1x2 : icon1 : track_name1 : end_string
                String[] messageIds = message.split(":");
                // Set first icon visible
                setIconsVisible("NULL", messageIds[1], "NULL");
                complex2x2text.setText(messageIds[2]);
                complex3x2grid.setVisibility(View.VISIBLE);
            }
        });
    }

    public void menuVolumeProgressBar(final String message){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                closeAllDisplays();
                // string ex: menu_volume : function_name : value : end_string
                String[] messageIds = message.split(":");
                functionName.setText(messageIds[1]);
                currentValue.setText(messageIds[2]);
                menuVolumeProgressBar.setProgress(Integer.parseInt(messageIds[2]));
                settigsMenuProgressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    private void menuMusicalAtmosphere(String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int bassVolume;
                int trebleVolume;
                closeAllDisplays();
                bassTrebleLayout.setVisibility(View.INVISIBLE);
                bassTextCard.setCardBackgroundColor(getResources().getColor(R.color.sourceMainBackground));
                trebleTextCard.setCardBackgroundColor(getResources().getColor(R.color.sourceMainBackground));
                // String ex: musical_atmosphere : 2 : off : on : _ : title_1 : title_2 : _ : + : 5 : - : 2 : end_string
                String[] messageIds = message.split(":");
                if (messageIds[1].toLowerCase().contains("1")){
                    //set other CardView to main colors
                    bassTextCard.setCardBackgroundColor(getResources().getColor(R.color.sourceMainBackground));
                    trebleTextCard.setCardBackgroundColor(getResources().getColor(R.color.sourceMainBackground));
                    //set main CardView to select color
                    cardRow2.setCardBackgroundColor(getResources().getColor(R.color.sourceSelected));
                    cardText2.setCardBackgroundColor(getResources().getColor(R.color.sourceSelected));

                }
                if (messageIds[1].toLowerCase().contains("2")){
                    //set other CardView to main colors
                    cardRow2.setCardBackgroundColor(getResources().getColor(R.color.sourceMainBackground));
                    cardText2.setCardBackgroundColor(getResources().getColor(R.color.sourceMainBackground));
                    trebleTextCard.setCardBackgroundColor(getResources().getColor(R.color.sourceMainBackground));
                    //set main CardView to select color
                    bassTextCard.setCardBackgroundColor(getResources().getColor(R.color.sourceSelected));
                }
                if (messageIds[1].toLowerCase().contains("3")){
                    //set other CardView to main colors
                    cardRow2.setCardBackgroundColor(getResources().getColor(R.color.sourceMainBackground));
                    cardText2.setCardBackgroundColor(getResources().getColor(R.color.sourceMainBackground));
                    bassTextCard.setCardBackgroundColor(getResources().getColor(R.color.sourceMainBackground));
                    //set main CardView to select color
                    trebleTextCard.setCardBackgroundColor(getResources().getColor(R.color.sourceSelected));
                }

                if (messageIds[2].length() == 0){
                    emptyCercleRow1.setVisibility(View.INVISIBLE);
                    checkedCercleRow1.setVisibility(View.INVISIBLE);
                }
                if (messageIds[2].toLowerCase().contains("off")){
                    emptyCercleRow1.setVisibility(View.VISIBLE);
                    checkedCercleRow1.setVisibility(View.INVISIBLE);
                }
                if (messageIds[2].toLowerCase().contains("on")){
                    emptyCercleRow1.setVisibility(View.INVISIBLE);
                    checkedCercleRow1.setVisibility(View.VISIBLE);
                }

                if (messageIds[3].length() == 0){
                    emptyCercleRow2.setVisibility(View.INVISIBLE);
                    checkedCercleRow2.setVisibility(View.INVISIBLE);
                }
                if (messageIds[3].toLowerCase().contains("off")){
                    emptyCercleRow2.setVisibility(View.VISIBLE);
                    checkedCercleRow2.setVisibility(View.INVISIBLE);
                }
                if (messageIds[3].toLowerCase().contains("on")){
                    emptyCercleRow2.setVisibility(View.INVISIBLE);
                    checkedCercleRow2.setVisibility(View.VISIBLE);
                }

                if (messageIds[4].length() == 0){
                    emptyCercleRow3.setVisibility(View.INVISIBLE);
                    checkedCercleRow3.setVisibility(View.INVISIBLE);
                }
                if (messageIds[4].toLowerCase().contains("off")){
                    emptyCercleRow3.setVisibility(View.VISIBLE);
                    checkedCercleRow3.setVisibility(View.INVISIBLE);
                }
                if (messageIds[4].toLowerCase().contains("on")){
                    emptyCercleRow3.setVisibility(View.INVISIBLE);
                    checkedCercleRow3.setVisibility(View.VISIBLE);
                }

                musicalAtmosphereMainText1.setText(messageIds[5]);
                musicalAtmosphereMainText2.setText(messageIds[6]);
                musicalAtmosphereMainText3.setText(messageIds[7]);

                if (messageIds[6].toLowerCase().contains("bass")){
                    bassTrebleLayout.setVisibility(View.VISIBLE);
                }

                if (messageIds[8] == "+"){
                    bassVolume = Integer.parseInt(messageIds[9]) + 10;
                    bassProgressBar.setProgress(bassVolume);
                }else if (messageIds[8] == "-"){
                    bassVolume = Integer.parseInt(messageIds[9]);
                    bassProgressBar.setProgress(bassVolume);
                }

                if (messageIds[10] == "+"){
                    trebleVolume = Integer.parseInt(messageIds[11]) + 10;
                    trebleProgressBar.setProgress(trebleVolume);
                }else if (messageIds[10] == "-"){
                    trebleVolume = Integer.parseInt(messageIds[11]);
                    trebleProgressBar.setProgress(trebleVolume);
                }

                musicalAtmosphere.setVisibility(View.VISIBLE);
            }
        });
    }

    public void confirm_cancel(String message){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                closeAllDisplays();
                clearTextBoxes();
                // string ex: confirm_cancel_function : Cancel : Confirm : : Do you want to reset : these parameters? : end_string
                String[] messageIds = message.split(":");
                //Set text
                grid4text1x1.setText(messageIds[1]);
                grid4text2x1.setText(messageIds[2]);
                grid4text3x1.setText(messageIds[3]);
                grid4text1x2.append(messageIds[4] + "\r\n" + messageIds[5] + "\r\n" + messageIds[6]);
                //----------------------------------
                grid4.setVisibility(View.VISIBLE);
            }
        });
    }

    public void informationBox(String message){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                closeAllDisplays();
                infoGridText.setText("");
                //String ex   --------------------------------------
                String[] messageIds = message.split(":");
                //Set
                infoGrid.setVisibility(View.VISIBLE);
            }
        });
    }

    public void closeAllDisplays(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                radio3x4grid.setVisibility(View.INVISIBLE);
                radio3x3grid.setVisibility(View.INVISIBLE);
                complex3x2grid.setVisibility(View.INVISIBLE);
                settigsMenuProgressBar.setVisibility(View.INVISIBLE);
                musicalAtmosphere.setVisibility(View.INVISIBLE);
                grid4.setVisibility(View.INVISIBLE);
                infoGrid.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void clearTextBoxes(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                radio2x1text.setText("");
                radio1x2text.setText("");
                radio1x3text.setText("");
                radio1x4text.setText("");
                radio2x2text.setText("");
                radio2x3text.setText("");
                radio2x4text.setText("");
                radio3x2text.setText("");
                radio3x3text.setText("");
                radio3x4text.setText("");

                complex1x2text.setText("");
                complex2x2text.setText("");
                complex3x2text.setText("");

                grid4text1x1.setText("");
                grid4text2x1.setText("");
                grid4text3x1.setText("");
                grid4text1x2.setText("");

                radio3x3grid1x1text.setText("");
                radio3x3grid1x2text.setText("");
                radio3x3grid1x3text.setText("");
                radio3x3grid2x1text.setText("");
                radio3x3grid2x2text.setText("");
                radio3x3grid2x3text.setText("");
                radio3x3grid3x1text.setText("");
                radio3x3grid3x2text.setText("");
                radio3x3grid3x3text.setText("");
            }
        });
    }

    public void setIconsToInvisible(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                complex1x1text.setVisibility(View.INVISIBLE);
                complex2x1text.setVisibility(View.INVISIBLE);
                complex3x1text.setVisibility(View.INVISIBLE);

                iconAudioSettings1x1.setVisibility(View.INVISIBLE);
                iconAudioSettings2x1.setVisibility(View.INVISIBLE);
                iconAudioSettings3x1.setVisibility(View.INVISIBLE);

                iconPhoneSettings1x1.setVisibility(View.INVISIBLE);
                iconPhoneSettings2x1.setVisibility(View.INVISIBLE);
                iconPhoneSettings3x1.setVisibility(View.INVISIBLE);

                iconSystemSettings1x1.setVisibility(View.INVISIBLE);
                iconSystemSettings2x1.setVisibility(View.INVISIBLE);
                iconSystemSettings3x1.setVisibility(View.INVISIBLE);

                iconBluetooth1x1.setVisibility(View.INVISIBLE);
                iconBluetooth2x1.setVisibility(View.INVISIBLE);
                iconBluetooth3x1.setVisibility(View.INVISIBLE);

                iconLanguage1x1.setVisibility(View.INVISIBLE);
                iconLanguage2x1.setVisibility(View.INVISIBLE);
                iconLanguage3x1.setVisibility(View.INVISIBLE);

                iconSoundOptimisation1x1.setVisibility(View.INVISIBLE);
                iconSoundOptimisation2x1.setVisibility(View.INVISIBLE);
                iconSoundOptimisation3x1.setVisibility(View.INVISIBLE);

                iconMusicalAtmosphere1x1.setVisibility(View.INVISIBLE);
                iconMusicalAtmosphere2x1.setVisibility(View.INVISIBLE);
                iconMusicalAtmosphere3x1.setVisibility(View.INVISIBLE);

                iconDisplayCDtime1x1.setVisibility(View.INVISIBLE);
                iconDisplayCDtime2x1.setVisibility(View.INVISIBLE);
                iconDisplayCDtime3x1.setVisibility(View.INVISIBLE);

                iconRadioFunctions1x1.setVisibility(View.INVISIBLE);
                iconRadioFunctions2x1.setVisibility(View.INVISIBLE);
                iconRadioFunctions3x1.setVisibility(View.INVISIBLE);

                iconSeriousClassics1x1.setVisibility(View.INVISIBLE);
                iconSeriousClassics2x1.setVisibility(View.INVISIBLE);
                iconSeriousClassics3x1.setVisibility(View.INVISIBLE);

                iconVariedSpeech1x1.setVisibility(View.INVISIBLE);
                iconVariedSpeech2x1.setVisibility(View.INVISIBLE);
                iconVariedSpeech3x1.setVisibility(View.INVISIBLE);

                iconNews1x1.setVisibility(View.INVISIBLE);
                iconNews2x1.setVisibility(View.INVISIBLE);
                iconNews3x1.setVisibility(View.INVISIBLE);

                iconSport1x1.setVisibility(View.INVISIBLE);
                iconSport2x1.setVisibility(View.INVISIBLE);
                iconSport3x1.setVisibility(View.INVISIBLE);

                iconPopMusic1x1.setVisibility(View.INVISIBLE);
                iconPopMusic2x1.setVisibility(View.INVISIBLE);
                iconPopMusic3x1.setVisibility(View.INVISIBLE);

                iconPairPhone1x1.setVisibility(View.INVISIBLE);
                iconPairPhone2x1.setVisibility(View.INVISIBLE);
                iconPairPhone3x1.setVisibility(View.INVISIBLE);

                iconMusicNote1x1.setVisibility(View.INVISIBLE);
                iconMusicNote2x1.setVisibility(View.INVISIBLE);
                iconMusicNote3x1.setVisibility(View.INVISIBLE);

                iconPhonebook1x1.setVisibility(View.INVISIBLE);
                iconPhonebook2x1.setVisibility(View.INVISIBLE);
                iconPhonebook3x1.setVisibility(View.INVISIBLE);

                iconCallHistory1x1.setVisibility(View.INVISIBLE);
                iconCallHistory2x1.setVisibility(View.INVISIBLE);
                iconCallHistory3x1.setVisibility(View.INVISIBLE);

                iconMailbox1x1.setVisibility(View.INVISIBLE);
                iconMailbox2x1.setVisibility(View.INVISIBLE);
                iconMailbox3x1.setVisibility(View.INVISIBLE);

                iconDirectoryManagement1x1.setVisibility(View.INVISIBLE);
                iconDirectoryManagement2x1.setVisibility(View.INVISIBLE);
                iconDirectoryManagement3x1.setVisibility(View.INVISIBLE);

                iconOtherAudioSettings1x1.setVisibility(View.INVISIBLE);
                iconOtherAudioSettings2x1.setVisibility(View.INVISIBLE);
                iconOtherAudioSettings3x1.setVisibility(View.INVISIBLE);

                iconMissedCalls1x1.setVisibility(View.INVISIBLE);
                iconMissedCalls2x1.setVisibility(View.INVISIBLE);
                iconMissedCalls3x1.setVisibility(View.INVISIBLE);

                iconReceivedCalls1x1.setVisibility(View.INVISIBLE);
                iconReceivedCalls2x1.setVisibility(View.INVISIBLE);
                iconReceivedCalls3x1.setVisibility(View.INVISIBLE);

                iconDialedNumbers1x1.setVisibility(View.INVISIBLE);
                iconDialedNumbers2x1.setVisibility(View.INVISIBLE);
                iconDialedNumbers3x1.setVisibility(View.INVISIBLE);

                iconPutOnHold1x1.setVisibility(View.INVISIBLE);
                iconPutOnHold2x1.setVisibility(View.INVISIBLE);
                iconPutOnHold3x1.setVisibility(View.INVISIBLE);

                iconUSB1x1.setVisibility(View.INVISIBLE);
                iconUSB2x1.setVisibility(View.INVISIBLE);
                iconUSB3x1.setVisibility(View.INVISIBLE);

                iconVolume1x1.setVisibility(View.INVISIBLE);
                iconVolume2x1.setVisibility(View.INVISIBLE);
                iconVolume3x1.setVisibility(View.INVISIBLE);

                iconRingtone1x1.setVisibility(View.INVISIBLE);
                iconRingtone2x1.setVisibility(View.INVISIBLE);
                iconRingtone3x1.setVisibility(View.INVISIBLE);

                iconCercle1x1empty.setVisibility(View.INVISIBLE);
                iconCercle2x1empty.setVisibility(View.INVISIBLE);
                iconCercle3x1empty.setVisibility(View.INVISIBLE);

                iconCercle1x1full.setVisibility(View.INVISIBLE);
                iconCercle2x1full.setVisibility(View.INVISIBLE);
                iconCercle3x1full.setVisibility(View.INVISIBLE);

                iconCheckedBox1x1.setVisibility(View.INVISIBLE);
                iconCheckedBox2x1.setVisibility(View.INVISIBLE);
                iconCheckedBox3x1.setVisibility(View.INVISIBLE);

                iconUncheckedBox1x1.setVisibility(View.INVISIBLE);
                iconUncheckedBox2x1.setVisibility(View.INVISIBLE);
                iconUncheckedBox3x1.setVisibility(View.INVISIBLE);

                iconFolder1x1.setVisibility(View.INVISIBLE);
                iconFolder2x1.setVisibility(View.INVISIBLE);
                iconFolder3x1.setVisibility(View.INVISIBLE);

                iconVoicePromptVolume1x1.setVisibility(View.INVISIBLE);
                iconVoicePromptVolume2x1.setVisibility(View.INVISIBLE);
                iconVoicePromptVolume3x1.setVisibility(View.INVISIBLE);

                iconNumber_1_1x1.setVisibility(View.INVISIBLE);
                iconNumber_1_2x1.setVisibility(View.INVISIBLE);
                iconNumber_1_3x1.setVisibility(View.INVISIBLE);

                iconNumber_2_1x1.setVisibility(View.INVISIBLE);
                iconNumber_2_2x1.setVisibility(View.INVISIBLE);
                iconNumber_2_3x1.setVisibility(View.INVISIBLE);

                iconNumber_3_1x1.setVisibility(View.INVISIBLE);
                iconNumber_3_2x1.setVisibility(View.INVISIBLE);
                iconNumber_3_3x1.setVisibility(View.INVISIBLE);

                iconNumber_4_1x1.setVisibility(View.INVISIBLE);
                iconNumber_4_2x1.setVisibility(View.INVISIBLE);
                iconNumber_4_3x1.setVisibility(View.INVISIBLE);

                iconNumber_5_1x1.setVisibility(View.INVISIBLE);
                iconNumber_5_2x1.setVisibility(View.INVISIBLE);
                iconNumber_5_3x1.setVisibility(View.INVISIBLE);

                iconAdaptationVolume1x1.setVisibility(View.INVISIBLE);
                iconAdaptationVolume2x1.setVisibility(View.INVISIBLE);
                iconAdaptationVolume3x1.setVisibility(View.INVISIBLE);

                iconEmergency1x1.setVisibility(View.INVISIBLE);
                iconEmergency2x1.setVisibility(View.INVISIBLE);
                iconEmergency3x1.setVisibility(View.INVISIBLE);

                iconFolder1x1.setVisibility(View.INVISIBLE);
                iconFolder2x1.setVisibility(View.INVISIBLE);
                iconFolder3x1.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void setIconsVisible(final String icon1, String icon2, String icon3){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!icon1.contains("icon")){
                    complex1x1text.setText(icon1);
                    complex1x1text.setVisibility(View.VISIBLE);
                }
                if (icon1.contains("icon_Audio_settings")) iconAudioSettings1x1.setVisibility(View.VISIBLE);
                if (icon1.contains("icon_Phone_settings")) iconPhoneSettings1x1.setVisibility(View.VISIBLE);
                if (icon1.contains("icon_System_settings")) iconSystemSettings1x1.setVisibility(View.VISIBLE);
                if (icon1.contains("icon_Bluetooth")) iconBluetooth1x1.setVisibility(View.VISIBLE);
                if (icon1.contains("icon_Language")) iconLanguage1x1.setVisibility(View.VISIBLE);
                if (icon1.contains("icon_Sound_optimization")) iconSoundOptimisation1x1.setVisibility(View.VISIBLE);
                if (icon1.contains("icon_Musical_atmosphere")) iconMusicalAtmosphere1x1.setVisibility(View.VISIBLE);
                if (icon1.contains("icon_Display_CD_time")) iconDisplayCDtime1x1.setVisibility(View.VISIBLE);
                if (icon1.contains("icon_Radio_functions")) iconRadioFunctions1x1.setVisibility(View.VISIBLE);
                if (icon1.contains("icon_Serious_classics")) iconSeriousClassics1x1.setVisibility(View.VISIBLE);
                if (icon1.contains("icon_Varied_speech")) iconVariedSpeech1x1.setVisibility(View.VISIBLE);
                if (icon1.contains("icon_News")) iconNews1x1.setVisibility(View.VISIBLE);
                if (icon1.contains("icon_Sport")) iconSport1x1.setVisibility(View.VISIBLE);
                if (icon1.contains("icon_Pop_music")) iconPopMusic1x1.setVisibility(View.VISIBLE);
                if (icon1.contains("icon_Pair_phone")) iconPairPhone1x1.setVisibility(View.VISIBLE);
                if (icon1.contains("icon_Music_note")) iconMusicNote1x1.setVisibility(View.VISIBLE);
                if (icon1.contains("icon_Vehicle_phonebook")) iconPhonebook1x1.setVisibility(View.VISIBLE);
                if (icon1.contains("icon_Mobile_phonebook")) iconPhonebook1x1.setVisibility(View.VISIBLE);
                if (icon1.contains("icon_Call_history")) iconCallHistory1x1.setVisibility(View.VISIBLE);
                if (icon1.contains("icon_Mail_box")) iconMailbox1x1.setVisibility(View.VISIBLE);
                if (icon1.contains("icon_Directory_management")) iconDirectoryManagement1x1.setVisibility(View.VISIBLE);
                if (icon1.contains("icon_Other_audio_settings")) iconOtherAudioSettings1x1.setVisibility(View.VISIBLE);
                if (icon1.contains("icon_Missed_calls")) iconMissedCalls1x1.setVisibility(View.VISIBLE);
                if (icon1.contains("icon_Received_calls")) iconReceivedCalls1x1.setVisibility(View.VISIBLE);
                if (icon1.contains("icon_Dialed_numbers")) iconDialedNumbers1x1.setVisibility(View.VISIBLE);
                if (icon1.contains("icon_Put_on_hold")) iconPutOnHold1x1.setVisibility(View.VISIBLE);
                if (icon1.contains("icon_USB")) iconUSB1x1.setVisibility(View.VISIBLE);
                if (icon1.contains("icon_Volume")) iconVolume1x1.setVisibility(View.VISIBLE);
                if (icon1.contains("icon_Ringtone")) iconRingtone1x1.setVisibility(View.VISIBLE);
                if (icon1.contains("icon_Cercle_empty")) iconCercle1x1empty.setVisibility(View.VISIBLE);
                if (icon1.contains("icon_Cercle_full")) iconCercle1x1full.setVisibility(View.VISIBLE);
                if (icon1.contains("icon_Checked_box")) iconCheckedBox1x1.setVisibility(View.VISIBLE);
                if (icon1.contains("icon_Unchecked_box")) iconUncheckedBox1x1.setVisibility(View.VISIBLE);
                if (icon1.contains("icon_Folder")) iconFolder1x1.setVisibility(View.VISIBLE);
                if (icon1.contains("icon_Voice_prompt_volume")) iconVoicePromptVolume1x1.setVisibility(View.VISIBLE);
                if (icon1.contains("icon_1")) iconNumber_1_1x1.setVisibility(View.VISIBLE);
                if (icon1.contains("icon_2")) iconNumber_2_1x1.setVisibility(View.VISIBLE);
                if (icon1.contains("icon_3")) iconNumber_3_1x1.setVisibility(View.VISIBLE);
                if (icon1.contains("icon_4")) iconNumber_4_1x1.setVisibility(View.VISIBLE);
                if (icon1.contains("icon_5")) iconNumber_5_1x1.setVisibility(View.VISIBLE);
                if (icon1.contains("icon_Adaptation_volume")) iconAdaptationVolume1x1.setVisibility(View.VISIBLE);
                if (icon1.contains("icon_Emergency")) iconEmergency1x1.setVisibility(View.VISIBLE);
                //-------------------------------------------------------------------------------------
                if (!icon2.contains("icon")) {
                    complex2x1text.setText(icon2);
                    complex2x1text.setVisibility(View.VISIBLE);
                }
                if (icon2.contains("icon_Audio_settings")) iconAudioSettings2x1.setVisibility(View.VISIBLE);
                if (icon2.contains("icon_Phone_settings")) iconPhoneSettings2x1.setVisibility(View.VISIBLE);
                if (icon2.contains("icon_System_settings")) iconSystemSettings2x1.setVisibility(View.VISIBLE);
                if (icon2.contains("icon_Bluetooth")) iconBluetooth2x1.setVisibility(View.VISIBLE);
                if (icon2.contains("icon_Language")) iconLanguage2x1.setVisibility(View.VISIBLE);
                if (icon2.contains("icon_Sound_optimization")) iconSoundOptimisation2x1.setVisibility(View.VISIBLE);
                if (icon2.contains("icon_Musical_atmosphere")) iconMusicalAtmosphere2x1.setVisibility(View.VISIBLE);
                if (icon2.contains("icon_Display_CD_time")) iconDisplayCDtime2x1.setVisibility(View.VISIBLE);
                if (icon2.contains("icon_Radio_functions")) iconRadioFunctions2x1.setVisibility(View.VISIBLE);
                if (icon2.contains("icon_Serious_classics")) iconSeriousClassics2x1.setVisibility(View.VISIBLE);
                if (icon2.contains("icon_Varied_speech")) iconVariedSpeech2x1.setVisibility(View.VISIBLE);
                if (icon2.contains("icon_News")) iconNews2x1.setVisibility(View.VISIBLE);
                if (icon2.contains("icon_Sport")) iconSport2x1.setVisibility(View.VISIBLE);
                if (icon2.contains("icon_Pop_music")) iconPopMusic2x1.setVisibility(View.VISIBLE);
                if (icon2.contains("icon_Pair_phone")) iconPairPhone2x1.setVisibility(View.VISIBLE);
                if (icon2.contains("icon_Music_note")) iconMusicNote2x1.setVisibility(View.VISIBLE);
                if (icon2.contains("icon_Vehicle_phonebook")) iconPhonebook2x1.setVisibility(View.VISIBLE);
                if (icon2.contains("icon_Mobile_phonebook")) iconPhonebook2x1.setVisibility(View.VISIBLE);
                if (icon2.contains("icon_Call_history")) iconCallHistory2x1.setVisibility(View.VISIBLE);
                if (icon2.contains("icon_Mail_box")) iconMailbox2x1.setVisibility(View.VISIBLE);
                if (icon2.contains("icon_Directory_management")) iconDirectoryManagement2x1.setVisibility(View.VISIBLE);
                if (icon2.contains("icon_Other_audio_settings")) iconOtherAudioSettings2x1.setVisibility(View.VISIBLE);
                if (icon2.contains("icon_Missed_calls")) iconMissedCalls2x1.setVisibility(View.VISIBLE);
                if (icon2.contains("icon_Received_calls")) iconReceivedCalls2x1.setVisibility(View.VISIBLE);
                if (icon2.contains("icon_Dialed_numbers")) iconDialedNumbers2x1.setVisibility(View.VISIBLE);
                if (icon2.contains("icon_Put_on_hold")) iconPutOnHold2x1.setVisibility(View.VISIBLE);
                if (icon2.contains("icon_USB")) iconUSB2x1.setVisibility(View.VISIBLE);
                if (icon2.contains("icon_Volume")) iconVolume2x1.setVisibility(View.VISIBLE);
                if (icon2.contains("icon_Ringtone")) iconRingtone2x1.setVisibility(View.VISIBLE);
                if (icon2.contains("icon_Cercle_empty")) iconCercle2x1empty.setVisibility(View.VISIBLE);
                if (icon2.contains("icon_Cercle_full")) iconCercle2x1full.setVisibility(View.VISIBLE);
                if (icon2.contains("icon_Checked_box")) iconCheckedBox2x1.setVisibility(View.VISIBLE);
                if (icon2.contains("icon_Unchecked_box")) iconUncheckedBox2x1.setVisibility(View.VISIBLE);
                if (icon2.contains("icon_Folder")) iconFolder2x1.setVisibility(View.VISIBLE);
                if (icon2.contains("icon_Voice_prompt_volume")) iconVoicePromptVolume2x1.setVisibility(View.VISIBLE);
                if (icon2.contains("icon_1")) iconNumber_1_2x1.setVisibility(View.VISIBLE);
                if (icon2.contains("icon_2")) iconNumber_2_2x1.setVisibility(View.VISIBLE);
                if (icon2.contains("icon_3")) iconNumber_3_2x1.setVisibility(View.VISIBLE);
                if (icon2.contains("icon_4")) iconNumber_4_2x1.setVisibility(View.VISIBLE);
                if (icon2.contains("icon_5")) iconNumber_5_2x1.setVisibility(View.VISIBLE);
                if (icon2.contains("icon_Adaptation_volume")) iconAdaptationVolume2x1.setVisibility(View.VISIBLE);
                if (icon2.contains("icon_Emergency")) iconEmergency2x1.setVisibility(View.VISIBLE);
                //-------------------------------------------------------------------------------------
                if (!icon3.contains("icon")) {
                    complex3x1text.setText(icon3);
                    complex3x1text.setVisibility(View.INVISIBLE);
                }
                if (icon3.contains("icon_Audio_settings")) iconAudioSettings3x1.setVisibility(View.VISIBLE);
                if (icon3.contains("icon_Phone_settings")) iconPhoneSettings3x1.setVisibility(View.VISIBLE);
                if (icon3.contains("icon_System_settings")) iconSystemSettings3x1.setVisibility(View.VISIBLE);
                if (icon3.contains("icon_Bluetooth")) iconBluetooth3x1.setVisibility(View.VISIBLE);
                if (icon3.contains("icon_Language")) iconLanguage3x1.setVisibility(View.VISIBLE);
                if (icon3.contains("icon_Sound_optimization")) iconSoundOptimisation3x1.setVisibility(View.VISIBLE);
                if (icon3.contains("icon_Musical_atmosphere")) iconMusicalAtmosphere3x1.setVisibility(View.VISIBLE);
                if (icon3.contains("icon_Display_CD_time")) iconDisplayCDtime3x1.setVisibility(View.VISIBLE);
                if (icon3.contains("icon_Radio_functions")) iconRadioFunctions3x1.setVisibility(View.VISIBLE);
                if (icon3.contains("icon_Serious_classics")) iconSeriousClassics3x1.setVisibility(View.VISIBLE);
                if (icon3.contains("icon_Varied_speech")) iconVariedSpeech3x1.setVisibility(View.VISIBLE);
                if (icon3.contains("icon_News")) iconNews3x1.setVisibility(View.VISIBLE);
                if (icon3.contains("icon_Sport")) iconSport3x1.setVisibility(View.VISIBLE);
                if (icon3.contains("icon_Pop_music")) iconPopMusic3x1.setVisibility(View.VISIBLE);
                if (icon3.contains("icon_Pair_phone")) iconPairPhone3x1.setVisibility(View.VISIBLE);
                if (icon3.contains("icon_Music_note")) iconMusicNote3x1.setVisibility(View.VISIBLE);
                if (icon3.contains("icon_Vehicle_phonebook")) iconPhonebook3x1.setVisibility(View.VISIBLE);
                if (icon3.contains("icon_Mobile_phonebook")) iconPhonebook3x1.setVisibility(View.VISIBLE);
                if (icon3.contains("icon_Call_history")) iconCallHistory3x1.setVisibility(View.VISIBLE);
                if (icon3.contains("icon_Mail_box")) iconMailbox3x1.setVisibility(View.VISIBLE);
                if (icon3.contains("icon_Directory_management")) iconDirectoryManagement3x1.setVisibility(View.VISIBLE);
                if (icon3.contains("icon_Other_audio_settings")) iconOtherAudioSettings3x1.setVisibility(View.VISIBLE);
                if (icon3.contains("icon_Missed_calls")) iconMissedCalls3x1.setVisibility(View.VISIBLE);
                if (icon3.contains("icon_Received_calls")) iconReceivedCalls3x1.setVisibility(View.VISIBLE);
                if (icon3.contains("icon_Dialed_numbers")) iconDialedNumbers3x1.setVisibility(View.VISIBLE);
                if (icon3.contains("icon_Put_on_hold")) iconPutOnHold3x1.setVisibility(View.VISIBLE);
                if (icon3.contains("icon_USB")) iconUSB3x1.setVisibility(View.VISIBLE);
                if (icon3.contains("icon_Volume")) iconVolume3x1.setVisibility(View.VISIBLE);
                if (icon3.contains("icon_Ringtone")) iconRingtone3x1.setVisibility(View.VISIBLE);
                if (icon3.contains("icon_Cercle_empty")) iconCercle3x1empty.setVisibility(View.VISIBLE);
                if (icon3.contains("icon_Cercle_full")) iconCercle3x1full.setVisibility(View.VISIBLE);
                if (icon3.contains("icon_Checked_box")) iconCheckedBox3x1.setVisibility(View.VISIBLE);
                if (icon3.contains("icon_Unchecked_box")) iconUncheckedBox3x1.setVisibility(View.VISIBLE);
                if (icon3.contains("icon_Folder")) iconFolder3x1.setVisibility(View.VISIBLE);
                if (icon3.contains("icon_Voice_prompt_volume")) iconVoicePromptVolume3x1.setVisibility(View.VISIBLE);
                if (icon3.contains("icon_1")) iconNumber_1_3x1.setVisibility(View.VISIBLE);
                if (icon3.contains("icon_2")) iconNumber_2_3x1.setVisibility(View.VISIBLE);
                if (icon3.contains("icon_3")) iconNumber_3_3x1.setVisibility(View.VISIBLE);
                if (icon3.contains("icon_4")) iconNumber_4_3x1.setVisibility(View.VISIBLE);
                if (icon3.contains("icon_5")) iconNumber_5_3x1.setVisibility(View.VISIBLE);
                if (icon3.contains("icon_Adaptation_volume")) iconAdaptationVolume3x1.setVisibility(View.VISIBLE);
                if (icon3.contains("icon_Emergency")) iconEmergency3x1.setVisibility(View.VISIBLE);
            }
        });

    }

    public void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

}