package ar.com.lacomarcasistemas.centrocristalesmobile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;

import ar.com.lacomarcasistemas.centrocristalesmobile.model.Banner;
import ar.com.lacomarcasistemas.centrocristalesmobile.network.BannerRequest;


public class HomeActivity extends BaseSpiceActivity {

    private Banner banner;
    private BannerRequest bannerRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        TextView txtInfo = (TextView)findViewById(R.id.txtVersionInfo);
        txtInfo.setText(String.format("Versión: %s", BuildConfig.VERSION_NAME));

        ImageView imageView = (ImageView) findViewById(R.id.bannerPrincipal);
        banner = new Banner(getString(R.string.main_banner_id), imageView, this);
        banner.setImage();

        bannerRequest = new BannerRequest(getString(R.string.main_banner_id));
        getBannerWSManager().execute(bannerRequest, "bannerLink", DurationInMillis.ALWAYS_EXPIRED, new HomeActivity.BannerRequestListener());

    }

    public void onConsultarOrdenTrabajoClick(View view) {
        Intent intent = new Intent(this, ConsultaOtActivity.class);
        startActivity(intent);
    }

    public void onPedidoPresupuestoClick(View view) {
        Intent intent = new Intent(this, PedidoPresupuestoActivity.class);
        startActivity(intent);
    }

    public void showWebsite(View view) {
        String link = banner.getLink();
        if (link == null) link = getString(R.string.default_banner_website);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        startActivity(browserIntent);
    }

    public class BannerRequestListener implements com.octo.android.robospice.request.listener.RequestListener<Banner> {
        @Override
        public void onRequestFailure(SpiceException spiceException) {
            spiceException.printStackTrace();
            banner.link = getString(R.string.default_banner_website);
        }

        @Override
        public void onRequestSuccess(Banner newBanner) {
            banner.link = newBanner.link;
        }
    }
}
