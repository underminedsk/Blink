package controllers;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import models.User;
import play.jobs.Job;
import support.Log;
import support.TransactionManager;

public class TransactionController extends BaseController {
	
	private static final String TAG = "TransactionController";
	
	public static void index() {
		renderText("blink server is up!");
	}

	/*
	 * A client may call this method in one of two configurations.  They may specify either
	 * 1.  a parameter labeled 'knownKey' and a parameter labeled 'unknownKey'
	 * 2.  a parameter labeled 'knownKey' with no other parameters
	 * 
	 * In situation 1, this person is scanning a QR Code.  For this reason, they the information
	 * of both parties so this API Endpoint simply just returns to them the information of the other
	 * person immediately.
	 * 
	 * In situation 2, this person is having their QR Code scanned.  If the other person has not sent
	 * a QR Scanned notification to the server, then this method spawns a job to wait until that occurs.
	 * When that does register, this method is able to determine who scanned their QR Code, and subsequently 
	 * pass that information back to the person that called this API endpoint.
	 */
	public static void handleTransactionRequest() {
		if (!params._contains("knownkey")) {
			generateErrorResponse("valid transaction requests must contain a knownkey parameter");
			return;
		}
		
		final String knownKey = params.get("knownkey");
		
		if (User.findUserByKey(knownKey) == null) {
			generateErrorResponse("no user matches that key.");
			return;
		}
		
		String unknownKey = null;
		
		if (params._contains("unknownkey")) {
			unknownKey = params.get("unknownkey");
		} else {
			if (request.isNew) {
				Future<String> transactionJob = new Job<String>() {
					public String doJobWithResult() {
						String unknownKey = TransactionManager.handleTransaction(knownKey);
						return unknownKey;
					}
				}.now();
				request.args.put( "transaction", transactionJob);
				waitFor(transactionJob);
				
			} else {
				Future<String> transactionJob = (Future<String>) request.args.get("transaction");
				try {
					unknownKey = transactionJob.get();
				} catch (Exception e) {
					Log.error(TAG, e);
				}
			}
		}
		
		User unknownUser = User.findUserByKey(unknownKey);
		if (unknownUser == null) {
			//should never happen because we checked for key existence in the scanner API call.
			generateErrorResponse("no user matches that key.");
			return;
		}
		
		renderJSON(unknownUser.toJSONString());
	}
}
