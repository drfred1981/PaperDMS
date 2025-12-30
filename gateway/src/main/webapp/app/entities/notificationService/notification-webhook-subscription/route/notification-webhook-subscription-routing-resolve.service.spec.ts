import { TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { ActivatedRoute, ActivatedRouteSnapshot, Router, convertToParamMap } from '@angular/router';
import { of } from 'rxjs';

import { INotificationWebhookSubscription } from '../notification-webhook-subscription.model';
import { NotificationWebhookSubscriptionService } from '../service/notification-webhook-subscription.service';

import notificationWebhookSubscriptionResolve from './notification-webhook-subscription-routing-resolve.service';

describe('NotificationWebhookSubscription routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: NotificationWebhookSubscriptionService;
  let resultNotificationWebhookSubscription: INotificationWebhookSubscription | null | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    service = TestBed.inject(NotificationWebhookSubscriptionService);
    resultNotificationWebhookSubscription = undefined;
  });

  describe('resolve', () => {
    it('should return INotificationWebhookSubscription returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        notificationWebhookSubscriptionResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultNotificationWebhookSubscription = result;
          },
        });
      });

      // THEN
      expect(service.find).toHaveBeenCalledWith(123);
      expect(resultNotificationWebhookSubscription).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      TestBed.runInInjectionContext(() => {
        notificationWebhookSubscriptionResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultNotificationWebhookSubscription = result;
          },
        });
      });

      // THEN
      expect(service.find).not.toHaveBeenCalled();
      expect(resultNotificationWebhookSubscription).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<INotificationWebhookSubscription>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        notificationWebhookSubscriptionResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultNotificationWebhookSubscription = result;
          },
        });
      });

      // THEN
      expect(service.find).toHaveBeenCalledWith(123);
      expect(resultNotificationWebhookSubscription).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
