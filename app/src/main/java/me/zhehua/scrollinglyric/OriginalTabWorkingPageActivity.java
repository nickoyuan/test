package me.zhehua.scrollinglyric;

import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

import me.zhehua.uilibrary.LyricView;
import me.zhehua.uilibrary.adapter.LyricAdapter;
import me.zhehua.uilibrary.adapter.SimpleLyricAdapter;

import android.media.MediaPlayer;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class OriginalTabWorkingPageActivity extends AppCompatActivity implements OnClickListener{
    private static final String TAG = "OriginalTabWorkingPageActivity";
    LyricView mLyricView;
    private TextView songCurrentDurationLabel,songTitleLabel;
    private TextView songTotalDurationLabel;
    SeekBar seek_bar;
    ImageView play_button, pause_button, play, singer, record;
    MediaPlayer player;
    TextView recording_text, recording_time;
    Handler seekHandler = new Handler();
    boolean singer_song = true;
    boolean record_song = true;
    private boolean playState = false;
    private MediaRecorder mRecorder;
    private static String mFileName = null;
    public static final int REQUEST_AUDIO_PERMISSION_CODE = 1;
    private static final String LOG_TAG = "AudioRecording";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_original_tab_working_page);

        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/AudioRecording.3gp";

        mLyricView = (LyricView) findViewById(R.id.lv_main);
        InputStream lrcAStream = getResources().openRawResource(R.raw.yesterday_text);
      //  InputStream lrcBStream = getResources().openRawResource(R.raw.sample_cn);
        try {
            LyricAdapter lyricAdapter = new SimpleLyricAdapter(lrcAStream);
            lrcAStream.close();
         //   lrcBStream.close();
            mLyricView.setLyricAdapter(lyricAdapter);
        } catch (IOException e) {
            e.printStackTrace();
        }




        getInit();
        seekUpdation();

        songCurrentDurationLabel = (TextView) findViewById(R.id.songCurrentDurationLabel);
        songTotalDurationLabel = (TextView) findViewById(R.id.songTotalDurationLabel);
        songCurrentDurationLabel.setText("");
        songTotalDurationLabel.setText("");
        play=(ImageView)findViewById(R.id.btnPlay);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!playState){

                    play.setImageResource(R.drawable.btn_pause);

                    doStart(v);
                    playState = true;

                }else{
                    play.setImageResource(R.drawable.btn_play);

                    doPause(v);
                    playState = false;
                }

            }
        });

        singer=(ImageView)findViewById(R.id.btnSinger);
     singer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(singer_song){

                     singer.setImageResource(R.drawable.singer_on);
                    singer_song=false;
                }else{
                    singer.setImageResource(R.drawable.singer_off);
                    singer_song=true;
                }

            }
        });

        recording_text=(TextView) findViewById(R.id.textView_recording);
        recording_time=(TextView) findViewById(R.id.textView_time);

        record=(ImageView)findViewById(R.id.btnRecord);
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(record_song){

                    record.setImageResource(R.drawable.microphone);
                    record_song=false;
                    recording_text.setVisibility(View.GONE);
                    recording_time.setVisibility(View.GONE);

                }else{
                    record.setImageResource(R.drawable.stop_record);
                    record_song=true;
                    recording_text.setVisibility(View.VISIBLE);
                    recording_time.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Recording Started", Toast.LENGTH_LONG).show();
                }

            }
        });
        seek_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                if(progress == mediaPlayer.getDuration() ){
//                    if(mediaPlayer!=null)
//                        mediaPlayer.reset();
//                    play.setImageResource(R.drawable.btn_play);
//                    playState=false;
//                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int value = seekBar.getProgress();
                player.seekTo(value);
            }
        });



    }

    public void onClick(View view) {
     /*   switch(view.getId()){
            case R.id.btnRecord:{
        if(singer_song)
                {
                    singer.setImageResource(R.drawable.singer_on);
                    singer_song=false;
                }
                else
                {
                    singer.setImageResource(R.drawable.singer_off);
                    singer_song=true;
                }
                return;
            }
        }
        */
    }



    public void doStart(View view)  {
        // The duration in milliseconds
        int duration = this.player.getDuration();

        int currentPosition = this.player.getCurrentPosition();
        if(currentPosition== 0)  {
            this.seek_bar.setMax(duration);
            String maxTimeString = this.millisecondsToString(duration);
            this.songTotalDurationLabel.setText(maxTimeString);
        } else if(currentPosition== duration)  {
            // Resets the MediaPlayer to its uninitialized state.

            this.player.reset();
        }

        this.player.start();
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //mp.reset();
                mp.seekTo(0);
                play.setImageResource(R.drawable.btn_play);
                playState=false;
            }
        });
        // Create a thread to update position of SeekBar.
        UpdateSeekBarThread updateSeekBarThread= new UpdateSeekBarThread();
        seekHandler.postDelayed(updateSeekBarThread,50);

        //this.buttonPause.setEnabled(true);
        //this.buttonStart.setEnabled(false);
    }
    public void doPause(View view)  {
        this.player.pause();
    }

    private String millisecondsToString(int milliseconds)  {
        long minutes = milliseconds/(1000 * 60);
        long seconds =   milliseconds%(1000 * 60)/1000;
        return minutes+":"+ seconds;
    }



    public class UpdateSeekBarThread implements Runnable {

        public void run()  {
            int currentPosition = player.getCurrentPosition();
            int duration = player.getDuration();
            String currentPositionStr = millisecondsToString(currentPosition);
            songCurrentDurationLabel.setText(currentPositionStr);

            seek_bar.setProgress(currentPosition);
            // Delay thread 50 milisecond.
            seekHandler.postDelayed(this, 50);
        }
    }



    public void getInit() {
        seek_bar = (SeekBar) findViewById(R.id.seek_bar);
      //  play_button = (ImageView) findViewById(R.id.btnPlay);
      //  pause_button = (ImageView) findViewById(R.id.btnForward);
     //   text_shown = (TextView) findViewById(R.id.text_shown);
      //  play_button.setOnClickListener(this);
      //  pause_button.setOnClickListener(this);
        player = MediaPlayer.create(this, R.raw.yesterday_song);
        seek_bar.setMax(player.getDuration());

    }



    Runnable run = new Runnable() {
        @Override
        public void run() {

            seekUpdation();

        } };
         public void seekUpdation() {
             seek_bar.setProgress(player.getCurrentPosition());

             seekHandler.postDelayed(run, 1000); }

/*
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnPlay:
              //  text_shown.setText("Playing...");
                player.start();
                break;
            case R.id.btnForward:
                player.pause();
             //   text_shown.setText("Paused...");
        }


    }

*/













    boolean started = false;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus && !started) {
            Log.i(TAG, "window focused");
            mLyricView.startScroll();
            started = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_AUDIO_PERMISSION_CODE:
                if (grantResults.length> 0) {
                    boolean permissionToRecord = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean permissionToStore = grantResults[1] ==  PackageManager.PERMISSION_GRANTED;
                    if (permissionToRecord && permissionToStore) {
                        Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(),"Permission Denied",Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }
    public boolean CheckPermissions() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }
    private void RequestPermissions() {
        ActivityCompat.requestPermissions(OriginalTabWorkingPageActivity.this, new String[]{RECORD_AUDIO, WRITE_EXTERNAL_STORAGE}, REQUEST_AUDIO_PERMISSION_CODE);
    }

}
