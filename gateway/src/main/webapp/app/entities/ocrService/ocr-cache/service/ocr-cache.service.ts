import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { IOcrCache, NewOcrCache } from '../ocr-cache.model';

export type PartialUpdateOcrCache = Partial<IOcrCache> & Pick<IOcrCache, 'id'>;

type RestOf<T extends IOcrCache | NewOcrCache> = Omit<T, 'lastAccessDate' | 'createdDate' | 'expirationDate'> & {
  lastAccessDate?: string | null;
  createdDate?: string | null;
  expirationDate?: string | null;
};

export type RestOcrCache = RestOf<IOcrCache>;

export type NewRestOcrCache = RestOf<NewOcrCache>;

export type PartialUpdateRestOcrCache = RestOf<PartialUpdateOcrCache>;

export type EntityResponseType = HttpResponse<IOcrCache>;
export type EntityArrayResponseType = HttpResponse<IOcrCache[]>;

@Injectable({ providedIn: 'root' })
export class OcrCacheService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ocr-caches', 'ocrservice');

  create(ocrCache: NewOcrCache): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ocrCache);
    return this.http
      .post<RestOcrCache>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(ocrCache: IOcrCache): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ocrCache);
    return this.http
      .put<RestOcrCache>(`${this.resourceUrl}/${encodeURIComponent(this.getOcrCacheIdentifier(ocrCache))}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(ocrCache: PartialUpdateOcrCache): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ocrCache);
    return this.http
      .patch<RestOcrCache>(`${this.resourceUrl}/${encodeURIComponent(this.getOcrCacheIdentifier(ocrCache))}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestOcrCache>(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestOcrCache[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' });
  }

  getOcrCacheIdentifier(ocrCache: Pick<IOcrCache, 'id'>): number {
    return ocrCache.id;
  }

  compareOcrCache(o1: Pick<IOcrCache, 'id'> | null, o2: Pick<IOcrCache, 'id'> | null): boolean {
    return o1 && o2 ? this.getOcrCacheIdentifier(o1) === this.getOcrCacheIdentifier(o2) : o1 === o2;
  }

  addOcrCacheToCollectionIfMissing<Type extends Pick<IOcrCache, 'id'>>(
    ocrCacheCollection: Type[],
    ...ocrCachesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const ocrCaches: Type[] = ocrCachesToCheck.filter(isPresent);
    if (ocrCaches.length > 0) {
      const ocrCacheCollectionIdentifiers = ocrCacheCollection.map(ocrCacheItem => this.getOcrCacheIdentifier(ocrCacheItem));
      const ocrCachesToAdd = ocrCaches.filter(ocrCacheItem => {
        const ocrCacheIdentifier = this.getOcrCacheIdentifier(ocrCacheItem);
        if (ocrCacheCollectionIdentifiers.includes(ocrCacheIdentifier)) {
          return false;
        }
        ocrCacheCollectionIdentifiers.push(ocrCacheIdentifier);
        return true;
      });
      return [...ocrCachesToAdd, ...ocrCacheCollection];
    }
    return ocrCacheCollection;
  }

  protected convertDateFromClient<T extends IOcrCache | NewOcrCache | PartialUpdateOcrCache>(ocrCache: T): RestOf<T> {
    return {
      ...ocrCache,
      lastAccessDate: ocrCache.lastAccessDate?.toJSON() ?? null,
      createdDate: ocrCache.createdDate?.toJSON() ?? null,
      expirationDate: ocrCache.expirationDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restOcrCache: RestOcrCache): IOcrCache {
    return {
      ...restOcrCache,
      lastAccessDate: restOcrCache.lastAccessDate ? dayjs(restOcrCache.lastAccessDate) : undefined,
      createdDate: restOcrCache.createdDate ? dayjs(restOcrCache.createdDate) : undefined,
      expirationDate: restOcrCache.expirationDate ? dayjs(restOcrCache.expirationDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestOcrCache>): HttpResponse<IOcrCache> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestOcrCache[]>): HttpResponse<IOcrCache[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
