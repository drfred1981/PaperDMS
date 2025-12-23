package fr.smartprod.paperdms.document.service.dto;

import fr.smartprod.paperdms.document.domain.enumeration.PermissionType;
import fr.smartprod.paperdms.document.domain.enumeration.PrincipalType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.document.domain.DocumentPermission} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentPermissionDTO implements Serializable {

    private Long id;

    @NotNull
    private Long documentId;

    @NotNull
    private PrincipalType principalType;

    @NotNull
    @Size(max = 100)
    private String principalId;

    @NotNull
    private PermissionType permission;

    @NotNull
    private Boolean canDelegate;

    @NotNull
    @Size(max = 50)
    private String grantedBy;

    @NotNull
    private Instant grantedDate;

    private PermissionGroupDTO permissionGroup;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public PrincipalType getPrincipalType() {
        return principalType;
    }

    public void setPrincipalType(PrincipalType principalType) {
        this.principalType = principalType;
    }

    public String getPrincipalId() {
        return principalId;
    }

    public void setPrincipalId(String principalId) {
        this.principalId = principalId;
    }

    public PermissionType getPermission() {
        return permission;
    }

    public void setPermission(PermissionType permission) {
        this.permission = permission;
    }

    public Boolean getCanDelegate() {
        return canDelegate;
    }

    public void setCanDelegate(Boolean canDelegate) {
        this.canDelegate = canDelegate;
    }

    public String getGrantedBy() {
        return grantedBy;
    }

    public void setGrantedBy(String grantedBy) {
        this.grantedBy = grantedBy;
    }

    public Instant getGrantedDate() {
        return grantedDate;
    }

    public void setGrantedDate(Instant grantedDate) {
        this.grantedDate = grantedDate;
    }

    public PermissionGroupDTO getPermissionGroup() {
        return permissionGroup;
    }

    public void setPermissionGroup(PermissionGroupDTO permissionGroup) {
        this.permissionGroup = permissionGroup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentPermissionDTO)) {
            return false;
        }

        DocumentPermissionDTO documentPermissionDTO = (DocumentPermissionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, documentPermissionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentPermissionDTO{" +
            "id=" + getId() +
            ", documentId=" + getDocumentId() +
            ", principalType='" + getPrincipalType() + "'" +
            ", principalId='" + getPrincipalId() + "'" +
            ", permission='" + getPermission() + "'" +
            ", canDelegate='" + getCanDelegate() + "'" +
            ", grantedBy='" + getGrantedBy() + "'" +
            ", grantedDate='" + getGrantedDate() + "'" +
            ", permissionGroup=" + getPermissionGroup() +
            "}";
    }
}
