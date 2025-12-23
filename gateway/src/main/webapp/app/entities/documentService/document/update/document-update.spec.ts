import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IDocumentType } from 'app/entities/documentService/document-type/document-type.model';
import { DocumentTypeService } from 'app/entities/documentService/document-type/service/document-type.service';
import { IFolder } from 'app/entities/documentService/folder/folder.model';
import { FolderService } from 'app/entities/documentService/folder/service/folder.service';
import { IDocument } from '../document.model';
import { DocumentService } from '../service/document.service';

import { DocumentFormService } from './document-form.service';
import { DocumentUpdate } from './document-update';

describe('Document Management Update Component', () => {
  let comp: DocumentUpdate;
  let fixture: ComponentFixture<DocumentUpdate>;
  let activatedRoute: ActivatedRoute;
  let documentFormService: DocumentFormService;
  let documentService: DocumentService;
  let folderService: FolderService;
  let documentTypeService: DocumentTypeService;

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

    fixture = TestBed.createComponent(DocumentUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    documentFormService = TestBed.inject(DocumentFormService);
    documentService = TestBed.inject(DocumentService);
    folderService = TestBed.inject(FolderService);
    documentTypeService = TestBed.inject(DocumentTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Folder query and add missing value', () => {
      const document: IDocument = { id: 4007 };
      const folder: IFolder = { id: 4745 };
      document.folder = folder;

      const folderCollection: IFolder[] = [{ id: 4745 }];
      jest.spyOn(folderService, 'query').mockReturnValue(of(new HttpResponse({ body: folderCollection })));
      const additionalFolders = [folder];
      const expectedCollection: IFolder[] = [...additionalFolders, ...folderCollection];
      jest.spyOn(folderService, 'addFolderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ document });
      comp.ngOnInit();

      expect(folderService.query).toHaveBeenCalled();
      expect(folderService.addFolderToCollectionIfMissing).toHaveBeenCalledWith(
        folderCollection,
        ...additionalFolders.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.foldersSharedCollection()).toEqual(expectedCollection);
    });

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
        ...additionalDocumentTypes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.documentTypesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const document: IDocument = { id: 4007 };
      const folder: IFolder = { id: 4745 };
      document.folder = folder;
      const documentType: IDocumentType = { id: 9974 };
      document.documentType = documentType;

      activatedRoute.data = of({ document });
      comp.ngOnInit();

      expect(comp.foldersSharedCollection()).toContainEqual(folder);
      expect(comp.documentTypesSharedCollection()).toContainEqual(documentType);
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
    describe('compareFolder', () => {
      it('should forward to folderService', () => {
        const entity = { id: 4745 };
        const entity2 = { id: 16447 };
        jest.spyOn(folderService, 'compareFolder');
        comp.compareFolder(entity, entity2);
        expect(folderService.compareFolder).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareDocumentType', () => {
      it('should forward to documentTypeService', () => {
        const entity = { id: 9974 };
        const entity2 = { id: 29456 };
        jest.spyOn(documentTypeService, 'compareDocumentType');
        comp.compareDocumentType(entity, entity2);
        expect(documentTypeService.compareDocumentType).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
