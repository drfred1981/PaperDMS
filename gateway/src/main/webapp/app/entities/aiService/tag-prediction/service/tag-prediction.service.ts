import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { ITagPrediction, NewTagPrediction } from '../tag-prediction.model';

export type PartialUpdateTagPrediction = Partial<ITagPrediction> & Pick<ITagPrediction, 'id'>;

type RestOf<T extends ITagPrediction | NewTagPrediction> = Omit<T, 'acceptedDate' | 'predictionDate'> & {
  acceptedDate?: string | null;
  predictionDate?: string | null;
};

export type RestTagPrediction = RestOf<ITagPrediction>;

export type NewRestTagPrediction = RestOf<NewTagPrediction>;

export type PartialUpdateRestTagPrediction = RestOf<PartialUpdateTagPrediction>;

export type EntityResponseType = HttpResponse<ITagPrediction>;
export type EntityArrayResponseType = HttpResponse<ITagPrediction[]>;

@Injectable({ providedIn: 'root' })
export class TagPredictionService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tag-predictions', 'aiservice');

  create(tagPrediction: NewTagPrediction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tagPrediction);
    return this.http
      .post<RestTagPrediction>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(tagPrediction: ITagPrediction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tagPrediction);
    return this.http
      .put<RestTagPrediction>(`${this.resourceUrl}/${encodeURIComponent(this.getTagPredictionIdentifier(tagPrediction))}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(tagPrediction: PartialUpdateTagPrediction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tagPrediction);
    return this.http
      .patch<RestTagPrediction>(`${this.resourceUrl}/${encodeURIComponent(this.getTagPredictionIdentifier(tagPrediction))}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestTagPrediction>(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestTagPrediction[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' });
  }

  getTagPredictionIdentifier(tagPrediction: Pick<ITagPrediction, 'id'>): number {
    return tagPrediction.id;
  }

  compareTagPrediction(o1: Pick<ITagPrediction, 'id'> | null, o2: Pick<ITagPrediction, 'id'> | null): boolean {
    return o1 && o2 ? this.getTagPredictionIdentifier(o1) === this.getTagPredictionIdentifier(o2) : o1 === o2;
  }

  addTagPredictionToCollectionIfMissing<Type extends Pick<ITagPrediction, 'id'>>(
    tagPredictionCollection: Type[],
    ...tagPredictionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const tagPredictions: Type[] = tagPredictionsToCheck.filter(isPresent);
    if (tagPredictions.length > 0) {
      const tagPredictionCollectionIdentifiers = tagPredictionCollection.map(tagPredictionItem =>
        this.getTagPredictionIdentifier(tagPredictionItem),
      );
      const tagPredictionsToAdd = tagPredictions.filter(tagPredictionItem => {
        const tagPredictionIdentifier = this.getTagPredictionIdentifier(tagPredictionItem);
        if (tagPredictionCollectionIdentifiers.includes(tagPredictionIdentifier)) {
          return false;
        }
        tagPredictionCollectionIdentifiers.push(tagPredictionIdentifier);
        return true;
      });
      return [...tagPredictionsToAdd, ...tagPredictionCollection];
    }
    return tagPredictionCollection;
  }

  protected convertDateFromClient<T extends ITagPrediction | NewTagPrediction | PartialUpdateTagPrediction>(tagPrediction: T): RestOf<T> {
    return {
      ...tagPrediction,
      acceptedDate: tagPrediction.acceptedDate?.toJSON() ?? null,
      predictionDate: tagPrediction.predictionDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restTagPrediction: RestTagPrediction): ITagPrediction {
    return {
      ...restTagPrediction,
      acceptedDate: restTagPrediction.acceptedDate ? dayjs(restTagPrediction.acceptedDate) : undefined,
      predictionDate: restTagPrediction.predictionDate ? dayjs(restTagPrediction.predictionDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestTagPrediction>): HttpResponse<ITagPrediction> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestTagPrediction[]>): HttpResponse<ITagPrediction[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
