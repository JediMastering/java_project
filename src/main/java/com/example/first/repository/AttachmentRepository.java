package com.example.first.repository;

import com.example.first.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    /**
     * Encontra todos os anexos para uma entidade específica, usando a associação polimórfica.
     *
     * @param entityType O tipo da entidade (ex: "USER", "TASK").
     * @param entityId   O ID da entidade.
     * @return Uma lista de anexos.
     */
    List<Attachment> findByEntityTypeAndEntityId(String entityType, String entityId);
}
