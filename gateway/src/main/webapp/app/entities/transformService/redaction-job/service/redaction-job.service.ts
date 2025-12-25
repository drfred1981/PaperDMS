import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRedactionJob, NewRedactionJob } from '../redaction-job.model';

export type PartialUpdateRedactionJob = Partial<IRedactionJob> & Pick<IRedactionJob, 'id'>;

type RestOf<T extends IRedactionJob | NewRedactionJob> = Omit<T, 'startDate' | 'endDate' | 'createdDate'> & {
  startDate?: string | null;
  endDate?: string | null;
  createdDate?: string | null;
};

export type RestRedactionJob = RestOf<IRedactionJob>;

export type NewRestRedactionJob = RestOf<NewRedactionJob>;

export type PartialUpdateRestRedactionJob = RestOf<PartialUpdateRedactionJob>;

export type EntityResponseType = HttpResponse<IRedactionJob>;
export type EntityArrayResponseType = HttpResponse<IRedactionJob[]>;

@Injectable({ providedIn: 'root' })
export class RedactionJobService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/redaction-jobs', 'transformservice');

  create(redactionJob: NewRedactionJob): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(redactionJob);
    return this.http
      .post<RestRedactionJob>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(redactionJob: IRedactionJob): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(redactionJob);
    return this.http
      .put<RestRedactionJob>(`${this.resourceUrl}/${this.getRedactionJobIdentifier(redactionJob)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(redactionJob: PartialUpdateRedactionJob): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(redactionJob);
    return this.http
      .patch<RestRedactionJob>(`${this.resourceUrl}/${this.getRedactionJobIdentifier(redactionJob)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestRedactionJob>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestRedactionJob[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getRedactionJobIdentifier(redactionJob: Pick<IRedactionJob, 'id'>): number {
    return redactionJob.id;
  }

  compareRedactionJob(o1: Pick<IRedactionJob, 'id'> | null, o2: Pick<IRedactionJob, 'id'> | null): boolean {
    return o1 && o2 ? this.getRedactionJobIdentifier(o1) === this.getRedactionJobIdentifier(o2) : o1 === o2;
  }

  addRedactionJobToCollectionIfMissing<Type extends Pick<IRedactionJob, 'id'>>(
    redactionJobCollection: Type[],
    ...redactionJobsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const redactionJobs: Type[] = redactionJobsToCheck.filter(isPresent);
    if (redactionJobs.length > 0) {
      const redactionJobCollectionIdentifiers = redactionJobCollection.map(redactionJobItem =>
        this.getRedactionJobIdentifier(redactionJobItem),
      );
      const redactionJobsToAdd = redactionJobs.filter(redactionJobItem => {
        const redactionJobIdentifier = this.getRedactionJobIdentifier(redactionJobItem);
        if (redactionJobCollectionIdentifiers.includes(redactionJobIdentifier)) {
          return false;
        }
        redactionJobCollectionIdentifiers.push(redactionJobIdentifier);
        return true;
      });
      return [...redactionJobsToAdd, ...redactionJobCollection];
    }
    return redactionJobCollection;
  }

  protected convertDateFromClient<T extends IRedactionJob | NewRedactionJob | PartialUpdateRedactionJob>(redactionJob: T): RestOf<T> {
    return {
      ...redactionJob,
      startDate: redactionJob.startDate?.toJSON() ?? null,
      endDate: redactionJob.endDate?.toJSON() ?? null,
      createdDate: redactionJob.createdDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restRedactionJob: RestRedactionJob): IRedactionJob {
    return {
      ...restRedactionJob,
      startDate: restRedactionJob.startDate ? dayjs(restRedactionJob.startDate) : undefined,
      endDate: restRedactionJob.endDate ? dayjs(restRedactionJob.endDate) : undefined,
      createdDate: restRedactionJob.createdDate ? dayjs(restRedactionJob.createdDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestRedactionJob>): HttpResponse<IRedactionJob> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestRedactionJob[]>): HttpResponse<IRedactionJob[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
