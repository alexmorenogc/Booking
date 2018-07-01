package es.ulpgc.eite.clean.mvp.masterdetail.app;

public class BookingItem implements Item {
  private Booking item;

  public BookingItem(Booking item) {
    this.item = item;
  }

  public String getDetails() {
    return "Name: " + item.getName() + "\n" + "Shop:" + item.getShopId() + "\n" + "Mail:" + item.getMail() + "\n";
  }

  public int getId() {
    return item.getId();
  }

  public String getContent() {
    return item.getName();
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
