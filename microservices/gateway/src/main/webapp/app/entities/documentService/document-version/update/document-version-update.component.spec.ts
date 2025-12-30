import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IDocument } from 'app/entities/documentService/document/document.model';
import { DocumentService } from 'app/entities/documentService/document/service/document.service';
import { DocumentVersionService } from '../service/document-version.service';
import { IDocumentVersion } from '../document-version.model';
import { DocumentVersionFormService } from './document-version-form.service';

import { DocumentVersionUpdateComponent } from './document-version-update.component';

describe('DocumentVersion Management Update Component', () => {
  let comp: DocumentVersionUpdateComponent;
  let fixture: ComponentFixture<DocumentVersionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let documentVersionFormService: DocumentVersionFormService;
  let documentVersionService: DocumentVersionService;
  let documentService: DocumentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [DocumentVersionUpdateComponent],
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
      .overrideTemplate(DocumentVersionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DocumentVersionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    documentVersionFormService = TestBed.inject(DocumentVersionFormService);
    documentVersionService = TestBed.inject(DocumentVersionService);
    documentService = TestBed.inject(DocumentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Document query and add missing value', () => {
      const documentVersion: IDocumentVersion = { id: 25578 };
      const document: IDocument = { id: 24703 };
      documentVersion.document = document;

      const documentCollection: IDocument[] = [{ id: 24703 }];
      jest.spyOn(documentService, 'query').mockReturnValue(of(new HttpResponse({ body: documentCollection })));
      const additionalDocuments = [document];
      const expectedCollection: IDocument[] = [...additionalDocuments, ...documentCollection];
      jest.spyOn(documentService, 'addDocumentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ documentVersion });
      comp.ngOnInit();

      expect(documentService.query).toHaveBeenCalled();
      expect(documentService.addDocumentToCollectionIfMissing).toHaveBeenCalledWith(
        documentCollection,
        ...additionalDocuments.map(expect.objectContaining),
      );
      expect(comp.documentsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const documentVersion: IDocumentVersion = { id: 25578 };
      const document: IDocument = { id: 24703 };
      documentVersion.document = document;

      activatedRoute.data = of({ documentVersion });
      comp.ngOnInit();

      expect(comp.documentsSharedCollection).toContainEqual(document);
      expect(comp.documentVersion).toEqual(documentVersion);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentVersion>>();
      const documentVersion = { id: 2205 };
      jest.spyOn(documentVersionFormService, 'getDocumentVersion').mockReturnValue(documentVersion);
      jest.spyOn(documentVersionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentVersion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: documentVersion }));
      saveSubject.complete();

      // THEN
      expect(documentVersionFormService.getDocumentVersion).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(documentVersionService.update).toHaveBeenCalledWith(expect.objectContaining(documentVersion));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentVersion>>();
      const documentVersion = { id: 2205 };
      jest.spyOn(documentVersionFormService, 'getDocumentVersion').mockReturnValue({ id: null });
      jest.spyOn(documentVersionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentVersion: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: documentVersion }));
      saveSubject.complete();

      // THEN
      expect(documentVersionFormService.getDocumentVersion).toHaveBeenCalled();
      expect(documentVersionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentVersion>>();
      const documentVersion = { id: 2205 };
      jest.spyOn(documentVersionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentVersion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(documentVersionService.update).toHaveBeenCalled();
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
