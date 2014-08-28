package edu.ntust.cs.idsl.nomissing.model;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
public class Weather {
	
	private int cityID;
	private String city;
	private String stno;
	private String time;
	private String memo;
	private String audio;
	private String createdAt;
	private String updatedAt;
	
	public Weather() {}
	
	public Weather(int cityID, String city, String stno, String time,
			String memo, String audio, String createdAt, String updatedAt) {
		super();
		this.cityID = cityID;
		this.city = city;
		this.stno = stno;
		this.time = time;
		this.memo = memo;
		this.audio = audio;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public int getCityID() {
		return cityID;
	}

	public void setCityID(int cityID) {
		this.cityID = cityID;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStno() {
		return stno;
	}

	public void setStno(String stno) {
		this.stno = stno;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getAudio() {
		return audio;
	}

	public void setAudio(String audio) {
		this.audio = audio;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}



}
