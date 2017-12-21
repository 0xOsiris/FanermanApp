package com.roguestudios.fanermanapp;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import com.rouguestudios.fanermanapp.R;

import java.util.concurrent.TimeUnit;




public class SleepTimer extends AppCompatActivity {
    NumberPicker noPickerHours;
    NumberPicker noPickerMinutes;
    TextView textViewTime;
    ImageButton buttonStart;
    private ShareActionProvider mShareActionProvider;
    public CounterClass timer;
    int hours = 0;
    int minutes = 0;
    int seconds = 0;
    ImageButton buttonStop;

    Button removeAds;

    public String hms;


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_timer);




        textViewTime = (TextView) findViewById(R.id.textView);
        timer = new CounterClass((hours * 60 * 60 * 1000) + (minutes * 60 * 1000), 1000);
        timer.cancel();
        noPickerMinutes = (NumberPicker) findViewById(R.id.numberPicker2);
        noPickerMinutes.setMaxValue(59);
        noPickerMinutes.setMinValue(1);
        noPickerHours = (NumberPicker) findViewById(R.id.numberPicker);
        noPickerHours.setMaxValue(12);
        noPickerHours.setMinValue(0);
        buttonStart = (ImageButton)findViewById(R.id.button);
        buttonStop = (ImageButton)findViewById(R.id.buttonStop);
        buttonStart.setImageResource(R.mipmap.playbuttongrey);
        buttonStart.setEnabled(false);
        buttonStop.setEnabled(true);
        onStartClick();
        onStopClick();
        onShareClick();
        noPickerHours.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                hours = newVal;
                timer = new CounterClass((hours * 60 * 60 * 1000), 1000);
                String hms = String.format(("%02d:%02d:%02d"), hours, minutes, seconds);
                textViewTime.setText(hms);
                buttonStart.setImageResource(R.mipmap.playbutton);
                buttonStop.setImageResource(R.mipmap.stopbutton);
                buttonStop.setEnabled(true);
                if(timer != null){
                    buttonStart.setEnabled(true);
                }else{
                    buttonStart.setEnabled(false);
                }

            }
        });

        noPickerMinutes.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                minutes = newVal;
                timer = new CounterClass((minutes * 60 * 1000), 1000);
                String hms = String.format(("%02d:%02d:%02d"), hours, minutes, seconds);
                textViewTime.setText(hms);
                buttonStart.setImageResource(R.mipmap.playbutton);
                buttonStop.setImageResource(R.mipmap.stopbutton);

                if(timer != null){
                    buttonStart.setEnabled(true);
                }else{
                    buttonStart.setEnabled(false);
                }
                buttonStop.setEnabled(true);

            }
        });
        timer = new CounterClass((hours * 60 * 60 * 1000) + (minutes * 60 * 1000), 1000);





    }
    public void onStartClick(){
        buttonStart.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                    timer = new CounterClass((hours * 60 * 60 * 1000) + (minutes * 60 * 1000), 1000);
                    String hms = String.format(("%02d:%02d:%02d"), hours, minutes, seconds);
                textViewTime.setText(hms);
                    Intent intent = new Intent(SleepTimer.this, MainActivity.class);
                    String TimeValue = (hms);
                    intent.putExtra("TimeValue", TimeValue);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                buttonStart.setImageResource(R.mipmap.playbuttongrey);
                buttonStart.setEnabled(false);

                buttonStop.setEnabled(true);
                timer.cancel();



            }
        });
    }
    public void onStopClick(){
        buttonStop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                textViewTime.setText("00:00:00");
                buttonStop.setImageResource(R.mipmap.stopbuttongrey);
                buttonStart.setImageResource(R.mipmap.playbutton);
                buttonStop.setEnabled(false);
                buttonStart.setEnabled(true);
                String hms = String.format(("%02d:%02d:%02d"), hours, minutes, seconds);
                Intent intent = new Intent(SleepTimer.this, MainActivity.class);
                String TimeValue = (hms);
                intent.putExtra("StopValue", TimeValue);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);

                timer.cancel();

            }
        });

    }

    public class CounterClass extends CountDownTimer {


        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */

        public CounterClass(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }



        @TargetApi(Build.VERSION_CODES.GINGERBREAD)
        @Override

        public void onTick(long millisUntilFinished) {

            long millis = millisUntilFinished;
            String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis), TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                    TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
            textViewTime.setText(hms);

        }

        @Override
        public void onFinish() {

        }

    }

    @Override
    public void onBackPressed() {

        textViewTime.setText("00:00:00");
        buttonStop.setImageResource(R.mipmap.stopbuttongrey);
        buttonStart.setImageResource(R.mipmap.playbutton);
        buttonStop.setEnabled(false);
        buttonStart.setEnabled(true);
        String hms = String.format(("%02d:%02d:%02d"), hours, minutes, seconds);
        Intent intent = new Intent(SleepTimer.this, MainActivity.class);
        String TimeValue = (hms);
        intent.putExtra("StopValue", TimeValue);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);

        timer.cancel();
        super.onBackPressed();

    }

    public void onShareClick() {

        Button shareButton = (Button) findViewById(R.id.ShareButton);

        shareButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "This is a must have app for anyone who needs a fan to sleep!  https://goo.gl/nsXBYk";

                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Fanerman");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));

            }


        });

    }



}
