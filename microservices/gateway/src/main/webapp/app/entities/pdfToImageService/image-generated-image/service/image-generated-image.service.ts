import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IImageGeneratedImage, NewImageGeneratedImage } from '../image-generated-image.model';

export type PartialUpdateImageGeneratedImage = Partial<IImageGeneratedImage> & Pick<IImageGeneratedImage, 'id'>;

type RestOf<T extends IImageGeneratedImage | NewImageGeneratedImage> = Omit<T, 'urlExpiresAt' | 'generatedAt'> & {
  urlExpiresAt?: string | null;
  generatedAt?: string | null;
};

export type RestImageGeneratedImage = RestOf<IImageGeneratedImage>;

export type NewRestImageGeneratedImage = RestOf<NewImageGeneratedImage>;

export type PartialUpdateRestImageGeneratedImage = RestOf<PartialUpdateImageGeneratedImage>;

export type EntityResponseType = HttpResponse<IImageGeneratedImage>;
export type EntityArrayResponseType = HttpResponse<IImageGeneratedImage[]>;

@Injectable({ providedIn: 'root' })
export class ImageGeneratedImageService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/image-generated-images', 'pdftoimageservice');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/image-generated-images/_search', 'pdftoimageservice');

  create(imageGeneratedImage: NewImageGeneratedImage): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(imageGeneratedImage);
    return this.http
      .post<RestImageGeneratedImage>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(imageGeneratedImage: IImageGeneratedImage): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(imageGeneratedImage);
    return this.http
      .put<RestImageGeneratedImage>(`${this.resourceUrl}/${this.getImageGeneratedImageIdentifier(imageGeneratedImage)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(imageGeneratedImage: PartialUpdateImageGeneratedImage): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(imageGeneratedImage);
    return this.http
      .patch<RestImageGeneratedImage>(`${this.resourceUrl}/${this.getImageGeneratedImageIdentifier(imageGeneratedImage)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestImageGeneratedImage>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestImageGeneratedImage[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestImageGeneratedImage[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IImageGeneratedImage[]>()], asapScheduler)),
    );
  }

  getImageGeneratedImageIdentifier(imageGeneratedImage: Pick<IImageGeneratedImage, 'id'>): number {
    return imageGeneratedImage.id;
  }

  compareImageGeneratedImage(o1: Pick<IImageGeneratedImage, 'id'> | null, o2: Pick<IImageGeneratedImage, 'id'> | null): boolean {
    return o1 && o2 ? this.getImageGeneratedImageIdentifier(o1) === this.getImageGeneratedImageIdentifier(o2) : o1 === o2;
  }

  addImageGeneratedImageToCollectionIfMissing<Type extends Pick<IImageGeneratedImage, 'id'>>(
    imageGeneratedImageCollection: Type[],
    ...imageGeneratedImagesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const imageGeneratedImages: Type[] = imageGeneratedImagesToCheck.filter(isPresent);
    if (imageGeneratedImages.length > 0) {
      const imageGeneratedImageCollectionIdentifiers = imageGeneratedImageCollection.map(imageGeneratedImageItem =>
        this.getImageGeneratedImageIdentifier(imageGeneratedImageItem),
      );
      const imageGeneratedImagesToAdd = imageGeneratedImages.filter(imageGeneratedImageItem => {
        const imageGeneratedImageIdentifier = this.getImageGeneratedImageIdentifier(imageGeneratedImageItem);
        if (imageGeneratedImageCollectionIdentifiers.includes(imageGeneratedImageIdentifier)) {
          return false;
        }
        imageGeneratedImageCollectionIdentifiers.push(imageGeneratedImageIdentifier);
        return true;
      });
      return [...imageGeneratedImagesToAdd, ...imageGeneratedImageCollection];
    }
    return imageGeneratedImageCollection;
  }

  protected convertDateFromClient<T extends IImageGeneratedImage | NewImageGeneratedImage | PartialUpdateImageGeneratedImage>(
    imageGeneratedImage: T,
  ): RestOf<T> {
    return {
      ...imageGeneratedImage,
      urlExpiresAt: imageGeneratedImage.urlExpiresAt?.toJSON() ?? null,
      generatedAt: imageGeneratedImage.generatedAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restImageGeneratedImage: RestImageGeneratedImage): IImageGeneratedImage {
    return {
      ...restImageGeneratedImage,
      urlExpiresAt: restImageGeneratedImage.urlExpiresAt ? dayjs(restImageGeneratedImage.urlExpiresAt) : undefined,
      generatedAt: restImageGeneratedImage.generatedAt ? dayjs(restImageGeneratedImage.generatedAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestImageGeneratedImage>): HttpResponse<IImageGeneratedImage> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestImageGeneratedImage[]>): HttpResponse<IImageGeneratedImage[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
