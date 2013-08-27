package first.endtoend;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import fr.unice.mbds.nfc.library.ApduCallBack;
import fr.unice.mbds.nfc.library.ApduError;
import fr.unice.mbds.nfc.library.NfcController;

public class MyActivity extends Activity implements ApduCallBack{

	protected boolean areSEAndAppletPresents;
	
	protected NfcController ctrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApplication act = (MyApplication)getApplication(); 
	    ctrl = act.getController();
	    areSEAndAppletPresents = act.areSEAndAppletPresents;
	    
	    
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		ctrl.setCallback(this);
		if(!ctrl.isServiceConnected()) {
		    ctrl.initService();
		}
	}
	
		
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.option_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.logout:
			logout();
			return (true);
		}
		return (super.onOptionsItemSelected(item));
	}


	private void logout() {
		AlertDialog.Builder ab = new AlertDialog.Builder(this);
		ab.setTitle(R.string.logout);

		ab.setMessage(R.string.logout_message);


		ab.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();

			}
		});

		ab.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(MyActivity.this, LoginActivity.class);
				startActivity(intent);
				MyActivity.this.finish();
			}
		});
		ab.show();


	}
	
	
	@Override
	public void onNotConnected() { 
		// TODO Auto-generated method stub
	}

	@Override
	public void onNoSecureElement() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onAPDUError(ApduError error) {
		// TODO handle errors 
		Toast.makeText(this, error.getMessage(), Toast.LENGTH_LONG).show();
		System.out.println("error code "+error.getApduError()[0]);
		
		
	}

	@Override
	public void onNoReader() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setMessage("Sorry! this phone is not NFC Capable");
		alert.setCancelable(false);
		alert.setPositiveButton("leave App", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				finish();
			}
		});
		alert.show();
	}
 
	@Override
	public void onNoApplet() {
		// TODO call service to perform TSM task (install applet on simcard)
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setMessage("Your Sim Card need to be updated");
		alert.setCancelable(false);
		alert.setPositiveButton("leave App", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				finish();
			}
		});
		alert.show();

	}

	@Override
	public void onIOException(String message) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onConnected() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPINRequired() {
		// TODO Auto-generated method stub
		startActivity(new Intent(this, PinEntryView.class));
	}

	@Override
	public void onResponse(String[] response, int commandId) {
		// TODO Auto-generated method stub
		
	}


}
