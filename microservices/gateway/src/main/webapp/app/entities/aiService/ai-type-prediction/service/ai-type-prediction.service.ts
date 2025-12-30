import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IAITypePrediction, NewAITypePrediction } from '../ai-type-prediction.model';

export type PartialUpdateAITypePrediction = Partial<IAITypePrediction> & Pick<IAITypePrediction, 'id'>;

type RestOf<T extends IAITypePrediction | NewAITypePrediction> = Omit<T, 'acceptedDate' | 'predictionDate'> & {
  acceptedDate?: string | null;
  predictionDate?: string | null;
};

export type RestAITypePrediction = RestOf<IAITypePrediction>;

export type NewRestAITypePrediction = RestOf<NewAITypePrediction>;

export type PartialUpdateRestAITypePrediction = RestOf<PartialUpdateAITypePrediction>;

export type EntityResponseType = HttpResponse<IAITypePrediction>;
export type EntityArrayResponseType = HttpResponse<IAITypePrediction[]>;

@Injectable({ providedIn: 'root' })
export class AITypePredictionService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ai-type-predictions', 'aiservice');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/ai-type-predictions/_search', 'aiservice');

  create(aITypePrediction: NewAITypePrediction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(aITypePrediction);
    return this.http
      .post<RestAITypePrediction>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(aITypePrediction: IAITypePrediction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(aITypePrediction);
    return this.http
      .put<RestAITypePrediction>(`${this.resourceUrl}/${this.getAITypePredictionIdentifier(aITypePrediction)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(aITypePrediction: PartialUpdateAITypePrediction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(aITypePrediction);
    return this.http
      .patch<RestAITypePrediction>(`${this.resourceUrl}/${this.getAITypePredictionIdentifier(aITypePrediction)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestAITypePrediction>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestAITypePrediction[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestAITypePrediction[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IAITypePrediction[]>()], asapScheduler)),
    );
  }

  getAITypePredictionIdentifier(aITypePrediction: Pick<IAITypePrediction, 'id'>): number {
    return aITypePrediction.id;
  }

  compareAITypePrediction(o1: Pick<IAITypePrediction, 'id'> | null, o2: Pick<IAITypePrediction, 'id'> | null): boolean {
    return o1 && o2 ? this.getAITypePredictionIdentifier(o1) === this.getAITypePredictionIdentifier(o2) : o1 === o2;
  }

  addAITypePredictionToCollectionIfMissing<Type extends Pick<IAITypePrediction, 'id'>>(
    aITypePredictionCollection: Type[],
    ...aITypePredictionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const aITypePredictions: Type[] = aITypePredictionsToCheck.filter(isPresent);
    if (aITypePredictions.length > 0) {
      const aITypePredictionCollectionIdentifiers = aITypePredictionCollection.map(aITypePredictionItem =>
        this.getAITypePredictionIdentifier(aITypePredictionItem),
      );
      const aITypePredictionsToAdd = aITypePredictions.filter(aITypePredictionItem => {
        const aITypePredictionIdentifier = this.getAITypePredictionIdentifier(aITypePredictionItem);
        if (aITypePredictionCollectionIdentifiers.includes(aITypePredictionIdentifier)) {
          return false;
        }
        aITypePredictionCollectionIdentifiers.push(aITypePredictionIdentifier);
        return true;
      });
      return [...aITypePredictionsToAdd, ...aITypePredictionCollection];
    }
    return aITypePredictionCollection;
  }

  protected convertDateFromClient<T extends IAITypePrediction | NewAITypePrediction | PartialUpdateAITypePrediction>(
    aITypePrediction: T,
  ): RestOf<T> {
    return {
      ...aITypePrediction,
      acceptedDate: aITypePrediction.acceptedDate?.toJSON() ?? null,
      predictionDate: aITypePrediction.predictionDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restAITypePrediction: RestAITypePrediction): IAITypePrediction {
    return {
      ...restAITypePrediction,
      acceptedDate: restAITypePrediction.acceptedDate ? dayjs(restAITypePrediction.acceptedDate) : undefined,
      predictionDate: restAITypePrediction.predictionDate ? dayjs(restAITypePrediction.predictionDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestAITypePrediction>): HttpResponse<IAITypePrediction> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestAITypePrediction[]>): HttpResponse<IAITypePrediction[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
