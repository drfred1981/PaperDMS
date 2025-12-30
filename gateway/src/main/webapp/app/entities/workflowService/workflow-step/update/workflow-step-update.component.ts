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
import { IWorkflow } from 'app/entities/workflowService/workflow/workflow.model';
import { WorkflowService } from 'app/entities/workflowService/workflow/service/workflow.service';
import { WorkflowStepType } from 'app/entities/enumerations/workflow-step-type.model';
import { AssigneeType } from 'app/entities/enumerations/assignee-type.model';
import { WorkflowStepService } from '../service/workflow-step.service';
import { IWorkflowStep } from '../workflow-step.model';
import { WorkflowStepFormGroup, WorkflowStepFormService } from './workflow-step-form.service';

@Component({
  selector: 'jhi-workflow-step-update',
  templateUrl: './workflow-step-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class WorkflowStepUpdateComponent implements OnInit {
  isSaving = false;
  workflowStep: IWorkflowStep | null = null;
  workflowStepTypeValues = Object.keys(WorkflowStepType);
  assigneeTypeValues = Object.keys(AssigneeType);

  workflowsSharedCollection: IWorkflow[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected workflowStepService = inject(WorkflowStepService);
  protected workflowStepFormService = inject(WorkflowStepFormService);
  protected workflowService = inject(WorkflowService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: WorkflowStepFormGroup = this.workflowStepFormService.createWorkflowStepFormGroup();

  compareWorkflow = (o1: IWorkflow | null, o2: IWorkflow | null): boolean => this.workflowService.compareWorkflow(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ workflowStep }) => {
      this.workflowStep = workflowStep;
      if (workflowStep) {
        this.updateForm(workflowStep);
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
    const workflowStep = this.workflowStepFormService.getWorkflowStep(this.editForm);
    if (workflowStep.id !== null) {
      this.subscribeToSaveResponse(this.workflowStepService.update(workflowStep));
    } else {
      this.subscribeToSaveResponse(this.workflowStepService.create(workflowStep));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWorkflowStep>>): void {
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

  protected updateForm(workflowStep: IWorkflowStep): void {
    this.workflowStep = workflowStep;
    this.workflowStepFormService.resetForm(this.editForm, workflowStep);

    this.workflowsSharedCollection = this.workflowService.addWorkflowToCollectionIfMissing<IWorkflow>(
      this.workflowsSharedCollection,
      workflowStep.workflow,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.workflowService
      .query()
      .pipe(map((res: HttpResponse<IWorkflow[]>) => res.body ?? []))
      .pipe(
        map((workflows: IWorkflow[]) =>
          this.workflowService.addWorkflowToCollectionIfMissing<IWorkflow>(workflows, this.workflowStep?.workflow),
        ),
      )
      .subscribe((workflows: IWorkflow[]) => (this.workflowsSharedCollection = workflows));
  }
}
