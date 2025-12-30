jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, fakeAsync, inject, tick } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { NotificationWebhookLogService } from '../service/notification-webhook-log.service';

import { NotificationWebhookLogDeleteDialogComponent } from './notification-webhook-log-delete-dialog.component';

describe('NotificationWebhookLog Management Delete Component', () => {
  let comp: NotificationWebhookLogDeleteDialogComponent;
  let fixture: ComponentFixture<NotificationWebhookLogDeleteDialogComponent>;
  let service: NotificationWebhookLogService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [NotificationWebhookLogDeleteDialogComponent],
      providers: [provideHttpClient(), NgbActiveModal],
    })
      .overrideTemplate(NotificationWebhookLogDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(NotificationWebhookLogDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(NotificationWebhookLogService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));

        // WHEN
        comp.confirmDelete(123);
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      }),
    ));

    it('should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
