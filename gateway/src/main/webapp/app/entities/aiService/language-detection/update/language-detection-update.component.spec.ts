import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { LanguageDetectionService } from '../service/language-detection.service';
import { ILanguageDetection } from '../language-detection.model';
import { LanguageDetectionFormService } from './language-detection-form.service';

import { LanguageDetectionUpdateComponent } from './language-detection-update.component';

describe('LanguageDetection Management Update Component', () => {
  let comp: LanguageDetectionUpdateComponent;
  let fixture: ComponentFixture<LanguageDetectionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let languageDetectionFormService: LanguageDetectionFormService;
  let languageDetectionService: LanguageDetectionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [LanguageDetectionUpdateComponent],
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
      .overrideTemplate(LanguageDetectionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LanguageDetectionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    languageDetectionFormService = TestBed.inject(LanguageDetectionFormService);
    languageDetectionService = TestBed.inject(LanguageDetectionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const languageDetection: ILanguageDetection = { id: 434 };

      activatedRoute.data = of({ languageDetection });
      comp.ngOnInit();

      expect(comp.languageDetection).toEqual(languageDetection);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILanguageDetection>>();
      const languageDetection = { id: 20981 };
      jest.spyOn(languageDetectionFormService, 'getLanguageDetection').mockReturnValue(languageDetection);
      jest.spyOn(languageDetectionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ languageDetection });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: languageDetection }));
      saveSubject.complete();

      // THEN
      expect(languageDetectionFormService.getLanguageDetection).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(languageDetectionService.update).toHaveBeenCalledWith(expect.objectContaining(languageDetection));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILanguageDetection>>();
      const languageDetection = { id: 20981 };
      jest.spyOn(languageDetectionFormService, 'getLanguageDetection').mockReturnValue({ id: null });
      jest.spyOn(languageDetectionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ languageDetection: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: languageDetection }));
      saveSubject.complete();

      // THEN
      expect(languageDetectionFormService.getLanguageDetection).toHaveBeenCalled();
      expect(languageDetectionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILanguageDetection>>();
      const languageDetection = { id: 20981 };
      jest.spyOn(languageDetectionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ languageDetection });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(languageDetectionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
