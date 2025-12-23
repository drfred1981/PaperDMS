package fr.smartprod.paperdms.business.service.dto;

import fr.smartprod.paperdms.business.domain.enumeration.ClauseType;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.business.domain.ContractClause} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContractClauseDTO implements Serializable {

    private Long id;

    @NotNull
    private Long contractId;

    @NotNull
    @Size(max = 50)
    private String clauseNumber;

    @NotNull
    @Size(max = 255)
    private String title;

    @Lob
    private String content;

    private ClauseType clauseType;

    @NotNull
    private Boolean isMandatory;

    @NotNull
    private ContractDTO contract;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public String getClauseNumber() {
        return clauseNumber;
    }

    public void setClauseNumber(String clauseNumber) {
        this.clauseNumber = clauseNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ClauseType getClauseType() {
        return clauseType;
    }

    public void setClauseType(ClauseType clauseType) {
        this.clauseType = clauseType;
    }

    public Boolean getIsMandatory() {
        return isMandatory;
    }

    public void setIsMandatory(Boolean isMandatory) {
        this.isMandatory = isMandatory;
    }

    public ContractDTO getContract() {
        return contract;
    }

    public void setContract(ContractDTO contract) {
        this.contract = contract;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContractClauseDTO)) {
            return false;
        }

        ContractClauseDTO contractClauseDTO = (ContractClauseDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, contractClauseDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContractClauseDTO{" +
            "id=" + getId() +
            ", contractId=" + getContractId() +
            ", clauseNumber='" + getClauseNumber() + "'" +
            ", title='" + getTitle() + "'" +
            ", content='" + getContent() + "'" +
            ", clauseType='" + getClauseType() + "'" +
            ", isMandatory='" + getIsMandatory() + "'" +
            ", contract=" + getContract() +
            "}";
    }
}
