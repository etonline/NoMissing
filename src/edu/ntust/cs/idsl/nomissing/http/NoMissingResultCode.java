package edu.ntust.cs.idsl.nomissing.http;

public class NoMissingResultCode {
	
	public static final String TAG = "NoMissingServer";
	
	// Register
	public static final int REGISTER_SUCCESS = 0;
	public static final int REGISTER_INVALID_REGISTRATION = -1;
	public static final int REGISTER_UUID_ALREADY_IN_USE = -2;
	
	// Login
	public static final int LOGIN_SUCCESS = 0;
	public static final int LOGIN_FORM_VALIDATION_FAILED = -1;
	public static final int LOGIN_INVALID_LOGIN = -2;
	
	// Signup
	public static final int SIGNUP_SUCCESS = 0;
	public static final int SIGNUP_FORM_VALIDATION_FAILED = -1;
	public static final int SIGNUP_USERNAME_ALREADY_IN_USE = -2;	
	public static final int SIGNUP_EMAIL_ADDRESS_ALREADY_IN_USE = -3;	
	
	// TTS Convert Text
	public static final int CONVERT_TEXT_SUCCESS = 0;
	public static final int CONVERT_TEXT_NO_MATCHED_SPEAKER = -1;
	public static final int CONVERT_TEXT_VOICE_SETTING_FAIL = -2;
	public static final int CONVERT_TEXT_OUTPUT_FILE_SETTING_FAIL = -3;
	public static final int CONVERT_TEXT_INVALID_LOGIN = -4;
	public static final int CONVERT_TEXT_INVALID_TTS_FONT_FORMAT = -5;
	public static final int CONVERT_TEXT_OUT_OF_SERVICE_SPACE = -6;
	public static final int CONVERT_TEXT_ACCOUNT_INCATIVE = -7;
	public static final int CONVERT_TEXT_POSSWORD_INCATIVE = -8;
	public static final int CONVERT_TEXT_INCATIVE_TTSSPEAKER = -9;
	public static final int CONVERT_TEXT_INCATIVE_VOLUME = -10;
	public static final int CONVERT_TEXT_INCATIVE_SPEED = -11;
	public static final int CONVERT_TEXT_INVALID_TEXT = -12;
	public static final int CONVERT_TEXT_CONTENT_SIZE_IS_TOO_LARGE = -13;
	public static final int CONVERT_TEXT_INACTIVE_CONVERTID = -14;
	public static final int CONVERT_TEXT_INACTIVE_OUTPUTTYPE = -15;
	public static final int CONVERT_TEXT_INACTIVE_PITCHMODIFY = -16;
	
	// Get Convert Status
	public static final int CONVERT_STATUS_QUEUED = 0;
	public static final int CONVERT_STATUS_PROCESSING = 1;
	public static final int CONVERT_STATUS_COMPLETED = 2;
	
}
