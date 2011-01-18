package support;

import java.util.Collections;
import java.util.HashMap;


public class TransactionManager {
	//scannee, scanner
	private static HashMap<String, String> transactions = (HashMap<String, String>) Collections.synchronizedMap(new HashMap<String, String>());
	
	public static void handleTransaction(String knownKey, String unknownKey) {
		transactions.put(knownKey, unknownKey);
	}
	
	//blocks the calling thread until the job is done.
	public static String handleTransaction(String knownKey) {
		
		while (transactions.get(knownKey) == null) {
			try {
				Thread.sleep(25);
			} catch (InterruptedException e) {
			}
		}
		
		//returning the key of the scanner.
		String unknownKey = transactions.remove(knownKey);
		return unknownKey;
	}
	
	
	
	static class TransactionPair {
		String scannerKey;
		String scanneeKey;
	}
}
