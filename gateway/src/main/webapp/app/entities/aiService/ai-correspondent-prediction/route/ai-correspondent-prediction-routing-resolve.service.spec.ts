import { TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { ActivatedRoute, ActivatedRouteSnapshot, Router, convertToParamMap } from '@angular/router';
import { of } from 'rxjs';

import { IAICorrespondentPrediction } from '../ai-correspondent-prediction.model';
import { AICorrespondentPredictionService } from '../service/ai-correspondent-prediction.service';

import aICorrespondentPredictionResolve from './ai-correspondent-prediction-routing-resolve.service';

describe('AICorrespondentPrediction routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: AICorrespondentPredictionService;
  let resultAICorrespondentPrediction: IAICorrespondentPrediction | null | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    service = TestBed.inject(AICorrespondentPredictionService);
    resultAICorrespondentPrediction = undefined;
  });

  describe('resolve', () => {
    it('should return IAICorrespondentPrediction returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        aICorrespondentPredictionResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultAICorrespondentPrediction = result;
          },
        });
      });

      // THEN
      expect(service.find).toHaveBeenCalledWith(123);
      expect(resultAICorrespondentPrediction).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      TestBed.runInInjectionContext(() => {
        aICorrespondentPredictionResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultAICorrespondentPrediction = result;
          },
        });
      });

      // THEN
      expect(service.find).not.toHaveBeenCalled();
      expect(resultAICorrespondentPrediction).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IAICorrespondentPrediction>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        aICorrespondentPredictionResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultAICorrespondentPrediction = result;
          },
        });
      });

      // THEN
      expect(service.find).toHaveBeenCalledWith(123);
      expect(resultAICorrespondentPrediction).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
