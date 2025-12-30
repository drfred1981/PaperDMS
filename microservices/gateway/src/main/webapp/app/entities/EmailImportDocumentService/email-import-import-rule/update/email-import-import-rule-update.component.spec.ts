import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { EmailImportImportRuleService } from '../service/email-import-import-rule.service';
import { IEmailImportImportRule } from '../email-import-import-rule.model';
import { EmailImportImportRuleFormService } from './email-import-import-rule-form.service';

import { EmailImportImportRuleUpdateComponent } from './email-import-import-rule-update.component';

describe('EmailImportImportRule Management Update Component', () => {
  let comp: EmailImportImportRuleUpdateComponent;
  let fixture: ComponentFixture<EmailImportImportRuleUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let emailImportImportRuleFormService: EmailImportImportRuleFormService;
  let emailImportImportRuleService: EmailImportImportRuleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [EmailImportImportRuleUpdateComponent],
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
      .overrideTemplate(EmailImportImportRuleUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EmailImportImportRuleUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    emailImportImportRuleFormService = TestBed.inject(EmailImportImportRuleFormService);
    emailImportImportRuleService = TestBed.inject(EmailImportImportRuleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const emailImportImportRule: IEmailImportImportRule = { id: 16106 };

      activatedRoute.data = of({ emailImportImportRule });
      comp.ngOnInit();

      expect(comp.emailImportImportRule).toEqual(emailImportImportRule);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmailImportImportRule>>();
      const emailImportImportRule = { id: 12705 };
      jest.spyOn(emailImportImportRuleFormService, 'getEmailImportImportRule').mockReturnValue(emailImportImportRule);
      jest.spyOn(emailImportImportRuleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ emailImportImportRule });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: emailImportImportRule }));
      saveSubject.complete();

      // THEN
      expect(emailImportImportRuleFormService.getEmailImportImportRule).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(emailImportImportRuleService.update).toHaveBeenCalledWith(expect.objectContaining(emailImportImportRule));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmailImportImportRule>>();
      const emailImportImportRule = { id: 12705 };
      jest.spyOn(emailImportImportRuleFormService, 'getEmailImportImportRule').mockReturnValue({ id: null });
      jest.spyOn(emailImportImportRuleService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ emailImportImportRule: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: emailImportImportRule }));
      saveSubject.complete();

      // THEN
      expect(emailImportImportRuleFormService.getEmailImportImportRule).toHaveBeenCalled();
      expect(emailImportImportRuleService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmailImportImportRule>>();
      const emailImportImportRule = { id: 12705 };
      jest.spyOn(emailImportImportRuleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ emailImportImportRule });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(emailImportImportRuleService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
