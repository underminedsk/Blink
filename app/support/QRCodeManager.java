package support;

import com.sun.xml.internal.rngom.util.Uri;

public class QRCodeManager {
	
	public static String generateQRCodeURL(String input) {
		if (input.length() > 250) throw new IllegalArgumentException("the input string can not be over 250 characters.");
		String url = Env.QRCODE_API_ENDPOINT + input;
		return Uri.escapeDisallowedChars(url);
	}
}
