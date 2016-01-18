package com.amayd.uploadservice.rest.resource;

import org.springframework.hateoas.ResourceSupport;

public class ImageEntityResource extends ResourceSupport{

    private String name;
    private String url;
    private int height;
    private int width;

    public ImageEntityResource() {
    }

    public ImageEntityResource(String name, String url, Integer height, Integer width) {
        this.name = name;
        this.url = url;
        this.height = height;
        this.width = width;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
