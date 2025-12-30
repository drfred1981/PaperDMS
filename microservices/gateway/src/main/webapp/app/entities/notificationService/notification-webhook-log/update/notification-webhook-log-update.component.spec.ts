import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { INotificationWebhookSubscription } from 'app/entities/notificationService/notification-webhook-subscription/notification-webhook-subscription.model';
import { NotificationWebhookSubscriptionService } from 'app/entities/notificationService/notification-webhook-subscription/service/notification-webhook-subscription.service';
import { NotificationWebhookLogService } from '../service/notification-webhook-log.service';
import { INotificationWebhookLog } from '../notification-webhook-log.model';
import { NotificationWebhookLogFormService } from './notification-webhook-log-form.service';

import { NotificationWebhookLogUpdateComponent } from './notification-webhook-log-update.component';

describe('NotificationWebhookLog Management Update Component', () => {
  let comp: NotificationWebhookLogUpdateComponent;
  let fixture: ComponentFixture<NotificationWebhookLogUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let notificationWebhookLogFormService: NotificationWebhookLogFormService;
  let notificationWebhookLogService: NotificationWebhookLogService;
  let notificationWebhookSubscriptionService: NotificationWebhookSubscriptionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [NotificationWebhookLogUpdateComponent],
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
      .overrideTemplate(NotificationWebhookLogUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(NotificationWebhookLogUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    notificationWebhookLogFormService = TestBed.inject(NotificationWebhookLogFormService);
    notificationWebhookLogService = TestBed.inject(NotificationWebhookLogService);
    notificationWebhookSubscriptionService = TestBed.inject(NotificationWebhookSubscriptionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call NotificationWebhookSubscription query and add missing value', () => {
      const notificationWebhookLog: INotificationWebhookLog = { id: 7958 };
      const subscription: INotificationWebhookSubscription = { id: 19116 };
      notificationWebhookLog.subscription = subscription;

      const notificationWebhookSubscriptionCollection: INotificationWebhookSubscription[] = [{ id: 19116 }];
      jest
        .spyOn(notificationWebhookSubscriptionService, 'query')
        .mockReturnValue(of(new HttpResponse({ body: notificationWebhookSubscriptionCollection })));
      const additionalNotificationWebhookSubscriptions = [subscription];
      const expectedCollection: INotificationWebhookSubscription[] = [
        ...additionalNotificationWebhookSubscriptions,
        ...notificationWebhookSubscriptionCollection,
      ];
      jest
        .spyOn(notificationWebhookSubscriptionService, 'addNotificationWebhookSubscriptionToCollectionIfMissing')
        .mockReturnValue(expectedCollection);

      activatedRoute.data = of({ notificationWebhookLog });
      comp.ngOnInit();

      expect(notificationWebhookSubscriptionService.query).toHaveBeenCalled();
      expect(notificationWebhookSubscriptionService.addNotificationWebhookSubscriptionToCollectionIfMissing).toHaveBeenCalledWith(
        notificationWebhookSubscriptionCollection,
        ...additionalNotificationWebhookSubscriptions.map(expect.objectContaining),
      );
      expect(comp.notificationWebhookSubscriptionsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const notificationWebhookLog: INotificationWebhookLog = { id: 7958 };
      const subscription: INotificationWebhookSubscription = { id: 19116 };
      notificationWebhookLog.subscription = subscription;

      activatedRoute.data = of({ notificationWebhookLog });
      comp.ngOnInit();

      expect(comp.notificationWebhookSubscriptionsSharedCollection).toContainEqual(subscription);
      expect(comp.notificationWebhookLog).toEqual(notificationWebhookLog);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<INotificationWebhookLog>>();
      const notificationWebhookLog = { id: 9697 };
      jest.spyOn(notificationWebhookLogFormService, 'getNotificationWebhookLog').mockReturnValue(notificationWebhookLog);
      jest.spyOn(notificationWebhookLogService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ notificationWebhookLog });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: notificationWebhookLog }));
      saveSubject.complete();

      // THEN
      expect(notificationWebhookLogFormService.getNotificationWebhookLog).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(notificationWebhookLogService.update).toHaveBeenCalledWith(expect.objectContaining(notificationWebhookLog));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<INotificationWebhookLog>>();
      const notificationWebhookLog = { id: 9697 };
      jest.spyOn(notificationWebhookLogFormService, 'getNotificationWebhookLog').mockReturnValue({ id: null });
      jest.spyOn(notificationWebhookLogService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ notificationWebhookLog: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: notificationWebhookLog }));
      saveSubject.complete();

      // THEN
      expect(notificationWebhookLogFormService.getNotificationWebhookLog).toHaveBeenCalled();
      expect(notificationWebhookLogService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<INotificationWebhookLog>>();
      const notificationWebhookLog = { id: 9697 };
      jest.spyOn(notificationWebhookLogService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ notificationWebhookLog });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(notificationWebhookLogService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareNotificationWebhookSubscription', () => {
      it('should forward to notificationWebhookSubscriptionService', () => {
        const entity = { id: 19116 };
        const entity2 = { id: 13817 };
        jest.spyOn(notificationWebhookSubscriptionService, 'compareNotificationWebhookSubscription');
        comp.compareNotificationWebhookSubscription(entity, entity2);
        expect(notificationWebhookSubscriptionService.compareNotificationWebhookSubscription).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
