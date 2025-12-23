import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IDocumentType } from '../document-type.model';
import { DocumentTypeService } from '../service/document-type.service';

import { DocumentTypeFormService } from './document-type-form.service';
import { DocumentTypeUpdate } from './document-type-update';

describe('DocumentType Management Update Component', () => {
  let comp: DocumentTypeUpdate;
  let fixture: ComponentFixture<DocumentTypeUpdate>;
  let activatedRoute: ActivatedRoute;
  let documentTypeFormService: DocumentTypeFormService;
  let documentTypeService: DocumentTypeService;

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

    fixture = TestBed.createComponent(DocumentTypeUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    documentTypeFormService = TestBed.inject(DocumentTypeFormService);
    documentTypeService = TestBed.inject(DocumentTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const documentType: IDocumentType = { id: 29456 };

      activatedRoute.data = of({ documentType });
      comp.ngOnInit();

      expect(comp.documentType).toEqual(documentType);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentType>>();
      const documentType = { id: 9974 };
      jest.spyOn(documentTypeFormService, 'getDocumentType').mockReturnValue(documentType);
      jest.spyOn(documentTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: documentType }));
      saveSubject.complete();

      // THEN
      expect(documentTypeFormService.getDocumentType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(documentTypeService.update).toHaveBeenCalledWith(expect.objectContaining(documentType));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentType>>();
      const documentType = { id: 9974 };
      jest.spyOn(documentTypeFormService, 'getDocumentType').mockReturnValue({ id: null });
      jest.spyOn(documentTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: documentType }));
      saveSubject.complete();

      // THEN
      expect(documentTypeFormService.getDocumentType).toHaveBeenCalled();
      expect(documentTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentType>>();
      const documentType = { id: 9974 };
      jest.spyOn(documentTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(documentTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
