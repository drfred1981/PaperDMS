import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IAICorrespondentPrediction, NewAICorrespondentPrediction } from '../ai-correspondent-prediction.model';

export type PartialUpdateAICorrespondentPrediction = Partial<IAICorrespondentPrediction> & Pick<IAICorrespondentPrediction, 'id'>;

type RestOf<T extends IAICorrespondentPrediction | NewAICorrespondentPrediction> = Omit<T, 'acceptedDate' | 'predictionDate'> & {
  acceptedDate?: string | null;
  predictionDate?: string | null;
};

export type RestAICorrespondentPrediction = RestOf<IAICorrespondentPrediction>;

export type NewRestAICorrespondentPrediction = RestOf<NewAICorrespondentPrediction>;

export type PartialUpdateRestAICorrespondentPrediction = RestOf<PartialUpdateAICorrespondentPrediction>;

export type EntityResponseType = HttpResponse<IAICorrespondentPrediction>;
export type EntityArrayResponseType = HttpResponse<IAICorrespondentPrediction[]>;

@Injectable({ providedIn: 'root' })
export class AICorrespondentPredictionService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ai-correspondent-predictions', 'aiservice');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/ai-correspondent-predictions/_search', 'aiservice');

  create(aICorrespondentPrediction: NewAICorrespondentPrediction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(aICorrespondentPrediction);
    return this.http
      .post<RestAICorrespondentPrediction>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(aICorrespondentPrediction: IAICorrespondentPrediction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(aICorrespondentPrediction);
    return this.http
      .put<RestAICorrespondentPrediction>(
        `${this.resourceUrl}/${this.getAICorrespondentPredictionIdentifier(aICorrespondentPrediction)}`,
        copy,
        { observe: 'response' },
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(aICorrespondentPrediction: PartialUpdateAICorrespondentPrediction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(aICorrespondentPrediction);
    return this.http
      .patch<RestAICorrespondentPrediction>(
        `${this.resourceUrl}/${this.getAICorrespondentPredictionIdentifier(aICorrespondentPrediction)}`,
        copy,
        { observe: 'response' },
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestAICorrespondentPrediction>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestAICorrespondentPrediction[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestAICorrespondentPrediction[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IAICorrespondentPrediction[]>()], asapScheduler)),
    );
  }

  getAICorrespondentPredictionIdentifier(aICorrespondentPrediction: Pick<IAICorrespondentPrediction, 'id'>): number {
    return aICorrespondentPrediction.id;
  }

  compareAICorrespondentPrediction(
    o1: Pick<IAICorrespondentPrediction, 'id'> | null,
    o2: Pick<IAICorrespondentPrediction, 'id'> | null,
  ): boolean {
    return o1 && o2 ? this.getAICorrespondentPredictionIdentifier(o1) === this.getAICorrespondentPredictionIdentifier(o2) : o1 === o2;
  }

  addAICorrespondentPredictionToCollectionIfMissing<Type extends Pick<IAICorrespondentPrediction, 'id'>>(
    aICorrespondentPredictionCollection: Type[],
    ...aICorrespondentPredictionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const aICorrespondentPredictions: Type[] = aICorrespondentPredictionsToCheck.filter(isPresent);
    if (aICorrespondentPredictions.length > 0) {
      const aICorrespondentPredictionCollectionIdentifiers = aICorrespondentPredictionCollection.map(aICorrespondentPredictionItem =>
        this.getAICorrespondentPredictionIdentifier(aICorrespondentPredictionItem),
      );
      const aICorrespondentPredictionsToAdd = aICorrespondentPredictions.filter(aICorrespondentPredictionItem => {
        const aICorrespondentPredictionIdentifier = this.getAICorrespondentPredictionIdentifier(aICorrespondentPredictionItem);
        if (aICorrespondentPredictionCollectionIdentifiers.includes(aICorrespondentPredictionIdentifier)) {
          return false;
        }
        aICorrespondentPredictionCollectionIdentifiers.push(aICorrespondentPredictionIdentifier);
        return true;
      });
      return [...aICorrespondentPredictionsToAdd, ...aICorrespondentPredictionCollection];
    }
    return aICorrespondentPredictionCollection;
  }

  protected convertDateFromClient<
    T extends IAICorrespondentPrediction | NewAICorrespondentPrediction | PartialUpdateAICorrespondentPrediction,
  >(aICorrespondentPrediction: T): RestOf<T> {
    return {
      ...aICorrespondentPrediction,
      acceptedDate: aICorrespondentPrediction.acceptedDate?.toJSON() ?? null,
      predictionDate: aICorrespondentPrediction.predictionDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restAICorrespondentPrediction: RestAICorrespondentPrediction): IAICorrespondentPrediction {
    return {
      ...restAICorrespondentPrediction,
      acceptedDate: restAICorrespondentPrediction.acceptedDate ? dayjs(restAICorrespondentPrediction.acceptedDate) : undefined,
      predictionDate: restAICorrespondentPrediction.predictionDate ? dayjs(restAICorrespondentPrediction.predictionDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestAICorrespondentPrediction>): HttpResponse<IAICorrespondentPrediction> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestAICorrespondentPrediction[]>): HttpResponse<IAICorrespondentPrediction[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
