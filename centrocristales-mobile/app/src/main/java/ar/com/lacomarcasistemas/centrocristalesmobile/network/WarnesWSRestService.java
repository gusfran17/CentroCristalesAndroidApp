package ar.com.lacomarcasistemas.centrocristalesmobile.network;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.octo.android.robospice.retrofit.RetrofitGsonSpiceService;

import ar.com.lacomarcasistemas.centrocristalesmobile.model.EstadoOrdenTrabajo;

public class WarnesWSRestService extends RetrofitGsonSpiceService {

    private static final String BASE_URL = "http://centrocristales.dynns.com:8094/WSCRMREST/";

    @Override
    public void onCreate() {
        super.onCreate();
        addRetrofitInterface(EstadoOrdenTrabajo.class);
    }

    @Override
    protected String getServerUrl() {
        return BASE_URL;
    }
}
