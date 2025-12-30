import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { AICacheService } from '../service/ai-cache.service';
import { IAICache } from '../ai-cache.model';
import { AICacheFormService } from './ai-cache-form.service';

import { AICacheUpdateComponent } from './ai-cache-update.component';

describe('AICache Management Update Component', () => {
  let comp: AICacheUpdateComponent;
  let fixture: ComponentFixture<AICacheUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let aICacheFormService: AICacheFormService;
  let aICacheService: AICacheService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AICacheUpdateComponent],
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
      .overrideTemplate(AICacheUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AICacheUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    aICacheFormService = TestBed.inject(AICacheFormService);
    aICacheService = TestBed.inject(AICacheService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const aICache: IAICache = { id: 16798 };

      activatedRoute.data = of({ aICache });
      comp.ngOnInit();

      expect(comp.aICache).toEqual(aICache);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAICache>>();
      const aICache = { id: 29414 };
      jest.spyOn(aICacheFormService, 'getAICache').mockReturnValue(aICache);
      jest.spyOn(aICacheService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aICache });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: aICache }));
      saveSubject.complete();

      // THEN
      expect(aICacheFormService.getAICache).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(aICacheService.update).toHaveBeenCalledWith(expect.objectContaining(aICache));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAICache>>();
      const aICache = { id: 29414 };
      jest.spyOn(aICacheFormService, 'getAICache').mockReturnValue({ id: null });
      jest.spyOn(aICacheService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aICache: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: aICache }));
      saveSubject.complete();

      // THEN
      expect(aICacheFormService.getAICache).toHaveBeenCalled();
      expect(aICacheService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAICache>>();
      const aICache = { id: 29414 };
      jest.spyOn(aICacheService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aICache });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(aICacheService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
