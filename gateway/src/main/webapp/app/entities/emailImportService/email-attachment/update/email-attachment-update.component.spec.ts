import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IEmailImport } from 'app/entities/emailImportService/email-import/email-import.model';
import { EmailImportService } from 'app/entities/emailImportService/email-import/service/email-import.service';
import { EmailAttachmentService } from '../service/email-attachment.service';
import { IEmailAttachment } from '../email-attachment.model';
import { EmailAttachmentFormService } from './email-attachment-form.service';

import { EmailAttachmentUpdateComponent } from './email-attachment-update.component';

describe('EmailAttachment Management Update Component', () => {
  let comp: EmailAttachmentUpdateComponent;
  let fixture: ComponentFixture<EmailAttachmentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let emailAttachmentFormService: EmailAttachmentFormService;
  let emailAttachmentService: EmailAttachmentService;
  let emailImportService: EmailImportService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [EmailAttachmentUpdateComponent],
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
      .overrideTemplate(EmailAttachmentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EmailAttachmentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    emailAttachmentFormService = TestBed.inject(EmailAttachmentFormService);
    emailAttachmentService = TestBed.inject(EmailAttachmentService);
    emailImportService = TestBed.inject(EmailImportService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call EmailImport query and add missing value', () => {
      const emailAttachment: IEmailAttachment = { id: 18557 };
      const emailImport: IEmailImport = { id: 3064 };
      emailAttachment.emailImport = emailImport;

      const emailImportCollection: IEmailImport[] = [{ id: 3064 }];
      jest.spyOn(emailImportService, 'query').mockReturnValue(of(new HttpResponse({ body: emailImportCollection })));
      const additionalEmailImports = [emailImport];
      const expectedCollection: IEmailImport[] = [...additionalEmailImports, ...emailImportCollection];
      jest.spyOn(emailImportService, 'addEmailImportToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ emailAttachment });
      comp.ngOnInit();

      expect(emailImportService.query).toHaveBeenCalled();
      expect(emailImportService.addEmailImportToCollectionIfMissing).toHaveBeenCalledWith(
        emailImportCollection,
        ...additionalEmailImports.map(expect.objectContaining),
      );
      expect(comp.emailImportsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const emailAttachment: IEmailAttachment = { id: 18557 };
      const emailImport: IEmailImport = { id: 3064 };
      emailAttachment.emailImport = emailImport;

      activatedRoute.data = of({ emailAttachment });
      comp.ngOnInit();

      expect(comp.emailImportsSharedCollection).toContainEqual(emailImport);
      expect(comp.emailAttachment).toEqual(emailAttachment);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmailAttachment>>();
      const emailAttachment = { id: 28685 };
      jest.spyOn(emailAttachmentFormService, 'getEmailAttachment').mockReturnValue(emailAttachment);
      jest.spyOn(emailAttachmentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ emailAttachment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: emailAttachment }));
      saveSubject.complete();

      // THEN
      expect(emailAttachmentFormService.getEmailAttachment).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(emailAttachmentService.update).toHaveBeenCalledWith(expect.objectContaining(emailAttachment));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmailAttachment>>();
      const emailAttachment = { id: 28685 };
      jest.spyOn(emailAttachmentFormService, 'getEmailAttachment').mockReturnValue({ id: null });
      jest.spyOn(emailAttachmentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ emailAttachment: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: emailAttachment }));
      saveSubject.complete();

      // THEN
      expect(emailAttachmentFormService.getEmailAttachment).toHaveBeenCalled();
      expect(emailAttachmentService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmailAttachment>>();
      const emailAttachment = { id: 28685 };
      jest.spyOn(emailAttachmentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ emailAttachment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(emailAttachmentService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareEmailImport', () => {
      it('should forward to emailImportService', () => {
        const entity = { id: 3064 };
        const entity2 = { id: 14958 };
        jest.spyOn(emailImportService, 'compareEmailImport');
        comp.compareEmailImport(entity, entity2);
        expect(emailImportService.compareEmailImport).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
