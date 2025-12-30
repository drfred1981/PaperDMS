import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IWorkflowInstance } from 'app/entities/workflowService/workflow-instance/workflow-instance.model';
import { WorkflowInstanceService } from 'app/entities/workflowService/workflow-instance/service/workflow-instance.service';
import { TaskAction } from 'app/entities/enumerations/task-action.model';
import { WorkflowApprovalHistoryService } from '../service/workflow-approval-history.service';
import { IWorkflowApprovalHistory } from '../workflow-approval-history.model';
import { WorkflowApprovalHistoryFormGroup, WorkflowApprovalHistoryFormService } from './workflow-approval-history-form.service';

@Component({
  selector: 'jhi-workflow-approval-history-update',
  templateUrl: './workflow-approval-history-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class WorkflowApprovalHistoryUpdateComponent implements OnInit {
  isSaving = false;
  workflowApprovalHistory: IWorkflowApprovalHistory | null = null;
  taskActionValues = Object.keys(TaskAction);

  workflowInstancesSharedCollection: IWorkflowInstance[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected workflowApprovalHistoryService = inject(WorkflowApprovalHistoryService);
  protected workflowApprovalHistoryFormService = inject(WorkflowApprovalHistoryFormService);
  protected workflowInstanceService = inject(WorkflowInstanceService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: WorkflowApprovalHistoryFormGroup = this.workflowApprovalHistoryFormService.createWorkflowApprovalHistoryFormGroup();

  compareWorkflowInstance = (o1: IWorkflowInstance | null, o2: IWorkflowInstance | null): boolean =>
    this.workflowInstanceService.compareWorkflowInstance(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ workflowApprovalHistory }) => {
      this.workflowApprovalHistory = workflowApprovalHistory;
      if (workflowApprovalHistory) {
        this.updateForm(workflowApprovalHistory);
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
        this.eventManager.broadcast(new EventWithContent<AlertError>('gatewayApp.error', { ...err, key: `error.file.${err.key}` })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const workflowApprovalHistory = this.workflowApprovalHistoryFormService.getWorkflowApprovalHistory(this.editForm);
    if (workflowApprovalHistory.id !== null) {
      this.subscribeToSaveResponse(this.workflowApprovalHistoryService.update(workflowApprovalHistory));
    } else {
      this.subscribeToSaveResponse(this.workflowApprovalHistoryService.create(workflowApprovalHistory));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWorkflowApprovalHistory>>): void {
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

  protected updateForm(workflowApprovalHistory: IWorkflowApprovalHistory): void {
    this.workflowApprovalHistory = workflowApprovalHistory;
    this.workflowApprovalHistoryFormService.resetForm(this.editForm, workflowApprovalHistory);

    this.workflowInstancesSharedCollection = this.workflowInstanceService.addWorkflowInstanceToCollectionIfMissing<IWorkflowInstance>(
      this.workflowInstancesSharedCollection,
      workflowApprovalHistory.workflowInstance,
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
            this.workflowApprovalHistory?.workflowInstance,
          ),
        ),
      )
      .subscribe((workflowInstances: IWorkflowInstance[]) => (this.workflowInstancesSharedCollection = workflowInstances));
  }
}
