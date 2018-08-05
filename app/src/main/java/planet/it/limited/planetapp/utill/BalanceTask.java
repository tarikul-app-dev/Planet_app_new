package planet.it.limited.planetapp.utill;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import planet.it.limited.planetapp.activity.SettingsActivity;

import static planet.it.limited.planetapp.utill.SaveValueSharedPreference.saveToSharedPreferences;

/**
 * Created by Tarikul on 7/30/2018.
 */

public class BalanceTask {
    Context mContext;
    String  retBalance = " ";
    String  balanceAPI = " ";
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public BalanceTask(Context context){
        this.mContext = context;
        balanceAPI = Constant.balanceAPI ;
    }

  public void getBalance(String userName ,String password){
            RetBalanceTask retBalanceTask = new RetBalanceTask(userName,password);
            retBalanceTask.execute();
  }

    public class RetBalanceTask extends AsyncTask<String, Integer, String> {

        String mUserName;
        String mPassword;
        // private Dialog loadingDialog;
        public RetBalanceTask (String userName,String password){

            mUserName = userName;
            mPassword = password;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
          //  loadingDialog = ProgressDialog.show(mContext, "Please wait", "Loading...");

        }

        @Override
        protected String doInBackground(String... params) {

            OkHttpClient client = new OkHttpClient();
            String credentials = mUserName + ":" + mPassword;
            final String basic = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

            try {

               // JSONObject json = new JSONObject();

                //RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));


                Request request = new Request.Builder()
                        .addHeader("Authorization",basic)
                        .addHeader("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==")
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Accept", "application/json")
                        .url(balanceAPI)
                        .build();


                Response response = null;
                //client.setRetryOnConnectionFailure(true);
                response = client.newCall(request).execute();
                if (response.isSuccessful()){
                    final String result = response.body().string();
                    // Log.d(RESPONSE_LOG,result);

                    try {
                        JSONObject jsonObject = new JSONObject(result);

                        retBalance = jsonObject.getString("balance");
                        saveToSharedPreferences("balance",retBalance,mContext);
                        if(retBalance.length()>0){
                            SettingsActivity.checkBalance = true;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    saveToSharedPreferences("balance","",mContext);
                    SettingsActivity.checkBalance = false;
                }
            } catch (IOException e) {
                e.printStackTrace();

            }
            return retBalance;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }


    }

}
