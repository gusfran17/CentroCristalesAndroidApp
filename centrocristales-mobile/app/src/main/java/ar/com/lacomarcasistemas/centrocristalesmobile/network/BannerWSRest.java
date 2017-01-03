package ar.com.lacomarcasistemas.centrocristalesmobile.network;

import ar.com.lacomarcasistemas.centrocristalesmobile.model.Banner;
import ar.com.lacomarcasistemas.centrocristalesmobile.model.EstadoOrdenTrabajo;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by Admin on 03/01/2017.
 */

public interface BannerWSRest {
    @GET("/bannerlink/{bannerId}")
    Banner bannerlink(@Path("bannerId") String bannerId);
}
