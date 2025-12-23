import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { IImportRule } from 'app/entities/emailImportService/import-rule/import-rule.model';
import { ImportRuleService } from 'app/entities/emailImportService/import-rule/service/import-rule.service';
import { EmailField } from 'app/entities/enumerations/email-field.model';
import { MappingTransformation } from 'app/entities/enumerations/mapping-transformation.model';
import { AlertErrorModel } from 'app/shared/alert/alert-error.model';
import SharedModule from 'app/shared/shared.module';
import { IImportMapping } from '../import-mapping.model';
import { ImportMappingService } from '../service/import-mapping.service';

import { ImportMappingFormGroup, ImportMappingFormService } from './import-mapping-form.service';

@Component({
  selector: 'jhi-import-mapping-update',
  templateUrl: './import-mapping-update.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class ImportMappingUpdate implements OnInit {
  isSaving = false;
  importMapping: IImportMapping | null = null;
  emailFieldValues = Object.keys(EmailField);
  mappingTransformationValues = Object.keys(MappingTransformation);

  importRulesSharedCollection = signal<IImportRule[]>([]);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected importMappingService = inject(ImportMappingService);
  protected importMappingFormService = inject(ImportMappingFormService);
  protected importRuleService = inject(ImportRuleService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ImportMappingFormGroup = this.importMappingFormService.createImportMappingFormGroup();

  compareImportRule = (o1: IImportRule | null, o2: IImportRule | null): boolean => this.importRuleService.compareImportRule(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ importMapping }) => {
      this.importMapping = importMapping;
      if (importMapping) {
        this.updateForm(importMapping);
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
        this.eventManager.broadcast(new EventWithContent<AlertErrorModel>('gatewayApp.error', { ...err, key: `error.file.${err.key}` })),
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving = true;
    const importMapping = this.importMappingFormService.getImportMapping(this.editForm);
    if (importMapping.id === null) {
      this.subscribeToSaveResponse(this.importMappingService.create(importMapping));
    } else {
      this.subscribeToSaveResponse(this.importMappingService.update(importMapping));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IImportMapping>>): void {
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

  protected updateForm(importMapping: IImportMapping): void {
    this.importMapping = importMapping;
    this.importMappingFormService.resetForm(this.editForm, importMapping);

    this.importRulesSharedCollection.set(
      this.importRuleService.addImportRuleToCollectionIfMissing<IImportRule>(this.importRulesSharedCollection(), importMapping.rule),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.importRuleService
      .query()
      .pipe(map((res: HttpResponse<IImportRule[]>) => res.body ?? []))
      .pipe(
        map((importRules: IImportRule[]) =>
          this.importRuleService.addImportRuleToCollectionIfMissing<IImportRule>(importRules, this.importMapping?.rule),
        ),
      )
      .subscribe((importRules: IImportRule[]) => this.importRulesSharedCollection.set(importRules));
  }
}
