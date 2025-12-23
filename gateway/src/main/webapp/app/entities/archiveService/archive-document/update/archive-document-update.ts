import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IArchiveJob } from 'app/entities/archiveService/archive-job/archive-job.model';
import { ArchiveJobService } from 'app/entities/archiveService/archive-job/service/archive-job.service';
import SharedModule from 'app/shared/shared.module';
import { IArchiveDocument } from '../archive-document.model';
import { ArchiveDocumentService } from '../service/archive-document.service';

import { ArchiveDocumentFormGroup, ArchiveDocumentFormService } from './archive-document-form.service';

@Component({
  selector: 'jhi-archive-document-update',
  templateUrl: './archive-document-update.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class ArchiveDocumentUpdate implements OnInit {
  isSaving = false;
  archiveDocument: IArchiveDocument | null = null;

  archiveJobsSharedCollection = signal<IArchiveJob[]>([]);

  protected archiveDocumentService = inject(ArchiveDocumentService);
  protected archiveDocumentFormService = inject(ArchiveDocumentFormService);
  protected archiveJobService = inject(ArchiveJobService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ArchiveDocumentFormGroup = this.archiveDocumentFormService.createArchiveDocumentFormGroup();

  compareArchiveJob = (o1: IArchiveJob | null, o2: IArchiveJob | null): boolean => this.archiveJobService.compareArchiveJob(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ archiveDocument }) => {
      this.archiveDocument = archiveDocument;
      if (archiveDocument) {
        this.updateForm(archiveDocument);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving = true;
    const archiveDocument = this.archiveDocumentFormService.getArchiveDocument(this.editForm);
    if (archiveDocument.id === null) {
      this.subscribeToSaveResponse(this.archiveDocumentService.create(archiveDocument));
    } else {
      this.subscribeToSaveResponse(this.archiveDocumentService.update(archiveDocument));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IArchiveDocument>>): void {
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

  protected updateForm(archiveDocument: IArchiveDocument): void {
    this.archiveDocument = archiveDocument;
    this.archiveDocumentFormService.resetForm(this.editForm, archiveDocument);

    this.archiveJobsSharedCollection.set(
      this.archiveJobService.addArchiveJobToCollectionIfMissing<IArchiveJob>(
        this.archiveJobsSharedCollection(),
        archiveDocument.archiveJob,
      ),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.archiveJobService
      .query()
      .pipe(map((res: HttpResponse<IArchiveJob[]>) => res.body ?? []))
      .pipe(
        map((archiveJobs: IArchiveJob[]) =>
          this.archiveJobService.addArchiveJobToCollectionIfMissing<IArchiveJob>(archiveJobs, this.archiveDocument?.archiveJob),
        ),
      )
      .subscribe((archiveJobs: IArchiveJob[]) => this.archiveJobsSharedCollection.set(archiveJobs));
  }
}
