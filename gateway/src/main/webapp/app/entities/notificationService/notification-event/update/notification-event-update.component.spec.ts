import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { NotificationEventService } from '../service/notification-event.service';
import { INotificationEvent } from '../notification-event.model';
import { NotificationEventFormService } from './notification-event-form.service';

import { NotificationEventUpdateComponent } from './notification-event-update.component';

describe('NotificationEvent Management Update Component', () => {
  let comp: NotificationEventUpdateComponent;
  let fixture: ComponentFixture<NotificationEventUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let notificationEventFormService: NotificationEventFormService;
  let notificationEventService: NotificationEventService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [NotificationEventUpdateComponent],
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
      .overrideTemplate(NotificationEventUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(NotificationEventUpdateComponent);
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
