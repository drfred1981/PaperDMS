import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IDocument } from 'app/entities/documentService/document/document.model';
import { DocumentService } from 'app/entities/documentService/document/service/document.service';
import { DocumentAuditService } from '../service/document-audit.service';
import { IDocumentAudit } from '../document-audit.model';
import { DocumentAuditFormService } from './document-audit-form.service';

import { DocumentAuditUpdateComponent } from './document-audit-update.component';

describe('DocumentAudit Management Update Component', () => {
  let comp: DocumentAuditUpdateComponent;
  let fixture: ComponentFixture<DocumentAuditUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let documentAuditFormService: DocumentAuditFormService;
  let documentAuditService: DocumentAuditService;
  let documentService: DocumentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [DocumentAuditUpdateComponent],
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
      .overrideTemplate(DocumentAuditUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DocumentAuditUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    documentAuditFormService = TestBed.inject(DocumentAuditFormService);
    documentAuditService = TestBed.inject(DocumentAuditService);
    documentService = TestBed.inject(DocumentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Document query and add missing value', () => {
      const documentAudit: IDocumentAudit = { id: 5164 };
      const document: IDocument = { id: 24703 };
      documentAudit.document = document;

      const documentCollection: IDocument[] = [{ id: 24703 }];
      jest.spyOn(documentService, 'query').mockReturnValue(of(new HttpResponse({ body: documentCollection })));
      const additionalDocuments = [document];
      const expectedCollection: IDocument[] = [...additionalDocuments, ...documentCollection];
      jest.spyOn(documentService, 'addDocumentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ documentAudit });
      comp.ngOnInit();

      expect(documentService.query).toHaveBeenCalled();
      expect(documentService.addDocumentToCollectionIfMissing).toHaveBeenCalledWith(
        documentCollection,
        ...additionalDocuments.map(expect.objectContaining),
      );
      expect(comp.documentsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const documentAudit: IDocumentAudit = { id: 5164 };
      const document: IDocument = { id: 24703 };
      documentAudit.document = document;

      activatedRoute.data = of({ documentAudit });
      comp.ngOnInit();

      expect(comp.documentsSharedCollection).toContainEqual(document);
      expect(comp.documentAudit).toEqual(documentAudit);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentAudit>>();
      const documentAudit = { id: 12096 };
      jest.spyOn(documentAuditFormService, 'getDocumentAudit').mockReturnValue(documentAudit);
      jest.spyOn(documentAuditService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentAudit });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: documentAudit }));
      saveSubject.complete();

      // THEN
      expect(documentAuditFormService.getDocumentAudit).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(documentAuditService.update).toHaveBeenCalledWith(expect.objectContaining(documentAudit));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentAudit>>();
      const documentAudit = { id: 12096 };
      jest.spyOn(documentAuditFormService, 'getDocumentAudit').mockReturnValue({ id: null });
      jest.spyOn(documentAuditService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentAudit: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: documentAudit }));
      saveSubject.complete();

      // THEN
      expect(documentAuditFormService.getDocumentAudit).toHaveBeenCalled();
      expect(documentAuditService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentAudit>>();
      const documentAudit = { id: 12096 };
      jest.spyOn(documentAuditService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentAudit });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(documentAuditService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareDocument', () => {
      it('should forward to documentService', () => {
        const entity = { id: 24703 };
        const entity2 = { id: 4007 };
        jest.spyOn(documentService, 'compareDocument');
        comp.compareDocument(entity, entity2);
        expect(documentService.compareDocument).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
