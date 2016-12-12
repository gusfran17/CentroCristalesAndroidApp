package ar.com.lacomarcasistemas.centrocristalesmobile.network;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import ar.com.lacomarcasistemas.centrocristalesmobile.model.PedidoPresupuesto;
import ar.com.lacomarcasistemas.centrocristalesmobile.model.PostGeneric;
import retrofit.mime.TypedFile;
import roboguice.util.temp.Ln;

/**
 * Created by Test on 20/06/2016.
 */
public class PedidoPresupuestoRequest extends RetrofitSpiceRequest<PedidoPresupuesto, CentroCristalesTurnosWS> {

    private final String email;
    private final String mensaje;
    private final TypedFile imagen;

    public PedidoPresupuestoRequest(String email, String mensaje, TypedFile imagen) {
        super(PedidoPresupuesto.class, CentroCristalesTurnosWS.class);

        this.email= email;
        this.mensaje = mensaje;
        this.imagen = imagen;
    }


    @Override
    public PedidoPresupuesto loadDataFromNetwork() throws Exception {
        Ln.d("Call PedidoPresupuestoRequest method");

        return getService().sendPedidoPresupuesto(this.email, this.mensaje, this.imagen);
    }
}
