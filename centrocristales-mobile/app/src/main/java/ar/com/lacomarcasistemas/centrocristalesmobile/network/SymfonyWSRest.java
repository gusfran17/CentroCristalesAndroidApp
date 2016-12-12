package ar.com.lacomarcasistemas.centrocristalesmobile.network;

import ar.com.lacomarcasistemas.centrocristalesmobile.model.PostGeneric;
import retrofit.Callback;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.mime.TypedFile;

/**
 * Created by Test on 14/06/2016.
 */
public interface SymfonyWSRest {
    @Multipart
    @POST("/generics.json")
    PostGeneric postGeneric(@Part("myfile") TypedFile file, @Part("description") String description);
}
