import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IScannedPage, NewScannedPage } from '../scanned-page.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IScannedPage for edit and NewScannedPageFormGroupInput for create.
 */
type ScannedPageFormGroupInput = IScannedPage | PartialWithRequiredKeyOf<NewScannedPage>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IScannedPage | NewScannedPage> = Omit<T, 'scannedDate'> & {
  scannedDate?: string | null;
};

type ScannedPageFormRawValue = FormValueOf<IScannedPage>;

type NewScannedPageFormRawValue = FormValueOf<NewScannedPage>;

type ScannedPageFormDefaults = Pick<NewScannedPage, 'id' | 'scannedDate'>;

type ScannedPageFormGroupContent = {
  id: FormControl<ScannedPageFormRawValue['id'] | NewScannedPage['id']>;
  scanJobId: FormControl<ScannedPageFormRawValue['scanJobId']>;
  pageNumber: FormControl<ScannedPageFormRawValue['pageNumber']>;
  sha256: FormControl<ScannedPageFormRawValue['sha256']>;
  s3Key: FormControl<ScannedPageFormRawValue['s3Key']>;
  s3PreviewKey: FormControl<ScannedPageFormRawValue['s3PreviewKey']>;
  fileSize: FormControl<ScannedPageFormRawValue['fileSize']>;
  width: FormControl<ScannedPageFormRawValue['width']>;
  height: FormControl<ScannedPageFormRawValue['height']>;
  dpi: FormControl<ScannedPageFormRawValue['dpi']>;
  documentId: FormControl<ScannedPageFormRawValue['documentId']>;
  scannedDate: FormControl<ScannedPageFormRawValue['scannedDate']>;
  scanJob: FormControl<ScannedPageFormRawValue['scanJob']>;
};

export type ScannedPageFormGroup = FormGroup<ScannedPageFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ScannedPageFormService {
  createScannedPageFormGroup(scannedPage: ScannedPageFormGroupInput = { id: null }): ScannedPageFormGroup {
    const scannedPageRawValue = this.convertScannedPageToScannedPageRawValue({
      ...this.getFormDefaults(),
      ...scannedPage,
    });
    return new FormGroup<ScannedPageFormGroupContent>({
      id: new FormControl(
        { value: scannedPageRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      scanJobId: new FormControl(scannedPageRawValue.scanJobId, {
        validators: [Validators.required],
      }),
      pageNumber: new FormControl(scannedPageRawValue.pageNumber, {
        validators: [Validators.required],
      }),
      sha256: new FormControl(scannedPageRawValue.sha256, {
        validators: [Validators.required, Validators.maxLength(64)],
      }),
      s3Key: new FormControl(scannedPageRawValue.s3Key, {
        validators: [Validators.required, Validators.maxLength(1000)],
      }),
      s3PreviewKey: new FormControl(scannedPageRawValue.s3PreviewKey, {
        validators: [Validators.maxLength(1000)],
      }),
      fileSize: new FormControl(scannedPageRawValue.fileSize),
      width: new FormControl(scannedPageRawValue.width),
      height: new FormControl(scannedPageRawValue.height),
      dpi: new FormControl(scannedPageRawValue.dpi),
      documentId: new FormControl(scannedPageRawValue.documentId),
      scannedDate: new FormControl(scannedPageRawValue.scannedDate, {
        validators: [Validators.required],
      }),
      scanJob: new FormControl(scannedPageRawValue.scanJob, {
        validators: [Validators.required],
      }),
    });
  }

  getScannedPage(form: ScannedPageFormGroup): IScannedPage | NewScannedPage {
    return this.convertScannedPageRawValueToScannedPage(form.getRawValue() as ScannedPageFormRawValue | NewScannedPageFormRawValue);
  }

  resetForm(form: ScannedPageFormGroup, scannedPage: ScannedPageFormGroupInput): void {
    const scannedPageRawValue = this.convertScannedPageToScannedPageRawValue({ ...this.getFormDefaults(), ...scannedPage });
    form.reset(
      {
        ...scannedPageRawValue,
        id: { value: scannedPageRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ScannedPageFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      scannedDate: currentTime,
    };
  }

  private convertScannedPageRawValueToScannedPage(
    rawScannedPage: ScannedPageFormRawValue | NewScannedPageFormRawValue,
  ): IScannedPage | NewScannedPage {
    return {
      ...rawScannedPage,
      scannedDate: dayjs(rawScannedPage.scannedDate, DATE_TIME_FORMAT),
    };
  }

  private convertScannedPageToScannedPageRawValue(
    scannedPage: IScannedPage | (Partial<NewScannedPage> & ScannedPageFormDefaults),
  ): ScannedPageFormRawValue | PartialWithRequiredKeyOf<NewScannedPageFormRawValue> {
    return {
      ...scannedPage,
      scannedDate: scannedPage.scannedDate ? scannedPage.scannedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
