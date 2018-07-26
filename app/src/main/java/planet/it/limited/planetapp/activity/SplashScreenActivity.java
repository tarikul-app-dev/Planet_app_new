package planet.it.limited.planetapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import planet.it.limited.planetapp.R;
import planet.it.limited.planetapp.utill.LanguageUtility;

import static planet.it.limited.planetapp.utill.SaveValueSharedPreference.getValueFromSharedPreferences;

public class SplashScreenActivity extends AppCompatActivity {
    boolean isLogin = true;
   // SaveValueSharedPreference saveValueSharedPreference;
   LanguageUtility languageUtility;
    String checkLan = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
       // isLogin = saveValueSharedPreference.getBoleanValueSharedPreferences("islogin",SplashScreenActivity.this);
        languageUtility = new LanguageUtility(SplashScreenActivity.this);
        checkLan = getValueFromSharedPreferences("language",SplashScreenActivity.this);
        if(checkLan!=null){
            if(checkLan.equals("en")){
                languageUtility.selectLanguage("en");

            }else {
                languageUtility.selectLanguage("bn");

            }
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if(isLogin){
                    waitSomeMin();
                }

            }
        }, 2000);
    }
    public void waitSomeMin(){
        Intent intent = new Intent(SplashScreenActivity.this,MainActivity.class);
        startActivity(intent);
        ActivityCompat.finishAffinity(SplashScreenActivity.this);
    }
}
