package first.endtoend.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class InternetHelper {

    public static boolean isOnline(Context context) {
    	boolean status = false;
        try {
        	ConnectivityManager connectivityManager = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        status = networkInfo != null && networkInfo.isAvailable() &&
                networkInfo.isConnected();
        return status;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }
}