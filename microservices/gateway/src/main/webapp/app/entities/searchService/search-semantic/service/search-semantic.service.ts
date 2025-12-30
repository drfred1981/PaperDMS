import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ISearchSemantic, NewSearchSemantic } from '../search-semantic.model';

export type PartialUpdateSearchSemantic = Partial<ISearchSemantic> & Pick<ISearchSemantic, 'id'>;

type RestOf<T extends ISearchSemantic | NewSearchSemantic> = Omit<T, 'searchDate'> & {
  searchDate?: string | null;
};

export type RestSearchSemantic = RestOf<ISearchSemantic>;

export type NewRestSearchSemantic = RestOf<NewSearchSemantic>;

export type PartialUpdateRestSearchSemantic = RestOf<PartialUpdateSearchSemantic>;

export type EntityResponseType = HttpResponse<ISearchSemantic>;
export type EntityArrayResponseType = HttpResponse<ISearchSemantic[]>;

@Injectable({ providedIn: 'root' })
export class SearchSemanticService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/search-semantics', 'searchservice');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/search-semantics/_search', 'searchservice');

  create(searchSemantic: NewSearchSemantic): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(searchSemantic);
    return this.http
      .post<RestSearchSemantic>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(searchSemantic: ISearchSemantic): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(searchSemantic);
    return this.http
      .put<RestSearchSemantic>(`${this.resourceUrl}/${this.getSearchSemanticIdentifier(searchSemantic)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(searchSemantic: PartialUpdateSearchSemantic): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(searchSemantic);
    return this.http
      .patch<RestSearchSemantic>(`${this.resourceUrl}/${this.getSearchSemanticIdentifier(searchSemantic)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestSearchSemantic>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestSearchSemantic[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestSearchSemantic[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<ISearchSemantic[]>()], asapScheduler)),
    );
  }

  getSearchSemanticIdentifier(searchSemantic: Pick<ISearchSemantic, 'id'>): number {
    return searchSemantic.id;
  }

  compareSearchSemantic(o1: Pick<ISearchSemantic, 'id'> | null, o2: Pick<ISearchSemantic, 'id'> | null): boolean {
    return o1 && o2 ? this.getSearchSemanticIdentifier(o1) === this.getSearchSemanticIdentifier(o2) : o1 === o2;
  }

  addSearchSemanticToCollectionIfMissing<Type extends Pick<ISearchSemantic, 'id'>>(
    searchSemanticCollection: Type[],
    ...searchSemanticsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const searchSemantics: Type[] = searchSemanticsToCheck.filter(isPresent);
    if (searchSemantics.length > 0) {
      const searchSemanticCollectionIdentifiers = searchSemanticCollection.map(searchSemanticItem =>
        this.getSearchSemanticIdentifier(searchSemanticItem),
      );
      const searchSemanticsToAdd = searchSemantics.filter(searchSemanticItem => {
        const searchSemanticIdentifier = this.getSearchSemanticIdentifier(searchSemanticItem);
        if (searchSemanticCollectionIdentifiers.includes(searchSemanticIdentifier)) {
          return false;
        }
        searchSemanticCollectionIdentifiers.push(searchSemanticIdentifier);
        return true;
      });
      return [...searchSemanticsToAdd, ...searchSemanticCollection];
    }
    return searchSemanticCollection;
  }

  protected convertDateFromClient<T extends ISearchSemantic | NewSearchSemantic | PartialUpdateSearchSemantic>(
    searchSemantic: T,
  ): RestOf<T> {
    return {
      ...searchSemantic,
      searchDate: searchSemantic.searchDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restSearchSemantic: RestSearchSemantic): ISearchSemantic {
    return {
      ...restSearchSemantic,
      searchDate: restSearchSemantic.searchDate ? dayjs(restSearchSemantic.searchDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestSearchSemantic>): HttpResponse<ISearchSemantic> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestSearchSemantic[]>): HttpResponse<ISearchSemantic[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
