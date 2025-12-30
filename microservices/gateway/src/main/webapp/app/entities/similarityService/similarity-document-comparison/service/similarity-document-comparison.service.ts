import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ISimilarityDocumentComparison, NewSimilarityDocumentComparison } from '../similarity-document-comparison.model';

export type PartialUpdateSimilarityDocumentComparison = Partial<ISimilarityDocumentComparison> & Pick<ISimilarityDocumentComparison, 'id'>;

type RestOf<T extends ISimilarityDocumentComparison | NewSimilarityDocumentComparison> = Omit<T, 'computedDate' | 'reviewedDate'> & {
  computedDate?: string | null;
  reviewedDate?: string | null;
};

export type RestSimilarityDocumentComparison = RestOf<ISimilarityDocumentComparison>;

export type NewRestSimilarityDocumentComparison = RestOf<NewSimilarityDocumentComparison>;

export type PartialUpdateRestSimilarityDocumentComparison = RestOf<PartialUpdateSimilarityDocumentComparison>;

export type EntityResponseType = HttpResponse<ISimilarityDocumentComparison>;
export type EntityArrayResponseType = HttpResponse<ISimilarityDocumentComparison[]>;

@Injectable({ providedIn: 'root' })
export class SimilarityDocumentComparisonService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/similarity-document-comparisons', 'similarityservice');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor(
    'api/similarity-document-comparisons/_search',
    'similarityservice',
  );

  create(similarityDocumentComparison: NewSimilarityDocumentComparison): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(similarityDocumentComparison);
    return this.http
      .post<RestSimilarityDocumentComparison>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(similarityDocumentComparison: ISimilarityDocumentComparison): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(similarityDocumentComparison);
    return this.http
      .put<RestSimilarityDocumentComparison>(
        `${this.resourceUrl}/${this.getSimilarityDocumentComparisonIdentifier(similarityDocumentComparison)}`,
        copy,
        { observe: 'response' },
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(similarityDocumentComparison: PartialUpdateSimilarityDocumentComparison): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(similarityDocumentComparison);
    return this.http
      .patch<RestSimilarityDocumentComparison>(
        `${this.resourceUrl}/${this.getSimilarityDocumentComparisonIdentifier(similarityDocumentComparison)}`,
        copy,
        { observe: 'response' },
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestSimilarityDocumentComparison>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestSimilarityDocumentComparison[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestSimilarityDocumentComparison[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<ISimilarityDocumentComparison[]>()], asapScheduler)),
    );
  }

  getSimilarityDocumentComparisonIdentifier(similarityDocumentComparison: Pick<ISimilarityDocumentComparison, 'id'>): number {
    return similarityDocumentComparison.id;
  }

  compareSimilarityDocumentComparison(
    o1: Pick<ISimilarityDocumentComparison, 'id'> | null,
    o2: Pick<ISimilarityDocumentComparison, 'id'> | null,
  ): boolean {
    return o1 && o2 ? this.getSimilarityDocumentComparisonIdentifier(o1) === this.getSimilarityDocumentComparisonIdentifier(o2) : o1 === o2;
  }

  addSimilarityDocumentComparisonToCollectionIfMissing<Type extends Pick<ISimilarityDocumentComparison, 'id'>>(
    similarityDocumentComparisonCollection: Type[],
    ...similarityDocumentComparisonsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const similarityDocumentComparisons: Type[] = similarityDocumentComparisonsToCheck.filter(isPresent);
    if (similarityDocumentComparisons.length > 0) {
      const similarityDocumentComparisonCollectionIdentifiers = similarityDocumentComparisonCollection.map(
        similarityDocumentComparisonItem => this.getSimilarityDocumentComparisonIdentifier(similarityDocumentComparisonItem),
      );
      const similarityDocumentComparisonsToAdd = similarityDocumentComparisons.filter(similarityDocumentComparisonItem => {
        const similarityDocumentComparisonIdentifier = this.getSimilarityDocumentComparisonIdentifier(similarityDocumentComparisonItem);
        if (similarityDocumentComparisonCollectionIdentifiers.includes(similarityDocumentComparisonIdentifier)) {
          return false;
        }
        similarityDocumentComparisonCollectionIdentifiers.push(similarityDocumentComparisonIdentifier);
        return true;
      });
      return [...similarityDocumentComparisonsToAdd, ...similarityDocumentComparisonCollection];
    }
    return similarityDocumentComparisonCollection;
  }

  protected convertDateFromClient<
    T extends ISimilarityDocumentComparison | NewSimilarityDocumentComparison | PartialUpdateSimilarityDocumentComparison,
  >(similarityDocumentComparison: T): RestOf<T> {
    return {
      ...similarityDocumentComparison,
      computedDate: similarityDocumentComparison.computedDate?.toJSON() ?? null,
      reviewedDate: similarityDocumentComparison.reviewedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restSimilarityDocumentComparison: RestSimilarityDocumentComparison): ISimilarityDocumentComparison {
    return {
      ...restSimilarityDocumentComparison,
      computedDate: restSimilarityDocumentComparison.computedDate ? dayjs(restSimilarityDocumentComparison.computedDate) : undefined,
      reviewedDate: restSimilarityDocumentComparison.reviewedDate ? dayjs(restSimilarityDocumentComparison.reviewedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestSimilarityDocumentComparison>): HttpResponse<ISimilarityDocumentComparison> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(
    res: HttpResponse<RestSimilarityDocumentComparison[]>,
  ): HttpResponse<ISimilarityDocumentComparison[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
