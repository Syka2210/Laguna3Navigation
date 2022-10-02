package com.example.multimediainterfacev12;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.helper.widget.Layer;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.usb.UsbDevice;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.RecognizerIntent;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.github.pwittchen.weathericonview.WeatherIconView;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import me.aflak.arduino.Arduino;
import me.aflak.arduino.ArduinoListener;

public class MainActivity extends AppCompatActivity implements ArduinoListener {
    private static final String TAG = "MainActivity";
    public int selectedPopUpApp = 1;
    public int popUpMenuShown = 0;
    public String volumeLast = "-";
    public String iconID;
    private Arduino arduino;

    /**
     * Initialize log file
     */
    //private static final String LOG_FILE = "dataReceiveLog.txt";
    private static final String LOG_FILE = new SimpleDateFormat("yyyyMMddHHmm'.txt'").format(new Date());
    /**
     * Coordinates with timer handler
     */
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Coordinates coordinates;

    /**
     * Initialize weather
     */
    final WeatherDataService weatherDataService = new WeatherDataService(MainActivity.this);
    int REQUEST_CODE_LOCATION_PERMISSION = 1;
    TextView weather_cityName;
    TextView weather_mainTemp;
    WeatherIconView weather_icon;
    TextView weather_description;
    TextView weather_tempMin;
    TextView weather_tempMax;
    double latitude = 0;
    double longitude = 0;

    /**
     * Debug box
     */
    private TextView debugTextbox;

    /**
     * App selection pop up
     */
    Dialog appSelectPopUp;
    private CardView googleMapsCard;
    private CardView wazeCard;
    private CardView spotifyCard;
    TextView app_select_tv;
    String destination;
    String popUpMenuAppSelected;
    private static final int RECOGNIZER_RESULT = 1;
    LinearLayout appSelectionLayout;
    CardView gMapsCard;
    CardView layout_wazeCard;
    CardView layout_spotifyCard;

    /**
     *Declaring the volume layout and the volume text box
     */
    private TextView volume;
    /**
     *Declaring the Radio Layout and the textviews
     */
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

    /**
     *Declaring the 3x3 grid Layout
     */
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

    /**
     * Declaring the 3x2 menu Layout
     */
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

    /**
     *Declaring the volume settings Layout
     */
    private LinearLayout settigsMenuProgressBar;
    private ProgressBar menuVolumeProgressBar;
    private TextView functionName;
    private TextView currentValue;

    /**
     * Declaring the musical atmosphere menu Layout
     */
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

    /**
     * Declaring the Source Tab Layout
     */
    CardView radioCard;
    CardView cdplayerCard;
    CardView auxCard;

    /**
     * Declaring the grid 4 Layout
     */
    private LinearLayout grid4;
    private TextView grid4text1x1;
    private TextView grid4text2x1;
    private TextView grid4text3x1;
    private TextView grid4text1x2;

    /**
     *Declaring the Information one box Layout
     */
    private LinearLayout infoGrid;
    private TextView infoGridText;

    String messageReceived = "";


    /**
     * METHODS START
     */


    @SuppressLint("WrongViewCast")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
/**
 * Set the date bellow the digital clock
 * TODO: BEWARE!!! Check for regular update of the date, or at midnight it will not change. Maybe implement it with the weather refresh?
 */
        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance().format(calendar.getTime());
        TextView textDate = findViewById(R.id.textDate);
        textDate.setText(currentDate);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        arduino = new Arduino(this, 250000);
        arduino.addVendorId(11914); // Use 11914 for the Raspberry Pi Pico board or 9025 for the Arduino boards
        arduino.addVendorId(9025);

        /**
         *Coordinates
         */
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        /**
         *Weather items
         */
        weather_cityName = findViewById(R.id.weather_cityName);
        weather_mainTemp = findViewById(R.id.weather_mainTemp);
        weather_icon = findViewById(R.id.weather_icon);
        weather_description = findViewById(R.id.weather_description);
        weather_tempMax = findViewById(R.id.weather_tempMax);
        weather_tempMin = findViewById(R.id.weather_tempMin);

        /**
         * Start timed weather update
         */
        //updateWeather();
        timedRefreshWeather.run();


        /**
         * Debug box
         */
        debugTextbox = findViewById(R.id.debugText);
        debugTextbox.setMovementMethod(new ScrollingMovementMethod());

