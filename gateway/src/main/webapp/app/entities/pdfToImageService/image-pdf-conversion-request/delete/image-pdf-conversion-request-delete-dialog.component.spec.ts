jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, fakeAsync, inject, tick } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ImagePdfConversionRequestService } from '../service/image-pdf-conversion-request.service';

import { ImagePdfConversionRequestDeleteDialogComponent } from './image-pdf-conversion-request-delete-dialog.component';

describe('ImagePdfConversionRequest Management Delete Component', () => {
  let comp: ImagePdfConversionRequestDeleteDialogComponent;
  let fixture: ComponentFixture<ImagePdfConversionRequestDeleteDialogComponent>;
  let service: ImagePdfConversionRequestService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ImagePdfConversionRequestDeleteDialogComponent],
      providers: [provideHttpClient(), NgbActiveModal],
    })
      .overrideTemplate(ImagePdfConversionRequestDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ImagePdfConversionRequestDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ImagePdfConversionRequestService);
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
