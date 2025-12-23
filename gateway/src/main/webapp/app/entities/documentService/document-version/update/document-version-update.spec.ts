import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IDocument } from 'app/entities/documentService/document/document.model';
import { DocumentService } from 'app/entities/documentService/document/service/document.service';
import { IDocumentVersion } from '../document-version.model';
import { DocumentVersionService } from '../service/document-version.service';

import { DocumentVersionFormService } from './document-version-form.service';
import { DocumentVersionUpdate } from './document-version-update';

describe('DocumentVersion Management Update Component', () => {
  let comp: DocumentVersionUpdate;
  let fixture: ComponentFixture<DocumentVersionUpdate>;
  let activatedRoute: ActivatedRoute;
  let documentVersionFormService: DocumentVersionFormService;
  let documentVersionService: DocumentVersionService;
  let documentService: DocumentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TranslateModule.forRoot()],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    });

    fixture = TestBed.createComponent(DocumentVersionUpdate);
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
        ...additionalDocuments.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.documentsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const documentVersion: IDocumentVersion = { id: 25578 };
      const document: IDocument = { id: 24703 };
      documentVersion.document = document;

      activatedRoute.data = of({ documentVersion });
      comp.ngOnInit();

      expect(comp.documentsSharedCollection()).toContainEqual(document);
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
