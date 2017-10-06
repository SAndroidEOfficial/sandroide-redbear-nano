package com.example.giova.nano;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import it.unibs.sandroide.lib.BLEContext;
import it.unibs.sandroide.lib.activities.SandroideBaseActivity;
import it.unibs.sandroide.lib.item.generalIO.BLEGeneralIO;
import it.unibs.sandroide.lib.item.generalIO.BLEGeneralIOEvent;
import it.unibs.sandroide.lib.item.generalIO.BLEOnGeneralIOEventListener;
import it.unibs.sandroide.lib.item.generalIO.SandroideDevice;
import it.unibs.sandroide.lib.item.generalIO.SandroidePin;

public class MainActivity extends SandroideBaseActivity {

    protected static final String TAG = "MainActivity";

    TextView tvButton, tvTrimmer, tvLed;
    SandroideDevice nano;
    SandroidePin nanoButton;
    SandroidePin nanoLed;
    SandroidePin nanoTrimmer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BLEContext.initBLE(this);

        tvButton =(TextView) findViewById(R.id.tvButtonState);
        tvLed =(TextView) findViewById(R.id.tvLedState);
        tvTrimmer =(TextView) findViewById(R.id.tvAnalogValue);

        nano = ((SandroideDevice) BLEContext.findViewById("nanoservice_sandroide_device"));

        if (nano!=null) {

            nano.setOnDeviceConnected(new SandroideDevice.OnDeviceConnectedListener() {
                @Override
                public void onEvent(SandroideDevice device) {
                    Log.i(TAG,"OK, Nano connected");
                }
            });

            nanoButton = new SandroidePin().setDevice(nano,15)
                    .setMode(SandroidePin.PIN_MODE_DIGITAL_INPUT)
                    .setDeltaThreshold(0.02)
                    .setSamplingInterval(50)
                    .setOnValueReceived(new SandroidePin.OnValueReceivedListener() {
                        @Override
                        public void onEvent(final Object newValue, Object oldValue) {
                            nanoLed.setValue(newValue);
                            Log.d(TAG, "button pressing: " + newValue);
                            runOnUiThread(new Runnable() {
                                              @Override
                                              public void run() {
                                                  tvButton.setText((int)newValue==1?"Button PRESSED":"Button not pressed");
                                                  tvButton.setTextColor((int)newValue==1?Color.BLUE:Color.BLACK);
                                              }
                                          }
                            );
                        }
                    });
            nanoLed = new SandroidePin().setDevice(nano,28)
                    .setMode(SandroidePin.PIN_MODE_DIGITAL_OUTPUT)
                    .setValue(true)
                    .setOnValueReceived(new SandroidePin.OnValueReceivedListener() {
                        @Override
                        public void onEvent(final Object newValue, Object oldValue) {
                            Log.d(TAG, "led state: " + newValue);
                            runOnUiThread(new Runnable() {
                                              @Override
                                              public void run() {
                                                  tvLed.setText((int)newValue == 1 ? "Led ON" : "Led OFF");
                                                  tvLed.setTextColor((int)newValue == 1 ? Color.BLUE : Color.BLACK);
                                              }
                                          }
                            );
                        }
                    });

            nanoTrimmer = new SandroidePin().setDevice(nano,6)
                    .setMode(SandroidePin.PIN_MODE_ANALOG_INPUT,1)
                    //.setDeltaThreshold(0.2)
                    //.setSamplingInterval(400)
                    .setOnValueReceived(new SandroidePin.OnValueReceivedListener() {
                        @Override
                        public void onEvent(final Object newValue, Object oldValue) {
                            Log.d(TAG, "Trimmer: " + newValue);
                            runOnUiThread(new Runnable() {
                                              @Override
                                              public void run() {
                                                  tvTrimmer.setText(newValue.toString());
                                              }
                                          }
                            );
                        }
                    });
        }
    }

}