package com.amayd.uploadservice.rest.model;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class ProcessingResult {

    @Id
    Long id;
    @OneToOne
    ImageEntity imageEntity;
    Boolean finished;

    public ProcessingResult() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ImageEntity getImageEntity() {
        return imageEntity;
    }

    public void setImageEntity(ImageEntity imageEntity) {
        this.imageEntity = imageEntity;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }
}
