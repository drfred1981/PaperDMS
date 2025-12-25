import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IBankStatement } from 'app/entities/businessDocService/bank-statement/bank-statement.model';
import { BankStatementService } from 'app/entities/businessDocService/bank-statement/service/bank-statement.service';
import { BankTransactionService } from '../service/bank-transaction.service';
import { IBankTransaction } from '../bank-transaction.model';
import { BankTransactionFormService } from './bank-transaction-form.service';

import { BankTransactionUpdateComponent } from './bank-transaction-update.component';

describe('BankTransaction Management Update Component', () => {
  let comp: BankTransactionUpdateComponent;
  let fixture: ComponentFixture<BankTransactionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let bankTransactionFormService: BankTransactionFormService;
  let bankTransactionService: BankTransactionService;
  let bankStatementService: BankStatementService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [BankTransactionUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(BankTransactionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BankTransactionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    bankTransactionFormService = TestBed.inject(BankTransactionFormService);
    bankTransactionService = TestBed.inject(BankTransactionService);
    bankStatementService = TestBed.inject(BankStatementService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call BankStatement query and add missing value', () => {
      const bankTransaction: IBankTransaction = { id: 28890 };
      const statement: IBankStatement = { id: 32402 };
      bankTransaction.statement = statement;

      const bankStatementCollection: IBankStatement[] = [{ id: 32402 }];
      jest.spyOn(bankStatementService, 'query').mockReturnValue(of(new HttpResponse({ body: bankStatementCollection })));
      const additionalBankStatements = [statement];
      const expectedCollection: IBankStatement[] = [...additionalBankStatements, ...bankStatementCollection];
      jest.spyOn(bankStatementService, 'addBankStatementToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ bankTransaction });
      comp.ngOnInit();

      expect(bankStatementService.query).toHaveBeenCalled();
      expect(bankStatementService.addBankStatementToCollectionIfMissing).toHaveBeenCalledWith(
        bankStatementCollection,
        ...additionalBankStatements.map(expect.objectContaining),
      );
      expect(comp.bankStatementsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const bankTransaction: IBankTransaction = { id: 28890 };
      const statement: IBankStatement = { id: 32402 };
      bankTransaction.statement = statement;

      activatedRoute.data = of({ bankTransaction });
      comp.ngOnInit();

      expect(comp.bankStatementsSharedCollection).toContainEqual(statement);
      expect(comp.bankTransaction).toEqual(bankTransaction);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBankTransaction>>();
      const bankTransaction = { id: 28516 };
      jest.spyOn(bankTransactionFormService, 'getBankTransaction').mockReturnValue(bankTransaction);
      jest.spyOn(bankTransactionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bankTransaction });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bankTransaction }));
      saveSubject.complete();

      // THEN
      expect(bankTransactionFormService.getBankTransaction).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(bankTransactionService.update).toHaveBeenCalledWith(expect.objectContaining(bankTransaction));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBankTransaction>>();
      const bankTransaction = { id: 28516 };
      jest.spyOn(bankTransactionFormService, 'getBankTransaction').mockReturnValue({ id: null });
      jest.spyOn(bankTransactionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bankTransaction: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bankTransaction }));
      saveSubject.complete();

      // THEN
      expect(bankTransactionFormService.getBankTransaction).toHaveBeenCalled();
      expect(bankTransactionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBankTransaction>>();
      const bankTransaction = { id: 28516 };
      jest.spyOn(bankTransactionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bankTransaction });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(bankTransactionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareBankStatement', () => {
      it('should forward to bankStatementService', () => {
        const entity = { id: 32402 };
        const entity2 = { id: 28585 };
        jest.spyOn(bankStatementService, 'compareBankStatement');
        comp.compareBankStatement(entity, entity2);
        expect(bankStatementService.compareBankStatement).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
