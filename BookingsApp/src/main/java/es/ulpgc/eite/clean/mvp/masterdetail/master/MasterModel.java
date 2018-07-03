package es.ulpgc.eite.clean.mvp.masterdetail.master;

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
import es.ulpgc.eite.clean.mvp.masterdetail.app.Booking;
import es.ulpgc.eite.clean.mvp.masterdetail.data.BookingItem;
import es.ulpgc.eite.clean.mvp.masterdetail.data.Item;
import es.ulpgc.eite.clean.mvp.masterdetail.data.ShopItem;
import es.ulpgc.eite.clean.mvp.masterdetail.app.Shop;


public class MasterModel
    extends GenericModel<Master.ModelToPresenter> implements Master.PresenterToModel {

  public List<Item> items = null;
  public List<Item> bookingItems = null;
  private boolean runningTask, isRunningTaskBookingList;
  private String errorMsg;
  private boolean bookingListReady = false;

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
      queryOnDatabase();
    } else {
      if(!runningTask){
        if (bookingItems == null){
          getPresenter().onLoadItemsTaskFinished(items);
        } else {
          getPresenter().onLoadItemsTaskFinished(bookingItems);
        }
      } else {
        getPresenter().onLoadItemsTaskStarted();
      }
    }
  }

  /**
   * Método que borra del arraylist el item y además también borra el item de Firebase.
   * @param item a borrar
   */
  @Override
  public void deleteItem(Item item) {
    // Es posible tener null proque sólo borraremos si es necesario, sino, metemos null
    if (item != null) {
      if (bookingItems.contains(item)){
          bookingItems.remove(item);
          // Generamos un BookingItem si pertenece a esta y hacemos el borrado.
          if (item instanceof BookingItem){
            BookingItem itemToDelete = (BookingItem) item;
            connection.child("booking").child(itemToDelete.getIdFirebase()).removeValue();
          }
      } else {
        getPresenter().onErrorDeletingItem(item);
      }
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
    bookingItems = null;
    loadItems();
  }

  @Override
  public String getErrorMessage() {
    return errorMsg;
  }

  /**
   * Método que comprueba si hay array generado de los items de las reservas
   * y si no lo hay ejecuta el método para obtenerlo con la id de la tienda
   * @param id de la tienda a buscar
   */
  @Override
  public void onShopClickedLoadBookings(int id) {
    if(bookingItems == null && !isRunningTaskBookingList) {
      queryOnDatabaseBookingList(id);
    } else {
      if(!isRunningTaskBookingList){
        getPresenter().onLoadItemsTaskFinished(bookingItems);
      } else {
        getPresenter().onLoadItemsTaskStarted();
      }
    }
  }

  @Override
  public boolean isBookingListReady() {
    return bookingListReady;
  }

  @Override
  public void setBookingListReady(boolean b) {
    bookingListReady = b;
  }

  /**
   * Método que genera el array con las reservas desde Firebase y manda al presentador
   * el array con todas las reservas.
   * @param id de la tienda a buscar
   */
  private void queryOnDatabaseBookingList(int id) {
    Log.d(TAG, "calling queryOnDatabaseBookingList()");
    isRunningTaskBookingList = true;
    getPresenter().onLoadItemsTaskStarted();

    Query query = connection.child("booking").orderByChild("shopId").equalTo(id);
    query.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        if (dataSnapshot.exists()){
          ArrayList<Booking> bookingList = new ArrayList<>();
          ArrayList<String> bookingListNames = new ArrayList<>();
          for (DataSnapshot child: dataSnapshot.getChildren()) {
            bookingList.add(child.getValue(Booking.class));
            bookingListNames.add(child.getKey());
          }
          setBookingItems(bookingList, bookingListNames);
          bookingListReady = true;
          isRunningTaskBookingList = false;
          getPresenter().onLoadItemsTaskFinished(bookingItems);
        } else {
          bookingListReady = true;
          isRunningTaskBookingList = false;
          getPresenter().onLoadItemsTaskFinished(emptyBookingList());
        }
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {

      }
    });
  }

  /**
   * Método privado para generar una lista vacía con el texto No hay reservas.
   * @return lista vacía
   */
  private List<Item> emptyBookingList() {
    ArrayList<Item> emptyList = new ArrayList<>();
    Booking empty = new Booking(-1,"No hay reservas","-","-","-",-1,-1);
    BookingItem emptyItem = new BookingItem(empty,"-");
    emptyList.add(emptyItem);
    return emptyList;
  }

  /////////////////////////////////////////////////////////////////////////////////////


  /**
   * Método en desuso para añadir items al arraylist
   * @param item
   */
  private void addItem(Item item) {
    //items.add(item);
  }

  /**
   * Método para crear un item en una posición en concreto.
   * @param position
   * @return
   */
  private Item createItem(int position) {
    return null;
  }

  /**
   * Método en desuso para generar los detalles un objeto.
   * @param position
   * @return
   */
  private String makeDetails(int position) {
    StringBuilder builder = new StringBuilder();
    builder.append("Details about Item: ").append(position).append("\n");
    for (int count = 0; count < position; count++) {
      builder.append("\nMore details information here.");
    }
    return builder.toString();
  }

  /**
   * Método privado para generar el ArrayList de items a partir de un ArrayList de tiendas
   * @param shops ArrayList de tiendas
   */
  private void setShopItems(ArrayList<Shop> shops){
    Log.d(TAG, "calling setShopItems");

    this.items = new ArrayList<>();
    for (int i = 0; i < shops.size(); i++){
      ShopItem item = new ShopItem(shops.get(i));
      items.add(item);
    }
  }

  /**
   * Método privado para generar el ArrayList de items a partir de un ArrayList de reservas
   * @param bookings ArrayList de tiendas
   * @param idsFirebase identificadores de Firebase
   */
  private void setBookingItems(ArrayList<Booking> bookings, ArrayList<String> idsFirebase){
    Log.d(TAG, "calling setBookingItems");

    this.bookingItems = new ArrayList<>();
    for (int i = 0; i < bookings.size(); i++){
      BookingItem item = new BookingItem(bookings.get(i),idsFirebase.get(i));
      bookingItems.add(item);
    }
  }

  /**
   * Método privado para obtener de Firebase la lista de tiendas
   */
  private void queryOnDatabase() {
    Log.d(TAG, "calling queryOnDatabase()");
    runningTask = true;
    getPresenter().onLoadItemsTaskStarted();

    Query query = connection.child("shops");
    query.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        GenericTypeIndicator<ArrayList<Shop>> indicator = new GenericTypeIndicator<ArrayList<Shop>>() { };
        ArrayList<Shop> shopList = dataSnapshot.getValue(indicator);
        if (!shopList.isEmpty()) {
          setShopItems(shopList);
          runningTask = false;
          getPresenter().onLoadItemsTaskFinished(items);
        } else {
          runningTask = false;
          getPresenter().onLoadItemsTaskFinished(emptyShopList());
        }
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {

      }
    });
  }

  /**
   * Método para generar ArrayList vacío de tiendas.
   * @return ArrayList vacío
   */
  private List<Item> emptyShopList() {
    ArrayList<Item> emptyList = new ArrayList<>();
    Shop empty = new Shop("No hay tiendas","",-1,"",-1,"","");
    ShopItem emptyItem = new ShopItem(empty);
    emptyList.add(emptyItem);
    return emptyList;
  }

}
