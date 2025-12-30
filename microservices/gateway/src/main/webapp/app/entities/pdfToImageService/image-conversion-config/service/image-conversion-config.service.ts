import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IImageConversionConfig, NewImageConversionConfig } from '../image-conversion-config.model';

export type PartialUpdateImageConversionConfig = Partial<IImageConversionConfig> & Pick<IImageConversionConfig, 'id'>;

type RestOf<T extends IImageConversionConfig | NewImageConversionConfig> = Omit<T, 'createdAt' | 'updatedAt'> & {
  createdAt?: string | null;
  updatedAt?: string | null;
};

export type RestImageConversionConfig = RestOf<IImageConversionConfig>;

export type NewRestImageConversionConfig = RestOf<NewImageConversionConfig>;

export type PartialUpdateRestImageConversionConfig = RestOf<PartialUpdateImageConversionConfig>;

export type EntityResponseType = HttpResponse<IImageConversionConfig>;
export type EntityArrayResponseType = HttpResponse<IImageConversionConfig[]>;

@Injectable({ providedIn: 'root' })
export class ImageConversionConfigService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/image-conversion-configs', 'pdftoimageservice');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/image-conversion-configs/_search', 'pdftoimageservice');

  create(imageConversionConfig: NewImageConversionConfig): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(imageConversionConfig);
    return this.http
      .post<RestImageConversionConfig>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(imageConversionConfig: IImageConversionConfig): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(imageConversionConfig);
    return this.http
      .put<RestImageConversionConfig>(`${this.resourceUrl}/${this.getImageConversionConfigIdentifier(imageConversionConfig)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(imageConversionConfig: PartialUpdateImageConversionConfig): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(imageConversionConfig);
    return this.http
      .patch<RestImageConversionConfig>(`${this.resourceUrl}/${this.getImageConversionConfigIdentifier(imageConversionConfig)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestImageConversionConfig>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestImageConversionConfig[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestImageConversionConfig[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IImageConversionConfig[]>()], asapScheduler)),
    );
  }

  getImageConversionConfigIdentifier(imageConversionConfig: Pick<IImageConversionConfig, 'id'>): number {
    return imageConversionConfig.id;
  }

  compareImageConversionConfig(o1: Pick<IImageConversionConfig, 'id'> | null, o2: Pick<IImageConversionConfig, 'id'> | null): boolean {
    return o1 && o2 ? this.getImageConversionConfigIdentifier(o1) === this.getImageConversionConfigIdentifier(o2) : o1 === o2;
  }

  addImageConversionConfigToCollectionIfMissing<Type extends Pick<IImageConversionConfig, 'id'>>(
    imageConversionConfigCollection: Type[],
    ...imageConversionConfigsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const imageConversionConfigs: Type[] = imageConversionConfigsToCheck.filter(isPresent);
    if (imageConversionConfigs.length > 0) {
      const imageConversionConfigCollectionIdentifiers = imageConversionConfigCollection.map(imageConversionConfigItem =>
        this.getImageConversionConfigIdentifier(imageConversionConfigItem),
      );
      const imageConversionConfigsToAdd = imageConversionConfigs.filter(imageConversionConfigItem => {
        const imageConversionConfigIdentifier = this.getImageConversionConfigIdentifier(imageConversionConfigItem);
        if (imageConversionConfigCollectionIdentifiers.includes(imageConversionConfigIdentifier)) {
          return false;
        }
        imageConversionConfigCollectionIdentifiers.push(imageConversionConfigIdentifier);
        return true;
      });
      return [...imageConversionConfigsToAdd, ...imageConversionConfigCollection];
    }
    return imageConversionConfigCollection;
  }

  protected convertDateFromClient<T extends IImageConversionConfig | NewImageConversionConfig | PartialUpdateImageConversionConfig>(
    imageConversionConfig: T,
  ): RestOf<T> {
    return {
      ...imageConversionConfig,
      createdAt: imageConversionConfig.createdAt?.toJSON() ?? null,
      updatedAt: imageConversionConfig.updatedAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restImageConversionConfig: RestImageConversionConfig): IImageConversionConfig {
    return {
      ...restImageConversionConfig,
      createdAt: restImageConversionConfig.createdAt ? dayjs(restImageConversionConfig.createdAt) : undefined,
      updatedAt: restImageConversionConfig.updatedAt ? dayjs(restImageConversionConfig.updatedAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestImageConversionConfig>): HttpResponse<IImageConversionConfig> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestImageConversionConfig[]>): HttpResponse<IImageConversionConfig[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
