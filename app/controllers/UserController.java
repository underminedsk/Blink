package controllers;

import java.util.List;

import play.Logger;
import support.Log;

import models.User;

public class UserController extends BaseController {

	private static final String TAG = "UserController";
	
    public static void all() {
    	List<User>  users = (List<User>) User.all();
    	renderJSON(users);
    }
    
    public static void get(String key) {
    	Log.debug(TAG, "get(" + key + ") called.");
    	User user = User.findUserByKey(key);
    	if (user == null) {
    		generateErrorResponse("could not find user matching that key");
    	}
    	
    	renderJSON(user.toJSONString());
    }
    
    public static void insertOrUpdate() {
    	Log.debug(TAG, "insert() called.");
    	String jsonString = getBodyAsString();
    	if (jsonString == null) {
    		generateErrorResponse("error parsing body as string");
    	} 
    	
    	Log.debug(TAG, "JSON is " + jsonString );
    	
    	try {
			User user = User.fromJSONString(jsonString);
			user.save();
			renderJSON(user.toJSONString());
			
		} catch (Exception e) {
			generateErrorResponse(e);
		} 
    }
}