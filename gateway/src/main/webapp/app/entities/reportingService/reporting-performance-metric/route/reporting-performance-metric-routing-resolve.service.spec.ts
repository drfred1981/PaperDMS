import { TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { ActivatedRoute, ActivatedRouteSnapshot, Router, convertToParamMap } from '@angular/router';
import { of } from 'rxjs';

import { IReportingPerformanceMetric } from '../reporting-performance-metric.model';
import { ReportingPerformanceMetricService } from '../service/reporting-performance-metric.service';

import reportingPerformanceMetricResolve from './reporting-performance-metric-routing-resolve.service';

describe('ReportingPerformanceMetric routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: ReportingPerformanceMetricService;
  let resultReportingPerformanceMetric: IReportingPerformanceMetric | null | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    service = TestBed.inject(ReportingPerformanceMetricService);
    resultReportingPerformanceMetric = undefined;
  });

  describe('resolve', () => {
    it('should return IReportingPerformanceMetric returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        reportingPerformanceMetricResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultReportingPerformanceMetric = result;
          },
        });
      });

      // THEN
      expect(service.find).toHaveBeenCalledWith(123);
      expect(resultReportingPerformanceMetric).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      TestBed.runInInjectionContext(() => {
        reportingPerformanceMetricResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultReportingPerformanceMetric = result;
          },
        });
      });

      // THEN
      expect(service.find).not.toHaveBeenCalled();
      expect(resultReportingPerformanceMetric).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IReportingPerformanceMetric>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        reportingPerformanceMetricResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultReportingPerformanceMetric = result;
          },
        });
      });

      // THEN
      expect(service.find).toHaveBeenCalledWith(123);
      expect(resultReportingPerformanceMetric).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
