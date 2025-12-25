import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../bank-statement.test-samples';

import { BankStatementFormService } from './bank-statement-form.service';

describe('BankStatement Form Service', () => {
  let service: BankStatementFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BankStatementFormService);
  });

  describe('Service methods', () => {
    describe('createBankStatementFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createBankStatementFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentId: expect.any(Object),
            accountNumber: expect.any(Object),
            bankName: expect.any(Object),
            statementDate: expect.any(Object),
            statementPeriodStart: expect.any(Object),
            statementPeriodEnd: expect.any(Object),
            openingBalance: expect.any(Object),
            closingBalance: expect.any(Object),
            currency: expect.any(Object),
            status: expect.any(Object),
            isReconciled: expect.any(Object),
            createdDate: expect.any(Object),
          }),
        );
      });

      it('passing IBankStatement should create a new form with FormGroup', () => {
        const formGroup = service.createBankStatementFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentId: expect.any(Object),
            accountNumber: expect.any(Object),
            bankName: expect.any(Object),
            statementDate: expect.any(Object),
            statementPeriodStart: expect.any(Object),
            statementPeriodEnd: expect.any(Object),
            openingBalance: expect.any(Object),
            closingBalance: expect.any(Object),
            currency: expect.any(Object),
            status: expect.any(Object),
            isReconciled: expect.any(Object),
            createdDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getBankStatement', () => {
      it('should return NewBankStatement for default BankStatement initial value', () => {
        const formGroup = service.createBankStatementFormGroup(sampleWithNewData);

        const bankStatement = service.getBankStatement(formGroup) as any;

        expect(bankStatement).toMatchObject(sampleWithNewData);
      });

      it('should return NewBankStatement for empty BankStatement initial value', () => {
        const formGroup = service.createBankStatementFormGroup();

        const bankStatement = service.getBankStatement(formGroup) as any;

        expect(bankStatement).toMatchObject({});
      });

      it('should return IBankStatement', () => {
        const formGroup = service.createBankStatementFormGroup(sampleWithRequiredData);

        const bankStatement = service.getBankStatement(formGroup) as any;

        expect(bankStatement).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IBankStatement should not enable id FormControl', () => {
        const formGroup = service.createBankStatementFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewBankStatement should disable id FormControl', () => {
        const formGroup = service.createBankStatementFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
