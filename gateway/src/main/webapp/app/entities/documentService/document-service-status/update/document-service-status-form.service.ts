import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDocumentServiceStatus, NewDocumentServiceStatus } from '../document-service-status.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDocumentServiceStatus for edit and NewDocumentServiceStatusFormGroupInput for create.
 */
type DocumentServiceStatusFormGroupInput = IDocumentServiceStatus | PartialWithRequiredKeyOf<NewDocumentServiceStatus>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IDocumentServiceStatus | NewDocumentServiceStatus> = Omit<
  T,
  'lastProcessedDate' | 'processingStartDate' | 'processingEndDate' | 'updatedDate'
> & {
  lastProcessedDate?: string | null;
  processingStartDate?: string | null;
  processingEndDate?: string | null;
  updatedDate?: string | null;
};

type DocumentServiceStatusFormRawValue = FormValueOf<IDocumentServiceStatus>;

type NewDocumentServiceStatusFormRawValue = FormValueOf<NewDocumentServiceStatus>;

type DocumentServiceStatusFormDefaults = Pick<
  NewDocumentServiceStatus,
  'id' | 'lastProcessedDate' | 'processingStartDate' | 'processingEndDate' | 'updatedDate'
>;

type DocumentServiceStatusFormGroupContent = {
  id: FormControl<DocumentServiceStatusFormRawValue['id'] | NewDocumentServiceStatus['id']>;
  documentId: FormControl<DocumentServiceStatusFormRawValue['documentId']>;
  serviceType: FormControl<DocumentServiceStatusFormRawValue['serviceType']>;
  status: FormControl<DocumentServiceStatusFormRawValue['status']>;
  statusDetails: FormControl<DocumentServiceStatusFormRawValue['statusDetails']>;
  errorMessage: FormControl<DocumentServiceStatusFormRawValue['errorMessage']>;
  retryCount: FormControl<DocumentServiceStatusFormRawValue['retryCount']>;
  lastProcessedDate: FormControl<DocumentServiceStatusFormRawValue['lastProcessedDate']>;
  processingStartDate: FormControl<DocumentServiceStatusFormRawValue['processingStartDate']>;
  processingEndDate: FormControl<DocumentServiceStatusFormRawValue['processingEndDate']>;
  processingDuration: FormControl<DocumentServiceStatusFormRawValue['processingDuration']>;
  jobId: FormControl<DocumentServiceStatusFormRawValue['jobId']>;
  priority: FormControl<DocumentServiceStatusFormRawValue['priority']>;
  updatedBy: FormControl<DocumentServiceStatusFormRawValue['updatedBy']>;
  updatedDate: FormControl<DocumentServiceStatusFormRawValue['updatedDate']>;
};

export type DocumentServiceStatusFormGroup = FormGroup<DocumentServiceStatusFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DocumentServiceStatusFormService {
  createDocumentServiceStatusFormGroup(documentServiceStatus?: DocumentServiceStatusFormGroupInput): DocumentServiceStatusFormGroup {
    const documentServiceStatusRawValue = this.convertDocumentServiceStatusToDocumentServiceStatusRawValue({
      ...this.getFormDefaults(),
      ...(documentServiceStatus ?? { id: null }),
    });
    return new FormGroup<DocumentServiceStatusFormGroupContent>({
      id: new FormControl(
        { value: documentServiceStatusRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      documentId: new FormControl(documentServiceStatusRawValue.documentId, {
        validators: [Validators.required],
      }),
      serviceType: new FormControl(documentServiceStatusRawValue.serviceType, {
        validators: [Validators.required],
      }),
      status: new FormControl(documentServiceStatusRawValue.status, {
        validators: [Validators.required],
      }),
      statusDetails: new FormControl(documentServiceStatusRawValue.statusDetails),
      errorMessage: new FormControl(documentServiceStatusRawValue.errorMessage),
      retryCount: new FormControl(documentServiceStatusRawValue.retryCount),
      lastProcessedDate: new FormControl(documentServiceStatusRawValue.lastProcessedDate),
      processingStartDate: new FormControl(documentServiceStatusRawValue.processingStartDate),
      processingEndDate: new FormControl(documentServiceStatusRawValue.processingEndDate),
      processingDuration: new FormControl(documentServiceStatusRawValue.processingDuration),
      jobId: new FormControl(documentServiceStatusRawValue.jobId, {
        validators: [Validators.maxLength(100)],
      }),
      priority: new FormControl(documentServiceStatusRawValue.priority),
      updatedBy: new FormControl(documentServiceStatusRawValue.updatedBy, {
        validators: [Validators.maxLength(50)],
      }),
      updatedDate: new FormControl(documentServiceStatusRawValue.updatedDate, {
        validators: [Validators.required],
      }),
    });
  }

  getDocumentServiceStatus(form: DocumentServiceStatusFormGroup): IDocumentServiceStatus | NewDocumentServiceStatus {
    return this.convertDocumentServiceStatusRawValueToDocumentServiceStatus(
      form.getRawValue() as DocumentServiceStatusFormRawValue | NewDocumentServiceStatusFormRawValue,
    );
  }

  resetForm(form: DocumentServiceStatusFormGroup, documentServiceStatus: DocumentServiceStatusFormGroupInput): void {
    const documentServiceStatusRawValue = this.convertDocumentServiceStatusToDocumentServiceStatusRawValue({
      ...this.getFormDefaults(),
      ...documentServiceStatus,
    });
    form.reset({
      ...documentServiceStatusRawValue,
      id: { value: documentServiceStatusRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): DocumentServiceStatusFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      lastProcessedDate: currentTime,
      processingStartDate: currentTime,
      processingEndDate: currentTime,
      updatedDate: currentTime,
    };
  }

  private convertDocumentServiceStatusRawValueToDocumentServiceStatus(
    rawDocumentServiceStatus: DocumentServiceStatusFormRawValue | NewDocumentServiceStatusFormRawValue,
  ): IDocumentServiceStatus | NewDocumentServiceStatus {
    return {
      ...rawDocumentServiceStatus,
      lastProcessedDate: dayjs(rawDocumentServiceStatus.lastProcessedDate, DATE_TIME_FORMAT),
      processingStartDate: dayjs(rawDocumentServiceStatus.processingStartDate, DATE_TIME_FORMAT),
      processingEndDate: dayjs(rawDocumentServiceStatus.processingEndDate, DATE_TIME_FORMAT),
      updatedDate: dayjs(rawDocumentServiceStatus.updatedDate, DATE_TIME_FORMAT),
    };
  }

  private convertDocumentServiceStatusToDocumentServiceStatusRawValue(
    documentServiceStatus: IDocumentServiceStatus | (Partial<NewDocumentServiceStatus> & DocumentServiceStatusFormDefaults),
  ): DocumentServiceStatusFormRawValue | PartialWithRequiredKeyOf<NewDocumentServiceStatusFormRawValue> {
    return {
      ...documentServiceStatus,
      lastProcessedDate: documentServiceStatus.lastProcessedDate
        ? documentServiceStatus.lastProcessedDate.format(DATE_TIME_FORMAT)
        : undefined,
      processingStartDate: documentServiceStatus.processingStartDate
        ? documentServiceStatus.processingStartDate.format(DATE_TIME_FORMAT)
        : undefined,
      processingEndDate: documentServiceStatus.processingEndDate
        ? documentServiceStatus.processingEndDate.format(DATE_TIME_FORMAT)
        : undefined,
      updatedDate: documentServiceStatus.updatedDate ? documentServiceStatus.updatedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
