package first.endtoend;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.cookie.Cookie;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.first.nfc.apduql.exceptions.ApduError;
import com.google.android.gcm.GCMRegistrar;
import com.google.gson.reflect.TypeToken;

import first.endtoend.facades.AddressFacade;
import first.endtoend.facades.AidFacade;
import first.endtoend.facades.BeneficiaryFacade;
import first.endtoend.facades.CategoryFacade;
import first.endtoend.facades.FamilyFacade;
import first.endtoend.facades.PortfolioDetailFacade;
import first.endtoend.facades.PortfolioFacade;
import first.endtoend.facades.ProductFacade;
import first.endtoend.facades.RationCardFacade;
import first.endtoend.helpers.Constant;
import first.endtoend.helpers.DialogHelper;
import first.endtoend.helpers.InternetHelper;
import first.endtoend.helpers.JsonHelper;
import first.endtoend.models.Address;
import first.endtoend.models.Aid;
import first.endtoend.models.Beneficiary;
import first.endtoend.models.Category;
import first.endtoend.models.FIAgent;
import first.endtoend.models.Family;
import first.endtoend.models.Portfolio;
import first.endtoend.models.PortfolioDetail;
import first.endtoend.models.Product;
import first.endtoend.models.RationCard;

public class LoginActivity extends MyActivity {
	String username, password, message,regId, usernameStored, passwordStored;
	public static FIAgent user;
	public static List<Cookie> cookies;
	AlertDialog.Builder alert;
	SharedPreferences settings;
	String PREF_NAME = "my_pref_settings";
	ProgressDialog dialog;
	Boolean hasRun;
	EditText editLogin, editPassword;
	CategoryFacade categoryFacade;
	ProductFacade productFacade;
	AidFacade aidFacade;
	FamilyFacade familyFacade;
	BeneficiaryFacade beneficiaryFacade;
	PortfolioFacade portfolioFacade;
	PortfolioDetailFacade portfolioDetailFacade;
	AddressFacade addressFacade;
	RationCardFacade rationCardFacade;
	AQuery aquery;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		getActionBar().hide();
		dialog = new ProgressDialog(this);
		aquery = new AQuery(this);
		settings = getSharedPreferences(PREF_NAME, 0); // load the preferences
		hasRun = settings.getBoolean("hasRun", false); // see if it's run before, default no		
		editLogin = (EditText) findViewById(R.id.editLogin);
		editPassword = (EditText) findViewById(R.id.editPassword);
		categoryFacade = new CategoryFacade(this);
		productFacade = new ProductFacade(this);
		aidFacade = new AidFacade(this);
		familyFacade = new FamilyFacade(this);
		beneficiaryFacade = new BeneficiaryFacade(this);
		portfolioFacade = new PortfolioFacade(this);
		portfolioDetailFacade = new PortfolioDetailFacade(this);
		addressFacade = new AddressFacade(this);
		rationCardFacade = new RationCardFacade(this);


		Button connexion = (Button) findViewById(R.id.boutonConnexion);
		connexion.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				username = editLogin.getText().toString();
				password = editPassword.getText().toString();

