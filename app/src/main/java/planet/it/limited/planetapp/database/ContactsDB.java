package planet.it.limited.planetapp.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import planet.it.limited.planetapp.model.ConToSMSM;
import planet.it.limited.planetapp.model.ContactModel;


/**
 * Created by Tarikul on 7/5/2018.
 */

public class ContactsDB {
    // db version
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "contacts_info";
    private static final String DATABASE_TABLE_CONTACTS = "contacts";
    private ContactsDB.DBHelper dbhelper;
    private final Context context;
    private SQLiteDatabase database;

    // insert row
    public static final String KEY_ROWID = "id";
    public static final String KEY_MOBILE_NUMBER = "mobile_number";
    public static final String KEY_NAME = "name";
    //  public static final String KEY_IMAGE = "image";


    //to use print



    private static class DBHelper extends SQLiteOpenHelper {

        @SuppressLint("NewApi")
        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            // create table to store msgs
            db.execSQL(" CREATE TABLE " + DATABASE_TABLE_CONTACTS + " ("
                    + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + KEY_NAME + " TEXT, "
                    + KEY_MOBILE_NUMBER + " TEXT );");


        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_CONTACTS);


            onCreate(db);
        }

    }
    // constructor
    public ContactsDB(Context c) {
        context = c;
    }

    // open db
    public ContactsDB open() {
        dbhelper = new  DBHelper(context);
        database = dbhelper.getWritableDatabase();
        return this;
    }

    // close db
    public void close() {
        dbhelper.close();
    }


    public long saveAllContacts(String name,String mobNumber){
        ContentValues cv = new ContentValues();
        cv.put(KEY_NAME, name);
        cv.put(KEY_MOBILE_NUMBER, mobNumber);

        long dbInsert = database.insert(DATABASE_TABLE_CONTACTS, null, cv);

        if(dbInsert != -1) {

            // Toast.makeText(context, "Contacts Save Success" + dbInsert, Toast.LENGTH_SHORT).show();
        }else{
            //Toast.makeText(context, "Something wrong", Toast.LENGTH_SHORT).show();
        }



        return dbInsert;
    }





    public ArrayList getInputData(){

        ArrayList<ConToSMSM> contactList = new ArrayList<>();
        String select_query = "SELECT  * FROM " + DATABASE_TABLE_CONTACTS ;


        Cursor cursor = database.rawQuery(select_query,null);

        // if(cursor != null && cursor.moveToFirst()){
        //int iDbId = cursor.getColumnIndex(KEY_ROWID);
        int iUserName = cursor.getColumnIndex(KEY_NAME);
        int iMobNumber = cursor.getColumnIndex(KEY_MOBILE_NUMBER);



        for (cursor.moveToLast(); ! cursor.isBeforeFirst(); cursor.moveToPrevious()) {
            //    for (cursor.moveToFirst(); ! cursor.isAfterLast(); cursor.moveToNext()) {

            ConToSMSM contactModel = new ConToSMSM();
            contactModel.setUserName(cursor.getString(iUserName));
            contactModel.setContactNumber(cursor.getString(iMobNumber));

            contactList.add(contactModel);


        }
        cursor.close();
        return contactList;
    }
    public ArrayList getOnlyNumber(){

        ArrayList<ContactModel> numberList = new ArrayList<>();
        String select_query = "SELECT  mobile_number FROM " + DATABASE_TABLE_CONTACTS ;
        Cursor cursor = database.rawQuery(select_query,null);

        //  int iUserName = cursor.getColumnIndex(KEY_NAME);
        int iMobNumber = cursor.getColumnIndex(KEY_MOBILE_NUMBER);



        for (cursor.moveToLast(); ! cursor.isBeforeFirst(); cursor.moveToPrevious()) {
            //    for (cursor.moveToFirst(); ! cursor.isAfterLast(); cursor.moveToNext()) {

            ContactModel contactModel = new ContactModel();
            // contactModel.setUserName(cursor.getString(iUserName));
            contactModel.setContactNumber(cursor.getString(iMobNumber));

            numberList.add(contactModel);


        }
        cursor.close();
        return numberList;
    }


    public ArrayList searchByName(String searchItem){
        ArrayList<ContactModel> searchList = new ArrayList<>();


        String select_query   = " SELECT * FROM " + DATABASE_TABLE_CONTACTS + " WHERE "  + KEY_NAME + " like '%" + searchItem + "%'"
                + " OR " + KEY_MOBILE_NUMBER + " like '%" + searchItem + "%'" ;


        Cursor cursor = database.rawQuery(select_query,null);

        // if(cursor != null && cursor.moveToFirst()){
        //int iDbId = cursor.getColumnIndex(KEY_ROWID);
        int iUserName = cursor.getColumnIndex(KEY_NAME);
        int iMobNumber = cursor.getColumnIndex(KEY_MOBILE_NUMBER);


        for (cursor.moveToLast(); ! cursor.isBeforeFirst(); cursor.moveToPrevious()) {
            //    for (cursor.moveToFirst(); ! cursor.isAfterLast(); cursor.moveToNext()) {

            ContactModel contactModel = new ContactModel();
            contactModel.setUserName(cursor.getString(iUserName));
            contactModel.setContactNumber(cursor.getString(iMobNumber));

            searchList.add(contactModel);


        }
        cursor.close();



        return searchList;


    }

    public void deleteAllContacts()
    {
        database.execSQL(" delete from " + DATABASE_TABLE_CONTACTS);
    }


}
