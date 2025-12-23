import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { TaskAction } from 'app/entities/enumerations/task-action.model';
import { TaskStatus } from 'app/entities/enumerations/task-status.model';
import { WorkflowInstanceService } from 'app/entities/workflowService/workflow-instance/service/workflow-instance.service';
import { IWorkflowInstance } from 'app/entities/workflowService/workflow-instance/workflow-instance.model';
import { WorkflowStepService } from 'app/entities/workflowService/workflow-step/service/workflow-step.service';
import { IWorkflowStep } from 'app/entities/workflowService/workflow-step/workflow-step.model';
import { AlertErrorModel } from 'app/shared/alert/alert-error.model';
import SharedModule from 'app/shared/shared.module';

import { IWorkflowTask } from '../workflow-task.model';
import { WorkflowTaskFormService, WorkflowTaskFormGroup } from './workflow-task-form.service';
import { WorkflowTaskService } from '../service/workflow-task.service';

@Component({
  selector: 'jhi-workflow-task-update',
  templateUrl: './workflow-task-update.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class WorkflowTaskUpdate implements OnInit {
  isSaving = false;
  workflowTask: IWorkflowTask | null = null;
  taskStatusValues = Object.keys(TaskStatus);
  taskActionValues = Object.keys(TaskAction);

  workflowInstancesSharedCollection = signal<IWorkflowInstance[]>([]);
  workflowStepsSharedCollection = signal<IWorkflowStep[]>([]);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected workflowTaskService = inject(WorkflowTaskService);
  protected workflowTaskFormService = inject(WorkflowTaskFormService);
  protected workflowInstanceService = inject(WorkflowInstanceService);
  protected workflowStepService = inject(WorkflowStepService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: WorkflowTaskFormGroup = this.workflowTaskFormService.createWorkflowTaskFormGroup();

  compareWorkflowInstance = (o1: IWorkflowInstance | null, o2: IWorkflowInstance | null): boolean =>
    this.workflowInstanceService.compareWorkflowInstance(o1, o2);

  compareWorkflowStep = (o1: IWorkflowStep | null, o2: IWorkflowStep | null): boolean =>
    this.workflowStepService.compareWorkflowStep(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ workflowTask }) => {
      this.workflowTask = workflowTask;
      if (workflowTask) {
        this.updateForm(workflowTask);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertErrorModel>('gatewayApp.error', { ...err, key: `error.file.${err.key}` })),
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving = true;
    const workflowTask = this.workflowTaskFormService.getWorkflowTask(this.editForm);
    if (workflowTask.id === null) {
      this.subscribeToSaveResponse(this.workflowTaskService.create(workflowTask));
    } else {
      this.subscribeToSaveResponse(this.workflowTaskService.update(workflowTask));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWorkflowTask>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(workflowTask: IWorkflowTask): void {
    this.workflowTask = workflowTask;
    this.workflowTaskFormService.resetForm(this.editForm, workflowTask);

    this.workflowInstancesSharedCollection.set(
      this.workflowInstanceService.addWorkflowInstanceToCollectionIfMissing<IWorkflowInstance>(
        this.workflowInstancesSharedCollection(),
        workflowTask.instance,
      ),
    );
    this.workflowStepsSharedCollection.set(
      this.workflowStepService.addWorkflowStepToCollectionIfMissing<IWorkflowStep>(this.workflowStepsSharedCollection(), workflowTask.step),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.workflowInstanceService
      .query()
      .pipe(map((res: HttpResponse<IWorkflowInstance[]>) => res.body ?? []))
      .pipe(
        map((workflowInstances: IWorkflowInstance[]) =>
          this.workflowInstanceService.addWorkflowInstanceToCollectionIfMissing<IWorkflowInstance>(
            workflowInstances,
            this.workflowTask?.instance,
          ),
        ),
      )
      .subscribe((workflowInstances: IWorkflowInstance[]) => this.workflowInstancesSharedCollection.set(workflowInstances));

    this.workflowStepService
      .query()
      .pipe(map((res: HttpResponse<IWorkflowStep[]>) => res.body ?? []))
      .pipe(
        map((workflowSteps: IWorkflowStep[]) =>
          this.workflowStepService.addWorkflowStepToCollectionIfMissing<IWorkflowStep>(workflowSteps, this.workflowTask?.step),
        ),
      )
      .subscribe((workflowSteps: IWorkflowStep[]) => this.workflowStepsSharedCollection.set(workflowSteps));
  }
}
