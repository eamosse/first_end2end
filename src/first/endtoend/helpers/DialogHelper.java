package first.endtoend.helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import first.endtoend.R;

public class DialogHelper {

	//Progress Dialog showed when leaving app
	public static void processingDisconnection(final ProgressDialog progressDialog, final Activity activity){
		progressDialog.setTitle("Login out");
		progressDialog.setMessage("Disconnection ...");
		progressDialog.show();
		new Thread(new Runnable() { 
			@Override
			public void run() {
				for (int i = 0; i < 2; i++) {
					try {
						Thread.sleep(1500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				progressDialog.dismiss();
				activity.finish();
			}
		}).start();
	}

	public static void showErrorDialog(AlertDialog.Builder alert, String title, String message){
		
		alert.setTitle(title);
		alert.setIcon(android.R.drawable.ic_delete);
		alert.setMessage(message);
		alert.setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		alert.show();
	}
	
	public static void showSettingsDialog(final Context context, AlertDialog.Builder alert){
		
		alert.setTitle(R.string.internet_error);
		alert.setIcon(android.R.drawable.ic_dialog_alert);
		alert.setMessage(R.string.internet_error_message);
		alert.setPositiveButton(R.string.settings, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				context.startActivity(new Intent(Settings.ACTION_SETTINGS));
			}
		});
		alert.setNegativeButton(R.string.retry, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				return;
			}
		});
		alert.show();
	}
	
	public static void showProcessingDialog(ProgressDialog dialog, String title, String message){
		dialog.setIndeterminate(true);
		dialog.setCancelable(true);
		dialog.setInverseBackgroundForced(false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setTitle(title);
		dialog.setMessage(message);
	}
	
	
public static void showGPSSettingsDialog(final Context context, AlertDialog.Builder alert, String message){
		
	alert.setTitle(R.string.location_error);
	alert.setIcon(android.R.drawable.ic_delete);
	alert.setMessage(message);
		alert.setPositiveButton(R.string.settings, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
			}
		});
		alert.setNegativeButton(R.string.retry, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				return;
			}
		});
		alert.show();
	}

}
