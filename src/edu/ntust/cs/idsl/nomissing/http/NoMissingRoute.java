package edu.ntust.cs.idsl.nomissing.http;

public class NoMissingRoute {
	
	// Base URL
	public static final String BASE_URL = "http://140.118.110.128/nomissing";
	
	// Authentication
	public static final String SIGNUP = BASE_URL + "/signup";
	public static final String LOGIN = BASE_URL + "/login";
	public static final String LOGOUT = BASE_URL + "/logout";
	
	// RESTful API prefix (need basic auth)
	public static final String PREFIX = "/api";
	
	// RESTful API
	public static final String WEATHER = BASE_URL + PREFIX + "/weather";
	public static final String CONVERT_TEXT = BASE_URL + PREFIX + "/TTSService/ConvertText";
	public static final String GET_CONVERT_STATUS = BASE_URL + PREFIX + "/TTSService/GetConvertStatus";
	
}
