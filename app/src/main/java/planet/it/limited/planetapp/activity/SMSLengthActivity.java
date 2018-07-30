package planet.it.limited.planetapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import planet.it.limited.planetapp.R;
import planet.it.limited.planetapp.utill.FontCustomization;

public class SMSLengthActivity extends AppCompatActivity {
    TextView txvMsgCount,txvLengthOfText;
    public static EditText edtContentMsg;
    FontCustomization fontCustomization;
    Toolbar toolbar;
    TextView toolbarText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_length_checker);
        toolbar = (Toolbar)findViewById(R.id.toolbar_sms_checker_app);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
        initViews();
    }
    public void initViews(){
        toolbarText = (TextView)findViewById(R.id.txv_main);
        txvLengthOfText = (TextView)findViewById(R.id.txv_text_length);
        txvMsgCount = (TextView)findViewById(R.id.txv_message_count);
        edtContentMsg = (EditText)findViewById(R.id.edt_msg_content);

        fontCustomization = new FontCustomization(SMSLengthActivity.this);
        toolbarText.setTypeface(fontCustomization.getHeadLandOne());
        txvLengthOfText.setTypeface(fontCustomization.getTexgyreHerosRegular());
        txvMsgCount.setTypeface(fontCustomization.getTexgyreHerosRegular());
        edtContentMsg.setTypeface(fontCustomization.getTexgyreHerosRegular());

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

    }

}
