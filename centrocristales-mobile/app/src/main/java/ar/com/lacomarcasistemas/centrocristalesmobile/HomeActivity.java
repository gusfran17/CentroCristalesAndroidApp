package ar.com.lacomarcasistemas.centrocristalesmobile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import ar.com.lacomarcasistemas.centrocristalesmobile.model.Banner;


public class HomeActivity extends AppCompatActivity {

    private Banner banner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        TextView txtInfo = (TextView)findViewById(R.id.txtVersionInfo);
        txtInfo.setText(String.format("Versión: %s", BuildConfig.VERSION_NAME));

        ImageView imageView = (ImageView) findViewById(R.id.bannerPrincipal);
        banner = new Banner(getString(R.string.main_banner_id));
        banner.getImage(imageView, this);

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
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(banner.getLink()));
        startActivity(browserIntent);
    }
}
