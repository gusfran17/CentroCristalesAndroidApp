package ar.com.lacomarcasistemas.centrocristalesmobile.network;

import com.octo.android.robospice.retrofit.RetrofitGsonSpiceService;

import ar.com.lacomarcasistemas.centrocristalesmobile.model.EstadoOrdenTrabajo;
import ar.com.lacomarcasistemas.centrocristalesmobile.model.PedidoPresupuesto;
import ar.com.lacomarcasistemas.centrocristalesmobile.model.PostGeneric;

public class CentroCristalesTurnosWSService extends RetrofitGsonSpiceService {

    //private static final String BASE_URL = "http://demos.lacomarcasistemas.com.ar/centrocristales-turnos/web/ws/v1";
    //private static final String BASE_URL = "http://10.0.2.2:88/lcs/centrocristales-turnos/web/app_dev.php/ws/v1";
    //private static final String BASE_URL = "http://centrocristales.com/turnos-app-test/web/ws/v1";
    private static final String BASE_URL = "http://centrocristales.com/turnos-app/web/ws/v1";

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
