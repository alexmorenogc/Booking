package es.ulpgc.eite.clean.mvp.masterdetail.master;


import android.content.Context;

import java.util.List;

import es.ulpgc.eite.clean.mvp.ContextView;
import es.ulpgc.eite.clean.mvp.Model;
import es.ulpgc.eite.clean.mvp.Presenter;
import es.ulpgc.eite.clean.mvp.masterdetail.data.Item;

public interface Master {

  /**
   * Interfaz que permite fijar el estado de la pantalla del maestro cuando la app arranca
   */
  interface ToMaster {
    void onScreenStarted();
    void setToolbarVisibility(boolean visible);
  }

  /**
   * Interfaz que permite iniciar la pantalla del detalle y recopilar los valores necesarios
   * para rellenar el estado inicial que se pasará a la pantalla del detalle al iniciarse
   */
  interface MasterToDetail {
    Context getManagedContext();
    Item getSelectedItem();
    boolean getToolbarVisibility();
  }

  /**
   * Interfaz que permite fijar los valores incluidos en el estado pasado desde la pantalla
   * del detalle cuando está finaliza
   */
  interface DetailToMaster {
    void onScreenResumed();
    void setItemToDelete(Item item);
  }


  /////////////////////////////////////////////////////////////////////////////////////


  /**
   * Methods offered to VIEW to communicate with PRESENTER
   */
  interface ViewToPresenter extends Presenter<PresenterToView> {
    void onItemClicked(Item item);
    void onRestoreActionClicked();
    void onResumingContent();
  }

  /**
   * Required VIEW methods available to PRESENTER
   */
  interface PresenterToView extends ContextView {
    void hideProgress();
    void hideToolbar();
    void showError(String msg);
    void showProgress();
    void setRecyclerAdapterContent(List<Item> items);
  }

  /**
   * Methods offered to MODEL to communicate with PRESENTER
   */
  interface PresenterToModel extends Model<ModelToPresenter> {
    void deleteItem(Item item);
    void loadItems();
    void reloadItems();
    String getErrorMessage();
    void onShopClickedLoadBookings(int id);
    boolean isBookingListReady();
    void setBookingListReady(boolean b);
  }

  /**
   * Required PRESENTER methods available to MODEL
   */
  interface ModelToPresenter {
    void onErrorDeletingItem(Item item);
    void onLoadItemsTaskFinished(List<Item> items);
    void onLoadItemsTaskStarted();
  }

}
