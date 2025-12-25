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
import { ImportRuleService } from '../service/import-rule.service';
import { IImportRule } from '../import-rule.model';
import { ImportRuleFormGroup, ImportRuleFormService } from './import-rule-form.service';

@Component({
  selector: 'jhi-import-rule-update',
  templateUrl: './import-rule-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ImportRuleUpdateComponent implements OnInit {
  isSaving = false;
  importRule: IImportRule | null = null;

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected importRuleService = inject(ImportRuleService);
  protected importRuleFormService = inject(ImportRuleFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ImportRuleFormGroup = this.importRuleFormService.createImportRuleFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ importRule }) => {
      this.importRule = importRule;
      if (importRule) {
        this.updateForm(importRule);
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
    const importRule = this.importRuleFormService.getImportRule(this.editForm);
    if (importRule.id !== null) {
      this.subscribeToSaveResponse(this.importRuleService.update(importRule));
    } else {
      this.subscribeToSaveResponse(this.importRuleService.create(importRule));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IImportRule>>): void {
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

  protected updateForm(importRule: IImportRule): void {
    this.importRule = importRule;
    this.importRuleFormService.resetForm(this.editForm, importRule);
  }
}
