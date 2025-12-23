import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IMergeJob } from '../merge-job.model';
import { MergeJobService } from '../service/merge-job.service';

import { MergeJobFormService } from './merge-job-form.service';
import { MergeJobUpdate } from './merge-job-update';

describe('MergeJob Management Update Component', () => {
  let comp: MergeJobUpdate;
  let fixture: ComponentFixture<MergeJobUpdate>;
  let activatedRoute: ActivatedRoute;
  let mergeJobFormService: MergeJobFormService;
  let mergeJobService: MergeJobService;

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

    fixture = TestBed.createComponent(MergeJobUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    mergeJobFormService = TestBed.inject(MergeJobFormService);
    mergeJobService = TestBed.inject(MergeJobService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const mergeJob: IMergeJob = { id: 13745 };

      activatedRoute.data = of({ mergeJob });
      comp.ngOnInit();

      expect(comp.mergeJob).toEqual(mergeJob);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMergeJob>>();
      const mergeJob = { id: 19247 };
      jest.spyOn(mergeJobFormService, 'getMergeJob').mockReturnValue(mergeJob);
      jest.spyOn(mergeJobService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ mergeJob });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: mergeJob }));
      saveSubject.complete();

      // THEN
      expect(mergeJobFormService.getMergeJob).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(mergeJobService.update).toHaveBeenCalledWith(expect.objectContaining(mergeJob));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMergeJob>>();
      const mergeJob = { id: 19247 };
      jest.spyOn(mergeJobFormService, 'getMergeJob').mockReturnValue({ id: null });
      jest.spyOn(mergeJobService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ mergeJob: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: mergeJob }));
      saveSubject.complete();

      // THEN
      expect(mergeJobFormService.getMergeJob).toHaveBeenCalled();
      expect(mergeJobService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMergeJob>>();
      const mergeJob = { id: 19247 };
      jest.spyOn(mergeJobService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ mergeJob });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(mergeJobService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
