package com.example.simplegameloopandroidapp;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class GameObject {

   private float x; // Położenie obiektu na osi X
   private float y; // Położenie obiektu na osi Y
   private float speedX; // Prędkość na osi X w pikselach na sekundę
   private float speedY; // Prędkość na osi Y w pikselach na sekundę
   private float rotationAngle; // Kąt obrotu obiektu
   private float rotationDirection = 200f; // Kierunek obrotu (stopnie na sekundę)

   private Bitmap bitmap; // Grafika obiektu
   private int screenWidth; // Szerokość ekranu
   private int screenHeight; // Wysokość ekranu

   public GameObject(Bitmap bitmap, int screenWidth, int screenHeight) {
      this.bitmap = bitmap;
      this.screenWidth = screenWidth;
      this.screenHeight = screenHeight;

      y = 150; // Początkowa pozycja Y
      x = 150; // Początkowa pozycja X
      speedX = 170; // Prędkość początkowa na osi X (zakładamy, że w pikselach na sekundę)
      speedY = 170; // Prędkość początkowa na osi Y (zakładamy, że w pikselach na sekundę)
   }

   public void update(double deltaTime) {
      // Aktualizacja pozycji na podstawie prędkości i czasu delta
      x += speedX * deltaTime;
      y += speedY * deltaTime;

      // Dodatkowy log dla celów debugowania (można usunąć)
      // Log.d("GameObject", "Update wywołana! deltaTime: " + deltaTime);

      // Sprawdzanie kolizji i odbicie (aktualizacja speedX i speedY)
      if (x + bitmap.getWidth() > screenWidth || x < 0) {
         speedX = -speedX; // Odwrócenie kierunku ruchu w poziomie
         rotationDirection = -rotationDirection; // Odwrócenie kierunku obrotu

         // Dodatkowy log dla celów debugowania (można usunąć)
         // Log.d("GameObject", "x po aktualizacji: " + x);
      }
      if (y + bitmap.getHeight() > screenHeight || y < 0) {
         speedY = -speedY; // Odwrócenie kierunku ruchu w pionie
         rotationDirection = -rotationDirection; // Odwrócenie kierunku obrotu
      }

      // Aktualizacja obrotu
      rotationAngle += 2f * rotationDirection * deltaTime; // Skalowanie obrotu przez deltaTime
      if (rotationAngle >= 360f) {
         rotationAngle = 0f;
      }
   }

   public void draw(Canvas canvas) {
      canvas.save(); // Zapis stanu Canvas przed rotacją
      canvas.rotate(rotationAngle, x + bitmap.getWidth() / 2f, y + bitmap.getHeight() / 2f); // Rotacja wokół środka obiektu
      canvas.drawBitmap(bitmap, x, y, null); // Rysowanie bitmapy na Canvas
      canvas.restore(); // Przywrócenie stanu Canvas
   }
}