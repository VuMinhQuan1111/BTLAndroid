package com.example.game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Background {
    int x = 0, y = 0;
    Bitmap backgroundgame, backgroundgamedask ; // Đối tượng Bitmap--> Ảnh Background


    // KHỞI TẠO
    Background (int screenX, int screenY, Resources res) {


        backgroundgame = BitmapFactory.decodeResource(res, R.drawable.backgroundgame);
        //Tạo các đối tượng Bitmap từ nhiều nguồn khác nhau, bao gồm tệp, luồng và mảng byte.

        backgroundgame = Bitmap.createScaledBitmap(backgroundgame, screenX, screenY, false);
        //Tạo một bitmap mới, được chia tỷ lệ từ bitmap hiện có, khi có thể.

        backgroundgamedask = BitmapFactory.decodeResource(res, R.drawable.backgrounddark);
        //Tạo các đối tượng Bitmap từ nhiều nguồn khác nhau, bao gồm tệp, luồng và mảng byte.

        backgroundgamedask = Bitmap.createScaledBitmap(backgroundgamedask, screenX, screenY, false);
        //Tạo một bitmap mới, được chia tỷ lệ từ bitmap hiện có, khi có thể.

    }

    Bitmap getDask() {
        return backgroundgamedask;
    }

    Bitmap getLight() {
        return backgroundgame;
    }

}
