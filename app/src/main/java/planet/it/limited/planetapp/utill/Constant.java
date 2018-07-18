package planet.it.limited.planetapp.utill;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Tarikul on 7/3/2018.
 */

public class Constant {
    public static final String singleSMSAPI = "https://api.infobip.com/sms/1/text/single";

    Context mContext;
    public Constant(Context context){
        this.mContext = context ;
    }

    public  boolean isConnectingToInternet() {

        ConnectivityManager cm =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
}
