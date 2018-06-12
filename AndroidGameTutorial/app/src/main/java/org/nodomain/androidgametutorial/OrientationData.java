package org.nodomain.androidgametutorial;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.List;

public class OrientationData implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mMagnometer;

    private float[] mAccelOutput;
    public float[] mMagOutput;

    private float[] mOrientation;
    private float[] mStartOrientation;

    public OrientationData() {
        mStartOrientation = null;

        mSensorManager = (SensorManager) Constants.CURRENT_CONTEXT.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MOTION_DETECT);

        mOrientation = new float[3];
    }


    public float[] getOrientation() {
        return mOrientation;
    }

    public float[] getStartOrientation() {
        return mStartOrientation;
    }

    public void newGame() {
        mStartOrientation = null;
    }

    public void register() {
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this, mMagnometer, SensorManager.SENSOR_DELAY_GAME);
    }

    public void pause() {
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            mAccelOutput = event.values;
        } else if(event.sensor.getType() == Sensor.TYPE_MOTION_DETECT) {
            mMagOutput = event.values;
        }

        List<Sensor> deviceSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        for(Sensor sensor: deviceSensors) {
            System.out.println("Sensor: " + sensor.getName());
        }

        if(mAccelOutput != null && mMagOutput != null) {
            System.out.println("Sensor Changed");
            float[] R;
            float[] I;
            boolean success;

            R = new float[9];
            I = new float[9];
            success = SensorManager.getRotationMatrix(R, I, mAccelOutput, mMagOutput);

            if(success) {
                SensorManager.getOrientation(R, mAccelOutput);
                if(mStartOrientation == null) {
                    mStartOrientation = new float[mOrientation.length];
                    System.arraycopy(mOrientation, 0, mStartOrientation, 0, mOrientation.length);
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
