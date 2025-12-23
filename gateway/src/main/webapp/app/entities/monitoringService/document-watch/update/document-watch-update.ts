import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { WatchType } from 'app/entities/enumerations/watch-type.model';
import SharedModule from 'app/shared/shared.module';
import { IDocumentWatch } from '../document-watch.model';
import { DocumentWatchService } from '../service/document-watch.service';

import { DocumentWatchFormGroup, DocumentWatchFormService } from './document-watch-form.service';

@Component({
  selector: 'jhi-document-watch-update',
  templateUrl: './document-watch-update.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class DocumentWatchUpdate implements OnInit {
  isSaving = false;
  documentWatch: IDocumentWatch | null = null;
  watchTypeValues = Object.keys(WatchType);

  protected documentWatchService = inject(DocumentWatchService);
  protected documentWatchFormService = inject(DocumentWatchFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DocumentWatchFormGroup = this.documentWatchFormService.createDocumentWatchFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ documentWatch }) => {
      this.documentWatch = documentWatch;
      if (documentWatch) {
        this.updateForm(documentWatch);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving = true;
    const documentWatch = this.documentWatchFormService.getDocumentWatch(this.editForm);
    if (documentWatch.id === null) {
      this.subscribeToSaveResponse(this.documentWatchService.create(documentWatch));
    } else {
      this.subscribeToSaveResponse(this.documentWatchService.update(documentWatch));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDocumentWatch>>): void {
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

  protected updateForm(documentWatch: IDocumentWatch): void {
    this.documentWatch = documentWatch;
    this.documentWatchFormService.resetForm(this.editForm, documentWatch);
  }
}
