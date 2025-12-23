import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IPerformanceMetric } from '../performance-metric.model';
import { PerformanceMetricService } from '../service/performance-metric.service';

import { PerformanceMetricFormService } from './performance-metric-form.service';
import { PerformanceMetricUpdate } from './performance-metric-update';

describe('PerformanceMetric Management Update Component', () => {
  let comp: PerformanceMetricUpdate;
  let fixture: ComponentFixture<PerformanceMetricUpdate>;
  let activatedRoute: ActivatedRoute;
  let performanceMetricFormService: PerformanceMetricFormService;
  let performanceMetricService: PerformanceMetricService;

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

    fixture = TestBed.createComponent(PerformanceMetricUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    performanceMetricFormService = TestBed.inject(PerformanceMetricFormService);
    performanceMetricService = TestBed.inject(PerformanceMetricService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const performanceMetric: IPerformanceMetric = { id: 23965 };

      activatedRoute.data = of({ performanceMetric });
      comp.ngOnInit();

      expect(comp.performanceMetric).toEqual(performanceMetric);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPerformanceMetric>>();
      const performanceMetric = { id: 7414 };
      jest.spyOn(performanceMetricFormService, 'getPerformanceMetric').mockReturnValue(performanceMetric);
      jest.spyOn(performanceMetricService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ performanceMetric });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: performanceMetric }));
      saveSubject.complete();

      // THEN
      expect(performanceMetricFormService.getPerformanceMetric).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(performanceMetricService.update).toHaveBeenCalledWith(expect.objectContaining(performanceMetric));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPerformanceMetric>>();
      const performanceMetric = { id: 7414 };
      jest.spyOn(performanceMetricFormService, 'getPerformanceMetric').mockReturnValue({ id: null });
      jest.spyOn(performanceMetricService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ performanceMetric: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: performanceMetric }));
      saveSubject.complete();

      // THEN
      expect(performanceMetricFormService.getPerformanceMetric).toHaveBeenCalled();
      expect(performanceMetricService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPerformanceMetric>>();
      const performanceMetric = { id: 7414 };
      jest.spyOn(performanceMetricService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ performanceMetric });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(performanceMetricService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
