package planet.it.limited.planetapp.activity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;

import planet.it.limited.planetapp.R;
import planet.it.limited.planetapp.adapter.ContactsAdapter;
import planet.it.limited.planetapp.database.ContactsDB;
import planet.it.limited.planetapp.model.ContactModel;
import planet.it.limited.planetapp.model.SortBasedOnName;
import planet.it.limited.planetapp.utill.Constant;
import planet.it.limited.planetapp.utill.FontCustomization;
import planet.it.limited.planetapp.utill.SendSingleSMS;

import static android.Manifest.permission.READ_CONTACTS;
import static planet.it.limited.planetapp.utill.SaveValueSharedPreference.getBoleanValueSharedPreferences;
import static planet.it.limited.planetapp.utill.SaveValueSharedPreference.getValueFromSharedPreferences;
import static planet.it.limited.planetapp.utill.SaveValueSharedPreference.saveBoleanValueSharedPreferences;
import android.view.ViewGroup.LayoutParams;

public class SingleSMSActivity extends AppCompatActivity {
    public static AutoCompleteTextView txtPhoneNo;
           AutoCompleteTextView txvSender;
    public static EditText edtContentMsg;
    String senderNumber = " ";
    String userName = "";
    String password = "";
    public ArrayList<ContactModel> contactNumList ;
    Button btnSendMsg;
    SendSingleSMS sendSingleSMS;
    Toolbar toolbar;
    TextView txvMsgCount,txvLengthOfText;
    boolean isReadContacts;
    ContactsDB contactsDB;
    private PopupWindow pw;
    ListView lvContacts;
    ContactsAdapter contactsAdapter;
    LinearLayout linlayoutForm ,linearLayoutListView;
    public Constant constant;

