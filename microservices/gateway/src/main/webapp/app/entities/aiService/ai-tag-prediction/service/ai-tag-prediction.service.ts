import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IAITagPrediction, NewAITagPrediction } from '../ai-tag-prediction.model';

export type PartialUpdateAITagPrediction = Partial<IAITagPrediction> & Pick<IAITagPrediction, 'id'>;

type RestOf<T extends IAITagPrediction | NewAITagPrediction> = Omit<T, 'acceptedDate' | 'predictionDate'> & {
  acceptedDate?: string | null;
  predictionDate?: string | null;
};

export type RestAITagPrediction = RestOf<IAITagPrediction>;

export type NewRestAITagPrediction = RestOf<NewAITagPrediction>;

export type PartialUpdateRestAITagPrediction = RestOf<PartialUpdateAITagPrediction>;

export type EntityResponseType = HttpResponse<IAITagPrediction>;
export type EntityArrayResponseType = HttpResponse<IAITagPrediction[]>;

@Injectable({ providedIn: 'root' })
export class AITagPredictionService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ai-tag-predictions', 'aiservice');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/ai-tag-predictions/_search', 'aiservice');

  create(aITagPrediction: NewAITagPrediction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(aITagPrediction);
    return this.http
      .post<RestAITagPrediction>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(aITagPrediction: IAITagPrediction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(aITagPrediction);
    return this.http
      .put<RestAITagPrediction>(`${this.resourceUrl}/${this.getAITagPredictionIdentifier(aITagPrediction)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(aITagPrediction: PartialUpdateAITagPrediction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(aITagPrediction);
    return this.http
      .patch<RestAITagPrediction>(`${this.resourceUrl}/${this.getAITagPredictionIdentifier(aITagPrediction)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestAITagPrediction>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestAITagPrediction[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestAITagPrediction[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IAITagPrediction[]>()], asapScheduler)),
    );
  }

  getAITagPredictionIdentifier(aITagPrediction: Pick<IAITagPrediction, 'id'>): number {
    return aITagPrediction.id;
  }

  compareAITagPrediction(o1: Pick<IAITagPrediction, 'id'> | null, o2: Pick<IAITagPrediction, 'id'> | null): boolean {
    return o1 && o2 ? this.getAITagPredictionIdentifier(o1) === this.getAITagPredictionIdentifier(o2) : o1 === o2;
  }

  addAITagPredictionToCollectionIfMissing<Type extends Pick<IAITagPrediction, 'id'>>(
    aITagPredictionCollection: Type[],
    ...aITagPredictionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const aITagPredictions: Type[] = aITagPredictionsToCheck.filter(isPresent);
    if (aITagPredictions.length > 0) {
      const aITagPredictionCollectionIdentifiers = aITagPredictionCollection.map(aITagPredictionItem =>
        this.getAITagPredictionIdentifier(aITagPredictionItem),
      );
      const aITagPredictionsToAdd = aITagPredictions.filter(aITagPredictionItem => {
        const aITagPredictionIdentifier = this.getAITagPredictionIdentifier(aITagPredictionItem);
        if (aITagPredictionCollectionIdentifiers.includes(aITagPredictionIdentifier)) {
          return false;
        }
        aITagPredictionCollectionIdentifiers.push(aITagPredictionIdentifier);
        return true;
      });
      return [...aITagPredictionsToAdd, ...aITagPredictionCollection];
    }
    return aITagPredictionCollection;
  }

  protected convertDateFromClient<T extends IAITagPrediction | NewAITagPrediction | PartialUpdateAITagPrediction>(
    aITagPrediction: T,
  ): RestOf<T> {
    return {
      ...aITagPrediction,
      acceptedDate: aITagPrediction.acceptedDate?.toJSON() ?? null,
      predictionDate: aITagPrediction.predictionDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restAITagPrediction: RestAITagPrediction): IAITagPrediction {
    return {
      ...restAITagPrediction,
      acceptedDate: restAITagPrediction.acceptedDate ? dayjs(restAITagPrediction.acceptedDate) : undefined,
      predictionDate: restAITagPrediction.predictionDate ? dayjs(restAITagPrediction.predictionDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestAITagPrediction>): HttpResponse<IAITagPrediction> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestAITagPrediction[]>): HttpResponse<IAITagPrediction[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
