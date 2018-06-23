package es.ulpgc.eite.clean.mvp.masterdetail.master;

import android.os.Handler;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import es.ulpgc.eite.clean.mvp.GenericModel;
import es.ulpgc.eite.clean.mvp.masterdetail.app.ModelItem;
import es.ulpgc.eite.clean.mvp.masterdetail.app.Shop;


public class MasterModel
    extends GenericModel<Master.ModelToPresenter> implements Master.PresenterToModel {

  public List<ModelItem> items = null;
  private boolean runningTask;
  private String errorMsg;

  private DatabaseReference connection;
  private FirebaseDatabase database;

  /**
   * Method that recovers a reference to the PRESENTER
   * You must ALWAYS call {@link super#onCreate(Object)} here
   *
   * @param presenter Presenter interface
   */
  @Override
  public void onCreate(Master.ModelToPresenter presenter) {
    super.onCreate(presenter);
    Log.d(TAG, "calling onCreate()");

    errorMsg = "Error deleting item!";

    // Conectar con la BBDD
    database = FirebaseDatabase.getInstance();
    // Generar una referencia con la que conectar.
    connection = database.getReference();
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


  /**
   * Llamado para recuperar los elementos a mostrar en la lista.
   * Si el contenido ya ha sido fijado antes, se notificará inmediatamente al presentador y,
   * sino es el caso, la notificación se realizará al finalizar la tarea que fija este contenido
   */
  @Override
  public void loadItems() {
    if(items == null && !runningTask) {
      startDelayedTask();
    } else {
      if(!runningTask){
        getPresenter().onLoadItemsTaskFinished(items);
      } else {
        getPresenter().onLoadItemsTaskStarted();
      }
    }
  }


  @Override
  public void deleteItem(ModelItem item) {
    if (items.contains(item)){
      items.remove(item);
    } else {
      getPresenter().onErrorDeletingItem(item);
    }
  }

  /**
   * Llamado para recuperar los elementos iniciales de la lista.
   * En este caso siempre se llamará a la tarea asíncrona
   */
  @Override
  public void reloadItems() {
    items = null;
    loadItems();
  }

  @Override
  public String getErrorMessage() {
    return errorMsg;
  }

  /////////////////////////////////////////////////////////////////////////////////////


  private void addItem(ModelItem item) {
    items.add(item);
  }

  private ModelItem createItem(int position) {
    // TODO: 23/6/18 Hacer crear item 
    return null;
  }

  private String makeDetails(int position) {
    StringBuilder builder = new StringBuilder();
    builder.append("Details about Item: ").append(position).append("\n");
    for (int count = 0; count < position; count++) {
      builder.append("\nMore details information here.");
    }
    return builder.toString();
  }

  private void setItems(ArrayList<Shop> query){
    this.items = new ArrayList<>();
    for (int i = 0; i < query.size(); i++){
      ModelItem item = new ModelItem(query.get(i));
      items.add(item);
    }
  }

  /**
   * Llamado para recuperar los elementos a mostrar en la lista.
   * Consiste en una tarea asíncrona que retrasa un tiempo la obtención del contenido.
   * El modelo notificará al presentador cuando se inicia y cuando finaliza esta tarea.
   */
  private void startDelayedTask() {
    Log.d(TAG, "calling startDelayedTask()");
    runningTask = true;
    getPresenter().onLoadItemsTaskStarted();

    Query query = connection.child("shops");
    query.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        GenericTypeIndicator<ArrayList<Shop>> indicator = new GenericTypeIndicator<ArrayList<Shop>>() { };
        ArrayList<Shop> shopList = dataSnapshot.getValue(indicator);
        if (!shopList.isEmpty()) {
          setItems(shopList);
          runningTask = false;
          getPresenter().onLoadItemsTaskFinished(items);
        } else {
          getPresenter().onLoadItemsTaskFinished(null);
        }
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {

      }
    });
    /*
    // Mock Hello: A handler to delay the answer
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        //setItems();
        runningTask = false;
        getPresenter().onLoadItemsTaskFinished(items);
      }
    }, 1000);
    */
  }

}
