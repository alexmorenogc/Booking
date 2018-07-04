package es.ulpgc.eite.clean.mvp.masterdetail.app;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import es.ulpgc.eite.clean.mvp.masterdetail.data.Item;
import es.ulpgc.eite.clean.mvp.masterdetail.detail.Detail;
import es.ulpgc.eite.clean.mvp.masterdetail.detail.DetailView;
import es.ulpgc.eite.clean.mvp.masterdetail.login.Login;
import es.ulpgc.eite.clean.mvp.masterdetail.login.LoginView;
import es.ulpgc.eite.clean.mvp.masterdetail.master.Master;
import es.ulpgc.eite.clean.mvp.masterdetail.master.MasterView;


public class MediatorApp extends Application implements Mediator.Lifecycle, Mediator.Navigation {

  private final String TAG = this.getClass().getSimpleName();

  private MasterState toMasterState;
  private DetailState masterToDetailState;
  private ListState detailToMasterState;
  private LoginState toLoginState;

  /**
   * Fija el estado inicial de la app al arrancar
   */
  @Override
  public void onCreate() {
    super.onCreate();
    Log.d(TAG, "calling onCreate()");

    //Log.d(TAG, "calling creatingInitialLoginState()");
    //toLoginState = new LoginState();
    //toLoginState.username = null;

    //Log.d(TAG, "calling creatingInitialMasterState()");
    //toMasterState = new MasterState();
    //toMasterState.hideToolbar = false;
  }

  /////////////////////////////////////////////////////////////////////////////////////


  /**
   * Llamado cuando arranca la app para fijar el estado inicial del maestro
   *
   * @param presenter implementando la interfaz necesaria para fijar el estado inicial
   */
  @Override
  public void startingMasterScreen(Master.ToMaster presenter){
    if(toMasterState != null) {
      Log.d(TAG, "calling settingInitialMasterState()");
      presenter.setToolbarVisibility(!toMasterState.hideToolbar);
      presenter.setUsername(toMasterState.username);
    }

    // Una vez fijado el estado inicial, el maestro puede iniciarse normalmente
    presenter.onScreenStarted();
  }

  /**
   * Llamado al navegar desde el maestro al detalle para fijar el estado inicial del detalle
   * en funcion de los valores recogidos desde el maestro
   *
   * @param presenter implementando la interfaz necesaria para recoger el estado a pasar al detalle
   */
  @Override
  public void goToDetailScreen(Master.MasterToDetail presenter) {
    Log.d(TAG, "calling savingInitialDetailState()");
    masterToDetailState = new DetailState();
    masterToDetailState.hideToolbar = !presenter.getToolbarVisibility();
    masterToDetailState.selectedItem = presenter.getSelectedItem();
    masterToDetailState.username = presenter.getUsername();

    // Arrancamos la pantalla del detalle sin finalizar la del maestro
    Context view = presenter.getManagedContext();
    if (view != null) {
      Log.d(TAG, "calling startingDetailScreen()");
      view.startActivity(new Intent(view, DetailView.class));
    }

  }

  @Override
  public void backToLoginScreen(Login.ToLogin presenter) {
    Log.d(TAG, "saving LoginState()");
    toLoginState = new LoginState();
    toLoginState.username = presenter.getUsername();

    Context view = presenter.getManagedContext();
    if (view != null) {
      Log.d(TAG, "calling startingLoginScreen()");
      Intent intent = new Intent(view, LoginView.class);
      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
      view.startActivity(intent);

      Log.d(TAG, "calling destroyView()");
      //presenter.destroyView();
    }
  }

  @Override
  public void goToNextScreen(Login.LoginTo presenter) {
    Log.d(TAG, "saving LoginState()");
    toLoginState = new LoginState();
    toLoginState.username = presenter.getUsername();


    Log.d(TAG, "calling creatingInitialMasterState()");
    toMasterState = new MasterState();
    toMasterState.hideToolbar = false;
    toMasterState.username = presenter.getUsername();

    Context view = presenter.getManagedContext();
    if (view != null) {
      Log.d(TAG, "calling startingMasterScreen()");
      view.startActivity(new Intent(view, MasterView.class));
      Log.d(TAG, "calling destroyView()");
      presenter.destroyView();
    }
  }

