import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { ISavedSearch, NewSavedSearch } from '../saved-search.model';

export type PartialUpdateSavedSearch = Partial<ISavedSearch> & Pick<ISavedSearch, 'id'>;

type RestOf<T extends ISavedSearch | NewSavedSearch> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

export type RestSavedSearch = RestOf<ISavedSearch>;

export type NewRestSavedSearch = RestOf<NewSavedSearch>;

export type PartialUpdateRestSavedSearch = RestOf<PartialUpdateSavedSearch>;

export type EntityResponseType = HttpResponse<ISavedSearch>;
export type EntityArrayResponseType = HttpResponse<ISavedSearch[]>;

@Injectable({ providedIn: 'root' })
export class SavedSearchService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/saved-searches', 'documentservice');

  create(savedSearch: NewSavedSearch): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(savedSearch);
    return this.http
      .post<RestSavedSearch>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(savedSearch: ISavedSearch): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(savedSearch);
    return this.http
      .put<RestSavedSearch>(`${this.resourceUrl}/${encodeURIComponent(this.getSavedSearchIdentifier(savedSearch))}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(savedSearch: PartialUpdateSavedSearch): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(savedSearch);
    return this.http
      .patch<RestSavedSearch>(`${this.resourceUrl}/${encodeURIComponent(this.getSavedSearchIdentifier(savedSearch))}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestSavedSearch>(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestSavedSearch[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' });
  }

  getSavedSearchIdentifier(savedSearch: Pick<ISavedSearch, 'id'>): number {
    return savedSearch.id;
  }

  compareSavedSearch(o1: Pick<ISavedSearch, 'id'> | null, o2: Pick<ISavedSearch, 'id'> | null): boolean {
    return o1 && o2 ? this.getSavedSearchIdentifier(o1) === this.getSavedSearchIdentifier(o2) : o1 === o2;
  }

  addSavedSearchToCollectionIfMissing<Type extends Pick<ISavedSearch, 'id'>>(
    savedSearchCollection: Type[],
    ...savedSearchesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const savedSearches: Type[] = savedSearchesToCheck.filter(isPresent);
    if (savedSearches.length > 0) {
      const savedSearchCollectionIdentifiers = savedSearchCollection.map(savedSearchItem => this.getSavedSearchIdentifier(savedSearchItem));
      const savedSearchesToAdd = savedSearches.filter(savedSearchItem => {
        const savedSearchIdentifier = this.getSavedSearchIdentifier(savedSearchItem);
        if (savedSearchCollectionIdentifiers.includes(savedSearchIdentifier)) {
          return false;
        }
        savedSearchCollectionIdentifiers.push(savedSearchIdentifier);
        return true;
      });
      return [...savedSearchesToAdd, ...savedSearchCollection];
    }
    return savedSearchCollection;
  }

  protected convertDateFromClient<T extends ISavedSearch | NewSavedSearch | PartialUpdateSavedSearch>(savedSearch: T): RestOf<T> {
    return {
      ...savedSearch,
      createdDate: savedSearch.createdDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restSavedSearch: RestSavedSearch): ISavedSearch {
    return {
      ...restSavedSearch,
      createdDate: restSavedSearch.createdDate ? dayjs(restSavedSearch.createdDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestSavedSearch>): HttpResponse<ISavedSearch> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestSavedSearch[]>): HttpResponse<ISavedSearch[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
