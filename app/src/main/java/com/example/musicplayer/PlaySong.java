package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class PlaySong extends AppCompatActivity {
    TextView songname;
    ImageView play;
    ImageView prev;
    ImageView next;
    SeekBar seekBar;
    ArrayList<File> songs;
    MediaPlayer mediaPlayer;
    int position;
    Thread updateSeek;
    String songName;
    @Override
    protected void onDestroy() {

        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        updateSeek.interrupt();
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);
        songname=findViewById(R.id.textView);
        play=findViewById(R.id.play);
        prev=findViewById(R.id.previous);
        next=findViewById(R.id.next);
        seekBar=findViewById(R.id.seekBar);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        songs=(ArrayList)bundle.getParcelableArrayList("songList");
        songName=bundle.getString("currentSong");
        songname.setText(songName);
        songname.setSelected(true);
        position=bundle.getInt("position");
        Uri uri= Uri.parse(songs.get(position).toString());
        mediaPlayer=MediaPlayer.create(this,uri);
        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
        updateSeek=new Thread(){
            @Override
            public void run() {
                int cur=0;
                try{
                    while(cur<mediaPlayer.getDuration()){
                        cur=mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(cur);
                        sleep(80);
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        updateSeek.start();


        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()){
                    play.setImageResource(R.drawable.play);
                    mediaPlayer.pause();
                }
                else{
                    play.setImageResource(R.drawable.pause);
                    mediaPlayer.start();
                }
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if(position!=0){
                    position--;
                }
                else {
                    position = songs.size() - 1;
                }
                Uri uri= Uri.parse(songs.get(position).toString());
                songName=songs.get(position).getName().toString();
                songname.setText(songName);
                mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);

                mediaPlayer.start();
                play.setImageResource(R.drawable.pause);
                seekBar.setMax(mediaPlayer.getDuration());

            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if(position!=songs.size()-1){
                    position++;
                }
                else {
                    position = 0;
                }
                Uri uri= Uri.parse(songs.get(position).toString());
                songName=songs.get(position).getName().toString();
                songname.setText(songName);
                mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
                mediaPlayer.start();

                seekBar.setMax(mediaPlayer.getDuration());



                play.setImageResource(R.drawable.pause);
            }
        });

    }
}