package com.example.simplegameloopandroidapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

   private GameView gameView;
   private TextView fpsTpsTextView;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      gameView = findViewById(R.id.gameView);
      fpsTpsTextView = findViewById(R.id.fpsTpsTextView);
      gameView.setFpsTpsTextView(fpsTpsTextView);
   }
}
