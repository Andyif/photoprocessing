package com.amayd.uploadservice.rest.model;

public class ImageEntity {

    private int id;
    private String name;
    private String url;
    private ImageNewSize imageNewSize;
    private Boolean finished = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ImageNewSize getImageNewSize() {
        return imageNewSize;
    }

    public void setImageNewSize(ImageNewSize imageNewSize) {
        this.imageNewSize = imageNewSize;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }
}
