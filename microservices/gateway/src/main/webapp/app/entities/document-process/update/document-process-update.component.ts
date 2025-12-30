import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { WorkflowInstanceStatus } from 'app/entities/enumerations/workflow-instance-status.model';
import { IDocumentProcess } from '../document-process.model';
import { DocumentProcessService } from '../service/document-process.service';
import { DocumentProcessFormGroup, DocumentProcessFormService } from './document-process-form.service';

@Component({
  selector: 'jhi-document-process-update',
  templateUrl: './document-process-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DocumentProcessUpdateComponent implements OnInit {
  isSaving = false;
  documentProcess: IDocumentProcess | null = null;
  workflowInstanceStatusValues = Object.keys(WorkflowInstanceStatus);

  protected documentProcessService = inject(DocumentProcessService);
  protected documentProcessFormService = inject(DocumentProcessFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DocumentProcessFormGroup = this.documentProcessFormService.createDocumentProcessFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ documentProcess }) => {
      this.documentProcess = documentProcess;
      if (documentProcess) {
        this.updateForm(documentProcess);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const documentProcess = this.documentProcessFormService.getDocumentProcess(this.editForm);
    if (documentProcess.id !== null) {
      this.subscribeToSaveResponse(this.documentProcessService.update(documentProcess));
    } else {
      this.subscribeToSaveResponse(this.documentProcessService.create(documentProcess));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDocumentProcess>>): void {
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

  protected updateForm(documentProcess: IDocumentProcess): void {
    this.documentProcess = documentProcess;
    this.documentProcessFormService.resetForm(this.editForm, documentProcess);
  }
}
