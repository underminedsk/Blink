package support;

import java.net.URI;
import java.net.URISyntaxException;



public class QRCodeManager {
	
	public static String generateQRCodeURL(String input) throws URISyntaxException {
		if (input.length() > 250) throw new IllegalArgumentException("the input string can not be over 250 characters.");
		String url = Env.QRCODE_API_ENDPOINT + input;
		return new URI(url).toString();
	}
}
