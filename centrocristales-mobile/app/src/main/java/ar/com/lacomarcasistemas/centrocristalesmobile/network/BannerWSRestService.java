package ar.com.lacomarcasistemas.centrocristalesmobile.network;

import com.octo.android.robospice.retrofit.RetrofitGsonSpiceService;

import ar.com.lacomarcasistemas.centrocristalesmobile.model.PedidoPresupuesto;

/**
 * Created by Admin on 03/01/2017.
 */

public class BannerWSRestService extends RetrofitGsonSpiceService {
    private static final String BASE_URL = "http://centrocristales.com/turnos-app-new/web/ws/v1";

    @Override
    public void onCreate() {
        super.onCreate();
        addRetrofitInterface(PedidoPresupuesto.class);
    }

    @Override
    protected String getServerUrl() {
        return BASE_URL;
    }
}
