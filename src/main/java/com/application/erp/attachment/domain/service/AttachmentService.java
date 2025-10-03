package com.application.erp.attachment.domain.service;

import com.application.erp.shared.config.AttachmentType;
import com.application.erp.attachment.domain.entity.Attachment;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface AttachmentService {

    /**
     * Armazena um arquivo e o associa a uma entidade.
     *
     * @param file           O arquivo enviado.
     * @param entityType     O tipo da entidade (ex: "USER").
     * @param entityId       O ID da entidade.
     * @param useRandomName  True para gerar um nome de arquivo aleatório (UUID).
     * @param allowedTypes   Um conjunto de tipos de arquivo permitidos para este upload específico. Se nulo, usa os padrões do sistema.
     * @return A entidade Attachment salva.
     */
    Attachment storeFile(MultipartFile file, String entityType, String entityId, boolean useRandomName, Set<AttachmentType> allowedTypes) throws IOException;

    /**
     * Lista todos os anexos de uma entidade específica.
     *
     * @param entityType O tipo da entidade.
     * @param entityId   O ID da entidade.
     * @return A lista de anexos.
     */
    List<Attachment> getAttachmentsForEntity(String entityType, String entityId);

    /**
     * Carrega um anexo como um recurso para download.
     *
     * @param attachmentId O ID do anexo.
     * @return O recurso do arquivo.
     */
    Resource loadFileAsResource(Long attachmentId);
    
    /**
     * Deleta um anexo.
     *
     * @param attachmentId O ID do anexo a ser deletado.
     */
    void deleteAttachment(Long attachmentId) throws IOException;
}
