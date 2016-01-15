package com.amayd.uploadservice.rest.model;

public class ImageNewSize {

    private int height;
    private int width;

    public ImageNewSize(int height, int width) {
        this.height = height;
        this.width = width;
    }

    public ImageNewSize() {
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
