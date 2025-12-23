import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';

import { ScheduledReportService } from '../service/scheduled-report.service';

import { ScheduledReportDeleteDialog } from './scheduled-report-delete-dialog';

describe('ScheduledReport Management Delete Component', () => {
  let comp: ScheduledReportDeleteDialog;
  let fixture: ComponentFixture<ScheduledReportDeleteDialog>;
  let service: ScheduledReportService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), NgbActiveModal],
    });
    fixture = TestBed.createComponent(ScheduledReportDeleteDialog);
    comp = fixture.componentInstance;
    service = TestBed.inject(ScheduledReportService);
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
