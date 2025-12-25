import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { DocumentFingerprintService } from '../service/document-fingerprint.service';
import { IDocumentFingerprint } from '../document-fingerprint.model';
import { DocumentFingerprintFormService } from './document-fingerprint-form.service';

import { DocumentFingerprintUpdateComponent } from './document-fingerprint-update.component';

describe('DocumentFingerprint Management Update Component', () => {
  let comp: DocumentFingerprintUpdateComponent;
  let fixture: ComponentFixture<DocumentFingerprintUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let documentFingerprintFormService: DocumentFingerprintFormService;
  let documentFingerprintService: DocumentFingerprintService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [DocumentFingerprintUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(DocumentFingerprintUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DocumentFingerprintUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    documentFingerprintFormService = TestBed.inject(DocumentFingerprintFormService);
    documentFingerprintService = TestBed.inject(DocumentFingerprintService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const documentFingerprint: IDocumentFingerprint = { id: 19955 };

      activatedRoute.data = of({ documentFingerprint });
      comp.ngOnInit();

      expect(comp.documentFingerprint).toEqual(documentFingerprint);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentFingerprint>>();
      const documentFingerprint = { id: 26494 };
      jest.spyOn(documentFingerprintFormService, 'getDocumentFingerprint').mockReturnValue(documentFingerprint);
      jest.spyOn(documentFingerprintService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentFingerprint });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: documentFingerprint }));
      saveSubject.complete();

      // THEN
      expect(documentFingerprintFormService.getDocumentFingerprint).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(documentFingerprintService.update).toHaveBeenCalledWith(expect.objectContaining(documentFingerprint));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentFingerprint>>();
      const documentFingerprint = { id: 26494 };
      jest.spyOn(documentFingerprintFormService, 'getDocumentFingerprint').mockReturnValue({ id: null });
      jest.spyOn(documentFingerprintService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentFingerprint: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: documentFingerprint }));
      saveSubject.complete();

      // THEN
      expect(documentFingerprintFormService.getDocumentFingerprint).toHaveBeenCalled();
      expect(documentFingerprintService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentFingerprint>>();
      const documentFingerprint = { id: 26494 };
      jest.spyOn(documentFingerprintService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentFingerprint });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(documentFingerprintService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
