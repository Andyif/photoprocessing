package com.amayd.uploadservice.rest.resource;


import com.amayd.uploadservice.rest.model.ImageEntity;
import org.springframework.hateoas.ResourceSupport;

public class ProcessingResultResource extends ResourceSupport {

    private Boolean finished;
    private Long uid;

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Boolean getFinished() {
        return finished;
    }

    public Long getUid() {
        return uid;
    }
}
