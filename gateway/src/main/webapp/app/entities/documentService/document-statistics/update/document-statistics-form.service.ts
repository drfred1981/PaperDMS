import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDocumentStatistics, NewDocumentStatistics } from '../document-statistics.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDocumentStatistics for edit and NewDocumentStatisticsFormGroupInput for create.
 */
type DocumentStatisticsFormGroupInput = IDocumentStatistics | PartialWithRequiredKeyOf<NewDocumentStatistics>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IDocumentStatistics | NewDocumentStatistics> = Omit<T, 'lastUpdated'> & {
  lastUpdated?: string | null;
};

type DocumentStatisticsFormRawValue = FormValueOf<IDocumentStatistics>;

type NewDocumentStatisticsFormRawValue = FormValueOf<NewDocumentStatistics>;

type DocumentStatisticsFormDefaults = Pick<NewDocumentStatistics, 'id' | 'lastUpdated'>;

type DocumentStatisticsFormGroupContent = {
  id: FormControl<DocumentStatisticsFormRawValue['id'] | NewDocumentStatistics['id']>;
  documentId: FormControl<DocumentStatisticsFormRawValue['documentId']>;
  viewsTotal: FormControl<DocumentStatisticsFormRawValue['viewsTotal']>;
  downloadsTotal: FormControl<DocumentStatisticsFormRawValue['downloadsTotal']>;
  uniqueViewers: FormControl<DocumentStatisticsFormRawValue['uniqueViewers']>;
  lastUpdated: FormControl<DocumentStatisticsFormRawValue['lastUpdated']>;
};

export type DocumentStatisticsFormGroup = FormGroup<DocumentStatisticsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DocumentStatisticsFormService {
  createDocumentStatisticsFormGroup(documentStatistics: DocumentStatisticsFormGroupInput = { id: null }): DocumentStatisticsFormGroup {
    const documentStatisticsRawValue = this.convertDocumentStatisticsToDocumentStatisticsRawValue({
      ...this.getFormDefaults(),
      ...documentStatistics,
    });
    return new FormGroup<DocumentStatisticsFormGroupContent>({
      id: new FormControl(
        { value: documentStatisticsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      documentId: new FormControl(documentStatisticsRawValue.documentId, {
        validators: [Validators.required],
      }),
      viewsTotal: new FormControl(documentStatisticsRawValue.viewsTotal),
      downloadsTotal: new FormControl(documentStatisticsRawValue.downloadsTotal),
      uniqueViewers: new FormControl(documentStatisticsRawValue.uniqueViewers),
      lastUpdated: new FormControl(documentStatisticsRawValue.lastUpdated, {
        validators: [Validators.required],
      }),
    });
  }

  getDocumentStatistics(form: DocumentStatisticsFormGroup): IDocumentStatistics | NewDocumentStatistics {
    return this.convertDocumentStatisticsRawValueToDocumentStatistics(
      form.getRawValue() as DocumentStatisticsFormRawValue | NewDocumentStatisticsFormRawValue,
    );
  }

  resetForm(form: DocumentStatisticsFormGroup, documentStatistics: DocumentStatisticsFormGroupInput): void {
    const documentStatisticsRawValue = this.convertDocumentStatisticsToDocumentStatisticsRawValue({
      ...this.getFormDefaults(),
      ...documentStatistics,
    });
    form.reset(
      {
        ...documentStatisticsRawValue,
        id: { value: documentStatisticsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DocumentStatisticsFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      lastUpdated: currentTime,
    };
  }

  private convertDocumentStatisticsRawValueToDocumentStatistics(
    rawDocumentStatistics: DocumentStatisticsFormRawValue | NewDocumentStatisticsFormRawValue,
  ): IDocumentStatistics | NewDocumentStatistics {
    return {
      ...rawDocumentStatistics,
      lastUpdated: dayjs(rawDocumentStatistics.lastUpdated, DATE_TIME_FORMAT),
    };
  }

  private convertDocumentStatisticsToDocumentStatisticsRawValue(
    documentStatistics: IDocumentStatistics | (Partial<NewDocumentStatistics> & DocumentStatisticsFormDefaults),
  ): DocumentStatisticsFormRawValue | PartialWithRequiredKeyOf<NewDocumentStatisticsFormRawValue> {
    return {
      ...documentStatistics,
      lastUpdated: documentStatistics.lastUpdated ? documentStatistics.lastUpdated.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
