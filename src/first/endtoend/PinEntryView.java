package first.endtoend;

import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.first.nfc.apduql.exceptions.*;

import first.endtoend.helpers.Constant;

public class PinEntryView extends MyActivity {

	String userEntered;
	
	final int PIN_LENGTH = 4;
	boolean keyPadLockedFlag = false;
	Context appContext;
	
	TextView titleView;
	
	TextView pinBox0;
	TextView pinBox1;
	TextView pinBox2;
	TextView pinBox3;
	
	TextView [] pinBoxArray;
	
	TextView statusView;
	
	Button button0;
	Button button1;
	Button button2;
	Button button3;
	Button button4;
	Button button5;
	Button button6;
	Button button7;
	Button button8;
	Button button9;
	Button button10;
	Button buttonExit;
	Button buttonDelete;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		appContext = this;
		userEntered = "";
		
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.activity_pin_entry_view);
		
		Typeface xpressive=Typeface.createFromAsset(getAssets(), "fonts/XpressiveBold.ttf");  
		
		buttonExit = (Button) findViewById(R.id.buttonExit);
		buttonExit.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		    
		    	//Exit app
		    	Intent i = new Intent();
		    	i.setAction(Intent.ACTION_MAIN);
		    	i.addCategory(Intent.CATEGORY_HOME);
		    	appContext.startActivity(i); 
		    	finish();
		        
		    }
		    
		    }
		);
		buttonExit.setTypeface(xpressive);
		
		
		buttonDelete = (Button) findViewById(R.id.buttonDeleteBack);
		buttonDelete.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		    
		    	if (keyPadLockedFlag == true)
		    	{
		    		return;
		    	}
		    	
		    	if (userEntered.length()>0)
		    	{
		    		userEntered = userEntered.substring(0,userEntered.length()-1);
		    		pinBoxArray[userEntered.length()].setText("");
		    	}
		    
		    }
		    
		    }
		);
		
		titleView = (TextView)findViewById(R.id.titleBox);
		titleView.setTypeface(xpressive);
		
		pinBox0 = (TextView)findViewById(R.id.pinBox0);
		pinBox1 = (TextView)findViewById(R.id.pinBox1);
		pinBox2 = (TextView)findViewById(R.id.pinBox2);
		pinBox3 = (TextView)findViewById(R.id.pinBox3);
		
		pinBoxArray = new TextView[PIN_LENGTH];
		pinBoxArray[0] = pinBox0;
		pinBoxArray[1] = pinBox1;
		pinBoxArray[2] = pinBox2;
		pinBoxArray[3] = pinBox3;
		
		
		
		statusView = (TextView) findViewById(R.id.statusMessage);
		statusView.setTypeface(xpressive);
	
		View.OnClickListener pinButtonHandler = new View.OnClickListener() {
		    public void onClick(View v) {
		    	
		    	if (keyPadLockedFlag == true)
		    	{
		    		return;
		    	}
		    	
		    	Button pressedButton = (Button)v;
	    		
		    	
		    	if (userEntered.length()<PIN_LENGTH)
		    	{
		    		userEntered = userEntered + pressedButton.getText();
		    		Log.v("PinView", "User entered="+userEntered);
		    		
		    		//Update pin boxes
		    		pinBoxArray[userEntered.length()-1].setText("8");
		    		
		    		if (userEntered.length()==PIN_LENGTH)
		    		{
		    			//Check if entered PIN is correct
		    			ctrl.execute("select pin from pds_applet where (pin="+userEntered+")", Constant.GET_PIN_CODE);	

		    		}	
		    	}
		    	else
		    	{
		    		//Roll over
		    		pinBoxArray[0].setText("");
		    		pinBoxArray[1].setText("");
		    		pinBoxArray[2].setText("");
		    		pinBoxArray[3].setText("");
		    		
		    		userEntered = "";
		    		
		    		statusView.setText("");
		    		
		    		userEntered = userEntered + pressedButton.getText();
		    		Log.v("PinView", "User entered="+userEntered);
		    		
		    		//Update pin boxes
		    		pinBoxArray[userEntered.length()-1].setText("8");
		    		
		    	}
		    	
		    	
		    }
		  };
		
		
		button0 = (Button)findViewById(R.id.button0);
		button0.setTypeface(xpressive);
		button0.setOnClickListener(pinButtonHandler);
		
		button1 = (Button)findViewById(R.id.button1);
		button1.setTypeface(xpressive);
		button1.setOnClickListener(pinButtonHandler);
		
		button2 = (Button)findViewById(R.id.button2);
		button2.setTypeface(xpressive);
		button2.setOnClickListener(pinButtonHandler);
		
		
		button3 = (Button)findViewById(R.id.button3);
		button3.setTypeface(xpressive);
		button3.setOnClickListener(pinButtonHandler);
		
		button4 = (Button)findViewById(R.id.button4);
		button4.setTypeface(xpressive);
		button4.setOnClickListener(pinButtonHandler);
		
		button5 = (Button)findViewById(R.id.button5);
		button5.setTypeface(xpressive);
		button5.setOnClickListener(pinButtonHandler);
		
		button6 = (Button)findViewById(R.id.button6);
		button6.setTypeface(xpressive);
		button6.setOnClickListener(pinButtonHandler);
		
		button7 = (Button)findViewById(R.id.button7);
		button7.setTypeface(xpressive);
		button7.setOnClickListener(pinButtonHandler);
		
		button8 = (Button)findViewById(R.id.button8);
		button8.setTypeface(xpressive);
		button8.setOnClickListener(pinButtonHandler);
		
		button9 = (Button)findViewById(R.id.button9);
		button9.setTypeface(xpressive);
		button9.setOnClickListener(pinButtonHandler);
		
		
		
		
		
		buttonDelete = (Button)findViewById(R.id.buttonDeleteBack);
		buttonDelete.setTypeface(xpressive);
		
		
		
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		
		//App not allowed to go back to Parent activity until correct pin entered.
		return;
		//super.onBackPressed();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.activity_pin_entry_view, menu);
		return true;
	}

	
	private class LockKeyPadOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
              for(int i=0;i<2;i++) {
                  try {
                      Thread.sleep(1000);
                  } catch (InterruptedException e) {
                      // TODO Auto-generated catch block
                      e.printStackTrace();
                  }
              }

              return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
                statusView.setText("");

            //Roll over
            pinBoxArray[0].setText("");
                pinBoxArray[1].setText("");
                pinBoxArray[2].setText("");
                pinBoxArray[3].setText("");

                userEntered = "";

                keyPadLockedFlag = false;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
  }
	
	@Override
	public void onAPDUError(ApduError error) {
		statusView.setTextColor(Color.RED);
		statusView.setText("Wrong PIN. Keypad Locked");
		keyPadLockedFlag = true;
		Log.v("PinView", "Wrong PIN");
		new LockKeyPadOperation().execute("");
	}




	@Override
	public void onResponse(Map<String, Object> results, int commandId) {
		switch (commandId) {
		case Constant.GET_PIN_CODE:
			System.out.println("Response received again!!!!!!!!!");
			statusView.setTextColor(Color.GREEN);
			statusView.setText("Correct");
			Log.v("PinView", "Correct PIN");
			ctrl.execute("select username, password from pds_applet", 
					Constant.GET_LOGIN_INFO_CODE);
//			setResult(RESULT_OK);
//			finish();
			break;

		case Constant.GET_LOGIN_INFO_CODE:
			if(results.size()>= 2) {
				LoginActivity.usernameStored = String.valueOf(results.get("username"));
				LoginActivity.passwordStored = String.valueOf(results.get("password"));
			}
			setResult(RESULT_OK);
			finish();
			break;
		}
		
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		//ctrl.destroy();
	}

	
	
}
