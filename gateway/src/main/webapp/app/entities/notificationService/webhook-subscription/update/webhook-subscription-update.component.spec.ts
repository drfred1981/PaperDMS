import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { WebhookSubscriptionService } from '../service/webhook-subscription.service';
import { IWebhookSubscription } from '../webhook-subscription.model';
import { WebhookSubscriptionFormService } from './webhook-subscription-form.service';

import { WebhookSubscriptionUpdateComponent } from './webhook-subscription-update.component';

describe('WebhookSubscription Management Update Component', () => {
  let comp: WebhookSubscriptionUpdateComponent;
  let fixture: ComponentFixture<WebhookSubscriptionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let webhookSubscriptionFormService: WebhookSubscriptionFormService;
  let webhookSubscriptionService: WebhookSubscriptionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [WebhookSubscriptionUpdateComponent],
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
      .overrideTemplate(WebhookSubscriptionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(WebhookSubscriptionUpdateComponent);
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
