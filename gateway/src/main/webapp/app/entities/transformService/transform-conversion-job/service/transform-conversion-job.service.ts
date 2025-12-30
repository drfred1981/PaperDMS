import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ITransformConversionJob, NewTransformConversionJob } from '../transform-conversion-job.model';

export type PartialUpdateTransformConversionJob = Partial<ITransformConversionJob> & Pick<ITransformConversionJob, 'id'>;

type RestOf<T extends ITransformConversionJob | NewTransformConversionJob> = Omit<T, 'startDate' | 'endDate' | 'createdDate'> & {
  startDate?: string | null;
  endDate?: string | null;
  createdDate?: string | null;
};

export type RestTransformConversionJob = RestOf<ITransformConversionJob>;

export type NewRestTransformConversionJob = RestOf<NewTransformConversionJob>;

export type PartialUpdateRestTransformConversionJob = RestOf<PartialUpdateTransformConversionJob>;

export type EntityResponseType = HttpResponse<ITransformConversionJob>;
export type EntityArrayResponseType = HttpResponse<ITransformConversionJob[]>;

@Injectable({ providedIn: 'root' })
export class TransformConversionJobService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/transform-conversion-jobs', 'transformservice');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/transform-conversion-jobs/_search', 'transformservice');

  create(transformConversionJob: NewTransformConversionJob): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(transformConversionJob);
    return this.http
      .post<RestTransformConversionJob>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(transformConversionJob: ITransformConversionJob): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(transformConversionJob);
    return this.http
      .put<RestTransformConversionJob>(`${this.resourceUrl}/${this.getTransformConversionJobIdentifier(transformConversionJob)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(transformConversionJob: PartialUpdateTransformConversionJob): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(transformConversionJob);
    return this.http
      .patch<RestTransformConversionJob>(`${this.resourceUrl}/${this.getTransformConversionJobIdentifier(transformConversionJob)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestTransformConversionJob>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestTransformConversionJob[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestTransformConversionJob[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<ITransformConversionJob[]>()], asapScheduler)),
    );
  }

  getTransformConversionJobIdentifier(transformConversionJob: Pick<ITransformConversionJob, 'id'>): number {
    return transformConversionJob.id;
  }

  compareTransformConversionJob(o1: Pick<ITransformConversionJob, 'id'> | null, o2: Pick<ITransformConversionJob, 'id'> | null): boolean {
    return o1 && o2 ? this.getTransformConversionJobIdentifier(o1) === this.getTransformConversionJobIdentifier(o2) : o1 === o2;
  }

  addTransformConversionJobToCollectionIfMissing<Type extends Pick<ITransformConversionJob, 'id'>>(
    transformConversionJobCollection: Type[],
    ...transformConversionJobsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const transformConversionJobs: Type[] = transformConversionJobsToCheck.filter(isPresent);
    if (transformConversionJobs.length > 0) {
      const transformConversionJobCollectionIdentifiers = transformConversionJobCollection.map(transformConversionJobItem =>
        this.getTransformConversionJobIdentifier(transformConversionJobItem),
      );
      const transformConversionJobsToAdd = transformConversionJobs.filter(transformConversionJobItem => {
        const transformConversionJobIdentifier = this.getTransformConversionJobIdentifier(transformConversionJobItem);
        if (transformConversionJobCollectionIdentifiers.includes(transformConversionJobIdentifier)) {
          return false;
        }
        transformConversionJobCollectionIdentifiers.push(transformConversionJobIdentifier);
        return true;
      });
      return [...transformConversionJobsToAdd, ...transformConversionJobCollection];
    }
    return transformConversionJobCollection;
  }

  protected convertDateFromClient<T extends ITransformConversionJob | NewTransformConversionJob | PartialUpdateTransformConversionJob>(
    transformConversionJob: T,
  ): RestOf<T> {
    return {
      ...transformConversionJob,
      startDate: transformConversionJob.startDate?.toJSON() ?? null,
      endDate: transformConversionJob.endDate?.toJSON() ?? null,
      createdDate: transformConversionJob.createdDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restTransformConversionJob: RestTransformConversionJob): ITransformConversionJob {
    return {
      ...restTransformConversionJob,
      startDate: restTransformConversionJob.startDate ? dayjs(restTransformConversionJob.startDate) : undefined,
      endDate: restTransformConversionJob.endDate ? dayjs(restTransformConversionJob.endDate) : undefined,
      createdDate: restTransformConversionJob.createdDate ? dayjs(restTransformConversionJob.createdDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestTransformConversionJob>): HttpResponse<ITransformConversionJob> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestTransformConversionJob[]>): HttpResponse<ITransformConversionJob[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
