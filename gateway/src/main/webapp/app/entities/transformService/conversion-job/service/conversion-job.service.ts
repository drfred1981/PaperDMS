import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { IConversionJob, NewConversionJob } from '../conversion-job.model';

export type PartialUpdateConversionJob = Partial<IConversionJob> & Pick<IConversionJob, 'id'>;

type RestOf<T extends IConversionJob | NewConversionJob> = Omit<T, 'startDate' | 'endDate' | 'createdDate'> & {
  startDate?: string | null;
  endDate?: string | null;
  createdDate?: string | null;
};

export type RestConversionJob = RestOf<IConversionJob>;

export type NewRestConversionJob = RestOf<NewConversionJob>;

export type PartialUpdateRestConversionJob = RestOf<PartialUpdateConversionJob>;

export type EntityResponseType = HttpResponse<IConversionJob>;
export type EntityArrayResponseType = HttpResponse<IConversionJob[]>;

@Injectable({ providedIn: 'root' })
export class ConversionJobService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/conversion-jobs', 'transformservice');

  create(conversionJob: NewConversionJob): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(conversionJob);
    return this.http
      .post<RestConversionJob>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(conversionJob: IConversionJob): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(conversionJob);
    return this.http
      .put<RestConversionJob>(`${this.resourceUrl}/${encodeURIComponent(this.getConversionJobIdentifier(conversionJob))}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(conversionJob: PartialUpdateConversionJob): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(conversionJob);
    return this.http
      .patch<RestConversionJob>(`${this.resourceUrl}/${encodeURIComponent(this.getConversionJobIdentifier(conversionJob))}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestConversionJob>(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestConversionJob[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' });
  }

  getConversionJobIdentifier(conversionJob: Pick<IConversionJob, 'id'>): number {
    return conversionJob.id;
  }

  compareConversionJob(o1: Pick<IConversionJob, 'id'> | null, o2: Pick<IConversionJob, 'id'> | null): boolean {
    return o1 && o2 ? this.getConversionJobIdentifier(o1) === this.getConversionJobIdentifier(o2) : o1 === o2;
  }

  addConversionJobToCollectionIfMissing<Type extends Pick<IConversionJob, 'id'>>(
    conversionJobCollection: Type[],
    ...conversionJobsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const conversionJobs: Type[] = conversionJobsToCheck.filter(isPresent);
    if (conversionJobs.length > 0) {
      const conversionJobCollectionIdentifiers = conversionJobCollection.map(conversionJobItem =>
        this.getConversionJobIdentifier(conversionJobItem),
      );
      const conversionJobsToAdd = conversionJobs.filter(conversionJobItem => {
        const conversionJobIdentifier = this.getConversionJobIdentifier(conversionJobItem);
        if (conversionJobCollectionIdentifiers.includes(conversionJobIdentifier)) {
          return false;
        }
        conversionJobCollectionIdentifiers.push(conversionJobIdentifier);
        return true;
      });
      return [...conversionJobsToAdd, ...conversionJobCollection];
    }
    return conversionJobCollection;
  }

  protected convertDateFromClient<T extends IConversionJob | NewConversionJob | PartialUpdateConversionJob>(conversionJob: T): RestOf<T> {
    return {
      ...conversionJob,
      startDate: conversionJob.startDate?.toJSON() ?? null,
      endDate: conversionJob.endDate?.toJSON() ?? null,
      createdDate: conversionJob.createdDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restConversionJob: RestConversionJob): IConversionJob {
    return {
      ...restConversionJob,
      startDate: restConversionJob.startDate ? dayjs(restConversionJob.startDate) : undefined,
      endDate: restConversionJob.endDate ? dayjs(restConversionJob.endDate) : undefined,
      createdDate: restConversionJob.createdDate ? dayjs(restConversionJob.createdDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestConversionJob>): HttpResponse<IConversionJob> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestConversionJob[]>): HttpResponse<IConversionJob[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
