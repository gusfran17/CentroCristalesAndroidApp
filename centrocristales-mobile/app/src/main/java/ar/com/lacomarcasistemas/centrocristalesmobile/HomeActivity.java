package ar.com.lacomarcasistemas.centrocristalesmobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        TextView txtInfo = (TextView)findViewById(R.id.txtVersionInfo);
        txtInfo.setText(String.format("Versión: %s", BuildConfig.VERSION_NAME));
    }

    public void onConsultarOrdenTrabajoClick(View view) {
        Intent intent = new Intent(this, ConsultaOtActivity.class);
        startActivity(intent);
    }

    public void onPedidoPresupuestoClick(View view) {
        Intent intent = new Intent(this, PedidoPresupuestoActivity.class);
        startActivity(intent);
    }
}
