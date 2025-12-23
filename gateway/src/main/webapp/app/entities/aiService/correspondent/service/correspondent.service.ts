import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { isPresent } from 'app/core/util/operators';
import { ICorrespondent, NewCorrespondent } from '../correspondent.model';

export type PartialUpdateCorrespondent = Partial<ICorrespondent> & Pick<ICorrespondent, 'id'>;

type RestOf<T extends ICorrespondent | NewCorrespondent> = Omit<T, 'verifiedDate' | 'extractedDate'> & {
  verifiedDate?: string | null;
  extractedDate?: string | null;
};

export type RestCorrespondent = RestOf<ICorrespondent>;

export type NewRestCorrespondent = RestOf<NewCorrespondent>;

export type PartialUpdateRestCorrespondent = RestOf<PartialUpdateCorrespondent>;

export type EntityResponseType = HttpResponse<ICorrespondent>;
export type EntityArrayResponseType = HttpResponse<ICorrespondent[]>;

@Injectable({ providedIn: 'root' })
export class CorrespondentService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/correspondents', 'aiservice');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/correspondents/_search', 'aiservice');

  create(correspondent: NewCorrespondent): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(correspondent);
    return this.http
      .post<RestCorrespondent>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(correspondent: ICorrespondent): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(correspondent);
    return this.http
      .put<RestCorrespondent>(`${this.resourceUrl}/${encodeURIComponent(this.getCorrespondentIdentifier(correspondent))}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(correspondent: PartialUpdateCorrespondent): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(correspondent);
    return this.http
      .patch<RestCorrespondent>(`${this.resourceUrl}/${encodeURIComponent(this.getCorrespondentIdentifier(correspondent))}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestCorrespondent>(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestCorrespondent[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestCorrespondent[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<ICorrespondent[]>()], asapScheduler)),
    );
  }

  getCorrespondentIdentifier(correspondent: Pick<ICorrespondent, 'id'>): number {
    return correspondent.id;
  }

  compareCorrespondent(o1: Pick<ICorrespondent, 'id'> | null, o2: Pick<ICorrespondent, 'id'> | null): boolean {
    return o1 && o2 ? this.getCorrespondentIdentifier(o1) === this.getCorrespondentIdentifier(o2) : o1 === o2;
  }

  addCorrespondentToCollectionIfMissing<Type extends Pick<ICorrespondent, 'id'>>(
    correspondentCollection: Type[],
    ...correspondentsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const correspondents: Type[] = correspondentsToCheck.filter(isPresent);
    if (correspondents.length > 0) {
      const correspondentCollectionIdentifiers = correspondentCollection.map(correspondentItem =>
        this.getCorrespondentIdentifier(correspondentItem),
      );
      const correspondentsToAdd = correspondents.filter(correspondentItem => {
        const correspondentIdentifier = this.getCorrespondentIdentifier(correspondentItem);
        if (correspondentCollectionIdentifiers.includes(correspondentIdentifier)) {
          return false;
        }
        correspondentCollectionIdentifiers.push(correspondentIdentifier);
        return true;
      });
      return [...correspondentsToAdd, ...correspondentCollection];
    }
    return correspondentCollection;
  }

  protected convertDateFromClient<T extends ICorrespondent | NewCorrespondent | PartialUpdateCorrespondent>(correspondent: T): RestOf<T> {
    return {
      ...correspondent,
      verifiedDate: correspondent.verifiedDate?.toJSON() ?? null,
      extractedDate: correspondent.extractedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restCorrespondent: RestCorrespondent): ICorrespondent {
    return {
      ...restCorrespondent,
      verifiedDate: restCorrespondent.verifiedDate ? dayjs(restCorrespondent.verifiedDate) : undefined,
      extractedDate: restCorrespondent.extractedDate ? dayjs(restCorrespondent.extractedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestCorrespondent>): HttpResponse<ICorrespondent> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestCorrespondent[]>): HttpResponse<ICorrespondent[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
