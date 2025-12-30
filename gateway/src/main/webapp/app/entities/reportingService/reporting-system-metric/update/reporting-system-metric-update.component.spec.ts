import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ReportingSystemMetricService } from '../service/reporting-system-metric.service';
import { IReportingSystemMetric } from '../reporting-system-metric.model';
import { ReportingSystemMetricFormService } from './reporting-system-metric-form.service';

import { ReportingSystemMetricUpdateComponent } from './reporting-system-metric-update.component';

describe('ReportingSystemMetric Management Update Component', () => {
  let comp: ReportingSystemMetricUpdateComponent;
  let fixture: ComponentFixture<ReportingSystemMetricUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let reportingSystemMetricFormService: ReportingSystemMetricFormService;
  let reportingSystemMetricService: ReportingSystemMetricService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ReportingSystemMetricUpdateComponent],
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
      .overrideTemplate(ReportingSystemMetricUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ReportingSystemMetricUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    reportingSystemMetricFormService = TestBed.inject(ReportingSystemMetricFormService);
    reportingSystemMetricService = TestBed.inject(ReportingSystemMetricService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const reportingSystemMetric: IReportingSystemMetric = { id: 3362 };

      activatedRoute.data = of({ reportingSystemMetric });
      comp.ngOnInit();

      expect(comp.reportingSystemMetric).toEqual(reportingSystemMetric);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReportingSystemMetric>>();
      const reportingSystemMetric = { id: 23948 };
      jest.spyOn(reportingSystemMetricFormService, 'getReportingSystemMetric').mockReturnValue(reportingSystemMetric);
      jest.spyOn(reportingSystemMetricService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reportingSystemMetric });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reportingSystemMetric }));
      saveSubject.complete();

      // THEN
      expect(reportingSystemMetricFormService.getReportingSystemMetric).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(reportingSystemMetricService.update).toHaveBeenCalledWith(expect.objectContaining(reportingSystemMetric));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReportingSystemMetric>>();
      const reportingSystemMetric = { id: 23948 };
      jest.spyOn(reportingSystemMetricFormService, 'getReportingSystemMetric').mockReturnValue({ id: null });
      jest.spyOn(reportingSystemMetricService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reportingSystemMetric: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reportingSystemMetric }));
      saveSubject.complete();

      // THEN
      expect(reportingSystemMetricFormService.getReportingSystemMetric).toHaveBeenCalled();
      expect(reportingSystemMetricService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReportingSystemMetric>>();
      const reportingSystemMetric = { id: 23948 };
      jest.spyOn(reportingSystemMetricService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reportingSystemMetric });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(reportingSystemMetricService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
