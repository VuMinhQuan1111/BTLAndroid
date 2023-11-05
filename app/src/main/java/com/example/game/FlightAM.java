package com.example.game;

import static com.example.game.GameView.screenRatioX;
import static com.example.game.GameView.screenRatioY;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class FlightAM {
    public int speed = 2; // Tốc độ
    public boolean wasShot = true; // Bị bắn
    int x = 0, y, width, height, birdCounter = 1; //Dài, Rộng, Số Chim
    Bitmap fl1; // Ảnh chim

    // KHỞI TẠO
    FlightAM(Resources res) {
        //Tạo các đối tượng Bitmap từ nhiều nguồn khác nhau, bao gồm tệp, luồng và mảng byte.
        fl1 = BitmapFactory.decodeResource(res, R.drawable.fly3);


        //Lấy chiều rộng, chiều cao
        width = fl1.getWidth();
        height = fl1.getHeight();

        //Chia nhỏ chiều rộng chiều cao
        width /= 15;
        height /= 15;

        //Nhân với tỷ lệ màn hình ở gameview
        width = (int) (width * screenRatioX);
        height = (int) (height * screenRatioY);

        //Tạo một bitmap mới, được chia tỷ lệ từ bitmap hiện có, khi có thể.
        fl1 = Bitmap.createScaledBitmap(fl1, width, height, false);


        //???
        y = -height;
    }

    // LẤY ĐỐI TƯỢNG
    Bitmap getFlightAM () {

        //Nếu không thì số chim =1 ==> Trả về Ảnh 4
        birdCounter = 1;

        return fl1;
    }

    /*Rect chứa bốn tọa độ nguyên cho một hình chữ nhật.
    Hình chữ nhật được biểu thị bằng tọa độ 4 cạnh của nó (trái, trên, dưới phải).
    Những trường này có thể được truy cập trực tiếp. Sử dụng width() và Height() để lấy chiều rộng và chiều cao của hình chữ nhật.
    Lưu ý: hầu hết các phương pháp không kiểm tra xem tọa độ có được sắp xếp chính xác hay không
    (tức là trái <= phải và trên cùng <= dưới cùng).

    Lưu ý rằng tọa độ bên phải và dưới cùng là độc quyền.
    Điều này có nghĩa là một Rect được vẽ chưa được chuyển đổi thành a Canvas sẽ vẽ vào cột và hàng
    được mô tả bằng tọa độ bên trái và trên cùng của nó, chứ không phải tọa độ dưới cùng và bên phải của nó.*/

    //-> Hình dạng va chạm
    Rect getCollisionShape () {
        return new Rect(x, y, x + width, y + height);
    }

}
