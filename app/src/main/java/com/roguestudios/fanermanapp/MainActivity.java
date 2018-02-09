package com.roguestudios.fanermanapp;


import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;
import com.rouguestudios.fanermanapp.R;

import java.util.concurrent.TimeUnit;

import static com.rouguestudios.fanermanapp.R.raw.alternate_fan;

/**
 * Created by Leyton on 10/27/2015.
 */

public class MainActivity extends AppCompatActivity {


    private static ImageView imgView;
    private static ImageView logoImage;
    private static Button button;
    private static ImageButton imageButton;
    private int current_image_index;
    private static Button creditsButton;
    private static ImageButton sleepTimer;
    int[] Fanimages = {R.drawable.fan_off_new, R.drawable.fan_on_new};
    MediaPlayer standardFanSound;
    MediaPlayer alternateFanSound;
    MediaPlayer whiteFanSound;
    MediaPlayer standardFanSound2;
    MediaPlayer alternateFanSound2;
    MediaPlayer whiteFanSound2;
    ImageView blade;
    TextView textTime;
    CounterClass timer;
    Boolean timerStopped;
    float volume = 1;
    float speed = 0.05f;

    //Interstitial Ad
    PublisherInterstitialAd mPublisherInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        /*AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest request = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("A2A04678E30DAAD01448F6EFD8642BFC")
                .build();
        mAdView.loadAd(request);*/

        //Interstitial
        mPublisherInterstitialAd = new PublisherInterstitialAd(this);
        mPublisherInterstitialAd.setAdUnitId("ca-app-pub-5005569223959807/8551408374");

        mPublisherInterstitialAd.setAdListener(new AdListener() {


            @Override
            public void onAdClosed() {
                requestNewInterstitial();

                Intent intent = new Intent("com.roguestudios.fanermanapp.SleepTimer");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });

        requestNewInterstitial();


        AppRater.app_launched(this);

        sleepTimerClick();
        buttonClick();
        creditsClick();
        settingsClick();

        logoImage = (ImageView) findViewById(R.id.logo);
        Bundle extras = getIntent().getExtras();

        String startString;
        String backPressed;

        if (extras != null) {

            if (extras.getString("TimeValue") != null) {

                startString = extras.getString("TimeValue");
                String split[] = startString.split(":");
                long futureInMillis = Integer.parseInt(split[0]) * 60 * 60 * 1000 + Integer.parseInt(split[1]) * 60 * 1000;
                timer = new CounterClass(futureInMillis, 1000);

                textTime = (TextView) findViewById(R.id.timeText);
                textTime.setText(startString);

                logoImage.setImageResource(R.mipmap.timer_logo);

                imgView = (ImageView) findViewById(R.id.imageView);
                button = (Button) findViewById(R.id.button);
                blade = (ImageView) findViewById(R.id.bladeImage);
                Animation animRotate = AnimationUtils.loadAnimation(this, R.anim.rotate);
                standardFanSound = MediaPlayer.create(this, R.raw.fanpost);

                alternateFanSound = MediaPlayer.create(this, R.raw.alternate_fan);

                whiteFanSound = MediaPlayer.create(this, R.raw.whitenoise);

                SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

                String soundValue = getPrefs.getString("soundPref", "Standard");


                fanStarted();

                if(textTime.equals(startString)){
                    timer.cancel();
                    timer.start();
                }else{
                    timer.start();
                }


            } else if (extras.getString("StopValue") != null) {
                startString = extras.getString("StopValue");
                String split[] = startString.split(":");
                long futureInMillis = Integer.parseInt(split[0]) * 60 * 60 * 1000 + Integer.parseInt(split[1]) * 60 * 1000;
                timer = new CounterClass(futureInMillis, 1000);

                textTime = (TextView) findViewById(R.id.timeText);
                textTime.setText(startString);
                timer.cancel();
                logoImage.setImageResource(R.mipmap.logo);
                timerStopped = true;

                onTimeStopped();


            } else {
                textTime = (TextView) findViewById(R.id.timeText);
                textTime.setText("");
            }

        }
    }

    //InterstitialAd
    public void requestNewInterstitial() {
        PublisherAdRequest adRequest = new PublisherAdRequest.Builder()
                .build();
        mPublisherInterstitialAd.loadAd(adRequest);


    }


