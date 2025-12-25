import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { DocumentWatchService } from '../service/document-watch.service';
import { IDocumentWatch } from '../document-watch.model';
import { DocumentWatchFormService } from './document-watch-form.service';

import { DocumentWatchUpdateComponent } from './document-watch-update.component';

describe('DocumentWatch Management Update Component', () => {
  let comp: DocumentWatchUpdateComponent;
  let fixture: ComponentFixture<DocumentWatchUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let documentWatchFormService: DocumentWatchFormService;
  let documentWatchService: DocumentWatchService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [DocumentWatchUpdateComponent],
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
      .overrideTemplate(DocumentWatchUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DocumentWatchUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    documentWatchFormService = TestBed.inject(DocumentWatchFormService);
    documentWatchService = TestBed.inject(DocumentWatchService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const documentWatch: IDocumentWatch = { id: 28672 };

      activatedRoute.data = of({ documentWatch });
      comp.ngOnInit();

      expect(comp.documentWatch).toEqual(documentWatch);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentWatch>>();
      const documentWatch = { id: 13046 };
      jest.spyOn(documentWatchFormService, 'getDocumentWatch').mockReturnValue(documentWatch);
      jest.spyOn(documentWatchService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentWatch });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: documentWatch }));
      saveSubject.complete();

      // THEN
      expect(documentWatchFormService.getDocumentWatch).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(documentWatchService.update).toHaveBeenCalledWith(expect.objectContaining(documentWatch));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentWatch>>();
      const documentWatch = { id: 13046 };
      jest.spyOn(documentWatchFormService, 'getDocumentWatch').mockReturnValue({ id: null });
      jest.spyOn(documentWatchService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentWatch: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: documentWatch }));
      saveSubject.complete();

      // THEN
      expect(documentWatchFormService.getDocumentWatch).toHaveBeenCalled();
      expect(documentWatchService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentWatch>>();
      const documentWatch = { id: 13046 };
      jest.spyOn(documentWatchService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentWatch });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(documentWatchService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
