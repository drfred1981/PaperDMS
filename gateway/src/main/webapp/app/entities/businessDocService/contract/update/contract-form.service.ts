import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IContract, NewContract } from '../contract.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IContract for edit and NewContractFormGroupInput for create.
 */
type ContractFormGroupInput = IContract | PartialWithRequiredKeyOf<NewContract>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IContract | NewContract> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

type ContractFormRawValue = FormValueOf<IContract>;

type NewContractFormRawValue = FormValueOf<NewContract>;

type ContractFormDefaults = Pick<NewContract, 'id' | 'autoRenew' | 'createdDate'>;

type ContractFormGroupContent = {
  id: FormControl<ContractFormRawValue['id'] | NewContract['id']>;
  documentId: FormControl<ContractFormRawValue['documentId']>;
  contractNumber: FormControl<ContractFormRawValue['contractNumber']>;
  contractType: FormControl<ContractFormRawValue['contractType']>;
  title: FormControl<ContractFormRawValue['title']>;
  partyA: FormControl<ContractFormRawValue['partyA']>;
  partyB: FormControl<ContractFormRawValue['partyB']>;
  startDate: FormControl<ContractFormRawValue['startDate']>;
  endDate: FormControl<ContractFormRawValue['endDate']>;
  autoRenew: FormControl<ContractFormRawValue['autoRenew']>;
  contractValue: FormControl<ContractFormRawValue['contractValue']>;
  currency: FormControl<ContractFormRawValue['currency']>;
  status: FormControl<ContractFormRawValue['status']>;
  createdDate: FormControl<ContractFormRawValue['createdDate']>;
};

export type ContractFormGroup = FormGroup<ContractFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ContractFormService {
  createContractFormGroup(contract?: ContractFormGroupInput): ContractFormGroup {
    const contractRawValue = this.convertContractToContractRawValue({
      ...this.getFormDefaults(),
      ...(contract ?? { id: null }),
    });
    return new FormGroup<ContractFormGroupContent>({
      id: new FormControl(
        { value: contractRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      documentId: new FormControl(contractRawValue.documentId, {
        validators: [Validators.required],
      }),
      contractNumber: new FormControl(contractRawValue.contractNumber, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      contractType: new FormControl(contractRawValue.contractType, {
        validators: [Validators.required],
      }),
      title: new FormControl(contractRawValue.title, {
        validators: [Validators.required, Validators.maxLength(500)],
      }),
      partyA: new FormControl(contractRawValue.partyA, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      partyB: new FormControl(contractRawValue.partyB, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      startDate: new FormControl(contractRawValue.startDate, {
        validators: [Validators.required],
      }),
      endDate: new FormControl(contractRawValue.endDate),
      autoRenew: new FormControl(contractRawValue.autoRenew, {
        validators: [Validators.required],
      }),
      contractValue: new FormControl(contractRawValue.contractValue),
      currency: new FormControl(contractRawValue.currency, {
        validators: [Validators.maxLength(3)],
      }),
      status: new FormControl(contractRawValue.status, {
        validators: [Validators.required],
      }),
      createdDate: new FormControl(contractRawValue.createdDate, {
        validators: [Validators.required],
      }),
    });
  }

  getContract(form: ContractFormGroup): IContract | NewContract {
    return this.convertContractRawValueToContract(form.getRawValue() as ContractFormRawValue | NewContractFormRawValue);
  }

  resetForm(form: ContractFormGroup, contract: ContractFormGroupInput): void {
    const contractRawValue = this.convertContractToContractRawValue({ ...this.getFormDefaults(), ...contract });
    form.reset({
      ...contractRawValue,
      id: { value: contractRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ContractFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      autoRenew: false,
      createdDate: currentTime,
    };
  }

  private convertContractRawValueToContract(rawContract: ContractFormRawValue | NewContractFormRawValue): IContract | NewContract {
    return {
      ...rawContract,
      createdDate: dayjs(rawContract.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertContractToContractRawValue(
    contract: IContract | (Partial<NewContract> & ContractFormDefaults),
  ): ContractFormRawValue | PartialWithRequiredKeyOf<NewContractFormRawValue> {
    return {
      ...contract,
      createdDate: contract.createdDate ? contract.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
