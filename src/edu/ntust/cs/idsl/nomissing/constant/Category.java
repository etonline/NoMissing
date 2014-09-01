package edu.ntust.cs.idsl.nomissing.constant;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
public enum Category {
    
    REMINDER("reminder"), 
    CHIME("chime"), 
    WEATHER("weather"),
    SMS("sms");

    private String name;

    private Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
