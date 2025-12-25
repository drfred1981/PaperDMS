import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDocumentWatch, NewDocumentWatch } from '../document-watch.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDocumentWatch for edit and NewDocumentWatchFormGroupInput for create.
 */
type DocumentWatchFormGroupInput = IDocumentWatch | PartialWithRequiredKeyOf<NewDocumentWatch>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IDocumentWatch | NewDocumentWatch> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

type DocumentWatchFormRawValue = FormValueOf<IDocumentWatch>;

type NewDocumentWatchFormRawValue = FormValueOf<NewDocumentWatch>;

type DocumentWatchFormDefaults = Pick<
  NewDocumentWatch,
  'id' | 'notifyOnView' | 'notifyOnDownload' | 'notifyOnModify' | 'notifyOnShare' | 'notifyOnDelete' | 'createdDate'
>;

type DocumentWatchFormGroupContent = {
  id: FormControl<DocumentWatchFormRawValue['id'] | NewDocumentWatch['id']>;
  documentId: FormControl<DocumentWatchFormRawValue['documentId']>;
  userId: FormControl<DocumentWatchFormRawValue['userId']>;
  watchType: FormControl<DocumentWatchFormRawValue['watchType']>;
  notifyOnView: FormControl<DocumentWatchFormRawValue['notifyOnView']>;
  notifyOnDownload: FormControl<DocumentWatchFormRawValue['notifyOnDownload']>;
  notifyOnModify: FormControl<DocumentWatchFormRawValue['notifyOnModify']>;
  notifyOnShare: FormControl<DocumentWatchFormRawValue['notifyOnShare']>;
  notifyOnDelete: FormControl<DocumentWatchFormRawValue['notifyOnDelete']>;
  createdDate: FormControl<DocumentWatchFormRawValue['createdDate']>;
};

export type DocumentWatchFormGroup = FormGroup<DocumentWatchFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DocumentWatchFormService {
  createDocumentWatchFormGroup(documentWatch: DocumentWatchFormGroupInput = { id: null }): DocumentWatchFormGroup {
    const documentWatchRawValue = this.convertDocumentWatchToDocumentWatchRawValue({
      ...this.getFormDefaults(),
      ...documentWatch,
    });
    return new FormGroup<DocumentWatchFormGroupContent>({
      id: new FormControl(
        { value: documentWatchRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      documentId: new FormControl(documentWatchRawValue.documentId, {
        validators: [Validators.required],
      }),
      userId: new FormControl(documentWatchRawValue.userId, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      watchType: new FormControl(documentWatchRawValue.watchType, {
        validators: [Validators.required],
      }),
      notifyOnView: new FormControl(documentWatchRawValue.notifyOnView, {
        validators: [Validators.required],
      }),
      notifyOnDownload: new FormControl(documentWatchRawValue.notifyOnDownload, {
        validators: [Validators.required],
      }),
      notifyOnModify: new FormControl(documentWatchRawValue.notifyOnModify, {
        validators: [Validators.required],
      }),
      notifyOnShare: new FormControl(documentWatchRawValue.notifyOnShare, {
        validators: [Validators.required],
      }),
      notifyOnDelete: new FormControl(documentWatchRawValue.notifyOnDelete, {
        validators: [Validators.required],
      }),
      createdDate: new FormControl(documentWatchRawValue.createdDate, {
        validators: [Validators.required],
      }),
    });
  }

  getDocumentWatch(form: DocumentWatchFormGroup): IDocumentWatch | NewDocumentWatch {
    return this.convertDocumentWatchRawValueToDocumentWatch(form.getRawValue() as DocumentWatchFormRawValue | NewDocumentWatchFormRawValue);
  }

  resetForm(form: DocumentWatchFormGroup, documentWatch: DocumentWatchFormGroupInput): void {
    const documentWatchRawValue = this.convertDocumentWatchToDocumentWatchRawValue({ ...this.getFormDefaults(), ...documentWatch });
    form.reset(
      {
        ...documentWatchRawValue,
        id: { value: documentWatchRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DocumentWatchFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      notifyOnView: false,
      notifyOnDownload: false,
      notifyOnModify: false,
      notifyOnShare: false,
      notifyOnDelete: false,
      createdDate: currentTime,
    };
  }

  private convertDocumentWatchRawValueToDocumentWatch(
    rawDocumentWatch: DocumentWatchFormRawValue | NewDocumentWatchFormRawValue,
  ): IDocumentWatch | NewDocumentWatch {
    return {
      ...rawDocumentWatch,
      createdDate: dayjs(rawDocumentWatch.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertDocumentWatchToDocumentWatchRawValue(
    documentWatch: IDocumentWatch | (Partial<NewDocumentWatch> & DocumentWatchFormDefaults),
  ): DocumentWatchFormRawValue | PartialWithRequiredKeyOf<NewDocumentWatchFormRawValue> {
    return {
      ...documentWatch,
      createdDate: documentWatch.createdDate ? documentWatch.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
