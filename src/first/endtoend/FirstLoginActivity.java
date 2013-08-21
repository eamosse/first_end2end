package first.endtoend;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.cookie.Cookie;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.android.gcm.GCMRegistrar;

import first.endtoend.helpers.Constant;
import first.endtoend.helpers.DialogHelper;
import first.endtoend.helpers.InternetHelper;
import first.endtoend.helpers.JsonHelper;
import first.endtoend.models.FIAgent;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class FirstLoginActivity extends Activity {
	

	// Values for email and password at the time of the login attempt.
	private String username;
	private String password;

	// UI references.
	private EditText usernameView;
	private EditText passwordView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;
	//String username, password;
	public String messageReceived;
	public  FIAgent user;
	public static  List<Cookie> cookies;
	AlertDialog.Builder alert;
	SharedPreferences settings;
	String PREF_NAME = "my_pref_settings";
	ProgressDialog dialog;
	String regId;
	AQuery aquery;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dialog = new ProgressDialog(this);
		aquery = new AQuery(this);
		setContentView(R.layout.activity_first_login);
		settings = getSharedPreferences(PREF_NAME, 0); //load the preferences
		// Set up the login form.
		//mEmail = getIntent().getStringExtra(EXTRA_EMAIL);
		String storedUserString  = settings.getString(Constant.USER, ""); 
		System.out.println("user " + storedUserString);
		if(!storedUserString.isEmpty()){
			user = JsonHelper.getObjectFromJson(storedUserString,FIAgent.class,"");
		}
		username = user!=null ? user.getUsername() : "";
		usernameView = (EditText) findViewById(R.id.email);
		usernameView.setText(username);

		passwordView = (EditText) findViewById(R.id.password);
		passwordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView, int id,
					KeyEvent keyEvent) {
				if (id == R.id.login || id == EditorInfo.IME_NULL) {
					attemptLogin();
					return true;
				}
				return false;
			}
		});

		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptLogin();
					}
				});
	}



	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		// Reset errors.
		usernameView.setError(null);
		passwordView.setError(null);

		// Store values at the time of the login attempt.
		username = usernameView.getText().toString();
		password = passwordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(password)) {
			passwordView.setError(getString(R.string.error_field_required));
			focusView = passwordView;
			cancel = true;
		} else if (password.length() < 4) {
			passwordView.setError(getString(R.string.error_invalid_password));
			focusView = passwordView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(username)) {
			usernameView.setError(getString(R.string.error_field_required));
			focusView = usernameView;
			cancel = true;
		} 


		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			settings = getSharedPreferences(PREF_NAME, 0); //load the preferences
			Boolean hasRun = settings.getBoolean("hasRun", false); //see if it's run before, default no
			String url = getString(R.string.url) + getString(R.string.connexion);
			boolean isOnline= InternetHelper.isOnline(FirstLoginActivity.this);
			if (!hasRun) {
				if(isOnline)
					async_post(url, true, false,username, password);
				else {
					alert = new AlertDialog.Builder(FirstLoginActivity.this);
					DialogHelper.showSettingsDialog(FirstLoginActivity.this, alert);
				}
			}else{
				String usernameStored = user!=null ? user.getUsername():"";
				String passwordStored = user!=null ? user.getPassword(): "";
				if(username.equals(usernameStored) && password.equals(passwordStored)){
					Intent intent = new Intent(FirstLoginActivity.this, TagActivity.class);
					startActivity(intent);
					finish();
				}
				else{	
					alert = new AlertDialog.Builder(FirstLoginActivity.this);
					DialogHelper.showErrorDialog(alert,getString(R.string.error), getString(R.string.wrong));
				}
			}
		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
			.alpha(show ? 1 : 0)
			.setListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					mLoginStatusView.setVisibility(show ? View.VISIBLE
							: View.GONE);
				}
			});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
			.alpha(show ? 0 : 1)
			.setListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					mLoginFormView.setVisibility(show ? View.GONE
							: View.VISIBLE);
				}
			});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}


	/**
	 * Method for connection online
	 * @param url
	 * @param ajax
	 * @param c
	 * @param username
	 * @param password
	 */
	public void async_post(String url, boolean ajax, boolean c, final String username, final String password) {
		showProgress(true);
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put(Constant.SPRING_SECURITY_USERNAME, username);
			params.put(Constant.SPRING_SECURITY_PASSWORD, password);
			params.put(Constant.SPRING_SECURITY_REMEMBER_ME, "true");
			AjaxCallback<String> cb = new AjaxCallback<String>() {
				@Override
				public void callback(String url, String json, AjaxStatus status) {
					switch ((status.getCode())) {
					case Constant.REQUEST_OK:  //request http success
						System.out.println("the user returned :"+json);
						int code = JsonHelper.getResponseCodeFromJson(json);
						switch (code) {
						case Constant.REQUEST_OK:
							//get the cookies set by spring security
							cookies = status.getCookies();
							user =  JsonHelper.getObjectFromJson(json, FIAgent.class, Constant.USER);
							SharedPreferences.Editor edit = settings.edit();
							edit.putString(Constant.USER,JsonHelper.createJsonObject(user).toString());
							for (Cookie cookie : cookies){
								if(cookie.getName().equals(Constant.SESSION_ID)){
									edit.putString("cookieName", cookie.getName());
									edit.putString("cookieValue", cookie.getValue());
									//edit.putLong("cookieExpiryDate", cookie.getExpiryDate().getTime());

									edit.commit(); //apply
								}
							}
							//sendRegId();
							Boolean hasRun = settings.getBoolean("hasRun", false);
							if(hasRun)
								startActivity(new Intent(FirstLoginActivity.this, TagActivity.class));
							else
								startActivity(new Intent(FirstLoginActivity.this, SetupActivity.class));
							finish();
							break;

						case Constant.NOT_FOUND:
							showProgress(false);
							alert = new AlertDialog.Builder(FirstLoginActivity.this);
							DialogHelper.showErrorDialog(alert,getString(R.string.error), getString(R.string.wrong));
							break;
						}
						break;

					default: // Request http failed
						alert = new AlertDialog.Builder(FirstLoginActivity.this);
						DialogHelper.showErrorDialog(alert, getString(R.string.code_404),getString(R.string.request_not_received));
						showProgress(false);
						break;
					}
				}
			};
			cb.header("X-Requested-With", "true");

			aquery.progress(dialog).ajax(url, params, String.class, cb);
		} catch (Exception e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}
	}


	/**
	 * 
	 * @return the GCM Registration Id for this phone
	 */
	public String getGCMRegistrationId(Context context){

		GCMRegistrar.checkDevice(context);
		GCMRegistrar.checkManifest(context);
		regId = GCMRegistrar.getRegistrationId(context);
		Log.i("GCM Register info", "reg Id is : "+regId);
		if(regId == ""){
			GCMRegistrar.register(context, Constant.GCM_ID);
			regId = GCMRegistrar.getRegistrationId(context);

			Log.i("GCM Register info", "New GCM Register Id for this phone is : "+regId);
		}
		else {
			Log.d("GCM Register info", "this phone is already registered as :" + regId);
		}
		return regId;
	}



}
