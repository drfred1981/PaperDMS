import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../contract-clause.test-samples';

import { ContractClauseFormService } from './contract-clause-form.service';

describe('ContractClause Form Service', () => {
  let service: ContractClauseFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ContractClauseFormService);
  });

  describe('Service methods', () => {
    describe('createContractClauseFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createContractClauseFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            contractId: expect.any(Object),
            clauseNumber: expect.any(Object),
            title: expect.any(Object),
            content: expect.any(Object),
            clauseType: expect.any(Object),
            isMandatory: expect.any(Object),
            contract: expect.any(Object),
          }),
        );
      });

      it('passing IContractClause should create a new form with FormGroup', () => {
        const formGroup = service.createContractClauseFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            contractId: expect.any(Object),
            clauseNumber: expect.any(Object),
            title: expect.any(Object),
            content: expect.any(Object),
            clauseType: expect.any(Object),
            isMandatory: expect.any(Object),
            contract: expect.any(Object),
          }),
        );
      });
    });

    describe('getContractClause', () => {
      it('should return NewContractClause for default ContractClause initial value', () => {
        const formGroup = service.createContractClauseFormGroup(sampleWithNewData);

        const contractClause = service.getContractClause(formGroup) as any;

        expect(contractClause).toMatchObject(sampleWithNewData);
      });

      it('should return NewContractClause for empty ContractClause initial value', () => {
        const formGroup = service.createContractClauseFormGroup();

        const contractClause = service.getContractClause(formGroup) as any;

        expect(contractClause).toMatchObject({});
      });

      it('should return IContractClause', () => {
        const formGroup = service.createContractClauseFormGroup(sampleWithRequiredData);

        const contractClause = service.getContractClause(formGroup) as any;

        expect(contractClause).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IContractClause should not enable id FormControl', () => {
        const formGroup = service.createContractClauseFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewContractClause should disable id FormControl', () => {
        const formGroup = service.createContractClauseFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
