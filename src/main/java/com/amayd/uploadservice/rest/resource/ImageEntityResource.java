package com.amayd.uploadservice.rest.resource;

import com.amayd.uploadservice.rest.model.ImageNewSize;
import org.springframework.hateoas.ResourceSupport;

public class ImageEntityResource extends ResourceSupport{

    private String name;
    private String url;
    private ImageNewSize imageNewSize;

    public ImageEntityResource() {
    }

    public ImageEntityResource(String name, String url, ImageNewSize imageNewSize) {
        this.name = name;
        this.url = url;
        this.imageNewSize = imageNewSize;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setImageNewSize(ImageNewSize imageNewSize) {
        this.imageNewSize = imageNewSize;
    }
}
