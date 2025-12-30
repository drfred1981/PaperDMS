import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDocumentComment, NewDocumentComment } from '../document-comment.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDocumentComment for edit and NewDocumentCommentFormGroupInput for create.
 */
type DocumentCommentFormGroupInput = IDocumentComment | PartialWithRequiredKeyOf<NewDocumentComment>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IDocumentComment | NewDocumentComment> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

type DocumentCommentFormRawValue = FormValueOf<IDocumentComment>;

type NewDocumentCommentFormRawValue = FormValueOf<NewDocumentComment>;

type DocumentCommentFormDefaults = Pick<NewDocumentComment, 'id' | 'isResolved' | 'createdDate'>;

type DocumentCommentFormGroupContent = {
  id: FormControl<DocumentCommentFormRawValue['id'] | NewDocumentComment['id']>;
  content: FormControl<DocumentCommentFormRawValue['content']>;
  pageNumber: FormControl<DocumentCommentFormRawValue['pageNumber']>;
  isResolved: FormControl<DocumentCommentFormRawValue['isResolved']>;
  authorId: FormControl<DocumentCommentFormRawValue['authorId']>;
  createdDate: FormControl<DocumentCommentFormRawValue['createdDate']>;
  document: FormControl<DocumentCommentFormRawValue['document']>;
  parentComment: FormControl<DocumentCommentFormRawValue['parentComment']>;
};

export type DocumentCommentFormGroup = FormGroup<DocumentCommentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DocumentCommentFormService {
  createDocumentCommentFormGroup(documentComment: DocumentCommentFormGroupInput = { id: null }): DocumentCommentFormGroup {
    const documentCommentRawValue = this.convertDocumentCommentToDocumentCommentRawValue({
      ...this.getFormDefaults(),
      ...documentComment,
    });
    return new FormGroup<DocumentCommentFormGroupContent>({
      id: new FormControl(
        { value: documentCommentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      content: new FormControl(documentCommentRawValue.content, {
        validators: [Validators.required],
      }),
      pageNumber: new FormControl(documentCommentRawValue.pageNumber),
      isResolved: new FormControl(documentCommentRawValue.isResolved, {
        validators: [Validators.required],
      }),
      authorId: new FormControl(documentCommentRawValue.authorId, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      createdDate: new FormControl(documentCommentRawValue.createdDate, {
        validators: [Validators.required],
      }),
      document: new FormControl(documentCommentRawValue.document),
      parentComment: new FormControl(documentCommentRawValue.parentComment),
    });
  }

  getDocumentComment(form: DocumentCommentFormGroup): IDocumentComment | NewDocumentComment {
    return this.convertDocumentCommentRawValueToDocumentComment(
      form.getRawValue() as DocumentCommentFormRawValue | NewDocumentCommentFormRawValue,
    );
  }

  resetForm(form: DocumentCommentFormGroup, documentComment: DocumentCommentFormGroupInput): void {
    const documentCommentRawValue = this.convertDocumentCommentToDocumentCommentRawValue({ ...this.getFormDefaults(), ...documentComment });
    form.reset(
      {
        ...documentCommentRawValue,
        id: { value: documentCommentRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DocumentCommentFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isResolved: false,
      createdDate: currentTime,
    };
  }

  private convertDocumentCommentRawValueToDocumentComment(
    rawDocumentComment: DocumentCommentFormRawValue | NewDocumentCommentFormRawValue,
  ): IDocumentComment | NewDocumentComment {
    return {
      ...rawDocumentComment,
      createdDate: dayjs(rawDocumentComment.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertDocumentCommentToDocumentCommentRawValue(
    documentComment: IDocumentComment | (Partial<NewDocumentComment> & DocumentCommentFormDefaults),
  ): DocumentCommentFormRawValue | PartialWithRequiredKeyOf<NewDocumentCommentFormRawValue> {
    return {
      ...documentComment,
      createdDate: documentComment.createdDate ? documentComment.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
