package com.amayd.uploadservice.rest.resourceAssemblers;

import com.amayd.uploadservice.rest.controller.ResizeImageController;
import com.amayd.uploadservice.rest.model.ImageEntity;
import com.amayd.uploadservice.rest.resource.ImageEntityResource;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

public class ImageEntityAssembler extends ResourceAssemblerSupport<ImageEntity, ImageEntityResource>{

    public ImageEntityAssembler() {
        super(ResizeImageController.class, ImageEntityResource.class);
    }

    @Override
    public ImageEntityResource toResource(ImageEntity imageEntity) {
        ImageEntityResource resource = createResourceWithId(imageEntity.getId(), imageEntity);
        resource.setHeight(imageEntity.getHeight());
        resource.setWidth(imageEntity.getWidth());
        resource.setName(imageEntity.getName());
        resource.setUrl(imageEntity.getUrl());
        return resource;
    }
}
