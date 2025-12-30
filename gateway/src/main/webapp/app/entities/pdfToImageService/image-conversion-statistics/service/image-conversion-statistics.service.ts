import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IImageConversionStatistics, NewImageConversionStatistics } from '../image-conversion-statistics.model';

export type PartialUpdateImageConversionStatistics = Partial<IImageConversionStatistics> & Pick<IImageConversionStatistics, 'id'>;

type RestOf<T extends IImageConversionStatistics | NewImageConversionStatistics> = Omit<T, 'statisticsDate' | 'calculatedAt'> & {
  statisticsDate?: string | null;
  calculatedAt?: string | null;
};

export type RestImageConversionStatistics = RestOf<IImageConversionStatistics>;

export type NewRestImageConversionStatistics = RestOf<NewImageConversionStatistics>;

export type PartialUpdateRestImageConversionStatistics = RestOf<PartialUpdateImageConversionStatistics>;

export type EntityResponseType = HttpResponse<IImageConversionStatistics>;
export type EntityArrayResponseType = HttpResponse<IImageConversionStatistics[]>;

@Injectable({ providedIn: 'root' })
export class ImageConversionStatisticsService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/image-conversion-statistics', 'pdftoimageservice');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor(
    'api/image-conversion-statistics/_search',
    'pdftoimageservice',
  );

  create(imageConversionStatistics: NewImageConversionStatistics): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(imageConversionStatistics);
    return this.http
      .post<RestImageConversionStatistics>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(imageConversionStatistics: IImageConversionStatistics): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(imageConversionStatistics);
    return this.http
      .put<RestImageConversionStatistics>(
        `${this.resourceUrl}/${this.getImageConversionStatisticsIdentifier(imageConversionStatistics)}`,
        copy,
        { observe: 'response' },
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(imageConversionStatistics: PartialUpdateImageConversionStatistics): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(imageConversionStatistics);
    return this.http
      .patch<RestImageConversionStatistics>(
        `${this.resourceUrl}/${this.getImageConversionStatisticsIdentifier(imageConversionStatistics)}`,
        copy,
        { observe: 'response' },
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestImageConversionStatistics>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestImageConversionStatistics[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestImageConversionStatistics[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IImageConversionStatistics[]>()], asapScheduler)),
    );
  }

  getImageConversionStatisticsIdentifier(imageConversionStatistics: Pick<IImageConversionStatistics, 'id'>): number {
    return imageConversionStatistics.id;
  }

  compareImageConversionStatistics(
    o1: Pick<IImageConversionStatistics, 'id'> | null,
    o2: Pick<IImageConversionStatistics, 'id'> | null,
  ): boolean {
    return o1 && o2 ? this.getImageConversionStatisticsIdentifier(o1) === this.getImageConversionStatisticsIdentifier(o2) : o1 === o2;
  }

  addImageConversionStatisticsToCollectionIfMissing<Type extends Pick<IImageConversionStatistics, 'id'>>(
    imageConversionStatisticsCollection: Type[],
    ...imageConversionStatisticsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const imageConversionStatistics: Type[] = imageConversionStatisticsToCheck.filter(isPresent);
    if (imageConversionStatistics.length > 0) {
      const imageConversionStatisticsCollectionIdentifiers = imageConversionStatisticsCollection.map(imageConversionStatisticsItem =>
        this.getImageConversionStatisticsIdentifier(imageConversionStatisticsItem),
      );
      const imageConversionStatisticsToAdd = imageConversionStatistics.filter(imageConversionStatisticsItem => {
        const imageConversionStatisticsIdentifier = this.getImageConversionStatisticsIdentifier(imageConversionStatisticsItem);
        if (imageConversionStatisticsCollectionIdentifiers.includes(imageConversionStatisticsIdentifier)) {
          return false;
        }
        imageConversionStatisticsCollectionIdentifiers.push(imageConversionStatisticsIdentifier);
        return true;
      });
      return [...imageConversionStatisticsToAdd, ...imageConversionStatisticsCollection];
    }
    return imageConversionStatisticsCollection;
  }

  protected convertDateFromClient<
    T extends IImageConversionStatistics | NewImageConversionStatistics | PartialUpdateImageConversionStatistics,
  >(imageConversionStatistics: T): RestOf<T> {
    return {
      ...imageConversionStatistics,
      statisticsDate: imageConversionStatistics.statisticsDate?.format(DATE_FORMAT) ?? null,
      calculatedAt: imageConversionStatistics.calculatedAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restImageConversionStatistics: RestImageConversionStatistics): IImageConversionStatistics {
    return {
      ...restImageConversionStatistics,
      statisticsDate: restImageConversionStatistics.statisticsDate ? dayjs(restImageConversionStatistics.statisticsDate) : undefined,
      calculatedAt: restImageConversionStatistics.calculatedAt ? dayjs(restImageConversionStatistics.calculatedAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestImageConversionStatistics>): HttpResponse<IImageConversionStatistics> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestImageConversionStatistics[]>): HttpResponse<IImageConversionStatistics[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
