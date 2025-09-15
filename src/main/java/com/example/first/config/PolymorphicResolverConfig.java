package com.example.first.config;

import com.example.first.polymorphic.PolymorphicEntityResolver;
import com.example.first.repository.AccessGroupRepository;
import com.example.first.repository.FeatureRepository;
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