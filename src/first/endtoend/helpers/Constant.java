package first.endtoend.helpers;


public class Constant {
	/**
	 * Request Response Code
	 */
	public static final int REQUEST_OK = 200;
	public static final int INTERNAL_ERROR = 500;
	public static final int NOT_FOUND = 404;
	public static final int MALFORMED_REQUEST = 400;
	public static final int NULL_RESPONSE = 501;


	/**
	 * Spring Security login values
	 */
	public static final  String SPRING_SECURITY_USERNAME = "j_username";
	public static final  String SPRING_SECURITY_PASSWORD = "j_password";
	public static final  String SPRING_SECURITY_REMEMBER_ME = "_spring_security_remember_me";
	public static final String SESSION_ID = "JSESSIONID";

	/**
	 *   Constant JSON KEY
	 */
	public static final  String TRACE_KEY = "Trace";
	public static final  String RESPONSE_CODE_KEY = "code";
	public static final String USER = "user";
	public static final String AGENT_ID = "agentId";
	public static final String FAMILY = "family";
	public static final String LIST_CATEGORIES = "listCategories";
	public static final String LIST_PRODUCTS = "listProducts";
	public static final String LIST_AIDS = "listAids";
	public static final String LIST_FAMILIES = "listFamilies";
	
	
	/**
	 * Constant Database
	 */
	
	public static final String DB_NAME = "FirstDB";
	public static final int DB_VERSION = 1;
	
	/**
	 * Constant for the GCM
	 */
	
	public static final String GCM_ID = "457719800374";
	public static final String REG_ID = "regId";
	
	//Notification codes
	public static final int CODE_GCM_1 = 1; // add new family
	public static final int CODE_GCM_2 = 2; //update existing family
	public static final int CODE_GCM_3 = 3; //delete existing family and all data related to its
	public static final int CODE_GCM_4 = 4; //add new product
	public static final int CODE_GCM_5 = 5; //update existing product
	public static final int CODE_GCM_6 = 6; //delete existing product and all data related to its
	public static final int CODE_GCM_7 = 7; //add new aid
	public static final int CODE_GCM_8 = 8; //update existing aid
	public static final int CODE_GCM_9 = 9; //delete existing aid
	public static final int CODE_GCM_10 = 10;//add benef
	public static final int CODE_GCM_11 = 11;//update benef
	public static final int CODE_GCM_12 = 12;// delete benef
	
	
	/**
	 * APDU Constants
	 */
	
	public static final int GET_LOGIN_INFO_CODE = 0;
	public static final int SET_LOGIN_INFO_CODE = 1;
	public static final int SAY_HELLO_CODE = 2;
	public static final int GET_PIN_CODE = 3;
	public static final int GET_KEY_CODE = 4;
	public static final int PIN_LAUNCHED_REQUEST = 0;
}