    // init global boolean
    private boolean isReached = false;
    FontCustomization fontCustomization;
    TextView txvToolbarText;
    TextView txvTo,txvFrom,txvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_sms);
        toolbar = (Toolbar)findViewById(R.id.toolbar_single_sms);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 onBackPressed();
            }
        });
    //    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);

        initViews();



    }
    public void initViews(){
        fontCustomization = new FontCustomization(SingleSMSActivity.this);
        txvToolbarText = (TextView)findViewById(R.id.txv_main);
        contactsDB = new ContactsDB(SingleSMSActivity.this);
        contactsDB.open();
        isReadContacts = getBoleanValueSharedPreferences("is_read", SingleSMSActivity.this);
        txvLengthOfText = (TextView)findViewById(R.id.txv_text_length);
        txvMsgCount = (TextView)findViewById(R.id.txv_message_count);
        txtPhoneNo = (AutoCompleteTextView) findViewById(R.id.txv_recipients);
        txvSender = (AutoCompleteTextView) findViewById(R.id.txv_sender);
        edtContentMsg = (EditText)findViewById(R.id.edt_msg_content);
        btnSendMsg = (Button)findViewById(R.id.btn_send_msg);
        txvTo = (TextView)findViewById(R.id.txv_to);
        txvFrom = (TextView)findViewById(R.id.txv_from);
        txvContent = (TextView)findViewById(R.id.txv_content);

        senderNumber = getValueFromSharedPreferences("sender_number",SingleSMSActivity.this);
        userName = getValueFromSharedPreferences("user_name",SingleSMSActivity.this);
        password = getValueFromSharedPreferences("pass_word",SingleSMSActivity.this);
        sendSingleSMS = new SendSingleSMS(SingleSMSActivity.this);
        lvContacts = (ListView)findViewById(R.id.lv_contact);
        linlayoutForm = (LinearLayout)findViewById(R.id.linlayout_form);
        linearLayoutListView = (LinearLayout)findViewById(R.id.linlayout_listview);
        constant = new Constant(SingleSMSActivity.this);
        btnSendMsg.setTransformationMethod(null);

        // to set font style
        txvToolbarText.setTypeface(fontCustomization.getHeadLandOne());
        txvLengthOfText.setTypeface(fontCustomization.getTexgyreHerosRegular());
        txvMsgCount.setTypeface(fontCustomization.getTexgyreHerosRegular());
        txtPhoneNo.setTypeface(fontCustomization.getTexgyreHerosRegular());
        txvSender.setTypeface(fontCustomization.getTexgyreHerosRegular());
        edtContentMsg.setTypeface(fontCustomization.getTexgyreHerosRegular());
        btnSendMsg.setTypeface(fontCustomization.getTexgyreHerosRegular());
        txvTo.setTypeface(fontCustomization.getTexgyreHerosRegular());
        txvFrom.setTypeface(fontCustomization.getTexgyreHerosRegular());
        txvContent.setTypeface(fontCustomization.getTexgyreHerosRegular());

        if(senderNumber!=null){
            txvSender.setText(senderNumber);
        }

        btnSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String contentMsg = edtContentMsg.getText().toString();
                String toRecipients = txtPhoneNo.getText().toString();
                if(toRecipients.length()>0){
                    String checkCode=toRecipients.substring(0,1);
                    if(!checkCode.equals("8")){
                        toRecipients = "88" + toRecipients ;
                    }
                }


                if(contentMsg.length()>0 && toRecipients.length()>0){
                    if(senderNumber!=null){
                        if(senderNumber.length()>0){
                            if(constant.isConnectingToInternet()){
                                sendSingleSMS.sendToSingleSMS(senderNumber,toRecipients,contentMsg,userName,password);
                            }else {
                                Toast.makeText(SingleSMSActivity.this,"Ohh!Your Device is Offline",Toast.LENGTH_SHORT).show();
                            }

                        }
                    }else {
                        Toast.makeText(SingleSMSActivity.this,"Please set your sender from Settings",Toast.LENGTH_SHORT).show();
                    }


                }else {
                    Toast.makeText(SingleSMSActivity.this,"Please give Recipients and Content Msg",Toast.LENGTH_SHORT).show();
                }

            }
        });

        final TextWatcher txwatcher = new TextWatcher() {
            int smsLength =0;
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                smsLength = s.length();
                int smsTotalChar = 160 ;
                if(s.length()>0){
                    int smsCount = smsTotalChar - s.length();
                    if(smsCount>0){
                        txvLengthOfText.setText(String.valueOf(smsCount));

                    }
                    if(smsLength>159){
                        int secTotalSMSCount = 145;
                        int smsLength2 = 305;

                        txvLengthOfText.setText(String.valueOf(secTotalSMSCount));
                        int SecSMSCount = smsLength2 - s.length();
                        if(SecSMSCount>0){
                            txvLengthOfText.setText(String.valueOf(SecSMSCount));
                        }
                        if(s.length()==161){
                            txvMsgCount.setText("2");
                        }

                    }

                    if(smsLength>304){
                        int thirdTotalSMSCount = 152;
                        int smsLength3 = 457;

                        txvLengthOfText.setText(String.valueOf(thirdTotalSMSCount));
                        int thirdSMSCount = smsLength3 - s.length();
                        if(thirdSMSCount>0){
                            txvLengthOfText.setText(String.valueOf(thirdSMSCount));
                        }
                        if(s.length()==305){
                            txvMsgCount.setText("3");
                        }

                    }

                    if(smsLength>456){
                        int fourTotalSMSCount = 152;
                        int smsLength4 = 609;

                        txvLengthOfText.setText(String.valueOf(fourTotalSMSCount));
                        int fourSMSCount = smsLength4 - s.length();
                        if(fourSMSCount>0){
                            txvLengthOfText.setText(String.valueOf(fourSMSCount));
                        }
                        if(s.length()==457){
                            txvMsgCount.setText("4");
                        }

                    }

                    if(smsLength>608){
                        int fiveTotalSMSCount = 152;
                        int smsLength5 = 761;

                        txvLengthOfText.setText(String.valueOf(fiveTotalSMSCount));
                        int fiveSMSCount = smsLength5 - s.length();
                        if(fiveSMSCount>0){
                            txvLengthOfText.setText(String.valueOf(fiveSMSCount));
                        }
                        if(s.length()==609){
                            txvMsgCount.setText("5");
                        }

                    }

                    if(smsLength>760){
                        int sixTotalSMSCount = 152;
                        int smsLength6 = 913;

                        txvLengthOfText.setText(String.valueOf(sixTotalSMSCount));
                        int sixSMSCount = smsLength6 - s.length();
                        if(sixSMSCount>0){
                            txvLengthOfText.setText(String.valueOf(sixSMSCount));
                        }
                        if(s.length()==761){
                            txvMsgCount.setText("6");
                        }

                    }
                    if(smsLength>912){
                        int sevenTotalSMSCount = 152;
                        int smsLength7 = 1065;

                        txvLengthOfText.setText(String.valueOf(sevenTotalSMSCount));
                        int sevSMSCount = smsLength7 - s.length();
                        if(sevSMSCount>0){
                            txvLengthOfText.setText(String.valueOf(sevSMSCount));
                        }
                        if(s.length()==913){
                            txvMsgCount.setText("7");
                        }

                    }

                    if(smsLength>1064){
                        int eightTotalSMSCount = 152;
                        int smsLength8 = 1217;

                        txvLengthOfText.setText(String.valueOf(eightTotalSMSCount));
                        int eightSMSCount = smsLength8 - s.length();
                        if(eightSMSCount>0){
                            txvLengthOfText.setText(String.valueOf(eightSMSCount));
                        }
                        if(s.length()==1065){
                            txvMsgCount.setText("8");
                        }

                    }

                    if(smsLength>1216){
                        int nineTotalSMSCount = 152;
                        int smsLength9 = 1369;

                        txvLengthOfText.setText(String.valueOf(nineTotalSMSCount));
                        int nineSMSCount = smsLength9 - s.length();
                        if(nineSMSCount>0){
                            txvLengthOfText.setText(String.valueOf(nineSMSCount));
                        }
                        if(s.length()==1217){
                            txvMsgCount.setText("9");
                        }

                    }

                    if(smsLength>1368){
                        int tenTotalSMSCount = 152;
                        int smsLength10 = 1521;

                        txvLengthOfText.setText(String.valueOf(tenTotalSMSCount));
                        int tenSMSCount = smsLength10 - s.length();
                        if(tenSMSCount>0){
                            txvLengthOfText.setText(String.valueOf(tenSMSCount));
                        }
                        if(s.length()==1369){
                            txvMsgCount.setText("10");
                        }

                    }

                }


            }

            public void afterTextChanged(Editable s) {

            }
        };

        edtContentMsg.addTextChangedListener(txwatcher);






        txtPhoneNo.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                linlayoutForm.setVisibility(View.GONE);
                linearLayoutListView.setVisibility(View.VISIBLE);
            }
        });

        // Set onclicklistener to the list item.
        lvContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //TODO Do whatever you want with the list data
                String name = contactNumList.get(position).getUserName();
                String number = contactNumList.get(position).getContactNumber();


                //txtPhoneNo.setText(name);
                txtPhoneNo.setText(number);
                linearLayoutListView.setVisibility(View.GONE);
                linlayoutForm.setVisibility(View.VISIBLE);
            }
        });
        txtPhoneNo.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String text = txtPhoneNo.getText().toString();
                contactsDB.open();
                contactNumList = contactsDB.searchByName(text);
                contactsAdapter = new ContactsAdapter(contactNumList, SingleSMSActivity.this);
                lvContacts.setAdapter(contactsAdapter);


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                if(txtPhoneNo.getText().length() == 11 && !isReached) {
                    linlayoutForm.setVisibility(View.VISIBLE);
                    linearLayoutListView.setVisibility(View.GONE);
                    isReached = true;
                }else if(txtPhoneNo.getText().length() == 5 && !isReached){
                    linlayoutForm.setVisibility(View.VISIBLE);
                    linearLayoutListView.setVisibility(View.GONE);
                    isReached = true;
                }else if(txtPhoneNo.getText().length() == 13 && !isReached){
                    linlayoutForm.setVisibility(View.VISIBLE);
                    linearLayoutListView.setVisibility(View.GONE);
                    isReached = true;
                }
                // if edittext has less than 10chars & boolean has changed, reset
                if(txtPhoneNo.getText().length() < 12 && isReached) isReached = false;
                if(txtPhoneNo.getText().length() < 6 && isReached) isReached = false;
                if(txtPhoneNo.getText().length() < 14 && isReached) isReached = false;
            }
        });
    }


}
