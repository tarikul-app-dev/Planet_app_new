package planet.it.limited.planetapp.activity;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import planet.it.limited.planetapp.R;


public class ServicesActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView txvBulkSMS,txvPushPull,txvAppDev,txvMobileTopUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
        toolbar = (Toolbar)findViewById(R.id.toolbar_services);
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
            txvBulkSMS = (TextView)findViewById(R.id.headline_one);
            txvPushPull = (TextView)findViewById(R.id.headline_two);
            txvAppDev = (TextView)findViewById(R.id.headline_three);
            txvMobileTopUp = (TextView)findViewById(R.id.headline_four);




    }


}