        /**
         * App selection pop-up
         */
//        appSelectPopUp = new Dialog(this);
//        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
//        @SuppressLint("InflateParams") View vi = inflater.inflate(R.layout.app_selection_menu, null);
//        googleMapsCard = vi.findViewById(R.id.googleMapsCard);
//        wazeCard = vi.findViewById(R.id.wazeCard);
//        spotifyCard = vi.findViewById(R.id.spotifyCard);
//        app_select_tv = vi.findViewById(R.id.app_select_tv);
        appSelectionLayout = findViewById(R.id.appSelectionLayout);
        gMapsCard = findViewById(R.id.gMapsCard);
        layout_wazeCard = findViewById(R.id.wazeCard);
        layout_spotifyCard = findViewById(R.id.spotifyCard);

        /**
         * Volume
         */
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
        iconNumber_5_1x1 = findViewById(R.id.complex1x1number_5);
        iconNumber_5_2x1 = findViewById(R.id.complex2x1number_5);
        iconNumber_5_3x1 = findViewById(R.id.complex3x1number_5);
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
        menuVolumeProgressBar = (ProgressBar) findViewById(R.id.menuVolumeProgressBar);
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
        bassProgressBar = (ProgressBar) findViewById(R.id.bassProgressBar);
        trebleProgressBar = (ProgressBar) findViewById(R.id.trebleProgressBar);


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

