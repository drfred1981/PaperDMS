import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { WebhookSubscriptionService } from 'app/entities/notificationService/webhook-subscription/service/webhook-subscription.service';
import { IWebhookSubscription } from 'app/entities/notificationService/webhook-subscription/webhook-subscription.model';
import { WebhookLogService } from '../service/webhook-log.service';
import { IWebhookLog } from '../webhook-log.model';

import { WebhookLogFormService } from './webhook-log-form.service';
import { WebhookLogUpdate } from './webhook-log-update';

describe('WebhookLog Management Update Component', () => {
  let comp: WebhookLogUpdate;
  let fixture: ComponentFixture<WebhookLogUpdate>;
  let activatedRoute: ActivatedRoute;
  let webhookLogFormService: WebhookLogFormService;
  let webhookLogService: WebhookLogService;
  let webhookSubscriptionService: WebhookSubscriptionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TranslateModule.forRoot()],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    });

    fixture = TestBed.createComponent(WebhookLogUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    webhookLogFormService = TestBed.inject(WebhookLogFormService);
    webhookLogService = TestBed.inject(WebhookLogService);
    webhookSubscriptionService = TestBed.inject(WebhookSubscriptionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call WebhookSubscription query and add missing value', () => {
      const webhookLog: IWebhookLog = { id: 26019 };
      const subscription: IWebhookSubscription = { id: 28283 };
      webhookLog.subscription = subscription;

      const webhookSubscriptionCollection: IWebhookSubscription[] = [{ id: 28283 }];
      jest.spyOn(webhookSubscriptionService, 'query').mockReturnValue(of(new HttpResponse({ body: webhookSubscriptionCollection })));
      const additionalWebhookSubscriptions = [subscription];
      const expectedCollection: IWebhookSubscription[] = [...additionalWebhookSubscriptions, ...webhookSubscriptionCollection];
      jest.spyOn(webhookSubscriptionService, 'addWebhookSubscriptionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ webhookLog });
      comp.ngOnInit();

      expect(webhookSubscriptionService.query).toHaveBeenCalled();
      expect(webhookSubscriptionService.addWebhookSubscriptionToCollectionIfMissing).toHaveBeenCalledWith(
        webhookSubscriptionCollection,
        ...additionalWebhookSubscriptions.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.webhookSubscriptionsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const webhookLog: IWebhookLog = { id: 26019 };
      const subscription: IWebhookSubscription = { id: 28283 };
      webhookLog.subscription = subscription;

      activatedRoute.data = of({ webhookLog });
      comp.ngOnInit();

      expect(comp.webhookSubscriptionsSharedCollection()).toContainEqual(subscription);
      expect(comp.webhookLog).toEqual(webhookLog);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWebhookLog>>();
      const webhookLog = { id: 27220 };
      jest.spyOn(webhookLogFormService, 'getWebhookLog').mockReturnValue(webhookLog);
      jest.spyOn(webhookLogService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ webhookLog });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: webhookLog }));
      saveSubject.complete();

      // THEN
      expect(webhookLogFormService.getWebhookLog).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(webhookLogService.update).toHaveBeenCalledWith(expect.objectContaining(webhookLog));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWebhookLog>>();
      const webhookLog = { id: 27220 };
      jest.spyOn(webhookLogFormService, 'getWebhookLog').mockReturnValue({ id: null });
      jest.spyOn(webhookLogService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ webhookLog: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: webhookLog }));
      saveSubject.complete();

      // THEN
      expect(webhookLogFormService.getWebhookLog).toHaveBeenCalled();
      expect(webhookLogService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWebhookLog>>();
      const webhookLog = { id: 27220 };
      jest.spyOn(webhookLogService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ webhookLog });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(webhookLogService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareWebhookSubscription', () => {
      it('should forward to webhookSubscriptionService', () => {
        const entity = { id: 28283 };
        const entity2 = { id: 24725 };
        jest.spyOn(webhookSubscriptionService, 'compareWebhookSubscription');
        comp.compareWebhookSubscription(entity, entity2);
        expect(webhookSubscriptionService.compareWebhookSubscription).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