    public void onTimeStopped(){
        if(timerStopped == true){
            logoImage.setImageResource(R.mipmap.logo);

            textTime.setText("");
        }

    }


    //Credits Screen Activity
    public void creditsClick() {
        creditsButton = (Button) findViewById(R.id.creditsButton);
        creditsButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent("com.roguestudios.fanermanapp.Main2Activity");
                        startActivity(intent);
                    }
                }
        );
    }

    //Activity SleepTimer
    public void sleepTimerClick(){
        blade = (ImageView)findViewById(R.id.bladeImage);
        final Animation animRotate = AnimationUtils.loadAnimation(this, R.anim.rotate);

        imgView = (ImageView) findViewById(R.id.imageView);

        standardFanSound = MediaPlayer.create(this, R.raw.fanpost);
        alternateFanSound = MediaPlayer.create(this, alternate_fan);
        whiteFanSound = MediaPlayer.create(this, R.raw.whitenoise);

        sleepTimer = (ImageButton)findViewById(R.id.sleepTimer);
        sleepTimer.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        if (blade.getAnimation() != null) {
                            blade.clearAnimation();
                            standardFanSound.stop();
                            standardFanSound.prepareAsync();
                            alternateFanSound.stop();
                            alternateFanSound.prepareAsync();
                            whiteFanSound.stop();
                            whiteFanSound.prepareAsync();
                            current_image_index++;
                            current_image_index = current_image_index % Fanimages.length;
                            imgView.setImageResource(Fanimages[current_image_index]);
                            imgView.invalidate();
                            if (timer != null) {
                                timer.cancel();
                            }

                        }
                        if (mPublisherInterstitialAd.isLoaded()) {
                            mPublisherInterstitialAd.show();
                        } else {

                            Intent intent = new Intent("com.roguestudios.fanermanapp.SleepTimer");
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    }
                }
        );
    }

    //Activity Settings Screen
    public void settingsClick() {
        imageButton = (ImageButton) findViewById(R.id.imageButton);
        imageButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent("com.roguestudios.fanermanapp.PREFS");
                        startActivity(intent);
                    }
                }
        );
    }

    //Fan Click
    public void buttonClick() {
        imgView = (ImageView) findViewById(R.id.imageView);
        button = (Button) findViewById(R.id.button);
        blade = (ImageView)findViewById(R.id.bladeImage);
        final Animation animRotate = AnimationUtils.loadAnimation(this, R.anim.rotate);
        standardFanSound = MediaPlayer.create(this, R.raw.fanpost);

        alternateFanSound = MediaPlayer.create(this, alternate_fan);

        whiteFanSound = MediaPlayer.create(this, R.raw.whitenoise);

        standardFanSound2 = MediaPlayer.create(this, R.raw.fanpost);

        alternateFanSound2 = MediaPlayer.create(this, alternate_fan);

        whiteFanSound2 = MediaPlayer.create(this, R.raw.whitenoise);


        button.setOnClickListener(

                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {



                        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

                        String soundValue = getPrefs.getString("soundPref", "Standard");


                        if (blade.getAnimation() == null) {


                            // no animation, start it
                            if (soundValue.equals("Alternate")){
                                alternateFanSound.start();
                                alternateFanSound.setLooping(true);
                                blade.startAnimation(animRotate);
                                if(blade.getAnimation() == null){
                                    alternateFanSound.stop();
                                    alternateFanSound.prepareAsync();
                                }



                            } else if (soundValue.equals("White")){
                                whiteFanSound.start();
                                whiteFanSound.setLooping(true);
                                blade.startAnimation(animRotate);
                                if(blade.getAnimation() == null){
                                    whiteFanSound.stop();
                                    whiteFanSound.prepareAsync();
                                }

                            } else if (soundValue.equals("Standard")) {
                                standardFanSound.start();
                                standardFanSound.setLooping(true);
                                blade.startAnimation(animRotate);
                                if(blade.getAnimation() == null){
                                    float deltaTime = 5;

                                    standardFanSound.setVolume(volume, volume);
                                    volume -= speed* deltaTime;

                                    standardFanSound.prepareAsync();
                                }

                            }

                        } else {
                            //animation is showing, stop it
                            blade.clearAnimation();
                            alternateFanSound.stop();
                            alternateFanSound.prepareAsync();
                            whiteFanSound.stop();
                            whiteFanSound.prepareAsync();
                            standardFanSound.stop();
                            standardFanSound.prepareAsync();

                        }
                        current_image_index++;
                        current_image_index = current_image_index % Fanimages.length;
                        imgView.setImageResource(Fanimages[current_image_index]);
                        imgView.invalidate();


                    }


                }
        );
    }


    public void fanStarted() {
        imgView = (ImageView) findViewById(R.id.imageView);
        button = (Button) findViewById(R.id.button);
        blade = (ImageView) findViewById(R.id.bladeImage);
        final Animation animRotate = AnimationUtils.loadAnimation(this, R.anim.rotate);
        standardFanSound = MediaPlayer.create(this, R.raw.fanpost);

        alternateFanSound = MediaPlayer.create(this, alternate_fan);

        whiteFanSound = MediaPlayer.create(this, R.raw.whitenoise);


        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        String soundValue = getPrefs.getString("soundPref", "Standard");


        if (blade.getAnimation() == null) {


            // no animation, start it
            if (soundValue.equals("Alternate")) {
                alternateFanSound.start();
                alternateFanSound.setLooping(true);
                blade.startAnimation(animRotate);
                if (blade.getAnimation() == null) {
                    alternateFanSound.stop();

                    alternateFanSound.prepareAsync();
                }


            } else if (soundValue.equals("White")) {
                whiteFanSound.start();
                whiteFanSound.setLooping(true);
                blade.startAnimation(animRotate);
                if (blade.getAnimation() == null) {
                    whiteFanSound.stop();
                    whiteFanSound.prepareAsync();
                }

            } else if (soundValue.equals("Standard")) {
                standardFanSound.start();
                standardFanSound.setLooping(true);
                blade.startAnimation(animRotate);
                if (blade.getAnimation() == null) {

                    standardFanSound.stop();

                    standardFanSound.prepareAsync();
                }

            }

        } else {
            //animation is showing, stop it
            blade.clearAnimation();
            alternateFanSound.stop();
            alternateFanSound.prepareAsync();
            whiteFanSound.stop();
            whiteFanSound.prepareAsync();
            standardFanSound.stop();
            standardFanSound.prepareAsync();

        }
        current_image_index++;
        current_image_index = current_image_index % Fanimages.length;
        imgView.setImageResource(Fanimages[current_image_index]);
        imgView.invalidate();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }

    @Override
    public void onBackPressed() {
        standardFanSound = MediaPlayer.create(this, R.raw.fanpost);

        alternateFanSound = MediaPlayer.create(this, alternate_fan);

        whiteFanSound = MediaPlayer.create(this, R.raw.whitenoise);

        if(standardFanSound.isPlaying()){
            standardFanSound.stop();
            standardFanSound.prepareAsync();
        }else if(alternateFanSound.isPlaying()){
            alternateFanSound.stop();
            alternateFanSound.prepareAsync();
        }else if(whiteFanSound.isPlaying()){
            whiteFanSound.stop();
            whiteFanSound.prepareAsync();
        }else{

        }

        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
            textTime.setText(hms);



        }

        @Override
        public void onFinish() {
            blade = (ImageView)findViewById(R.id.bladeImage);
            imgView = (ImageView) findViewById(R.id.imageView);
            standardFanSound = MediaPlayer.create(MainActivity.this, R.raw.fanpost);
            alternateFanSound = MediaPlayer.create(MainActivity.this, alternate_fan);
            whiteFanSound = MediaPlayer.create(MainActivity.this, R.raw.whitenoise);

            SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());


            if(blade.getAnimation() == null) {
                textTime.setText("");
                logoImage.setImageResource(R.mipmap.logo);
            }else {

                alternateFanSound.stop();
                alternateFanSound.prepareAsync();
                whiteFanSound.stop();
                whiteFanSound.prepareAsync();
                standardFanSound.stop();
                standardFanSound.prepareAsync();

                logoImage.setImageResource(R.mipmap.logo);

                current_image_index++;
                current_image_index = current_image_index % Fanimages.length;
                imgView.setImageResource(Fanimages[current_image_index]);
                imgView.invalidate();
                blade.clearAnimation();
                textTime.setText("");

            }

        }


    }
}

