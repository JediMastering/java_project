package com.example.first.polymorphic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class PolymorphicEntityResolver {

    private final Map<String, JpaRepository<?, Long>> registry = new HashMap<>();

    public void register(String type, JpaRepository<?, Long> repository) {
        registry.put(type.toUpperCase(), repository);
    }

    public Object resolve(String type, Long id) {
        if (type == null || id == null) {
            return null;
        }
        JpaRepository<?, Long> repo = registry.get(type.toUpperCase());
        if (repo == null) {
            throw new IllegalArgumentException("Tipo não registrado: " + type);
        }
        return repo.findById(id).orElse(null);
    }
}