        Log.i(TAG, "onCreate");
        logFile("ANDROID: onCreate");
    }


    @Override
    protected void onStart(){
        super.onStart();
        arduino.setArduinoListener((ArduinoListener) this);
        Log.i(TAG, "onStart");
        logFile("ANDROID: onStart");
        /*
        String msg = "reqMsg";
        arduino.send(msg.getBytes());
        Log.i(TAG, msg);

         */
    }
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
        logFile("ANDROID: onResume");

        String msg = "reqMsg";
        arduino.send(msg.getBytes());
        Log.i(TAG, "The folowing message was sent to Arduino:" + msg);
    }
    protected void onPause(){
        super.onPause();
        Log.i(TAG, "onPause");
        logFile("ANDROID: onPause");
    }
    protected void onStop(){
        super.onStop();
        Log.i(TAG, "onStop");
        logFile("ANDROID: onStop");
    }
    protected void onRestart(){
        super.onRestart();
        Log.i(TAG, "onRestart");
        logFile("ANDROID: onRestart");

        String msg = "reqMsg";
        arduino.send(msg.getBytes());
        Log.i(TAG, "The folowing message was sent to Arduino:" + msg);
    }

    @Override
    public void onArduinoAttached(UsbDevice device){
        showToast("Arduino attached!");
        arduino.open(device);
        Log.i(TAG, "Arduino attached");
        logFile("ANDROID: onArduinoAttached");
    }

    @Override
    public void onArduinoDetached() {
        showToast("Arduino detached!");
        arduino.close();
        Log.i(TAG, "Arduino detached");
        logFile("ANDROID: onArduinoDetached");
    }

    @Override
    public void onArduinoMessage(byte[] bytes) {
        String str = new String(bytes);
        messageReceived = messageReceived + str;

        // -- Print the message received on the debug window on the right
        if (messageReceived.contains("end_string")) debugTextbox(messageReceived);

        if (messageReceived.contains("end_string")){
            logFile(messageReceived);
            Log.i(TAG, messageReceived);
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
            else if (messageReceived.toLowerCase().contains("view_41")){
                // string ex:  view_41 : FM : 3 (highlighted box) : 92.90 : KISS FM : 1 : end_string
                radioDisplayShort(messageReceived);
                messageReceived = "";
            }

            //Radio 3x3 grid ---> LONG
            else if (messageReceived.toLowerCase().contains("view_43")){
                // string ex: view_43 : FM : 3 (highlighted box) : 92.90 : 100.00 : 120.20 : KISS FM : PRO FM : DIGI FM : 1 : 2 : 3 : end_string
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
            //////////////////////////RADIO/////////////////////////////


            //////////////////////////MENU 3x2/////////////////////////////
            //Menu 3x2 grid ---> LONG
            else if (messageReceived.toLowerCase().contains("view_c3")){
                // STRING EX ---> view_c3 : icon1 : icon2 : icon3 : track_name1 : track_name2 : track_name3 : end_string
                menuDisplay(messageReceived);
                messageReceived = "";
            }

            //Menu 1x2 grid ---> SHORT
            else if (messageReceived.toLowerCase().contains("view_c1")){
                // STRING EX ---> view_c1 : icon1 : track_name1 : end_string
                menuDisplayShort(messageReceived);
                messageReceived = "";
            }

            //////////////////////////MENU 3x2/////////////////////////////


            ////////////////////VOLUME PROGRESS BAR////////////////////////
            else if (messageReceived.toLowerCase().contains("view_70")){
                // string ex ---> view_70 : function_name : value : end_string
                menuVolumeProgressBar(messageReceived);
                messageReceived = "";
            }
            ////////////////////VOLUME PROGRESS BAR////////////////////////


            //////////////////////MUSICAL ATMOSPHERE//////////////////////
            else if (messageReceived.toLowerCase().contains("view_73")){
                // The musical atmosphere menu
                // String ex: view_73 : 2 (high box) : icon : icon : icon : title_1 : title_2 : title_3 : + : 5 : - : 2 : end_string
                menuMusicalAtmosphere(messageReceived);
                messageReceived = "";
            }
            //////////////////////MUSICAL ATMOSPHERE//////////////////////

            // 3+1 selection
            else if (messageReceived.toLowerCase().contains("view_63")){
                // The confirm_cancel_function
                // string ex: view_63 : Cancel : Confirm : _ : Do you want to reset : these parameters? : end_string
                confirm_cancel(messageReceived);
                messageReceived = "";
            }

            //info box
            else if(messageReceived.toLowerCase().contains("view_52")){
                // The info box
                // string ex: view_52 : Message is received here bla bla : end_string
                informationBox(messageReceived);
                messageReceived = "";
            }

            //KeyPad
            else if(messageReceived.toLowerCase().contains("keypad")){
                String[] messageIds = messageReceived.split(":");
                // string ex: keypad : Menu : end_string
                appSelectionMenu(messageIds[1]);
                messageReceived = "";
            }

            //log string
            else if(messageReceived.toLowerCase().contains("candata")){
                logFile(messageReceived);
                messageReceived = "";
            }


            //CLEAR STRING IF NOT RECOGNIZED
            else {
                //logFile(messageReceived);
                messageReceived = "";
            }

        }

    }

    @Override
    public void onArduinoOpened() {
    }

    @Override
    public void onUsbPermissionDenied() {
        Log.i(TAG, "USB permission denied");
        logFile("ANDROID: onUsbPermissionDenied");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        arduino.unsetArduinoListener();
        arduino.close();
        Log.i(TAG, "onDestroy");
        logFile("ANDROID: onDestroy");
    }

    /**
     * Timed refresh of the weather widget
     */
    private Runnable timedRefreshWeather = new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void run() {
            updateWeather();
            /**
             * repeat the update every 10 minutes
             */
            mHandler.postDelayed(this, 600000);
            logFile("ANDROID: timedRefreshWeather");
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void refreshWeather(View view){
        updateWeather();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void updateWeather(){
        Log.i(TAG, "Weather updated");
        logFile("ANDROID: updateWeather");
        /**
         * Get coordinates
         */
        coordinates = new Coordinates(MainActivity.this);
        if (coordinates.canGetLocation()){
            latitude = coordinates.getLatitude();
            longitude = coordinates.getLongitude();
        } else {
            coordinates.showSettingsAlert();
        }
        if (latitude != 0 && longitude != 0){
            Log.i(TAG, "got latitude and longitude");
            Log.i(TAG, "https://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&appid=63b3859e68a3572bd20b683f5dde6411" + "&units=metric");
            weatherDataService.getWeatherByCoordinates(latitude, longitude, new WeatherDataService.VolleyResponseListener() {
                @Override
                public void onError(String message) {
                    Toast.makeText(MainActivity.this, "something wrong", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(String id, String main, String description, String icon, String temp, String temp_min, String temp_max, String feels_like, String pressure, String humidity, String cityName) {
                    //Toast.makeText(MainActivity.this, "Returned main= " + main + " description=" + description, Toast.LENGTH_SHORT).show();
                    weather_cityName.setText(cityName);

                    if (icon.substring(icon.length() - 1).contains("d")){
                        id = "wi_owm_day_" + id;
                    } else {
                        id = "wi_owm_night_" + id;
                    }
                    String pack = getPackageName();
                    int resId = getResources().getIdentifier(id, "string", pack);
                    if (resId != 0 ){
                        weather_icon.setIconResource(getString(resId));
                        Toast.makeText(MainActivity.this, "Weather updated", Toast.LENGTH_SHORT).show();
                    } else {
                        weather_icon.setVisibility(View.INVISIBLE);
                    }

                    int indexCharacter = temp.indexOf(".");
                    if (indexCharacter != -1){
                        temp = temp.substring(0, indexCharacter);
                        weather_mainTemp.setText(temp + "\u00B0");
                    } else Log.i(TAG, "Temp index = -1!");

                    description = description.substring(0,1).toUpperCase() + description.substring(1).toLowerCase();
                    weather_description.setText(description);

                    indexCharacter = temp_max.indexOf(".");
                    if (indexCharacter != -1){
                        temp_max = temp_max.substring(0, indexCharacter);
                        weather_tempMax.setText("H:" + temp_max + "\u00B0");
                    }
                    indexCharacter = temp_min.indexOf(".");
                    if (indexCharacter != -1){
                        temp_min = temp_min.substring(0, indexCharacter);
                        weather_tempMin.setText("L:" + temp_min + "\u00B0");
                    }

                    latitude = 0;
                    longitude = 0;
                    Log.i(TAG, "Coordinates reset are:" + latitude + " " + longitude);
                }
            });
        }
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

    public void source(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "source");
                logFile("ANDROID: source");
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
                Log.i(TAG, "METHOD - volume");
                logFile("ANDROID: volume");
                // string ex: volume : 22 : end_string
                String[] messageIds = message.split(":");
                if (messageIds[1].toLowerCase().contains("return")){
                    volume.setText(volumeLast);
                    //Log.i(TAG, "Last volume is :" + volumeLast);
                }
                else if (messageIds[1].toLowerCase().contains("pause")){
                    volume.setText(messageIds[1]);
                }
                else if (!messageIds[1].toLowerCase().contains("pause")){
                    volume.setText(messageIds[1]);
                    volumeLast = messageIds[1];
                    //Log.i(TAG, "current value is :" + volumeLast);
                }
            }
        });
    }

    public void highBox(final String message){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "highBox");
                logFile("ANDROID: highBox");
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
                Log.i(TAG, "radioDisplayShort");
                logFile("ANDROID: radioDisplayShort");
                closeAllDisplays();
                clearTextBoxes();
                //Set all card colours to main colour
                radio2x2card.setCardBackgroundColor(getResources().getColor(R.color.sourceMainBackground));
                radio2x3card.setCardBackgroundColor(getResources().getColor(R.color.sourceMainBackground));
                radio2x4card.setCardBackgroundColor(getResources().getColor(R.color.sourceMainBackground));
                // STRING EX -->  view_41 : FM : 3 (highlighted box) : 92.90 : KISS FM : 1 : end_string
                String[] messageIds = message.split(":");
                radio2x1text.setText(messageIds[1]);
                //---------------------------------
                radio2x2text.setText(messageIds[3]);
                radio2x3text.setText(messageIds[4]);
                radio2x4text.setText(messageIds[5]);
                //---------------------------------
                if (messageIds[2].contains("2")) radio2x2card.setCardBackgroundColor(getResources().getColor(R.color.sourceSelected));
                else if (messageIds[2].contains("3")) radio2x3card.setCardBackgroundColor(getResources().getColor(R.color.sourceSelected));
                else if (messageIds[2].contains("4")) radio2x4card.setCardBackgroundColor(getResources().getColor(R.color.sourceSelected));
                //Bring front the Radio layout.
                radio3x4grid.setVisibility(View.VISIBLE);
            }
        });
    }

    public void radioDisplay(final String message){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "radioDisplay");
                logFile("ANDROID: radioDisplay");
                closeAllDisplays();
                clearTextBoxes();
                //Set all card colours to main colour
                radio2x2card.setCardBackgroundColor(getResources().getColor(R.color.sourceMainBackground));
                radio2x3card.setCardBackgroundColor(getResources().getColor(R.color.sourceMainBackground));
                radio2x4card.setCardBackgroundColor(getResources().getColor(R.color.sourceMainBackground));
                // string ex: view_43 : FM : 3 (highlighted box) : 92.90 : 100.00 : 120.20 : KISS FM : PRO FM : DIGI FM : 1 : 2 : 3 : end_string
                String[] messageIds = message.split(":");
                radio2x1text.setText(messageIds[1]);
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
                if (messageIds[2].contains("2")) radio2x2card.setCardBackgroundColor(getResources().getColor(R.color.sourceSelected));
                else if (messageIds[2].contains("3")) radio2x3card.setCardBackgroundColor(getResources().getColor(R.color.sourceSelected));
                else if (messageIds[2].contains("4")) radio2x4card.setCardBackgroundColor(getResources().getColor(R.color.sourceSelected));
                //Bring front the Radio layout.
                radio3x4grid.setVisibility(View.VISIBLE);


            }
        });
    }

    public void frequency3x3Display(final String message){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "frequency3x3Display");
                logFile("ANDROID: frequency3x3Display");
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
                Log.i(TAG, "frequency3x3DisplayShort");
                logFile("ANDROID: frequency3x3DisplayShort");
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

