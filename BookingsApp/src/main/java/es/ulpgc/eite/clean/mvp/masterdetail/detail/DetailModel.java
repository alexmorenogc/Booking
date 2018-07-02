package es.ulpgc.eite.clean.mvp.masterdetail.detail;

import android.util.Log;

import es.ulpgc.eite.clean.mvp.GenericModel;
import es.ulpgc.eite.clean.mvp.masterdetail.data.Item;


public class DetailModel
    extends GenericModel<Detail.ModelToPresenter> implements Detail.PresenterToModel {

  private Item item;

  /**
   * Method that recovers a reference to the PRESENTER
   * You must ALWAYS call {@link super#onCreate(Object)} here
   *
   * @param presenter Presenter interface
   */
  @Override
  public void onCreate(Detail.ModelToPresenter presenter) {
    super.onCreate(presenter);
    Log.d(TAG, "calling onCreate()");
  }

  /**
   * Called by layer PRESENTER when VIEW pass for a reconstruction/destruction.
   * Usefull for kill/stop activities that could be running on the background Threads
   *
   * @param isChangingConfiguration Informs that a change is occurring on the configuration
   */
  @Override
  public void onDestroy(boolean isChangingConfiguration) {

  }

  /////////////////////////////////////////////////////////////////////////////////////
  // Presenter To Model //////////////////////////////////////////////////////////////

  @Override
  public Item getItem() {
    return item;
  }

  @Override
  public void setItem(Item item) {
    this.item = item;
  }

}
