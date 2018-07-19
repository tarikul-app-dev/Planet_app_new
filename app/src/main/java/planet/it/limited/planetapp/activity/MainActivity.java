package planet.it.limited.planetapp.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import planet.it.limited.planetapp.R;
import planet.it.limited.planetapp.adapter.ButtonAdapter;
import planet.it.limited.planetapp.database.ContactsDB;

import static android.Manifest.permission.READ_CONTACTS;
import static planet.it.limited.planetapp.utill.SaveValueSharedPreference.getBoleanValueSharedPreferences;
import static planet.it.limited.planetapp.utill.SaveValueSharedPreference.saveBoleanValueSharedPreferences;

public class MainActivity extends AppCompatActivity implements ButtonAdapter.GridViewButtonInterface{
    GridView btnGridView;
    public String[] filesnames = {
            "Single SMS",
            "Contacts To SMS",
            "File To SMS",
            "Settings",
            "Account TopUp"

    };
    public Drawable[] drawables = new Drawable[5];
    public int colors[] = new int[5];

    boolean isReadContacts;
    ContactsDB contactsDB;
    String name = null;
    String phoneNumber = "";
    private static final int REQUEST_READ_CONTACTS = 444;
    Cursor cursor;
    int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }


    public void initViews(){
        drawables[0] = this.getResources().getDrawable(R.drawable.ic_sms);
        drawables[1] = this.getResources().getDrawable(R.drawable.ic_contacts);
        drawables[2] = this.getResources().getDrawable(R.drawable.ic_file_to_sms);
        drawables[3] = this.getResources().getDrawable(R.drawable.ic_settings);
        drawables[4] = this.getResources().getDrawable(R.drawable.ic_top_up);

        colors[0] = ContextCompat.getColor(MainActivity.this, R.color.md_teal_600);
        colors[1] =ContextCompat.getColor(MainActivity.this, R.color.color_contacts);
        colors[2] = ContextCompat.getColor(MainActivity.this, R.color.color_file_to_sms);
        colors[3] =  ContextCompat.getColor(MainActivity.this, R.color.color_settings);
        colors[4] =  ContextCompat.getColor(MainActivity.this, R.color.color_top_up);
        btnGridView = (GridView)findViewById(R.id.btn_gridview);

        contactsDB = new ContactsDB(MainActivity.this);
        contactsDB.open();
        isReadContacts = getBoleanValueSharedPreferences("is_read", MainActivity.this);
        btnGridView.setAdapter(new ButtonAdapter(MainActivity.this,filesnames,this,drawables,colors));
        if (!isReadContacts) {
            getContacts();
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
        }

        //else if(position==4){
//            Intent intent = new Intent(MainActivity.this,PeopleActivity.class);
//            startActivity(intent);
//            // ActivityCompat.finishAffinity(MainActivity.this);
//        }
    }
}
