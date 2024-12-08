package com.example.simplegameloopandroidapp;

import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Choreographer;
import android.view.SurfaceHolder;
import android.widget.TextView;

public class GameEngine implements Choreographer.FrameCallback {

   private static final int TARGET_TPS = 60;
   private static final int MAX_FPS = 60; // Maksymalna liczba FPS
   private static final long TPS_PERIOD = 1000000000 / TARGET_TPS; // Czas na jedną aktualizację w nanosekundach
   private static final long FRAME_PERIOD = 1000000000 / MAX_FPS; // Czas na jedną klatkę w nanosekundach
   private SurfaceHolder surfaceHolder;
   private boolean isRunning = false;
   private int fps;
   private int tps;
   private long lastTpsUpdateTime = 0;
   private long lastUpdateTextTime = 0;
   private long lastFrameTime = 0;
   private GameObject gameObject;
   private TextView fpsTpsTextView;
   private Handler uiHandler = new Handler(Looper.getMainLooper(), msg -> true);

   public GameEngine(SurfaceHolder surfaceHolder, GameObject gameObject, TextView fpsTpsTextView) {
      this.surfaceHolder = surfaceHolder;
      this.gameObject = gameObject;
      this.fpsTpsTextView = fpsTpsTextView;
   }

   public void start() {
      isRunning = true;
      lastFrameTime = System.nanoTime();
      lastTpsUpdateTime = lastFrameTime;
      Choreographer.getInstance().postFrameCallback(this);
    //  Log.d("GameEngine", "Choreographer postFrameCallback called");
   }

   public void stop() {
      isRunning = false;
      Choreographer.getInstance().removeFrameCallback(this);
      //Log.d("GameEngine", "Choreographer removeFrameCallback called");
   }

   @Override
   public void doFrame(long frameTimeNanos) {
      if (isRunning) {
         long currentTimeNanos = System.nanoTime();
         long elapsedFrameTime = currentTimeNanos - lastFrameTime;
         long elapsedTpsTime = currentTimeNanos - lastTpsUpdateTime;

         // Aktualizacja obiektu gry, jeśli minęło wystarczająco czasu
         if (elapsedTpsTime >= TPS_PERIOD) {
            gameObject.update();
            tps = (int) (1000000000.0 / elapsedTpsTime); // Obliczanie TPS
            lastTpsUpdateTime = currentTimeNanos;
         }

         // Renderowanie, jeśli minęło wystarczająco czasu
         if (elapsedFrameTime >= FRAME_PERIOD) {
            render();
            fps = (int) (1000000000.0 / elapsedFrameTime); // Obliczanie FPS
            lastFrameTime = currentTimeNanos;
         }

         // Aktualizacja TextView co sekundę
         if (currentTimeNanos - lastUpdateTextTime >= 1000000000) {
            uiHandler.post(() -> {
               if (fpsTpsTextView != null) {
        //          Log.d("GameEngine", "Updating fpsTpsTextView: FPS: " + fps + ", TPS: " + tps);
                  fpsTpsTextView.setText("FPS: " + fps + ", TPS: " + tps);
               } else {
          //        Log.d("GameEngine", "fpsTpsTextView is null");
               }
            });
            lastUpdateTextTime = currentTimeNanos;
         }

         // Rejestracja następnej ramki
         Choreographer.getInstance().postFrameCallback(this);
      }
   }

   private void render() {
      if (!surfaceHolder.getSurface().isValid()) {
         return;
      }

      Canvas canvas = surfaceHolder.lockCanvas();
      canvas.drawColor(Color.BLACK);
      gameObject.draw(canvas);
      surfaceHolder.unlockCanvasAndPost(canvas);
   }
}
