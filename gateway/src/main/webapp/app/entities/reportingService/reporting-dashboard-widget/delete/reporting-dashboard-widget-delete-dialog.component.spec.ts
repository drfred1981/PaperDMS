jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, fakeAsync, inject, tick } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ReportingDashboardWidgetService } from '../service/reporting-dashboard-widget.service';

import { ReportingDashboardWidgetDeleteDialogComponent } from './reporting-dashboard-widget-delete-dialog.component';

describe('ReportingDashboardWidget Management Delete Component', () => {
  let comp: ReportingDashboardWidgetDeleteDialogComponent;
  let fixture: ComponentFixture<ReportingDashboardWidgetDeleteDialogComponent>;
  let service: ReportingDashboardWidgetService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ReportingDashboardWidgetDeleteDialogComponent],
      providers: [provideHttpClient(), NgbActiveModal],
    })
      .overrideTemplate(ReportingDashboardWidgetDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ReportingDashboardWidgetDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ReportingDashboardWidgetService);
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
