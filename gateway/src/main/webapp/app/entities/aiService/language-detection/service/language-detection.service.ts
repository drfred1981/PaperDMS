import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { ILanguageDetection, NewLanguageDetection } from '../language-detection.model';

export type PartialUpdateLanguageDetection = Partial<ILanguageDetection> & Pick<ILanguageDetection, 'id'>;

type RestOf<T extends ILanguageDetection | NewLanguageDetection> = Omit<T, 'detectedDate'> & {
  detectedDate?: string | null;
};

export type RestLanguageDetection = RestOf<ILanguageDetection>;

export type NewRestLanguageDetection = RestOf<NewLanguageDetection>;

export type PartialUpdateRestLanguageDetection = RestOf<PartialUpdateLanguageDetection>;

export type EntityResponseType = HttpResponse<ILanguageDetection>;
export type EntityArrayResponseType = HttpResponse<ILanguageDetection[]>;

@Injectable({ providedIn: 'root' })
export class LanguageDetectionService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/language-detections', 'aiservice');

  create(languageDetection: NewLanguageDetection): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(languageDetection);
    return this.http
      .post<RestLanguageDetection>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(languageDetection: ILanguageDetection): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(languageDetection);
    return this.http
      .put<RestLanguageDetection>(
        `${this.resourceUrl}/${encodeURIComponent(this.getLanguageDetectionIdentifier(languageDetection))}`,
        copy,
        { observe: 'response' },
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(languageDetection: PartialUpdateLanguageDetection): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(languageDetection);
    return this.http
      .patch<RestLanguageDetection>(
        `${this.resourceUrl}/${encodeURIComponent(this.getLanguageDetectionIdentifier(languageDetection))}`,
        copy,
        { observe: 'response' },
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestLanguageDetection>(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestLanguageDetection[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' });
  }

  getLanguageDetectionIdentifier(languageDetection: Pick<ILanguageDetection, 'id'>): number {
    return languageDetection.id;
  }

  compareLanguageDetection(o1: Pick<ILanguageDetection, 'id'> | null, o2: Pick<ILanguageDetection, 'id'> | null): boolean {
    return o1 && o2 ? this.getLanguageDetectionIdentifier(o1) === this.getLanguageDetectionIdentifier(o2) : o1 === o2;
  }

  addLanguageDetectionToCollectionIfMissing<Type extends Pick<ILanguageDetection, 'id'>>(
    languageDetectionCollection: Type[],
    ...languageDetectionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const languageDetections: Type[] = languageDetectionsToCheck.filter(isPresent);
    if (languageDetections.length > 0) {
      const languageDetectionCollectionIdentifiers = languageDetectionCollection.map(languageDetectionItem =>
        this.getLanguageDetectionIdentifier(languageDetectionItem),
      );
      const languageDetectionsToAdd = languageDetections.filter(languageDetectionItem => {
        const languageDetectionIdentifier = this.getLanguageDetectionIdentifier(languageDetectionItem);
        if (languageDetectionCollectionIdentifiers.includes(languageDetectionIdentifier)) {
          return false;
        }
        languageDetectionCollectionIdentifiers.push(languageDetectionIdentifier);
        return true;
      });
      return [...languageDetectionsToAdd, ...languageDetectionCollection];
    }
    return languageDetectionCollection;
  }

  protected convertDateFromClient<T extends ILanguageDetection | NewLanguageDetection | PartialUpdateLanguageDetection>(
    languageDetection: T,
  ): RestOf<T> {
    return {
      ...languageDetection,
      detectedDate: languageDetection.detectedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restLanguageDetection: RestLanguageDetection): ILanguageDetection {
    return {
      ...restLanguageDetection,
      detectedDate: restLanguageDetection.detectedDate ? dayjs(restLanguageDetection.detectedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestLanguageDetection>): HttpResponse<ILanguageDetection> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestLanguageDetection[]>): HttpResponse<ILanguageDetection[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
