package first.endtoend;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.google.android.gcm.GCMRegistrar;

import first.endtoend.helpers.Constant;

public class SplashScreenActivity extends Activity {

	private static final int STOPSPLASH = 0;
	/**
	 * Default duration for the splash screen (milliseconds)
	 */
	private static final long SPLASHTIME = 2000;

	SharedPreferences settings;
	String PREF_NAME = "my_pref_settings";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splashscreen);

		settings = getSharedPreferences(PREF_NAME, 0); //load the preferences
		Boolean hasRun = settings.getBoolean("hasRun", false); //see if it's run before, default no

		if (!hasRun) {	 
			GCMRegistrar.checkDevice(getApplicationContext());
			//uncomment below lines to unregister the device 
			GCMRegistrar.unregister(getApplicationContext());
			Log.d("info","unregistereddd....." + GCMRegistrar.getRegistrationId(getApplicationContext()));
			GCMRegistrar.checkManifest(getApplicationContext());
			if (GCMRegistrar.isRegistered(getApplicationContext())) {
				Log.d("info", GCMRegistrar.getRegistrationId(getApplicationContext()));
			}
			String regId = GCMRegistrar.getRegistrationId(getApplicationContext());
			if (regId.equals("")) {
				// replace this with the project ID
				GCMRegistrar.register(getApplicationContext(), Constant.GCM_ID);

				regId = GCMRegistrar.getRegistrationId(getApplicationContext());
				Log.d("info", regId);

			} else {
				Log.d("info", "already registered as" + regId);
			}
			
			settings.edit().putString("regId", regId);
			settings.edit().commit();

		}	
		
		final Message msg = new Message();
		msg.what = STOPSPLASH;
		splashHandler.sendMessageDelayed(msg, SPLASHTIME);

	}

	/**
	 * Handler to close this activity and to start automatically {@link MainActivity}
	 * after <code>SPLASHTIME</code> seconds.
	 */
	private final transient Handler splashHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			if (msg.what == STOPSPLASH)
			{
				final Animation animation = AnimationUtils.loadAnimation(getBaseContext(),
						android.R.anim.fade_out);
				animation.setAnimationListener(new AnimationListener()
				{

					public void onAnimationEnd(Animation arg0) {
						findViewById(R.id.splash).setVisibility(View.INVISIBLE);
						final Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
						startActivity(intent);
						finish();
					}

					public void onAnimationRepeat(Animation arg0) {
						// TODO Auto-generated method stub

					}

					public void onAnimationStart(Animation arg0) {
						// TODO Auto-generated method stub

					}
				});

				((LinearLayout)findViewById(R.id.splash)).startAnimation(animation);
			}


			super.handleMessage(msg);
		}
	};


}
