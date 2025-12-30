import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { SimilarityClusterService } from '../service/similarity-cluster.service';
import { ISimilarityCluster } from '../similarity-cluster.model';
import { SimilarityClusterFormService } from './similarity-cluster-form.service';

import { SimilarityClusterUpdateComponent } from './similarity-cluster-update.component';

describe('SimilarityCluster Management Update Component', () => {
  let comp: SimilarityClusterUpdateComponent;
  let fixture: ComponentFixture<SimilarityClusterUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let similarityClusterFormService: SimilarityClusterFormService;
  let similarityClusterService: SimilarityClusterService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [SimilarityClusterUpdateComponent],
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
      .overrideTemplate(SimilarityClusterUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SimilarityClusterUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    similarityClusterFormService = TestBed.inject(SimilarityClusterFormService);
    similarityClusterService = TestBed.inject(SimilarityClusterService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const similarityCluster: ISimilarityCluster = { id: 15350 };

      activatedRoute.data = of({ similarityCluster });
      comp.ngOnInit();

      expect(comp.similarityCluster).toEqual(similarityCluster);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISimilarityCluster>>();
      const similarityCluster = { id: 27919 };
      jest.spyOn(similarityClusterFormService, 'getSimilarityCluster').mockReturnValue(similarityCluster);
      jest.spyOn(similarityClusterService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ similarityCluster });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: similarityCluster }));
      saveSubject.complete();

      // THEN
      expect(similarityClusterFormService.getSimilarityCluster).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(similarityClusterService.update).toHaveBeenCalledWith(expect.objectContaining(similarityCluster));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISimilarityCluster>>();
      const similarityCluster = { id: 27919 };
      jest.spyOn(similarityClusterFormService, 'getSimilarityCluster').mockReturnValue({ id: null });
      jest.spyOn(similarityClusterService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ similarityCluster: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: similarityCluster }));
      saveSubject.complete();

      // THEN
      expect(similarityClusterFormService.getSimilarityCluster).toHaveBeenCalled();
      expect(similarityClusterService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISimilarityCluster>>();
      const similarityCluster = { id: 27919 };
      jest.spyOn(similarityClusterService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ similarityCluster });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(similarityClusterService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
