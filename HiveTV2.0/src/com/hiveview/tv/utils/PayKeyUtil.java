package com.hiveview.tv.utils;

import android.content.Context;

import com.hiveview.EncryptionHiveviewInfo;
import com.hiveview.SafeFilterManager;
import com.hiveview.tv.service.exception.ServiceException;


public class PayKeyUtil {
	private final static String TAG = "PayKeyUtil";
	private SafeFilterManager mg;
	private EncryptionHiveviewInfo info;
	private byte[] orgKey_A = { (byte) 'j', (byte) 's' };
	private byte[] orgKey_B = { (byte) 't', (byte) 'x' };
	private byte[] orgKey_C = { (byte) 'z', (byte) 'f' };
	private int[] encrypKey1_A = new int[8];
	private int[] encrypKey2_A = new int[8];
	private int[] encrypKey1_B = new int[8];
	private int[] encrypKey2_B = new int[8];
	private int[] encrypKey1_C = new int[8];
	private int[] encrypKey2_C = new int[8];
	private byte[] requestKey = { (byte) 'x', (byte) 'f', (byte) '2', (byte) '4', (byte) 'f', (byte) 'f', (byte) 'v', (byte) '1' };
	private byte[] requestSecureKey = new byte[8];
	private byte[] privkey = new byte[29];
	private byte[] privSecurekey = new byte[29];

	private byte[] userEncrypKey = { (byte) '3', (byte) '6', (byte) '5', (byte) 'a', (byte) '1', (byte) '6', (byte) 'd', (byte) '8', (byte) 'd',
			(byte) '8', (byte) '2', (byte) 'a', (byte) '0', (byte) '2', (byte) '2', (byte) '5', (byte) '8', (byte) 'f', (byte) '6', (byte) '5',
			(byte) 'b', (byte) 'a', (byte) '0', (byte) '6', (byte) '3', (byte) '0', (byte) 'b', (byte) 'a', (byte) '6', (byte) '1', (byte) 'd',
			(byte) 'b', (byte) '3', (byte) '7', (byte) 'f', (byte) '1', };
	private byte[] userSecureKey = new byte[32];
	private byte[] userKey = new byte[32];

	public String getPayKey(Context context) throws Exception {
		info = new EncryptionHiveviewInfo(context);
		boolean ret = info.getEncypKey(orgKey_A, encrypKey1_A, encrypKey2_A);
		if (ret) {
			/*
			 * Log.e("huxing","encrypKey1_A[0] = " + encrypKey1_A[0] +
			 * " , encrypKey1_A[1] = " +encrypKey1_A[1] +
			 * " , encrypKey1_A[2] = " + encrypKey1_A[2] +
			 * " , encrypKey1_A[3] = " + encrypKey1_A[3] +
			 * " , encrypKey1_A[4] = " + encrypKey1_A[4] +
			 * " , encrypKey1_A[5] = " + encrypKey1_A[5] +
			 * " , encrypKey1_A[6] = " + encrypKey1_A[6] +
			 * " , encrypKey1_A[7] = " +encrypKey1_A[7] );
			 * Log.e("huxing","encrypKey2_A[0] = " + encrypKey2_A[0] +
			 * " , encrypKey2_A[1] = " +encrypKey2_A[1] +
			 * " , encrypKey2_A[2] = " + encrypKey2_A[2] +
			 * " , encrypKey2_A[3] = " + encrypKey2_A[3] +
			 * " , encrypKey2_A[4] = " + encrypKey2_A[4] +
			 * " , encrypKey2_A[5] = " + encrypKey2_A[5] +
			 * " , encrypKey2_A[6] = " + encrypKey2_A[6] +
			 * " , encrypKey2_A[7] = " +encrypKey2_A[7] );
			 */
		}

		info.getEncypKey(orgKey_B, encrypKey1_B, encrypKey2_B);
		info.getEncryptionData(requestKey, requestSecureKey, encrypKey1_A, encrypKey2_A);
		mg = SafeFilterManager.getSafeFilterManager();
		mg.getSafeKeyData(requestSecureKey, requestSecureKey.length, privSecurekey, privSecurekey.length);
		info.getDecryptionData(privSecurekey, privkey, encrypKey1_B, encrypKey2_B);
//		Log.e(TAG, "huxing----------privkey = " + new String(privkey));
		
//		info.getEncypKey(orgKey_C, encrypKey1_C, encrypKey2_C);
//		mg.getUserKeyData(userEncrypKey, userEncrypKey.length, userSecureKey, userSecureKey.length, 12);
//		info.getDecryptionData(userSecureKey, userKey, encrypKey1_C, encrypKey2_C);
//		Log.e(TAG,"huxing----------userSecureKey = " +new String(userSecureKey));
//		Log.e(TAG,"huxing----------userKey = " +new String(userKey));

		return new String(privkey);
	}

	public String decodePayKey(Context context ,String token, int tagIndex) throws ServiceException {
		info = new EncryptionHiveviewInfo(context);
		info.getEncypKey(orgKey_C, encrypKey1_C, encrypKey2_C);
		mg = SafeFilterManager.getSafeFilterManager();
		mg.getUserKeyData(token.getBytes(), token.length(), userSecureKey, userSecureKey.length, tagIndex);
		info.getDecryptionData(userSecureKey, userKey, encrypKey1_C, encrypKey2_C);
		return new String(userKey);
	}

}
