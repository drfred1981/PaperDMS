import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ISimilarityDocumentFingerprint, NewSimilarityDocumentFingerprint } from '../similarity-document-fingerprint.model';

export type PartialUpdateSimilarityDocumentFingerprint = Partial<ISimilarityDocumentFingerprint> &
  Pick<ISimilarityDocumentFingerprint, 'id'>;

type RestOf<T extends ISimilarityDocumentFingerprint | NewSimilarityDocumentFingerprint> = Omit<T, 'computedDate' | 'lastUpdated'> & {
  computedDate?: string | null;
  lastUpdated?: string | null;
};

export type RestSimilarityDocumentFingerprint = RestOf<ISimilarityDocumentFingerprint>;

export type NewRestSimilarityDocumentFingerprint = RestOf<NewSimilarityDocumentFingerprint>;

export type PartialUpdateRestSimilarityDocumentFingerprint = RestOf<PartialUpdateSimilarityDocumentFingerprint>;

export type EntityResponseType = HttpResponse<ISimilarityDocumentFingerprint>;
export type EntityArrayResponseType = HttpResponse<ISimilarityDocumentFingerprint[]>;

@Injectable({ providedIn: 'root' })
export class SimilarityDocumentFingerprintService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/similarity-document-fingerprints', 'similarityservice');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor(
    'api/similarity-document-fingerprints/_search',
    'similarityservice',
  );

  create(similarityDocumentFingerprint: NewSimilarityDocumentFingerprint): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(similarityDocumentFingerprint);
    return this.http
      .post<RestSimilarityDocumentFingerprint>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(similarityDocumentFingerprint: ISimilarityDocumentFingerprint): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(similarityDocumentFingerprint);
    return this.http
      .put<RestSimilarityDocumentFingerprint>(
        `${this.resourceUrl}/${this.getSimilarityDocumentFingerprintIdentifier(similarityDocumentFingerprint)}`,
        copy,
        { observe: 'response' },
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(similarityDocumentFingerprint: PartialUpdateSimilarityDocumentFingerprint): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(similarityDocumentFingerprint);
    return this.http
      .patch<RestSimilarityDocumentFingerprint>(
        `${this.resourceUrl}/${this.getSimilarityDocumentFingerprintIdentifier(similarityDocumentFingerprint)}`,
        copy,
        { observe: 'response' },
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestSimilarityDocumentFingerprint>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestSimilarityDocumentFingerprint[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestSimilarityDocumentFingerprint[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<ISimilarityDocumentFingerprint[]>()], asapScheduler)),
    );
  }

  getSimilarityDocumentFingerprintIdentifier(similarityDocumentFingerprint: Pick<ISimilarityDocumentFingerprint, 'id'>): number {
    return similarityDocumentFingerprint.id;
  }

  compareSimilarityDocumentFingerprint(
    o1: Pick<ISimilarityDocumentFingerprint, 'id'> | null,
    o2: Pick<ISimilarityDocumentFingerprint, 'id'> | null,
  ): boolean {
    return o1 && o2
      ? this.getSimilarityDocumentFingerprintIdentifier(o1) === this.getSimilarityDocumentFingerprintIdentifier(o2)
      : o1 === o2;
  }

  addSimilarityDocumentFingerprintToCollectionIfMissing<Type extends Pick<ISimilarityDocumentFingerprint, 'id'>>(
    similarityDocumentFingerprintCollection: Type[],
    ...similarityDocumentFingerprintsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const similarityDocumentFingerprints: Type[] = similarityDocumentFingerprintsToCheck.filter(isPresent);
    if (similarityDocumentFingerprints.length > 0) {
      const similarityDocumentFingerprintCollectionIdentifiers = similarityDocumentFingerprintCollection.map(
        similarityDocumentFingerprintItem => this.getSimilarityDocumentFingerprintIdentifier(similarityDocumentFingerprintItem),
      );
      const similarityDocumentFingerprintsToAdd = similarityDocumentFingerprints.filter(similarityDocumentFingerprintItem => {
        const similarityDocumentFingerprintIdentifier = this.getSimilarityDocumentFingerprintIdentifier(similarityDocumentFingerprintItem);
        if (similarityDocumentFingerprintCollectionIdentifiers.includes(similarityDocumentFingerprintIdentifier)) {
          return false;
        }
        similarityDocumentFingerprintCollectionIdentifiers.push(similarityDocumentFingerprintIdentifier);
        return true;
      });
      return [...similarityDocumentFingerprintsToAdd, ...similarityDocumentFingerprintCollection];
    }
    return similarityDocumentFingerprintCollection;
  }

  protected convertDateFromClient<
    T extends ISimilarityDocumentFingerprint | NewSimilarityDocumentFingerprint | PartialUpdateSimilarityDocumentFingerprint,
  >(similarityDocumentFingerprint: T): RestOf<T> {
    return {
      ...similarityDocumentFingerprint,
      computedDate: similarityDocumentFingerprint.computedDate?.toJSON() ?? null,
      lastUpdated: similarityDocumentFingerprint.lastUpdated?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restSimilarityDocumentFingerprint: RestSimilarityDocumentFingerprint): ISimilarityDocumentFingerprint {
    return {
      ...restSimilarityDocumentFingerprint,
      computedDate: restSimilarityDocumentFingerprint.computedDate ? dayjs(restSimilarityDocumentFingerprint.computedDate) : undefined,
      lastUpdated: restSimilarityDocumentFingerprint.lastUpdated ? dayjs(restSimilarityDocumentFingerprint.lastUpdated) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestSimilarityDocumentFingerprint>): HttpResponse<ISimilarityDocumentFingerprint> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(
    res: HttpResponse<RestSimilarityDocumentFingerprint[]>,
  ): HttpResponse<ISimilarityDocumentFingerprint[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
