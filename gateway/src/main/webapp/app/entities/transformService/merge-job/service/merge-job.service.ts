import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMergeJob, NewMergeJob } from '../merge-job.model';

export type PartialUpdateMergeJob = Partial<IMergeJob> & Pick<IMergeJob, 'id'>;

type RestOf<T extends IMergeJob | NewMergeJob> = Omit<T, 'startDate' | 'endDate' | 'createdDate'> & {
  startDate?: string | null;
  endDate?: string | null;
  createdDate?: string | null;
};

export type RestMergeJob = RestOf<IMergeJob>;

export type NewRestMergeJob = RestOf<NewMergeJob>;

export type PartialUpdateRestMergeJob = RestOf<PartialUpdateMergeJob>;

export type EntityResponseType = HttpResponse<IMergeJob>;
export type EntityArrayResponseType = HttpResponse<IMergeJob[]>;

@Injectable({ providedIn: 'root' })
export class MergeJobService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/merge-jobs', 'transformservice');

  create(mergeJob: NewMergeJob): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(mergeJob);
    return this.http
      .post<RestMergeJob>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(mergeJob: IMergeJob): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(mergeJob);
    return this.http
      .put<RestMergeJob>(`${this.resourceUrl}/${this.getMergeJobIdentifier(mergeJob)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(mergeJob: PartialUpdateMergeJob): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(mergeJob);
    return this.http
      .patch<RestMergeJob>(`${this.resourceUrl}/${this.getMergeJobIdentifier(mergeJob)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestMergeJob>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestMergeJob[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMergeJobIdentifier(mergeJob: Pick<IMergeJob, 'id'>): number {
    return mergeJob.id;
  }

  compareMergeJob(o1: Pick<IMergeJob, 'id'> | null, o2: Pick<IMergeJob, 'id'> | null): boolean {
    return o1 && o2 ? this.getMergeJobIdentifier(o1) === this.getMergeJobIdentifier(o2) : o1 === o2;
  }

  addMergeJobToCollectionIfMissing<Type extends Pick<IMergeJob, 'id'>>(
    mergeJobCollection: Type[],
    ...mergeJobsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const mergeJobs: Type[] = mergeJobsToCheck.filter(isPresent);
    if (mergeJobs.length > 0) {
      const mergeJobCollectionIdentifiers = mergeJobCollection.map(mergeJobItem => this.getMergeJobIdentifier(mergeJobItem));
      const mergeJobsToAdd = mergeJobs.filter(mergeJobItem => {
        const mergeJobIdentifier = this.getMergeJobIdentifier(mergeJobItem);
        if (mergeJobCollectionIdentifiers.includes(mergeJobIdentifier)) {
          return false;
        }
        mergeJobCollectionIdentifiers.push(mergeJobIdentifier);
        return true;
      });
      return [...mergeJobsToAdd, ...mergeJobCollection];
    }
    return mergeJobCollection;
  }

  protected convertDateFromClient<T extends IMergeJob | NewMergeJob | PartialUpdateMergeJob>(mergeJob: T): RestOf<T> {
    return {
      ...mergeJob,
      startDate: mergeJob.startDate?.toJSON() ?? null,
      endDate: mergeJob.endDate?.toJSON() ?? null,
      createdDate: mergeJob.createdDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restMergeJob: RestMergeJob): IMergeJob {
    return {
      ...restMergeJob,
      startDate: restMergeJob.startDate ? dayjs(restMergeJob.startDate) : undefined,
      endDate: restMergeJob.endDate ? dayjs(restMergeJob.endDate) : undefined,
      createdDate: restMergeJob.createdDate ? dayjs(restMergeJob.createdDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestMergeJob>): HttpResponse<IMergeJob> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestMergeJob[]>): HttpResponse<IMergeJob[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
