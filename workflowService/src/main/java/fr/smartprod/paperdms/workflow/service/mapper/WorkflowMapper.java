package fr.smartprod.paperdms.workflow.service.mapper;

import fr.smartprod.paperdms.workflow.domain.Workflow;
import fr.smartprod.paperdms.workflow.service.dto.WorkflowDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Workflow} and its DTO {@link WorkflowDTO}.
 */
@Mapper(componentModel = "spring")
public interface WorkflowMapper extends EntityMapper<WorkflowDTO, Workflow> {}
