package ka.masato.irarraysensorlib;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AMG8833SensorService extends IntentService {

    public static final String INTENT_SERVICE_NAME = "AMG8833SensorService";

    public AMG8833SensorService() {
        super(INTENT_SERVICE_NAME);
    }

    @Override
    public void onCreate() {


        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try{
            Thread.sleep(5000);
        }catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }
}
