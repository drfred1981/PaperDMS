import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IContractClause, NewContractClause } from '../contract-clause.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IContractClause for edit and NewContractClauseFormGroupInput for create.
 */
type ContractClauseFormGroupInput = IContractClause | PartialWithRequiredKeyOf<NewContractClause>;

type ContractClauseFormDefaults = Pick<NewContractClause, 'id' | 'isMandatory'>;

type ContractClauseFormGroupContent = {
  id: FormControl<IContractClause['id'] | NewContractClause['id']>;
  contractId: FormControl<IContractClause['contractId']>;
  clauseNumber: FormControl<IContractClause['clauseNumber']>;
  title: FormControl<IContractClause['title']>;
  content: FormControl<IContractClause['content']>;
  clauseType: FormControl<IContractClause['clauseType']>;
  isMandatory: FormControl<IContractClause['isMandatory']>;
  contract: FormControl<IContractClause['contract']>;
};

export type ContractClauseFormGroup = FormGroup<ContractClauseFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ContractClauseFormService {
  createContractClauseFormGroup(contractClause: ContractClauseFormGroupInput = { id: null }): ContractClauseFormGroup {
    const contractClauseRawValue = {
      ...this.getFormDefaults(),
      ...contractClause,
    };
    return new FormGroup<ContractClauseFormGroupContent>({
      id: new FormControl(
        { value: contractClauseRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      contractId: new FormControl(contractClauseRawValue.contractId, {
        validators: [Validators.required],
      }),
      clauseNumber: new FormControl(contractClauseRawValue.clauseNumber, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      title: new FormControl(contractClauseRawValue.title, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      content: new FormControl(contractClauseRawValue.content, {
        validators: [Validators.required],
      }),
      clauseType: new FormControl(contractClauseRawValue.clauseType),
      isMandatory: new FormControl(contractClauseRawValue.isMandatory, {
        validators: [Validators.required],
      }),
      contract: new FormControl(contractClauseRawValue.contract, {
        validators: [Validators.required],
      }),
    });
  }

  getContractClause(form: ContractClauseFormGroup): IContractClause | NewContractClause {
    return form.getRawValue() as IContractClause | NewContractClause;
  }

  resetForm(form: ContractClauseFormGroup, contractClause: ContractClauseFormGroupInput): void {
    const contractClauseRawValue = { ...this.getFormDefaults(), ...contractClause };
    form.reset(
      {
        ...contractClauseRawValue,
        id: { value: contractClauseRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ContractClauseFormDefaults {
    return {
      id: null,
      isMandatory: false,
    };
  }
}
