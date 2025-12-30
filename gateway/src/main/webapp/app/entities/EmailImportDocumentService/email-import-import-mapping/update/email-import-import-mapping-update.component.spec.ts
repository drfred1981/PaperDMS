import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IEmailImportImportRule } from 'app/entities/EmailImportDocumentService/email-import-import-rule/email-import-import-rule.model';
import { EmailImportImportRuleService } from 'app/entities/EmailImportDocumentService/email-import-import-rule/service/email-import-import-rule.service';
import { EmailImportImportMappingService } from '../service/email-import-import-mapping.service';
import { IEmailImportImportMapping } from '../email-import-import-mapping.model';
import { EmailImportImportMappingFormService } from './email-import-import-mapping-form.service';

import { EmailImportImportMappingUpdateComponent } from './email-import-import-mapping-update.component';

describe('EmailImportImportMapping Management Update Component', () => {
  let comp: EmailImportImportMappingUpdateComponent;
  let fixture: ComponentFixture<EmailImportImportMappingUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let emailImportImportMappingFormService: EmailImportImportMappingFormService;
  let emailImportImportMappingService: EmailImportImportMappingService;
  let emailImportImportRuleService: EmailImportImportRuleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [EmailImportImportMappingUpdateComponent],
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
      .overrideTemplate(EmailImportImportMappingUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EmailImportImportMappingUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    emailImportImportMappingFormService = TestBed.inject(EmailImportImportMappingFormService);
    emailImportImportMappingService = TestBed.inject(EmailImportImportMappingService);
    emailImportImportRuleService = TestBed.inject(EmailImportImportRuleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call EmailImportImportRule query and add missing value', () => {
      const emailImportImportMapping: IEmailImportImportMapping = { id: 30950 };
      const rule: IEmailImportImportRule = { id: 12705 };
      emailImportImportMapping.rule = rule;

      const emailImportImportRuleCollection: IEmailImportImportRule[] = [{ id: 12705 }];
      jest.spyOn(emailImportImportRuleService, 'query').mockReturnValue(of(new HttpResponse({ body: emailImportImportRuleCollection })));
      const additionalEmailImportImportRules = [rule];
      const expectedCollection: IEmailImportImportRule[] = [...additionalEmailImportImportRules, ...emailImportImportRuleCollection];
      jest.spyOn(emailImportImportRuleService, 'addEmailImportImportRuleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ emailImportImportMapping });
      comp.ngOnInit();

      expect(emailImportImportRuleService.query).toHaveBeenCalled();
      expect(emailImportImportRuleService.addEmailImportImportRuleToCollectionIfMissing).toHaveBeenCalledWith(
        emailImportImportRuleCollection,
        ...additionalEmailImportImportRules.map(expect.objectContaining),
      );
      expect(comp.emailImportImportRulesSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const emailImportImportMapping: IEmailImportImportMapping = { id: 30950 };
      const rule: IEmailImportImportRule = { id: 12705 };
      emailImportImportMapping.rule = rule;

      activatedRoute.data = of({ emailImportImportMapping });
      comp.ngOnInit();

      expect(comp.emailImportImportRulesSharedCollection).toContainEqual(rule);
      expect(comp.emailImportImportMapping).toEqual(emailImportImportMapping);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmailImportImportMapping>>();
      const emailImportImportMapping = { id: 20388 };
      jest.spyOn(emailImportImportMappingFormService, 'getEmailImportImportMapping').mockReturnValue(emailImportImportMapping);
      jest.spyOn(emailImportImportMappingService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ emailImportImportMapping });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: emailImportImportMapping }));
      saveSubject.complete();

      // THEN
      expect(emailImportImportMappingFormService.getEmailImportImportMapping).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(emailImportImportMappingService.update).toHaveBeenCalledWith(expect.objectContaining(emailImportImportMapping));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmailImportImportMapping>>();
      const emailImportImportMapping = { id: 20388 };
      jest.spyOn(emailImportImportMappingFormService, 'getEmailImportImportMapping').mockReturnValue({ id: null });
      jest.spyOn(emailImportImportMappingService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ emailImportImportMapping: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: emailImportImportMapping }));
      saveSubject.complete();

      // THEN
      expect(emailImportImportMappingFormService.getEmailImportImportMapping).toHaveBeenCalled();
      expect(emailImportImportMappingService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmailImportImportMapping>>();
      const emailImportImportMapping = { id: 20388 };
      jest.spyOn(emailImportImportMappingService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ emailImportImportMapping });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(emailImportImportMappingService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareEmailImportImportRule', () => {
      it('should forward to emailImportImportRuleService', () => {
        const entity = { id: 12705 };
        const entity2 = { id: 16106 };
        jest.spyOn(emailImportImportRuleService, 'compareEmailImportImportRule');
        comp.compareEmailImportImportRule(entity, entity2);
        expect(emailImportImportRuleService.compareEmailImportImportRule).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
