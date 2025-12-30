import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ITransformMergeJob, NewTransformMergeJob } from '../transform-merge-job.model';

export type PartialUpdateTransformMergeJob = Partial<ITransformMergeJob> & Pick<ITransformMergeJob, 'id'>;

type RestOf<T extends ITransformMergeJob | NewTransformMergeJob> = Omit<T, 'startDate' | 'endDate' | 'createdDate'> & {
  startDate?: string | null;
  endDate?: string | null;
  createdDate?: string | null;
};

export type RestTransformMergeJob = RestOf<ITransformMergeJob>;

export type NewRestTransformMergeJob = RestOf<NewTransformMergeJob>;

export type PartialUpdateRestTransformMergeJob = RestOf<PartialUpdateTransformMergeJob>;

export type EntityResponseType = HttpResponse<ITransformMergeJob>;
export type EntityArrayResponseType = HttpResponse<ITransformMergeJob[]>;

@Injectable({ providedIn: 'root' })
export class TransformMergeJobService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/transform-merge-jobs', 'transformservice');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/transform-merge-jobs/_search', 'transformservice');

  create(transformMergeJob: NewTransformMergeJob): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(transformMergeJob);
    return this.http
      .post<RestTransformMergeJob>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(transformMergeJob: ITransformMergeJob): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(transformMergeJob);
    return this.http
      .put<RestTransformMergeJob>(`${this.resourceUrl}/${this.getTransformMergeJobIdentifier(transformMergeJob)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(transformMergeJob: PartialUpdateTransformMergeJob): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(transformMergeJob);
    return this.http
      .patch<RestTransformMergeJob>(`${this.resourceUrl}/${this.getTransformMergeJobIdentifier(transformMergeJob)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestTransformMergeJob>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestTransformMergeJob[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestTransformMergeJob[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<ITransformMergeJob[]>()], asapScheduler)),
    );
  }

  getTransformMergeJobIdentifier(transformMergeJob: Pick<ITransformMergeJob, 'id'>): number {
    return transformMergeJob.id;
  }

  compareTransformMergeJob(o1: Pick<ITransformMergeJob, 'id'> | null, o2: Pick<ITransformMergeJob, 'id'> | null): boolean {
    return o1 && o2 ? this.getTransformMergeJobIdentifier(o1) === this.getTransformMergeJobIdentifier(o2) : o1 === o2;
  }

  addTransformMergeJobToCollectionIfMissing<Type extends Pick<ITransformMergeJob, 'id'>>(
    transformMergeJobCollection: Type[],
    ...transformMergeJobsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const transformMergeJobs: Type[] = transformMergeJobsToCheck.filter(isPresent);
    if (transformMergeJobs.length > 0) {
      const transformMergeJobCollectionIdentifiers = transformMergeJobCollection.map(transformMergeJobItem =>
        this.getTransformMergeJobIdentifier(transformMergeJobItem),
      );
      const transformMergeJobsToAdd = transformMergeJobs.filter(transformMergeJobItem => {
        const transformMergeJobIdentifier = this.getTransformMergeJobIdentifier(transformMergeJobItem);
        if (transformMergeJobCollectionIdentifiers.includes(transformMergeJobIdentifier)) {
          return false;
        }
        transformMergeJobCollectionIdentifiers.push(transformMergeJobIdentifier);
        return true;
      });
      return [...transformMergeJobsToAdd, ...transformMergeJobCollection];
    }
    return transformMergeJobCollection;
  }

  protected convertDateFromClient<T extends ITransformMergeJob | NewTransformMergeJob | PartialUpdateTransformMergeJob>(
    transformMergeJob: T,
  ): RestOf<T> {
    return {
      ...transformMergeJob,
      startDate: transformMergeJob.startDate?.toJSON() ?? null,
      endDate: transformMergeJob.endDate?.toJSON() ?? null,
      createdDate: transformMergeJob.createdDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restTransformMergeJob: RestTransformMergeJob): ITransformMergeJob {
    return {
      ...restTransformMergeJob,
      startDate: restTransformMergeJob.startDate ? dayjs(restTransformMergeJob.startDate) : undefined,
      endDate: restTransformMergeJob.endDate ? dayjs(restTransformMergeJob.endDate) : undefined,
      createdDate: restTransformMergeJob.createdDate ? dayjs(restTransformMergeJob.createdDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestTransformMergeJob>): HttpResponse<ITransformMergeJob> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestTransformMergeJob[]>): HttpResponse<ITransformMergeJob[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
