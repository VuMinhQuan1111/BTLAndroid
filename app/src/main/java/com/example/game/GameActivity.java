package com.example.game;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity implements SensorEventListener {

    private GameView gameView;

    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private Sensor lightSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if (sensorManager != null) {
            accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
            lightSensor =sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        }

        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);

        gameView = new GameView(this, point.x, point.y);

        setContentView(gameView);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }

        gameView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (accelerometerSensor != null) {
            sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);

        }
        if (lightSensor != null) {
            sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);

        }

        gameView.resume();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            float valuez=event.values[2];
            float valuex=event.values[1];
            if (valuez > 0.5f || valuex > 0) {
                gameView.flight.isGoingUp=true;

            } else if (valuez < -0.5f || valuex < 0) {
                gameView.flight.isGoingUp=false;

            }
            else if(valuez > -0.5f && valuez < 0.5f){
                //gameView.flight.x= gameView.screenX-gameView.flight.height;
            }
        }
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            float lightValue = event.values[0];
            if (lightValue < 200f) {

                gameView.isDask=true;

            } else {
                gameView.isDask=false;

            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}