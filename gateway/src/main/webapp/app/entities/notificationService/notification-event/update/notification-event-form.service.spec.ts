import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../notification-event.test-samples';

import { NotificationEventFormService } from './notification-event-form.service';

describe('NotificationEvent Form Service', () => {
  let service: NotificationEventFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(NotificationEventFormService);
  });

  describe('Service methods', () => {
    describe('createNotificationEventFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createNotificationEventFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            eventType: expect.any(Object),
            entityType: expect.any(Object),
            entityId: expect.any(Object),
            userId: expect.any(Object),
            eventData: expect.any(Object),
            eventDate: expect.any(Object),
            processed: expect.any(Object),
            processedDate: expect.any(Object),
          }),
        );
      });

      it('passing INotificationEvent should create a new form with FormGroup', () => {
        const formGroup = service.createNotificationEventFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            eventType: expect.any(Object),
            entityType: expect.any(Object),
            entityId: expect.any(Object),
            userId: expect.any(Object),
            eventData: expect.any(Object),
            eventDate: expect.any(Object),
            processed: expect.any(Object),
            processedDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getNotificationEvent', () => {
      it('should return NewNotificationEvent for default NotificationEvent initial value', () => {
        const formGroup = service.createNotificationEventFormGroup(sampleWithNewData);

        const notificationEvent = service.getNotificationEvent(formGroup) as any;

        expect(notificationEvent).toMatchObject(sampleWithNewData);
      });

      it('should return NewNotificationEvent for empty NotificationEvent initial value', () => {
        const formGroup = service.createNotificationEventFormGroup();

        const notificationEvent = service.getNotificationEvent(formGroup) as any;

        expect(notificationEvent).toMatchObject({});
      });

      it('should return INotificationEvent', () => {
        const formGroup = service.createNotificationEventFormGroup(sampleWithRequiredData);

        const notificationEvent = service.getNotificationEvent(formGroup) as any;

        expect(notificationEvent).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing INotificationEvent should not enable id FormControl', () => {
        const formGroup = service.createNotificationEventFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewNotificationEvent should disable id FormControl', () => {
        const formGroup = service.createNotificationEventFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