//—————————————————————————————————————————————————————————————————————————————————————————————————————————————————
//  A three row / three columns view, usually with icons in the first column, and text on the second
//  There may be instances where in the second column we have additional icons (mainly in the settings - bluetooth)
//  ||icon||TEXT||
//  ||icon||TEXT||
//  ||icon||TEXT||
//—————————————————————————————————————————————————————————————————————————————————————————————————————————————————
    public void menuDisplay(final String message){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "menuDisplay");
                logFile("ANDROID: menuDisplay");
                closeAllDisplays();
                clearTextBoxes();
                setIconsToInvisible();
                // STRING EX ---> view_c3 : icon1 : icon2 : icon3 : track_name1 : track_name2 : track_name3 : end_string
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

//—————————————————————————————————————————————————————————————————————————————————————————————————————————————————
//  A one row / two columns view, usually with an icon in the first column, and text on the second
//  There may be instances where in the second column we have additional icons (mainly in the settings - bluetooth)
//  ||icon||TEXT||
//—————————————————————————————————————————————————————————————————————————————————————————————————————————————————
    public void menuDisplayShort(final String message){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "menuDisplayShort");
                logFile("ANDROID: menuDisplayShort");
                closeAllDisplays();
                clearTextBoxes();
                setIconsToInvisible();
                // STRING EX ---> view_c1 : icon1 : track_name1 : end_string
                String[] messageIds = message.split(":");
                // Set first icon visible
                setIconsVisible("", messageIds[1], "");
                complex2x2text.setText(messageIds[2]);
                complex3x2grid.setVisibility(View.VISIBLE);
            }
        });
    }

