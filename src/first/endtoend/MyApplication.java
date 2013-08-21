package first.endtoend;

import first.endtoend.helpers.Constant;
import fr.unice.mbds.nfc.library.NfcController;
import android.app.Application;

public class MyApplication extends Application {
	 NfcController ctrl;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		init();
	}

	protected void init() {
		System.out.println("init activity");
		ctrl = new NfcController(getApplicationContext());
		ctrl.setAppletAID(Constant.APPLET_AID);
		ctrl.setCLA(Constant.CLA);
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

}
