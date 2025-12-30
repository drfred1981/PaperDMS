import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../monitoring-maintenance-task.test-samples';

import { MonitoringMaintenanceTaskFormService } from './monitoring-maintenance-task-form.service';

describe('MonitoringMaintenanceTask Form Service', () => {
  let service: MonitoringMaintenanceTaskFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MonitoringMaintenanceTaskFormService);
  });

  describe('Service methods', () => {
    describe('createMonitoringMaintenanceTaskFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMonitoringMaintenanceTaskFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            taskType: expect.any(Object),
            schedule: expect.any(Object),
            status: expect.any(Object),
            isActive: expect.any(Object),
            lastRun: expect.any(Object),
            nextRun: expect.any(Object),
            duration: expect.any(Object),
            recordsProcessed: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
          }),
        );
      });

      it('passing IMonitoringMaintenanceTask should create a new form with FormGroup', () => {
        const formGroup = service.createMonitoringMaintenanceTaskFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            taskType: expect.any(Object),
            schedule: expect.any(Object),
            status: expect.any(Object),
            isActive: expect.any(Object),
            lastRun: expect.any(Object),
            nextRun: expect.any(Object),
            duration: expect.any(Object),
            recordsProcessed: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getMonitoringMaintenanceTask', () => {
      it('should return NewMonitoringMaintenanceTask for default MonitoringMaintenanceTask initial value', () => {
        const formGroup = service.createMonitoringMaintenanceTaskFormGroup(sampleWithNewData);

        const monitoringMaintenanceTask = service.getMonitoringMaintenanceTask(formGroup) as any;

        expect(monitoringMaintenanceTask).toMatchObject(sampleWithNewData);
      });

      it('should return NewMonitoringMaintenanceTask for empty MonitoringMaintenanceTask initial value', () => {
        const formGroup = service.createMonitoringMaintenanceTaskFormGroup();

        const monitoringMaintenanceTask = service.getMonitoringMaintenanceTask(formGroup) as any;

        expect(monitoringMaintenanceTask).toMatchObject({});
      });

      it('should return IMonitoringMaintenanceTask', () => {
        const formGroup = service.createMonitoringMaintenanceTaskFormGroup(sampleWithRequiredData);

        const monitoringMaintenanceTask = service.getMonitoringMaintenanceTask(formGroup) as any;

        expect(monitoringMaintenanceTask).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMonitoringMaintenanceTask should not enable id FormControl', () => {
        const formGroup = service.createMonitoringMaintenanceTaskFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMonitoringMaintenanceTask should disable id FormControl', () => {
        const formGroup = service.createMonitoringMaintenanceTaskFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
