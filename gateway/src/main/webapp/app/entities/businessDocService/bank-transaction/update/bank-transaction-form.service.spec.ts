import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../bank-transaction.test-samples';

import { BankTransactionFormService } from './bank-transaction-form.service';

describe('BankTransaction Form Service', () => {
  let service: BankTransactionFormService;

  beforeEach(() => {
    service = TestBed.inject(BankTransactionFormService);
  });

  describe('Service methods', () => {
    describe('createBankTransactionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createBankTransactionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            statementId: expect.any(Object),
            transactionDate: expect.any(Object),
            description: expect.any(Object),
            debitAmount: expect.any(Object),
            creditAmount: expect.any(Object),
            balance: expect.any(Object),
            isReconciled: expect.any(Object),
            statement: expect.any(Object),
          }),
        );
      });

      it('passing IBankTransaction should create a new form with FormGroup', () => {
        const formGroup = service.createBankTransactionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            statementId: expect.any(Object),
            transactionDate: expect.any(Object),
            description: expect.any(Object),
            debitAmount: expect.any(Object),
            creditAmount: expect.any(Object),
            balance: expect.any(Object),
            isReconciled: expect.any(Object),
            statement: expect.any(Object),
          }),
        );
      });
    });

    describe('getBankTransaction', () => {
      it('should return NewBankTransaction for default BankTransaction initial value', () => {
        const formGroup = service.createBankTransactionFormGroup(sampleWithNewData);

        const bankTransaction = service.getBankTransaction(formGroup);

        expect(bankTransaction).toMatchObject(sampleWithNewData);
      });

      it('should return NewBankTransaction for empty BankTransaction initial value', () => {
        const formGroup = service.createBankTransactionFormGroup();

        const bankTransaction = service.getBankTransaction(formGroup);

        expect(bankTransaction).toMatchObject({});
      });

      it('should return IBankTransaction', () => {
        const formGroup = service.createBankTransactionFormGroup(sampleWithRequiredData);

        const bankTransaction = service.getBankTransaction(formGroup);

        expect(bankTransaction).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IBankTransaction should not enable id FormControl', () => {
        const formGroup = service.createBankTransactionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewBankTransaction should disable id FormControl', () => {
        const formGroup = service.createBankTransactionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