				if ((username.equals("")) && (password.equals(""))) {
					Toast.makeText(LoginActivity.this, R.string.fill,
							Toast.LENGTH_SHORT).show();

				} else {
					ctrl.execute("select username, password from pds_applet", 
							Constant.GET_LOGIN_INFO_CODE);
				}
			}
		});
	}

	
	/**
	 * Method to process login, either online or offline
	 * @param username
	 * @param password
	 */
	private void loginProcess(String username, String password){
		if(loginAndPasswordOk(username, password)){
			if (!hasRun) {
				loginOnServer(username, password);
			} else {
				user = retrieveUserStored();
				Intent intent = new Intent(LoginActivity.this,TagActivity.class);
				startActivity(intent);
				finish();
			}
		}
		else{
			alert = new AlertDialog.Builder(LoginActivity.this);
			DialogHelper.showErrorDialog(alert, getString(R.string.error),
					getString(R.string.wrong));
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == Constant.PIN_LAUNCHED_REQUEST && resultCode == RESULT_OK){
			if(loginAndPasswordOk(username, password)){
				if (!hasRun) {
					loginOnServer(username, password);
				} else {
					user = retrieveUserStored();
					Intent intent = new Intent(LoginActivity.this,TagActivity.class);
					startActivity(intent);
					finish();
				}
			}
			else{
				alert = new AlertDialog.Builder(LoginActivity.this);
				DialogHelper.showErrorDialog(alert, getString(R.string.error),
						getString(R.string.wrong));
			}
		}
	}

	private void loginOnServer(String username, String password) {
		// code for if this is the first time the app has run
		String url = getString(R.string.url)+ getString(R.string.connexion);

		if (InternetHelper.isOnline(LoginActivity.this)) {
			async_post(url, true, false, username, password);
		} else {
			alert = new AlertDialog.Builder(LoginActivity.this);
			showInternetErrorDialog(0);
		}
	}


	@Override
	public void onBackPressed() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setMessage(getString(R.string.quitconfirm));
		alert.setPositiveButton(R.string.yes,
				new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dlg, int sumthin) {
				finish();
			}
		});
		alert.setNegativeButton(R.string.no,
				new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		alert.show();
	}

	/**
	 * Method for connection online
	 * 
	 * @param url
	 * @param ajax
	 * @param c
	 * @param username
	 * @param password
	 */
	public void async_post(String url, boolean ajax, boolean c,
			final String username, final String password) {

		DialogHelper.showProcessingDialog(dialog, getString(R.string.login),
				getString(R.string.wait));


		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put(Constant.SPRING_SECURITY_USERNAME, username);
			params.put(Constant.SPRING_SECURITY_PASSWORD, password);
			params.put(Constant.SPRING_SECURITY_REMEMBER_ME, "true");
			AjaxCallback<String> cb = new AjaxCallback<String>() {
				@Override
				public void callback(String url, String json, AjaxStatus status) {
					switch ((status.getCode())) {
					case Constant.REQUEST_OK: // request http success
						// get the cookies set by spring security
						cookies = status.getCookies();
						int code = JsonHelper.getResponseCodeFromJson(json);
						switch (code) {
						case Constant.REQUEST_OK:

							cookies = status.getCookies();
							user = JsonHelper.getObjectFromJson(json,
									FIAgent.class, Constant.USER);
							SharedPreferences.Editor edit = settings.edit();
							edit.putString(Constant.USER, JsonHelper
									.createJsonObject(user).toString());
							for (Cookie cookie : cookies) {
								if (cookie.getName()
										.equals(Constant.SESSION_ID)) {
									edit.putString("cookieName",
											cookie.getName());
									edit.putString("cookieValue",
											cookie.getValue());
									// edit.putLong("cookieExpiryDate",
									// cookie.getExpiryDate().getTime());

									edit.commit(); // apply
								}

							}
							load_categories();
							break;

						case Constant.NOT_FOUND:
							alert = new AlertDialog.Builder(LoginActivity.this);
							DialogHelper.showErrorDialog(alert, getString(R.string.error),
									getString(R.string.wrong));							
							break;
						}
						break;

					default: // Request http failed

						String msg = getString(R.string.code_404) + " "
								+ getString(R.string.request_not_received);
						showErrorDialog(msg, 0);
						break;
					}
					cookies = status.getCookies();
				}
			};
			cb.header("X-Requested-With", "true");

			aquery.progress(dialog).ajax(url, params, String.class, cb);

		} catch (Exception e) {
			e.printStackTrace(); // To change body of catch statement use File |
		}
	}

	/**
	 * 
	 * @return the GCM Registration Id for this phone
	 */
	public String getGCMRegistrationId(Context context) {
		GCMRegistrar.checkDevice(context);
		GCMRegistrar.checkManifest(context);
		regId = GCMRegistrar.getRegistrationId(context);
		if (regId == "") {
			GCMRegistrar.register(context, Constant.GCM_ID);
			regId = GCMRegistrar.getRegistrationId(context);
		}
		return regId;
	}

	public void sendRegId() {
		if (InternetHelper.isOnline(LoginActivity.this)) {
			final String cookieName = settings.getString("cookieName", "");
			final String cookieValue = settings.getString("cookieValue", "");

			String regId = getGCMRegistrationId(getApplicationContext());
			String url = getString(R.string.url)
					+ getString(R.string.sendRegId);
			DialogHelper.showProcessingDialog(dialog,
					getString(R.string.sending), getString(R.string.sending));

			final HashMap<String, String> params = new HashMap<String, String>();
			params.put(Constant.REG_ID, regId);

			aquery.progress(dialog).ajax(url, params, JSONObject.class,
					new AjaxCallback<JSONObject>() {
				@Override
				public void callback(String url, JSONObject object,
						AjaxStatus status) {
					switch (status.getCode()) {
					case Constant.REQUEST_OK:

						int code = JsonHelper
						.getResponseCodeFromJson(object);
						System.out
						.println("SendRegId Response code is :"
								+ code);
						switch (code) {
						case Constant.REQUEST_OK:

							storeUser();

							alert = new AlertDialog.Builder(
									LoginActivity.this)
							.setTitle(R.string.information)
							.setIcon(R.drawable.successicon)
							.setMessage(
									R.string.load_successfully)
									.setPositiveButton(
											R.string.ok,
											new DialogInterface.OnClickListener() {
												@Override
												public void onClick(
														DialogInterface dialog,
														int which) {
													Intent intent = new Intent(
															LoginActivity.this,
															TagActivity.class);
													startActivity(intent);
													finish();
												}
											});
							alert.show();
							break;

						default:
							message = getString(R.string.error_while_sending)
							+ " " + Constant.REG_ID;
							showErrorDialog(message, 1);
							break;
						}
						break;

					default:
						message = getString(R.string.error_while_sending)
						+ " " + Constant.REG_ID;
						showErrorDialog(message, 1);
						break;
					}
				}
			}.cookie(cookieName, cookieValue));
		} else {
			showInternetErrorDialog(1);
		}
	}

	public void load_categories() {
		if (InternetHelper.isOnline(LoginActivity.this)) {
			System.out.println(">>>>>>>>>> Loading Categories <<<<<<<");
			String url = getString(R.string.url)
					+ getString(R.string.load_Categories);
			DialogHelper.showProcessingDialog(dialog,
					getString(R.string.loading),
					getString(R.string.wait_loading));
			AjaxCallback<String> cb = new AjaxCallback<String>() {
				@Override
				public void callback(String url, String json, AjaxStatus status) {
					switch (status.getCode()) {
					case Constant.REQUEST_OK:
						if (!status.getCookies().isEmpty())
							LoginActivity.cookies = status.getCookies();
						int code = JsonHelper.getResponseCodeFromJson(json);
						switch (code) {
						case Constant.REQUEST_OK:
							Type listType = new TypeToken<ArrayList<Category>>() {
							}.getType();
							ArrayList<Category> categories = JsonHelper
									.getListObjectFromJson(json,
											Constant.LIST_CATEGORIES, listType);

							if (!categories.isEmpty())
								for (Category c : categories)
									try {
										categoryFacade.insert(c);
									} catch (Exception e) {
										e.printStackTrace();
									}

							System.out
							.println("<<<<>>>> End Loading Categories");
							load_products();
							break;

						default:
							message = getString(R.string.error_while_loading)
							+ " " + Constant.LIST_CATEGORIES;
							showErrorDialog(message, 2);
							break;
						}
						break;

					default:
						message = getString(R.string.error_while_loading) + " "
								+ Constant.LIST_CATEGORIES + ". "
								+ getString(R.string.request_not_received);
						showErrorDialog(message, 2);
						break;
					}
				}
			};
			if (LoginActivity.cookies != null)
				for (Cookie cookie : LoginActivity.cookies) {
					Log.v("Adding cookie", cookie.getName() + " Value "
							+ cookie.getValue());
					cb.cookie(cookie.getName(), cookie.getValue());
				}
			aquery.progress(dialog).ajax(url, null, String.class, cb);
		} else {
			showInternetErrorDialog(2);
		}
	}

	public void load_products() {
		if (InternetHelper.isOnline(LoginActivity.this)) {
			System.out.println(">>>>>>< Loading Products >><<<<<<<");
			String url = getString(R.string.url)
					+ getString(R.string.load_Products);
			DialogHelper.showProcessingDialog(dialog,
					getString(R.string.loading),
					getString(R.string.wait_loading));
			AjaxCallback<String> cb = new AjaxCallback<String>() {

				@Override
				public void callback(String url, String json, AjaxStatus status) {
					switch (status.getCode()) {
					case Constant.REQUEST_OK:
						if (!status.getCookies().isEmpty())
							LoginActivity.cookies = status.getCookies();
						int code = JsonHelper.getResponseCodeFromJson(json);
						switch (code) {
						case Constant.REQUEST_OK:
							Type listType = new TypeToken<ArrayList<Product>>() {
							}.getType();
							ArrayList<Product> products = JsonHelper
									.getListObjectFromJson(json,
											Constant.LIST_PRODUCTS, listType);

							if (!products.isEmpty())

								for (Product p : products)
									try {
										if (p.getIconeURL() == null)
											p.setIconeURL("");
										productFacade.insert(p);

										String path = getString(R.string.productPhotosOffLine)
												+ p.getIconeURL();
										File file = new File(
												Environment
												.getExternalStorageDirectory(),
												path);

										if (!file.exists()) {
											download(
													getString(R.string.url)
													+ getString(R.string.productPhotosOnLine),
													p.getIconeURL(),
													getString(R.string.productPhotosOffLine));
										}
									} catch (Exception e) {
										e.printStackTrace();
									}
							System.out
							.println(">>>>>>>>>>>  End Loading Product <<<<<<<<<<<<");
							load_aids();
							break;

						default:
							message = getString(R.string.error_while_loading)
							+ " " + Constant.LIST_PRODUCTS;
							showErrorDialog(message, 3);
							break;
						}
						break;

					default:
						message = getString(R.string.error_while_loading) + " "
								+ Constant.LIST_PRODUCTS + ". "
								+ getString(R.string.request_not_received);
						showErrorDialog(message, 3);
						break;
					}
				}
			};
			if (LoginActivity.cookies != null)
				for (Cookie cookie : LoginActivity.cookies) {
					Log.v("Adding cookie", cookie.getName() + " Value "
							+ cookie.getValue());
					cb.cookie(cookie.getName(), cookie.getValue());
				}
			aquery.progress(dialog).ajax(url, null, String.class, cb);
		} else {
			showInternetErrorDialog(3);
		}
	}

	public void load_aids() {
		if (InternetHelper.isOnline(LoginActivity.this)) {
			System.out.println(">>>>>>> Loading Aids <<<<<<<<<<<<<<<");
			String url = getString(R.string.url)
					+ getString(R.string.load_Aids);
			DialogHelper.showProcessingDialog(dialog,
					getString(R.string.loading),
					getString(R.string.wait_loading));
			AjaxCallback<String> cb = new AjaxCallback<String>() {
				@Override
				public void callback(String url, String json, AjaxStatus status) {
					switch (status.getCode()) {
					case Constant.REQUEST_OK:
						if (!status.getCookies().isEmpty())
							LoginActivity.cookies = status.getCookies();
						int code = JsonHelper.getResponseCodeFromJson(json);
						switch (code) {
						case Constant.REQUEST_OK:
							Type listType = new TypeToken<ArrayList<Aid>>() {
							}.getType();
							ArrayList<Aid> aids = JsonHelper
									.getListObjectFromJson(json,
											Constant.LIST_AIDS, listType);

							if (!aids.isEmpty())
								for (Aid a : aids)
									try {
										aidFacade.insert(a);
									} catch (Exception e) {
										e.printStackTrace();
									}
							System.out
							.println(">>>>>> End Loading Aids >>>>>><<<<<<<");
							load_families();
							break;

						case Constant.NULL_RESPONSE:
							System.out
							.println("LoginActivity Load aids : No Aids yet");
							load_families();
							break;

						default:
							message = getString(R.string.error_while_loading)
							+ " " + Constant.LIST_AIDS;
							showErrorDialog(message, 4);
							break;
						}
						break;

					default:
						message = getString(R.string.error_while_loading) + " "
								+ Constant.LIST_AIDS + ". "
								+ getString(R.string.request_not_received);
						showErrorDialog(message, 4);
						break;
					}
				}
			};
			if (LoginActivity.cookies != null)
				for (Cookie cookie : LoginActivity.cookies) {
					Log.v("Adding cookie", cookie.getName() + " Value "
							+ cookie.getValue());
					cb.cookie(cookie.getName(), cookie.getValue());
				}
			aquery.progress(dialog).ajax(url, null, String.class, cb);
		} else {
			showInternetErrorDialog(4);
		}
	}

	public void load_families() {
		if (InternetHelper.isOnline(LoginActivity.this)) {
			System.out.println(">>>>> Loading Families <<<<<<<<<<<<");
			String url = getString(R.string.url)
					+ getString(R.string.load_Families);
			DialogHelper.showProcessingDialog(dialog,
					getString(R.string.loading),
					getString(R.string.wait_loading));
			AjaxCallback<String> cb = new AjaxCallback<String>() {

				@Override
				public void callback(String url, String json, AjaxStatus status) {
					switch (status.getCode()) {
					case Constant.REQUEST_OK:
						if (!status.getCookies().isEmpty())
							LoginActivity.cookies = status.getCookies();
						int code = JsonHelper.getResponseCodeFromJson(json);
						switch (code) {
						case Constant.REQUEST_OK:
							Type listType = new TypeToken<ArrayList<Family>>() {
							}.getType();
							ArrayList<Family> families = JsonHelper
									.getListObjectFromJson(json,
											Constant.LIST_FAMILIES, listType);
							if (!families.isEmpty())

								for (Family f : families) {
									try {
										familyFacade.insert(f);

										for (Beneficiary fib : f
												.getBeneficiaries()) {
											fib.setFamily(f);
											beneficiaryFacade.insert(fib);

											String path = getString(R.string.benefPhotosOffLine)
													+ fib.getPhotoURL();
											File file = new File(
													Environment
													.getExternalStorageDirectory(),
													path);
											if (!file.exists()) {
												download(
														getString(R.string.url)
														+ getString(R.string.benefPhotosOnLine),
														fib.getPhotoURL(),
														getString(R.string.benefPhotosOffLine));
											}
										}

										Portfolio p = f.getPortfolio();
										if (p != null) {
											p.setFamily(f);
											portfolioFacade.insert(p);

											for (PortfolioDetail pfd : p
													.getDetails()) {
												pfd.setPortfolio(p);
												portfolioDetailFacade
												.insert(pfd);
											}
										}

										RationCard r = f.getRationCard();
										if (r != null) {
											r.setFamily(f);
											rationCardFacade.insert(r);
										}

										Address adr = f.getAddress();
										if (adr != null) {
											adr.setFamily(f);
											addressFacade.insert(adr);
										}
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							System.out
							.println(">>>>>>>> End Loading all Families <<<<<<<<<<");
							sendRegId();
							break;

						case Constant.NULL_RESPONSE:
							System.out
							.println("LoginActivity load families : No Families yet!");
							sendRegId();

							break;

						default:
							message = getString(R.string.error_while_loading)
							+ " " + Constant.LIST_FAMILIES;
							showErrorDialog(message, 5);
							break;
						}
						break;

					default:
						message = getString(R.string.error_while_loading) + " "
								+ Constant.LIST_FAMILIES + ". "
								+ getString(R.string.request_not_received);
						showErrorDialog(message, 5);
						break;
					}
				}
			};
			if (LoginActivity.cookies != null)
				for (Cookie cookie : LoginActivity.cookies) {
					Log.v("Adding cookie", cookie.getName() + " Value "
							+ cookie.getValue());
					cb.cookie(cookie.getName(), cookie.getValue());
				}
			aquery.progress(dialog).ajax(url, null, String.class, cb);
		} else {
			showInternetErrorDialog(5);
		}
	}

	public void download(String url, String imageName, String location) {
		File ext = Environment.getExternalStorageDirectory();
		File target = new File(ext, location + imageName);
		aquery.download(url + imageName, target, new AjaxCallback<File>() {
			@Override
			public void callback(String url, File file, AjaxStatus status) {
				if (file == null) {
					System.out.println("Failed to load file");
				}
			}
		});
	}

	/**
	 * Action to launch when internet error
	 * 
	 * @param action
	 */

	public void showInternetErrorDialog(final int action) {
		AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
		alert.setTitle(R.string.internet_error);
		alert.setIcon(android.R.drawable.ic_dialog_alert);
		alert.setMessage(R.string.internet_error_message);
		alert.setPositiveButton(R.string.settings,new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				LoginActivity.this.startActivity(new Intent(
						Settings.ACTION_WIFI_SETTINGS));
			}
		});
		alert.setNegativeButton(R.string.retry,	new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				actionToDoWhenErrorOccurs(action);
			}
		});
		alert.show();
	}

	/**
	 * Action to launch when error occured while processing with server
	 * 
	 * @param message
	 * @param action
	 */
	public void showErrorDialog(String message, final int action) {
		AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
		alert.setIcon(android.R.drawable.ic_delete);
		alert.setTitle(R.string.error);
		alert.setMessage(message);
		alert.setPositiveButton(R.string.retry,	new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				actionToDoWhenErrorOccurs(action);
			}
		});
		alert.setNegativeButton(R.string.close,	new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		alert.show();
	}

	@Override
	public void onAPDUError(ApduError error) {
		Toast.makeText(this, error.getMessage(), Toast.LENGTH_LONG).show();
		System.out.println("error code " + error.getApduError()[0]);
	}


	@Override
	public void onResponse(Map<String, Object> results, int commandId) {
		switch (commandId) {
		case Constant.GET_LOGIN_INFO_CODE:
			if(results.size()>= 2) {
				usernameStored = String.valueOf(results.get("username"));
				passwordStored = String.valueOf(results.get("password"));
				loginProcess(username, password);
			}
			break;
		}
	}

	private FIAgent retrieveUserStored() {		
		String firstNameStored = settings.getString("firstName", "");
		String lastNameStored = settings.getString("lastName", "");
		String sessionIdStored = settings.getString("sessionId", "");
		int agentIdStored = settings.getInt("agentId", 0);
		return new FIAgent(agentIdStored, firstNameStored,
				lastNameStored,sessionIdStored);
	}


	private boolean loginAndPasswordOk(String username, String password){
		return (username.equals(usernameStored)	&& password.equals(passwordStored));
		
	}

	private void storeUser() {
		SharedPreferences.Editor edit = settings.edit();
		// Storing the user agent data
		edit.putInt("agentId", user.getId());								
		edit.putString("firstName",	user.getFirstName());
		edit.putString("lastName",user.getLastName());
		edit.putString("sessionId",user.getSessionId());
		edit.putBoolean("hasRun", true); // set to hasrun
		edit.commit(); // apply	
	}

	public void actionToDoWhenErrorOccurs(int action){
		switch (action) {
		case 0:
			if(InternetHelper.isOnline(LoginActivity.this)){
				async_post(getString(R.string.url)
						+ getString(R.string.connexion), true,
						false, usernameStored, passwordStored);
			}else{
				showInternetErrorDialog(0);
			}
			break;
		case 1: sendRegId(); break;
		case 2:	load_categories(); break;
		case 3:	load_products(); break;
		case 4: load_aids(); break;
		case 5: load_families(); break;
		}
	}
}
