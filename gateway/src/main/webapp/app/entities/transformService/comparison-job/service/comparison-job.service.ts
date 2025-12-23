import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { IComparisonJob, NewComparisonJob } from '../comparison-job.model';

export type PartialUpdateComparisonJob = Partial<IComparisonJob> & Pick<IComparisonJob, 'id'>;

type RestOf<T extends IComparisonJob | NewComparisonJob> = Omit<T, 'comparedDate'> & {
  comparedDate?: string | null;
};

export type RestComparisonJob = RestOf<IComparisonJob>;

export type NewRestComparisonJob = RestOf<NewComparisonJob>;

export type PartialUpdateRestComparisonJob = RestOf<PartialUpdateComparisonJob>;

export type EntityResponseType = HttpResponse<IComparisonJob>;
export type EntityArrayResponseType = HttpResponse<IComparisonJob[]>;

@Injectable({ providedIn: 'root' })
export class ComparisonJobService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/comparison-jobs', 'transformservice');

  create(comparisonJob: NewComparisonJob): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(comparisonJob);
    return this.http
      .post<RestComparisonJob>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(comparisonJob: IComparisonJob): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(comparisonJob);
    return this.http
      .put<RestComparisonJob>(`${this.resourceUrl}/${encodeURIComponent(this.getComparisonJobIdentifier(comparisonJob))}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(comparisonJob: PartialUpdateComparisonJob): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(comparisonJob);
    return this.http
      .patch<RestComparisonJob>(`${this.resourceUrl}/${encodeURIComponent(this.getComparisonJobIdentifier(comparisonJob))}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestComparisonJob>(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestComparisonJob[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' });
  }

  getComparisonJobIdentifier(comparisonJob: Pick<IComparisonJob, 'id'>): number {
    return comparisonJob.id;
  }

  compareComparisonJob(o1: Pick<IComparisonJob, 'id'> | null, o2: Pick<IComparisonJob, 'id'> | null): boolean {
    return o1 && o2 ? this.getComparisonJobIdentifier(o1) === this.getComparisonJobIdentifier(o2) : o1 === o2;
  }

  addComparisonJobToCollectionIfMissing<Type extends Pick<IComparisonJob, 'id'>>(
    comparisonJobCollection: Type[],
    ...comparisonJobsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const comparisonJobs: Type[] = comparisonJobsToCheck.filter(isPresent);
    if (comparisonJobs.length > 0) {
      const comparisonJobCollectionIdentifiers = comparisonJobCollection.map(comparisonJobItem =>
        this.getComparisonJobIdentifier(comparisonJobItem),
      );
      const comparisonJobsToAdd = comparisonJobs.filter(comparisonJobItem => {
        const comparisonJobIdentifier = this.getComparisonJobIdentifier(comparisonJobItem);
        if (comparisonJobCollectionIdentifiers.includes(comparisonJobIdentifier)) {
          return false;
        }
        comparisonJobCollectionIdentifiers.push(comparisonJobIdentifier);
        return true;
      });
      return [...comparisonJobsToAdd, ...comparisonJobCollection];
    }
    return comparisonJobCollection;
  }

  protected convertDateFromClient<T extends IComparisonJob | NewComparisonJob | PartialUpdateComparisonJob>(comparisonJob: T): RestOf<T> {
    return {
      ...comparisonJob,
      comparedDate: comparisonJob.comparedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restComparisonJob: RestComparisonJob): IComparisonJob {
    return {
      ...restComparisonJob,
      comparedDate: restComparisonJob.comparedDate ? dayjs(restComparisonJob.comparedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestComparisonJob>): HttpResponse<IComparisonJob> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestComparisonJob[]>): HttpResponse<IComparisonJob[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
