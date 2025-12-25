import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { DocumentStatisticsService } from '../service/document-statistics.service';
import { IDocumentStatistics } from '../document-statistics.model';
import { DocumentStatisticsFormService } from './document-statistics-form.service';

import { DocumentStatisticsUpdateComponent } from './document-statistics-update.component';

describe('DocumentStatistics Management Update Component', () => {
  let comp: DocumentStatisticsUpdateComponent;
  let fixture: ComponentFixture<DocumentStatisticsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let documentStatisticsFormService: DocumentStatisticsFormService;
  let documentStatisticsService: DocumentStatisticsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [DocumentStatisticsUpdateComponent],
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
      .overrideTemplate(DocumentStatisticsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DocumentStatisticsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    documentStatisticsFormService = TestBed.inject(DocumentStatisticsFormService);
    documentStatisticsService = TestBed.inject(DocumentStatisticsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const documentStatistics: IDocumentStatistics = { id: 4489 };

      activatedRoute.data = of({ documentStatistics });
      comp.ngOnInit();

      expect(comp.documentStatistics).toEqual(documentStatistics);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentStatistics>>();
      const documentStatistics = { id: 5208 };
      jest.spyOn(documentStatisticsFormService, 'getDocumentStatistics').mockReturnValue(documentStatistics);
      jest.spyOn(documentStatisticsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentStatistics });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: documentStatistics }));
      saveSubject.complete();

      // THEN
      expect(documentStatisticsFormService.getDocumentStatistics).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(documentStatisticsService.update).toHaveBeenCalledWith(expect.objectContaining(documentStatistics));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentStatistics>>();
      const documentStatistics = { id: 5208 };
      jest.spyOn(documentStatisticsFormService, 'getDocumentStatistics').mockReturnValue({ id: null });
      jest.spyOn(documentStatisticsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentStatistics: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: documentStatistics }));
      saveSubject.complete();

      // THEN
      expect(documentStatisticsFormService.getDocumentStatistics).toHaveBeenCalled();
      expect(documentStatisticsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentStatistics>>();
      const documentStatistics = { id: 5208 };
      jest.spyOn(documentStatisticsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentStatistics });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(documentStatisticsService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
