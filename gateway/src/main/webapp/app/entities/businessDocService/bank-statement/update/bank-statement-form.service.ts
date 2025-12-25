import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IBankStatement, NewBankStatement } from '../bank-statement.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBankStatement for edit and NewBankStatementFormGroupInput for create.
 */
type BankStatementFormGroupInput = IBankStatement | PartialWithRequiredKeyOf<NewBankStatement>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IBankStatement | NewBankStatement> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

type BankStatementFormRawValue = FormValueOf<IBankStatement>;

type NewBankStatementFormRawValue = FormValueOf<NewBankStatement>;

type BankStatementFormDefaults = Pick<NewBankStatement, 'id' | 'isReconciled' | 'createdDate'>;

type BankStatementFormGroupContent = {
  id: FormControl<BankStatementFormRawValue['id'] | NewBankStatement['id']>;
  documentId: FormControl<BankStatementFormRawValue['documentId']>;
  accountNumber: FormControl<BankStatementFormRawValue['accountNumber']>;
  bankName: FormControl<BankStatementFormRawValue['bankName']>;
  statementDate: FormControl<BankStatementFormRawValue['statementDate']>;
  statementPeriodStart: FormControl<BankStatementFormRawValue['statementPeriodStart']>;
  statementPeriodEnd: FormControl<BankStatementFormRawValue['statementPeriodEnd']>;
  openingBalance: FormControl<BankStatementFormRawValue['openingBalance']>;
  closingBalance: FormControl<BankStatementFormRawValue['closingBalance']>;
  currency: FormControl<BankStatementFormRawValue['currency']>;
  status: FormControl<BankStatementFormRawValue['status']>;
  isReconciled: FormControl<BankStatementFormRawValue['isReconciled']>;
  createdDate: FormControl<BankStatementFormRawValue['createdDate']>;
};

export type BankStatementFormGroup = FormGroup<BankStatementFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BankStatementFormService {
  createBankStatementFormGroup(bankStatement: BankStatementFormGroupInput = { id: null }): BankStatementFormGroup {
    const bankStatementRawValue = this.convertBankStatementToBankStatementRawValue({
      ...this.getFormDefaults(),
      ...bankStatement,
    });
    return new FormGroup<BankStatementFormGroupContent>({
      id: new FormControl(
        { value: bankStatementRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      documentId: new FormControl(bankStatementRawValue.documentId, {
        validators: [Validators.required],
      }),
      accountNumber: new FormControl(bankStatementRawValue.accountNumber, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      bankName: new FormControl(bankStatementRawValue.bankName, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      statementDate: new FormControl(bankStatementRawValue.statementDate, {
        validators: [Validators.required],
      }),
      statementPeriodStart: new FormControl(bankStatementRawValue.statementPeriodStart, {
        validators: [Validators.required],
      }),
      statementPeriodEnd: new FormControl(bankStatementRawValue.statementPeriodEnd, {
        validators: [Validators.required],
      }),
      openingBalance: new FormControl(bankStatementRawValue.openingBalance, {
        validators: [Validators.required],
      }),
      closingBalance: new FormControl(bankStatementRawValue.closingBalance, {
        validators: [Validators.required],
      }),
      currency: new FormControl(bankStatementRawValue.currency, {
        validators: [Validators.required, Validators.maxLength(3)],
      }),
      status: new FormControl(bankStatementRawValue.status, {
        validators: [Validators.required],
      }),
      isReconciled: new FormControl(bankStatementRawValue.isReconciled, {
        validators: [Validators.required],
      }),
      createdDate: new FormControl(bankStatementRawValue.createdDate, {
        validators: [Validators.required],
      }),
    });
  }

  getBankStatement(form: BankStatementFormGroup): IBankStatement | NewBankStatement {
    return this.convertBankStatementRawValueToBankStatement(form.getRawValue() as BankStatementFormRawValue | NewBankStatementFormRawValue);
  }

  resetForm(form: BankStatementFormGroup, bankStatement: BankStatementFormGroupInput): void {
    const bankStatementRawValue = this.convertBankStatementToBankStatementRawValue({ ...this.getFormDefaults(), ...bankStatement });
    form.reset(
      {
        ...bankStatementRawValue,
        id: { value: bankStatementRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): BankStatementFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isReconciled: false,
      createdDate: currentTime,
    };
  }

  private convertBankStatementRawValueToBankStatement(
    rawBankStatement: BankStatementFormRawValue | NewBankStatementFormRawValue,
  ): IBankStatement | NewBankStatement {
    return {
      ...rawBankStatement,
      createdDate: dayjs(rawBankStatement.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertBankStatementToBankStatementRawValue(
    bankStatement: IBankStatement | (Partial<NewBankStatement> & BankStatementFormDefaults),
  ): BankStatementFormRawValue | PartialWithRequiredKeyOf<NewBankStatementFormRawValue> {
    return {
      ...bankStatement,
      createdDate: bankStatement.createdDate ? bankStatement.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
