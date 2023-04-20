package pl.edu.pbs.apka10_smsy.ui.home;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel implements SensorEventListener {

    private final MutableLiveData<String> mText;
    private final SensorManager mSensorManager;
    private final Sensor mRotationSensor;

    public HomeViewModel(Context context) {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mRotationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
    }

    public LiveData<String> getText() {
        return mText;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == mRotationSensor) {
            float[] rotationMatrix = new float[9];
            float[] orientationAngles = new float[3];
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);
            SensorManager.getOrientation(rotationMatrix, orientationAngles);
            float azimuth = (float) Math.toDegrees(orientationAngles[0]);
            mText.postValue("Azimuth: " + azimuth);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not used
    }

    public void registerListener() {
        mSensorManager.registerListener(this, mRotationSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void unregisterListener() {
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onCleared() {
        unregisterListener();
        super.onCleared();
    }
}
