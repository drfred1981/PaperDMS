package fr.smartprod.paperdms.workflow.service.mapper;

import fr.smartprod.paperdms.workflow.domain.WorkflowInstance;
import fr.smartprod.paperdms.workflow.domain.WorkflowStep;
import fr.smartprod.paperdms.workflow.domain.WorkflowTask;
import fr.smartprod.paperdms.workflow.service.dto.WorkflowInstanceDTO;
import fr.smartprod.paperdms.workflow.service.dto.WorkflowStepDTO;
import fr.smartprod.paperdms.workflow.service.dto.WorkflowTaskDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WorkflowTask} and its DTO {@link WorkflowTaskDTO}.
 */
@Mapper(componentModel = "spring")
public interface WorkflowTaskMapper extends EntityMapper<WorkflowTaskDTO, WorkflowTask> {
    @Mapping(target = "instance", source = "instance", qualifiedByName = "workflowInstanceId")
    @Mapping(target = "step", source = "step", qualifiedByName = "workflowStepId")
    WorkflowTaskDTO toDto(WorkflowTask s);

    @Named("workflowInstanceId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    WorkflowInstanceDTO toDtoWorkflowInstanceId(WorkflowInstance workflowInstance);

    @Named("workflowStepId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    WorkflowStepDTO toDtoWorkflowStepId(WorkflowStep workflowStep);
}
