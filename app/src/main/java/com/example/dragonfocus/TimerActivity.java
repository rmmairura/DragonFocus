package com.example.dragonfocus;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TimerActivity extends AppCompatActivity {

    TextView study_timerDisplay;

    TextView break_timerDisplay;

    TextView rounds_display;

    ProgressBar progressBar;

    ImageView imageView;
    private long totalStudyMillis;
    private long elapsedStudyMillis = 0;

    Animation shake;
    TextView phaseLabel;
    CountDownTimer countDownTimer;
    int roundsLeft;
    boolean isStudyPhase = true;

    boolean isPaused= false;

    Button pauseButton;
    boolean isTimerRunning = false;

    private long timeLeftInMillis;
    long studyMillis, breakMillis;
    MediaPlayer mediaPlayer;
    int studyMinutes;
    int breakMinutes;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_timer);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        pauseButton = findViewById(R.id.pause);
        studyMinutes = getIntent().getIntExtra("studyMinutes", 25);
        breakMinutes = getIntent().getIntExtra("breakMinutes", 5);
        roundsLeft = getIntent().getIntExtra("rounds",4);
        study_timerDisplay = findViewById(R.id.study_timer);
        phaseLabel = findViewById(R.id.phase);
        rounds_display = findViewById(R.id.rounds_number);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(100);
        imageView = findViewById(R.id.egg);
        shake = AnimationUtils.loadAnimation(this, R.anim.shake);


        //mediaPlayer = MediaPlayer.create(this, R.raw.timer_notification);


        studyMillis= studyMinutes * 60 *1000;
        breakMillis = breakMinutes * 60 * 1000;
        Log.d("DEBUG", "roundsLeft = " + roundsLeft);
        rounds_display.setText(String.format("Rounds left: %d", roundsLeft));;
        isStudyPhase = true;
        startTimer(studyMillis);
        mediaPlayer = MediaPlayer.create(this, R.raw.timer_notification);
        pauseButton.setOnClickListener(v -> {
            if (isTimerRunning) {
                countDownTimer.cancel();
                isTimerRunning = false;
                isPaused = true;
                pauseButton.setText("Resume");
            } else if (isPaused) {
                startTimer(timeLeftInMillis);
                pauseButton.setText("Pause");
            }
        });
    }

    private void startTimer(long millis){
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        timeLeftInMillis = millis;
        totalStudyMillis = studyMillis * roundsLeft;
        countDownTimer = new CountDownTimer(millis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                imageView.startAnimation(shake);
                long hours = millisUntilFinished / (1000 * 60 * 60);
                long min = (millisUntilFinished / (1000 * 60)) % 60;
                long seconds = (millisUntilFinished / 1000) % 60;
                timeLeftInMillis = millisUntilFinished;
                study_timerDisplay.setText(String.format("%02d: %02d: %02d", hours, min, seconds));


                if(isStudyPhase){
                    long roundElapsed = studyMillis - millisUntilFinished;
                    long totalElapsed = elapsedStudyMillis + roundElapsed;
                    int progress = (int) ((totalElapsed * 100) / totalStudyMillis);
                    progressBar.setProgress(progress);
                }



            }

            @Override
            public void onFinish() {
                elapsedStudyMillis += studyMillis;
                isTimerRunning = false;
                if (mediaPlayer != null) {
                    mediaPlayer.start();
                }

                if(isStudyPhase){
                    elapsedStudyMillis += studyMillis;
                    isStudyPhase = false;
                    phaseLabel.setText("Break");
                    startTimer(breakMillis);
                }else {
                   roundsLeft--;
                   if (roundsLeft > 0){
                       isStudyPhase = true;
                       phaseLabel.setText("Study");
                       startTimer(studyMillis);
                       rounds_display.setText(String.format("Rounds left: %d", roundsLeft));;
                   } else {
                       if (mediaPlayer != null) {
                           mediaPlayer.start();
                       }

                       imageView.setImageResource(R.drawable.green_dragon2);
                       study_timerDisplay.setText("00:00:00");
                       rounds_display.setText("Rounds left: 0");
                       progressBar.setProgress(100);
                   }
                }
            }
        }.start();
        isTimerRunning = true;
        isPaused = false;

    }




    public void launchSetUp(View v){
        Intent i = new Intent(this, SetUp_TimerActivity.class);
        startActivity(i);
    }







}