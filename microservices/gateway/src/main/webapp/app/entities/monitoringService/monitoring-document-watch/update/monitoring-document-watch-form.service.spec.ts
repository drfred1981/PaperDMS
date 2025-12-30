import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../monitoring-document-watch.test-samples';

import { MonitoringDocumentWatchFormService } from './monitoring-document-watch-form.service';

describe('MonitoringDocumentWatch Form Service', () => {
  let service: MonitoringDocumentWatchFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MonitoringDocumentWatchFormService);
  });

  describe('Service methods', () => {
    describe('createMonitoringDocumentWatchFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMonitoringDocumentWatchFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentSha256: expect.any(Object),
            userId: expect.any(Object),
            watchType: expect.any(Object),
            notifyOnView: expect.any(Object),
            notifyOnDownload: expect.any(Object),
            notifyOnModify: expect.any(Object),
            notifyOnShare: expect.any(Object),
            notifyOnDelete: expect.any(Object),
            createdDate: expect.any(Object),
          }),
        );
      });

      it('passing IMonitoringDocumentWatch should create a new form with FormGroup', () => {
        const formGroup = service.createMonitoringDocumentWatchFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentSha256: expect.any(Object),
            userId: expect.any(Object),
            watchType: expect.any(Object),
            notifyOnView: expect.any(Object),
            notifyOnDownload: expect.any(Object),
            notifyOnModify: expect.any(Object),
            notifyOnShare: expect.any(Object),
            notifyOnDelete: expect.any(Object),
            createdDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getMonitoringDocumentWatch', () => {
      it('should return NewMonitoringDocumentWatch for default MonitoringDocumentWatch initial value', () => {
        const formGroup = service.createMonitoringDocumentWatchFormGroup(sampleWithNewData);

        const monitoringDocumentWatch = service.getMonitoringDocumentWatch(formGroup) as any;

        expect(monitoringDocumentWatch).toMatchObject(sampleWithNewData);
      });

      it('should return NewMonitoringDocumentWatch for empty MonitoringDocumentWatch initial value', () => {
        const formGroup = service.createMonitoringDocumentWatchFormGroup();

        const monitoringDocumentWatch = service.getMonitoringDocumentWatch(formGroup) as any;

        expect(monitoringDocumentWatch).toMatchObject({});
      });

      it('should return IMonitoringDocumentWatch', () => {
        const formGroup = service.createMonitoringDocumentWatchFormGroup(sampleWithRequiredData);

        const monitoringDocumentWatch = service.getMonitoringDocumentWatch(formGroup) as any;

        expect(monitoringDocumentWatch).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMonitoringDocumentWatch should not enable id FormControl', () => {
        const formGroup = service.createMonitoringDocumentWatchFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMonitoringDocumentWatch should disable id FormControl', () => {
        const formGroup = service.createMonitoringDocumentWatchFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