//—————————————————————————————————————————————————————————————————————————————————————————————————————————————————
//  A progress bar that displays the level of volume for different setting of the audio.
//        || Bluetooth volume      || +20 ||
//        ||   ---------------            ||
//—————————————————————————————————————————————————————————————————————————————————————————————————————————————————
    public void menuVolumeProgressBar(final String message){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "menuVolumeProgressBar");
                logFile("ANDROID: menuVolumeProgressBar");
                closeAllDisplays();
                // string ex ---> view_70 : function_name : value : end_string
                String[] messageIds = message.split(":");
                functionName.setText(messageIds[1]);
                currentValue.setText(messageIds[2]);
                menuVolumeProgressBar.setProgress(Integer.parseInt(messageIds[2]));
                settigsMenuProgressBar.setVisibility(View.VISIBLE);
            }
        });
    }

//—————————————————————————————————————————————————————————————————————————————————————————————————————————————————
//  The musical atmosphere menu, which has a selection circle (empty or full), and on the Bass/treble option there
//  are two level indicators for those settings.
//        || ICON ||  "Rock"         ||  --   --
//        || ICON ||  Bass/treble    ||  --   --
//        || ICON ||  "Voice"        ||  --   --
//                                        B   T
//—————————————————————————————————————————————————————————————————————————————————————————————————————————————————
    private void menuMusicalAtmosphere(String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "menuMusicalAtmosphere");
                logFile("ANDROID: menuMusicalAtmosphere");
                int bassVolume;
                int trebleVolume;
                closeAllDisplays();
                bassTrebleLayout.setVisibility(View.INVISIBLE);
                bassTextCard.setCardBackgroundColor(getResources().getColor(R.color.sourceMainBackground));
                trebleTextCard.setCardBackgroundColor(getResources().getColor(R.color.sourceMainBackground));
                // String ex: view_73 : 2 (high box) : icon : icon : icon : title_1 : title_2 : title_3 : + : 5 : - : 2 : end_string
                String[] messageIds = message.split(":");
                if (messageIds[1].toLowerCase().contains("2")){
                    //set other CardView to main colors
                    bassTextCard.setCardBackgroundColor(getResources().getColor(R.color.sourceMainBackground));
                    trebleTextCard.setCardBackgroundColor(getResources().getColor(R.color.sourceMainBackground));
                    //set main CardView to select color
                    cardRow2.setCardBackgroundColor(getResources().getColor(R.color.sourceSelected));
                    cardText2.setCardBackgroundColor(getResources().getColor(R.color.sourceSelected));

                }
                if (messageIds[1].toLowerCase().contains("3")){
                    //set other CardView to main colors
                    cardRow2.setCardBackgroundColor(getResources().getColor(R.color.sourceMainBackground));
                    cardText2.setCardBackgroundColor(getResources().getColor(R.color.sourceMainBackground));
                    trebleTextCard.setCardBackgroundColor(getResources().getColor(R.color.sourceMainBackground));
                    //set main CardView to select color
                    bassTextCard.setCardBackgroundColor(getResources().getColor(R.color.sourceSelected));
                }
                if (messageIds[1].toLowerCase().contains("4")){
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
                if (messageIds[2].toLowerCase().contains("icon_cercle_empty")){
                    emptyCercleRow1.setVisibility(View.VISIBLE);
                    checkedCercleRow1.setVisibility(View.INVISIBLE);
                }
                if (messageIds[2].toLowerCase().contains("icon_cercle_full")){
                    emptyCercleRow1.setVisibility(View.INVISIBLE);
                    checkedCercleRow1.setVisibility(View.VISIBLE);
                }

                if (messageIds[3].length() == 0){
                    emptyCercleRow2.setVisibility(View.INVISIBLE);
                    checkedCercleRow2.setVisibility(View.INVISIBLE);
                }
                if (messageIds[3].toLowerCase().contains("icon_cercle_empty")){
                    emptyCercleRow2.setVisibility(View.VISIBLE);
                    checkedCercleRow2.setVisibility(View.INVISIBLE);
                }
                if (messageIds[3].toLowerCase().contains("icon_cercle_full")){
                    emptyCercleRow2.setVisibility(View.INVISIBLE);
                    checkedCercleRow2.setVisibility(View.VISIBLE);
                }

                if (messageIds[4].length() == 0){
                    emptyCercleRow3.setVisibility(View.INVISIBLE);
                    checkedCercleRow3.setVisibility(View.INVISIBLE);
                }
                if (messageIds[4].toLowerCase().contains("icon_cercle_empty")){
                    emptyCercleRow3.setVisibility(View.VISIBLE);
                    checkedCercleRow3.setVisibility(View.INVISIBLE);
                }
                if (messageIds[4].toLowerCase().contains("icon_cercle_full")){
                    emptyCercleRow3.setVisibility(View.INVISIBLE);
                    checkedCercleRow3.setVisibility(View.VISIBLE);
                }

                musicalAtmosphereMainText1.setText(messageIds[5]);
                musicalAtmosphereMainText2.setText(messageIds[6]);
                musicalAtmosphereMainText3.setText(messageIds[7]);

                if (messageIds[6].toLowerCase().contains("bass")){
                    bassTrebleLayout.setVisibility(View.VISIBLE);
                }

                if (messageIds[8].contains("+")){
                    bassVolume = Integer.parseInt(messageIds[9]) + 10;
                    bassProgressBar.setProgress(bassVolume);
                }else if (messageIds[8].contains("-")){
                    bassVolume = 10 - Integer.parseInt(messageIds[9]);
                    bassProgressBar.setProgress(bassVolume);
                }

                if (messageIds[10].contains("+")){
                    trebleVolume = Integer.parseInt(messageIds[11]) + 10;
                    trebleProgressBar.setProgress(trebleVolume);
                }else if (messageIds[10].contains("-")){
                    trebleVolume = 10 - Integer.parseInt(messageIds[11]);
                    trebleProgressBar.setProgress(trebleVolume);
                }

                musicalAtmosphere.setVisibility(View.VISIBLE);
            }
        });
    }

