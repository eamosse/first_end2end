package first.endtoend;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import first.endtoend.facades.TraceDetailFacade;
import first.endtoend.facades.TraceFacade;
import first.endtoend.helpers.Constant;
import first.endtoend.helpers.InternetHelper;
import first.endtoend.helpers.JsonHelper;
import first.endtoend.models.Trace;
import first.endtoend.models.TraceDetail;

public class MySyncService extends Service {

	public static Context context;
	private Timer timer = new Timer();
	private static final long UPDATE_INTERVAL = 3600000;
	SharedPreferences settings;
	String PREF_NAME = "my_pref_settings";

	public static void setContext(Context context){
		MySyncService.context = context;
	}


	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}


	@Override
	public void onCreate() {
		super.onCreate();

		timer.scheduleAtFixedRate(
				new TimerTask() {
					public void run() {
						if(InternetHelper.isOnline(context))
							try {
								syncData(context);
							} catch (Exception e) {
								e.printStackTrace();
							} 
					}
				},
				0,
				UPDATE_INTERVAL);
		Log.i(getClass().getSimpleName(), "Synchronisation Started for "+getString(R.string.app_name));
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (timer != null) timer.cancel();
		Log.i(getClass().getSimpleName(), "Synchronisation Stopped for "+getString(R.string.app_name));
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}


	public void syncData(Context context){

		//variables
		TraceFacade trFacade;
		TraceDetailFacade trDetailFacade;
		List<Trace> traces;
		List<TraceDetail> traceDetails;
		
		//instantiation of a AQUERY
		AQuery aquery = new AQuery(context);


		try {
			//instantiation of facade to be used
			trFacade = new TraceFacade(context);
			trDetailFacade = new TraceDetailFacade(context);

			//getting all traces to be synchronized
			traces = trFacade.findAllToSync();
			for(Trace t : traces){
				traceDetails = trDetailFacade.findEntitiesByForeignKey(t.getTraceId(), Trace.class);
				for(TraceDetail detail : traceDetails){
					t.getTraceDetails().add(detail);
				}
				doSync(t, aquery, trFacade);					
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * method to synchronize an instance of trace
	 * @param t
	 * @param url
	 * @param aquery
	 * @param trFacade
	 */
	public void doSync(final Trace t, AQuery aquery, final TraceFacade trFacade){
		String url = getString(R.string.url)+getString(R.string.syncTrace);
		settings = getSharedPreferences(PREF_NAME, 0); 
		final String cookieName = settings.getString("cookieName", "");
		final String cookieValue = settings.getString("cookieValue", "");
		System.out.println("Cookie is :"+cookieName+" Value : "+cookieValue);

		JSONObject ob = JsonHelper.createJsonObject(t);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Constant.TRACE_KEY, ob.toString());		
		aquery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
			@Override
			public void callback(String url, JSONObject json, AjaxStatus status) {

				switch (status.getCode()) {
				case Constant.REQUEST_OK:
					
					int code = JsonHelper.getResponseCodeFromJson(json.toString());
					
					switch (code) {
					case Constant.REQUEST_OK:
						t.setSync(1);
						break;
					default:
						Log.i("First Service ", "in Server response : Trace cannot be Sync now! will be tried later on");
						break;
					}
					break;

				default:
					Log.i("First Service ", "in HTTP failed : Trace cannot be sync now! will be tried later on. Code is :"+status.getCode());
					break;
				}
				
				try {
					trFacade.update(t);
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.out.println(json);  
			}
		}.cookie(cookieName, cookieValue));
	}
	
	
	public void updateData(Context context){
		//TODO 
//		CategoryFacade categoryFacade;
//		ProductFacade productFacade;
//		AidFacade aidFacade;
//		FamilyFacade familyFacade;
//		BeneficiaryFacade beneficiaryFacade;
//		PortfolioFacade portfolioFacade;
//		PortfolioDetailFacade portfolioDetailFacade;
//		AddressFacade addressFacade;
//		RationCardFacade rationCardFacade;
//		
//		try {
//			categoryFacade = new CategoryFacade(context);
//			productFacade = new ProductFacade(context);
//			aidFacade = new AidFacade(context);
//			familyFacade = new FamilyFacade(context);
//			beneficiaryFacade = new BeneficiaryFacade(context);
//			portfolioFacade = new PortfolioFacade(context);
//			portfolioDetailFacade = new PortfolioDetailFacade(context);
//			addressFacade = new AddressFacade(context);
//			rationCardFacade = new RationCardFacade(context);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
		

	}
}