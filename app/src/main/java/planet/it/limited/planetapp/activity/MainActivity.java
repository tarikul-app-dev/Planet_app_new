package planet.it.limited.planetapp.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.TextView;

import android.app.AlertDialog;

import java.util.List;
import planet.it.limited.planetapp.R;
import planet.it.limited.planetapp.adapter.ButtonAdapter;
import planet.it.limited.planetapp.database.ContactsDB;
import planet.it.limited.planetapp.detectContact.ContactWatchService;
import planet.it.limited.planetapp.utill.FontCustomization;
import planet.it.limited.planetapp.utill.LanguageUtility;

import static android.Manifest.permission.READ_CONTACTS;
import static planet.it.limited.planetapp.utill.SaveValueSharedPreference.getBoleanValueSharedPreferences;
import static planet.it.limited.planetapp.utill.SaveValueSharedPreference.getValueFromSharedPreferences;
import static planet.it.limited.planetapp.utill.SaveValueSharedPreference.saveBoleanValueSharedPreferences;

import android.support.v7.widget.Toolbar;
public class MainActivity extends AppCompatActivity implements ButtonAdapter.GridViewButtonInterface{

    GridView btnGridView;
    public String[] filesnames = new String[8];

    public Drawable[] drawables = new Drawable[8];
   // public int colors[] = new int[8];

    boolean isReadContacts;
    ContactsDB contactsDB;
    String name = null;
    String phoneNumber = "";
    private static final int REQUEST_READ_CONTACTS = 444;
    Cursor cursor;
    int counter;

    private static final String LOG_TAG = "DPObserver";
    private Handler handler = new Handler();
    //public final static int MY_PERMISSIONS_READ_CONTACTS = 0x1;
    public static boolean checkAutoStartPermission;

    FontCustomization fontCustomization;
    TextView txvUserName,txvBalance;
    AlertDialog b;
    LanguageUtility languageUtility;
    Toolbar toolbar;

    String userName = "";
    String password = " ";
    String retBalance = " ";
    String defaultUserName = "planet user";
    String checkLan = "";


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar_main_dashboard);
        setSupportActionBar(toolbar);
        initViews();
        userName = getValueFromSharedPreferences("user_name",MainActivity.this);
        password = getValueFromSharedPreferences("pass_word",MainActivity.this);

