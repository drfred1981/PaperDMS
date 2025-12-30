import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IImagePdfConversionRequest, NewImagePdfConversionRequest } from '../image-pdf-conversion-request.model';

export type PartialUpdateImagePdfConversionRequest = Partial<IImagePdfConversionRequest> & Pick<IImagePdfConversionRequest, 'id'>;

type RestOf<T extends IImagePdfConversionRequest | NewImagePdfConversionRequest> = Omit<T, 'requestedAt' | 'startedAt' | 'completedAt'> & {
  requestedAt?: string | null;
  startedAt?: string | null;
  completedAt?: string | null;
};

export type RestImagePdfConversionRequest = RestOf<IImagePdfConversionRequest>;

export type NewRestImagePdfConversionRequest = RestOf<NewImagePdfConversionRequest>;

export type PartialUpdateRestImagePdfConversionRequest = RestOf<PartialUpdateImagePdfConversionRequest>;

export type EntityResponseType = HttpResponse<IImagePdfConversionRequest>;
export type EntityArrayResponseType = HttpResponse<IImagePdfConversionRequest[]>;

@Injectable({ providedIn: 'root' })
export class ImagePdfConversionRequestService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/image-pdf-conversion-requests', 'pdftoimageservice');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor(
    'api/image-pdf-conversion-requests/_search',
    'pdftoimageservice',
  );

  create(imagePdfConversionRequest: NewImagePdfConversionRequest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(imagePdfConversionRequest);
    return this.http
      .post<RestImagePdfConversionRequest>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(imagePdfConversionRequest: IImagePdfConversionRequest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(imagePdfConversionRequest);
    return this.http
      .put<RestImagePdfConversionRequest>(
        `${this.resourceUrl}/${this.getImagePdfConversionRequestIdentifier(imagePdfConversionRequest)}`,
        copy,
        { observe: 'response' },
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(imagePdfConversionRequest: PartialUpdateImagePdfConversionRequest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(imagePdfConversionRequest);
    return this.http
      .patch<RestImagePdfConversionRequest>(
        `${this.resourceUrl}/${this.getImagePdfConversionRequestIdentifier(imagePdfConversionRequest)}`,
        copy,
        { observe: 'response' },
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestImagePdfConversionRequest>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestImagePdfConversionRequest[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestImagePdfConversionRequest[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IImagePdfConversionRequest[]>()], asapScheduler)),
    );
  }

  getImagePdfConversionRequestIdentifier(imagePdfConversionRequest: Pick<IImagePdfConversionRequest, 'id'>): number {
    return imagePdfConversionRequest.id;
  }

  compareImagePdfConversionRequest(
    o1: Pick<IImagePdfConversionRequest, 'id'> | null,
    o2: Pick<IImagePdfConversionRequest, 'id'> | null,
  ): boolean {
    return o1 && o2 ? this.getImagePdfConversionRequestIdentifier(o1) === this.getImagePdfConversionRequestIdentifier(o2) : o1 === o2;
  }

  addImagePdfConversionRequestToCollectionIfMissing<Type extends Pick<IImagePdfConversionRequest, 'id'>>(
    imagePdfConversionRequestCollection: Type[],
    ...imagePdfConversionRequestsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const imagePdfConversionRequests: Type[] = imagePdfConversionRequestsToCheck.filter(isPresent);
    if (imagePdfConversionRequests.length > 0) {
      const imagePdfConversionRequestCollectionIdentifiers = imagePdfConversionRequestCollection.map(imagePdfConversionRequestItem =>
        this.getImagePdfConversionRequestIdentifier(imagePdfConversionRequestItem),
      );
      const imagePdfConversionRequestsToAdd = imagePdfConversionRequests.filter(imagePdfConversionRequestItem => {
        const imagePdfConversionRequestIdentifier = this.getImagePdfConversionRequestIdentifier(imagePdfConversionRequestItem);
        if (imagePdfConversionRequestCollectionIdentifiers.includes(imagePdfConversionRequestIdentifier)) {
          return false;
        }
        imagePdfConversionRequestCollectionIdentifiers.push(imagePdfConversionRequestIdentifier);
        return true;
      });
      return [...imagePdfConversionRequestsToAdd, ...imagePdfConversionRequestCollection];
    }
    return imagePdfConversionRequestCollection;
  }

  protected convertDateFromClient<
    T extends IImagePdfConversionRequest | NewImagePdfConversionRequest | PartialUpdateImagePdfConversionRequest,
  >(imagePdfConversionRequest: T): RestOf<T> {
    return {
      ...imagePdfConversionRequest,
      requestedAt: imagePdfConversionRequest.requestedAt?.toJSON() ?? null,
      startedAt: imagePdfConversionRequest.startedAt?.toJSON() ?? null,
      completedAt: imagePdfConversionRequest.completedAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restImagePdfConversionRequest: RestImagePdfConversionRequest): IImagePdfConversionRequest {
    return {
      ...restImagePdfConversionRequest,
      requestedAt: restImagePdfConversionRequest.requestedAt ? dayjs(restImagePdfConversionRequest.requestedAt) : undefined,
      startedAt: restImagePdfConversionRequest.startedAt ? dayjs(restImagePdfConversionRequest.startedAt) : undefined,
      completedAt: restImagePdfConversionRequest.completedAt ? dayjs(restImagePdfConversionRequest.completedAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestImagePdfConversionRequest>): HttpResponse<IImagePdfConversionRequest> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestImagePdfConversionRequest[]>): HttpResponse<IImagePdfConversionRequest[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
