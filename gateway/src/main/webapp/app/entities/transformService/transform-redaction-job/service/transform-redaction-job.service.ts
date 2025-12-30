import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ITransformRedactionJob, NewTransformRedactionJob } from '../transform-redaction-job.model';

export type PartialUpdateTransformRedactionJob = Partial<ITransformRedactionJob> & Pick<ITransformRedactionJob, 'id'>;

type RestOf<T extends ITransformRedactionJob | NewTransformRedactionJob> = Omit<T, 'startDate' | 'endDate' | 'createdDate'> & {
  startDate?: string | null;
  endDate?: string | null;
  createdDate?: string | null;
};

export type RestTransformRedactionJob = RestOf<ITransformRedactionJob>;

export type NewRestTransformRedactionJob = RestOf<NewTransformRedactionJob>;

export type PartialUpdateRestTransformRedactionJob = RestOf<PartialUpdateTransformRedactionJob>;

export type EntityResponseType = HttpResponse<ITransformRedactionJob>;
export type EntityArrayResponseType = HttpResponse<ITransformRedactionJob[]>;

@Injectable({ providedIn: 'root' })
export class TransformRedactionJobService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/transform-redaction-jobs', 'transformservice');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/transform-redaction-jobs/_search', 'transformservice');

  create(transformRedactionJob: NewTransformRedactionJob): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(transformRedactionJob);
    return this.http
      .post<RestTransformRedactionJob>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(transformRedactionJob: ITransformRedactionJob): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(transformRedactionJob);
    return this.http
      .put<RestTransformRedactionJob>(`${this.resourceUrl}/${this.getTransformRedactionJobIdentifier(transformRedactionJob)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(transformRedactionJob: PartialUpdateTransformRedactionJob): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(transformRedactionJob);
    return this.http
      .patch<RestTransformRedactionJob>(`${this.resourceUrl}/${this.getTransformRedactionJobIdentifier(transformRedactionJob)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestTransformRedactionJob>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestTransformRedactionJob[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestTransformRedactionJob[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<ITransformRedactionJob[]>()], asapScheduler)),
    );
  }

  getTransformRedactionJobIdentifier(transformRedactionJob: Pick<ITransformRedactionJob, 'id'>): number {
    return transformRedactionJob.id;
  }

  compareTransformRedactionJob(o1: Pick<ITransformRedactionJob, 'id'> | null, o2: Pick<ITransformRedactionJob, 'id'> | null): boolean {
    return o1 && o2 ? this.getTransformRedactionJobIdentifier(o1) === this.getTransformRedactionJobIdentifier(o2) : o1 === o2;
  }

  addTransformRedactionJobToCollectionIfMissing<Type extends Pick<ITransformRedactionJob, 'id'>>(
    transformRedactionJobCollection: Type[],
    ...transformRedactionJobsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const transformRedactionJobs: Type[] = transformRedactionJobsToCheck.filter(isPresent);
    if (transformRedactionJobs.length > 0) {
      const transformRedactionJobCollectionIdentifiers = transformRedactionJobCollection.map(transformRedactionJobItem =>
        this.getTransformRedactionJobIdentifier(transformRedactionJobItem),
      );
      const transformRedactionJobsToAdd = transformRedactionJobs.filter(transformRedactionJobItem => {
        const transformRedactionJobIdentifier = this.getTransformRedactionJobIdentifier(transformRedactionJobItem);
        if (transformRedactionJobCollectionIdentifiers.includes(transformRedactionJobIdentifier)) {
          return false;
        }
        transformRedactionJobCollectionIdentifiers.push(transformRedactionJobIdentifier);
        return true;
      });
      return [...transformRedactionJobsToAdd, ...transformRedactionJobCollection];
    }
    return transformRedactionJobCollection;
  }

  protected convertDateFromClient<T extends ITransformRedactionJob | NewTransformRedactionJob | PartialUpdateTransformRedactionJob>(
    transformRedactionJob: T,
  ): RestOf<T> {
    return {
      ...transformRedactionJob,
      startDate: transformRedactionJob.startDate?.toJSON() ?? null,
      endDate: transformRedactionJob.endDate?.toJSON() ?? null,
      createdDate: transformRedactionJob.createdDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restTransformRedactionJob: RestTransformRedactionJob): ITransformRedactionJob {
    return {
      ...restTransformRedactionJob,
      startDate: restTransformRedactionJob.startDate ? dayjs(restTransformRedactionJob.startDate) : undefined,
      endDate: restTransformRedactionJob.endDate ? dayjs(restTransformRedactionJob.endDate) : undefined,
      createdDate: restTransformRedactionJob.createdDate ? dayjs(restTransformRedactionJob.createdDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestTransformRedactionJob>): HttpResponse<ITransformRedactionJob> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestTransformRedactionJob[]>): HttpResponse<ITransformRedactionJob[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
