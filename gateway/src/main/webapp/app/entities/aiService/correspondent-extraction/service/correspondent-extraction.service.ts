import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { ICorrespondentExtraction, NewCorrespondentExtraction } from '../correspondent-extraction.model';

export type PartialUpdateCorrespondentExtraction = Partial<ICorrespondentExtraction> & Pick<ICorrespondentExtraction, 'id'>;

type RestOf<T extends ICorrespondentExtraction | NewCorrespondentExtraction> = Omit<T, 'startDate' | 'endDate' | 'createdDate'> & {
  startDate?: string | null;
  endDate?: string | null;
  createdDate?: string | null;
};

export type RestCorrespondentExtraction = RestOf<ICorrespondentExtraction>;

export type NewRestCorrespondentExtraction = RestOf<NewCorrespondentExtraction>;

export type PartialUpdateRestCorrespondentExtraction = RestOf<PartialUpdateCorrespondentExtraction>;

export type EntityResponseType = HttpResponse<ICorrespondentExtraction>;
export type EntityArrayResponseType = HttpResponse<ICorrespondentExtraction[]>;

@Injectable({ providedIn: 'root' })
export class CorrespondentExtractionService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/correspondent-extractions', 'aiservice');

  create(correspondentExtraction: NewCorrespondentExtraction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(correspondentExtraction);
    return this.http
      .post<RestCorrespondentExtraction>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(correspondentExtraction: ICorrespondentExtraction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(correspondentExtraction);
    return this.http
      .put<RestCorrespondentExtraction>(
        `${this.resourceUrl}/${encodeURIComponent(this.getCorrespondentExtractionIdentifier(correspondentExtraction))}`,
        copy,
        { observe: 'response' },
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(correspondentExtraction: PartialUpdateCorrespondentExtraction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(correspondentExtraction);
    return this.http
      .patch<RestCorrespondentExtraction>(
        `${this.resourceUrl}/${encodeURIComponent(this.getCorrespondentExtractionIdentifier(correspondentExtraction))}`,
        copy,
        { observe: 'response' },
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestCorrespondentExtraction>(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestCorrespondentExtraction[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' });
  }

  getCorrespondentExtractionIdentifier(correspondentExtraction: Pick<ICorrespondentExtraction, 'id'>): number {
    return correspondentExtraction.id;
  }

  compareCorrespondentExtraction(
    o1: Pick<ICorrespondentExtraction, 'id'> | null,
    o2: Pick<ICorrespondentExtraction, 'id'> | null,
  ): boolean {
    return o1 && o2 ? this.getCorrespondentExtractionIdentifier(o1) === this.getCorrespondentExtractionIdentifier(o2) : o1 === o2;
  }

  addCorrespondentExtractionToCollectionIfMissing<Type extends Pick<ICorrespondentExtraction, 'id'>>(
    correspondentExtractionCollection: Type[],
    ...correspondentExtractionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const correspondentExtractions: Type[] = correspondentExtractionsToCheck.filter(isPresent);
    if (correspondentExtractions.length > 0) {
      const correspondentExtractionCollectionIdentifiers = correspondentExtractionCollection.map(correspondentExtractionItem =>
        this.getCorrespondentExtractionIdentifier(correspondentExtractionItem),
      );
      const correspondentExtractionsToAdd = correspondentExtractions.filter(correspondentExtractionItem => {
        const correspondentExtractionIdentifier = this.getCorrespondentExtractionIdentifier(correspondentExtractionItem);
        if (correspondentExtractionCollectionIdentifiers.includes(correspondentExtractionIdentifier)) {
          return false;
        }
        correspondentExtractionCollectionIdentifiers.push(correspondentExtractionIdentifier);
        return true;
      });
      return [...correspondentExtractionsToAdd, ...correspondentExtractionCollection];
    }
    return correspondentExtractionCollection;
  }

  protected convertDateFromClient<T extends ICorrespondentExtraction | NewCorrespondentExtraction | PartialUpdateCorrespondentExtraction>(
    correspondentExtraction: T,
  ): RestOf<T> {
    return {
      ...correspondentExtraction,
      startDate: correspondentExtraction.startDate?.toJSON() ?? null,
      endDate: correspondentExtraction.endDate?.toJSON() ?? null,
      createdDate: correspondentExtraction.createdDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restCorrespondentExtraction: RestCorrespondentExtraction): ICorrespondentExtraction {
    return {
      ...restCorrespondentExtraction,
      startDate: restCorrespondentExtraction.startDate ? dayjs(restCorrespondentExtraction.startDate) : undefined,
      endDate: restCorrespondentExtraction.endDate ? dayjs(restCorrespondentExtraction.endDate) : undefined,
      createdDate: restCorrespondentExtraction.createdDate ? dayjs(restCorrespondentExtraction.createdDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestCorrespondentExtraction>): HttpResponse<ICorrespondentExtraction> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestCorrespondentExtraction[]>): HttpResponse<ICorrespondentExtraction[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
