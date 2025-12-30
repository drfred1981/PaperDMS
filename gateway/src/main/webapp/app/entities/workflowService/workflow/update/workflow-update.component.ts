import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { WorkflowService } from '../service/workflow.service';
import { IWorkflow } from '../workflow.model';
import { WorkflowFormGroup, WorkflowFormService } from './workflow-form.service';

@Component({
  selector: 'jhi-workflow-update',
  templateUrl: './workflow-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class WorkflowUpdateComponent implements OnInit {
  isSaving = false;
  workflow: IWorkflow | null = null;

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected workflowService = inject(WorkflowService);
  protected workflowFormService = inject(WorkflowFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: WorkflowFormGroup = this.workflowFormService.createWorkflowFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ workflow }) => {
      this.workflow = workflow;
      if (workflow) {
        this.updateForm(workflow);
      }
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
    const workflow = this.workflowFormService.getWorkflow(this.editForm);
    if (workflow.id !== null) {
      this.subscribeToSaveResponse(this.workflowService.update(workflow));
    } else {
      this.subscribeToSaveResponse(this.workflowService.create(workflow));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWorkflow>>): void {
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

  protected updateForm(workflow: IWorkflow): void {
    this.workflow = workflow;
    this.workflowFormService.resetForm(this.editForm, workflow);
  }
}
