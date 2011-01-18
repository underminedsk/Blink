package controllers;

import java.util.List;

import models.User;

public class UserController extends BaseController {

    public static void index() {
    	List<User>  users = (List<User>) User.all();
    	renderJSON(users);
    }
    
    public static void get(String key) {
    	User user = User.findUserByKey(key);
    	if (user == null) {
    		generateErrorResponse("could not find user matching that key");
    	}
    	
    	renderJSON(user.toJSONString());
    }
    
    public static void insert() {
    	String jsonString = getBodyAsString();
    	if (jsonString == null) {
    		generateErrorResponse("error parsing body as string");
    	} 
    	
    	try {
			User user = User.fromJSONString(jsonString);
			user.save();
			renderJSON(user.toJSONString());
			
		} catch (Exception e) {
			generateErrorResponse(e);
		} 
    }
}