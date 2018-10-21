package ka.masato.sample;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import com.google.android.things.pio.PeripheralManager;
import com.google.android.things.userdriver.UserDriverManager;
import com.google.android.things.userdriver.sensor.UserSensor;
import ka.masato.grideyelib.GridEyeManager;

import java.io.IOException;

import static java.lang.System.exit;

public class SensorDrierService extends Service {


    private static final String I2C_PORT = "I2C1";
    private static final int ADRESS = 0x68;
    private static final String LOG_TAG = "SensorDriverService";

    private UserSensor userSensor = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        UserDriverManager userDriverManager = UserDriverManager.getInstance();
        PeripheralManager mPeripheralManager = PeripheralManager.getInstance();
        GridEyeManager gridEyeManager = new GridEyeManager(mPeripheralManager, I2C_PORT, ADRESS);

        try {
            userSensor = gridEyeManager.getUserSensor();
            userDriverManager.registerSensor(userSensor);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Failed open sensor.");
            exit(-1);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        UserDriverManager userDriverManager = UserDriverManager.getInstance();
        userDriverManager.unregisterSensor(userSensor);
    }

}
