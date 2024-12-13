package com.example.simplegameloopandroidapp;

import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.Choreographer;
import android.widget.TextView;

public class GameEngine implements Choreographer.FrameCallback {

   // Uchwyt do powierzchni rysowania (Surface)
   private SurfaceHolder surfaceHolder;

   // Obiekt gry, który będziemy aktualizować i rysować
   private GameObject gameObject;

   // Komponent TextView do wyświetlania informacji o FPS i TPS
   private TextView fpsTpsTextView;

   // Czas ostatniej klatki (używany do obliczenia deltaTime)
   private long lastFrameTime = 0;

   // Aktualne FPS (ilość klatek na sekundę)
   private int fps;

   // Aktualne TPS (ilość aktualizacji na sekundę)
   private int tps;

   // Czas ostatniej aktualizacji (używany do aktualizacji TextView)
   private long lastUpdateTime = 0;

   // Docelowa wartość TPS (liczba aktualizacji na sekundę)
   private int targetTps = 30;

   // Akumulator czasu dla precyzyjnej kontroli TPS
   private double updateAccumulator = 0;

   public GameEngine(SurfaceHolder surfaceHolder, GameObject gameObject, TextView fpsTpsTextView) {
      this.surfaceHolder = surfaceHolder;
      this.gameObject = gameObject;
      this.fpsTpsTextView = fpsTpsTextView;
   }

   // Rozpoczęcie działania silnika gry
   public void start() {
      lastFrameTime = System.nanoTime(); // Pamiętamy czas rozpoczęcia pierwszej klatki
      Choreographer.getInstance().postFrameCallback(this); // Rejestrujemy się do otrzymywania callbacków dla klatek
   }

   // Zatrzymanie działania silnika gry
   public void stop() {
      Choreographer.getInstance().removeFrameCallback(this); // Wyrejestrowujemy się z callbacków dla klatek
   }

   // Ustawienie docelowej wartości TPS
   public void setTargetTps(int tps) {
      if (tps > 0) {
         this.targetTps = tps;
      } else {
         Log.w("GameEngine", "Nieprawidłowa wartość TPS. TPS musi być dodatnie.");
      }
   }

   // Callback wywoływany dla każdej klatki
   @Override
   public void doFrame(long frameTimeNanos) {

      // Obliczanie czasu delta (różnica między czasem obecnej i poprzedniej klatki)
      long deltaTime = frameTimeNanos - lastFrameTime;
      double deltaTimeSeconds = deltaTime / 1000000000.0; // Konwersja do sekund

      // Akumulujemy czas dla precyzyjnej kontroli TPS
      updateAccumulator += deltaTimeSeconds;

      // Wykonujemy aktualizacje gry, dopóki akumulator czasu przekracza interwał aktualizacji
      while (updateAccumulator >= 1.0 / targetTps) {
         gameObject.update(1.0 / targetTps); // Aktualizacja obiektu gry ze stałym deltaTime
         updateAccumulator -= 1.0 / targetTps;
         tps++; // Inkrementujemy licznik TPS dla każdej aktualizacji
      }

      // Renderowanie obiektu gry na ekran
      render();

      // Obliczenie FPS (ilość klatek na sekundę)
      fps = (int) (1000000000.0 / deltaTime);

      // Aktualizacja TextView z informacjami o FPS i TPS tylko raz na sekundę
      if (frameTimeNanos - lastUpdateTime >= 1000000000) {
         if (fpsTpsTextView != null) {
            if (fpsTpsTextView.getHandler() != null) {
               int finalTps = tps; // Kopiujemy wartość TPS, aby uniknąć konfliktów z wątkami
               fpsTpsTextView.getHandler().post(() -> fpsTpsTextView.setText("FPS: " + fps + ", TPS: " + finalTps));
               tps = 0; // Resetujemy licznik TPS po aktualizacji TextView
            }
         }

         lastUpdateTime = frameTimeNanos;
      }

      // Aktualizacja czasu ostatniej klatki
      lastFrameTime = frameTimeNanos;

      // Ponowne zarejestrowanie się do callbacku dla kolejnej klatki - *TO BYŁO BRAKUJĄCE!*
      Choreographer.getInstance().postFrameCallback(this);
   }

   private void render() {
      if (!surfaceHolder.getSurface().isValid()) {
         return;
      }

      Canvas canvas = surfaceHolder.lockCanvas();
      if (canvas != null) {
         canvas.drawColor(Color.BLACK);
         gameObject.draw(canvas);
         surfaceHolder.unlockCanvasAndPost(canvas);
      }
   }
}