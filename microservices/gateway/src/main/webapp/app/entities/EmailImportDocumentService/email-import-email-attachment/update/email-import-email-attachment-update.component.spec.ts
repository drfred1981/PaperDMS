import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IEmailImportDocument } from 'app/entities/EmailImportDocumentService/email-import-document/email-import-document.model';
import { EmailImportDocumentService } from 'app/entities/EmailImportDocumentService/email-import-document/service/email-import-document.service';
import { EmailImportEmailAttachmentService } from '../service/email-import-email-attachment.service';
import { IEmailImportEmailAttachment } from '../email-import-email-attachment.model';
import { EmailImportEmailAttachmentFormService } from './email-import-email-attachment-form.service';

import { EmailImportEmailAttachmentUpdateComponent } from './email-import-email-attachment-update.component';

describe('EmailImportEmailAttachment Management Update Component', () => {
  let comp: EmailImportEmailAttachmentUpdateComponent;
  let fixture: ComponentFixture<EmailImportEmailAttachmentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let emailImportEmailAttachmentFormService: EmailImportEmailAttachmentFormService;
  let emailImportEmailAttachmentService: EmailImportEmailAttachmentService;
  let emailImportDocumentService: EmailImportDocumentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [EmailImportEmailAttachmentUpdateComponent],
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
      .overrideTemplate(EmailImportEmailAttachmentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EmailImportEmailAttachmentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    emailImportEmailAttachmentFormService = TestBed.inject(EmailImportEmailAttachmentFormService);
    emailImportEmailAttachmentService = TestBed.inject(EmailImportEmailAttachmentService);
    emailImportDocumentService = TestBed.inject(EmailImportDocumentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call EmailImportDocument query and add missing value', () => {
      const emailImportEmailAttachment: IEmailImportEmailAttachment = { id: 8308 };
      const emailImportDocument: IEmailImportDocument = { id: 13070 };
      emailImportEmailAttachment.emailImportDocument = emailImportDocument;

      const emailImportDocumentCollection: IEmailImportDocument[] = [{ id: 13070 }];
      jest.spyOn(emailImportDocumentService, 'query').mockReturnValue(of(new HttpResponse({ body: emailImportDocumentCollection })));
      const additionalEmailImportDocuments = [emailImportDocument];
      const expectedCollection: IEmailImportDocument[] = [...additionalEmailImportDocuments, ...emailImportDocumentCollection];
      jest.spyOn(emailImportDocumentService, 'addEmailImportDocumentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ emailImportEmailAttachment });
      comp.ngOnInit();

      expect(emailImportDocumentService.query).toHaveBeenCalled();
      expect(emailImportDocumentService.addEmailImportDocumentToCollectionIfMissing).toHaveBeenCalledWith(
        emailImportDocumentCollection,
        ...additionalEmailImportDocuments.map(expect.objectContaining),
      );
      expect(comp.emailImportDocumentsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const emailImportEmailAttachment: IEmailImportEmailAttachment = { id: 8308 };
      const emailImportDocument: IEmailImportDocument = { id: 13070 };
      emailImportEmailAttachment.emailImportDocument = emailImportDocument;

      activatedRoute.data = of({ emailImportEmailAttachment });
      comp.ngOnInit();

      expect(comp.emailImportDocumentsSharedCollection).toContainEqual(emailImportDocument);
      expect(comp.emailImportEmailAttachment).toEqual(emailImportEmailAttachment);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmailImportEmailAttachment>>();
      const emailImportEmailAttachment = { id: 32731 };
      jest.spyOn(emailImportEmailAttachmentFormService, 'getEmailImportEmailAttachment').mockReturnValue(emailImportEmailAttachment);
      jest.spyOn(emailImportEmailAttachmentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ emailImportEmailAttachment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: emailImportEmailAttachment }));
      saveSubject.complete();

      // THEN
      expect(emailImportEmailAttachmentFormService.getEmailImportEmailAttachment).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(emailImportEmailAttachmentService.update).toHaveBeenCalledWith(expect.objectContaining(emailImportEmailAttachment));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmailImportEmailAttachment>>();
      const emailImportEmailAttachment = { id: 32731 };
      jest.spyOn(emailImportEmailAttachmentFormService, 'getEmailImportEmailAttachment').mockReturnValue({ id: null });
      jest.spyOn(emailImportEmailAttachmentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ emailImportEmailAttachment: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: emailImportEmailAttachment }));
      saveSubject.complete();

      // THEN
      expect(emailImportEmailAttachmentFormService.getEmailImportEmailAttachment).toHaveBeenCalled();
      expect(emailImportEmailAttachmentService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmailImportEmailAttachment>>();
      const emailImportEmailAttachment = { id: 32731 };
      jest.spyOn(emailImportEmailAttachmentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ emailImportEmailAttachment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(emailImportEmailAttachmentService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareEmailImportDocument', () => {
      it('should forward to emailImportDocumentService', () => {
        const entity = { id: 13070 };
        const entity2 = { id: 27775 };
        jest.spyOn(emailImportDocumentService, 'compareEmailImportDocument');
        comp.compareEmailImportDocument(entity, entity2);
        expect(emailImportDocumentService.compareEmailImportDocument).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
