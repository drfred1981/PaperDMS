import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { AlertErrorModel } from 'app/shared/alert/alert-error.model';
import SharedModule from 'app/shared/shared.module';
import { IImportRule } from '../import-rule.model';
import { ImportRuleService } from '../service/import-rule.service';

import { ImportRuleFormGroup, ImportRuleFormService } from './import-rule-form.service';

@Component({
  selector: 'jhi-import-rule-update',
  templateUrl: './import-rule-update.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class ImportRuleUpdate implements OnInit {
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
        this.eventManager.broadcast(new EventWithContent<AlertErrorModel>('gatewayApp.error', { ...err, key: `error.file.${err.key}` })),
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving = true;
    const importRule = this.importRuleFormService.getImportRule(this.editForm);
    if (importRule.id === null) {
      this.subscribeToSaveResponse(this.importRuleService.create(importRule));
    } else {
      this.subscribeToSaveResponse(this.importRuleService.update(importRule));
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
