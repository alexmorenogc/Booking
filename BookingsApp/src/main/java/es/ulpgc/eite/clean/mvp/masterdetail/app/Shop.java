package es.ulpgc.eite.clean.mvp.masterdetail.app;


public class Shop  {
    private int id;
    private String name;
    private String code;
    private int zone;
    private String mail;
    private int timetable;
    private String latitude;
    private String longitude;

    public Shop(){}

    public Shop(String name, String code, int zone, String mail, int timetable, String latitude, String longitude) {
        this.name = name;
        this.code = code;
        this.zone = zone;
        this.mail = mail;
        this.timetable = timetable;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Shop (String name, String latitude, String longitude){
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString() { return name;}

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Shop){
            Shop item = (Shop) obj;

            if (item.getCode() == getCode()){
                return true;
            }
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getZone() {
        return zone;
    }

    public void setZone(int zone) {
        this.zone = zone;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public int getTimetable() {
        return this.timetable;
    }

    public String getLatitude() { return latitude; }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public double getLatitudeD() { return Double.parseDouble(latitude); }

    public void setLatitudeD(double latitude) {
        this.latitude = Double.toString(latitude);
    }

    public String getLongitude() { return longitude; }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public double getLongitudeD() { return Double.parseDouble(longitude); }

    public void setLongitudeD(double longitude) {
        this.longitude = Double.toString(longitude);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
