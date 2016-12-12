package ar.com.lacomarcasistemas.centrocristalesmobile.network;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import ar.com.lacomarcasistemas.centrocristalesmobile.model.PostGeneric;
import retrofit.mime.TypedFile;
import roboguice.util.temp.Ln;

/**
 * Created by Test on 14/06/2016.
 */
public class PostGenericRequest extends RetrofitSpiceRequest<PostGeneric, SymfonyWSRest> {


    private final TypedFile file;
    private final String description;

    public PostGenericRequest(TypedFile file, String description) {
        super(PostGeneric.class, SymfonyWSRest.class);

        this.file = file;
        this.description = description;
    }

    @Override
    public PostGeneric loadDataFromNetwork() throws Exception {
        Ln.d("Call web service ");

        return getService().postGeneric(this.file, this.description);

    }
}
