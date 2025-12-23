import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../webhook-subscription.test-samples';

import { WebhookSubscriptionFormService } from './webhook-subscription-form.service';

describe('WebhookSubscription Form Service', () => {
  let service: WebhookSubscriptionFormService;

  beforeEach(() => {
    service = TestBed.inject(WebhookSubscriptionFormService);
  });

  describe('Service methods', () => {
    describe('createWebhookSubscriptionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createWebhookSubscriptionFormGroup();

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

      it('passing IWebhookSubscription should create a new form with FormGroup', () => {
        const formGroup = service.createWebhookSubscriptionFormGroup(sampleWithRequiredData);

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

    describe('getWebhookSubscription', () => {
      it('should return NewWebhookSubscription for default WebhookSubscription initial value', () => {
        const formGroup = service.createWebhookSubscriptionFormGroup(sampleWithNewData);

        const webhookSubscription = service.getWebhookSubscription(formGroup);

        expect(webhookSubscription).toMatchObject(sampleWithNewData);
      });

      it('should return NewWebhookSubscription for empty WebhookSubscription initial value', () => {
        const formGroup = service.createWebhookSubscriptionFormGroup();

        const webhookSubscription = service.getWebhookSubscription(formGroup);

        expect(webhookSubscription).toMatchObject({});
      });

      it('should return IWebhookSubscription', () => {
        const formGroup = service.createWebhookSubscriptionFormGroup(sampleWithRequiredData);

        const webhookSubscription = service.getWebhookSubscription(formGroup);

        expect(webhookSubscription).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IWebhookSubscription should not enable id FormControl', () => {
        const formGroup = service.createWebhookSubscriptionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewWebhookSubscription should disable id FormControl', () => {
        const formGroup = service.createWebhookSubscriptionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
