import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ManualType } from 'app/entities/enumerations/manual-type.model';
import { ManualStatus } from 'app/entities/enumerations/manual-status.model';
import { IManual } from '../manual.model';
import { ManualService } from '../service/manual.service';
import { ManualFormGroup, ManualFormService } from './manual-form.service';

@Component({
  selector: 'jhi-manual-update',
  templateUrl: './manual-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ManualUpdateComponent implements OnInit {
  isSaving = false;
  manual: IManual | null = null;
  manualTypeValues = Object.keys(ManualType);
  manualStatusValues = Object.keys(ManualStatus);

  protected manualService = inject(ManualService);
  protected manualFormService = inject(ManualFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ManualFormGroup = this.manualFormService.createManualFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ manual }) => {
      this.manual = manual;
      if (manual) {
        this.updateForm(manual);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const manual = this.manualFormService.getManual(this.editForm);
    if (manual.id !== null) {
      this.subscribeToSaveResponse(this.manualService.update(manual));
    } else {
      this.subscribeToSaveResponse(this.manualService.create(manual));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IManual>>): void {
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

  protected updateForm(manual: IManual): void {
    this.manual = manual;
    this.manualFormService.resetForm(this.editForm, manual);
  }
}
