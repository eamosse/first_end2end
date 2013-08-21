package first.endtoend;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		Log.d(context.getString(R.string.app_name), "Got the Boot Event>>>");
		Log.d(context.getString(R.string.app_name), "Starting Service>>>");
		MySyncService.setContext(context);
		Intent myIntent = new Intent(context,MySyncService.class);
		context.startService(myIntent);

	}
}