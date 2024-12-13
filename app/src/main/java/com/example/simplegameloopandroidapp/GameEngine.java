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
   private long lastUpdateTime = 0;

   public GameEngine(SurfaceHolder surfaceHolder, GameObject gameObject, TextView fpsTpsTextView) {
      this.surfaceHolder = surfaceHolder;
      this.gameObject = gameObject;
      this.fpsTpsTextView = fpsTpsTextView;
   }

   public void start() {
      lastFrameTime = System.nanoTime();
      Choreographer.getInstance().postFrameCallback(this);
   }



   public void stop(){
      Choreographer.getInstance().removeFrameCallback(this);
   }

   @Override
   public void doFrame(long frameTimeNanos) {
      long deltaTime = frameTimeNanos - lastFrameTime; // Oblicz deltaTime w nanosekundach

      // Przelicz deltaTime na sekundy (dla wygody obliczeń)
      double deltaTimeSeconds = deltaTime / 1000000000.0;
      //Log.d("GameEngine", "deltaTime: " + deltaTimeSeconds); // Dodaj ten log

      gameObject.update(deltaTimeSeconds); // PRZEKAZUJEMY deltaTime do aktualizacji!

      render();
      fps = (int) (1000000000.0 / deltaTime);

      if (frameTimeNanos - lastUpdateTime >= 1000000000) {
         updateTextView();
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
   private void updateTextView() {
      if (fpsTpsTextView != null) {
         if (fpsTpsTextView.getHandler() != null) {
            fpsTpsTextView.getHandler().post(() -> fpsTpsTextView.setText("FPS: " + fps));
         }

      }
   }
}
