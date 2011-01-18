package models;

import java.net.URISyntaxException;
import java.util.UUID;

import javax.persistence.Entity;

import org.json.JSONException;
import org.json.JSONObject;

import play.db.jpa.Model;
import support.Log;
import support.QRCodeManager;

@Entity
public class User extends Model {
	
	private static final String TAG = "User(Model)";
	
	public String first;
	public String last;
	
	//TODO: should be unique
	public String email;
	public String phone;
	public String key;
	
	
	private User(String fName, String lName, String email, String phone) {
		if (fName == null && lName == null && email == null && phone == null) 
			throw new IllegalArgumentException("cannot create a user object with all blank fields");
		
		this.first = fName.trim();
		this.last = lName.trim();
		this.email = email.trim();
		this.phone = phone.trim();
		this.key = UUID.randomUUID().toString();
	}
	
	public static User findUserByKey(String key) {
		return User.find("key", key).first();
	}
	
	public static User findUserById(long id) {
		return User.findById(id);
	}
	
	public static User fromJSONString(String jsonString) throws JSONException, IllegalArgumentException {
		JSONObject json = new JSONObject(jsonString);
		String fName, lName, pNumber, em, k;
		fName = lName = pNumber = em = k = null;
		try {
			if (json.has("first")) fName = json.getString("first");
			if (json.has("last")) lName = json.getString("last");
			if (json.has("phone")) pNumber = json.getString("phone");
			if (json.has("email")) em = json.getString("email");
			if (json.has("key")) k = json.getString("key");
		} catch (JSONException e) {
			Log.error(TAG, e);
		}
		User user = User.findUserByKey(k);
		if (user == null) {
			user = new User(fName, lName, pNumber, em);
		}
		
		return user;
	}
	
	public String toJSONString() {
		try {
			JSONObject json = new JSONObject().put("first", first)
								   .put("last", last)
								   .put("email", email)
								   .put("phone", phone)
								   .put("key", key);
			json.put("QRCodeURL", QRCodeManager.generateQRCodeURL(key));
			return json.toString();
		} catch (JSONException e) {
			Log.error(TAG, e);
			return null;
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public String toString() {
		String returnString = "------------------------";
		returnString += "  first: " + first;
		returnString += "  last: " + last;
		returnString += "  email: " + email;
		returnString += "  phone: " + phone;
		returnString += "  key: " + key;
		returnString += "------------------------";
		return returnString;
	}
}
