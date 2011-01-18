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
	
	public String firstName;
	public String lastName;
	
	//TODO: should be unique
	public String email;
	public String phoneNumber;
	public String key;
	
	
	private User(String fName, String lName, String email, String phoneNumber) {
		if (fName == null && lName == null && email == null && phoneNumber == null) 
			throw new IllegalArgumentException("cannot create a user object with all blank fields");
		
		this.firstName = fName.trim();
		this.lastName = lName.trim();
		this.email = email.trim();
		this.phoneNumber = phoneNumber.trim();
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
			if (json.has("firstName")) fName = json.getString("firstName");
			if (json.has("lastName")) lName = json.getString("lastName");
			if (json.has("phoneNumber")) pNumber = json.getString("phoneNumber");
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
			JSONObject json = new JSONObject().put("firstName", firstName)
								   .put("lastName", lastName)
								   .put("email", email)
								   .put("phoneNumber", phoneNumber)
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
		returnString += "  firstName: " + firstName;
		returnString += "  lastName: " + lastName;
		returnString += "  email: " + email;
		returnString += "  phoneNumber: " + phoneNumber;
		returnString += "  key: " + key;
		returnString += "------------------------";
		return returnString;
	}
}
