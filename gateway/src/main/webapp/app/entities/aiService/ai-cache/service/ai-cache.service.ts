import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IAICache, NewAICache } from '../ai-cache.model';

export type PartialUpdateAICache = Partial<IAICache> & Pick<IAICache, 'id'>;

type RestOf<T extends IAICache | NewAICache> = Omit<T, 'lastAccessDate' | 'createdDate' | 'expirationDate'> & {
  lastAccessDate?: string | null;
  createdDate?: string | null;
  expirationDate?: string | null;
};

export type RestAICache = RestOf<IAICache>;

export type NewRestAICache = RestOf<NewAICache>;

export type PartialUpdateRestAICache = RestOf<PartialUpdateAICache>;

export type EntityResponseType = HttpResponse<IAICache>;
export type EntityArrayResponseType = HttpResponse<IAICache[]>;

@Injectable({ providedIn: 'root' })
export class AICacheService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ai-caches', 'aiservice');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/ai-caches/_search', 'aiservice');

  create(aICache: NewAICache): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(aICache);
    return this.http
      .post<RestAICache>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(aICache: IAICache): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(aICache);
    return this.http
      .put<RestAICache>(`${this.resourceUrl}/${this.getAICacheIdentifier(aICache)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(aICache: PartialUpdateAICache): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(aICache);
    return this.http
      .patch<RestAICache>(`${this.resourceUrl}/${this.getAICacheIdentifier(aICache)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestAICache>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestAICache[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestAICache[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IAICache[]>()], asapScheduler)),
    );
  }

  getAICacheIdentifier(aICache: Pick<IAICache, 'id'>): number {
    return aICache.id;
  }

  compareAICache(o1: Pick<IAICache, 'id'> | null, o2: Pick<IAICache, 'id'> | null): boolean {
    return o1 && o2 ? this.getAICacheIdentifier(o1) === this.getAICacheIdentifier(o2) : o1 === o2;
  }

  addAICacheToCollectionIfMissing<Type extends Pick<IAICache, 'id'>>(
    aICacheCollection: Type[],
    ...aICachesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const aICaches: Type[] = aICachesToCheck.filter(isPresent);
    if (aICaches.length > 0) {
      const aICacheCollectionIdentifiers = aICacheCollection.map(aICacheItem => this.getAICacheIdentifier(aICacheItem));
      const aICachesToAdd = aICaches.filter(aICacheItem => {
        const aICacheIdentifier = this.getAICacheIdentifier(aICacheItem);
        if (aICacheCollectionIdentifiers.includes(aICacheIdentifier)) {
          return false;
        }
        aICacheCollectionIdentifiers.push(aICacheIdentifier);
        return true;
      });
      return [...aICachesToAdd, ...aICacheCollection];
    }
    return aICacheCollection;
  }

  protected convertDateFromClient<T extends IAICache | NewAICache | PartialUpdateAICache>(aICache: T): RestOf<T> {
    return {
      ...aICache,
      lastAccessDate: aICache.lastAccessDate?.toJSON() ?? null,
      createdDate: aICache.createdDate?.toJSON() ?? null,
      expirationDate: aICache.expirationDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restAICache: RestAICache): IAICache {
    return {
      ...restAICache,
      lastAccessDate: restAICache.lastAccessDate ? dayjs(restAICache.lastAccessDate) : undefined,
      createdDate: restAICache.createdDate ? dayjs(restAICache.createdDate) : undefined,
      expirationDate: restAICache.expirationDate ? dayjs(restAICache.expirationDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestAICache>): HttpResponse<IAICache> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestAICache[]>): HttpResponse<IAICache[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
