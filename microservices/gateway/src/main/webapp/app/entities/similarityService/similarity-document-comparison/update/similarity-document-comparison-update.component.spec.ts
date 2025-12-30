import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ISimilarityJob } from 'app/entities/similarityService/similarity-job/similarity-job.model';
import { SimilarityJobService } from 'app/entities/similarityService/similarity-job/service/similarity-job.service';
import { SimilarityDocumentComparisonService } from '../service/similarity-document-comparison.service';
import { ISimilarityDocumentComparison } from '../similarity-document-comparison.model';
import { SimilarityDocumentComparisonFormService } from './similarity-document-comparison-form.service';

import { SimilarityDocumentComparisonUpdateComponent } from './similarity-document-comparison-update.component';

describe('SimilarityDocumentComparison Management Update Component', () => {
  let comp: SimilarityDocumentComparisonUpdateComponent;
  let fixture: ComponentFixture<SimilarityDocumentComparisonUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let similarityDocumentComparisonFormService: SimilarityDocumentComparisonFormService;
  let similarityDocumentComparisonService: SimilarityDocumentComparisonService;
  let similarityJobService: SimilarityJobService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [SimilarityDocumentComparisonUpdateComponent],
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
      .overrideTemplate(SimilarityDocumentComparisonUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SimilarityDocumentComparisonUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    similarityDocumentComparisonFormService = TestBed.inject(SimilarityDocumentComparisonFormService);
    similarityDocumentComparisonService = TestBed.inject(SimilarityDocumentComparisonService);
    similarityJobService = TestBed.inject(SimilarityJobService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call SimilarityJob query and add missing value', () => {
      const similarityDocumentComparison: ISimilarityDocumentComparison = { id: 3431 };
      const job: ISimilarityJob = { id: 10779 };
      similarityDocumentComparison.job = job;

      const similarityJobCollection: ISimilarityJob[] = [{ id: 10779 }];
      jest.spyOn(similarityJobService, 'query').mockReturnValue(of(new HttpResponse({ body: similarityJobCollection })));
      const additionalSimilarityJobs = [job];
      const expectedCollection: ISimilarityJob[] = [...additionalSimilarityJobs, ...similarityJobCollection];
      jest.spyOn(similarityJobService, 'addSimilarityJobToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ similarityDocumentComparison });
      comp.ngOnInit();

      expect(similarityJobService.query).toHaveBeenCalled();
      expect(similarityJobService.addSimilarityJobToCollectionIfMissing).toHaveBeenCalledWith(
        similarityJobCollection,
        ...additionalSimilarityJobs.map(expect.objectContaining),
      );
      expect(comp.similarityJobsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const similarityDocumentComparison: ISimilarityDocumentComparison = { id: 3431 };
      const job: ISimilarityJob = { id: 10779 };
      similarityDocumentComparison.job = job;

      activatedRoute.data = of({ similarityDocumentComparison });
      comp.ngOnInit();

      expect(comp.similarityJobsSharedCollection).toContainEqual(job);
      expect(comp.similarityDocumentComparison).toEqual(similarityDocumentComparison);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISimilarityDocumentComparison>>();
      const similarityDocumentComparison = { id: 8038 };
      jest.spyOn(similarityDocumentComparisonFormService, 'getSimilarityDocumentComparison').mockReturnValue(similarityDocumentComparison);
      jest.spyOn(similarityDocumentComparisonService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ similarityDocumentComparison });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: similarityDocumentComparison }));
      saveSubject.complete();

      // THEN
      expect(similarityDocumentComparisonFormService.getSimilarityDocumentComparison).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(similarityDocumentComparisonService.update).toHaveBeenCalledWith(expect.objectContaining(similarityDocumentComparison));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISimilarityDocumentComparison>>();
      const similarityDocumentComparison = { id: 8038 };
      jest.spyOn(similarityDocumentComparisonFormService, 'getSimilarityDocumentComparison').mockReturnValue({ id: null });
      jest.spyOn(similarityDocumentComparisonService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ similarityDocumentComparison: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: similarityDocumentComparison }));
      saveSubject.complete();

      // THEN
      expect(similarityDocumentComparisonFormService.getSimilarityDocumentComparison).toHaveBeenCalled();
      expect(similarityDocumentComparisonService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISimilarityDocumentComparison>>();
      const similarityDocumentComparison = { id: 8038 };
      jest.spyOn(similarityDocumentComparisonService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ similarityDocumentComparison });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(similarityDocumentComparisonService.update).toHaveBeenCalled();
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
