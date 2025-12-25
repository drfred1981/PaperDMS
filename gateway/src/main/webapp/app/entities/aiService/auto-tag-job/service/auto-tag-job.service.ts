import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAutoTagJob, NewAutoTagJob } from '../auto-tag-job.model';

export type PartialUpdateAutoTagJob = Partial<IAutoTagJob> & Pick<IAutoTagJob, 'id'>;

type RestOf<T extends IAutoTagJob | NewAutoTagJob> = Omit<T, 'startDate' | 'endDate' | 'createdDate'> & {
  startDate?: string | null;
  endDate?: string | null;
  createdDate?: string | null;
};

export type RestAutoTagJob = RestOf<IAutoTagJob>;

export type NewRestAutoTagJob = RestOf<NewAutoTagJob>;

export type PartialUpdateRestAutoTagJob = RestOf<PartialUpdateAutoTagJob>;

export type EntityResponseType = HttpResponse<IAutoTagJob>;
export type EntityArrayResponseType = HttpResponse<IAutoTagJob[]>;

@Injectable({ providedIn: 'root' })
export class AutoTagJobService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/auto-tag-jobs', 'aiservice');

  create(autoTagJob: NewAutoTagJob): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(autoTagJob);
    return this.http
      .post<RestAutoTagJob>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(autoTagJob: IAutoTagJob): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(autoTagJob);
    return this.http
      .put<RestAutoTagJob>(`${this.resourceUrl}/${this.getAutoTagJobIdentifier(autoTagJob)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(autoTagJob: PartialUpdateAutoTagJob): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(autoTagJob);
    return this.http
      .patch<RestAutoTagJob>(`${this.resourceUrl}/${this.getAutoTagJobIdentifier(autoTagJob)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestAutoTagJob>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestAutoTagJob[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAutoTagJobIdentifier(autoTagJob: Pick<IAutoTagJob, 'id'>): number {
    return autoTagJob.id;
  }

  compareAutoTagJob(o1: Pick<IAutoTagJob, 'id'> | null, o2: Pick<IAutoTagJob, 'id'> | null): boolean {
    return o1 && o2 ? this.getAutoTagJobIdentifier(o1) === this.getAutoTagJobIdentifier(o2) : o1 === o2;
  }

  addAutoTagJobToCollectionIfMissing<Type extends Pick<IAutoTagJob, 'id'>>(
    autoTagJobCollection: Type[],
    ...autoTagJobsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const autoTagJobs: Type[] = autoTagJobsToCheck.filter(isPresent);
    if (autoTagJobs.length > 0) {
      const autoTagJobCollectionIdentifiers = autoTagJobCollection.map(autoTagJobItem => this.getAutoTagJobIdentifier(autoTagJobItem));
      const autoTagJobsToAdd = autoTagJobs.filter(autoTagJobItem => {
        const autoTagJobIdentifier = this.getAutoTagJobIdentifier(autoTagJobItem);
        if (autoTagJobCollectionIdentifiers.includes(autoTagJobIdentifier)) {
          return false;
        }
        autoTagJobCollectionIdentifiers.push(autoTagJobIdentifier);
        return true;
      });
      return [...autoTagJobsToAdd, ...autoTagJobCollection];
    }
    return autoTagJobCollection;
  }

  protected convertDateFromClient<T extends IAutoTagJob | NewAutoTagJob | PartialUpdateAutoTagJob>(autoTagJob: T): RestOf<T> {
    return {
      ...autoTagJob,
      startDate: autoTagJob.startDate?.toJSON() ?? null,
      endDate: autoTagJob.endDate?.toJSON() ?? null,
      createdDate: autoTagJob.createdDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restAutoTagJob: RestAutoTagJob): IAutoTagJob {
    return {
      ...restAutoTagJob,
      startDate: restAutoTagJob.startDate ? dayjs(restAutoTagJob.startDate) : undefined,
      endDate: restAutoTagJob.endDate ? dayjs(restAutoTagJob.endDate) : undefined,
      createdDate: restAutoTagJob.createdDate ? dayjs(restAutoTagJob.createdDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestAutoTagJob>): HttpResponse<IAutoTagJob> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestAutoTagJob[]>): HttpResponse<IAutoTagJob[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
