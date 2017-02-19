package com.example.telerik.urbanissues.tasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.example.telerik.urbanissues.R;
import com.example.telerik.urbanissues.models.BaseViewModel;
import com.example.telerik.urbanissues.models.ImageKind;
import com.telerik.everlive.sdk.core.EverliveApp;
import com.telerik.everlive.sdk.core.model.system.File;
import com.telerik.everlive.sdk.core.result.RequestResult;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.UUID;

public class BitmapDownloadTask extends AsyncTask<String, Void, Bitmap> {

    private EverliveApp urbanIssuesApp;
    private final WeakReference<ImageView> imageViewWeakReference;
    private Context context;
    private ImageKind imageKind;

    public BitmapDownloadTask(Context context, ImageView imageView, ImageKind imageKind) {
        this.imageViewWeakReference = new WeakReference<ImageView>(imageView);
        this.context = context;
        this.imageKind = imageKind;
        BaseViewModel.initialize(urbanIssuesApp);
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        String pictureId = strings[0];
        try {
            if (pictureId != null) {
                Bitmap bitmap = BaseViewModel.getInstance().getPictureById(UUID.fromString(pictureId));
                if (bitmap != null) {
                    return bitmap;
                }

                String url = null;
                RequestResult userImageRequest = urbanIssuesApp.workWith().files().getById(pictureId).executeSync();
                if (userImageRequest.getSuccess()) {
                    url = ((File) userImageRequest.getValue()).getUri();
                    URL imageUrl = new URL(url);
                    InputStream inputStream = imageUrl.openStream();
                    bitmap = BitmapFactory.decodeStream(inputStream);

                    BaseViewModel.getInstance().addPicture(UUID.fromString(pictureId), bitmap);

                    return bitmap;
                }
            }

            switch (this.imageKind) {
                case User : {
                    return BitmapFactory.decodeResource(this.context.getResources(), R.drawable.ic_no_picture);
                }
                default : {
                    return null;
                }
            }
        } catch (Exception ex) {
            System.out.println("Exception when downloading image: " + ex.getMessage().toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (imageViewWeakReference != null && bitmap != null) {
            final ImageView imageView = imageViewWeakReference.get();
            if (imageView != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }
}
