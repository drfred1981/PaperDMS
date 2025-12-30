import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IDocument } from 'app/entities/documentService/document/document.model';
import { DocumentService } from 'app/entities/documentService/document/service/document.service';
import { DocumentExtractedFieldService } from '../service/document-extracted-field.service';
import { IDocumentExtractedField } from '../document-extracted-field.model';
import { DocumentExtractedFieldFormService } from './document-extracted-field-form.service';

import { DocumentExtractedFieldUpdateComponent } from './document-extracted-field-update.component';

describe('DocumentExtractedField Management Update Component', () => {
  let comp: DocumentExtractedFieldUpdateComponent;
  let fixture: ComponentFixture<DocumentExtractedFieldUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let documentExtractedFieldFormService: DocumentExtractedFieldFormService;
  let documentExtractedFieldService: DocumentExtractedFieldService;
  let documentService: DocumentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [DocumentExtractedFieldUpdateComponent],
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
      .overrideTemplate(DocumentExtractedFieldUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DocumentExtractedFieldUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    documentExtractedFieldFormService = TestBed.inject(DocumentExtractedFieldFormService);
    documentExtractedFieldService = TestBed.inject(DocumentExtractedFieldService);
    documentService = TestBed.inject(DocumentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Document query and add missing value', () => {
      const documentExtractedField: IDocumentExtractedField = { id: 19727 };
      const document: IDocument = { id: 24703 };
      documentExtractedField.document = document;

      const documentCollection: IDocument[] = [{ id: 24703 }];
      jest.spyOn(documentService, 'query').mockReturnValue(of(new HttpResponse({ body: documentCollection })));
      const additionalDocuments = [document];
      const expectedCollection: IDocument[] = [...additionalDocuments, ...documentCollection];
      jest.spyOn(documentService, 'addDocumentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ documentExtractedField });
      comp.ngOnInit();

      expect(documentService.query).toHaveBeenCalled();
      expect(documentService.addDocumentToCollectionIfMissing).toHaveBeenCalledWith(
        documentCollection,
        ...additionalDocuments.map(expect.objectContaining),
      );
      expect(comp.documentsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const documentExtractedField: IDocumentExtractedField = { id: 19727 };
      const document: IDocument = { id: 24703 };
      documentExtractedField.document = document;

      activatedRoute.data = of({ documentExtractedField });
      comp.ngOnInit();

      expect(comp.documentsSharedCollection).toContainEqual(document);
      expect(comp.documentExtractedField).toEqual(documentExtractedField);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentExtractedField>>();
      const documentExtractedField = { id: 3377 };
      jest.spyOn(documentExtractedFieldFormService, 'getDocumentExtractedField').mockReturnValue(documentExtractedField);
      jest.spyOn(documentExtractedFieldService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentExtractedField });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: documentExtractedField }));
      saveSubject.complete();

      // THEN
      expect(documentExtractedFieldFormService.getDocumentExtractedField).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(documentExtractedFieldService.update).toHaveBeenCalledWith(expect.objectContaining(documentExtractedField));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentExtractedField>>();
      const documentExtractedField = { id: 3377 };
      jest.spyOn(documentExtractedFieldFormService, 'getDocumentExtractedField').mockReturnValue({ id: null });
      jest.spyOn(documentExtractedFieldService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentExtractedField: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: documentExtractedField }));
      saveSubject.complete();

      // THEN
      expect(documentExtractedFieldFormService.getDocumentExtractedField).toHaveBeenCalled();
      expect(documentExtractedFieldService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentExtractedField>>();
      const documentExtractedField = { id: 3377 };
      jest.spyOn(documentExtractedFieldService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentExtractedField });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(documentExtractedFieldService.update).toHaveBeenCalled();
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
