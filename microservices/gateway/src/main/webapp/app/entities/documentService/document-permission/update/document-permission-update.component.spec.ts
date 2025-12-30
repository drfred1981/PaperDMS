import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IDocument } from 'app/entities/documentService/document/document.model';
import { DocumentService } from 'app/entities/documentService/document/service/document.service';
import { IMetaPermissionGroup } from 'app/entities/documentService/meta-permission-group/meta-permission-group.model';
import { MetaPermissionGroupService } from 'app/entities/documentService/meta-permission-group/service/meta-permission-group.service';
import { IDocumentPermission } from '../document-permission.model';
import { DocumentPermissionService } from '../service/document-permission.service';
import { DocumentPermissionFormService } from './document-permission-form.service';

import { DocumentPermissionUpdateComponent } from './document-permission-update.component';

describe('DocumentPermission Management Update Component', () => {
  let comp: DocumentPermissionUpdateComponent;
  let fixture: ComponentFixture<DocumentPermissionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let documentPermissionFormService: DocumentPermissionFormService;
  let documentPermissionService: DocumentPermissionService;
  let documentService: DocumentService;
  let metaPermissionGroupService: MetaPermissionGroupService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [DocumentPermissionUpdateComponent],
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
      .overrideTemplate(DocumentPermissionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DocumentPermissionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    documentPermissionFormService = TestBed.inject(DocumentPermissionFormService);
    documentPermissionService = TestBed.inject(DocumentPermissionService);
    documentService = TestBed.inject(DocumentService);
    metaPermissionGroupService = TestBed.inject(MetaPermissionGroupService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Document query and add missing value', () => {
      const documentPermission: IDocumentPermission = { id: 25975 };
      const document: IDocument = { id: 24703 };
      documentPermission.document = document;

      const documentCollection: IDocument[] = [{ id: 24703 }];
      jest.spyOn(documentService, 'query').mockReturnValue(of(new HttpResponse({ body: documentCollection })));
      const additionalDocuments = [document];
      const expectedCollection: IDocument[] = [...additionalDocuments, ...documentCollection];
      jest.spyOn(documentService, 'addDocumentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ documentPermission });
      comp.ngOnInit();

      expect(documentService.query).toHaveBeenCalled();
      expect(documentService.addDocumentToCollectionIfMissing).toHaveBeenCalledWith(
        documentCollection,
        ...additionalDocuments.map(expect.objectContaining),
      );
      expect(comp.documentsSharedCollection).toEqual(expectedCollection);
    });

    it('should call MetaPermissionGroup query and add missing value', () => {
      const documentPermission: IDocumentPermission = { id: 25975 };
      const metaPermissionGroup: IMetaPermissionGroup = { id: 4839 };
      documentPermission.metaPermissionGroup = metaPermissionGroup;

      const metaPermissionGroupCollection: IMetaPermissionGroup[] = [{ id: 4839 }];
      jest.spyOn(metaPermissionGroupService, 'query').mockReturnValue(of(new HttpResponse({ body: metaPermissionGroupCollection })));
      const additionalMetaPermissionGroups = [metaPermissionGroup];
      const expectedCollection: IMetaPermissionGroup[] = [...additionalMetaPermissionGroups, ...metaPermissionGroupCollection];
      jest.spyOn(metaPermissionGroupService, 'addMetaPermissionGroupToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ documentPermission });
      comp.ngOnInit();

      expect(metaPermissionGroupService.query).toHaveBeenCalled();
      expect(metaPermissionGroupService.addMetaPermissionGroupToCollectionIfMissing).toHaveBeenCalledWith(
        metaPermissionGroupCollection,
        ...additionalMetaPermissionGroups.map(expect.objectContaining),
      );
      expect(comp.metaPermissionGroupsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const documentPermission: IDocumentPermission = { id: 25975 };
      const document: IDocument = { id: 24703 };
      documentPermission.document = document;
      const metaPermissionGroup: IMetaPermissionGroup = { id: 4839 };
      documentPermission.metaPermissionGroup = metaPermissionGroup;

      activatedRoute.data = of({ documentPermission });
      comp.ngOnInit();

      expect(comp.documentsSharedCollection).toContainEqual(document);
      expect(comp.metaPermissionGroupsSharedCollection).toContainEqual(metaPermissionGroup);
      expect(comp.documentPermission).toEqual(documentPermission);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentPermission>>();
      const documentPermission = { id: 5224 };
      jest.spyOn(documentPermissionFormService, 'getDocumentPermission').mockReturnValue(documentPermission);
      jest.spyOn(documentPermissionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentPermission });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: documentPermission }));
      saveSubject.complete();

      // THEN
      expect(documentPermissionFormService.getDocumentPermission).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(documentPermissionService.update).toHaveBeenCalledWith(expect.objectContaining(documentPermission));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentPermission>>();
      const documentPermission = { id: 5224 };
      jest.spyOn(documentPermissionFormService, 'getDocumentPermission').mockReturnValue({ id: null });
      jest.spyOn(documentPermissionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentPermission: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: documentPermission }));
      saveSubject.complete();

      // THEN
      expect(documentPermissionFormService.getDocumentPermission).toHaveBeenCalled();
      expect(documentPermissionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentPermission>>();
      const documentPermission = { id: 5224 };
      jest.spyOn(documentPermissionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentPermission });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(documentPermissionService.update).toHaveBeenCalled();
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

    describe('compareMetaPermissionGroup', () => {
      it('should forward to metaPermissionGroupService', () => {
        const entity = { id: 4839 };
        const entity2 = { id: 27974 };
        jest.spyOn(metaPermissionGroupService, 'compareMetaPermissionGroup');
        comp.compareMetaPermissionGroup(entity, entity2);
        expect(metaPermissionGroupService.compareMetaPermissionGroup).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
