import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IDocumentType } from 'app/entities/documentService/document-type/document-type.model';
import { DocumentTypeService } from 'app/entities/documentService/document-type/service/document-type.service';
import { IMetaFolder } from 'app/entities/documentService/meta-folder/meta-folder.model';
import { MetaFolderService } from 'app/entities/documentService/meta-folder/service/meta-folder.service';
import { IDocument } from '../document.model';
import { DocumentService } from '../service/document.service';
import { DocumentFormService } from './document-form.service';

import { DocumentUpdateComponent } from './document-update.component';

describe('Document Management Update Component', () => {
  let comp: DocumentUpdateComponent;
  let fixture: ComponentFixture<DocumentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let documentFormService: DocumentFormService;
  let documentService: DocumentService;
  let documentTypeService: DocumentTypeService;
  let metaFolderService: MetaFolderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [DocumentUpdateComponent],
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
      .overrideTemplate(DocumentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DocumentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    documentFormService = TestBed.inject(DocumentFormService);
    documentService = TestBed.inject(DocumentService);
    documentTypeService = TestBed.inject(DocumentTypeService);
    metaFolderService = TestBed.inject(MetaFolderService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call DocumentType query and add missing value', () => {
      const document: IDocument = { id: 4007 };
      const documentType: IDocumentType = { id: 9974 };
      document.documentType = documentType;

      const documentTypeCollection: IDocumentType[] = [{ id: 9974 }];
      jest.spyOn(documentTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: documentTypeCollection })));
      const additionalDocumentTypes = [documentType];
      const expectedCollection: IDocumentType[] = [...additionalDocumentTypes, ...documentTypeCollection];
      jest.spyOn(documentTypeService, 'addDocumentTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ document });
      comp.ngOnInit();

      expect(documentTypeService.query).toHaveBeenCalled();
      expect(documentTypeService.addDocumentTypeToCollectionIfMissing).toHaveBeenCalledWith(
        documentTypeCollection,
        ...additionalDocumentTypes.map(expect.objectContaining),
      );
      expect(comp.documentTypesSharedCollection).toEqual(expectedCollection);
    });

    it('should call MetaFolder query and add missing value', () => {
      const document: IDocument = { id: 4007 };
      const folder: IMetaFolder = { id: 18281 };
      document.folder = folder;

      const metaFolderCollection: IMetaFolder[] = [{ id: 18281 }];
      jest.spyOn(metaFolderService, 'query').mockReturnValue(of(new HttpResponse({ body: metaFolderCollection })));
      const additionalMetaFolders = [folder];
      const expectedCollection: IMetaFolder[] = [...additionalMetaFolders, ...metaFolderCollection];
      jest.spyOn(metaFolderService, 'addMetaFolderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ document });
      comp.ngOnInit();

      expect(metaFolderService.query).toHaveBeenCalled();
      expect(metaFolderService.addMetaFolderToCollectionIfMissing).toHaveBeenCalledWith(
        metaFolderCollection,
        ...additionalMetaFolders.map(expect.objectContaining),
      );
      expect(comp.metaFoldersSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const document: IDocument = { id: 4007 };
      const documentType: IDocumentType = { id: 9974 };
      document.documentType = documentType;
      const folder: IMetaFolder = { id: 18281 };
      document.folder = folder;

      activatedRoute.data = of({ document });
      comp.ngOnInit();

      expect(comp.documentTypesSharedCollection).toContainEqual(documentType);
      expect(comp.metaFoldersSharedCollection).toContainEqual(folder);
      expect(comp.document).toEqual(document);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocument>>();
      const document = { id: 24703 };
      jest.spyOn(documentFormService, 'getDocument').mockReturnValue(document);
      jest.spyOn(documentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ document });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: document }));
      saveSubject.complete();

      // THEN
      expect(documentFormService.getDocument).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(documentService.update).toHaveBeenCalledWith(expect.objectContaining(document));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocument>>();
      const document = { id: 24703 };
      jest.spyOn(documentFormService, 'getDocument').mockReturnValue({ id: null });
      jest.spyOn(documentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ document: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: document }));
      saveSubject.complete();

      // THEN
      expect(documentFormService.getDocument).toHaveBeenCalled();
      expect(documentService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocument>>();
      const document = { id: 24703 };
      jest.spyOn(documentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ document });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(documentService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareDocumentType', () => {
      it('should forward to documentTypeService', () => {
        const entity = { id: 9974 };
        const entity2 = { id: 29456 };
        jest.spyOn(documentTypeService, 'compareDocumentType');
        comp.compareDocumentType(entity, entity2);
        expect(documentTypeService.compareDocumentType).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareMetaFolder', () => {
      it('should forward to metaFolderService', () => {
        const entity = { id: 18281 };
        const entity2 = { id: 27869 };
        jest.spyOn(metaFolderService, 'compareMetaFolder');
        comp.compareMetaFolder(entity, entity2);
        expect(metaFolderService.compareMetaFolder).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
