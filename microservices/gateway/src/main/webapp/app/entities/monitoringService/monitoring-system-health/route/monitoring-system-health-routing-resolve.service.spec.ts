import { TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { ActivatedRoute, ActivatedRouteSnapshot, Router, convertToParamMap } from '@angular/router';
import { of } from 'rxjs';

import { IMonitoringSystemHealth } from '../monitoring-system-health.model';
import { MonitoringSystemHealthService } from '../service/monitoring-system-health.service';

import monitoringSystemHealthResolve from './monitoring-system-health-routing-resolve.service';

describe('MonitoringSystemHealth routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: MonitoringSystemHealthService;
  let resultMonitoringSystemHealth: IMonitoringSystemHealth | null | undefined;

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
    service = TestBed.inject(MonitoringSystemHealthService);
    resultMonitoringSystemHealth = undefined;
  });

  describe('resolve', () => {
    it('should return IMonitoringSystemHealth returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        monitoringSystemHealthResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultMonitoringSystemHealth = result;
          },
        });
      });

      // THEN
      expect(service.find).toHaveBeenCalledWith(123);
      expect(resultMonitoringSystemHealth).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      TestBed.runInInjectionContext(() => {
        monitoringSystemHealthResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultMonitoringSystemHealth = result;
          },
        });
      });

      // THEN
      expect(service.find).not.toHaveBeenCalled();
      expect(resultMonitoringSystemHealth).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IMonitoringSystemHealth>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        monitoringSystemHealthResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultMonitoringSystemHealth = result;
          },
        });
      });

      // THEN
      expect(service.find).toHaveBeenCalledWith(123);
      expect(resultMonitoringSystemHealth).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
