import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IDocumentProcess, NewDocumentProcess } from '../document-process.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDocumentProcess for edit and NewDocumentProcessFormGroupInput for create.
 */
type DocumentProcessFormGroupInput = IDocumentProcess | PartialWithRequiredKeyOf<NewDocumentProcess>;

type DocumentProcessFormDefaults = Pick<NewDocumentProcess, 'id'>;

type DocumentProcessFormGroupContent = {
  id: FormControl<IDocumentProcess['id'] | NewDocumentProcess['id']>;
  status: FormControl<IDocumentProcess['status']>;
  documentId: FormControl<IDocumentProcess['documentId']>;
  documentSha256: FormControl<IDocumentProcess['documentSha256']>;
};

export type DocumentProcessFormGroup = FormGroup<DocumentProcessFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DocumentProcessFormService {
  createDocumentProcessFormGroup(documentProcess: DocumentProcessFormGroupInput = { id: null }): DocumentProcessFormGroup {
    const documentProcessRawValue = {
      ...this.getFormDefaults(),
      ...documentProcess,
    };
    return new FormGroup<DocumentProcessFormGroupContent>({
      id: new FormControl(
        { value: documentProcessRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      status: new FormControl(documentProcessRawValue.status),
      documentId: new FormControl(documentProcessRawValue.documentId, {
        validators: [Validators.required],
      }),
      documentSha256: new FormControl(documentProcessRawValue.documentSha256, {
        validators: [Validators.required, Validators.maxLength(64)],
      }),
    });
  }

  getDocumentProcess(form: DocumentProcessFormGroup): IDocumentProcess | NewDocumentProcess {
    return form.getRawValue() as IDocumentProcess | NewDocumentProcess;
  }

  resetForm(form: DocumentProcessFormGroup, documentProcess: DocumentProcessFormGroupInput): void {
    const documentProcessRawValue = { ...this.getFormDefaults(), ...documentProcess };
    form.reset(
      {
        ...documentProcessRawValue,
        id: { value: documentProcessRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DocumentProcessFormDefaults {
    return {
      id: null,
    };
  }
}
