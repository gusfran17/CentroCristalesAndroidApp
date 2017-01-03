package ar.com.lacomarcasistemas.centrocristalesmobile.model;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import ar.com.lacomarcasistemas.centrocristalesmobile.R;

/**
 * Created by tavoArgento on 01/01/2017.
 */

public class Banner {
    private static final String bannerImageUrl = "http://centrocristales.com/turnos-app-new/web/ws/v1/bannerimage/";
    private String id;
    private Activity context;
    public String link;
    private Target target;

    public Banner(String id, final ImageView imageView, Activity context){
        this.id = id;
        this.context = context;
        target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                // Bitmap is loaded, use image here
                imageView.setImageBitmap(bitmap);
                imageView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }

        };
    }

    public void setImage(){
        String imageUrl= bannerImageUrl + this.id;
        Uri imageUri = Uri.parse(imageUrl);
        Picasso.with(context).load(imageUri).into(target);

    }

    public String getLink() {
        return this.link;
    }
}
