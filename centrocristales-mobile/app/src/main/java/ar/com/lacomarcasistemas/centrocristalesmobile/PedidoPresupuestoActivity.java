package ar.com.lacomarcasistemas.centrocristalesmobile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.content.ContextCompat;
import android.support.v4.app.ActivityCompat;
import android.Manifest;
import android.content.pm.PackageManager;

import com.octo.android.robospice.exception.NetworkException;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ar.com.lacomarcasistemas.centrocristalesmobile.helper.Io;
import ar.com.lacomarcasistemas.centrocristalesmobile.model.Banner;
import ar.com.lacomarcasistemas.centrocristalesmobile.model.PedidoPresupuesto;
import ar.com.lacomarcasistemas.centrocristalesmobile.network.PedidoPresupuestoRequest;
import retrofit.mime.TypedFile;

public class PedidoPresupuestoActivity extends BaseSpiceActivity  {
    private static final String TAG = "PresupuestoActivity";
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int RESULT_LOAD_CAMERA = 2;
    private static final int RESULT_PERMISSIONS = 3;
    private static final int MEDIA_TYPE_IMAGE = 10;
//    private PostGenericRequest postGenericRequest;
    private PedidoPresupuestoRequest pedidoPresupuestoRequest;
    private Uri selectedImageUri;
    private RelativeLayout bar;
    private Banner banner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido_presupuesto);

        bar = (RelativeLayout) this.findViewById(R.id.progressBarPedidoPresupuesto);

        ImageView imageView = (ImageView) findViewById(R.id.bannerPresupuesto);
        banner = new Banner(getString(R.string.presupuesto_banner_id));
        banner.getImage(imageView, this);
    }

    public void onLoadCameraClick(View view) {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, RESULT_PERMISSIONS);
        } else {
            loadCamera();
        }
    }

    public void onLoadPictureClick(View view) {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            loadPicture();
        }
    }

    public void loadCamera(){
        selectedImageUri = getOutputMediaUri(MEDIA_TYPE_IMAGE);
        if (selectedImageUri == null){
            Toast.makeText(this,
                    R.string.validation_mensaje_error_memoria_externa,
                    Toast.LENGTH_LONG).show();
        }
        Io.hideKeyboard(PedidoPresupuestoActivity.this);
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        i.putExtra(MediaStore.EXTRA_OUTPUT, selectedImageUri);
        startActivityForResult(i, RESULT_LOAD_CAMERA);
    }

    public void loadPicture(){
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

            if (resultCode == RESULT_OK) {
                if (requestCode == RESULT_LOAD_IMAGE && null != data) {
                    selectedImageUri = data.getData();
                }
                ImageView imageView = (ImageView) findViewById(R.id.imgView);
                Bitmap bitmap = Io.getBitmap(getBaseContext(), selectedImageUri);
                imageView.setImageBitmap(bitmap);
            }
        } catch (Exception e) {
            Toast.makeText(PedidoPresupuestoActivity.this, getString(R.string.error_seleccionando_imagen), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RESULT_PERMISSIONS : {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted and now can proceed
                    loadPicture(); //a sample method called

                } else {

                    // permission denied
                    Toast.makeText(this, R.string.validation_error_permisos, Toast.LENGTH_SHORT).show();
                }
                return;
            }
            // add other cases for more permissions
        }
    }

    private Uri getOutputMediaUri(int mediaType) {
        if (isExternalStorageAvailable()){
            //Get external storage directory
            File mediaStorageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

            //Create unique file name
            String fileName = "";
            String fileType = "";
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            if (mediaType == MEDIA_TYPE_IMAGE){
                fileName = "IMG" + timeStamp;
                fileType = ".jpg";
            }
            //Create file and return it
            try {
                File mediaFile = File.createTempFile(fileName, fileType, mediaStorageDir);
                Log.i(TAG, "File: " + Uri.fromFile(mediaFile));

                return Uri.fromFile(mediaFile);
            }
            catch (IOException e) {
                Log.e(TAG, "Error al crear el archivo: " +
                        mediaStorageDir.getAbsolutePath() + fileName + fileType);
            }
        }
        return null;
    }

    private boolean isExternalStorageAvailable(){
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        else {
            return false;
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
            Toast.makeText(PedidoPresupuestoActivity.this, R.string.validation_no_hay_imagen_seleccionada, Toast.LENGTH_LONG).show();
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

    public void showWebsite(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(banner.getLink()));
        startActivity(browserIntent);
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
