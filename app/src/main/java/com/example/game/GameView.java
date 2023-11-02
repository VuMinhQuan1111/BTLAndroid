package com.example.game;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameView extends SurfaceView implements Runnable {

    private Thread thread;
    private boolean isPlaying, isGameOver = false;
    private int screenX, screenY, score = 0;
    public static float screenRatioX, screenRatioY;
    private Paint paint;
    private FlightAM[] flightAM;
    private Random random;
    private SoundPool soundPool;
    private List<Bullet> bullets;
    private int sound;
    public Flight flight;
    private GameActivity activity;
    private Background background1, background2;

    private Control control1,control2;
    public boolean isDask=false;
    private SharedPreferences prefs;

    private boolean win=false;

    int n=4;
    int target;


    public GameView(GameActivity activity, int screenX, int screenY) {
        super(activity);

        this.activity = activity;

        prefs = activity.getSharedPreferences("game", Context.MODE_PRIVATE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .build();

        } else
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);

        sound = soundPool.load(activity, R.raw.shoot, 1);

        this.screenX = screenX;
        this.screenY = screenY;
        screenRatioX = 1920f / screenX;
        screenRatioY = 1080f / screenY;

        background1 = new Background(screenX, screenY, getResources());
        background2 = new Background(screenX, screenY, getResources());

        control1=new Control(this, screenY, getResources());
        control2=new Control(this, screenY, getResources());

        flight = new Flight(this, screenY, getResources());

        bullets = new ArrayList<>();

        background2.x = screenX;

        paint = new Paint();
        paint.setTextSize(128);
        paint.setColor(Color.WHITE);

        random = new Random();
        n = random.nextInt(7) + 4;

        flightAM = new FlightAM[n];

        for (int i = 0;i <flightAM.length;i++) {

            FlightAM flightam = new FlightAM(getResources());
            flightAM[i] = flightam;

        }

        target= random.nextInt(51) + 50;


    }

    @Override
    public void run() {

        while (isPlaying) {

            update ();
            draw ();
            sleep ();
        }
    }

    private void update () {

        if(score>=target){
            win=true;
            isGameOver=true;
            return;
        }

        control1.x = screenX/7;
        control1.y=screenY/5*4-control1.height;

        control2.x=screenX/7*6-control1.width;
        control2.y=screenY/5*4-control2.height;

        background1.x -= 10 * screenRatioX;
        background2.x -= 10 * screenRatioX;

        if (background1.x + background1.backgroundgame.getWidth() < 0) {
            background1.x = screenX;
        }

        if (background2.x + background2.backgroundgame.getWidth() < 0) {
            background2.x = screenX;
        }

        if (flight.isGoingUp)
            flight.y -= 30 * screenRatioY;
        else
            flight.y += 30 * screenRatioY;

        if (flight.y < 0)
            flight.y = 0;

        if (flight.y >= screenY - flight.height)
            flight.y = screenY - flight.height;

        List<Bullet> trash = new ArrayList<>();

        for (Bullet bullet : bullets) {

            if (bullet.x > screenX)
                trash.add(bullet);

            bullet.x += 50 * screenRatioX;

            for (FlightAM fli : flightAM) {


                if (Rect.intersects(fli.getCollisionShape(),
                        bullet.getCollisionShape())) {

                    score++;
                    fli.x = -500;
                    bullet.x = screenX + 500;
                    fli.wasShot = true;

                }

            }

        }

        for (Bullet bullet : trash)
            bullets.remove(bullet);


        for (FlightAM fliAM : flightAM) {


            fliAM.x -= fliAM.speed;

            if (fliAM.x + fliAM.width < 0) {

                if (!fliAM.wasShot) {
                    win=false;
                    isGameOver = true;
                    return;
                }

                int bound = (int) (30 * screenRatioX);
                fliAM.speed = random.nextInt(bound);

                if (fliAM.speed < 10 * screenRatioX)
                    fliAM.speed = (int) (10 * screenRatioX);

                fliAM.x = screenX;
                fliAM.y = random.nextInt(screenY - fliAM.height);

                fliAM.wasShot = false;
            }

            if (Rect.intersects(fliAM.getCollisionShape(), flight.getCollisionShape())) {
                win=false;
                isGameOver = true;
                return;
            }

        }

    }

    private void draw () {

        if (getHolder().getSurface().isValid()) {

            Canvas canvas = getHolder().lockCanvas();

            if(isDask==false) {

                canvas.drawBitmap(background1.getLight(), background1.x, background1.y, paint);
                canvas.drawBitmap(background2.getLight(), background2.x, background2.y, paint);

            }else {
                canvas.drawBitmap(background1.getDask(), background1.x, background1.y, paint);
                canvas.drawBitmap(background2.getDask(), background2.x, background2.y, paint);
            }

            canvas.drawBitmap(control1.getControl1(),control1.x,control1.y,paint);
            canvas.drawBitmap(control2.getControl2(),control2.x,control2.y,paint);

            for (FlightAM fl : flightAM)
                canvas.drawBitmap(fl.getFlightAM(), fl.x, fl.y, paint);

            canvas.drawText(score + "", screenX / 2f, 164, paint);
            canvas.drawText("Mục tiêu:"+target , screenX/5f , 164, paint);

            if(isGameOver==true && win==true){
                isPlaying = false;
                paint.setColor(Color.GREEN);

                paint.setTextSize(250);
                float textWidth = paint.measureText("Bạn Thắng");

                float x = (screenX - textWidth) / 2;
                float y = screenY / 2f;

                canvas.drawText( "Bạn Thắng",x, y, paint);

                canvas.drawBitmap(flight.getFlight(), flight.x, flight.y, paint);
                getHolder().unlockCanvasAndPost(canvas);
                waitBeforeExiting ();
                return;
            }

            if (isGameOver==true && win==false) {
                isPlaying = false;
                paint.setColor(Color.RED);

                paint.setTextSize(250);
                float textWidth = paint.measureText("Bạn Thua");

                float x = (screenX - textWidth) / 2;
                float y = screenY / 2f;

                canvas.drawText( "Bạn Thua",x, y, paint);

                canvas.drawBitmap(flight.getDead(), flight.x, flight.y, paint);
                getHolder().unlockCanvasAndPost(canvas);
                //saveIfHighScore();
                waitBeforeExiting ();
                return;
            }

            canvas.drawBitmap(flight.getFlight(), flight.x, flight.y, paint);

            for (Bullet bullet : bullets)
                canvas.drawBitmap(bullet.bullet, bullet.x, bullet.y, paint);

            getHolder().unlockCanvasAndPost(canvas);

        }

    }

    private void waitBeforeExiting() {

        try {
            Thread.sleep(3000);
            activity.startActivity(new Intent(activity, MainActivity.class));
            activity.finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void sleep () {
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume () {

        isPlaying = true;
        thread = new Thread(this);
        thread.start();

    }

    public void pause () {

        try {
            isPlaying = false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        Rect x=control1.getCollisionShape();
        Rect x2=control2.getCollisionShape();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(x.contains((int)event.getX(),(int)event.getY())||x2.contains((int)event.getX(),(int)event.getY())){
                    flight.toShoot++;
                }
                break;

        }

        return true;
    }

    public void newBullet() {
        if (!prefs.getBoolean("isMute", false))
           soundPool.play(sound, 1, 1, 0, 0, 1);

        Bullet bullet = new Bullet(getResources());
        bullet.x = flight.x + flight.width;
        bullet.y = flight.y + (flight.height / 2);
        bullets.add(bullet);

    }
}
