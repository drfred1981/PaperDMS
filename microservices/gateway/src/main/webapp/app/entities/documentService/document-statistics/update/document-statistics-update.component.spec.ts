import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IDocument } from 'app/entities/documentService/document/document.model';
import { DocumentService } from 'app/entities/documentService/document/service/document.service';
import { DocumentStatisticsService } from '../service/document-statistics.service';
import { IDocumentStatistics } from '../document-statistics.model';
import { DocumentStatisticsFormService } from './document-statistics-form.service';

import { DocumentStatisticsUpdateComponent } from './document-statistics-update.component';

describe('DocumentStatistics Management Update Component', () => {
  let comp: DocumentStatisticsUpdateComponent;
  let fixture: ComponentFixture<DocumentStatisticsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let documentStatisticsFormService: DocumentStatisticsFormService;
  let documentStatisticsService: DocumentStatisticsService;
  let documentService: DocumentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [DocumentStatisticsUpdateComponent],
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
      .overrideTemplate(DocumentStatisticsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DocumentStatisticsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    documentStatisticsFormService = TestBed.inject(DocumentStatisticsFormService);
    documentStatisticsService = TestBed.inject(DocumentStatisticsService);
    documentService = TestBed.inject(DocumentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Document query and add missing value', () => {
      const documentStatistics: IDocumentStatistics = { id: 4489 };
      const document: IDocument = { id: 24703 };
      documentStatistics.document = document;

      const documentCollection: IDocument[] = [{ id: 24703 }];
      jest.spyOn(documentService, 'query').mockReturnValue(of(new HttpResponse({ body: documentCollection })));
      const additionalDocuments = [document];
      const expectedCollection: IDocument[] = [...additionalDocuments, ...documentCollection];
      jest.spyOn(documentService, 'addDocumentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ documentStatistics });
      comp.ngOnInit();

      expect(documentService.query).toHaveBeenCalled();
      expect(documentService.addDocumentToCollectionIfMissing).toHaveBeenCalledWith(
        documentCollection,
        ...additionalDocuments.map(expect.objectContaining),
      );
      expect(comp.documentsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const documentStatistics: IDocumentStatistics = { id: 4489 };
      const document: IDocument = { id: 24703 };
      documentStatistics.document = document;

      activatedRoute.data = of({ documentStatistics });
      comp.ngOnInit();

      expect(comp.documentsSharedCollection).toContainEqual(document);
      expect(comp.documentStatistics).toEqual(documentStatistics);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentStatistics>>();
      const documentStatistics = { id: 5208 };
      jest.spyOn(documentStatisticsFormService, 'getDocumentStatistics').mockReturnValue(documentStatistics);
      jest.spyOn(documentStatisticsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentStatistics });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: documentStatistics }));
      saveSubject.complete();

      // THEN
      expect(documentStatisticsFormService.getDocumentStatistics).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(documentStatisticsService.update).toHaveBeenCalledWith(expect.objectContaining(documentStatistics));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentStatistics>>();
      const documentStatistics = { id: 5208 };
      jest.spyOn(documentStatisticsFormService, 'getDocumentStatistics').mockReturnValue({ id: null });
      jest.spyOn(documentStatisticsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentStatistics: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: documentStatistics }));
      saveSubject.complete();

      // THEN
      expect(documentStatisticsFormService.getDocumentStatistics).toHaveBeenCalled();
      expect(documentStatisticsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentStatistics>>();
      const documentStatistics = { id: 5208 };
      jest.spyOn(documentStatisticsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentStatistics });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(documentStatisticsService.update).toHaveBeenCalled();
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
