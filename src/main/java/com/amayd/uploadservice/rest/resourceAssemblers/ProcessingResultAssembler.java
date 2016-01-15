package com.amayd.uploadservice.rest.resourceAssemblers;

import com.amayd.uploadservice.rest.controller.ResizeImageController;
import com.amayd.uploadservice.rest.model.ImageEntity;
import com.amayd.uploadservice.rest.model.ProcessingResult;
import com.amayd.uploadservice.rest.resource.ImageEntityResource;
import com.amayd.uploadservice.rest.resource.ProcessingResultResource;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.web.bind.annotation.RestController;

public class ProcessingResultAssembler extends ResourceAssemblerSupport<ProcessingResult, ProcessingResultResource>{

    public ProcessingResultAssembler() {
        super(ResizeImageController.class, ProcessingResultResource.class);
    }

    @Override
    public ProcessingResultResource toResource(ProcessingResult processingResult) {
        ProcessingResultResource resource = createResourceWithId(processingResult.getId(), processingResult);
        resource.setFinished(processingResult.getFinished());
        resource.setImageEntity(processingResult.getImageEntity());
        return resource;
    }
}
