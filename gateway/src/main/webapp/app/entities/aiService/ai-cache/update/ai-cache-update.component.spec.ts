import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { AiCacheService } from '../service/ai-cache.service';
import { IAiCache } from '../ai-cache.model';
import { AiCacheFormService } from './ai-cache-form.service';

import { AiCacheUpdateComponent } from './ai-cache-update.component';

describe('AiCache Management Update Component', () => {
  let comp: AiCacheUpdateComponent;
  let fixture: ComponentFixture<AiCacheUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let aiCacheFormService: AiCacheFormService;
  let aiCacheService: AiCacheService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AiCacheUpdateComponent],
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
      .overrideTemplate(AiCacheUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AiCacheUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    aiCacheFormService = TestBed.inject(AiCacheFormService);
    aiCacheService = TestBed.inject(AiCacheService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const aiCache: IAiCache = { id: 16798 };

      activatedRoute.data = of({ aiCache });
      comp.ngOnInit();

      expect(comp.aiCache).toEqual(aiCache);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAiCache>>();
      const aiCache = { id: 29414 };
      jest.spyOn(aiCacheFormService, 'getAiCache').mockReturnValue(aiCache);
      jest.spyOn(aiCacheService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aiCache });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: aiCache }));
      saveSubject.complete();

      // THEN
      expect(aiCacheFormService.getAiCache).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(aiCacheService.update).toHaveBeenCalledWith(expect.objectContaining(aiCache));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAiCache>>();
      const aiCache = { id: 29414 };
      jest.spyOn(aiCacheFormService, 'getAiCache').mockReturnValue({ id: null });
      jest.spyOn(aiCacheService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aiCache: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: aiCache }));
      saveSubject.complete();

      // THEN
      expect(aiCacheFormService.getAiCache).toHaveBeenCalled();
      expect(aiCacheService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAiCache>>();
      const aiCache = { id: 29414 };
      jest.spyOn(aiCacheService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aiCache });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(aiCacheService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
