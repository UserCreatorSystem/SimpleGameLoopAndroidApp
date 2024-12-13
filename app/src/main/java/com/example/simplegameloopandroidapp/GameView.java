package com.example.simplegameloopandroidapp;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

   // Silnik gry, odpowiadający za logikę i aktualizację gry
   private GameEngine gameEngine;

   // Obiekt gry, który będziemy rysować na ekranie
   private GameObject gameObject;

   // Komponent TextView do wyświetlania informacji o FPS i TPS
   private TextView fpsTpsTextView;

   public GameView(Context context, AttributeSet attrs) {
      super(context, attrs);

      // Dodajemy listenera SurfaceHolder, który będzie informowany o zmianach powierzchni rysowania
      getHolder().addCallback(this);

      // Ustawienie możliwości skupienia, aby móc odbierać zdarzenia (np. dotyk)
      setFocusable(true);
   }

   // Wywoływane, gdy powierzchnia rysowania zostanie utworzona
   @Override
   public void surfaceCreated(SurfaceHolder holder) {
      // Tworzymy obiekt gry z bitmapy, szerokości i wysokości powierzchni
      gameObject = new GameObject(BitmapFactory.decodeResource(getResources(), R.drawable.stone_32x32), getWidth(), getHeight());

      // Tworzymy silnik gry, przekazując mu holder, obiekt gry i komponent TextView
      gameEngine = new GameEngine(holder, gameObject, fpsTpsTextView);

      // Uruchamiamy silnik gry
      gameEngine.start();

   }

   // Wywoływane, gdy parametry powierzchni rysowania się zmienią (np. zmiana rozmiaru ekranu)
   // (Pusta implementacja - Można zaimplementować logikę dla aktualizacji rozmiaru obiektu gry)
   @Override
   public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
      // (Pusta implementacja)
   }

   // Wywoływane, gdy powierzchnia rysowania zostanie zniszczona
   @Override
   public void surfaceDestroyed(SurfaceHolder holder) {
      // Zatrzymujemy silnik gry, aby zwolnić zasoby
      gameEngine.stop();
   }

   // Ustawienie komponentu TextView do wyświetlania informacji o FPS i TPS
   public void setFpsTpsTextView(TextView fpsTpsTextView) {
      this.fpsTpsTextView = fpsTpsTextView;

      // Wiadomość logowania informująca o ustawieniu komponentu TextView
      Log.d("GameView", "fpsTpsTextView set: " + (fpsTpsTextView != null));
   }
}