import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { isPresent } from 'app/core/util/operators';
import { IManual, NewManual } from '../manual.model';

export type PartialUpdateManual = Partial<IManual> & Pick<IManual, 'id'>;

type RestOf<T extends IManual | NewManual> = Omit<T, 'publicationDate' | 'createdDate'> & {
  publicationDate?: string | null;
  createdDate?: string | null;
};

export type RestManual = RestOf<IManual>;

export type NewRestManual = RestOf<NewManual>;

export type PartialUpdateRestManual = RestOf<PartialUpdateManual>;

export type EntityResponseType = HttpResponse<IManual>;
export type EntityArrayResponseType = HttpResponse<IManual[]>;

@Injectable({ providedIn: 'root' })
export class ManualService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/manuals', 'businessdocservice');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/manuals/_search', 'businessdocservice');

  create(manual: NewManual): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(manual);
    return this.http
      .post<RestManual>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(manual: IManual): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(manual);
    return this.http
      .put<RestManual>(`${this.resourceUrl}/${encodeURIComponent(this.getManualIdentifier(manual))}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(manual: PartialUpdateManual): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(manual);
    return this.http
      .patch<RestManual>(`${this.resourceUrl}/${encodeURIComponent(this.getManualIdentifier(manual))}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestManual>(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestManual[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestManual[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IManual[]>()], asapScheduler)),
    );
  }

  getManualIdentifier(manual: Pick<IManual, 'id'>): number {
    return manual.id;
  }

  compareManual(o1: Pick<IManual, 'id'> | null, o2: Pick<IManual, 'id'> | null): boolean {
    return o1 && o2 ? this.getManualIdentifier(o1) === this.getManualIdentifier(o2) : o1 === o2;
  }

  addManualToCollectionIfMissing<Type extends Pick<IManual, 'id'>>(
    manualCollection: Type[],
    ...manualsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const manuals: Type[] = manualsToCheck.filter(isPresent);
    if (manuals.length > 0) {
      const manualCollectionIdentifiers = manualCollection.map(manualItem => this.getManualIdentifier(manualItem));
      const manualsToAdd = manuals.filter(manualItem => {
        const manualIdentifier = this.getManualIdentifier(manualItem);
        if (manualCollectionIdentifiers.includes(manualIdentifier)) {
          return false;
        }
        manualCollectionIdentifiers.push(manualIdentifier);
        return true;
      });
      return [...manualsToAdd, ...manualCollection];
    }
    return manualCollection;
  }

  protected convertDateFromClient<T extends IManual | NewManual | PartialUpdateManual>(manual: T): RestOf<T> {
    return {
      ...manual,
      publicationDate: manual.publicationDate?.format(DATE_FORMAT) ?? null,
      createdDate: manual.createdDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restManual: RestManual): IManual {
    return {
      ...restManual,
      publicationDate: restManual.publicationDate ? dayjs(restManual.publicationDate) : undefined,
      createdDate: restManual.createdDate ? dayjs(restManual.createdDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestManual>): HttpResponse<IManual> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestManual[]>): HttpResponse<IManual[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
