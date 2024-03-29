package planet.it.limited.planetapp.detectContact;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.provider.ContactsContract;
import android.widget.Toast;

public class ContactWatchService extends Service {
    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            try {
                //Register contact observer
                startContactObserver();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void startContactObserver(){
        try{
          //  Toast.makeText(getApplicationContext(),"ContactWatchService Started",Toast.LENGTH_SHORT).show();
            //Registering contact observer
            getApplication().getContentResolver().registerContentObserver(ContactsContract.Contacts.CONTENT_URI, true,new MyContentObserver(new Handler(),getApplicationContext()));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);

        // If we get killed, after returning from here, restart
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        try{
            //Code below is commented.
            //Turn it on if you want to run your service even after your app is closed
            /*Intent intent=new Intent(getApplicationContext(), ContactWatchService.class);
            startService(intent);*/
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
