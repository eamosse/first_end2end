package first.endtoend;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;

import org.apache.http.cookie.Cookie;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.gson.reflect.TypeToken;

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
import first.endtoend.models.Aid;
import first.endtoend.models.Beneficiary;
import first.endtoend.models.Category;
import first.endtoend.models.FIAgent;
import first.endtoend.models.Family;
import first.endtoend.models.Portfolio;
import first.endtoend.models.PortfolioDetail;
import first.endtoend.models.Product;
import first.endtoend.models.RationCard;

public class LoadDataActivity extends MyActivity implements OnClickListener {

	AQuery aquery;
	CategoryFacade categoryFacade;
	ProductFacade productFacade;
	ProgressDialog dialog;
	AidFacade aidFacade;
	FamilyFacade familyFacade;
	BeneficiaryFacade beneficiaryFacade;
	PortfolioFacade portfolioFacade;
	PortfolioDetailFacade portfolioDetailFacade;
	//AddressFacade addressFacade;
	RationCardFacade rationCardFacade;
	FIAgent user;

	SharedPreferences settings;
	String PREF_NAME = "my_pref_settings";

	AlertDialog.Builder alert;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading_data);

		dialog = new ProgressDialog(this);
		aquery = new AQuery(this);

		settings = getSharedPreferences(PREF_NAME, 0); //load the preferences

		try {
			categoryFacade = new CategoryFacade(this);
			productFacade = new ProductFacade(this);
			aidFacade = new AidFacade(this);
			familyFacade = new FamilyFacade(this);
			beneficiaryFacade = new BeneficiaryFacade(this);
			portfolioFacade = new PortfolioFacade(this);
			portfolioDetailFacade = new PortfolioDetailFacade(this);
			//addressFacade = new AddressFacade(this);
			rationCardFacade = new RationCardFacade(this);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Button loadBtn = (Button) findViewById(R.id.loadBtn);
		loadBtn.setOnClickListener(this);

		//Displaying the name of the agent
		TextView agentName = (TextView) findViewById(R.id.agentName);
		agentName.setText("Agent : "+LoginActivity.user.getFirstName().charAt(0)+" "+LoginActivity.user.getLastName());

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.loadBtn:	
			boolean hasRun = settings.getBoolean("hasRun", false);
			if(!hasRun){
				load_categories();
			}
			else{
				alert = new AlertDialog.Builder(LoadDataActivity.this);
				alert.setTitle(R.string.information);
				alert.setIcon(android.R.drawable.ic_dialog_alert);
				alert.setMessage(R.string.hasRunBefore);
				alert.setPositiveButton(R.string.start, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(LoadDataActivity.this, TagActivity.class);
						startActivity(intent);
						finish();

					}
				});

				alert.setNegativeButton(R.string.quit, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						DialogHelper.processingDisconnection(LoadDataActivity.this.dialog, LoadDataActivity.this);
					}
				});

				alert.show();
			}
			break;
		}
	}


	public void load_categories(){
		if (InternetHelper. isOnline(LoadDataActivity.this)) {
			System.out.println(">>>>>><>>>> Loading Categories");
			String url = getString(R.string.url) + getString(R.string.load_Categories);

			DialogHelper.showProcessingDialog(dialog, getString(R.string.loading), getString(R.string.wait_loading));

			AjaxCallback<String> cb = new AjaxCallback<String>() {
				@Override
				public void callback(String url, String json, AjaxStatus status) {
					switch (status.getCode()){
					case Constant.REQUEST_OK:
						if(!status.getCookies().isEmpty())
							LoginActivity.cookies =   status.getCookies();

						int code = JsonHelper.getResponseCodeFromJson(json);

						switch (code) {
						case Constant.REQUEST_OK:	
							Type listType = new TypeToken<ArrayList<Category>>() {}.getType();
							ArrayList<Category> categories = JsonHelper.getListObjectFromJson(json,Constant.LIST_CATEGORIES,listType);

							if(!categories.isEmpty())
								for(Category c : categories)
									try {
										categoryFacade.insert(c);
									} catch (Exception e) {
										e.printStackTrace();
									}

							System.out.println("<<<<>>>> End Loading Categories");
							load_products();							
							break;

						default:
							alert = new AlertDialog.Builder(LoadDataActivity.this);
							alert.setIcon(android.R.drawable.ic_delete);
							alert.setTitle(R.string.error);
							alert.setMessage(getString(R.string.error_while_loading)+" "+Constant.LIST_CATEGORIES);
							alert.setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									load_categories();
								}
							});

							alert.setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
								}
							});	

							alert.show();
							break;
						}
						break;

					default:
						String message = getString(R.string.error_while_loading)+
						" "+Constant.LIST_CATEGORIES+". "+getString(R.string.request_not_received);

						alert = new AlertDialog.Builder(LoadDataActivity.this);
						DialogHelper.showErrorDialog(alert, getString(R.string.code_404), message);

						break;
					}
				}
			};

			if (LoginActivity.cookies != null)
				for (Cookie cookie : LoginActivity.cookies) {
					Log.v("Adding cookie", cookie.getName() + " Value " + cookie.getValue());
					cb.cookie(cookie.getName(), cookie.getValue());
				}
			aquery.progress(dialog).ajax(url, null, String.class, cb);

		}
		else {
			alert = new AlertDialog.Builder(LoadDataActivity.this);
			alert.setTitle(R.string.internet_error);
			alert.setIcon(android.R.drawable.ic_dialog_alert);
			alert.setMessage(R.string.internet_error_message);
			alert.setPositiveButton(R.string.settings, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					LoadDataActivity.this.startActivity(new Intent(Settings.ACTION_SETTINGS));
				}
			});
			alert.setNegativeButton(R.string.retry, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					load_categories();
				}
			});
			alert.show();
		}					    


	}

	public void load_products(){
		if (InternetHelper. isOnline(LoadDataActivity.this)) {
			System.out.println(">>>>>>< Loading Products >><<<<<<<");
			String url = getString(R.string.url) + getString(R.string.load_Products);

			DialogHelper.showProcessingDialog(dialog, getString(R.string.loading), getString(R.string.wait_loading));

			AjaxCallback<String> cb = new AjaxCallback<String>() {

				@Override
				public void callback(String url, String json, AjaxStatus status) {
					switch (status.getCode()){
					case Constant.REQUEST_OK:
						if(!status.getCookies().isEmpty())
							LoginActivity.cookies =   status.getCookies();

						int code = JsonHelper.getResponseCodeFromJson(json);

						switch (code) {
						case Constant.REQUEST_OK:	
							Type listType = new TypeToken<ArrayList<Product>>() {}.getType();
							ArrayList<Product> products = JsonHelper.getListObjectFromJson(json,Constant.LIST_PRODUCTS,listType);

							if(!products.isEmpty())

								for(Product p : products)
									try {
										if(p.getIconeURL() == null)
											p.setIconeURL("");
										productFacade.insert(p);

										String path = getString(R.string.productPhotosOffLine)+p.getIconeURL() ;
										File file = new File(Environment.getExternalStorageDirectory(),path);

										if(!file.exists()){
											Log.i("Load Data Activity", "Image of product "+p.getName()+" does not exist");
											download(getString(R.string.url)+getString(R.string.productPhotosOnLine), 
													p.getIconeURL(), getString(R.string.productPhotosOffLine));
										}
										else{
											Log.i("Load Data Activity", "Image of product "+p.getName()+" already exists");
										}
									} catch (Exception e) {
										e.printStackTrace();
									}

							System.out.println(">>>>>>>>>>>  End Loading Product<<<<<<<<<<<<");

							load_aids();							
							break;

						default:
							alert = new AlertDialog.Builder(LoadDataActivity.this);
							alert.setIcon(android.R.drawable.ic_delete);
							alert.setTitle(R.string.error);
							alert.setMessage(getString(R.string.error_while_loading)+" "+Constant.LIST_PRODUCTS);
							alert.setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									load_products();
								}
							});

							alert.setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
								}
							});	

							alert.show();
							break;

						}
						break;

					default:
						String message = getString(R.string.error_while_loading)+
						" "+Constant.LIST_PRODUCTS+". "+getString(R.string.request_not_received);
						alert = new AlertDialog.Builder(LoadDataActivity.this);
						DialogHelper.showErrorDialog(alert, getString(R.string.code_404), message);

						break;
					}
				}
			};

			if (LoginActivity.cookies != null)
				for (Cookie cookie : LoginActivity.cookies) {
					Log.v("Adding cookie", cookie.getName() + " Value " + cookie.getValue());
					cb.cookie(cookie.getName(), cookie.getValue());
				}
			aquery.progress(dialog).ajax(url, null, String.class, cb);

		}

		else {
			alert = new AlertDialog.Builder(LoadDataActivity.this);
			alert.setTitle(R.string.internet_error);
			alert.setIcon(android.R.drawable.ic_dialog_alert);
			alert.setMessage(R.string.internet_error_message);
			alert.setPositiveButton(R.string.settings, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					LoadDataActivity.this.startActivity(new Intent(Settings.ACTION_SETTINGS));
				}
			});
			alert.setNegativeButton(R.string.retry, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					load_products();
				}
			});
			alert.show();
		}					    

	}

	public void load_aids(){
		if (InternetHelper. isOnline(LoadDataActivity.this)) {
			System.out.println(">>>>>>> Loading Aids <<<<<<<<<<<<<<<");
			String url = getString(R.string.url) + getString(R.string.load_Aids);


			DialogHelper.showProcessingDialog(dialog, getString(R.string.loading), getString(R.string.wait_loading));

			AjaxCallback<String> cb = new AjaxCallback<String>() {

				@Override
				public void callback(String url, String json, AjaxStatus status) {
					switch (status.getCode()){
					case Constant.REQUEST_OK:
						if(!status.getCookies().isEmpty())
							LoginActivity.cookies =   status.getCookies();

						int code = JsonHelper.getResponseCodeFromJson(json);

						switch (code) {
						case Constant.REQUEST_OK:	
							Type listType = new TypeToken<ArrayList<Aid>>() {}.getType();
							ArrayList<Aid> aids = JsonHelper.getListObjectFromJson(json,Constant.LIST_AIDS,listType);

							if(!aids.isEmpty())
								for(Aid a : aids)
									try {
										aidFacade.insert(a);
									} catch (Exception e) {
										e.printStackTrace();
									}

							System.out.println(">>>>>> End Loading Aids >>>>>><<<<<<<");
							load_families();							
							break;

						default:

							alert = new AlertDialog.Builder(LoadDataActivity.this);
							alert.setIcon(android.R.drawable.ic_delete);
							alert.setTitle(R.string.error);
							alert.setMessage(getString(R.string.error_while_loading)+" "+Constant.LIST_AIDS);
							alert.setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									load_aids();
								}
							});

							alert.setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
								}
							});	

							alert.show();
							break;



						}
						break;

					default:
						String message = getString(R.string.error_while_loading)+
						" "+Constant.LIST_AIDS+". "+getString(R.string.request_not_received);

						alert = new AlertDialog.Builder(LoadDataActivity.this);
						DialogHelper.showErrorDialog(alert, getString(R.string.code_404), message);

						break;

					}
				}
			};

			if (LoginActivity.cookies != null)
				for (Cookie cookie : LoginActivity.cookies) {
					Log.v("Adding cookie", cookie.getName() + " Value " + cookie.getValue());
					cb.cookie(cookie.getName(), cookie.getValue());
				}
			aquery.progress(dialog).ajax(url, null, String.class, cb);

		}
		else {
			alert = new AlertDialog.Builder(LoadDataActivity.this);
			alert.setTitle(R.string.internet_error);
			alert.setIcon(android.R.drawable.ic_dialog_alert);
			alert.setMessage(R.string.internet_error_message);
			alert.setPositiveButton(R.string.settings, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					LoadDataActivity.this.startActivity(new Intent(Settings.ACTION_SETTINGS));
				}
			});
			alert.setNegativeButton(R.string.retry, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					load_aids();
				}
			});
			alert.show();
		}					    

	}


	public void load_families(){

		if (InternetHelper. isOnline(LoadDataActivity.this)) {

			System.out.println(">>>>> Loading Families <<<<<<<<<<<<");
			String url = getString(R.string.url) + getString(R.string.load_Families);

			DialogHelper.showProcessingDialog(dialog, getString(R.string.loading), getString(R.string.wait_loading));


			AjaxCallback<String> cb = new AjaxCallback<String>() {

				@Override
				public void callback(String url, String json, AjaxStatus status) {
					System.out.println(json);
					switch (status.getCode()){
					case Constant.REQUEST_OK:
						if(!status.getCookies().isEmpty())
							LoginActivity.cookies =   status.getCookies();

						int code = JsonHelper.getResponseCodeFromJson(json);

						switch (code) {
						case Constant.REQUEST_OK:	
							Type listType = new TypeToken<ArrayList<Family>>() {}.getType();
							ArrayList<Family> families = JsonHelper.getListObjectFromJson(json,Constant.LIST_FAMILIES,listType);
							if(!families.isEmpty())

								for(Family f : families){
									try {
										familyFacade.insert(f);

										for(Beneficiary fib : f.getBeneficiaries()){
											fib.setFamily(f);
											beneficiaryFacade.insert(fib);

											String path = getString(R.string.benefPhotosOffLine)+fib.getPhotoURL() ;
											File file = new File(Environment.getExternalStorageDirectory(),path);
											if(!file.exists()){
												Log.i("Load Data Activity", "Image of Beneficiary "+fib.getFirstName()+" does not exist");
												download(getString(R.string.url)+getString(R.string.benefPhotosOnLine), fib.getPhotoURL(), getString(R.string.benefPhotosOffLine));
											}
											else{
												Log.i("Load Data Activity", "Image of Beneficiary "+fib.getFirstName()+" already exist");
											}
										}

										Portfolio p = f.getPortfolio();
										if(p != null){
											p.setFamily(f);
											portfolioFacade.insert(p);

											for(PortfolioDetail pfd : p.getDetails()){
												pfd.setPortfolio(p);
												portfolioDetailFacade.insert(pfd);
											}
										}
										else{
											System.out.println("No possible inserting portfolio and pfd");
										}

										RationCard r = f.getRationCard();
										System.out.println("RationCard a inserer : "+r.getTagId());
										r.setFamily(f);
										rationCardFacade.insert(r);
										RationCard rc = rationCardFacade.findByTagId(r.getTagId());
										System.out.println("RationCard installee : "+rc.getTagId());


//										Address adr = f.getAddress();
//										adr.setFamily(f);
//										addressFacade.insert(adr);

									} catch (Exception e) {
										e.printStackTrace();
									}
								}

							System.out.println(">>>>>>>> End Loading all Families <<<<<<<<<<");


							SharedPreferences.Editor edit = settings.edit();
							edit.putBoolean("hasRun", true); //set to has run
							edit.commit(); //apply



							alert = new AlertDialog.Builder(LoadDataActivity.this);

							alert.setTitle(R.string.information);
							alert.setIcon(R.drawable.successicon);
							alert.setMessage(R.string.load_successfully);

							alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									Intent intent = new Intent(LoadDataActivity.this, TagActivity.class);

									startActivity(intent);
									finish();
								}
							});

							alert.show();
							break;

						default:
							System.out.println(status.getCode());
							alert = new AlertDialog.Builder(LoadDataActivity.this);
							alert.setIcon(android.R.drawable.ic_delete);
							alert.setTitle(R.string.error);
							alert.setMessage(getString(R.string.error_while_loading)+" "+Constant.LIST_FAMILIES);
							alert.setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									load_families();
								}
							});

							alert.setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
								}
							});	

							alert.show();
							break;

						}
						break;

					default:
						String message = getString(R.string.error_while_loading)+
						" "+Constant.LIST_FAMILIES+". "+getString(R.string.request_not_received);

						alert = new AlertDialog.Builder(LoadDataActivity.this);
						DialogHelper.showErrorDialog(alert, getString(R.string.code_404), message);

						break;

					}
				}
			};

			if (LoginActivity.cookies != null)
				for (Cookie cookie : LoginActivity.cookies) {
					Log.v("Adding cookie", cookie.getName() + " Value " + cookie.getValue());
					cb.cookie(cookie.getName(), cookie.getValue());
				}
			aquery.progress(dialog).ajax(url, null, String.class, cb);

		}
		else {
			alert = new AlertDialog.Builder(LoadDataActivity.this);
			alert.setTitle(R.string.internet_error);
			alert.setIcon(android.R.drawable.ic_dialog_alert);
			alert.setMessage(R.string.internet_error_message);
			alert.setPositiveButton(R.string.settings, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					LoadDataActivity.this.startActivity(new Intent(Settings.ACTION_SETTINGS));
				}
			});
			alert.setNegativeButton(R.string.retry, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					load_families();
				}
			});
			alert.show();
		}					    

	}

	public void download(String url,String imageName, String location){
		File ext = Environment.getExternalStorageDirectory();
		File target = new File(ext,location+imageName);              		
		aquery.progress(null).download(url+imageName, target, new AjaxCallback<File>(){
			@Override
			public void callback(String url, File file, AjaxStatus status) {
				if(file != null){
					System.out.println("File: " + file.length() + ":" + file+"  Status :"+status);
				}else{
					System.out.println("Failed to load file  "+ status);
				}
			}

		});
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
				DialogHelper.processingDisconnection(dialog, LoadDataActivity.this);
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
