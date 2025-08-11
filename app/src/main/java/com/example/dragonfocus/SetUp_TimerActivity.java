package com.example.dragonfocus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SetUp_TimerActivity extends AppCompatActivity {
    EditText studyInput;
    EditText breakInput;
    EditText roundInput;

    Button startButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_set_up_timer);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        studyInput = findViewById(R.id.et_studymin);
        breakInput = findViewById(R.id.et_break);
        roundInput = findViewById(R.id.et_rounds);
        startButton = findViewById(R.id.start_button);
        startButton.setOnClickListener(v -> {
            String studyText = studyInput.getText().toString();
            String breakText = breakInput.getText().toString();
            String roundText = roundInput.getText().toString();

            if(studyText.isEmpty() || breakText.isEmpty()|| roundText.isEmpty() ){
                Toast.makeText(this, "Please Fill all Fields", Toast.LENGTH_SHORT).show();
                return;
            }
            int studyMinutes = Integer.parseInt(studyText);
            int breakMinutes = Integer.parseInt(breakText);
            int rounds = Integer.parseInt(roundText);

            Intent i = new Intent(SetUp_TimerActivity.this, TimerActivity.class);
            i.putExtra("studyMinutes", studyMinutes);
            i.putExtra("breakMinutes", breakMinutes);
            i.putExtra("rounds", rounds);
            startActivity(i);
        });


    }
//    public void launchTimer(View v){
//
//        //launch setup_timer activity
//        Intent i = new Intent(SetUp_TimerActivity.this, TimerActivity.class);
//        i.putExtra("studyMinutes", studyMinutes);
//        i.putExtra("breakMinutes", breakMinutes);
//        i.putExtra("rounds", rounds);
//        startActivity(i);
//    }
}