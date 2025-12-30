import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../notification-webhook-log.test-samples';

import { NotificationWebhookLogFormService } from './notification-webhook-log-form.service';

describe('NotificationWebhookLog Form Service', () => {
  let service: NotificationWebhookLogFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(NotificationWebhookLogFormService);
  });

  describe('Service methods', () => {
    describe('createNotificationWebhookLogFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createNotificationWebhookLogFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            eventType: expect.any(Object),
            payload: expect.any(Object),
            responseStatus: expect.any(Object),
            responseBody: expect.any(Object),
            responseTime: expect.any(Object),
            attemptNumber: expect.any(Object),
            isSuccess: expect.any(Object),
            errorMessage: expect.any(Object),
            sentDate: expect.any(Object),
            subscription: expect.any(Object),
          }),
        );
      });

      it('passing INotificationWebhookLog should create a new form with FormGroup', () => {
        const formGroup = service.createNotificationWebhookLogFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            eventType: expect.any(Object),
            payload: expect.any(Object),
            responseStatus: expect.any(Object),
            responseBody: expect.any(Object),
            responseTime: expect.any(Object),
            attemptNumber: expect.any(Object),
            isSuccess: expect.any(Object),
            errorMessage: expect.any(Object),
            sentDate: expect.any(Object),
            subscription: expect.any(Object),
          }),
        );
      });
    });

    describe('getNotificationWebhookLog', () => {
      it('should return NewNotificationWebhookLog for default NotificationWebhookLog initial value', () => {
        const formGroup = service.createNotificationWebhookLogFormGroup(sampleWithNewData);

        const notificationWebhookLog = service.getNotificationWebhookLog(formGroup) as any;

        expect(notificationWebhookLog).toMatchObject(sampleWithNewData);
      });

      it('should return NewNotificationWebhookLog for empty NotificationWebhookLog initial value', () => {
        const formGroup = service.createNotificationWebhookLogFormGroup();

        const notificationWebhookLog = service.getNotificationWebhookLog(formGroup) as any;

        expect(notificationWebhookLog).toMatchObject({});
      });

      it('should return INotificationWebhookLog', () => {
        const formGroup = service.createNotificationWebhookLogFormGroup(sampleWithRequiredData);

        const notificationWebhookLog = service.getNotificationWebhookLog(formGroup) as any;

        expect(notificationWebhookLog).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing INotificationWebhookLog should not enable id FormControl', () => {
        const formGroup = service.createNotificationWebhookLogFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewNotificationWebhookLog should disable id FormControl', () => {
        const formGroup = service.createNotificationWebhookLogFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
