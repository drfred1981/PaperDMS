import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IAILanguageDetection, NewAILanguageDetection } from '../ai-language-detection.model';

export type PartialUpdateAILanguageDetection = Partial<IAILanguageDetection> & Pick<IAILanguageDetection, 'id'>;

type RestOf<T extends IAILanguageDetection | NewAILanguageDetection> = Omit<T, 'detectedDate'> & {
  detectedDate?: string | null;
};

export type RestAILanguageDetection = RestOf<IAILanguageDetection>;

export type NewRestAILanguageDetection = RestOf<NewAILanguageDetection>;

export type PartialUpdateRestAILanguageDetection = RestOf<PartialUpdateAILanguageDetection>;

export type EntityResponseType = HttpResponse<IAILanguageDetection>;
export type EntityArrayResponseType = HttpResponse<IAILanguageDetection[]>;

@Injectable({ providedIn: 'root' })
export class AILanguageDetectionService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ai-language-detections', 'aiservice');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/ai-language-detections/_search', 'aiservice');

  create(aILanguageDetection: NewAILanguageDetection): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(aILanguageDetection);
    return this.http
      .post<RestAILanguageDetection>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(aILanguageDetection: IAILanguageDetection): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(aILanguageDetection);
    return this.http
      .put<RestAILanguageDetection>(`${this.resourceUrl}/${this.getAILanguageDetectionIdentifier(aILanguageDetection)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(aILanguageDetection: PartialUpdateAILanguageDetection): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(aILanguageDetection);
    return this.http
      .patch<RestAILanguageDetection>(`${this.resourceUrl}/${this.getAILanguageDetectionIdentifier(aILanguageDetection)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestAILanguageDetection>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestAILanguageDetection[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestAILanguageDetection[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IAILanguageDetection[]>()], asapScheduler)),
    );
  }

  getAILanguageDetectionIdentifier(aILanguageDetection: Pick<IAILanguageDetection, 'id'>): number {
    return aILanguageDetection.id;
  }

  compareAILanguageDetection(o1: Pick<IAILanguageDetection, 'id'> | null, o2: Pick<IAILanguageDetection, 'id'> | null): boolean {
    return o1 && o2 ? this.getAILanguageDetectionIdentifier(o1) === this.getAILanguageDetectionIdentifier(o2) : o1 === o2;
  }

  addAILanguageDetectionToCollectionIfMissing<Type extends Pick<IAILanguageDetection, 'id'>>(
    aILanguageDetectionCollection: Type[],
    ...aILanguageDetectionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const aILanguageDetections: Type[] = aILanguageDetectionsToCheck.filter(isPresent);
    if (aILanguageDetections.length > 0) {
      const aILanguageDetectionCollectionIdentifiers = aILanguageDetectionCollection.map(aILanguageDetectionItem =>
        this.getAILanguageDetectionIdentifier(aILanguageDetectionItem),
      );
      const aILanguageDetectionsToAdd = aILanguageDetections.filter(aILanguageDetectionItem => {
        const aILanguageDetectionIdentifier = this.getAILanguageDetectionIdentifier(aILanguageDetectionItem);
        if (aILanguageDetectionCollectionIdentifiers.includes(aILanguageDetectionIdentifier)) {
          return false;
        }
        aILanguageDetectionCollectionIdentifiers.push(aILanguageDetectionIdentifier);
        return true;
      });
      return [...aILanguageDetectionsToAdd, ...aILanguageDetectionCollection];
    }
    return aILanguageDetectionCollection;
  }

  protected convertDateFromClient<T extends IAILanguageDetection | NewAILanguageDetection | PartialUpdateAILanguageDetection>(
    aILanguageDetection: T,
  ): RestOf<T> {
    return {
      ...aILanguageDetection,
      detectedDate: aILanguageDetection.detectedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restAILanguageDetection: RestAILanguageDetection): IAILanguageDetection {
    return {
      ...restAILanguageDetection,
      detectedDate: restAILanguageDetection.detectedDate ? dayjs(restAILanguageDetection.detectedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestAILanguageDetection>): HttpResponse<IAILanguageDetection> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestAILanguageDetection[]>): HttpResponse<IAILanguageDetection[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
