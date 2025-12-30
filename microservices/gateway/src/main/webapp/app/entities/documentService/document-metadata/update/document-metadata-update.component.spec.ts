import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IDocument } from 'app/entities/documentService/document/document.model';
import { DocumentService } from 'app/entities/documentService/document/service/document.service';
import { DocumentMetadataService } from '../service/document-metadata.service';
import { IDocumentMetadata } from '../document-metadata.model';
import { DocumentMetadataFormService } from './document-metadata-form.service';

import { DocumentMetadataUpdateComponent } from './document-metadata-update.component';

describe('DocumentMetadata Management Update Component', () => {
  let comp: DocumentMetadataUpdateComponent;
  let fixture: ComponentFixture<DocumentMetadataUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let documentMetadataFormService: DocumentMetadataFormService;
  let documentMetadataService: DocumentMetadataService;
  let documentService: DocumentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [DocumentMetadataUpdateComponent],
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
      .overrideTemplate(DocumentMetadataUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DocumentMetadataUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    documentMetadataFormService = TestBed.inject(DocumentMetadataFormService);
    documentMetadataService = TestBed.inject(DocumentMetadataService);
    documentService = TestBed.inject(DocumentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Document query and add missing value', () => {
      const documentMetadata: IDocumentMetadata = { id: 2476 };
      const document: IDocument = { id: 24703 };
      documentMetadata.document = document;

      const documentCollection: IDocument[] = [{ id: 24703 }];
      jest.spyOn(documentService, 'query').mockReturnValue(of(new HttpResponse({ body: documentCollection })));
      const additionalDocuments = [document];
      const expectedCollection: IDocument[] = [...additionalDocuments, ...documentCollection];
      jest.spyOn(documentService, 'addDocumentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ documentMetadata });
      comp.ngOnInit();

      expect(documentService.query).toHaveBeenCalled();
      expect(documentService.addDocumentToCollectionIfMissing).toHaveBeenCalledWith(
        documentCollection,
        ...additionalDocuments.map(expect.objectContaining),
      );
      expect(comp.documentsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const documentMetadata: IDocumentMetadata = { id: 2476 };
      const document: IDocument = { id: 24703 };
      documentMetadata.document = document;

      activatedRoute.data = of({ documentMetadata });
      comp.ngOnInit();

      expect(comp.documentsSharedCollection).toContainEqual(document);
      expect(comp.documentMetadata).toEqual(documentMetadata);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentMetadata>>();
      const documentMetadata = { id: 10583 };
      jest.spyOn(documentMetadataFormService, 'getDocumentMetadata').mockReturnValue(documentMetadata);
      jest.spyOn(documentMetadataService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentMetadata });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: documentMetadata }));
      saveSubject.complete();

      // THEN
      expect(documentMetadataFormService.getDocumentMetadata).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(documentMetadataService.update).toHaveBeenCalledWith(expect.objectContaining(documentMetadata));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentMetadata>>();
      const documentMetadata = { id: 10583 };
      jest.spyOn(documentMetadataFormService, 'getDocumentMetadata').mockReturnValue({ id: null });
      jest.spyOn(documentMetadataService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentMetadata: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: documentMetadata }));
      saveSubject.complete();

      // THEN
      expect(documentMetadataFormService.getDocumentMetadata).toHaveBeenCalled();
      expect(documentMetadataService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentMetadata>>();
      const documentMetadata = { id: 10583 };
      jest.spyOn(documentMetadataService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentMetadata });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(documentMetadataService.update).toHaveBeenCalled();
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
