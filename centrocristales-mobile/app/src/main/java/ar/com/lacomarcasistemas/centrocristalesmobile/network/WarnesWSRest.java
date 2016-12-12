package ar.com.lacomarcasistemas.centrocristalesmobile.network;

import ar.com.lacomarcasistemas.centrocristalesmobile.model.EstadoOrdenTrabajo;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by Test on 06/06/2016.
 */
public interface WarnesWSRest {
    @GET("/WarnesWSRest.svc/getEstadoSiniestro/{dominio}/{ot}")
    EstadoOrdenTrabajo getEstadoSiniestro(@Path("dominio") String dominio, @Path("ot") int ot);
}
