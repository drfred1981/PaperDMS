import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IDocument } from 'app/entities/documentService/document/document.model';
import { DocumentService } from 'app/entities/documentService/document/service/document.service';
import { DocumentServiceStatusService } from '../service/document-service-status.service';
import { IDocumentServiceStatus } from '../document-service-status.model';
import { DocumentServiceStatusFormService } from './document-service-status-form.service';

import { DocumentServiceStatusUpdateComponent } from './document-service-status-update.component';

describe('DocumentServiceStatus Management Update Component', () => {
  let comp: DocumentServiceStatusUpdateComponent;
  let fixture: ComponentFixture<DocumentServiceStatusUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let documentServiceStatusFormService: DocumentServiceStatusFormService;
  let documentServiceStatusService: DocumentServiceStatusService;
  let documentService: DocumentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [DocumentServiceStatusUpdateComponent],
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
      .overrideTemplate(DocumentServiceStatusUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DocumentServiceStatusUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    documentServiceStatusFormService = TestBed.inject(DocumentServiceStatusFormService);
    documentServiceStatusService = TestBed.inject(DocumentServiceStatusService);
    documentService = TestBed.inject(DocumentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Document query and add missing value', () => {
      const documentServiceStatus: IDocumentServiceStatus = { id: 16256 };
      const document: IDocument = { id: 24703 };
      documentServiceStatus.document = document;

      const documentCollection: IDocument[] = [{ id: 24703 }];
      jest.spyOn(documentService, 'query').mockReturnValue(of(new HttpResponse({ body: documentCollection })));
      const additionalDocuments = [document];
      const expectedCollection: IDocument[] = [...additionalDocuments, ...documentCollection];
      jest.spyOn(documentService, 'addDocumentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ documentServiceStatus });
      comp.ngOnInit();

      expect(documentService.query).toHaveBeenCalled();
      expect(documentService.addDocumentToCollectionIfMissing).toHaveBeenCalledWith(
        documentCollection,
        ...additionalDocuments.map(expect.objectContaining),
      );
      expect(comp.documentsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const documentServiceStatus: IDocumentServiceStatus = { id: 16256 };
      const document: IDocument = { id: 24703 };
      documentServiceStatus.document = document;

      activatedRoute.data = of({ documentServiceStatus });
      comp.ngOnInit();

      expect(comp.documentsSharedCollection).toContainEqual(document);
      expect(comp.documentServiceStatus).toEqual(documentServiceStatus);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentServiceStatus>>();
      const documentServiceStatus = { id: 1543 };
      jest.spyOn(documentServiceStatusFormService, 'getDocumentServiceStatus').mockReturnValue(documentServiceStatus);
      jest.spyOn(documentServiceStatusService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentServiceStatus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: documentServiceStatus }));
      saveSubject.complete();

      // THEN
      expect(documentServiceStatusFormService.getDocumentServiceStatus).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(documentServiceStatusService.update).toHaveBeenCalledWith(expect.objectContaining(documentServiceStatus));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentServiceStatus>>();
      const documentServiceStatus = { id: 1543 };
      jest.spyOn(documentServiceStatusFormService, 'getDocumentServiceStatus').mockReturnValue({ id: null });
      jest.spyOn(documentServiceStatusService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentServiceStatus: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: documentServiceStatus }));
      saveSubject.complete();

      // THEN
      expect(documentServiceStatusFormService.getDocumentServiceStatus).toHaveBeenCalled();
      expect(documentServiceStatusService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentServiceStatus>>();
      const documentServiceStatus = { id: 1543 };
      jest.spyOn(documentServiceStatusService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentServiceStatus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(documentServiceStatusService.update).toHaveBeenCalled();
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
