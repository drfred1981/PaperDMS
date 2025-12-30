import { TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { ActivatedRoute, ActivatedRouteSnapshot, Router, convertToParamMap } from '@angular/router';
import { of } from 'rxjs';

import { IMonitoringMaintenanceTask } from '../monitoring-maintenance-task.model';
import { MonitoringMaintenanceTaskService } from '../service/monitoring-maintenance-task.service';

import monitoringMaintenanceTaskResolve from './monitoring-maintenance-task-routing-resolve.service';

describe('MonitoringMaintenanceTask routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: MonitoringMaintenanceTaskService;
  let resultMonitoringMaintenanceTask: IMonitoringMaintenanceTask | null | undefined;

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
    service = TestBed.inject(MonitoringMaintenanceTaskService);
    resultMonitoringMaintenanceTask = undefined;
  });

  describe('resolve', () => {
    it('should return IMonitoringMaintenanceTask returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        monitoringMaintenanceTaskResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultMonitoringMaintenanceTask = result;
          },
        });
      });

      // THEN
      expect(service.find).toHaveBeenCalledWith(123);
      expect(resultMonitoringMaintenanceTask).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      TestBed.runInInjectionContext(() => {
        monitoringMaintenanceTaskResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultMonitoringMaintenanceTask = result;
          },
        });
      });

      // THEN
      expect(service.find).not.toHaveBeenCalled();
      expect(resultMonitoringMaintenanceTask).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IMonitoringMaintenanceTask>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        monitoringMaintenanceTaskResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultMonitoringMaintenanceTask = result;
          },
        });
      });

      // THEN
      expect(service.find).toHaveBeenCalledWith(123);
      expect(resultMonitoringMaintenanceTask).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
