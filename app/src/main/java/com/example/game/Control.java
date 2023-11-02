package com.example.game;

import static com.example.game.GameView.screenRatioX;
import static com.example.game.GameView.screenRatioY;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Control {
    //int toShoot = 0; // BẮn
    //boolean isGoingUp ; //Đang đi lên
    //boolean tthai=true;
    int x, y, width, height;//x,y: Trục tung/Hoành, Dài rộng , Bộ đếm chim, Đếm Bắn
    Bitmap wing1,wing2; // Ảnh máy bay, Ảnh bắn đạn , Ảnh Chết
    private GameView gameView; // Dối tượng Game View

    // KHỞI TẠO
    Control(GameView gameView, int screenY, Resources res) {

        //Khởi tạo Game View
        this.gameView = gameView;

        //Tạo các đối tượng Bitmap từ nhiều nguồn khác nhau, bao gồm tệp, luồng và mảng byte.
        wing1 = BitmapFactory.decodeResource(res, R.drawable.control1);
        wing2 = BitmapFactory.decodeResource(res, R.drawable.control2);

        //Lấy chiều rộng, chiều cao
        width = wing1.getWidth();
        height = wing1.getHeight();

        //Chia nhỏ chiều rộng chiều cao
        //width /= 8;
        //height /=8;

        width /= 12;
        height /=12;

        //Nhân với tỷ lệ màn hình ở gameview
        width = (int) (width * screenRatioX);
        // width = (int) screenRatioX;
        height = (int) (height * screenRatioY);
        //height = (int) GameView.screenRatioY;

        //Tạo một bitmap mới, được chia tỷ lệ từ bitmap hiện có, khi có thể.
        wing1 = Bitmap.createScaledBitmap(wing1, width, height, false);
        wing2 = Bitmap.createScaledBitmap(wing2, width, height, false);


        //????
        //y = screenY / 2;
        //x = (int) (64 * screenRatioX);

    }

    Bitmap getControl1(){return wing1;}
    Bitmap getControl2(){return wing2;}

    Rect getCollisionShape () {
        return new Rect(x, y, x + width, y + height);
    }
}
