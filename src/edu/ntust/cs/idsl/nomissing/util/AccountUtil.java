package edu.ntust.cs.idsl.nomissing.util;

import java.util.ArrayList;
import java.util.List;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.util.Log;

public class AccountUtil {
	
	public static final String ACCOUNT_TYPE_GOOGLE = "com.google";
	public static final String ACCOUNT_TYPE_NOMISSING = "edu.ntust.cs.idsl.nomissing";
	
	public static Account[] getAccounts(Context context) {
		AccountManager accountManager = AccountManager.get(context);
		Account[] accounts = accountManager.getAccounts();
		return accounts;
	}
	
	public static Account[] getGoogleAccounts(Context context) {
		AccountManager accountManager = AccountManager.get(context);
		Account[] accounts = accountManager.getAccountsByType(ACCOUNT_TYPE_GOOGLE);
		return accounts;
	}
}
