package first.endtoend;

import java.io.UnsupportedEncodingException;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.widget.TextView;
import first.endtoend.facades.BeneficiaryFacade;
import first.endtoend.facades.FamilyFacade;
import first.endtoend.facades.RationCardFacade;
import first.endtoend.helpers.Constant;
import first.endtoend.helpers.DialogHelper;
import first.endtoend.helpers.JsonHelper;
import first.endtoend.models.FIAgent;
import first.endtoend.models.Family;
import first.endtoend.models.RationCard;


public class TagActivity extends MyActivity {


	public static String idTag;
	
	String soundOrVib;
	Resources res;

	String messageReceived;
	public ProgressDialog progressDialog;

	AlertDialog.Builder alert;
	SharedPreferences settings;
	String PREF_NAME = "my_pref_settings";
	RationCardFacade rcFacade;

	FamilyFacade familyFacade;
	BeneficiaryFacade bf;

	private FIAgent user;

	public static Family family;
	public static RationCard rationCard;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		settings = getSharedPreferences(PREF_NAME, 0);
		String storedUserString  = settings.getString(Constant.USER, ""); 
		System.out.println("user " + storedUserString);
		if(!storedUserString.isEmpty()){
			user = JsonHelper.getObjectFromJson(storedUserString,FIAgent.class,"");
		}
		
		//TODO: if no user is found show an alert message and close this 
		progressDialog = new ProgressDialog(this);

		setContentView(R.layout.activity_tag);
		SharedPreferences mgr = PreferenceManager.getDefaultSharedPreferences(this);
		soundOrVib = mgr.getString("sound", "default");
		resolveIntent(getIntent());
		res = getResources();

		try {
			rcFacade = new RationCardFacade(this);
			familyFacade = new FamilyFacade(this);
			bf = new BeneficiaryFacade(this);
		} catch (Exception e) {
			e.printStackTrace();
		}

		//Displaying the name of the agent
		TextView agentName = (TextView) findViewById(R.id.agentName);
		agentName.setText("Agent : "+user.getFirstName().charAt(0)+" "+LoginActivity.user.getLastName());

	}
	
	@Override
	protected void onResume() {
		super.onResume();
		NfcAdapter mAdapter = NfcAdapter.getDefaultAdapter(this);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		mAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
	}


	@Override
	public void onNewIntent(Intent intent) {
		setIntent(intent);
		resolveIntent(intent);
	}


	void resolveIntent(Intent intent) {
		// Parse the intent
		String action = intent.getAction();
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
			Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
			NdefMessage[] messages = null;
			if (rawMsgs != null) {
				messages = new NdefMessage[rawMsgs.length];
				for (int i = 0; i < rawMsgs.length; i++) {
					messages[i] = (NdefMessage) rawMsgs[i];
				}
				if (messages.length != 0) {
					NdefRecord record = messages[0].getRecords()[0];
					String message = getTextData(record.getPayload());
					idTag = message;
					if(!idTag.isEmpty()){
						processTag(idTag);
					}
					else{
						alert = new AlertDialog.Builder(TagActivity.this);
						DialogHelper.showErrorDialog(alert, getString(R.string.error), getString(R.string.tagNotSupported));
					}
				}
			}
		}
	}

	public void processTag(String tagId){
		System.out.println(tagId);
		try {
			rationCard = rcFacade.findByTagId(tagId);				
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(rationCard != null){	

			family = rationCard.getFamily();		

			Intent intent = new Intent(TagActivity.this, ListBeneficiariesActivity.class);
			startActivity(intent);
			finish();
		}


		else{
			alert = new AlertDialog.Builder(TagActivity.this);
			DialogHelper.showErrorDialog(alert,getString( R.string.code_501),getString( R.string.code_501_message));
			System.out.println("No RationCard returned with this tagId :"+tagId);
		}


	}

	String getTextData(byte[] payload) {
		String textEncoding = ((payload[0] & 0200) == 0) ? "UTF-8" : "UTF-16";
		int languageCodeLength = payload[0] & 0077;
		try {
			return new String(payload,languageCodeLength+1,payload.length - languageCodeLength - 1, textEncoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";

	}

	/**
	 * overriding Action to do when click on phone back button
	 */
	@Override 
	public void onBackPressed() {
		alert = new AlertDialog.Builder(this);
		alert.setMessage(getString(R.string.quitconfirm)+" "+getString(R.string.logout_message));
		alert.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dlg, int sumthin) {
				DialogHelper.processingDisconnection(progressDialog, TagActivity.this);
			}
		});
		alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		alert.show();
	}
}