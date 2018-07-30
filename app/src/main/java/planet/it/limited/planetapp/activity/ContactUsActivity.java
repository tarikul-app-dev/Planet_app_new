package planet.it.limited.planetapp.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;


import me.drakeet.materialdialog.MaterialDialog;
import planet.it.limited.planetapp.R;
import planet.it.limited.planetapp.utill.FontCustomization;

public class ContactUsActivity extends AppCompatActivity {
    Toolbar toolbar;
    public static String TAG = "cookie";
    public static String TAG_PRO = "progress";
    ImageView imgvHome,imgvMap;
    TextView txvMap,txvCallHotline,txvCallSkype,txvPlanetIT,txvAddress,txvEmail;
    MaterialDialog alert;
    public static final int REQUEST_PERM_CALL = 102;

    FontCustomization fontCustomization;
    TextView txvToolbarText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        toolbar = (Toolbar)findViewById(R.id.toolbar_contact);
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
        txvPlanetIT.startAnimation(AnimationUtils.loadAnimation(this, R.anim.text_animate));
    }

    public void initViews(){
        imgvMap = (ImageView)findViewById(R.id.imgv_map);
        txvMap = (TextView)findViewById(R.id.txv_map);
        txvCallHotline = (TextView)findViewById(R.id.txv_hotline_call);
        txvCallSkype = (TextView)findViewById(R.id.txv_skype_call);
        txvPlanetIT = (TextView)findViewById(R.id.txv_planet_it);
        //txvContactUs = (TextView)findViewById(R.id.txv_main);
        txvAddress = (TextView)findViewById(R.id.txv_address);
        txvEmail = (TextView)findViewById(R.id.txv_email);
        txvToolbarText = (TextView)findViewById(R.id.txv_main);

        fontCustomization = new FontCustomization(ContactUsActivity.this);
        txvPlanetIT.setTypeface(fontCustomization.getTexgyreHerosRegular());
        txvCallHotline.setTypeface(fontCustomization.getTexgyreHerosRegular());
        txvCallSkype.setTypeface(fontCustomization.getTexgyreHerosRegular());
        txvToolbarText.setTypeface(fontCustomization.getHeadLandOne());
        txvAddress.setTypeface(fontCustomization.getTexgyreHerosRegular());
        txvEmail.setTypeface(fontCustomization.getTexgyreHerosRegular());



        imgvMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = "https://www.google.com/maps/place/Planet+IT/@23.7715952,90.4094301,17z/data=!3m1!4b1!4m5!3m4!1s0x3755c7788a1ea509:0x3317a74c79b39f34!8m2!3d23.7715903!4d90.4116188";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            }
        });

        txvMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = "https://www.google.com/maps/place/Planet+IT/@23.7715952,90.4094301,17z/data=!3m1!4b1!4m5!3m4!1s0x3755c7788a1ea509:0x3317a74c79b39f34!8m2!3d23.7715903!4d90.4116188";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            }
        });


        txvCallHotline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){

                    ActivityCompat.requestPermissions(ContactUsActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PERM_CALL);

                } else{
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:+88 01616 752638" ));
                    startActivity(intent);
                }


            }
        });


        txvCallSkype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PackageManager packageManager = getPackageManager();
                Intent skype = packageManager.getLaunchIntentForPackage("com.skype.raider");
                // skype.setData(Uri.parse("tel:+88 01616 752638"));
                startActivity(skype);
            }
        });
        //txvPlanetIT.startAnimation(AnimationUtils.loadAnimation(this, R.anim.text_animate));
    }

    @Override
    public void onResume() {
        super.onResume();
        txvPlanetIT = (TextView)findViewById(R.id.txv_planet_it);
        txvPlanetIT.startAnimation(AnimationUtils.loadAnimation(this, R.anim.text_animate));

    }
}
