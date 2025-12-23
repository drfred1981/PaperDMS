import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { TaskAction } from 'app/entities/enumerations/task-action.model';
import { AlertErrorModel } from 'app/shared/alert/alert-error.model';
import SharedModule from 'app/shared/shared.module';
import { IApprovalHistory } from '../approval-history.model';
import { ApprovalHistoryService } from '../service/approval-history.service';

import { ApprovalHistoryFormGroup, ApprovalHistoryFormService } from './approval-history-form.service';

@Component({
  selector: 'jhi-approval-history-update',
  templateUrl: './approval-history-update.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class ApprovalHistoryUpdate implements OnInit {
  isSaving = false;
  approvalHistory: IApprovalHistory | null = null;
  taskActionValues = Object.keys(TaskAction);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected approvalHistoryService = inject(ApprovalHistoryService);
  protected approvalHistoryFormService = inject(ApprovalHistoryFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ApprovalHistoryFormGroup = this.approvalHistoryFormService.createApprovalHistoryFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ approvalHistory }) => {
      this.approvalHistory = approvalHistory;
      if (approvalHistory) {
        this.updateForm(approvalHistory);
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
        this.eventManager.broadcast(new EventWithContent<AlertErrorModel>('gatewayApp.error', { ...err, key: `error.file.${err.key}` })),
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving = true;
    const approvalHistory = this.approvalHistoryFormService.getApprovalHistory(this.editForm);
    if (approvalHistory.id === null) {
      this.subscribeToSaveResponse(this.approvalHistoryService.create(approvalHistory));
    } else {
      this.subscribeToSaveResponse(this.approvalHistoryService.update(approvalHistory));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IApprovalHistory>>): void {
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

  protected updateForm(approvalHistory: IApprovalHistory): void {
    this.approvalHistory = approvalHistory;
    this.approvalHistoryFormService.resetForm(this.editForm, approvalHistory);
  }
}
