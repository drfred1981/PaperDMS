import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { NotificationWebhookSubscriptionService } from '../service/notification-webhook-subscription.service';
import { INotificationWebhookSubscription } from '../notification-webhook-subscription.model';
import { NotificationWebhookSubscriptionFormService } from './notification-webhook-subscription-form.service';

import { NotificationWebhookSubscriptionUpdateComponent } from './notification-webhook-subscription-update.component';

describe('NotificationWebhookSubscription Management Update Component', () => {
  let comp: NotificationWebhookSubscriptionUpdateComponent;
  let fixture: ComponentFixture<NotificationWebhookSubscriptionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let notificationWebhookSubscriptionFormService: NotificationWebhookSubscriptionFormService;
  let notificationWebhookSubscriptionService: NotificationWebhookSubscriptionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [NotificationWebhookSubscriptionUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(NotificationWebhookSubscriptionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(NotificationWebhookSubscriptionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    notificationWebhookSubscriptionFormService = TestBed.inject(NotificationWebhookSubscriptionFormService);
    notificationWebhookSubscriptionService = TestBed.inject(NotificationWebhookSubscriptionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const notificationWebhookSubscription: INotificationWebhookSubscription = { id: 13817 };

      activatedRoute.data = of({ notificationWebhookSubscription });
      comp.ngOnInit();

      expect(comp.notificationWebhookSubscription).toEqual(notificationWebhookSubscription);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<INotificationWebhookSubscription>>();
      const notificationWebhookSubscription = { id: 19116 };
      jest
        .spyOn(notificationWebhookSubscriptionFormService, 'getNotificationWebhookSubscription')
        .mockReturnValue(notificationWebhookSubscription);
      jest.spyOn(notificationWebhookSubscriptionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ notificationWebhookSubscription });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: notificationWebhookSubscription }));
      saveSubject.complete();

      // THEN
      expect(notificationWebhookSubscriptionFormService.getNotificationWebhookSubscription).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(notificationWebhookSubscriptionService.update).toHaveBeenCalledWith(expect.objectContaining(notificationWebhookSubscription));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<INotificationWebhookSubscription>>();
      const notificationWebhookSubscription = { id: 19116 };
      jest.spyOn(notificationWebhookSubscriptionFormService, 'getNotificationWebhookSubscription').mockReturnValue({ id: null });
      jest.spyOn(notificationWebhookSubscriptionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ notificationWebhookSubscription: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: notificationWebhookSubscription }));
      saveSubject.complete();

      // THEN
      expect(notificationWebhookSubscriptionFormService.getNotificationWebhookSubscription).toHaveBeenCalled();
      expect(notificationWebhookSubscriptionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<INotificationWebhookSubscription>>();
      const notificationWebhookSubscription = { id: 19116 };
      jest.spyOn(notificationWebhookSubscriptionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ notificationWebhookSubscription });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(notificationWebhookSubscriptionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
