package controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.json.JSONException;
import org.json.JSONObject;

import play.mvc.Controller;
import support.Log;

public class BaseController extends Controller {
	private static final String TAG = "BaseController";
	
	
	public static void index() {
		renderText("blink server is up! \n\n" +
				" use POST     /transaction       to initiate transactions  \n" +
				"      parameters: 'knownkey' (required)  \n" +
				"				   'unknownkey'(optional)   \n" +
				" use POST     /user/insert       with a JSON body to create a new user \n" +
				" use GET      /user/{key}        to get information on a user in JSON format \n\n" +
				" JSON User specification: \n" +
				"    firstName, lastName, email, phone, key");
	}

	
    public static String getBodyAsString() {
    	
    	if( ! request.isNew ) {
    		Log.error(TAG, "Cannot call getBodyAsString() twice for the same request");
    		return null;
    	}
        
    	if (request.body != null) {
            StringBuilder sb = new StringBuilder();
            String line;

            try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(request.body, "UTF-8"));
				try {
					while ((line = reader.readLine()) != null) {
						sb.append(line).append("\n");
					}
				}
				
				finally {
					reader.close();
				}
            }
            catch (IOException ioe) {
            	Log.error(TAG, "IO Exception getting body as a string: " + ioe.getMessage() );
                return null;
            }
            return sb.toString();
        } else {        
            return null;
        }
    }
    
    
    public static void generateErrorResponse(String message) {
    	Log.error(TAG, message);
    	JSONObject json = new JSONObject();
    	try {
        	json.put("error", message);
    	} catch (JSONException e) {
    		Log.error(TAG, e);
    	}
    	renderJSON(json);
    }
    
    public static void generateErrorResponse(Throwable e) {
    	Log.error(TAG, e.getMessage());
    	JSONObject json = new JSONObject();
    	try {
        	json.put("error", e.getMessage());
    	} catch (JSONException ee) {
    		Log.error(TAG, ee);
    	}
    	renderJSON(json);
    }
}
