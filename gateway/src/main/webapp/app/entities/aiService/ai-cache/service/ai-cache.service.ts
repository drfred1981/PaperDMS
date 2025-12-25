import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAiCache, NewAiCache } from '../ai-cache.model';

export type PartialUpdateAiCache = Partial<IAiCache> & Pick<IAiCache, 'id'>;

type RestOf<T extends IAiCache | NewAiCache> = Omit<T, 'lastAccessDate' | 'createdDate' | 'expirationDate'> & {
  lastAccessDate?: string | null;
  createdDate?: string | null;
  expirationDate?: string | null;
};

export type RestAiCache = RestOf<IAiCache>;

export type NewRestAiCache = RestOf<NewAiCache>;

export type PartialUpdateRestAiCache = RestOf<PartialUpdateAiCache>;

export type EntityResponseType = HttpResponse<IAiCache>;
export type EntityArrayResponseType = HttpResponse<IAiCache[]>;

@Injectable({ providedIn: 'root' })
export class AiCacheService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ai-caches', 'aiservice');

  create(aiCache: NewAiCache): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(aiCache);
    return this.http
      .post<RestAiCache>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(aiCache: IAiCache): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(aiCache);
    return this.http
      .put<RestAiCache>(`${this.resourceUrl}/${this.getAiCacheIdentifier(aiCache)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(aiCache: PartialUpdateAiCache): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(aiCache);
    return this.http
      .patch<RestAiCache>(`${this.resourceUrl}/${this.getAiCacheIdentifier(aiCache)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestAiCache>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestAiCache[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAiCacheIdentifier(aiCache: Pick<IAiCache, 'id'>): number {
    return aiCache.id;
  }

  compareAiCache(o1: Pick<IAiCache, 'id'> | null, o2: Pick<IAiCache, 'id'> | null): boolean {
    return o1 && o2 ? this.getAiCacheIdentifier(o1) === this.getAiCacheIdentifier(o2) : o1 === o2;
  }

  addAiCacheToCollectionIfMissing<Type extends Pick<IAiCache, 'id'>>(
    aiCacheCollection: Type[],
    ...aiCachesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const aiCaches: Type[] = aiCachesToCheck.filter(isPresent);
    if (aiCaches.length > 0) {
      const aiCacheCollectionIdentifiers = aiCacheCollection.map(aiCacheItem => this.getAiCacheIdentifier(aiCacheItem));
      const aiCachesToAdd = aiCaches.filter(aiCacheItem => {
        const aiCacheIdentifier = this.getAiCacheIdentifier(aiCacheItem);
        if (aiCacheCollectionIdentifiers.includes(aiCacheIdentifier)) {
          return false;
        }
        aiCacheCollectionIdentifiers.push(aiCacheIdentifier);
        return true;
      });
      return [...aiCachesToAdd, ...aiCacheCollection];
    }
    return aiCacheCollection;
  }

  protected convertDateFromClient<T extends IAiCache | NewAiCache | PartialUpdateAiCache>(aiCache: T): RestOf<T> {
    return {
      ...aiCache,
      lastAccessDate: aiCache.lastAccessDate?.toJSON() ?? null,
      createdDate: aiCache.createdDate?.toJSON() ?? null,
      expirationDate: aiCache.expirationDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restAiCache: RestAiCache): IAiCache {
    return {
      ...restAiCache,
      lastAccessDate: restAiCache.lastAccessDate ? dayjs(restAiCache.lastAccessDate) : undefined,
      createdDate: restAiCache.createdDate ? dayjs(restAiCache.createdDate) : undefined,
      expirationDate: restAiCache.expirationDate ? dayjs(restAiCache.expirationDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestAiCache>): HttpResponse<IAiCache> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestAiCache[]>): HttpResponse<IAiCache[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
