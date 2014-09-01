package edu.ntust.cs.idsl.nomissing.model;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
public class SMSMessage {
    
    private int id;
    private String from;
    private String message;
    private long time;
    private String audio;

    public SMSMessage() {
    }

    public SMSMessage(int id, String from, String message, long time, String audio) {
        this.id = id;
        this.from = from;
        this.message = message;
        this.time = time;
        this.audio = audio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

}
