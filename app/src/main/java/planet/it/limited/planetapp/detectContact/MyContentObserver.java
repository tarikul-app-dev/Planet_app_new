package planet.it.limited.planetapp.detectContact;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import planet.it.limited.planetapp.activity.MainActivity;
import planet.it.limited.planetapp.database.ContactsDB;

import static android.Manifest.permission.READ_CONTACTS;
import static planet.it.limited.planetapp.utill.SaveValueSharedPreference.saveBoleanValueSharedPreferences;

public class MyContentObserver extends ContentObserver {
    private Context context;
    Cursor cursor;
    int counter;
    boolean isReadContacts;
    ContactsDB contactsDB;
    String name = null;
    String phoneNumber = "";

    private static final int REQUEST_READ_CONTACTS = 444;

    public MyContentObserver(Handler handler) {
        super(handler);
    }

    public MyContentObserver(Handler handler, Context context) {
        super(handler);
        this.context = context;
        contactsDB = new ContactsDB(context);
        contactsDB.open();
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);
        if (!selfChange) {


//            try {
//                if (ActivityCompat.checkSelfPermission(context,
//                        Manifest.permission.READ_CONTACTS)
//                        == PackageManager.PERMISSION_GRANTED) {
//                    ContentResolver cr = context.getContentResolver();
//                    Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
//                    if (cursor != null && cursor.getCount() > 0) {
//                        //moving cursor to last position
//                        //to get last element added
//                        cursor.moveToLast();
//                        String contactName = null, photo = null, contactNumber = null;
//                        String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
//
//                        if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
//                            Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
//                            if (pCur != null) {
//                                while (pCur.moveToNext()) {
//                                    contactNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                                    if (contactNumber != null && contactNumber.length() > 0) {
//                                        contactNumber = contactNumber.replace(" ", "");
//                                    }
//                                    contactName = pCur.getString(pCur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
//                                    String msg = "Name : " + contactName + " Contact No. : " + contactNumber;
//                                    //Displaying result
//                                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
//                                }
//                                pCur.close();
//                            }
//                        }
//                        cursor.close();
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
           // Toast.makeText(context, "Contact Change from Phone Contacts ", Toast.LENGTH_SHORT).show();

            contactsDB.deleteAllContacts();

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

        ContentResolver contentResolver = context.getContentResolver();

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
              //  saveBoleanValueSharedPreferences("is_read", true, context);
            }


        }
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (context.checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (((Activity)context).shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            ((Activity)context).requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        } else {
            ((Activity)context).requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }


    public static String getMeMyNumber(String number)
    {
        String out = number.replaceAll("[^0-9]", "") ;       //remove all the non numbers (brackets dashes spaces etc.) except the + signs

        return out;

    }
}
