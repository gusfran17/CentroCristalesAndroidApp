package ar.com.lacomarcasistemas.centrocristalesmobile;

/**
 * Created by Test on 05/06/2016.
 */
import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import ar.com.lacomarcasistemas.centrocristalesmobile.helper.AppActivityLifecycleAdapter;
import ar.com.lacomarcasistemas.centrocristalesmobile.helper.FontsOverride;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //eventos comunes a la app
        registerActivityLifecycleCallbacks(new AppActivityLifecycleAdapter());

        try {
            FontsOverride.setDefaultFont(this, "MONOSPACE", "RobotoSlab-Regular.ttf");
        } catch (Exception e) {
            //no puede fallar la aplicación si no puede overridear el font.
        }
    }


}