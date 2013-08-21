package first.endtoend.helpers;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class JsonHelper {
    /**
     * Get an instance of a given class from a json object
     * @param <T>
     * @param theJson the json object
     * @param classToReturn the class
     * @return instance of the given class
     */
    public static <T> T getObjectFromJson(String theJson, Class<T> classToReturn, String jsonKey) {
        try {
            // Creates the json object which will manage the information received
            GsonBuilder builder = new GsonBuilder();
// Register an adapter to manage the date types as long values
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                @Override
				public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            GsonBuilder gsonb = new GsonBuilder();
            //gsonb.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            DateDeserializer ds = new DateDeserializer();
            gsonb.registerTypeAdapter(Date.class, ds);
            Gson gson = gsonb
                    .excludeFieldsWithoutExposeAnnotation()
                    .setDateFormat(DateFormat.LONG)
                    .create();
            JSONObject jsonObject = new JSONObject(theJson.toString());
            String str;
            if(!jsonKey.equals(""))
            {
                str =jsonObject.get(jsonKey).toString();
            }  else{
                str =jsonObject.toString();
            }
            return gson.fromJson(str, classToReturn);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            Log.v("Exception","Exception " +e.getMessage());
            return null;
        }
    }

    public static int getResponseCodeFromJson(Object theJson) {
        try {
            JSONObject jsonObject = new JSONObject(theJson.toString());
            if(jsonObject.has(Constant.RESPONSE_CODE_KEY))
            {
                return   jsonObject.getInt(Constant.RESPONSE_CODE_KEY);
            } else{
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            Log.v("Exception",e.getMessage());
            return -1;
        }
    }

    /**
     * get a list of objects of a given class
     * @param theJsonObject the json object
     * @param typeOfTheList type of the list
     * @param key
     * @return  return the list as an java object
     */
    
    public static <T> ArrayList<T>  getListObjectFromJson(String theJsonObject,  String JsonKey, Type type) {
        try {
            GsonBuilder gsonb = new GsonBuilder();
            DateSerializer ds = new DateSerializer();
            gsonb.registerTypeAdapter(Date.class, ds);
            Gson gson = gsonb
                    .excludeFieldsWithoutExposeAnnotation()
//                    .setDateFormat(DateFormat.MEDIUM)
                    .setDateFormat(DateFormat.LONG)
                    .create();
            JSONObject jsonObject = new JSONObject(theJsonObject);
            String str = jsonObject.get(JsonKey).toString();
            ArrayList<T> res = gson.fromJson(str, type);
            return res; 
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
          //  return null;
        }
        return null; 
    }
    
    
    public static <T> ArrayList<T> getListObjectFromJson(String theJsonObject, Type typeOfTheList) {
        try {
            GsonBuilder gsonb = new GsonBuilder();
            DateDeserializer ds = new DateDeserializer();
            gsonb.registerTypeAdapter(Date.class, ds);
            Gson gson = gsonb
                    .excludeFieldsWithoutExposeAnnotation()
                    .setDateFormat(DateFormat.LONG)
                    .create();
           JSONObject jsonObject = new JSONObject(theJsonObject);
           ArrayList<T> res = gson.fromJson(jsonObject.toString(), typeOfTheList);
            Log.v("Return", res.toString() + " " + res.toString());
            return res;
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return null;
        }
    }


    /**
     *
     * @param toTransformInJson the instance of class to serialize
     * @return a json object from the given instance or null if error occurs
     */
    public static JSONObject createJsonObject(Object toTransformInJson) {
        GsonBuilder gsonb = new GsonBuilder();
        DateSerializer ds = new DateSerializer();
        gsonb.registerTypeAdapter(Date.class, ds);
        Gson gson = gsonb
                .excludeFieldsWithoutExposeAnnotation()
                .setDateFormat(DateFormat.LONG)
                .create();

        JSONObject jsonUserModel = new JSONObject();
        try {
            String result = gson.toJson(toTransformInJson);
            jsonUserModel = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return jsonUserModel;
    }

    /**
     * Create a json array from a list of object
     * @param listOfObjectToTransformInJsonArray the list of objects
     * @return a json array
     */
    @SuppressWarnings("finally")
	public static JSONArray createJsonArray(ArrayList<?> listOfObjectToTransformInJsonArray) {
        JSONArray jsonArray = null;
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        try {
            jsonArray = new JSONArray(gson.toJson(listOfObjectToTransformInJsonArray));
        } catch (JSONException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            jsonArray = null;
        } finally {
            return jsonArray;
        }
    }

	public static boolean isResponseJson(String result) {
		// TODO Auto-generated method stub
		try{
			new JSONObject(result);
			return true;
		}catch(Exception e){
			return false;
		}
	}

	public static boolean isResponseSucess(String json) {
		// TODO Auto-generated method stub
		return JsonHelper.getResponseCodeFromJson(json)==200;
	}
}

