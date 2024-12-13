package com.example.simplegameloopandroidapp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

public class GameObject {

   private float x;
   private float y;
   private float speedX; // Speed in pixels per second
   private float speedY; // Speed in pixels per second
   private float rotationAngle;
   private float rotationDirection = 100f;

   private Bitmap bitmap;
   private int screenWidth;
   private int screenHeight;

   public GameObject(Bitmap bitmap, int screenWidth, int screenHeight) {
      this.bitmap = bitmap;
      this.screenWidth = screenWidth;
      this.screenHeight = screenHeight;

      x = 100;
      y = 100;
      speedX = 150; // Assuming these are meant to be pixels per second
      speedY = 140; // Assuming these are meant to be pixels per second
   }

   public void update(double deltaTime) {
      // Update position based on speed and deltaTime
      x += speedX * deltaTime;
      y += speedY * deltaTime;
      //Log.d("GameObject", "Update called! deltaTime: " + deltaTime); // Dodaj ten log

      // Check for collisions and bounce (update speedX and speedY)
      if (x + bitmap.getWidth() > screenWidth || x < 0) {
         speedX = -speedX;
         rotationDirection = -rotationDirection;
         //Log.d("GameObject", "x after update: " + x); // Log po zmianie x

      }
      if (y + bitmap.getHeight() > screenHeight || y < 0) {
         speedY = -speedY;
         rotationDirection = -rotationDirection;
      }

      // Update rotation
      rotationAngle += 2f * rotationDirection * deltaTime; // Scale rotation by deltaTime
      if (rotationAngle >= 360f) {
         rotationAngle = 0f;
      }
   }

   public void draw(Canvas canvas) {
      canvas.save();
      canvas.rotate(rotationAngle, x + bitmap.getWidth() / 2f, y + bitmap.getHeight() / 2f);
      canvas.drawBitmap(bitmap, x, y, null);
      canvas.restore();
   }
}