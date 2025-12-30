jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, fakeAsync, inject, tick } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { EmailImportEmailAttachmentService } from '../service/email-import-email-attachment.service';

import { EmailImportEmailAttachmentDeleteDialogComponent } from './email-import-email-attachment-delete-dialog.component';

describe('EmailImportEmailAttachment Management Delete Component', () => {
  let comp: EmailImportEmailAttachmentDeleteDialogComponent;
  let fixture: ComponentFixture<EmailImportEmailAttachmentDeleteDialogComponent>;
  let service: EmailImportEmailAttachmentService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [EmailImportEmailAttachmentDeleteDialogComponent],
      providers: [provideHttpClient(), NgbActiveModal],
    })
      .overrideTemplate(EmailImportEmailAttachmentDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EmailImportEmailAttachmentDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(EmailImportEmailAttachmentService);
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
