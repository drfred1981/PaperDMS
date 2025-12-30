import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { NotificationPreferenceService } from '../service/notification-preference.service';
import { INotificationPreference } from '../notification-preference.model';
import { NotificationPreferenceFormService } from './notification-preference-form.service';

import { NotificationPreferenceUpdateComponent } from './notification-preference-update.component';

describe('NotificationPreference Management Update Component', () => {
  let comp: NotificationPreferenceUpdateComponent;
  let fixture: ComponentFixture<NotificationPreferenceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let notificationPreferenceFormService: NotificationPreferenceFormService;
  let notificationPreferenceService: NotificationPreferenceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [NotificationPreferenceUpdateComponent],
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
      .overrideTemplate(NotificationPreferenceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(NotificationPreferenceUpdateComponent);
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
