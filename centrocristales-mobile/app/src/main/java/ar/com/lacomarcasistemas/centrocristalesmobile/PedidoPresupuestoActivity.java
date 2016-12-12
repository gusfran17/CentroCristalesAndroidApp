package ar.com.lacomarcasistemas.centrocristalesmobile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.octo.android.robospice.exception.NetworkException;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.io.File;

import ar.com.lacomarcasistemas.centrocristalesmobile.helper.Io;
import ar.com.lacomarcasistemas.centrocristalesmobile.model.PedidoPresupuesto;
import ar.com.lacomarcasistemas.centrocristalesmobile.network.PedidoPresupuestoRequest;
import retrofit.mime.TypedFile;

public class PedidoPresupuestoActivity extends BaseSpiceActivity  {

    private static int RESULT_LOAD_IMAGE = 1;
//    private PostGenericRequest postGenericRequest;
    private PedidoPresupuestoRequest pedidoPresupuestoRequest;
    private Uri selectedImageUri;
    private RelativeLayout bar;
    private TextView txtSeleccioneImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido_presupuesto);

        bar = (RelativeLayout) this.findViewById(R.id.progressBarPedidoPresupuesto);
        txtSeleccioneImage = (TextView) this.findViewById(R.id.txtSeleccioneImagen);
    }

    public void onLoadPictureClick(View view) {
        Io.hideKeyboard(PedidoPresupuestoActivity.this);
        Intent i = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        );

        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {

                txtSeleccioneImage.setVisibility(View.INVISIBLE);

                selectedImageUri = data.getData();

                ImageView imageView = (ImageView) findViewById(R.id.imgView);
                imageView.setImageBitmap(Io.getBitmap(getBaseContext(), selectedImageUri));

            }
            else
            {
                txtSeleccioneImage.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            Toast.makeText(PedidoPresupuestoActivity.this, getString(R.string.error_seleccionando_imagen), Toast.LENGTH_SHORT).show();
        }
    }

    public void onEnviarPedidoClick(View view) {

        try {
            if(!validateInputs())
            {
                return;
            }

            Io.hideKeyboard(PedidoPresupuestoActivity.this);

            EditText txtEmailSolicitante = ((EditText) findViewById(R.id.txtEmailSolicitante));
            EditText txtMensaje = ((EditText) findViewById(R.id.txtMensajePedidoPresupuesto));
            ImageView imageView = (ImageView) findViewById(R.id.imgView);

            //tengo que guardar la imagen reducida a un archivo.
            String scaledImageFilename = Io.saveTempScaledBitmap(getBaseContext(), Io.getBitmap(getBaseContext(), selectedImageUri));

            //Instancio el file de la imagen para setear en request
            TypedFile file = new TypedFile("multipart/form-data", new File(scaledImageFilename));

            pedidoPresupuestoRequest = new PedidoPresupuestoRequest(txtEmailSolicitante.getText().toString(),
                    txtMensaje.getText().toString(),
                    file);

            getCentroCristalesTurnosWSManager().execute(pedidoPresupuestoRequest, "pedidoPresupuestoRequest", DurationInMillis.ALWAYS_EXPIRED, new PedidoPresupuestoRequestListener());

            bar.setVisibility(View.VISIBLE);

        } catch (Exception e) {
            Toast.makeText(PedidoPresupuestoActivity.this, getString(R.string.error_inesperado), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateInputs() {

        boolean validate = true;

        EditText txtEmailSolicitante = ((EditText) findViewById(R.id.txtEmailSolicitante));
        EditText txtMensaje = ((EditText) findViewById(R.id.txtMensajePedidoPresupuesto));

        if(null ==  selectedImageUri){
            Toast.makeText(PedidoPresupuestoActivity.this, "No hay imagen seleccionada para enviar", Toast.LENGTH_LONG).show();
            validate = false;
        }

        String emailText = txtEmailSolicitante.getText().toString();
        if(emailText.length() == 0 || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailText).matches()){
            txtEmailSolicitante.setError(getString(R.string.validation_email_solicitante_valido));
            validate = false;
        }

        if(txtMensaje.getText().toString().length() == 0){
            txtMensaje.setError(getString(R.string.validation_mensaje_pedido_presupuesto_requerido));
            validate = false;
        }

        return validate;
    }


    // ============================================================================================
    // INNER CLASSES
    // ============================================================================================

    public final class PedidoPresupuestoRequestListener implements RequestListener<PedidoPresupuesto> {

       @Override
        public void onRequestFailure(SpiceException spiceException) {
            bar.setVisibility(View.GONE);

           if(spiceException instanceof NetworkException){

               Io.showAlert(PedidoPresupuestoActivity.this,
                       getString(R.string.label_pedido_presupuesto),
                       getString(R.string.error_conexion_servidor));
           }
           else{
               Toast.makeText(PedidoPresupuestoActivity.this,   getString(R.string.error_inesperado), Toast.LENGTH_SHORT).show();
           }
        }

        @Override
        public void onRequestSuccess(final PedidoPresupuesto  result) {
            bar.setVisibility(View.GONE);

            Io.showAlert(PedidoPresupuestoActivity.this,
                    getString(R.string.label_pedido_presupuesto),
                    getString(R.string.ok_ws_envio_pedido_presupuesto),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    },
                    null,
                    new DialogInterface.OnDismissListener() {
                        public void onDismiss(DialogInterface dialog) {
                            PedidoPresupuestoActivity.this.finish();
                        }
                    }
            );
        }
    }

}
