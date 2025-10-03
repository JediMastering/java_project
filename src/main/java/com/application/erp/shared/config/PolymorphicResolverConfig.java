package com.application.erp.shared.config;

import com.application.erp.shared.polymorphic.PolymorphicEntityResolver;
import com.application.erp.accessgroup.domain.repository.AccessGroupRepository;
import com.application.erp.feature.domain.repository.FeatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PolymorphicResolverConfig {

    @Autowired
    public void configure(
        PolymorphicEntityResolver resolver,
        AccessGroupRepository accessGroupRepository,
        FeatureRepository featureRepository
    ) {
        resolver.register("ACCESS_GROUP", accessGroupRepository);
        resolver.register("FEATURE", featureRepository);
    }
}