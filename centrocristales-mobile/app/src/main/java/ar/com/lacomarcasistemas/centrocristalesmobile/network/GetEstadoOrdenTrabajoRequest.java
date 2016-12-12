package ar.com.lacomarcasistemas.centrocristalesmobile.network;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import ar.com.lacomarcasistemas.centrocristalesmobile.model.EstadoOrdenTrabajo;
import roboguice.util.temp.Ln;

/**
 * Created by Test on 07/06/2016.
 */
    public class GetEstadoOrdenTrabajoRequest extends RetrofitSpiceRequest <EstadoOrdenTrabajo, WarnesWSRest> {

    private String dominio;
    private int ot;

    public GetEstadoOrdenTrabajoRequest(String Dominio, int Ot) {
        super(EstadoOrdenTrabajo.class, WarnesWSRest.class);
        this.dominio = Dominio;
        this.ot = Ot;
    }


    @Override
    public EstadoOrdenTrabajo loadDataFromNetwork() throws Exception {
        Ln.d("Call web service ");
        return getService().getEstadoSiniestro(dominio, ot);
    }
}
