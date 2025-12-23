import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { SimilarityJobService } from '../service/similarity-job.service';
import { ISimilarityJob } from '../similarity-job.model';

import { SimilarityJobFormService } from './similarity-job-form.service';
import { SimilarityJobUpdate } from './similarity-job-update';

describe('SimilarityJob Management Update Component', () => {
  let comp: SimilarityJobUpdate;
  let fixture: ComponentFixture<SimilarityJobUpdate>;
  let activatedRoute: ActivatedRoute;
  let similarityJobFormService: SimilarityJobFormService;
  let similarityJobService: SimilarityJobService;

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

    fixture = TestBed.createComponent(SimilarityJobUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    similarityJobFormService = TestBed.inject(SimilarityJobFormService);
    similarityJobService = TestBed.inject(SimilarityJobService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const similarityJob: ISimilarityJob = { id: 13718 };

      activatedRoute.data = of({ similarityJob });
      comp.ngOnInit();

      expect(comp.similarityJob).toEqual(similarityJob);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISimilarityJob>>();
      const similarityJob = { id: 10779 };
      jest.spyOn(similarityJobFormService, 'getSimilarityJob').mockReturnValue(similarityJob);
      jest.spyOn(similarityJobService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ similarityJob });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: similarityJob }));
      saveSubject.complete();

      // THEN
      expect(similarityJobFormService.getSimilarityJob).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(similarityJobService.update).toHaveBeenCalledWith(expect.objectContaining(similarityJob));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISimilarityJob>>();
      const similarityJob = { id: 10779 };
      jest.spyOn(similarityJobFormService, 'getSimilarityJob').mockReturnValue({ id: null });
      jest.spyOn(similarityJobService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ similarityJob: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: similarityJob }));
      saveSubject.complete();

      // THEN
      expect(similarityJobFormService.getSimilarityJob).toHaveBeenCalled();
      expect(similarityJobService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISimilarityJob>>();
      const similarityJob = { id: 10779 };
      jest.spyOn(similarityJobService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ similarityJob });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(similarityJobService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
