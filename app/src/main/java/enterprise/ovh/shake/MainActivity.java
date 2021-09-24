package enterprise.ovh.shake;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private SensorManager sm;

    private float acelVal; // aktualna wartość akceleracji i grawitacji
    private float acelLast; // ostatnia znana wartość akceleracji i grawitacji
    private float shake; // acceleration value differ from gravity
    private static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 1; // cyfrą może być dowolna liczna dodatnia

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sm.registerListener(sensorListener, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);

        acelVal = SensorManager.GRAVITY_EARTH;
        acelLast = SensorManager.GRAVITY_EARTH;
        shake = 0.00f;

    }

    private final SensorEventListener sensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) { // trzeba zmienić ręcznie drugi prametr na sensorEvent

            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            acelLast = acelVal;
            acelVal = (float) Math.sqrt((double) (x * x + y * y + z * z));
            float delta = acelVal - acelLast;
            shake = shake * 0.9f + delta;

            EditText editText = (EditText) findViewById(R.id.editText);
            String numer = (editText. getText(). toString());
            Button button = (Button) findViewById(R.id.button);

            if (shake > 1.3) {

                String phone = numer;
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", phone, null));
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE},MAKE_CALL_PERMISSION_REQUEST_CODE);
                }
                else
                {
                    startActivity(intent);
                }
                //Toast.makeText(MainActivity.this, "działa shake", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

}
