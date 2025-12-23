import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IDocumentServiceStatus } from '../document-service-status.model';
import { DocumentServiceStatusService } from '../service/document-service-status.service';

import { DocumentServiceStatusFormService } from './document-service-status-form.service';
import { DocumentServiceStatusUpdate } from './document-service-status-update';

describe('DocumentServiceStatus Management Update Component', () => {
  let comp: DocumentServiceStatusUpdate;
  let fixture: ComponentFixture<DocumentServiceStatusUpdate>;
  let activatedRoute: ActivatedRoute;
  let documentServiceStatusFormService: DocumentServiceStatusFormService;
  let documentServiceStatusService: DocumentServiceStatusService;

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

    fixture = TestBed.createComponent(DocumentServiceStatusUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    documentServiceStatusFormService = TestBed.inject(DocumentServiceStatusFormService);
    documentServiceStatusService = TestBed.inject(DocumentServiceStatusService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const documentServiceStatus: IDocumentServiceStatus = { id: 16256 };

      activatedRoute.data = of({ documentServiceStatus });
      comp.ngOnInit();

      expect(comp.documentServiceStatus).toEqual(documentServiceStatus);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentServiceStatus>>();
      const documentServiceStatus = { id: 1543 };
      jest.spyOn(documentServiceStatusFormService, 'getDocumentServiceStatus').mockReturnValue(documentServiceStatus);
      jest.spyOn(documentServiceStatusService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentServiceStatus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: documentServiceStatus }));
      saveSubject.complete();

      // THEN
      expect(documentServiceStatusFormService.getDocumentServiceStatus).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(documentServiceStatusService.update).toHaveBeenCalledWith(expect.objectContaining(documentServiceStatus));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentServiceStatus>>();
      const documentServiceStatus = { id: 1543 };
      jest.spyOn(documentServiceStatusFormService, 'getDocumentServiceStatus').mockReturnValue({ id: null });
      jest.spyOn(documentServiceStatusService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentServiceStatus: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: documentServiceStatus }));
      saveSubject.complete();

      // THEN
      expect(documentServiceStatusFormService.getDocumentServiceStatus).toHaveBeenCalled();
      expect(documentServiceStatusService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentServiceStatus>>();
      const documentServiceStatus = { id: 1543 };
      jest.spyOn(documentServiceStatusService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentServiceStatus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(documentServiceStatusService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