//        if(userName==null && password==null){
//            Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
//            startActivity(intent);
//           // initViews();
//        }else {
//            setContentView(R.layout.activity_main);
//            initViews();
//        }


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if((userName==null && password==null)||(userName.length()<0 && password.length()<0)){
                    Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
                    startActivity(intent);
                    // initViews();
                }

            }
        },10000);// set time as per your requirement

        startContactLookService();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void initViews(){
        languageUtility = new LanguageUtility(MainActivity.this);
        checkLan = getValueFromSharedPreferences("language",MainActivity.this);
        if(checkLan!=null){
            if(checkLan.equals("en")){
                languageUtility.selectLanguage("en");

            }else {
                languageUtility.selectLanguage("bn");

            }
        }
        txvUserName = (TextView)findViewById(R.id.txv_user_main);
        txvBalance = (TextView)findViewById(R.id.txv_balance);
        userName = getValueFromSharedPreferences("user_name",MainActivity.this);

        if(userName!=null && userName.length()>0){
            if(txvUserName!=null){
                txvUserName.setText(userName);
            }

        }

        fontCustomization = new FontCustomization(MainActivity.this);
       // txvUserName.setTypeface(fontCustomization.getHeadLandOne());

        retBalance = getValueFromSharedPreferences("balance",MainActivity.this);
        if(retBalance!=null && retBalance.length()>0){
            if(txvBalance!=null){
                txvBalance.setText(retBalance);
            }

        }

        filesnames[0] = getString(R.string.single_sms);
        filesnames[1] = getString(R.string.contacts_sms);
        filesnames[2] = getString(R.string.file_to_sms);
        filesnames[3] = getString(R.string.settings);
        filesnames[4] = getString(R.string.buy_credit);
        filesnames[5] = getString(R.string.our_services);
        filesnames[6] = getString(R.string.contact_us);
        filesnames[7] = getString(R.string.sms_length_check);

        drawables[0] = this.getResources().getDrawable(R.drawable.ic_sms);
        drawables[1] = this.getResources().getDrawable(R.drawable.ic_contacts);
        drawables[2] = this.getResources().getDrawable(R.drawable.ic_file_to_sms);
        drawables[3] = this.getResources().getDrawable(R.drawable.ic_settings);
        drawables[4] = this.getResources().getDrawable(R.drawable.ic_top_up);
        drawables[5] = this.getResources().getDrawable(R.drawable.ic_services);
        drawables[6] = this.getResources().getDrawable(R.drawable.ic_contact_us);
        drawables[7] = this.getResources().getDrawable(R.drawable.ic_sms_check_length);

//        colors[0] = ContextCompat.getColor(MainActivity.this, R.color.color_white);
//        colors[1] =ContextCompat.getColor(MainActivity.this, R.color.color_white);
//        colors[2] = ContextCompat.getColor(MainActivity.this, R.color.color_white);
//        colors[3] =  ContextCompat.getColor(MainActivity.this, R.color.color_white);
//        colors[4] =  ContextCompat.getColor(MainActivity.this, R.color.color_white);
//        colors[5] =  ContextCompat.getColor(MainActivity.this, R.color.color_white);
//        colors[6] =  ContextCompat.getColor(MainActivity.this, R.color.color_white);
//        colors[7] =  ContextCompat.getColor(MainActivity.this, R.color.color_white);



        btnGridView = (GridView)findViewById(R.id.btn_gridview);

        contactsDB = new ContactsDB(MainActivity.this);
        contactsDB.open();
        isReadContacts = getBoleanValueSharedPreferences("is_read", MainActivity.this);
        if(btnGridView!=null){
            btnGridView.setAdapter(new ButtonAdapter(MainActivity.this,filesnames,this,drawables));
        }

        if (!isReadContacts) {
            getContacts();
        }
        checkAutoStartPermission = getBoleanValueSharedPreferences("auto_start",MainActivity.this);
        if(checkAutoStartPermission==false){
            try {
                Intent intent = new Intent();
                String manufacturer = android.os.Build.MANUFACTURER;
                if ("xiaomi".equalsIgnoreCase(manufacturer)) {
                    intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
                    //checkAutoStartPermission = true;
                    saveBoleanValueSharedPreferences("auto_start",true,MainActivity.this);
                } else if ("oppo".equalsIgnoreCase(manufacturer)) {
                    intent.setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity"));
                    saveBoleanValueSharedPreferences("auto_start",true,MainActivity.this);
                } else if ("vivo".equalsIgnoreCase(manufacturer)) {
                    intent.setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"));
                    saveBoleanValueSharedPreferences("auto_start",true,MainActivity.this);
                }else if ("huawei".equalsIgnoreCase(manufacturer)) {
                    intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity"));
                    saveBoleanValueSharedPreferences("auto_start",true,MainActivity.this);
                }

                List<ResolveInfo> list = MainActivity.this.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                if  (list.size() > 0) {
                    MainActivity.this.startActivity(intent);
                }
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
    }
    public void getContacts() {

        if (!mayRequestContacts()) {
            return;
        }

        String email = null;

        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String _ID = ContactsContract.Contacts._ID;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

        Uri EmailCONTENT_URI = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        String EmailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
        String DATA = ContactsContract.CommonDataKinds.Email.DATA;

        StringBuffer output;

        ContentResolver contentResolver = getContentResolver();

        cursor = contentResolver.query(CONTENT_URI, null, null, null, null);

        // Iterate every contact in the phone
        if (cursor.getCount() > 0) {

            counter = 0;
            while (cursor.moveToNext()) {
                output = new StringBuffer();

                String contact_id = cursor.getString(cursor.getColumnIndex(_ID));
                name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));

                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)));

                if (hasPhoneNumber > 0) {

                    output.append("\n First Name:" + name);

                    //This is to read multiple phone numbers associated with the same contact
                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[]{contact_id}, null);

                    while (phoneCursor.moveToNext()) {
                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                        output.append("\n Phone number:" + phoneNumber);

                    }

                    phoneCursor.close();

                    // Read every email id associated with the contact
                    Cursor emailCursor = contentResolver.query(EmailCONTENT_URI, null, EmailCONTACT_ID + " = ?", new String[]{contact_id}, null);

                    while (emailCursor.moveToNext()) {

                        email = emailCursor.getString(emailCursor.getColumnIndex(DATA));

                        output.append("\n Email:" + email);

                    }

                    emailCursor.close();

                    String columns[] = {
                            ContactsContract.CommonDataKinds.Event.START_DATE,
                            ContactsContract.CommonDataKinds.Event.TYPE,
                            ContactsContract.CommonDataKinds.Event.MIMETYPE,
                    };

                    String where = ContactsContract.CommonDataKinds.Event.TYPE + "=" + ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY +
                            " and " + ContactsContract.CommonDataKinds.Event.MIMETYPE + " = '" + ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE + "' and " + ContactsContract.Data.CONTACT_ID + " = " + contact_id;

                    String[] selectionArgs = null;
                    String sortOrder = ContactsContract.Contacts.DISPLAY_NAME;

                    Cursor birthdayCur = contentResolver.query(ContactsContract.Data.CONTENT_URI, columns, where, selectionArgs, sortOrder);
                    Log.d("BDAY", birthdayCur.getCount() + "");
                    if (birthdayCur.getCount() > 0) {
                        while (birthdayCur.moveToNext()) {
                            String birthday = birthdayCur.getString(birthdayCur.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE));
                            output.append("Birthday :" + birthday);
                            Log.d("BDAY", birthday);
                        }
                    }
                    birthdayCur.close();
                }


                // Add the contact to the ArrayList
                String phNumber = getMeMyNumber(phoneNumber);
                contactsDB.saveAllContacts(name, phNumber);
                saveBoleanValueSharedPreferences("is_read", true, MainActivity.this);
            }


        }
    }
    public static String getMeMyNumber(String number)
    {
        String out = number.replaceAll("[^0-9]", "") ;       //remove all the non numbers (brackets dashes spaces etc.) except the + signs

        return out;

    }
    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }
    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getContacts();
            }
        }
    }

    @Override
    public void getGridButtonPosition(int position) {
        if(position==0){
            Intent intent = new Intent(MainActivity.this,SingleSMSActivity.class);
            startActivity(intent);
            // ActivityCompat.finishAffinity(MainActivity.this);
        }
      else if(position==1){
            Intent intent = new Intent(MainActivity.this,ContactsToSMSActivity.class);
            startActivity(intent);
//            // ActivityCompat.finishAffinity(MainActivity.this);
        }
        else if(position==2){
            Intent intent = new Intent(MainActivity.this,FileToSMSActivity.class);
            startActivity(intent);
        }
        else if(position==3){

            Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
            startActivity(intent);
            //  ActivityCompat.finishAffinity(MainActivity.this);
        }else if(position==4){

            Intent intent = new Intent(MainActivity.this,BuyCreditActivity.class);
            startActivity(intent);
            //  ActivityCompat.finishAffinity(MainActivity.this);
        }
        else if(position==5){

            Intent intent = new Intent(MainActivity.this,ServicesActivity.class);
            startActivity(intent);
            //  ActivityCompat.finishAffinity(MainActivity.this);
        }
        else if(position==6){

            Intent intent = new Intent(MainActivity.this,ContactUsActivity.class);
            startActivity(intent);
            //  ActivityCompat.finishAffinity(MainActivity.this);
        }else if(position==7){

            Intent intent = new Intent(MainActivity.this,SMSLengthActivity.class);
            startActivity(intent);
            //  ActivityCompat.finishAffinity(MainActivity.this);
        }

    }
    private void startContactLookService() {
        try {
            if (ActivityCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.READ_CONTACTS)
                    == PackageManager.PERMISSION_GRANTED) {//Checking permission
                //Starting service for registering ContactObserver
                Intent intent = new Intent(MainActivity.this, ContactWatchService.class);
                startService(intent);
            } else {
                //Ask for READ_CONTACTS permission
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_READ_CONTACTS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        String userName = getValueFromSharedPreferences("user_name",MainActivity.this); ;

        if(userName!=null){
            if(txvUserName!=null){
                txvUserName.setText(userName);
            }

        }
        retBalance = getValueFromSharedPreferences("balance",MainActivity.this);
        if(retBalance!=null && retBalance.length()>0){
            if(txvBalance!=null){
                txvBalance.setText(retBalance);
            }

        }
        checkLan = getValueFromSharedPreferences("language",MainActivity.this);
        if(checkLan!=null){
            if(checkLan.equals("en")){
                languageUtility.selectLanguage("en");

            }else {
                languageUtility.selectLanguage("bn");

            }
        }
    }


}
