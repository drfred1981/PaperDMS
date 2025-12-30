import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IEmailImportImportRule } from 'app/entities/EmailImportDocumentService/email-import-import-rule/email-import-import-rule.model';
import { EmailImportImportRuleService } from 'app/entities/EmailImportDocumentService/email-import-import-rule/service/email-import-import-rule.service';
import { EmailImportDocumentService } from '../service/email-import-document.service';
import { IEmailImportDocument } from '../email-import-document.model';
import { EmailImportDocumentFormService } from './email-import-document-form.service';

import { EmailImportDocumentUpdateComponent } from './email-import-document-update.component';

describe('EmailImportDocument Management Update Component', () => {
  let comp: EmailImportDocumentUpdateComponent;
  let fixture: ComponentFixture<EmailImportDocumentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let emailImportDocumentFormService: EmailImportDocumentFormService;
  let emailImportDocumentService: EmailImportDocumentService;
  let emailImportImportRuleService: EmailImportImportRuleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [EmailImportDocumentUpdateComponent],
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
      .overrideTemplate(EmailImportDocumentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EmailImportDocumentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    emailImportDocumentFormService = TestBed.inject(EmailImportDocumentFormService);
    emailImportDocumentService = TestBed.inject(EmailImportDocumentService);
    emailImportImportRuleService = TestBed.inject(EmailImportImportRuleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call EmailImportImportRule query and add missing value', () => {
      const emailImportDocument: IEmailImportDocument = { id: 27775 };
      const appliedRule: IEmailImportImportRule = { id: 12705 };
      emailImportDocument.appliedRule = appliedRule;

      const emailImportImportRuleCollection: IEmailImportImportRule[] = [{ id: 12705 }];
      jest.spyOn(emailImportImportRuleService, 'query').mockReturnValue(of(new HttpResponse({ body: emailImportImportRuleCollection })));
      const additionalEmailImportImportRules = [appliedRule];
      const expectedCollection: IEmailImportImportRule[] = [...additionalEmailImportImportRules, ...emailImportImportRuleCollection];
      jest.spyOn(emailImportImportRuleService, 'addEmailImportImportRuleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ emailImportDocument });
      comp.ngOnInit();

      expect(emailImportImportRuleService.query).toHaveBeenCalled();
      expect(emailImportImportRuleService.addEmailImportImportRuleToCollectionIfMissing).toHaveBeenCalledWith(
        emailImportImportRuleCollection,
        ...additionalEmailImportImportRules.map(expect.objectContaining),
      );
      expect(comp.emailImportImportRulesSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const emailImportDocument: IEmailImportDocument = { id: 27775 };
      const appliedRule: IEmailImportImportRule = { id: 12705 };
      emailImportDocument.appliedRule = appliedRule;

      activatedRoute.data = of({ emailImportDocument });
      comp.ngOnInit();

      expect(comp.emailImportImportRulesSharedCollection).toContainEqual(appliedRule);
      expect(comp.emailImportDocument).toEqual(emailImportDocument);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmailImportDocument>>();
      const emailImportDocument = { id: 13070 };
      jest.spyOn(emailImportDocumentFormService, 'getEmailImportDocument').mockReturnValue(emailImportDocument);
      jest.spyOn(emailImportDocumentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ emailImportDocument });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: emailImportDocument }));
      saveSubject.complete();

      // THEN
      expect(emailImportDocumentFormService.getEmailImportDocument).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(emailImportDocumentService.update).toHaveBeenCalledWith(expect.objectContaining(emailImportDocument));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmailImportDocument>>();
      const emailImportDocument = { id: 13070 };
      jest.spyOn(emailImportDocumentFormService, 'getEmailImportDocument').mockReturnValue({ id: null });
      jest.spyOn(emailImportDocumentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ emailImportDocument: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: emailImportDocument }));
      saveSubject.complete();

      // THEN
      expect(emailImportDocumentFormService.getEmailImportDocument).toHaveBeenCalled();
      expect(emailImportDocumentService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmailImportDocument>>();
      const emailImportDocument = { id: 13070 };
      jest.spyOn(emailImportDocumentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ emailImportDocument });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(emailImportDocumentService.update).toHaveBeenCalled();
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
