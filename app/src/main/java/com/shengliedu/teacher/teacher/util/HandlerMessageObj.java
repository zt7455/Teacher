package com.shengliedu.teacher.teacher.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by zt10 on 2017/2/13.
 */

public class HandlerMessageObj {
    private Context context;
    private String type;
    private String json;
    private String link;
    private ImageView imageView;
    private Bitmap bitmap;
    private JCVideoPlayerStandard jcVideoPlayer;

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setJcVideoPlayer(JCVideoPlayerStandard jcVideoPlayer) {
        this.jcVideoPlayer = jcVideoPlayer;
    }

    public JCVideoPlayerStandard getJcVideoPlayer() {
        return jcVideoPlayer;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
