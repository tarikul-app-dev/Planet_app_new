package planet.it.limited.planetapp.utill;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import planet.it.limited.planetapp.R;
import planet.it.limited.planetapp.activity.MainActivity;
import planet.it.limited.planetapp.activity.SingleSMSActivity;

/**
 * Created by Tarikul on 7/3/2018.
 */

public class SendSingleSMS {
    Context mContext;
    String  singleSMSAPI = "";
    String messageId = "";
   // String status = " ";

    public static SingleSMSActivity singleSMSActivity;
    private Dialog loadingDialog;

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public SendSingleSMS(Context context){
        this.mContext = context;
        singleSMSAPI = Constant.singleSMSAPI;
        singleSMSActivity = (SingleSMSActivity) mContext;

    }

    public void sendToSingleSMS(String senderNum,String recipientsNum,String msg,String username,String password){
        SendSMSTask sendSMSTask = new SendSMSTask(senderNum,recipientsNum,msg,username,password);
        sendSMSTask.execute();
    }

    public class SendSMSTask extends AsyncTask<String, Integer, String> {
        String mFromSender;
        String mToRecipients;
        String mContentMsg;
        String mUserName;
        String mPassword;
        // private Dialog loadingDialog;
        public SendSMSTask (String fromSender,String toRecipients,String contentMsg,String userName,String password){
            mFromSender = fromSender;
            mToRecipients = toRecipients;
            mContentMsg = contentMsg;
            mUserName = userName;
            mPassword = password;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingDialog = ProgressDialog.show(mContext, "Please wait", "Loading...");

        }

        @Override
        protected String doInBackground(String... params) {

            OkHttpClient client = new OkHttpClient();
            String credentials = mUserName + ":" + mPassword;
            final String basic = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

            try {

                JSONObject json = new JSONObject();
                json.put("from",mFromSender);
                json.put("to",mToRecipients);
                json.put("text",mContentMsg);

                RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));


                Request request = new Request.Builder()
                        .addHeader("Authorization",basic)
                        .addHeader("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==")
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Accept", "application/json")
                        .url(singleSMSAPI)
                        .post(requestBody)
                        .build();


                Response response = null;
                //client.setRetryOnConnectionFailure(true);
                response = client.newCall(request).execute();
                if (response.isSuccessful()){
                    final String result = response.body().string();
                   // Log.d(RESPONSE_LOG,result);

                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray jArray1 = jsonObject.getJSONArray("messages");
                       // JSONObject jsonObject1 = new JSONObject(jArray1);

                        for(int i = 0; i < jArray1 .length(); i++)
                        {
                            JSONObject object1 = jArray1.getJSONObject(i);

                           messageId = object1.getString("messageId");

//                           String searchStatus = object1.getString("status");
//                           JSONObject jsonObject1 = new JSONObject(searchStatus);
//                           status = jsonObject1.getString("groupName");


                        }
                        ((Activity)mContext).runOnUiThread (new Thread(new Runnable() {
                            public void run() {
                                if(messageId.length()>0){
                                    loadingDialog.cancel();
                                    openDialog(messageId);
                                }else {
                                    Toast.makeText(mContext,"Message Sending Failled",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {

                    ((Activity)mContext).runOnUiThread (new Thread(new Runnable() {
                        public void run() {
                            loadingDialog.cancel();
                                Toast.makeText(mContext,"Message Sending Failled",Toast.LENGTH_SHORT).show();
                        }
                    }));
                }
            } catch (IOException e) {
                e.printStackTrace();
                ((Activity)mContext).runOnUiThread (new Thread(new Runnable() {
                    public void run() {
                        loadingDialog.cancel();
                        Toast.makeText(mContext,"Message Sending Failled",Toast.LENGTH_SHORT).show();
                    }
                }));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }


    }

    public void openDialog(String msgId) {
        final Dialog dialog = new Dialog(mContext); // Context, this, etc.
        dialog.setContentView(R.layout.custom_dialog);
        TextView txvResponseMsg = (TextView) dialog.findViewById(R.id.dialog_info);
        txvResponseMsg.setText("Your Message Sent Success" + " Your Msg Id " + msgId);
        Button okButton = (Button) dialog.findViewById(R.id.dialog_ok);
//        Button cancleButton = (Button) dialog.findViewById(R.id.dialog_cancel);

        okButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                singleSMSActivity.finish();
                Intent intent = new Intent(mContext,SingleSMSActivity.class);
                mContext.startActivity(intent);

                SingleSMSActivity.txtPhoneNo.setText("");
                SingleSMSActivity.edtContentMsg.setText("");
                dialog.dismiss();
            }
        });
//        cancleButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });

        dialog.show();
    }
}
