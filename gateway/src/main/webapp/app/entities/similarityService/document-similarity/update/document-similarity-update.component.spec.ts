import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ISimilarityJob } from 'app/entities/similarityService/similarity-job/similarity-job.model';
import { SimilarityJobService } from 'app/entities/similarityService/similarity-job/service/similarity-job.service';
import { DocumentSimilarityService } from '../service/document-similarity.service';
import { IDocumentSimilarity } from '../document-similarity.model';
import { DocumentSimilarityFormService } from './document-similarity-form.service';

import { DocumentSimilarityUpdateComponent } from './document-similarity-update.component';

describe('DocumentSimilarity Management Update Component', () => {
  let comp: DocumentSimilarityUpdateComponent;
  let fixture: ComponentFixture<DocumentSimilarityUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let documentSimilarityFormService: DocumentSimilarityFormService;
  let documentSimilarityService: DocumentSimilarityService;
  let similarityJobService: SimilarityJobService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [DocumentSimilarityUpdateComponent],
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
      .overrideTemplate(DocumentSimilarityUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DocumentSimilarityUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    documentSimilarityFormService = TestBed.inject(DocumentSimilarityFormService);
    documentSimilarityService = TestBed.inject(DocumentSimilarityService);
    similarityJobService = TestBed.inject(SimilarityJobService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call SimilarityJob query and add missing value', () => {
      const documentSimilarity: IDocumentSimilarity = { id: 17777 };
      const job: ISimilarityJob = { id: 10779 };
      documentSimilarity.job = job;

      const similarityJobCollection: ISimilarityJob[] = [{ id: 10779 }];
      jest.spyOn(similarityJobService, 'query').mockReturnValue(of(new HttpResponse({ body: similarityJobCollection })));
      const additionalSimilarityJobs = [job];
      const expectedCollection: ISimilarityJob[] = [...additionalSimilarityJobs, ...similarityJobCollection];
      jest.spyOn(similarityJobService, 'addSimilarityJobToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ documentSimilarity });
      comp.ngOnInit();

      expect(similarityJobService.query).toHaveBeenCalled();
      expect(similarityJobService.addSimilarityJobToCollectionIfMissing).toHaveBeenCalledWith(
        similarityJobCollection,
        ...additionalSimilarityJobs.map(expect.objectContaining),
      );
      expect(comp.similarityJobsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const documentSimilarity: IDocumentSimilarity = { id: 17777 };
      const job: ISimilarityJob = { id: 10779 };
      documentSimilarity.job = job;

      activatedRoute.data = of({ documentSimilarity });
      comp.ngOnInit();

      expect(comp.similarityJobsSharedCollection).toContainEqual(job);
      expect(comp.documentSimilarity).toEqual(documentSimilarity);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentSimilarity>>();
      const documentSimilarity = { id: 18427 };
      jest.spyOn(documentSimilarityFormService, 'getDocumentSimilarity').mockReturnValue(documentSimilarity);
      jest.spyOn(documentSimilarityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentSimilarity });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: documentSimilarity }));
      saveSubject.complete();

      // THEN
      expect(documentSimilarityFormService.getDocumentSimilarity).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(documentSimilarityService.update).toHaveBeenCalledWith(expect.objectContaining(documentSimilarity));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentSimilarity>>();
      const documentSimilarity = { id: 18427 };
      jest.spyOn(documentSimilarityFormService, 'getDocumentSimilarity').mockReturnValue({ id: null });
      jest.spyOn(documentSimilarityService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentSimilarity: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: documentSimilarity }));
      saveSubject.complete();

      // THEN
      expect(documentSimilarityFormService.getDocumentSimilarity).toHaveBeenCalled();
      expect(documentSimilarityService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentSimilarity>>();
      const documentSimilarity = { id: 18427 };
      jest.spyOn(documentSimilarityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentSimilarity });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(documentSimilarityService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareSimilarityJob', () => {
      it('should forward to similarityJobService', () => {
        const entity = { id: 10779 };
        const entity2 = { id: 13718 };
        jest.spyOn(similarityJobService, 'compareSimilarityJob');
        comp.compareSimilarityJob(entity, entity2);
        expect(similarityJobService.compareSimilarityJob).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
