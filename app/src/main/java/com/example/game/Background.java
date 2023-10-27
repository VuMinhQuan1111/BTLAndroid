package com.example.game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Background {
    int x = 0, y = 0;
    Bitmap backgroundgame;

    Background (int screenX, int screenY, Resources res) {

        backgroundgame = BitmapFactory.decodeResource(res, R.drawable.backgroundgame);
        backgroundgame = Bitmap.createScaledBitmap(backgroundgame, screenX, screenY, false);

    }
}
