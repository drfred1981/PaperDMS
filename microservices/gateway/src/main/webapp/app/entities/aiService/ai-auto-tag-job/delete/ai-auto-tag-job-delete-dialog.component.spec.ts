jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, fakeAsync, inject, tick } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { AIAutoTagJobService } from '../service/ai-auto-tag-job.service';

import { AIAutoTagJobDeleteDialogComponent } from './ai-auto-tag-job-delete-dialog.component';

describe('AIAutoTagJob Management Delete Component', () => {
  let comp: AIAutoTagJobDeleteDialogComponent;
  let fixture: ComponentFixture<AIAutoTagJobDeleteDialogComponent>;
  let service: AIAutoTagJobService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AIAutoTagJobDeleteDialogComponent],
      providers: [provideHttpClient(), NgbActiveModal],
    })
      .overrideTemplate(AIAutoTagJobDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AIAutoTagJobDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(AIAutoTagJobService);
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
