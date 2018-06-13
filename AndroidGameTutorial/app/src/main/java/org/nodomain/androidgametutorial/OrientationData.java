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

        if(mAccelOutput != null && mMagOutput != null) {
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
        } else if(mAccelOutput != null) { /* No mag sensor */
            double gx;
            double gy;
            double gz;
            float pitch;
            float roll;
            float azimuth;
            float[] rotationMatrix;

            System.out.println("New method");

            gx = mAccelOutput[0] / 9.81f;
            gy = mAccelOutput[1] / 9.81f;
            gz = mAccelOutput[2] / 9.81f;

            pitch = (float) -Math.atan(gy / Math.sqrt(gx * gx + gz * gz));
            roll = (float) -Math.atan(gx / Math.sqrt(gy * gy + gz * gz));
            azimuth = 0; // Impossible to guess

            mMagOutput = new float[3];
            mMagOutput[0] = azimuth;
            mMagOutput[1] = pitch;
            mMagOutput[2] = roll;
            rotationMatrix = getRotationMatrixFromOrientation(mMagOutput);

            SensorManager.getOrientation(rotationMatrix, mAccelOutput);
            if(mStartOrientation == null) {
                mStartOrientation = new float[mOrientation.length];
                System.arraycopy(mOrientation, 0, mStartOrientation, 0, mOrientation.length);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public static float[] getRotationMatrixFromOrientation(float[] o) {
        float[] xM = new float[9];
        float[] yM = new float[9];
        float[] zM = new float[9];

        float sinX = (float) Math.sin(o[1]);
        float cosX = (float) Math.cos(o[1]);
        float sinY = (float) Math.sin(o[2]);
        float cosY = (float) Math.cos(o[2]);
        float sinZ = (float) Math.sin(o[0]);
        float cosZ = (float) Math.cos(o[0]);

        // rotation about x-axis (pitch)
        xM[0] = 1.0f;xM[1] = 0.0f;xM[2] = 0.0f;
        xM[3] = 0.0f;xM[4] = cosX;xM[5] = sinX;
        xM[6] = 0.0f;xM[7] =-sinX;xM[8] = cosX;

        // rotation about y-axis (roll)
        yM[0] = cosY;yM[1] = 0.0f;yM[2] = sinY;
        yM[3] = 0.0f;yM[4] = 1.0f;yM[5] = 0.0f;
        yM[6] =-sinY;yM[7] = 0.0f;yM[8] = cosY;

        // rotation about z-axis (azimuth)
        zM[0] = cosZ;zM[1] = sinZ;zM[2] = 0.0f;
        zM[3] =-sinZ;zM[4] = cosZ;zM[5] = 0.0f;
        zM[6] = 0.0f;zM[7] = 0.0f;zM[8] = 1.0f;

        // rotation order is y, x, z (roll, pitch, azimuth)
        float[] resultMatrix = matrixMultiplication(xM, yM);
        resultMatrix = matrixMultiplication(zM, resultMatrix);
        return resultMatrix;
    }
    public static float[] matrixMultiplication(float[] A, float[] B) {
        float[] result = new float[9];

        result[0] = A[0] * B[0] + A[1] * B[3] + A[2] * B[6];
        result[1] = A[0] * B[1] + A[1] * B[4] + A[2] * B[7];
        result[2] = A[0] * B[2] + A[1] * B[5] + A[2] * B[8];

        result[3] = A[3] * B[0] + A[4] * B[3] + A[5] * B[6];
        result[4] = A[3] * B[1] + A[4] * B[4] + A[5] * B[7];
        result[5] = A[3] * B[2] + A[4] * B[5] + A[5] * B[8];

        result[6] = A[6] * B[0] + A[7] * B[3] + A[8] * B[6];
        result[7] = A[6] * B[1] + A[7] * B[4] + A[8] * B[7];
        result[8] = A[6] * B[2] + A[7] * B[5] + A[8] * B[8];

        return result;
    }
}
