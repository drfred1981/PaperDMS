import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { INotificationTemplate } from '../notification-template.model';
import { NotificationTemplateService } from '../service/notification-template.service';

import { NotificationTemplateFormService } from './notification-template-form.service';
import { NotificationTemplateUpdate } from './notification-template-update';

describe('NotificationTemplate Management Update Component', () => {
  let comp: NotificationTemplateUpdate;
  let fixture: ComponentFixture<NotificationTemplateUpdate>;
  let activatedRoute: ActivatedRoute;
  let notificationTemplateFormService: NotificationTemplateFormService;
  let notificationTemplateService: NotificationTemplateService;

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

    fixture = TestBed.createComponent(NotificationTemplateUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    notificationTemplateFormService = TestBed.inject(NotificationTemplateFormService);
    notificationTemplateService = TestBed.inject(NotificationTemplateService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const notificationTemplate: INotificationTemplate = { id: 29982 };

      activatedRoute.data = of({ notificationTemplate });
      comp.ngOnInit();

      expect(comp.notificationTemplate).toEqual(notificationTemplate);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<INotificationTemplate>>();
      const notificationTemplate = { id: 25253 };
      jest.spyOn(notificationTemplateFormService, 'getNotificationTemplate').mockReturnValue(notificationTemplate);
      jest.spyOn(notificationTemplateService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ notificationTemplate });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: notificationTemplate }));
      saveSubject.complete();

      // THEN
      expect(notificationTemplateFormService.getNotificationTemplate).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(notificationTemplateService.update).toHaveBeenCalledWith(expect.objectContaining(notificationTemplate));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<INotificationTemplate>>();
      const notificationTemplate = { id: 25253 };
      jest.spyOn(notificationTemplateFormService, 'getNotificationTemplate').mockReturnValue({ id: null });
      jest.spyOn(notificationTemplateService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ notificationTemplate: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: notificationTemplate }));
      saveSubject.complete();

      // THEN
      expect(notificationTemplateFormService.getNotificationTemplate).toHaveBeenCalled();
      expect(notificationTemplateService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<INotificationTemplate>>();
      const notificationTemplate = { id: 25253 };
      jest.spyOn(notificationTemplateService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ notificationTemplate });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(notificationTemplateService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
