package com.example.simplegameloopandroidapp;

import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.Choreographer;
import android.widget.TextView;

public class GameEngine implements Choreographer.FrameCallback {

   private SurfaceHolder surfaceHolder;
   private GameObject gameObject;
   private TextView fpsTpsTextView;
   private long lastFrameTime = 0;
   private int fps;
   private int tps; // Dodajemy zmienną do przechowywania TPS
   private long lastUpdateTime = 0;
   private int targetTps = 30;
   private double updateAccumulator = 0; // Akumulator czasu dla TPS

   public GameEngine(SurfaceHolder surfaceHolder, GameObject gameObject, TextView fpsTpsTextView) {
      this.surfaceHolder = surfaceHolder;
      this.gameObject = gameObject;
      this.fpsTpsTextView = fpsTpsTextView;
   }

   public void start() {
      lastFrameTime = System.nanoTime();
      Choreographer.getInstance().postFrameCallback(this);
   }

   public void stop() {
      Choreographer.getInstance().removeFrameCallback(this);
   }

   public void setTargetTps(int tps) {
      if (tps > 0) {
         this.targetTps = tps;
      } else {
         Log.w("GameEngine", "Nieprawidłowa wartość TPS. TPS musi być dodatnie.");
      }
   }

   @Override
   public void doFrame(long frameTimeNanos) {
      long deltaTime = frameTimeNanos - lastFrameTime;
      double deltaTimeSeconds = deltaTime / 1000000000.0;

      // Akumulujemy czas, aby precyzyjnie kontrolować TPS
      updateAccumulator += deltaTimeSeconds;

      // Wykonujemy aktualizacje gry, dopóki akumulator czasu przekracza interwał aktualizacji
      while (updateAccumulator >= 1.0 / targetTps) {
         gameObject.update(1.0 / targetTps); // Stały deltaTime dla aktualizacji
         updateAccumulator -= 1.0 / targetTps;
         tps++; // Inkrementujemy licznik TPS dla każdej aktualizacji
      }

      render();

      fps = (int) (1000000000.0 / deltaTime);

      // Aktualizujemy TextView tylko raz na sekundę
      if (frameTimeNanos - lastUpdateTime >= 1000000000) {
         if (fpsTpsTextView != null) {
            if (fpsTpsTextView.getHandler() != null) {
               int finalTps = tps;
               fpsTpsTextView.getHandler().post(() -> fpsTpsTextView.setText("FPS: " + fps + ", TPS: " + finalTps));
               tps = 0; // Resetujemy licznik TPS po aktualizacji TextView
            }
         }

         lastUpdateTime = frameTimeNanos;
      }

      lastFrameTime = frameTimeNanos;
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