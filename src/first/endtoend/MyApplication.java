package first.endtoend;

import first.endtoend.helpers.Constant;
import fr.unice.mbds.nfc.library.ApduCallBack;
import fr.unice.mbds.nfc.library.ApduError;
import fr.unice.mbds.nfc.library.NfcController;
import android.app.Application;

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
		System.out.println("init activity");
		ctrl = new NfcController(getApplicationContext(), this);
		ctrl.setAppletAID(Constant.APPLET_AID);
		ctrl.setCLA(Constant.CLA);
		ctrl.initService();
	}

	public NfcController getController() {
		return ctrl;

	}
	
	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
		//ctrl.destroy();
	}

	@Override
	public void onNotConnected() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnected() {
		// TODO Auto-generated method stub
		ctrl.sayHello(Constant.SAY_HELLO_CODE);
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
	public void onResponse(String[] response, int commandId) {
		// TODO Auto-generated method stub
		switch (commandId) {
		case Constant.SAY_HELLO_CODE:
			areSEAndAppletPresents = true;
			break;
		}
		
	}

	@Override
	public void onPINRequired() {
		// TODO Auto-generated method stub
		
	}

}
