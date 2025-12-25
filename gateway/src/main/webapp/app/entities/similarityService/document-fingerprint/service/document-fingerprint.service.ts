import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IDocumentFingerprint, NewDocumentFingerprint } from '../document-fingerprint.model';

export type PartialUpdateDocumentFingerprint = Partial<IDocumentFingerprint> & Pick<IDocumentFingerprint, 'id'>;

type RestOf<T extends IDocumentFingerprint | NewDocumentFingerprint> = Omit<T, 'computedDate' | 'lastUpdated'> & {
  computedDate?: string | null;
  lastUpdated?: string | null;
};

export type RestDocumentFingerprint = RestOf<IDocumentFingerprint>;

export type NewRestDocumentFingerprint = RestOf<NewDocumentFingerprint>;

export type PartialUpdateRestDocumentFingerprint = RestOf<PartialUpdateDocumentFingerprint>;

export type EntityResponseType = HttpResponse<IDocumentFingerprint>;
export type EntityArrayResponseType = HttpResponse<IDocumentFingerprint[]>;

@Injectable({ providedIn: 'root' })
export class DocumentFingerprintService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/document-fingerprints', 'similarityservice');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/document-fingerprints/_search', 'similarityservice');

  create(documentFingerprint: NewDocumentFingerprint): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentFingerprint);
    return this.http
      .post<RestDocumentFingerprint>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(documentFingerprint: IDocumentFingerprint): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentFingerprint);
    return this.http
      .put<RestDocumentFingerprint>(`${this.resourceUrl}/${this.getDocumentFingerprintIdentifier(documentFingerprint)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(documentFingerprint: PartialUpdateDocumentFingerprint): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentFingerprint);
    return this.http
      .patch<RestDocumentFingerprint>(`${this.resourceUrl}/${this.getDocumentFingerprintIdentifier(documentFingerprint)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestDocumentFingerprint>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestDocumentFingerprint[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestDocumentFingerprint[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IDocumentFingerprint[]>()], asapScheduler)),
    );
  }

  getDocumentFingerprintIdentifier(documentFingerprint: Pick<IDocumentFingerprint, 'id'>): number {
    return documentFingerprint.id;
  }

  compareDocumentFingerprint(o1: Pick<IDocumentFingerprint, 'id'> | null, o2: Pick<IDocumentFingerprint, 'id'> | null): boolean {
    return o1 && o2 ? this.getDocumentFingerprintIdentifier(o1) === this.getDocumentFingerprintIdentifier(o2) : o1 === o2;
  }

  addDocumentFingerprintToCollectionIfMissing<Type extends Pick<IDocumentFingerprint, 'id'>>(
    documentFingerprintCollection: Type[],
    ...documentFingerprintsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const documentFingerprints: Type[] = documentFingerprintsToCheck.filter(isPresent);
    if (documentFingerprints.length > 0) {
      const documentFingerprintCollectionIdentifiers = documentFingerprintCollection.map(documentFingerprintItem =>
        this.getDocumentFingerprintIdentifier(documentFingerprintItem),
      );
      const documentFingerprintsToAdd = documentFingerprints.filter(documentFingerprintItem => {
        const documentFingerprintIdentifier = this.getDocumentFingerprintIdentifier(documentFingerprintItem);
        if (documentFingerprintCollectionIdentifiers.includes(documentFingerprintIdentifier)) {
          return false;
        }
        documentFingerprintCollectionIdentifiers.push(documentFingerprintIdentifier);
        return true;
      });
      return [...documentFingerprintsToAdd, ...documentFingerprintCollection];
    }
    return documentFingerprintCollection;
  }

  protected convertDateFromClient<T extends IDocumentFingerprint | NewDocumentFingerprint | PartialUpdateDocumentFingerprint>(
    documentFingerprint: T,
  ): RestOf<T> {
    return {
      ...documentFingerprint,
      computedDate: documentFingerprint.computedDate?.toJSON() ?? null,
      lastUpdated: documentFingerprint.lastUpdated?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restDocumentFingerprint: RestDocumentFingerprint): IDocumentFingerprint {
    return {
      ...restDocumentFingerprint,
      computedDate: restDocumentFingerprint.computedDate ? dayjs(restDocumentFingerprint.computedDate) : undefined,
      lastUpdated: restDocumentFingerprint.lastUpdated ? dayjs(restDocumentFingerprint.lastUpdated) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestDocumentFingerprint>): HttpResponse<IDocumentFingerprint> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestDocumentFingerprint[]>): HttpResponse<IDocumentFingerprint[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
