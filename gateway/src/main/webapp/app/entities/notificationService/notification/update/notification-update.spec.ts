import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { INotificationTemplate } from 'app/entities/notificationService/notification-template/notification-template.model';
import { NotificationTemplateService } from 'app/entities/notificationService/notification-template/service/notification-template.service';
import { INotification } from '../notification.model';
import { NotificationService } from '../service/notification.service';

import { NotificationFormService } from './notification-form.service';
import { NotificationUpdate } from './notification-update';

describe('Notification Management Update Component', () => {
  let comp: NotificationUpdate;
  let fixture: ComponentFixture<NotificationUpdate>;
  let activatedRoute: ActivatedRoute;
  let notificationFormService: NotificationFormService;
  let notificationService: NotificationService;
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

    fixture = TestBed.createComponent(NotificationUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    notificationFormService = TestBed.inject(NotificationFormService);
    notificationService = TestBed.inject(NotificationService);
    notificationTemplateService = TestBed.inject(NotificationTemplateService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call NotificationTemplate query and add missing value', () => {
      const notification: INotification = { id: 16244 };
      const template: INotificationTemplate = { id: 25253 };
      notification.template = template;

      const notificationTemplateCollection: INotificationTemplate[] = [{ id: 25253 }];
      jest.spyOn(notificationTemplateService, 'query').mockReturnValue(of(new HttpResponse({ body: notificationTemplateCollection })));
      const additionalNotificationTemplates = [template];
      const expectedCollection: INotificationTemplate[] = [...additionalNotificationTemplates, ...notificationTemplateCollection];
      jest.spyOn(notificationTemplateService, 'addNotificationTemplateToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ notification });
      comp.ngOnInit();

      expect(notificationTemplateService.query).toHaveBeenCalled();
      expect(notificationTemplateService.addNotificationTemplateToCollectionIfMissing).toHaveBeenCalledWith(
        notificationTemplateCollection,
        ...additionalNotificationTemplates.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.notificationTemplatesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const notification: INotification = { id: 16244 };
      const template: INotificationTemplate = { id: 25253 };
      notification.template = template;

      activatedRoute.data = of({ notification });
      comp.ngOnInit();

      expect(comp.notificationTemplatesSharedCollection()).toContainEqual(template);
      expect(comp.notification).toEqual(notification);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<INotification>>();
      const notification = { id: 16124 };
      jest.spyOn(notificationFormService, 'getNotification').mockReturnValue(notification);
      jest.spyOn(notificationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ notification });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: notification }));
      saveSubject.complete();

      // THEN
      expect(notificationFormService.getNotification).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(notificationService.update).toHaveBeenCalledWith(expect.objectContaining(notification));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<INotification>>();
      const notification = { id: 16124 };
      jest.spyOn(notificationFormService, 'getNotification').mockReturnValue({ id: null });
      jest.spyOn(notificationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ notification: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: notification }));
      saveSubject.complete();

      // THEN
      expect(notificationFormService.getNotification).toHaveBeenCalled();
      expect(notificationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<INotification>>();
      const notification = { id: 16124 };
      jest.spyOn(notificationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ notification });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(notificationService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareNotificationTemplate', () => {
      it('should forward to notificationTemplateService', () => {
        const entity = { id: 25253 };
        const entity2 = { id: 29982 };
        jest.spyOn(notificationTemplateService, 'compareNotificationTemplate');
        comp.compareNotificationTemplate(entity, entity2);
        expect(notificationTemplateService.compareNotificationTemplate).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
