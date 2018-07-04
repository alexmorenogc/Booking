package es.ulpgc.eite.clean.mvp.masterdetail.app;

import es.ulpgc.eite.clean.mvp.masterdetail.detail.Detail;
import es.ulpgc.eite.clean.mvp.masterdetail.login.Login;
import es.ulpgc.eite.clean.mvp.masterdetail.master.Master;

public interface Mediator {

  interface Lifecycle {

    void startingMasterScreen(Master.ToMaster presenter);
    void resumingMasterScreen(Master.DetailToMaster presenter);
    void startingDetailScreen(Detail.MasterToDetail presenter);

    //Login
    void startingLoginScreen(Login.ToLogin presenter);
    void resumingLoginScreen(Login.LoginTo presenter);
  }

  interface Navigation {

    void backToMasterScreen(Detail.DetailToMaster presenter);
    void goToDetailScreen(Master.MasterToDetail presenter);

    //Login
    void backToLoginScreen(Login.ToLogin presenter);
    void goToNextScreen(Login.LoginTo presenter);
  }

}
