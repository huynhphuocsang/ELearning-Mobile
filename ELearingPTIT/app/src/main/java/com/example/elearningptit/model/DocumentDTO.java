package com.example.elearningptit.model;

import java.sql.Timestamp;

public class DocumentDTO {
    private Long documentId;
    private String documentName;
    private Timestamp createAt;

    public DocumentDTO(Long documentId, String documentName, Timestamp createAt) {
        this.documentId = documentId;
        this.documentName = documentName;
        this.createAt = createAt;
    }

    public DocumentDTO() {
    }

    public Long getDocumentId() {
        return documentId;
    }

    public String getDocumentName() {
        return documentName;
    }

    public Timestamp getCreateAt() {
        return createAt;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }
}
