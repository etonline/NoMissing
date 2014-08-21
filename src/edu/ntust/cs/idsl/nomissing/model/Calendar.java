package edu.ntust.cs.idsl.nomissing.model;

import java.util.ArrayList;
import java.util.List;

import android.provider.CalendarContract.Calendars;
import edu.ntust.cs.idsl.nomissing.calendar.CalendarProviderDaoFactory;

public class Calendar {
	private long id;
	private String name;
	private String accountName;
	private String ownerAccount;
	private int accessLevel;

	public Calendar(long id, String name) {
		this.id = id;
		this.name = name;
	}

	public Calendar(long id, String name, String accountName,
			String ownerAccount, int accessLevel) {
		super();
		this.id = id;
		this.name = name;
		this.accountName = accountName;
		this.ownerAccount = ownerAccount;
		this.accessLevel = accessLevel;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getOwnerAccount() {
		return ownerAccount;
	}

	public void setOwnerAccount(String ownerAccount) {
		this.ownerAccount = ownerAccount;
	}

	public int getAccessLevel() {
		return accessLevel;
	}

	public void setAccessLevel(int accessLevel) {
		this.accessLevel = accessLevel;
	}
	
	public static List<String> getNameOfCalendars(List<Calendar> calendars) {
		List<String> nameOfCalendars = new ArrayList<String>();
		for (Calendar calendar : calendars) {
			nameOfCalendars.add(calendar.getName());
		}		
		return nameOfCalendars;
	}

}
