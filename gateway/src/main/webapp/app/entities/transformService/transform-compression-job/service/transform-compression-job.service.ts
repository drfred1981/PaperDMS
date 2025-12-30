import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ITransformCompressionJob, NewTransformCompressionJob } from '../transform-compression-job.model';

export type PartialUpdateTransformCompressionJob = Partial<ITransformCompressionJob> & Pick<ITransformCompressionJob, 'id'>;

type RestOf<T extends ITransformCompressionJob | NewTransformCompressionJob> = Omit<T, 'startDate' | 'endDate' | 'createdDate'> & {
  startDate?: string | null;
  endDate?: string | null;
  createdDate?: string | null;
};

export type RestTransformCompressionJob = RestOf<ITransformCompressionJob>;

export type NewRestTransformCompressionJob = RestOf<NewTransformCompressionJob>;

export type PartialUpdateRestTransformCompressionJob = RestOf<PartialUpdateTransformCompressionJob>;

export type EntityResponseType = HttpResponse<ITransformCompressionJob>;
export type EntityArrayResponseType = HttpResponse<ITransformCompressionJob[]>;

@Injectable({ providedIn: 'root' })
export class TransformCompressionJobService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/transform-compression-jobs', 'transformservice');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/transform-compression-jobs/_search', 'transformservice');

  create(transformCompressionJob: NewTransformCompressionJob): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(transformCompressionJob);
    return this.http
      .post<RestTransformCompressionJob>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(transformCompressionJob: ITransformCompressionJob): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(transformCompressionJob);
    return this.http
      .put<RestTransformCompressionJob>(`${this.resourceUrl}/${this.getTransformCompressionJobIdentifier(transformCompressionJob)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(transformCompressionJob: PartialUpdateTransformCompressionJob): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(transformCompressionJob);
    return this.http
      .patch<RestTransformCompressionJob>(
        `${this.resourceUrl}/${this.getTransformCompressionJobIdentifier(transformCompressionJob)}`,
        copy,
        { observe: 'response' },
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestTransformCompressionJob>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestTransformCompressionJob[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestTransformCompressionJob[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<ITransformCompressionJob[]>()], asapScheduler)),
    );
  }

  getTransformCompressionJobIdentifier(transformCompressionJob: Pick<ITransformCompressionJob, 'id'>): number {
    return transformCompressionJob.id;
  }

  compareTransformCompressionJob(
    o1: Pick<ITransformCompressionJob, 'id'> | null,
    o2: Pick<ITransformCompressionJob, 'id'> | null,
  ): boolean {
    return o1 && o2 ? this.getTransformCompressionJobIdentifier(o1) === this.getTransformCompressionJobIdentifier(o2) : o1 === o2;
  }

  addTransformCompressionJobToCollectionIfMissing<Type extends Pick<ITransformCompressionJob, 'id'>>(
    transformCompressionJobCollection: Type[],
    ...transformCompressionJobsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const transformCompressionJobs: Type[] = transformCompressionJobsToCheck.filter(isPresent);
    if (transformCompressionJobs.length > 0) {
      const transformCompressionJobCollectionIdentifiers = transformCompressionJobCollection.map(transformCompressionJobItem =>
        this.getTransformCompressionJobIdentifier(transformCompressionJobItem),
      );
      const transformCompressionJobsToAdd = transformCompressionJobs.filter(transformCompressionJobItem => {
        const transformCompressionJobIdentifier = this.getTransformCompressionJobIdentifier(transformCompressionJobItem);
        if (transformCompressionJobCollectionIdentifiers.includes(transformCompressionJobIdentifier)) {
          return false;
        }
        transformCompressionJobCollectionIdentifiers.push(transformCompressionJobIdentifier);
        return true;
      });
      return [...transformCompressionJobsToAdd, ...transformCompressionJobCollection];
    }
    return transformCompressionJobCollection;
  }

  protected convertDateFromClient<T extends ITransformCompressionJob | NewTransformCompressionJob | PartialUpdateTransformCompressionJob>(
    transformCompressionJob: T,
  ): RestOf<T> {
    return {
      ...transformCompressionJob,
      startDate: transformCompressionJob.startDate?.toJSON() ?? null,
      endDate: transformCompressionJob.endDate?.toJSON() ?? null,
      createdDate: transformCompressionJob.createdDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restTransformCompressionJob: RestTransformCompressionJob): ITransformCompressionJob {
    return {
      ...restTransformCompressionJob,
      startDate: restTransformCompressionJob.startDate ? dayjs(restTransformCompressionJob.startDate) : undefined,
      endDate: restTransformCompressionJob.endDate ? dayjs(restTransformCompressionJob.endDate) : undefined,
      createdDate: restTransformCompressionJob.createdDate ? dayjs(restTransformCompressionJob.createdDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestTransformCompressionJob>): HttpResponse<ITransformCompressionJob> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestTransformCompressionJob[]>): HttpResponse<ITransformCompressionJob[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
