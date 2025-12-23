import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IContract } from 'app/entities/businessDocService/contract/contract.model';
import { ContractService } from 'app/entities/businessDocService/contract/service/contract.service';
import { IContractClause } from '../contract-clause.model';
import { ContractClauseService } from '../service/contract-clause.service';

import { ContractClauseFormService } from './contract-clause-form.service';
import { ContractClauseUpdate } from './contract-clause-update';

describe('ContractClause Management Update Component', () => {
  let comp: ContractClauseUpdate;
  let fixture: ComponentFixture<ContractClauseUpdate>;
  let activatedRoute: ActivatedRoute;
  let contractClauseFormService: ContractClauseFormService;
  let contractClauseService: ContractClauseService;
  let contractService: ContractService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TranslateModule.forRoot()],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    });

    fixture = TestBed.createComponent(ContractClauseUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    contractClauseFormService = TestBed.inject(ContractClauseFormService);
    contractClauseService = TestBed.inject(ContractClauseService);
    contractService = TestBed.inject(ContractService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Contract query and add missing value', () => {
      const contractClause: IContractClause = { id: 5317 };
      const contract: IContract = { id: 26216 };
      contractClause.contract = contract;

      const contractCollection: IContract[] = [{ id: 26216 }];
      jest.spyOn(contractService, 'query').mockReturnValue(of(new HttpResponse({ body: contractCollection })));
      const additionalContracts = [contract];
      const expectedCollection: IContract[] = [...additionalContracts, ...contractCollection];
      jest.spyOn(contractService, 'addContractToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ contractClause });
      comp.ngOnInit();

      expect(contractService.query).toHaveBeenCalled();
      expect(contractService.addContractToCollectionIfMissing).toHaveBeenCalledWith(
        contractCollection,
        ...additionalContracts.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.contractsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const contractClause: IContractClause = { id: 5317 };
      const contract: IContract = { id: 26216 };
      contractClause.contract = contract;

      activatedRoute.data = of({ contractClause });
      comp.ngOnInit();

      expect(comp.contractsSharedCollection()).toContainEqual(contract);
      expect(comp.contractClause).toEqual(contractClause);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContractClause>>();
      const contractClause = { id: 6360 };
      jest.spyOn(contractClauseFormService, 'getContractClause').mockReturnValue(contractClause);
      jest.spyOn(contractClauseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contractClause });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contractClause }));
      saveSubject.complete();

      // THEN
      expect(contractClauseFormService.getContractClause).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(contractClauseService.update).toHaveBeenCalledWith(expect.objectContaining(contractClause));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContractClause>>();
      const contractClause = { id: 6360 };
      jest.spyOn(contractClauseFormService, 'getContractClause').mockReturnValue({ id: null });
      jest.spyOn(contractClauseService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contractClause: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contractClause }));
      saveSubject.complete();

      // THEN
      expect(contractClauseFormService.getContractClause).toHaveBeenCalled();
      expect(contractClauseService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContractClause>>();
      const contractClause = { id: 6360 };
      jest.spyOn(contractClauseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contractClause });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(contractClauseService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareContract', () => {
      it('should forward to contractService', () => {
        const entity = { id: 26216 };
        const entity2 = { id: 14870 };
        jest.spyOn(contractService, 'compareContract');
        comp.compareContract(entity, entity2);
        expect(contractService.compareContract).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
