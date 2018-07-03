package es.ulpgc.eite.clean.mvp.masterdetail.data;

import es.ulpgc.eite.clean.mvp.masterdetail.app.Shop;

public class ShopItem implements Item {
  private Shop item;

  public ShopItem(Shop item) {
    this.item = item;
  }

  public String getDetails() {
    return "Name: " + item.getName() + "\n Mail:" + item.getMail() + "\n";
  }

  public int getId() {
    return item.getId();
  }

  public String getContent() {
    return item.getName();
  }

  @Override
  public int getShopId() {
    return item.getId();
  }


  @Override
  public boolean equals(Object obj) {
    if (obj instanceof ShopItem){
      ShopItem item = (ShopItem) obj;
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
