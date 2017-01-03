package ar.com.lacomarcasistemas.centrocristalesmobile.network;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import ar.com.lacomarcasistemas.centrocristalesmobile.model.Banner;
import ar.com.lacomarcasistemas.centrocristalesmobile.model.EstadoOrdenTrabajo;

/**
 * Created by Admin on 03/01/2017.
 */

public class BannerRequest extends RetrofitSpiceRequest<Banner, BannerWSRest> {

    private String bannerId;

    public BannerRequest(String bannerId) {
        super(Banner.class, BannerWSRest.class);
        this.bannerId = bannerId;
    }

    @Override
    public Banner loadDataFromNetwork() throws Exception {
        return getService().bannerlink(bannerId);
    }
}
