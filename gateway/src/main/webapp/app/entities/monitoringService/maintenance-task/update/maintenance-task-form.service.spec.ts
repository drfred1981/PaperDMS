import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../maintenance-task.test-samples';

import { MaintenanceTaskFormService } from './maintenance-task-form.service';

describe('MaintenanceTask Form Service', () => {
  let service: MaintenanceTaskFormService;

  beforeEach(() => {
    service = TestBed.inject(MaintenanceTaskFormService);
  });

  describe('Service methods', () => {
    describe('createMaintenanceTaskFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMaintenanceTaskFormGroup();

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

      it('passing IMaintenanceTask should create a new form with FormGroup', () => {
        const formGroup = service.createMaintenanceTaskFormGroup(sampleWithRequiredData);

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

    describe('getMaintenanceTask', () => {
      it('should return NewMaintenanceTask for default MaintenanceTask initial value', () => {
        const formGroup = service.createMaintenanceTaskFormGroup(sampleWithNewData);

        const maintenanceTask = service.getMaintenanceTask(formGroup);

        expect(maintenanceTask).toMatchObject(sampleWithNewData);
      });

      it('should return NewMaintenanceTask for empty MaintenanceTask initial value', () => {
        const formGroup = service.createMaintenanceTaskFormGroup();

        const maintenanceTask = service.getMaintenanceTask(formGroup);

        expect(maintenanceTask).toMatchObject({});
      });

      it('should return IMaintenanceTask', () => {
        const formGroup = service.createMaintenanceTaskFormGroup(sampleWithRequiredData);

        const maintenanceTask = service.getMaintenanceTask(formGroup);

        expect(maintenanceTask).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMaintenanceTask should not enable id FormControl', () => {
        const formGroup = service.createMaintenanceTaskFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMaintenanceTask should disable id FormControl', () => {
        const formGroup = service.createMaintenanceTaskFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
