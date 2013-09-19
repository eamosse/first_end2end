package first.endtoend;

import java.util.Map;

import android.app.Application;

import com.first.nfc.apduql.ApduCallBack;
import com.first.nfc.apduql.ApduError;
import com.first.nfc.apduql.Applet;
import com.first.nfc.apduql.Command;
import com.first.nfc.apduql.Configuration;
import com.first.nfc.apduql.FieldModel;
import com.first.nfc.apduql.InstructionModel;
import com.first.nfc.apduql.NfcController;

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
		System.out.println("init activity");
		ctrl = new NfcController(getApplicationContext(), this);
		//ctrl.setAppletAID(Constant.APPLET_AID);
//		ctrl.setCLA(Constant.CLA);
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
		// TODO Auto-generated method stub
//		String AID = "A00041445000000001";
//		Applet pds_applet = new Applet();
//		pds_applet.setName("pds_applet");
//		pds_applet.setAID(AID);
//		
//		try {
//		
//			pds_applet.addClass(new InstructionModel(Command.SELECT, "B0"));
//			pds_applet.addClass(new InstructionModel(Command.INSERT, "C0"));
//			pds_applet.addFields(new FieldModel("username", "30", 9));
//			pds_applet.addFields(new FieldModel("password", "40",9));
//			pds_applet.addFields(new FieldModel("pin", "50",4));
//			pds_applet.addFields(new FieldModel("key", "31",255));
//			Configuration.save(pds_applet);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		System.out.println("Connected");
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
