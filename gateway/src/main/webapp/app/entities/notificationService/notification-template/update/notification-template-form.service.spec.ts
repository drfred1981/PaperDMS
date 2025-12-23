import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../notification-template.test-samples';

import { NotificationTemplateFormService } from './notification-template-form.service';

describe('NotificationTemplate Form Service', () => {
  let service: NotificationTemplateFormService;

  beforeEach(() => {
    service = TestBed.inject(NotificationTemplateFormService);
  });

  describe('Service methods', () => {
    describe('createNotificationTemplateFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createNotificationTemplateFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            subject: expect.any(Object),
            bodyTemplate: expect.any(Object),
            type: expect.any(Object),
            channel: expect.any(Object),
            variables: expect.any(Object),
            isActive: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedDate: expect.any(Object),
          }),
        );
      });

      it('passing INotificationTemplate should create a new form with FormGroup', () => {
        const formGroup = service.createNotificationTemplateFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            subject: expect.any(Object),
            bodyTemplate: expect.any(Object),
            type: expect.any(Object),
            channel: expect.any(Object),
            variables: expect.any(Object),
            isActive: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getNotificationTemplate', () => {
      it('should return NewNotificationTemplate for default NotificationTemplate initial value', () => {
        const formGroup = service.createNotificationTemplateFormGroup(sampleWithNewData);

        const notificationTemplate = service.getNotificationTemplate(formGroup);

        expect(notificationTemplate).toMatchObject(sampleWithNewData);
      });

      it('should return NewNotificationTemplate for empty NotificationTemplate initial value', () => {
        const formGroup = service.createNotificationTemplateFormGroup();

        const notificationTemplate = service.getNotificationTemplate(formGroup);

        expect(notificationTemplate).toMatchObject({});
      });

      it('should return INotificationTemplate', () => {
        const formGroup = service.createNotificationTemplateFormGroup(sampleWithRequiredData);

        const notificationTemplate = service.getNotificationTemplate(formGroup);

        expect(notificationTemplate).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing INotificationTemplate should not enable id FormControl', () => {
        const formGroup = service.createNotificationTemplateFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewNotificationTemplate should disable id FormControl', () => {
        const formGroup = service.createNotificationTemplateFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
