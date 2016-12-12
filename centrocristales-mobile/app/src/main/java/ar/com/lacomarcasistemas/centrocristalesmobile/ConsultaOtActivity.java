package ar.com.lacomarcasistemas.centrocristalesmobile;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.octo.android.robospice.exception.NetworkException;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import ar.com.lacomarcasistemas.centrocristalesmobile.helper.Io;
import ar.com.lacomarcasistemas.centrocristalesmobile.model.EstadoOrdenTrabajo;
import ar.com.lacomarcasistemas.centrocristalesmobile.network.GetEstadoOrdenTrabajoRequest;

public class ConsultaOtActivity extends BaseSpiceActivity {

    private TextView mTextView;
    private GetEstadoOrdenTrabajoRequest estadoOrdenTrabajoRequest;
    private RelativeLayout bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_ot);

        mTextView = (TextView) findViewById(R.id.txtStatus);
        bar = (RelativeLayout) this.findViewById(R.id.progressBarConsultaOt);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void consultarOrdenTrabajo(View view) {
        try {
            if (!validateInputs()) {
                return;
            }

            Io.hideKeyboard(ConsultaOtActivity.this);

            String dominio = ((EditText) findViewById(R.id.txtDominio)).getText().toString();
            String otText = ((EditText) findViewById(R.id.txtOrdenTrabajo)).getText().toString();

            int ot = Integer.parseInt(otText);

            estadoOrdenTrabajoRequest = new GetEstadoOrdenTrabajoRequest(dominio, ot);
            getCrmWSManager().execute(estadoOrdenTrabajoRequest, "getEstadoOt", DurationInMillis.ALWAYS_EXPIRED, new ConsultaEstadoOtRequestListener());
            bar.setVisibility(View.VISIBLE);

        } catch (Exception e) {
            Toast.makeText(ConsultaOtActivity.this, getString(R.string.error_inesperado), Toast.LENGTH_SHORT).show();
        }
    }

    // ============================================================================================
    // PRIVATE METHODS
    // ============================================================================================

    private void showEstadoOrdenTrabajo(final EstadoOrdenTrabajo estadoOrdenTrabajo) {
        try {
            if (estadoOrdenTrabajo.status == EstadoOrdenTrabajo.OT_ENCONTRADA) //OT Encontrada
            {
                mTextView.setText(getMensajeEstado(estadoOrdenTrabajo.detalleOrdenTrabajo.id_estado));
            } else {
                mTextView.setText("");
                Toast.makeText(this, getString(R.string.mensaje_ot_no_encontrada), Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.error_inesperado), Toast.LENGTH_SHORT).show();
        }
    }

    private String getMensajeEstado(String estadoId) {
        if (estadoId.equals(EstadoOrdenTrabajo.ESTADO_PASADO)) {
            return getString(R.string.estado_ot_descripcion_terminado);
        } else if (estadoId.equals(EstadoOrdenTrabajo.ESTADO_EN_TALLER)) {
            return getString(R.string.estado_ot_descripcion_taller);
        }

        return "N/A";
    }

    private boolean validateInputs() {
        boolean validate = true;

        EditText txtNumeroOt = ((EditText) findViewById(R.id.txtOrdenTrabajo));
        EditText txtDominio = ((EditText) findViewById(R.id.txtDominio));

        if (txtNumeroOt.getText().toString().length() == 0) {
            txtNumeroOt.setError(getString(R.string.validation_numero_ot_requerido));
            validate = false;
        }

        if (txtDominio.getText().toString().length() == 0) {
            txtDominio.setError(getString(R.string.validation_dominio_requerido));
            validate = false;
        }

        return validate;
    }


    // ============================================================================================
    // INNER CLASSES
    // ============================================================================================

    public final class ConsultaEstadoOtRequestListener implements RequestListener<EstadoOrdenTrabajo> {
        @Override
        public void onRequestFailure(SpiceException spiceException) {
            bar.setVisibility(View.GONE);

            if (spiceException instanceof NetworkException) {

                Io.showAlert(ConsultaOtActivity.this,
                        getString(R.string.label_pedido_presupuesto),
                        getString(R.string.error_conexion_servidor));
            } else {
                Toast.makeText(ConsultaOtActivity.this, getString(R.string.error_inesperado), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onRequestSuccess(final EstadoOrdenTrabajo result) {
            try {
                bar.setVisibility(View.GONE);
                if (result.status == 0) //OT encontrada
                {
                    result.buildDetalleOrdenTrabajo();
                }
                showEstadoOrdenTrabajo(result);
            } catch (Exception e) {
                Toast.makeText(ConsultaOtActivity.this, getString(R.string.error_inesperado), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
