package edu.ntust.cs.idsl.nomissing.model;

public class ProgressStatus {

	public static final int START = 0;
	public static final int PROGRESS_UPDATE = 1;
	public static final int FINISH = 2;

	private int status;
	private String title;
	private String message;
	private int progress;

	public ProgressStatus() {
	}

	public ProgressStatus(int status, String title, String message, int progress) {
		this.status = status;
		this.title = title;
		this.message = message;
		this.progress = progress;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

}