//—————————————————————————————————————————————————————————————————————————————————————————————————————————————————
//  A three rows on the first column with a only one box on the second column view.
//        || TEXT ||                 ||
//        ||------||                 ||
//        || TEXT ||                 ||
//        ||------||                 ||
//        || TEXT ||                 ||
//—————————————————————————————————————————————————————————————————————————————————————————————————————————————————
    public void confirm_cancel(String message){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "confirm_cancel");
                logFile("ANDROID: confirm_cancel");
                closeAllDisplays();
                clearTextBoxes();
                // string ex: view_63 : Cancel : Confirm : _ : Do you want to reset : these parameters? : end_string
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

//—————————————————————————————————————————————————————————————————————————————————————————————————————————————————
//  A INFO view. Only one box that receives characters. It might receive some icons, have to investigate.
//        || TEXT          ||
//        || TEXT          ||
//        || TEXT          ||
//—————————————————————————————————————————————————————————————————————————————————————————————————————————————————
    public void informationBox(String message){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "informationBox");
                logFile("ANDROID: informationBox");
                closeAllDisplays();
                infoGridText.setText("");
                // string ex: view_52 : Message is received here bla bla : end_string
                String[] messageIds = message.split(":");
                infoGridText.setText(messageIds[1]);
                infoGrid.setVisibility(View.VISIBLE);
            }
        });
    }

    public void closeAllDisplays(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "closeAllDisplays");
                logFile("ANDROID: closeAllDisplays");
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
                Log.i(TAG, "clearTextBoxes");
                logFile("ANDROID: clearTextBoxes");
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
                Log.i(TAG, "setIconsToInvisible");
                logFile("ANDROID: setIconsToInvisible");
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
                Log.i(TAG, "setIconsVisible");
                logFile("ANDROID: setIconsVisible");
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
                    complex3x1text.setVisibility(View.VISIBLE);
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

    public void appSelectionMenu(String keyReceived){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "appSelectionMenu");
                logFile("ANDROID: appSelectionMenu");
