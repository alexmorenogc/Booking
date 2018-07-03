package es.ulpgc.eite.clean.mvp.masterdetail.data;

import es.ulpgc.eite.clean.mvp.masterdetail.app.Booking;

public class BookingItem implements Item {
  private Booking item;
  private String idFirebase;

  public BookingItem(Booking item, String id) {
    this.item = item;
    this.idFirebase = id;
  }

  public String getDetails() {
    return "Name: " + item.getName() + "\n" + "Shop:" + item.getShopId() + "\n" + "Mail:" + item.getMail() + "\n";
  }

  public void setId(int id) { item.setId(id); }

  @Override
  public int getId() { return item.getId(); }

  public String getIdFirebase() { return idFirebase; }

  public void setIdFirebase(String idFirebase) { this.idFirebase = idFirebase; }

  public String getContent() {
    return item.getName();
  }

  @Override
  public int getShopId() {
    return item.getShopId();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof BookingItem){
      BookingItem item = (BookingItem) obj;
      if(item.getId() == getId()){
        return true;
      }
    }
    return false;
  }

  @Override
  public String toString() {
    return item.getName();
  }
}
