package org.nodomain.androidgametutorial;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class OrientationData implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    private float[] mAccelOutput;

    private float[] mOrientation;

    public OrientationData() {
        mSensorManager = (SensorManager) Constants.CURRENT_CONTEXT.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mOrientation = new float[3];
    }


    public float[] getOrientation() {
        return mOrientation;
    }

    public void newGame() {

    }

    public void register() {
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    public void pause() {
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            mAccelOutput = event.values;
        }

        if(mAccelOutput != null) {
            System.arraycopy(mAccelOutput, 0, mOrientation, 0, mOrientation.length);
        }
    }

    /* A low pass filter to smooth the sensor response if necessary */
    static final float ALPHA = 0.15f;
    protected float[] lowPass( float[] input, float[] output ) {
        if ( output == null ) return input;

        for ( int i=0; i<input.length; i++ ) {
            output[i] = output[i] + ALPHA * (input[i] - output[i]);
        }
        return output;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
