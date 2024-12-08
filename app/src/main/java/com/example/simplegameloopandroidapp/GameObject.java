package com.example.simplegameloopandroidapp;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class GameObject {

   private int x;
   private int y;
   private int speedX;
   private int speedY;
   private float rotationAngle;
   private float rotationDirection = 1f;

   private Bitmap bitmap;
   private int screenWidth;
   private int screenHeight;

   public GameObject(Bitmap bitmap, int screenWidth, int screenHeight) {
      this.bitmap = bitmap;
      this.screenWidth = screenWidth;
      this.screenHeight = screenHeight;

      x = 100;
      y = 100;
      speedX = 15;
      speedY = 14;
   }

   public void update() {
      x += speedX;
      y += speedY;

      if (x + bitmap.getWidth() > screenWidth || x < 0) {
         speedX = -speedX;
         rotationDirection = -rotationDirection;
      }
      if (y + bitmap.getHeight() > screenHeight || y < 0) {
         speedY = -speedY;
         rotationDirection = -rotationDirection;
      }

      rotationAngle += 2f * rotationDirection;
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
