import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../notification-preference.test-samples';

import { NotificationPreferenceFormService } from './notification-preference-form.service';

describe('NotificationPreference Form Service', () => {
  let service: NotificationPreferenceFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(NotificationPreferenceFormService);
  });

  describe('Service methods', () => {
    describe('createNotificationPreferenceFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createNotificationPreferenceFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            userId: expect.any(Object),
            emailEnabled: expect.any(Object),
            pushEnabled: expect.any(Object),
            inAppEnabled: expect.any(Object),
            notificationTypes: expect.any(Object),
            quietHoursStart: expect.any(Object),
            quietHoursEnd: expect.any(Object),
            frequency: expect.any(Object),
            lastModifiedDate: expect.any(Object),
          }),
        );
      });

      it('passing INotificationPreference should create a new form with FormGroup', () => {
        const formGroup = service.createNotificationPreferenceFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            userId: expect.any(Object),
            emailEnabled: expect.any(Object),
            pushEnabled: expect.any(Object),
            inAppEnabled: expect.any(Object),
            notificationTypes: expect.any(Object),
            quietHoursStart: expect.any(Object),
            quietHoursEnd: expect.any(Object),
            frequency: expect.any(Object),
            lastModifiedDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getNotificationPreference', () => {
      it('should return NewNotificationPreference for default NotificationPreference initial value', () => {
        const formGroup = service.createNotificationPreferenceFormGroup(sampleWithNewData);

        const notificationPreference = service.getNotificationPreference(formGroup) as any;

        expect(notificationPreference).toMatchObject(sampleWithNewData);
      });

      it('should return NewNotificationPreference for empty NotificationPreference initial value', () => {
        const formGroup = service.createNotificationPreferenceFormGroup();

        const notificationPreference = service.getNotificationPreference(formGroup) as any;

        expect(notificationPreference).toMatchObject({});
      });

      it('should return INotificationPreference', () => {
        const formGroup = service.createNotificationPreferenceFormGroup(sampleWithRequiredData);

        const notificationPreference = service.getNotificationPreference(formGroup) as any;

        expect(notificationPreference).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing INotificationPreference should not enable id FormControl', () => {
        const formGroup = service.createNotificationPreferenceFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewNotificationPreference should disable id FormControl', () => {
        const formGroup = service.createNotificationPreferenceFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
