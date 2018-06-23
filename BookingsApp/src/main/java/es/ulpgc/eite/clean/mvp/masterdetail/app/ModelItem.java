package es.ulpgc.eite.clean.mvp.masterdetail.app;

public class ModelItem {
  private Shop item;

  public ModelItem(Shop item) {
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
  public boolean equals(Object obj) {
    if (obj instanceof ModelItem){
      ModelItem item = (ModelItem) obj;
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
