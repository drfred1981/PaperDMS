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
import { IManual } from 'app/entities/businessDocService/manual/manual.model';
import { ManualService } from 'app/entities/businessDocService/manual/service/manual.service';
import { ManualChapterService } from '../service/manual-chapter.service';
import { IManualChapter } from '../manual-chapter.model';
import { ManualChapterFormGroup, ManualChapterFormService } from './manual-chapter-form.service';

@Component({
  selector: 'jhi-manual-chapter-update',
  templateUrl: './manual-chapter-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ManualChapterUpdateComponent implements OnInit {
  isSaving = false;
  manualChapter: IManualChapter | null = null;

  manualChaptersSharedCollection: IManualChapter[] = [];
  manualsSharedCollection: IManual[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected manualChapterService = inject(ManualChapterService);
  protected manualChapterFormService = inject(ManualChapterFormService);
  protected manualService = inject(ManualService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ManualChapterFormGroup = this.manualChapterFormService.createManualChapterFormGroup();

  compareManualChapter = (o1: IManualChapter | null, o2: IManualChapter | null): boolean =>
    this.manualChapterService.compareManualChapter(o1, o2);

  compareManual = (o1: IManual | null, o2: IManual | null): boolean => this.manualService.compareManual(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ manualChapter }) => {
      this.manualChapter = manualChapter;
      if (manualChapter) {
        this.updateForm(manualChapter);
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
    const manualChapter = this.manualChapterFormService.getManualChapter(this.editForm);
    if (manualChapter.id !== null) {
      this.subscribeToSaveResponse(this.manualChapterService.update(manualChapter));
    } else {
      this.subscribeToSaveResponse(this.manualChapterService.create(manualChapter));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IManualChapter>>): void {
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

  protected updateForm(manualChapter: IManualChapter): void {
    this.manualChapter = manualChapter;
    this.manualChapterFormService.resetForm(this.editForm, manualChapter);

    this.manualChaptersSharedCollection = this.manualChapterService.addManualChapterToCollectionIfMissing<IManualChapter>(
      this.manualChaptersSharedCollection,
      manualChapter.parentChapter,
    );
    this.manualsSharedCollection = this.manualService.addManualToCollectionIfMissing<IManual>(
      this.manualsSharedCollection,
      manualChapter.manual,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.manualChapterService
      .query()
      .pipe(map((res: HttpResponse<IManualChapter[]>) => res.body ?? []))
      .pipe(
        map((manualChapters: IManualChapter[]) =>
          this.manualChapterService.addManualChapterToCollectionIfMissing<IManualChapter>(
            manualChapters,
            this.manualChapter?.parentChapter,
          ),
        ),
      )
      .subscribe((manualChapters: IManualChapter[]) => (this.manualChaptersSharedCollection = manualChapters));

    this.manualService
      .query()
      .pipe(map((res: HttpResponse<IManual[]>) => res.body ?? []))
      .pipe(map((manuals: IManual[]) => this.manualService.addManualToCollectionIfMissing<IManual>(manuals, this.manualChapter?.manual)))
      .subscribe((manuals: IManual[]) => (this.manualsSharedCollection = manuals));
  }
}
