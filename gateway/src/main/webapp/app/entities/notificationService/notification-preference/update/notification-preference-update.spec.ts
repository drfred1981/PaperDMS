import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { INotificationPreference } from '../notification-preference.model';
import { NotificationPreferenceService } from '../service/notification-preference.service';

import { NotificationPreferenceFormService } from './notification-preference-form.service';
import { NotificationPreferenceUpdate } from './notification-preference-update';

describe('NotificationPreference Management Update Component', () => {
  let comp: NotificationPreferenceUpdate;
  let fixture: ComponentFixture<NotificationPreferenceUpdate>;
  let activatedRoute: ActivatedRoute;
  let notificationPreferenceFormService: NotificationPreferenceFormService;
  let notificationPreferenceService: NotificationPreferenceService;

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

    fixture = TestBed.createComponent(NotificationPreferenceUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    notificationPreferenceFormService = TestBed.inject(NotificationPreferenceFormService);
    notificationPreferenceService = TestBed.inject(NotificationPreferenceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const notificationPreference: INotificationPreference = { id: 19345 };

      activatedRoute.data = of({ notificationPreference });
      comp.ngOnInit();

      expect(comp.notificationPreference).toEqual(notificationPreference);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<INotificationPreference>>();
      const notificationPreference = { id: 17384 };
      jest.spyOn(notificationPreferenceFormService, 'getNotificationPreference').mockReturnValue(notificationPreference);
      jest.spyOn(notificationPreferenceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ notificationPreference });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: notificationPreference }));
      saveSubject.complete();

      // THEN
      expect(notificationPreferenceFormService.getNotificationPreference).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(notificationPreferenceService.update).toHaveBeenCalledWith(expect.objectContaining(notificationPreference));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<INotificationPreference>>();
      const notificationPreference = { id: 17384 };
      jest.spyOn(notificationPreferenceFormService, 'getNotificationPreference').mockReturnValue({ id: null });
      jest.spyOn(notificationPreferenceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ notificationPreference: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: notificationPreference }));
      saveSubject.complete();

      // THEN
      expect(notificationPreferenceFormService.getNotificationPreference).toHaveBeenCalled();
      expect(notificationPreferenceService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<INotificationPreference>>();
      const notificationPreference = { id: 17384 };
      jest.spyOn(notificationPreferenceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ notificationPreference });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(notificationPreferenceService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
