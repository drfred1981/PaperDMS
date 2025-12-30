package fr.smartprod.paperdms.workflow.service.mapper;

import fr.smartprod.paperdms.workflow.domain.Workflow;
import fr.smartprod.paperdms.workflow.domain.WorkflowStep;
import fr.smartprod.paperdms.workflow.service.dto.WorkflowDTO;
import fr.smartprod.paperdms.workflow.service.dto.WorkflowStepDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WorkflowStep} and its DTO {@link WorkflowStepDTO}.
 */
@Mapper(componentModel = "spring")
public interface WorkflowStepMapper extends EntityMapper<WorkflowStepDTO, WorkflowStep> {
    @Mapping(target = "workflow", source = "workflow", qualifiedByName = "workflowId")
    WorkflowStepDTO toDto(WorkflowStep s);

    @Named("workflowId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    WorkflowDTO toDtoWorkflowId(Workflow workflow);
}
