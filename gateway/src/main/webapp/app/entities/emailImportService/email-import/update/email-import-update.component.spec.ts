import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IImportRule } from 'app/entities/emailImportService/import-rule/import-rule.model';
import { ImportRuleService } from 'app/entities/emailImportService/import-rule/service/import-rule.service';
import { EmailImportService } from '../service/email-import.service';
import { IEmailImport } from '../email-import.model';
import { EmailImportFormService } from './email-import-form.service';

import { EmailImportUpdateComponent } from './email-import-update.component';

describe('EmailImport Management Update Component', () => {
  let comp: EmailImportUpdateComponent;
  let fixture: ComponentFixture<EmailImportUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let emailImportFormService: EmailImportFormService;
  let emailImportService: EmailImportService;
  let importRuleService: ImportRuleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [EmailImportUpdateComponent],
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
      .overrideTemplate(EmailImportUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EmailImportUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    emailImportFormService = TestBed.inject(EmailImportFormService);
    emailImportService = TestBed.inject(EmailImportService);
    importRuleService = TestBed.inject(ImportRuleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call ImportRule query and add missing value', () => {
      const emailImport: IEmailImport = { id: 14958 };
      const appliedRule: IImportRule = { id: 3928 };
      emailImport.appliedRule = appliedRule;

      const importRuleCollection: IImportRule[] = [{ id: 3928 }];
      jest.spyOn(importRuleService, 'query').mockReturnValue(of(new HttpResponse({ body: importRuleCollection })));
      const additionalImportRules = [appliedRule];
      const expectedCollection: IImportRule[] = [...additionalImportRules, ...importRuleCollection];
      jest.spyOn(importRuleService, 'addImportRuleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ emailImport });
      comp.ngOnInit();

      expect(importRuleService.query).toHaveBeenCalled();
      expect(importRuleService.addImportRuleToCollectionIfMissing).toHaveBeenCalledWith(
        importRuleCollection,
        ...additionalImportRules.map(expect.objectContaining),
      );
      expect(comp.importRulesSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const emailImport: IEmailImport = { id: 14958 };
      const appliedRule: IImportRule = { id: 3928 };
      emailImport.appliedRule = appliedRule;

      activatedRoute.data = of({ emailImport });
      comp.ngOnInit();

      expect(comp.importRulesSharedCollection).toContainEqual(appliedRule);
      expect(comp.emailImport).toEqual(emailImport);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmailImport>>();
      const emailImport = { id: 3064 };
      jest.spyOn(emailImportFormService, 'getEmailImport').mockReturnValue(emailImport);
      jest.spyOn(emailImportService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ emailImport });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: emailImport }));
      saveSubject.complete();

      // THEN
      expect(emailImportFormService.getEmailImport).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(emailImportService.update).toHaveBeenCalledWith(expect.objectContaining(emailImport));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmailImport>>();
      const emailImport = { id: 3064 };
      jest.spyOn(emailImportFormService, 'getEmailImport').mockReturnValue({ id: null });
      jest.spyOn(emailImportService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ emailImport: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: emailImport }));
      saveSubject.complete();

      // THEN
      expect(emailImportFormService.getEmailImport).toHaveBeenCalled();
      expect(emailImportService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmailImport>>();
      const emailImport = { id: 3064 };
      jest.spyOn(emailImportService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ emailImport });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(emailImportService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareImportRule', () => {
      it('should forward to importRuleService', () => {
        const entity = { id: 3928 };
        const entity2 = { id: 22098 };
        jest.spyOn(importRuleService, 'compareImportRule');
        comp.compareImportRule(entity, entity2);
        expect(importRuleService.compareImportRule).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
