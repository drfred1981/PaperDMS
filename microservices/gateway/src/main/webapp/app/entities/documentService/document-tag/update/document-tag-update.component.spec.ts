import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IDocument } from 'app/entities/documentService/document/document.model';
import { DocumentService } from 'app/entities/documentService/document/service/document.service';
import { IMetaTag } from 'app/entities/documentService/meta-tag/meta-tag.model';
import { MetaTagService } from 'app/entities/documentService/meta-tag/service/meta-tag.service';
import { IDocumentTag } from '../document-tag.model';
import { DocumentTagService } from '../service/document-tag.service';
import { DocumentTagFormService } from './document-tag-form.service';

import { DocumentTagUpdateComponent } from './document-tag-update.component';

describe('DocumentTag Management Update Component', () => {
  let comp: DocumentTagUpdateComponent;
  let fixture: ComponentFixture<DocumentTagUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let documentTagFormService: DocumentTagFormService;
  let documentTagService: DocumentTagService;
  let documentService: DocumentService;
  let metaTagService: MetaTagService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [DocumentTagUpdateComponent],
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
      .overrideTemplate(DocumentTagUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DocumentTagUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    documentTagFormService = TestBed.inject(DocumentTagFormService);
    documentTagService = TestBed.inject(DocumentTagService);
    documentService = TestBed.inject(DocumentService);
    metaTagService = TestBed.inject(MetaTagService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Document query and add missing value', () => {
      const documentTag: IDocumentTag = { id: 31172 };
      const document: IDocument = { id: 24703 };
      documentTag.document = document;

      const documentCollection: IDocument[] = [{ id: 24703 }];
      jest.spyOn(documentService, 'query').mockReturnValue(of(new HttpResponse({ body: documentCollection })));
      const additionalDocuments = [document];
      const expectedCollection: IDocument[] = [...additionalDocuments, ...documentCollection];
      jest.spyOn(documentService, 'addDocumentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ documentTag });
      comp.ngOnInit();

      expect(documentService.query).toHaveBeenCalled();
      expect(documentService.addDocumentToCollectionIfMissing).toHaveBeenCalledWith(
        documentCollection,
        ...additionalDocuments.map(expect.objectContaining),
      );
      expect(comp.documentsSharedCollection).toEqual(expectedCollection);
    });

    it('should call MetaTag query and add missing value', () => {
      const documentTag: IDocumentTag = { id: 31172 };
      const metaTag: IMetaTag = { id: 11753 };
      documentTag.metaTag = metaTag;

      const metaTagCollection: IMetaTag[] = [{ id: 11753 }];
      jest.spyOn(metaTagService, 'query').mockReturnValue(of(new HttpResponse({ body: metaTagCollection })));
      const additionalMetaTags = [metaTag];
      const expectedCollection: IMetaTag[] = [...additionalMetaTags, ...metaTagCollection];
      jest.spyOn(metaTagService, 'addMetaTagToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ documentTag });
      comp.ngOnInit();

      expect(metaTagService.query).toHaveBeenCalled();
      expect(metaTagService.addMetaTagToCollectionIfMissing).toHaveBeenCalledWith(
        metaTagCollection,
        ...additionalMetaTags.map(expect.objectContaining),
      );
      expect(comp.metaTagsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const documentTag: IDocumentTag = { id: 31172 };
      const document: IDocument = { id: 24703 };
      documentTag.document = document;
      const metaTag: IMetaTag = { id: 11753 };
      documentTag.metaTag = metaTag;

      activatedRoute.data = of({ documentTag });
      comp.ngOnInit();

      expect(comp.documentsSharedCollection).toContainEqual(document);
      expect(comp.metaTagsSharedCollection).toContainEqual(metaTag);
      expect(comp.documentTag).toEqual(documentTag);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentTag>>();
      const documentTag = { id: 12264 };
      jest.spyOn(documentTagFormService, 'getDocumentTag').mockReturnValue(documentTag);
      jest.spyOn(documentTagService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentTag });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: documentTag }));
      saveSubject.complete();

      // THEN
      expect(documentTagFormService.getDocumentTag).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(documentTagService.update).toHaveBeenCalledWith(expect.objectContaining(documentTag));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentTag>>();
      const documentTag = { id: 12264 };
      jest.spyOn(documentTagFormService, 'getDocumentTag').mockReturnValue({ id: null });
      jest.spyOn(documentTagService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentTag: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: documentTag }));
      saveSubject.complete();

      // THEN
      expect(documentTagFormService.getDocumentTag).toHaveBeenCalled();
      expect(documentTagService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentTag>>();
      const documentTag = { id: 12264 };
      jest.spyOn(documentTagService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentTag });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(documentTagService.update).toHaveBeenCalled();
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

    describe('compareMetaTag', () => {
      it('should forward to metaTagService', () => {
        const entity = { id: 11753 };
        const entity2 = { id: 29128 };
        jest.spyOn(metaTagService, 'compareMetaTag');
        comp.compareMetaTag(entity, entity2);
        expect(metaTagService.compareMetaTag).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
