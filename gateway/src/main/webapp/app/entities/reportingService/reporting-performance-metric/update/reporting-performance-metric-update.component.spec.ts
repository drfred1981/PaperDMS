import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ReportingPerformanceMetricService } from '../service/reporting-performance-metric.service';
import { IReportingPerformanceMetric } from '../reporting-performance-metric.model';
import { ReportingPerformanceMetricFormService } from './reporting-performance-metric-form.service';

import { ReportingPerformanceMetricUpdateComponent } from './reporting-performance-metric-update.component';

describe('ReportingPerformanceMetric Management Update Component', () => {
  let comp: ReportingPerformanceMetricUpdateComponent;
  let fixture: ComponentFixture<ReportingPerformanceMetricUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let reportingPerformanceMetricFormService: ReportingPerformanceMetricFormService;
  let reportingPerformanceMetricService: ReportingPerformanceMetricService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ReportingPerformanceMetricUpdateComponent],
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
      .overrideTemplate(ReportingPerformanceMetricUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ReportingPerformanceMetricUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    reportingPerformanceMetricFormService = TestBed.inject(ReportingPerformanceMetricFormService);
    reportingPerformanceMetricService = TestBed.inject(ReportingPerformanceMetricService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const reportingPerformanceMetric: IReportingPerformanceMetric = { id: 15464 };

      activatedRoute.data = of({ reportingPerformanceMetric });
      comp.ngOnInit();

      expect(comp.reportingPerformanceMetric).toEqual(reportingPerformanceMetric);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReportingPerformanceMetric>>();
      const reportingPerformanceMetric = { id: 32088 };
      jest.spyOn(reportingPerformanceMetricFormService, 'getReportingPerformanceMetric').mockReturnValue(reportingPerformanceMetric);
      jest.spyOn(reportingPerformanceMetricService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reportingPerformanceMetric });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reportingPerformanceMetric }));
      saveSubject.complete();

      // THEN
      expect(reportingPerformanceMetricFormService.getReportingPerformanceMetric).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(reportingPerformanceMetricService.update).toHaveBeenCalledWith(expect.objectContaining(reportingPerformanceMetric));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReportingPerformanceMetric>>();
      const reportingPerformanceMetric = { id: 32088 };
      jest.spyOn(reportingPerformanceMetricFormService, 'getReportingPerformanceMetric').mockReturnValue({ id: null });
      jest.spyOn(reportingPerformanceMetricService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reportingPerformanceMetric: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reportingPerformanceMetric }));
      saveSubject.complete();

      // THEN
      expect(reportingPerformanceMetricFormService.getReportingPerformanceMetric).toHaveBeenCalled();
      expect(reportingPerformanceMetricService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReportingPerformanceMetric>>();
      const reportingPerformanceMetric = { id: 32088 };
      jest.spyOn(reportingPerformanceMetricService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reportingPerformanceMetric });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(reportingPerformanceMetricService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
