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

public class MainActivity extends SandroideBaseActivity {

    protected static final String TAG = "MainActivity";

    TextView tvButton, tvTrimmer, tvLed;
    BLEGeneralIO nanoButton;
    BLEGeneralIO nanoLed;
    BLEGeneralIO nanoTrimmer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BLEContext.initBLE(this);

        tvButton =(TextView) findViewById(R.id.tvButtonState);
        tvLed =(TextView) findViewById(R.id.tvLedState);
        tvTrimmer =(TextView) findViewById(R.id.tvAnalogValue);
        nanoLed = (BLEGeneralIO) BLEContext.findViewById("nano_rbs_general_io_28");
        nanoButton = (BLEGeneralIO) BLEContext.findViewById("nano_rbs_general_io_15");
        nanoTrimmer = (BLEGeneralIO) BLEContext.findViewById("nano_rbs_general_io_4");

        if (nanoTrimmer!=null) {
            nanoTrimmer.setOnGeneralIOEventListener(new BLEOnGeneralIOEventListener() {
                @Override
                public void onBoardInitEnded() {
                    nanoTrimmer.setStatus(BLEGeneralIO.GENERAL_IO_AI);
                }

                @Override
                public void onDigitalInputValueChanged(BLEGeneralIOEvent bleGeneralIOEvent) {

                }

                @Override
                public void onAnalogValueChanged(BLEGeneralIOEvent bleGeneralIOEvent) {
                    Log.d(TAG, "analog value changing: " + bleGeneralIOEvent.values[1]);
                    final float val = bleGeneralIOEvent.values[1];
                    runOnUiThread(new Runnable() {
                                                              @Override
                                                              public void run() {
                                                                  tvTrimmer.setText("" + Math.round(val * 100) / (float) 100);
                                                              }
                                                          }
                    );
                }

                @Override
                public void onDigitalOutputValueChanged(BLEGeneralIOEvent bleGeneralIOEvent) {

                }

                @Override
                public void onServoValueChanged(BLEGeneralIOEvent bleGeneralIOEvent) {

                }

                @Override
                public void onPWMValueChanged(BLEGeneralIOEvent bleGeneralIOEvent) {

                }

                @Override
                public void onGeneralIOStatusChanged(BLEGeneralIOEvent bleGeneralIOEvent) {

                }

                @Override
                public void onSetGeneralIOParameter(BLEGeneralIOEvent bleGeneralIOEvent) {

                }
            });
        }


        if (nanoLed!=null) {
            nanoLed.setOnGeneralIOEventListener(new BLEOnGeneralIOEventListener() {
                @Override
                public void onBoardInitEnded() {
                    nanoLed.setStatus(BLEGeneralIO.GENERAL_IO_DO);
                }

                @Override
                public void onDigitalInputValueChanged(BLEGeneralIOEvent bleGeneralIOEvent) {
                }

                @Override
                public void onAnalogValueChanged(BLEGeneralIOEvent bleGeneralIOEvent) {
                }

                @Override
                public void onDigitalOutputValueChanged(BLEGeneralIOEvent bleGeneralIOEvent) {
                    final float val = bleGeneralIOEvent.values[1];
                    runOnUiThread(new Runnable() {
                                                              @Override
                                                              public void run() {
                                                                  tvLed.setText(val == 1 ? "Led ON" : "Led OFF");
                                                                  tvLed.setTextColor(val == 1 ? Color.BLUE : Color.BLACK);
                                                              }
                                                          }
                    );
                }

                @Override
                public void onServoValueChanged(BLEGeneralIOEvent bleGeneralIOEvent) {

                }

                @Override
                public void onPWMValueChanged(BLEGeneralIOEvent bleGeneralIOEvent) {

                }

                @Override
                public void onGeneralIOStatusChanged(BLEGeneralIOEvent bleGeneralIOEvent) {

                }

                @Override
                public void onSetGeneralIOParameter(BLEGeneralIOEvent bleGeneralIOEvent) {

                }
            });
        }

        if (nanoButton!=null) {
            nanoButton.setOnGeneralIOEventListener(new BLEOnGeneralIOEventListener() {
                @Override
                public void onBoardInitEnded() {
                    nanoButton.setStatus(BLEGeneralIO.GENERAL_IO_DI);
                }

                @Override
                public void onDigitalInputValueChanged(BLEGeneralIOEvent bleGeneralIOEvent) {
                    Log.d(TAG, "button pressing: " + bleGeneralIOEvent.values[1]);
                    if (bleGeneralIOEvent.values[1] == 1) {
                        Log.d(TAG, "led: setting HIGH");
                        nanoLed.setDigitalValueHigh(true);
                        runOnUiThread(new Runnable() {
                                                                  @Override
                                                                  public void run() {
                                                                      tvButton.setText("Button PRESSED");
                                                                      tvButton.setTextColor(Color.BLUE);
                                                                      //tvLed.setText("Led ON");
                                                                      //tvLed.setTextColor(Color.BLUE);
                                                                  }
                                                              }
                        );
                    } else {
                        Log.d(TAG, "led: setting LOW");
                        nanoLed.setDigitalValueHigh(false);
                        runOnUiThread(new Runnable() {
                                                                  @Override
                                                                  public void run() {
                                                                      tvButton.setText("Button not pressed");
                                                                      tvButton.setTextColor(Color.BLACK);
                                                                      //tvLed.setText("Led OFF");
                                                                      //tvLed.setTextColor(Color.BLACK);
                                                                  }
                                                              }
                        );
                    }
                }

                @Override
                public void onAnalogValueChanged(BLEGeneralIOEvent bleGeneralIOEvent) {

                }

                @Override
                public void onDigitalOutputValueChanged(BLEGeneralIOEvent bleGeneralIOEvent) {

                }

                @Override
                public void onServoValueChanged(BLEGeneralIOEvent bleGeneralIOEvent) {

                }

                @Override
                public void onPWMValueChanged(BLEGeneralIOEvent bleGeneralIOEvent) {

                }

                @Override
                public void onGeneralIOStatusChanged(BLEGeneralIOEvent bleGeneralIOEvent) {

                }

                @Override
                public void onSetGeneralIOParameter(BLEGeneralIOEvent bleGeneralIOEvent) {

                }
            });
        }

    }

}