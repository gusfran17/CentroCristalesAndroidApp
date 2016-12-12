package ar.com.lacomarcasistemas.centrocristalesmobile.network;

import ar.com.lacomarcasistemas.centrocristalesmobile.model.PedidoPresupuesto;
import ar.com.lacomarcasistemas.centrocristalesmobile.model.PostGeneric;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.mime.TypedFile;

/**
 * Created by Test on 20/06/2016.
 */
public interface CentroCristalesTurnosWS {
    @Multipart
    @POST("/sendpedidopresupuesto")
    PedidoPresupuesto sendPedidoPresupuesto(@Part("lcs_webservice_pedidopresupuesto[email]") String email,
                                            @Part("lcs_webservice_pedidopresupuesto[mensaje]") String mensaje,
                                            @Part("lcs_webservice_pedidopresupuesto[imagen]") TypedFile imagen);
}
