import { TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { ActivatedRoute, ActivatedRouteSnapshot, Router, convertToParamMap } from '@angular/router';
import { of } from 'rxjs';

import { ISimilarityDocumentComparison } from '../similarity-document-comparison.model';
import { SimilarityDocumentComparisonService } from '../service/similarity-document-comparison.service';

import similarityDocumentComparisonResolve from './similarity-document-comparison-routing-resolve.service';

describe('SimilarityDocumentComparison routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: SimilarityDocumentComparisonService;
  let resultSimilarityDocumentComparison: ISimilarityDocumentComparison | null | undefined;

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
    service = TestBed.inject(SimilarityDocumentComparisonService);
    resultSimilarityDocumentComparison = undefined;
  });

  describe('resolve', () => {
    it('should return ISimilarityDocumentComparison returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        similarityDocumentComparisonResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultSimilarityDocumentComparison = result;
          },
        });
      });

      // THEN
      expect(service.find).toHaveBeenCalledWith(123);
      expect(resultSimilarityDocumentComparison).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      TestBed.runInInjectionContext(() => {
        similarityDocumentComparisonResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultSimilarityDocumentComparison = result;
          },
        });
      });

      // THEN
      expect(service.find).not.toHaveBeenCalled();
      expect(resultSimilarityDocumentComparison).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<ISimilarityDocumentComparison>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        similarityDocumentComparisonResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultSimilarityDocumentComparison = result;
          },
        });
      });

      // THEN
      expect(service.find).toHaveBeenCalledWith(123);
      expect(resultSimilarityDocumentComparison).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
