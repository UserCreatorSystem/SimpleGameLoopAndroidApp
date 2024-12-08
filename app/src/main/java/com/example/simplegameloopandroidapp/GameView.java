package com.example.simplegameloopandroidapp;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

   private GameEngine gameEngine;
   private GameObject gameObject;
   private TextView fpsTpsTextView;

   public GameView(Context context, AttributeSet attrs) {
      super(context, attrs);
      getHolder().addCallback(this);
      setFocusable(true);
   }

   @Override
   public void surfaceCreated(SurfaceHolder holder) {
      gameObject = new GameObject(BitmapFactory.decodeResource(getResources(), R.drawable.stone_32x32), getWidth(), getHeight());
      gameEngine = new GameEngine(holder, gameObject, fpsTpsTextView);
      gameEngine.start();
      Log.d("GameView", "GameEngine started");
   }

   @Override
   public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
      // (Pusta implementacja)
   }

   @Override
   public void surfaceDestroyed(SurfaceHolder holder) {
      gameEngine.stop();
   }

   public void setFpsTpsTextView(TextView fpsTpsTextView) {
      this.fpsTpsTextView = fpsTpsTextView;
      Log.d("GameEngine", "fpsTpsTextView set: " + (fpsTpsTextView != null));
   }
}
