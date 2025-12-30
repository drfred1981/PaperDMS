import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { MonitoringMaintenanceTaskService } from '../service/monitoring-maintenance-task.service';
import { IMonitoringMaintenanceTask } from '../monitoring-maintenance-task.model';
import { MonitoringMaintenanceTaskFormService } from './monitoring-maintenance-task-form.service';

import { MonitoringMaintenanceTaskUpdateComponent } from './monitoring-maintenance-task-update.component';

describe('MonitoringMaintenanceTask Management Update Component', () => {
  let comp: MonitoringMaintenanceTaskUpdateComponent;
  let fixture: ComponentFixture<MonitoringMaintenanceTaskUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let monitoringMaintenanceTaskFormService: MonitoringMaintenanceTaskFormService;
  let monitoringMaintenanceTaskService: MonitoringMaintenanceTaskService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MonitoringMaintenanceTaskUpdateComponent],
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
      .overrideTemplate(MonitoringMaintenanceTaskUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MonitoringMaintenanceTaskUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    monitoringMaintenanceTaskFormService = TestBed.inject(MonitoringMaintenanceTaskFormService);
    monitoringMaintenanceTaskService = TestBed.inject(MonitoringMaintenanceTaskService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const monitoringMaintenanceTask: IMonitoringMaintenanceTask = { id: 12967 };

      activatedRoute.data = of({ monitoringMaintenanceTask });
      comp.ngOnInit();

      expect(comp.monitoringMaintenanceTask).toEqual(monitoringMaintenanceTask);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMonitoringMaintenanceTask>>();
      const monitoringMaintenanceTask = { id: 25382 };
      jest.spyOn(monitoringMaintenanceTaskFormService, 'getMonitoringMaintenanceTask').mockReturnValue(monitoringMaintenanceTask);
      jest.spyOn(monitoringMaintenanceTaskService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ monitoringMaintenanceTask });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: monitoringMaintenanceTask }));
      saveSubject.complete();

      // THEN
      expect(monitoringMaintenanceTaskFormService.getMonitoringMaintenanceTask).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(monitoringMaintenanceTaskService.update).toHaveBeenCalledWith(expect.objectContaining(monitoringMaintenanceTask));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMonitoringMaintenanceTask>>();
      const monitoringMaintenanceTask = { id: 25382 };
      jest.spyOn(monitoringMaintenanceTaskFormService, 'getMonitoringMaintenanceTask').mockReturnValue({ id: null });
      jest.spyOn(monitoringMaintenanceTaskService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ monitoringMaintenanceTask: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: monitoringMaintenanceTask }));
      saveSubject.complete();

      // THEN
      expect(monitoringMaintenanceTaskFormService.getMonitoringMaintenanceTask).toHaveBeenCalled();
      expect(monitoringMaintenanceTaskService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMonitoringMaintenanceTask>>();
      const monitoringMaintenanceTask = { id: 25382 };
      jest.spyOn(monitoringMaintenanceTaskService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ monitoringMaintenanceTask });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(monitoringMaintenanceTaskService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
