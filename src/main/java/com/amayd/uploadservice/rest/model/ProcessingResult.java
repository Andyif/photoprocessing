package com.amayd.uploadservice.rest.model;


import javax.persistence.*;

@Entity
public class ProcessingResult {

    @Id
    private Long uid;
    private Boolean finished = false;

    public ProcessingResult() {
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }
}
