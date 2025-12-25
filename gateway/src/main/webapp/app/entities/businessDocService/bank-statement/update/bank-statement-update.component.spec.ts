import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { BankStatementService } from '../service/bank-statement.service';
import { IBankStatement } from '../bank-statement.model';
import { BankStatementFormService } from './bank-statement-form.service';

import { BankStatementUpdateComponent } from './bank-statement-update.component';

describe('BankStatement Management Update Component', () => {
  let comp: BankStatementUpdateComponent;
  let fixture: ComponentFixture<BankStatementUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let bankStatementFormService: BankStatementFormService;
  let bankStatementService: BankStatementService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [BankStatementUpdateComponent],
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
      .overrideTemplate(BankStatementUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BankStatementUpdateComponent);
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
