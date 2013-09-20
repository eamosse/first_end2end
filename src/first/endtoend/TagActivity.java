package first.endtoend;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

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
import first.endtoend.helpers.Crypto;
import first.endtoend.helpers.DialogHelper;
import first.endtoend.helpers.JsonHelper;
import first.endtoend.models.FIAgent;
import first.endtoend.models.Family;
import first.endtoend.models.RationCard;


public class TagActivity extends MyActivity {


	public static String idTag;

	String soundOrVib,messageReceived;
	Resources res;

	public ProgressDialog progressDialog;

	AlertDialog.Builder alert;
	SharedPreferences settings;
	String PREF_NAME = "my_pref_settings";
	RationCardFacade rcFacade;

	FamilyFacade familyFacade;
	BeneficiaryFacade bf;

	private FIAgent user;
	private String decryptKey;

	public static Family family;
	public static RationCard rationCard;
	Parcelable[] rawMsgs;
	NdefMessage[] messages;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		settings = getSharedPreferences(PREF_NAME, 0);
		String storedUserString  = settings.getString(Constant.USER, ""); 
		if(!storedUserString.isEmpty()){
			user = JsonHelper.getObjectFromJson(storedUserString,FIAgent.class,"");
		}

		//TODO: if no user is found show an alert message and close this 
		progressDialog = new ProgressDialog(this);

		setContentView(R.layout.activity_tag);
		SharedPreferences mgr = PreferenceManager.getDefaultSharedPreferences(this);
		soundOrVib = mgr.getString("sound", "default");
		try {
			resolveIntent(getIntent());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
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
		try {
			resolveIntent(intent);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}


	void resolveIntent(Intent intent){



		// Parse the intent
		String action = intent.getAction();
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
			rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
			ctrl.execute("select key from pds_applet", Constant.GET_KEY_CODE);
			DialogHelper.showProcessingDialog(progressDialog, "Searching", "Please wait ...");
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

	String getTextData(byte[] payload, String decryptKey) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException {
		Crypto cr = new Crypto(decryptKey);
		String textEncoding = ((payload[0] & 0200) == 0) ? "UTF-8" : "UTF-16";
		int languageCodeLength = payload[0] & 0077;
		try {
			String message = cr.decrypt(new String(payload,languageCodeLength+1,payload.length - languageCodeLength - 1, textEncoding));
			System.out.println("Message decrypt : "+message);
			return message;
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
		alert.setTitle(R.string.exit);
		alert.setMessage(getString(R.string.quitconfirm));
		alert.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dlg, int sumthin) {
				logout();
			}
		});
		alert.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		alert.show();
	}


	@Override
	public void onResponse(Map<String, Object> results, int code) {
		if(code == Constant.GET_KEY_CODE){
			progressDialog.dismiss();
			decryptKey = String.valueOf(results.get("key"));
			System.out.println("Decrypt key --> "+decryptKey);
			if (rawMsgs != null) {
				messages = new NdefMessage[rawMsgs.length];
				for (int i = 0; i < rawMsgs.length; i++) {
					messages[i] = (NdefMessage) rawMsgs[i];
				}
				if (messages.length != 0) {
					NdefRecord record = messages[0].getRecords()[0];
					
						try {
							idTag = getTextData(record.getPayload(), decryptKey);
						} catch (InvalidKeyException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (NoSuchAlgorithmException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvalidKeySpecException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (NoSuchPaddingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalBlockSizeException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (BadPaddingException e) {
							// TODO Auto-generated catch block
							alert = new AlertDialog.Builder(TagActivity.this);
							DialogHelper.showErrorDialog(alert, getString(R.string.error), "Wrong data format");
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						System.out.println("IdTag ==> "+idTag);
					
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
}