  /**
   * LLamado por el detalle al finalizar para volver al maestro fijando su nuevo estado
   * si es que este ha cambiado
   *
   * @param presenter implementando la interfaz necesaria para recoger el estado a pasar al maestro
   */
  @Override
  public void backToMasterScreen(Detail.DetailToMaster presenter){
    Log.d(TAG, "calling savingUpdatedMasterState()");
    detailToMasterState = new ListState();
    detailToMasterState.itemToDelete = presenter.getItemToDelete();
    detailToMasterState.username = presenter.getUsername();

    // Al volver al maestro, el detalle debe finalizar
    Log.d(TAG, "calling finishingDetailScreen()");
    presenter.destroyView();
  }


  /////////////////////////////////////////////////////////////////////////////////////


  /**
   * Llamado por el maestro cada vez que se reinicie, ya sea por un giro de pantalla o
   * porque el detalle a finalizado
   *
   * @param presenter implementando la interfaz necesaria para actualizar el estado del maestro
   */
  @Override
  public void resumingMasterScreen(Master.DetailToMaster presenter) {
    if(detailToMasterState != null) {
      Log.d(TAG, "calling resumingMasterScreen()");
      Log.d(TAG, "calling restoringUpdatedMasterState()");
      // Sólo se borra si no es un objeto metido para relleno
      if (detailToMasterState.itemToDelete.getShopId() != -1){
        presenter.setItemToDelete(detailToMasterState.itemToDelete);
        presenter.setUsername(detailToMasterState.username);
      }

      Log.d(TAG, "calling removingUpdatedMasterState()");
      detailToMasterState = null;
    }

    // Una vez fijado el estado inicial, el maestro puede continuar normalmente
    presenter.onScreenResumed();
  }

  /**
   * Llamado cuando arranca el detalle para fijar su estado inicial
   *
   * @param presenter implementando la interfaz necesaria para fijar su estado inicial
   *  en funcion de los valores pasado desde el maestro
   */
  @Override
  public void startingDetailScreen(Detail.MasterToDetail presenter){
    if(masterToDetailState != null) {
      Log.d(TAG, "calling settingInitialDetailState()");
      presenter.setToolbarVisibility(!masterToDetailState.hideToolbar);
      presenter.setItem(masterToDetailState.selectedItem);
      presenter.setUsername(masterToDetailState.username);

      Log.d(TAG, "calling removingInitialDetailState()");
      masterToDetailState = null;
    }

    // Una vez fijado el estado inicial, el detalle puede iniciarse normalmente
    presenter.onScreenStarted();
  }

  @Override
  public void startingLoginScreen(Login.ToLogin presenter) {
    if (toLoginState != null) {
      Log.d(TAG, "calling settingLoginState()");
      presenter.setUsername(toLoginState.username);

      Log.d(TAG, "calling removingInitialLoginState()");
      toLoginState = null;
    }
    presenter.onScreenStarted();
  }

  @Override
  public void resumingLoginScreen(Login.LoginTo presenter) {
    presenter.onScreenResumed();
  }


  /////////////////////////////////////////////////////////////////////////////////////


  /**
   * Estado a actualizar en el maestro en función de la ejecución del detalle
   */
  private class ListState {
    Item itemToDelete;
    String username;
  }

  /**
   * Estado inicial del detalle pasado por el maestro
   */
  private class DetailState {
    boolean hideToolbar;
    Item selectedItem;
    String username;
  }

  /**
   * Estado inicial del maestro
   */
  private class MasterState {
    boolean hideToolbar;
    String username;
  }

  /**
   * Estado del Login
   */
  private class LoginState {
    String username;
  }
}
