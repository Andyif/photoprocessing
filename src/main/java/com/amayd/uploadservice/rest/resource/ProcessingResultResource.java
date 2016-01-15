package com.amayd.uploadservice.rest.resource;


import com.amayd.uploadservice.rest.model.ImageEntity;
import org.springframework.hateoas.ResourceSupport;

public class ProcessingResultResource extends ResourceSupport {

    Long id;
    ImageEntity imageEntity;
    Boolean finished;

    public void setId(Long id) {
        this.id = id;
    }

    public void setImageEntity(ImageEntity imageEntity) {
        this.imageEntity = imageEntity;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }
}
