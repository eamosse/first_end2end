package first.endtoend;

import java.util.Map;

import android.app.Application;

import com.api.sqlitehelper.Utils;
import com.first.nfc.apduql.ApduCallBack;
import com.first.nfc.apduql.exceptions.*;
import com.first.nfc.apduql.NfcController;

import first.endtoend.dal.EndToEndSqliteHelper;
import first.endtoend.helpers.Constant;

public class MyApplication extends Application implements ApduCallBack{
	 NfcController ctrl;
	 public boolean areSEAndAppletPresents = false;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		init();
	}

	protected void init() {
		Utils.helper = new EndToEndSqliteHelper(this, null);
		System.out.println("init activity");
		ctrl = new NfcController(getApplicationContext(), this);
		ctrl.initService();
	}

	public NfcController getController() {
		return ctrl;

	}
	

	@Override
	public void onNotConnected() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnected() {
		System.out.println("Connected to SE");
		ctrl.sayHello("pds_applet",Constant.SAY_HELLO_CODE);
	}

	@Override
	public void onNoSecureElement() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNoReader() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNoApplet() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAPDUError(ApduError error) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onIOException(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPINRequired() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBadRequest(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onResponse(Map<String, Object> results, int code) {
		// TODO Auto-generated method stub
		switch (code) {
		case Constant.SAY_HELLO_CODE:
			areSEAndAppletPresents = true;
			break;
		}
	}

}
