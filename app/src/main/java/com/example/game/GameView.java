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

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    //private boolean win=false;

    private int n=4;
    //private int target;

    private volatile boolean currentState = true;
    private static final long STATE_DURATION = 15000; // 10 seconds
    private static final long SWITCH_DELAY = 1000; // 2 seconds
    private int Levelofdifficult=5;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Score");


    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    boolean check=false;


    public GameView(Context context){
        super(context);
    }

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
        n = random.nextInt(3) + 3;

        flightAM = new FlightAM[n];

        for (int i = 0;i <flightAM.length;i++) {

            FlightAM flightam = new FlightAM(getResources());
            flightAM[i] = flightam;

        }

        //target= random.nextInt(51) + 50;


    }

    @Override
    public void run() {

        long startTime = System.currentTimeMillis();
        long switchTime = startTime + STATE_DURATION;

        while (isPlaying) {
            long currentTime = System.currentTimeMillis();
            if (currentTime >= switchTime) {
                currentState = !currentState;
                if(currentState==false){
                    Levelofdifficult+=5;
                }
                switchTime = currentTime + (currentState ? STATE_DURATION : SWITCH_DELAY);
            }

            update ();
            draw ();
            sleep ();
        }
    }

    private void update () {

        /*if(score>=target){
            win=true;
            isGameOver=true;
            return;
        }*/

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
                    //win=false;
                    isGameOver = true;
                    return;
                }

                fliAM.speed=Levelofdifficult;

                fliAM.x = screenX;
                fliAM.y = random.nextInt(screenY - fliAM.height);

                fliAM.wasShot = false;
            }

            if (Rect.intersects(fliAM.getCollisionShape(), flight.getCollisionShape())) {
                //win=false;
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


            paint.setTextSize(100);
            paint.setColor(Color.GREEN);
            paint.setFakeBoldText(true);
            canvas.drawText(score + "", screenX / 2f, 100, paint);


            if (isGameOver==true) {
                isPlaying = false;
                paint.setColor(Color.GREEN);

                paint.setTextSize(100);
                float textWidth = paint.measureText("Bạn đã được số điểm: ");

                float x = (screenX - textWidth) / 2;
                float y = screenY / 2f;

                canvas.drawText( "Bạn đã được số điểm: "+score,x, y, paint);

                canvas.drawBitmap(flight.getDead(), flight.x, flight.y, paint);
                getHolder().unlockCanvasAndPost(canvas);
                //saveIfHighScore();

                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();


                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(snapshot.exists()){
                            for (DataSnapshot snapshott : snapshot.getChildren()) {
                                ScoreUser usersc = snapshott.getValue(ScoreUser.class);
                                if(usersc.idus.equals(firebaseUser.getUid())){
                                    if(score>usersc.score){
                                        usersc.setScore(score);
                                        // Thực hiện cập nhật
                                        myRef.child(snapshott.getKey()).setValue(usersc);
                                    }
                                    check=true;
                                    break;
                                }
                                else{
                                    check=false;
                                }
                            }
                        }

                        /*if(check==false){
                            String userIdSc = myRef.push().getKey();
                            // creating user object
                            Score usersc = new Score(firebaseUser.getUid(), score);

                            // pushing user to 'users' node using the userId
                            myRef.child(userIdSc).setValue(usersc);
                        }*/
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

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
//private void waitBeforeExiting() {
//    try {
//        Thread.sleep(3000);
//        Intent intent = new Intent(activity, Bonus.class);
//        activity.startActivity(intent);
//        activity.finish();
//    } catch (InterruptedException e) {
//        e.printStackTrace();
//    }
//}


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

