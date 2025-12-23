import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { INotificationEvent } from '../notification-event.model';
import { NotificationEventService } from '../service/notification-event.service';

import { NotificationEventFormService } from './notification-event-form.service';
import { NotificationEventUpdate } from './notification-event-update';

describe('NotificationEvent Management Update Component', () => {
  let comp: NotificationEventUpdate;
  let fixture: ComponentFixture<NotificationEventUpdate>;
  let activatedRoute: ActivatedRoute;
  let notificationEventFormService: NotificationEventFormService;
  let notificationEventService: NotificationEventService;

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

    fixture = TestBed.createComponent(NotificationEventUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    notificationEventFormService = TestBed.inject(NotificationEventFormService);
    notificationEventService = TestBed.inject(NotificationEventService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const notificationEvent: INotificationEvent = { id: 30681 };

      activatedRoute.data = of({ notificationEvent });
      comp.ngOnInit();

      expect(comp.notificationEvent).toEqual(notificationEvent);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<INotificationEvent>>();
      const notificationEvent = { id: 9232 };
      jest.spyOn(notificationEventFormService, 'getNotificationEvent').mockReturnValue(notificationEvent);
      jest.spyOn(notificationEventService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ notificationEvent });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: notificationEvent }));
      saveSubject.complete();

      // THEN
      expect(notificationEventFormService.getNotificationEvent).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(notificationEventService.update).toHaveBeenCalledWith(expect.objectContaining(notificationEvent));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<INotificationEvent>>();
      const notificationEvent = { id: 9232 };
      jest.spyOn(notificationEventFormService, 'getNotificationEvent').mockReturnValue({ id: null });
      jest.spyOn(notificationEventService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ notificationEvent: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: notificationEvent }));
      saveSubject.complete();

      // THEN
      expect(notificationEventFormService.getNotificationEvent).toHaveBeenCalled();
      expect(notificationEventService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<INotificationEvent>>();
      const notificationEvent = { id: 9232 };
      jest.spyOn(notificationEventService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ notificationEvent });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(notificationEventService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
