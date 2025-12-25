import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { PerformanceMetricService } from '../service/performance-metric.service';
import { IPerformanceMetric } from '../performance-metric.model';
import { PerformanceMetricFormService } from './performance-metric-form.service';

import { PerformanceMetricUpdateComponent } from './performance-metric-update.component';

describe('PerformanceMetric Management Update Component', () => {
  let comp: PerformanceMetricUpdateComponent;
  let fixture: ComponentFixture<PerformanceMetricUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let performanceMetricFormService: PerformanceMetricFormService;
  let performanceMetricService: PerformanceMetricService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [PerformanceMetricUpdateComponent],
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
      .overrideTemplate(PerformanceMetricUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PerformanceMetricUpdateComponent);
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
