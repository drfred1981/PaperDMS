import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { WebhookSubscriptionService } from '../service/webhook-subscription.service';
import { IWebhookSubscription } from '../webhook-subscription.model';

import { WebhookSubscriptionFormService } from './webhook-subscription-form.service';
import { WebhookSubscriptionUpdate } from './webhook-subscription-update';

describe('WebhookSubscription Management Update Component', () => {
  let comp: WebhookSubscriptionUpdate;
  let fixture: ComponentFixture<WebhookSubscriptionUpdate>;
  let activatedRoute: ActivatedRoute;
  let webhookSubscriptionFormService: WebhookSubscriptionFormService;
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

    fixture = TestBed.createComponent(WebhookSubscriptionUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    webhookSubscriptionFormService = TestBed.inject(WebhookSubscriptionFormService);
    webhookSubscriptionService = TestBed.inject(WebhookSubscriptionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const webhookSubscription: IWebhookSubscription = { id: 24725 };

      activatedRoute.data = of({ webhookSubscription });
      comp.ngOnInit();

      expect(comp.webhookSubscription).toEqual(webhookSubscription);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWebhookSubscription>>();
      const webhookSubscription = { id: 28283 };
      jest.spyOn(webhookSubscriptionFormService, 'getWebhookSubscription').mockReturnValue(webhookSubscription);
      jest.spyOn(webhookSubscriptionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ webhookSubscription });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: webhookSubscription }));
      saveSubject.complete();

      // THEN
      expect(webhookSubscriptionFormService.getWebhookSubscription).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(webhookSubscriptionService.update).toHaveBeenCalledWith(expect.objectContaining(webhookSubscription));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWebhookSubscription>>();
      const webhookSubscription = { id: 28283 };
      jest.spyOn(webhookSubscriptionFormService, 'getWebhookSubscription').mockReturnValue({ id: null });
      jest.spyOn(webhookSubscriptionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ webhookSubscription: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: webhookSubscription }));
      saveSubject.complete();

      // THEN
      expect(webhookSubscriptionFormService.getWebhookSubscription).toHaveBeenCalled();
      expect(webhookSubscriptionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWebhookSubscription>>();
      const webhookSubscription = { id: 28283 };
      jest.spyOn(webhookSubscriptionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ webhookSubscription });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(webhookSubscriptionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
