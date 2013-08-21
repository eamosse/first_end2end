/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package first.endtoend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

import first.endtoend.facades.BeneficiaryFacade;
import first.endtoend.helpers.AQueryHelper;
import first.endtoend.helpers.Constant;
import first.endtoend.helpers.JsonHelper;
import first.endtoend.models.Beneficiary;

/**
 * {@link IntentService} responsible for handling GCM messages.
 */
public class GCMIntentService extends GCMBaseIntentService {

	private static final String TAG = "GCMIntentService onMessage";

	public GCMIntentService() {
		super(Constant.GCM_ID);
	}

	@Override
	protected void onError(Context arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onMessage(Context context, Intent intent) {

		String message = intent.getStringExtra("message");
		JSONObject json = null;

		Log.d(TAG, "RECEIVED A MESSAGE :"+message);



		System.out.println("GCMItentService onMessage :"+message);
		try {
			json = new JSONObject(message);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("notif", json.toString());
			JSONArray array = json.getJSONArray("data");
			int code = json.getInt(Constant.RESPONSE_CODE_KEY);


			switch (code) {
			//add new family
			case Constant.CODE_GCM_1:
				//update existing family
			case Constant.CODE_GCM_2:
				AQueryHelper.load_families(context, params);

				break;

				//delete existing family and all data related to its
			case Constant.CODE_GCM_3:
				if(array.length() > 0)
					for(int i = 0; i< array.length();i++)
						AQueryHelper.delete_family(context, array.getInt(i));

				break;

				//add new product
			case Constant.CODE_GCM_4:
				//update existing product
			case Constant.CODE_GCM_5:

				AQueryHelper.load_products(context, params);

				break;

				//delete existing product and all data related to its
			case Constant.CODE_GCM_6:
				if(array.length() > 0)
					for(int i = 0; i< array.length();i++)
						AQueryHelper.delete_product(context, array.getInt(i));

				break;

				//add new aid
			case Constant.CODE_GCM_7:

				//update existing aid
			case Constant.CODE_GCM_8:
				
				AQueryHelper.load_aids(context, params);

				break;

				//delete existing aid and all data related to its
			case Constant.CODE_GCM_9:
				if(array.length() > 0)
					for(int i = 0; i< array.length();i++)
						AQueryHelper.delete_aid(context, array.getInt(i));
				break;
								
				
			case Constant.CODE_GCM_12:				
				try {
					int beneficiaryId = array.getInt(0);
					System.out.println("the id to delete is :"+beneficiaryId);
					BeneficiaryFacade bFacade = new BeneficiaryFacade(context);
					Beneficiary b = bFacade.findById(beneficiaryId);
					if(b != null){
						System.out.println(JsonHelper.createJsonObject(b));
						int familyId = b.getFamily().getFamilyId();
						List<Integer> list = new ArrayList<Integer>();
						list.add(familyId);
						AQueryHelper.delete_beneficiary(context, beneficiaryId);
						
						JSONArray jsonArray = new JSONArray(list);
						Map<String, Object> parameters = new HashMap<String, Object>();
						JSONObject jo = new JSONObject();
						jo.put("code", Constant.CODE_GCM_1);
						jo.put("data", jsonArray);
						parameters.put("notif",jo.toString());

						AQueryHelper.load_families(context, parameters);
						
					}
					else{
						System.out.println("No Beneficiary in local with id :"+beneficiaryId);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				break;

			default:
				System.out.println("Code : "+code+" - Non-contracted Code");
				break;
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onRegistered(Context arg0, String arg1) {

	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) {

	}

}
