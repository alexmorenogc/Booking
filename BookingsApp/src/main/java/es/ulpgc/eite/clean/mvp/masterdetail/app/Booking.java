package es.ulpgc.eite.clean.mvp.masterdetail.app;

public class Booking {
    private int shopId;
    private String name;
    private String mail;
    private String date;
    private String products;
    private int phone;
    private int hourId;

    public Booking(int shopId, String name, String mail, String date, String products, int phone, int hourId) {
        this.shopId = shopId;
        this.name = name;
        this.mail = mail;
        this.date = date;
        this.products = products;
        this.phone = phone;
        this.hourId = hourId;
    }

    public Booking() {
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getProducts() {
        return products;
    }

    public void setProducts(String products) {
        this.products = products;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public int getHourId() {
        return hourId;
    }

    public void setHourId(int hourId) {
        this.hourId = hourId;
    }
}
