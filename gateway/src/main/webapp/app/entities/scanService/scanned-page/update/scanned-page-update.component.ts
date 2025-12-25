import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IScanJob } from 'app/entities/scanService/scan-job/scan-job.model';
import { ScanJobService } from 'app/entities/scanService/scan-job/service/scan-job.service';
import { IScannedPage } from '../scanned-page.model';
import { ScannedPageService } from '../service/scanned-page.service';
import { ScannedPageFormGroup, ScannedPageFormService } from './scanned-page-form.service';

@Component({
  selector: 'jhi-scanned-page-update',
  templateUrl: './scanned-page-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ScannedPageUpdateComponent implements OnInit {
  isSaving = false;
  scannedPage: IScannedPage | null = null;

  scanJobsSharedCollection: IScanJob[] = [];

  protected scannedPageService = inject(ScannedPageService);
  protected scannedPageFormService = inject(ScannedPageFormService);
  protected scanJobService = inject(ScanJobService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ScannedPageFormGroup = this.scannedPageFormService.createScannedPageFormGroup();

  compareScanJob = (o1: IScanJob | null, o2: IScanJob | null): boolean => this.scanJobService.compareScanJob(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ scannedPage }) => {
      this.scannedPage = scannedPage;
      if (scannedPage) {
        this.updateForm(scannedPage);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const scannedPage = this.scannedPageFormService.getScannedPage(this.editForm);
    if (scannedPage.id !== null) {
      this.subscribeToSaveResponse(this.scannedPageService.update(scannedPage));
    } else {
      this.subscribeToSaveResponse(this.scannedPageService.create(scannedPage));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IScannedPage>>): void {
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

  protected updateForm(scannedPage: IScannedPage): void {
    this.scannedPage = scannedPage;
    this.scannedPageFormService.resetForm(this.editForm, scannedPage);

    this.scanJobsSharedCollection = this.scanJobService.addScanJobToCollectionIfMissing<IScanJob>(
      this.scanJobsSharedCollection,
      scannedPage.scanJob,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.scanJobService
      .query()
      .pipe(map((res: HttpResponse<IScanJob[]>) => res.body ?? []))
      .pipe(
        map((scanJobs: IScanJob[]) => this.scanJobService.addScanJobToCollectionIfMissing<IScanJob>(scanJobs, this.scannedPage?.scanJob)),
      )
      .subscribe((scanJobs: IScanJob[]) => (this.scanJobsSharedCollection = scanJobs));
  }
}
