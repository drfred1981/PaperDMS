import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { WorkflowInstanceStatus } from 'app/entities/enumerations/workflow-instance-status.model';
import { WorkflowPriority } from 'app/entities/enumerations/workflow-priority.model';
import { WorkflowService } from 'app/entities/workflowService/workflow/service/workflow.service';
import { IWorkflow } from 'app/entities/workflowService/workflow/workflow.model';
import { AlertErrorModel } from 'app/shared/alert/alert-error.model';
import SharedModule from 'app/shared/shared.module';
import { WorkflowInstanceService } from '../service/workflow-instance.service';
import { IWorkflowInstance } from '../workflow-instance.model';

import { WorkflowInstanceFormGroup, WorkflowInstanceFormService } from './workflow-instance-form.service';

@Component({
  selector: 'jhi-workflow-instance-update',
  templateUrl: './workflow-instance-update.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class WorkflowInstanceUpdate implements OnInit {
  isSaving = false;
  workflowInstance: IWorkflowInstance | null = null;
  workflowInstanceStatusValues = Object.keys(WorkflowInstanceStatus);
  workflowPriorityValues = Object.keys(WorkflowPriority);

  workflowsSharedCollection = signal<IWorkflow[]>([]);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected workflowInstanceService = inject(WorkflowInstanceService);
  protected workflowInstanceFormService = inject(WorkflowInstanceFormService);
  protected workflowService = inject(WorkflowService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: WorkflowInstanceFormGroup = this.workflowInstanceFormService.createWorkflowInstanceFormGroup();

  compareWorkflow = (o1: IWorkflow | null, o2: IWorkflow | null): boolean => this.workflowService.compareWorkflow(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ workflowInstance }) => {
      this.workflowInstance = workflowInstance;
      if (workflowInstance) {
        this.updateForm(workflowInstance);
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
    const workflowInstance = this.workflowInstanceFormService.getWorkflowInstance(this.editForm);
    if (workflowInstance.id === null) {
      this.subscribeToSaveResponse(this.workflowInstanceService.create(workflowInstance));
    } else {
      this.subscribeToSaveResponse(this.workflowInstanceService.update(workflowInstance));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWorkflowInstance>>): void {
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

  protected updateForm(workflowInstance: IWorkflowInstance): void {
    this.workflowInstance = workflowInstance;
    this.workflowInstanceFormService.resetForm(this.editForm, workflowInstance);

    this.workflowsSharedCollection.set(
      this.workflowService.addWorkflowToCollectionIfMissing<IWorkflow>(this.workflowsSharedCollection(), workflowInstance.workflow),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.workflowService
      .query()
      .pipe(map((res: HttpResponse<IWorkflow[]>) => res.body ?? []))
      .pipe(
        map((workflows: IWorkflow[]) =>
          this.workflowService.addWorkflowToCollectionIfMissing<IWorkflow>(workflows, this.workflowInstance?.workflow),
        ),
      )
      .subscribe((workflows: IWorkflow[]) => this.workflowsSharedCollection.set(workflows));
  }
}
