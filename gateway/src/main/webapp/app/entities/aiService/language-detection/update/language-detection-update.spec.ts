import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { ILanguageDetection } from '../language-detection.model';
import { LanguageDetectionService } from '../service/language-detection.service';

import { LanguageDetectionFormService } from './language-detection-form.service';
import { LanguageDetectionUpdate } from './language-detection-update';

describe('LanguageDetection Management Update Component', () => {
  let comp: LanguageDetectionUpdate;
  let fixture: ComponentFixture<LanguageDetectionUpdate>;
  let activatedRoute: ActivatedRoute;
  let languageDetectionFormService: LanguageDetectionFormService;
  let languageDetectionService: LanguageDetectionService;

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

    fixture = TestBed.createComponent(LanguageDetectionUpdate);
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
