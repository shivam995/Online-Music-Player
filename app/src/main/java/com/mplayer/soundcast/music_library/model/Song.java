package com.mplayer.soundcast.music_library.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Song implements Serializable {


    @SerializedName("createdAt")
    @Expose
    private String createdAt;

    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;

    @SerializedName("link")
    @Expose
    private String link;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("music_file")
    @Expose
    private MusicFile music_file;

    @SerializedName("thumbnail_file")
    @Expose
    private MusicFile thumbnail_file;

    @SerializedName("objectId")
    @Expose
    private String objectId;

    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;


    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public MusicFile getMusic_file() {
        return music_file;
    }

    public void setMusic_file(MusicFile music_file) {
        this.music_file = music_file;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "ClassPojo [createdAt = " + createdAt + ", thumbnail = " + thumbnail + ", link = " + link + ", title = " + title + ", music_file = " + music_file + ", objectId = " + objectId + ", updatedAt = " + updatedAt + "]";
    }
}