import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../webhook-log.test-samples';

import { WebhookLogFormService } from './webhook-log-form.service';

describe('WebhookLog Form Service', () => {
  let service: WebhookLogFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WebhookLogFormService);
  });

  describe('Service methods', () => {
    describe('createWebhookLogFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createWebhookLogFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            subscriptionId: expect.any(Object),
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

      it('passing IWebhookLog should create a new form with FormGroup', () => {
        const formGroup = service.createWebhookLogFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            subscriptionId: expect.any(Object),
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

    describe('getWebhookLog', () => {
      it('should return NewWebhookLog for default WebhookLog initial value', () => {
        const formGroup = service.createWebhookLogFormGroup(sampleWithNewData);

        const webhookLog = service.getWebhookLog(formGroup) as any;

        expect(webhookLog).toMatchObject(sampleWithNewData);
      });

      it('should return NewWebhookLog for empty WebhookLog initial value', () => {
        const formGroup = service.createWebhookLogFormGroup();

        const webhookLog = service.getWebhookLog(formGroup) as any;

        expect(webhookLog).toMatchObject({});
      });

      it('should return IWebhookLog', () => {
        const formGroup = service.createWebhookLogFormGroup(sampleWithRequiredData);

        const webhookLog = service.getWebhookLog(formGroup) as any;

        expect(webhookLog).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IWebhookLog should not enable id FormControl', () => {
        const formGroup = service.createWebhookLogFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewWebhookLog should disable id FormControl', () => {
        const formGroup = service.createWebhookLogFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
