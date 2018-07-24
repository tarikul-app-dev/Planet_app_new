package planet.it.limited.planetapp.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import planet.it.limited.planetapp.R;
import planet.it.limited.planetapp.utill.FontCustomization;

import static planet.it.limited.planetapp.utill.SaveValueSharedPreference.getValueFromSharedPreferences;
import static planet.it.limited.planetapp.utill.SaveValueSharedPreference.saveToSharedPreferences;

public class SettingsActivity extends AppCompatActivity {
    AutoCompleteTextView edtUserName,edtPass,edtSenderNum;
    Button btnSave,btnUpdate;
    TextView txvUserName,txvPassword,txvSenderNum,txvToolbarText;
    Toolbar toolbar;
    FontCustomization fontCustomization;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        toolbar = (Toolbar)findViewById(R.id.toolbar_settings);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();


            }
        });


        initViews();
    }

    public void initViews(){
        fontCustomization = new FontCustomization(SettingsActivity.this);
        txvToolbarText = (TextView)findViewById(R.id.txv_main);
        txvUserName = (TextView)findViewById(R.id.txv_username_head);
        txvPassword = (TextView)findViewById(R.id.txv_password_head);
        txvSenderNum = (TextView)findViewById(R.id.txv_from_sender);
        edtUserName = (AutoCompleteTextView) findViewById(R.id.txv_user_name);
        edtPass = (AutoCompleteTextView) findViewById(R.id.txv_password);
        edtSenderNum = (AutoCompleteTextView) findViewById(R.id.txv_sender);
        btnSave = (Button)findViewById(R.id.btn_save);
        btnUpdate = (Button)findViewById(R.id.btn_update);

        // to set font style
        txvToolbarText.setTypeface(fontCustomization.getMerlin());
        txvUserName.setTypeface(fontCustomization.getTexgyreHerosRegular());
        txvPassword.setTypeface(fontCustomization.getTexgyreHerosRegular());
        txvSenderNum.setTypeface(fontCustomization.getTexgyreHerosRegular());
        edtUserName.setTypeface(fontCustomization.getTexgyreHerosRegular());
        edtPass.setTypeface(fontCustomization.getTexgyreHerosRegular());
        edtSenderNum.setTypeface(fontCustomization.getTexgyreHerosRegular());
        btnSave.setTypeface(fontCustomization.getTexgyreHerosRegular());
        btnUpdate.setTypeface(fontCustomization.getTexgyreHerosRegular());
        btnSave.setTransformationMethod(null);
        btnUpdate.setTransformationMethod(null);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveToSharedPreferences("user_name",edtUserName.getText().toString(),SettingsActivity.this);
                saveToSharedPreferences("pass_word",edtPass.getText().toString(),SettingsActivity.this);
                saveToSharedPreferences("sender_number",edtSenderNum.getText().toString(),SettingsActivity.this);

                Toast.makeText(SettingsActivity.this,"You Info Save Success",Toast.LENGTH_SHORT).show();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveToSharedPreferences("user_name",edtUserName.getText().toString(),SettingsActivity.this);
                saveToSharedPreferences("pass_word",edtPass.getText().toString(),SettingsActivity.this);
                saveToSharedPreferences("sender_number",edtSenderNum.getText().toString(),SettingsActivity.this);
                Toast.makeText(SettingsActivity.this,"You Info Update Success",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        String senderNum =getValueFromSharedPreferences("sender_number",SettingsActivity.this);;
        String userName = getValueFromSharedPreferences("user_name",SettingsActivity.this); ;
        String password = getValueFromSharedPreferences("pass_word",SettingsActivity.this);;

        if(senderNum!=null){
            edtSenderNum.setText(senderNum);
        }
        if(password!=null){
            edtPass.setText(password);
        }
        if (userName!=null){
            edtUserName.setText(userName);
        }



    }
}
