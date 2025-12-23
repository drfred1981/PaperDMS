import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IBankStatement } from '../bank-statement.model';
import { BankStatementService } from '../service/bank-statement.service';

import { BankStatementFormService } from './bank-statement-form.service';
import { BankStatementUpdate } from './bank-statement-update';

describe('BankStatement Management Update Component', () => {
  let comp: BankStatementUpdate;
  let fixture: ComponentFixture<BankStatementUpdate>;
  let activatedRoute: ActivatedRoute;
  let bankStatementFormService: BankStatementFormService;
  let bankStatementService: BankStatementService;

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

    fixture = TestBed.createComponent(BankStatementUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    bankStatementFormService = TestBed.inject(BankStatementFormService);
    bankStatementService = TestBed.inject(BankStatementService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const bankStatement: IBankStatement = { id: 28585 };

      activatedRoute.data = of({ bankStatement });
      comp.ngOnInit();

      expect(comp.bankStatement).toEqual(bankStatement);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBankStatement>>();
      const bankStatement = { id: 32402 };
      jest.spyOn(bankStatementFormService, 'getBankStatement').mockReturnValue(bankStatement);
      jest.spyOn(bankStatementService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bankStatement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bankStatement }));
      saveSubject.complete();

      // THEN
      expect(bankStatementFormService.getBankStatement).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(bankStatementService.update).toHaveBeenCalledWith(expect.objectContaining(bankStatement));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBankStatement>>();
      const bankStatement = { id: 32402 };
      jest.spyOn(bankStatementFormService, 'getBankStatement').mockReturnValue({ id: null });
      jest.spyOn(bankStatementService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bankStatement: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bankStatement }));
      saveSubject.complete();

      // THEN
      expect(bankStatementFormService.getBankStatement).toHaveBeenCalled();
      expect(bankStatementService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBankStatement>>();
      const bankStatement = { id: 32402 };
      jest.spyOn(bankStatementService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bankStatement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(bankStatementService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
