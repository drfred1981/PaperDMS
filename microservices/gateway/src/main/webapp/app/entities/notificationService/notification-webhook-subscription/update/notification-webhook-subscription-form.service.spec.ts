import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../notification-webhook-subscription.test-samples';

import { NotificationWebhookSubscriptionFormService } from './notification-webhook-subscription-form.service';

describe('NotificationWebhookSubscription Form Service', () => {
  let service: NotificationWebhookSubscriptionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(NotificationWebhookSubscriptionFormService);
  });

  describe('Service methods', () => {
    describe('createNotificationWebhookSubscriptionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createNotificationWebhookSubscriptionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            url: expect.any(Object),
            secret: expect.any(Object),
            events: expect.any(Object),
            headers: expect.any(Object),
            isActive: expect.any(Object),
            retryCount: expect.any(Object),
            maxRetries: expect.any(Object),
            retryDelay: expect.any(Object),
            lastTriggerDate: expect.any(Object),
            lastSuccessDate: expect.any(Object),
            lastErrorMessage: expect.any(Object),
            failureCount: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedDate: expect.any(Object),
          }),
        );
      });

      it('passing INotificationWebhookSubscription should create a new form with FormGroup', () => {
        const formGroup = service.createNotificationWebhookSubscriptionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            url: expect.any(Object),
            secret: expect.any(Object),
            events: expect.any(Object),
            headers: expect.any(Object),
            isActive: expect.any(Object),
            retryCount: expect.any(Object),
            maxRetries: expect.any(Object),
            retryDelay: expect.any(Object),
            lastTriggerDate: expect.any(Object),
            lastSuccessDate: expect.any(Object),
            lastErrorMessage: expect.any(Object),
            failureCount: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getNotificationWebhookSubscription', () => {
      it('should return NewNotificationWebhookSubscription for default NotificationWebhookSubscription initial value', () => {
        const formGroup = service.createNotificationWebhookSubscriptionFormGroup(sampleWithNewData);

        const notificationWebhookSubscription = service.getNotificationWebhookSubscription(formGroup) as any;

        expect(notificationWebhookSubscription).toMatchObject(sampleWithNewData);
      });

      it('should return NewNotificationWebhookSubscription for empty NotificationWebhookSubscription initial value', () => {
        const formGroup = service.createNotificationWebhookSubscriptionFormGroup();

        const notificationWebhookSubscription = service.getNotificationWebhookSubscription(formGroup) as any;

        expect(notificationWebhookSubscription).toMatchObject({});
      });

      it('should return INotificationWebhookSubscription', () => {
        const formGroup = service.createNotificationWebhookSubscriptionFormGroup(sampleWithRequiredData);

        const notificationWebhookSubscription = service.getNotificationWebhookSubscription(formGroup) as any;

        expect(notificationWebhookSubscription).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing INotificationWebhookSubscription should not enable id FormControl', () => {
        const formGroup = service.createNotificationWebhookSubscriptionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewNotificationWebhookSubscription should disable id FormControl', () => {
        const formGroup = service.createNotificationWebhookSubscriptionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
