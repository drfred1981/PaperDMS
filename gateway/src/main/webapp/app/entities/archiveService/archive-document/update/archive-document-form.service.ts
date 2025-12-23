import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IArchiveDocument, NewArchiveDocument } from '../archive-document.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IArchiveDocument for edit and NewArchiveDocumentFormGroupInput for create.
 */
type ArchiveDocumentFormGroupInput = IArchiveDocument | PartialWithRequiredKeyOf<NewArchiveDocument>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IArchiveDocument | NewArchiveDocument> = Omit<T, 'addedDate'> & {
  addedDate?: string | null;
};

type ArchiveDocumentFormRawValue = FormValueOf<IArchiveDocument>;

type NewArchiveDocumentFormRawValue = FormValueOf<NewArchiveDocument>;

type ArchiveDocumentFormDefaults = Pick<NewArchiveDocument, 'id' | 'addedDate'>;

type ArchiveDocumentFormGroupContent = {
  id: FormControl<ArchiveDocumentFormRawValue['id'] | NewArchiveDocument['id']>;
  archiveJobId: FormControl<ArchiveDocumentFormRawValue['archiveJobId']>;
  documentId: FormControl<ArchiveDocumentFormRawValue['documentId']>;
  documentSha256: FormControl<ArchiveDocumentFormRawValue['documentSha256']>;
  originalPath: FormControl<ArchiveDocumentFormRawValue['originalPath']>;
  archivePath: FormControl<ArchiveDocumentFormRawValue['archivePath']>;
  fileSize: FormControl<ArchiveDocumentFormRawValue['fileSize']>;
  addedDate: FormControl<ArchiveDocumentFormRawValue['addedDate']>;
  archiveJob: FormControl<ArchiveDocumentFormRawValue['archiveJob']>;
};

export type ArchiveDocumentFormGroup = FormGroup<ArchiveDocumentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ArchiveDocumentFormService {
  createArchiveDocumentFormGroup(archiveDocument?: ArchiveDocumentFormGroupInput): ArchiveDocumentFormGroup {
    const archiveDocumentRawValue = this.convertArchiveDocumentToArchiveDocumentRawValue({
      ...this.getFormDefaults(),
      ...(archiveDocument ?? { id: null }),
    });
    return new FormGroup<ArchiveDocumentFormGroupContent>({
      id: new FormControl(
        { value: archiveDocumentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      archiveJobId: new FormControl(archiveDocumentRawValue.archiveJobId, {
        validators: [Validators.required],
      }),
      documentId: new FormControl(archiveDocumentRawValue.documentId, {
        validators: [Validators.required],
      }),
      documentSha256: new FormControl(archiveDocumentRawValue.documentSha256, {
        validators: [Validators.required, Validators.maxLength(64)],
      }),
      originalPath: new FormControl(archiveDocumentRawValue.originalPath, {
        validators: [Validators.maxLength(1000)],
      }),
      archivePath: new FormControl(archiveDocumentRawValue.archivePath, {
        validators: [Validators.maxLength(1000)],
      }),
      fileSize: new FormControl(archiveDocumentRawValue.fileSize),
      addedDate: new FormControl(archiveDocumentRawValue.addedDate, {
        validators: [Validators.required],
      }),
      archiveJob: new FormControl(archiveDocumentRawValue.archiveJob, {
        validators: [Validators.required],
      }),
    });
  }

  getArchiveDocument(form: ArchiveDocumentFormGroup): IArchiveDocument | NewArchiveDocument {
    return this.convertArchiveDocumentRawValueToArchiveDocument(
      form.getRawValue() as ArchiveDocumentFormRawValue | NewArchiveDocumentFormRawValue,
    );
  }

  resetForm(form: ArchiveDocumentFormGroup, archiveDocument: ArchiveDocumentFormGroupInput): void {
    const archiveDocumentRawValue = this.convertArchiveDocumentToArchiveDocumentRawValue({ ...this.getFormDefaults(), ...archiveDocument });
    form.reset({
      ...archiveDocumentRawValue,
      id: { value: archiveDocumentRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ArchiveDocumentFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      addedDate: currentTime,
    };
  }

  private convertArchiveDocumentRawValueToArchiveDocument(
    rawArchiveDocument: ArchiveDocumentFormRawValue | NewArchiveDocumentFormRawValue,
  ): IArchiveDocument | NewArchiveDocument {
    return {
      ...rawArchiveDocument,
      addedDate: dayjs(rawArchiveDocument.addedDate, DATE_TIME_FORMAT),
    };
  }

  private convertArchiveDocumentToArchiveDocumentRawValue(
    archiveDocument: IArchiveDocument | (Partial<NewArchiveDocument> & ArchiveDocumentFormDefaults),
  ): ArchiveDocumentFormRawValue | PartialWithRequiredKeyOf<NewArchiveDocumentFormRawValue> {
    return {
      ...archiveDocument,
      addedDate: archiveDocument.addedDate ? archiveDocument.addedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
