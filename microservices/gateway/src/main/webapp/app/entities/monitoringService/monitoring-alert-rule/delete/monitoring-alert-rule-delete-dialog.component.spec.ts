jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, fakeAsync, inject, tick } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { MonitoringAlertRuleService } from '../service/monitoring-alert-rule.service';

import { MonitoringAlertRuleDeleteDialogComponent } from './monitoring-alert-rule-delete-dialog.component';

describe('MonitoringAlertRule Management Delete Component', () => {
  let comp: MonitoringAlertRuleDeleteDialogComponent;
  let fixture: ComponentFixture<MonitoringAlertRuleDeleteDialogComponent>;
  let service: MonitoringAlertRuleService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MonitoringAlertRuleDeleteDialogComponent],
      providers: [provideHttpClient(), NgbActiveModal],
    })
      .overrideTemplate(MonitoringAlertRuleDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(MonitoringAlertRuleDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(MonitoringAlertRuleService);
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
