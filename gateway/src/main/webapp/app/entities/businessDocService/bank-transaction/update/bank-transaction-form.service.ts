import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IBankTransaction, NewBankTransaction } from '../bank-transaction.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBankTransaction for edit and NewBankTransactionFormGroupInput for create.
 */
type BankTransactionFormGroupInput = IBankTransaction | PartialWithRequiredKeyOf<NewBankTransaction>;

type BankTransactionFormDefaults = Pick<NewBankTransaction, 'id' | 'isReconciled'>;

type BankTransactionFormGroupContent = {
  id: FormControl<IBankTransaction['id'] | NewBankTransaction['id']>;
  statementId: FormControl<IBankTransaction['statementId']>;
  transactionDate: FormControl<IBankTransaction['transactionDate']>;
  description: FormControl<IBankTransaction['description']>;
  debitAmount: FormControl<IBankTransaction['debitAmount']>;
  creditAmount: FormControl<IBankTransaction['creditAmount']>;
  balance: FormControl<IBankTransaction['balance']>;
  isReconciled: FormControl<IBankTransaction['isReconciled']>;
  statement: FormControl<IBankTransaction['statement']>;
};

export type BankTransactionFormGroup = FormGroup<BankTransactionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BankTransactionFormService {
  createBankTransactionFormGroup(bankTransaction?: BankTransactionFormGroupInput): BankTransactionFormGroup {
    const bankTransactionRawValue = {
      ...this.getFormDefaults(),
      ...(bankTransaction ?? { id: null }),
    };
    return new FormGroup<BankTransactionFormGroupContent>({
      id: new FormControl(
        { value: bankTransactionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      statementId: new FormControl(bankTransactionRawValue.statementId, {
        validators: [Validators.required],
      }),
      transactionDate: new FormControl(bankTransactionRawValue.transactionDate, {
        validators: [Validators.required],
      }),
      description: new FormControl(bankTransactionRawValue.description, {
        validators: [Validators.required, Validators.maxLength(500)],
      }),
      debitAmount: new FormControl(bankTransactionRawValue.debitAmount),
      creditAmount: new FormControl(bankTransactionRawValue.creditAmount),
      balance: new FormControl(bankTransactionRawValue.balance, {
        validators: [Validators.required],
      }),
      isReconciled: new FormControl(bankTransactionRawValue.isReconciled, {
        validators: [Validators.required],
      }),
      statement: new FormControl(bankTransactionRawValue.statement, {
        validators: [Validators.required],
      }),
    });
  }

  getBankTransaction(form: BankTransactionFormGroup): IBankTransaction | NewBankTransaction {
    return form.getRawValue() as IBankTransaction | NewBankTransaction;
  }

  resetForm(form: BankTransactionFormGroup, bankTransaction: BankTransactionFormGroupInput): void {
    const bankTransactionRawValue = { ...this.getFormDefaults(), ...bankTransaction };
    form.reset({
      ...bankTransactionRawValue,
      id: { value: bankTransactionRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): BankTransactionFormDefaults {
    return {
      id: null,
      isReconciled: false,
    };
  }
}
