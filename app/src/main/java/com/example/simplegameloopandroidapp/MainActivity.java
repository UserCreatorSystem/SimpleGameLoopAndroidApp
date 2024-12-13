package com.example.simplegameloopandroidapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

   // Widok gry (SurfaceView)
   private GameView gameView;

   // Komponent TextView do wyświetlania informacji o FPS i TPS
   private TextView fpsTpsTextView;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      // Ustawienie layoutu aktywności (activity_main.xml)
      setContentView(R.layout.activity_main);

      // Odnajdywanie elementów interfejsu w layoutcie
      gameView = findViewById(R.id.gameView);
      fpsTpsTextView = findViewById(R.id.fpsTpsTextView);

      // Przekazanie komponentu TextView do widoku gry
      gameView.setFpsTpsTextView(fpsTpsTextView);
   }
}