//                appSelectPopUp.setContentView(R.layout.app_selection_menu);
//                appSelectPopUp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                Intent googleMaps = getPackageManager().getLaunchIntentForPackage("com.google.android.apps.maps");
                Intent waze = getPackageManager().getLaunchIntentForPackage("com.waze");
                Intent spotify = getPackageManager().getLaunchIntentForPackage("com.spotify.music");

                //set all the app background to not selected
//                googleMapsCard.setCardBackgroundColor(getResources().getColor(R.color.popUpMenuSelected));
//                wazeCard.setCardBackgroundColor(getResources().getColor(R.color.popUpMenuSelected));
//                spotifyCard.setCardBackgroundColor(getResources().getColor(R.color.popUpMenuBackground));
//                app_select_tv.setText("0");
                gMapsCard.setCardBackgroundColor(getResources().getColor(R.color.popUpMenuBackground));
                layout_wazeCard.setCardBackgroundColor(getResources().getColor(R.color.popUpMenuBackground));
                layout_spotifyCard.setCardBackgroundColor(getResources().getColor(R.color.popUpMenuBackground));

                if (keyReceived.toLowerCase().contains("menu")){
                    if (appSelectionLayout.isShown()){
                        appSelectionLayout.setVisibility(View.INVISIBLE);
                    } else {
                        appSelectionLayout.setVisibility(View.VISIBLE);
                        selectedPopUpApp = 1;
                        gMapsCard.setCardBackgroundColor(getResources().getColor(R.color.popUpMenuSelected));
                    }
//                    if (appSelectPopUp.isShowing()){
//                        appSelectPopUp.dismiss();
//                        selectedPopUpApp = 1;
//                    }
//                    else {
//                        selectedPopUpApp = 1;
//                        appSelectPopUp.show();
//                        googleMapsCard.setCardBackgroundColor(getResources().getColor(R.color.popUpMenuSelected));
//
//                    }
                }
                else if (keyReceived.toLowerCase().contains("right")){
                    if (selectedPopUpApp == 3) selectedPopUpApp = 1;
                    else selectedPopUpApp++;
                }
                else if (keyReceived.toLowerCase().contains("left")){
                    if (selectedPopUpApp == 1) selectedPopUpApp = 3;
                    else selectedPopUpApp--;
                }

                //check which is the current app selected and highlight it
                if (selectedPopUpApp == 1) {
                    gMapsCard.setCardBackgroundColor(getResources().getColor(R.color.popUpMenuSelected));
//                    googleMapsCard.setCardBackgroundColor(getResources().getColor(R.color.popUpMenuSelected));
                    Log.i(TAG, "selectedPopUp=" + selectedPopUpApp +"; googleMapsCard");
                }
                else if (selectedPopUpApp == 2) {
                    layout_wazeCard.setCardBackgroundColor(getResources().getColor(R.color.popUpMenuSelected));
//                    wazeCard.setCardBackgroundColor(getResources().getColor(R.color.popUpMenuSelected));
                    Log.i(TAG, "selectedPopUp=" + selectedPopUpApp +"; wazeCard");
                }
                else if (selectedPopUpApp == 3) {
                    layout_spotifyCard.setCardBackgroundColor(getResources().getColor(R.color.popUpMenuSelected));
//                    spotifyCard.setCardBackgroundColor(getResources().getColor(R.color.popUpMenuSelected));
                    Log.i(TAG, "selectedPopUp=" + selectedPopUpApp +"; spotifyCard");
                }

                if (keyReceived.toLowerCase().contains("enter") && appSelectionLayout.getVisibility() == View.VISIBLE){
                    Log.i(TAG, "enter pressed");
                    if (selectedPopUpApp == 1){
                        Log.i(TAG, "selectedPopUp=1");
                         //appSelectPopUp.dismiss();
                        popUpMenuAppSelected = "googleMaps";
                        appSelectionLayout.setVisibility(View.INVISIBLE);

                        Intent speachIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                        speachIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                        speachIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Please specify your destination:");
                        startActivityForResult(speachIntent, RECOGNIZER_RESULT);
                    }
                    else if (selectedPopUpApp == 2){
                        //appSelectPopUp.dismiss();
                        popUpMenuAppSelected = "waze";
                        appSelectionLayout.setVisibility(View.INVISIBLE);

                        Intent speachIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                        speachIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                        speachIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Please specify your destination:");
                        startActivityForResult(speachIntent, RECOGNIZER_RESULT);
                    }
                    else if (selectedPopUpApp == 3){
                        if (spotify != null){
                            //appSelectPopUp.dismiss();
                            appSelectionLayout.setVisibility(View.INVISIBLE);
                            startActivity(spotify);
                        }
                    }
                }
            }
        });
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data){
        Log.i(TAG, "onActivityResult");
        logFile("ANDROID: onActivityResult -> get voice to text and open nav app with destination set");
        if (requestCode == RECOGNIZER_RESULT && resultCode == RESULT_OK){
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            destination = matches.get(0).toString();
        }
        super.onActivityResult(requestCode, resultCode, data);

        if (popUpMenuAppSelected == "googleMaps"){
            //Start GoogleMaps Intent using the data received from GoogleVoiceToText
            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + destination + "&mode=d");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            popUpMenuAppSelected = "";
            startActivity(mapIntent);
        }else if (popUpMenuAppSelected == "waze"){
            /**
             * Start Waze Intent using the data received from GoogleVoiceToText
             */
            popUpMenuAppSelected = "";
        }

    }

    public void openApp(View v){
        Intent googleMaps = getPackageManager().getLaunchIntentForPackage("com.google.android.apps.maps");
        Intent waze = getPackageManager().getLaunchIntentForPackage("com.waze");
        Intent spotify = getPackageManager().getLaunchIntentForPackage("com.spotify.music");

        if (v.getId() == R.id.googleMaps){
            if (googleMaps != null){
                startActivity(googleMaps);
            }
        }
        if (v.getId() == R.id.waze){
            if (waze != null){
                startActivity(waze);
            }
        }
        if (v.getId() == R.id.spotify){
            if (spotify != null){
                startActivity(spotify);
            }
        }
        Log.i(TAG, "openApp");
        logFile("ANDROID: openApp - Gmaps, Waze, Spotify");
    }

    public void onReqButton(View v){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String msg = "reqMsg";
                arduino.send(msg.getBytes());
                Log.i(TAG, "onReqButton");
                logFile("ANDROID: onReqButton");
            }
        });

    }

    public void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void logFile(String message){

        FileOutputStream fos = null;
        try {
            fos = openFileOutput(LOG_FILE, MODE_APPEND);
            fos.write(message.getBytes());
            fos.write("\r\n".getBytes());

            //Toast.makeText(this, "Saved to " + getFilesDir(), Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}