import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ITransformWatermarkJob, NewTransformWatermarkJob } from '../transform-watermark-job.model';

export type PartialUpdateTransformWatermarkJob = Partial<ITransformWatermarkJob> & Pick<ITransformWatermarkJob, 'id'>;

type RestOf<T extends ITransformWatermarkJob | NewTransformWatermarkJob> = Omit<T, 'startDate' | 'endDate' | 'createdDate'> & {
  startDate?: string | null;
  endDate?: string | null;
  createdDate?: string | null;
};

export type RestTransformWatermarkJob = RestOf<ITransformWatermarkJob>;

export type NewRestTransformWatermarkJob = RestOf<NewTransformWatermarkJob>;

export type PartialUpdateRestTransformWatermarkJob = RestOf<PartialUpdateTransformWatermarkJob>;

export type EntityResponseType = HttpResponse<ITransformWatermarkJob>;
export type EntityArrayResponseType = HttpResponse<ITransformWatermarkJob[]>;

@Injectable({ providedIn: 'root' })
export class TransformWatermarkJobService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/transform-watermark-jobs', 'transformservice');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/transform-watermark-jobs/_search', 'transformservice');

  create(transformWatermarkJob: NewTransformWatermarkJob): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(transformWatermarkJob);
    return this.http
      .post<RestTransformWatermarkJob>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(transformWatermarkJob: ITransformWatermarkJob): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(transformWatermarkJob);
    return this.http
      .put<RestTransformWatermarkJob>(`${this.resourceUrl}/${this.getTransformWatermarkJobIdentifier(transformWatermarkJob)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(transformWatermarkJob: PartialUpdateTransformWatermarkJob): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(transformWatermarkJob);
    return this.http
      .patch<RestTransformWatermarkJob>(`${this.resourceUrl}/${this.getTransformWatermarkJobIdentifier(transformWatermarkJob)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestTransformWatermarkJob>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestTransformWatermarkJob[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestTransformWatermarkJob[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<ITransformWatermarkJob[]>()], asapScheduler)),
    );
  }

  getTransformWatermarkJobIdentifier(transformWatermarkJob: Pick<ITransformWatermarkJob, 'id'>): number {
    return transformWatermarkJob.id;
  }

  compareTransformWatermarkJob(o1: Pick<ITransformWatermarkJob, 'id'> | null, o2: Pick<ITransformWatermarkJob, 'id'> | null): boolean {
    return o1 && o2 ? this.getTransformWatermarkJobIdentifier(o1) === this.getTransformWatermarkJobIdentifier(o2) : o1 === o2;
  }

  addTransformWatermarkJobToCollectionIfMissing<Type extends Pick<ITransformWatermarkJob, 'id'>>(
    transformWatermarkJobCollection: Type[],
    ...transformWatermarkJobsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const transformWatermarkJobs: Type[] = transformWatermarkJobsToCheck.filter(isPresent);
    if (transformWatermarkJobs.length > 0) {
      const transformWatermarkJobCollectionIdentifiers = transformWatermarkJobCollection.map(transformWatermarkJobItem =>
        this.getTransformWatermarkJobIdentifier(transformWatermarkJobItem),
      );
      const transformWatermarkJobsToAdd = transformWatermarkJobs.filter(transformWatermarkJobItem => {
        const transformWatermarkJobIdentifier = this.getTransformWatermarkJobIdentifier(transformWatermarkJobItem);
        if (transformWatermarkJobCollectionIdentifiers.includes(transformWatermarkJobIdentifier)) {
          return false;
        }
        transformWatermarkJobCollectionIdentifiers.push(transformWatermarkJobIdentifier);
        return true;
      });
      return [...transformWatermarkJobsToAdd, ...transformWatermarkJobCollection];
    }
    return transformWatermarkJobCollection;
  }

  protected convertDateFromClient<T extends ITransformWatermarkJob | NewTransformWatermarkJob | PartialUpdateTransformWatermarkJob>(
    transformWatermarkJob: T,
  ): RestOf<T> {
    return {
      ...transformWatermarkJob,
      startDate: transformWatermarkJob.startDate?.toJSON() ?? null,
      endDate: transformWatermarkJob.endDate?.toJSON() ?? null,
      createdDate: transformWatermarkJob.createdDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restTransformWatermarkJob: RestTransformWatermarkJob): ITransformWatermarkJob {
    return {
      ...restTransformWatermarkJob,
      startDate: restTransformWatermarkJob.startDate ? dayjs(restTransformWatermarkJob.startDate) : undefined,
      endDate: restTransformWatermarkJob.endDate ? dayjs(restTransformWatermarkJob.endDate) : undefined,
      createdDate: restTransformWatermarkJob.createdDate ? dayjs(restTransformWatermarkJob.createdDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestTransformWatermarkJob>): HttpResponse<ITransformWatermarkJob> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestTransformWatermarkJob[]>): HttpResponse<ITransformWatermarkJob[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
