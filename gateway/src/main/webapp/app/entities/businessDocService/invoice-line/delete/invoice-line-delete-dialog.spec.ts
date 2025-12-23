import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';

import { InvoiceLineService } from '../service/invoice-line.service';

import { InvoiceLineDeleteDialog } from './invoice-line-delete-dialog';

describe('InvoiceLine Management Delete Component', () => {
  let comp: InvoiceLineDeleteDialog;
  let fixture: ComponentFixture<InvoiceLineDeleteDialog>;
  let service: InvoiceLineService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), NgbActiveModal],
    });
    fixture = TestBed.createComponent(InvoiceLineDeleteDialog);
    comp = fixture.componentInstance;
    service = TestBed.inject(InvoiceLineService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('should call delete service on confirmDelete', () => {
      // GIVEN
      jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));
      jest.spyOn(mockActiveModal, 'close');

      // WHEN
      comp.confirmDelete(123);

      // THEN
      expect(service.delete).toHaveBeenCalledWith(123);
      expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
    });

    it('should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');
      jest.spyOn(mockActiveModal, 'close');
      jest.spyOn(mockActiveModal, 'dismiss');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
