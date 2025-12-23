import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { ICompressionJob, NewCompressionJob } from '../compression-job.model';

export type PartialUpdateCompressionJob = Partial<ICompressionJob> & Pick<ICompressionJob, 'id'>;

type RestOf<T extends ICompressionJob | NewCompressionJob> = Omit<T, 'startDate' | 'endDate' | 'createdDate'> & {
  startDate?: string | null;
  endDate?: string | null;
  createdDate?: string | null;
};

export type RestCompressionJob = RestOf<ICompressionJob>;

export type NewRestCompressionJob = RestOf<NewCompressionJob>;

export type PartialUpdateRestCompressionJob = RestOf<PartialUpdateCompressionJob>;

export type EntityResponseType = HttpResponse<ICompressionJob>;
export type EntityArrayResponseType = HttpResponse<ICompressionJob[]>;

@Injectable({ providedIn: 'root' })
export class CompressionJobService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/compression-jobs', 'transformservice');

  create(compressionJob: NewCompressionJob): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(compressionJob);
    return this.http
      .post<RestCompressionJob>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(compressionJob: ICompressionJob): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(compressionJob);
    return this.http
      .put<RestCompressionJob>(`${this.resourceUrl}/${encodeURIComponent(this.getCompressionJobIdentifier(compressionJob))}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(compressionJob: PartialUpdateCompressionJob): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(compressionJob);
    return this.http
      .patch<RestCompressionJob>(`${this.resourceUrl}/${encodeURIComponent(this.getCompressionJobIdentifier(compressionJob))}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestCompressionJob>(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestCompressionJob[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' });
  }

  getCompressionJobIdentifier(compressionJob: Pick<ICompressionJob, 'id'>): number {
    return compressionJob.id;
  }

  compareCompressionJob(o1: Pick<ICompressionJob, 'id'> | null, o2: Pick<ICompressionJob, 'id'> | null): boolean {
    return o1 && o2 ? this.getCompressionJobIdentifier(o1) === this.getCompressionJobIdentifier(o2) : o1 === o2;
  }

  addCompressionJobToCollectionIfMissing<Type extends Pick<ICompressionJob, 'id'>>(
    compressionJobCollection: Type[],
    ...compressionJobsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const compressionJobs: Type[] = compressionJobsToCheck.filter(isPresent);
    if (compressionJobs.length > 0) {
      const compressionJobCollectionIdentifiers = compressionJobCollection.map(compressionJobItem =>
        this.getCompressionJobIdentifier(compressionJobItem),
      );
      const compressionJobsToAdd = compressionJobs.filter(compressionJobItem => {
        const compressionJobIdentifier = this.getCompressionJobIdentifier(compressionJobItem);
        if (compressionJobCollectionIdentifiers.includes(compressionJobIdentifier)) {
          return false;
        }
        compressionJobCollectionIdentifiers.push(compressionJobIdentifier);
        return true;
      });
      return [...compressionJobsToAdd, ...compressionJobCollection];
    }
    return compressionJobCollection;
  }

  protected convertDateFromClient<T extends ICompressionJob | NewCompressionJob | PartialUpdateCompressionJob>(
    compressionJob: T,
  ): RestOf<T> {
    return {
      ...compressionJob,
      startDate: compressionJob.startDate?.toJSON() ?? null,
      endDate: compressionJob.endDate?.toJSON() ?? null,
      createdDate: compressionJob.createdDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restCompressionJob: RestCompressionJob): ICompressionJob {
    return {
      ...restCompressionJob,
      startDate: restCompressionJob.startDate ? dayjs(restCompressionJob.startDate) : undefined,
      endDate: restCompressionJob.endDate ? dayjs(restCompressionJob.endDate) : undefined,
      createdDate: restCompressionJob.createdDate ? dayjs(restCompressionJob.createdDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestCompressionJob>): HttpResponse<ICompressionJob> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestCompressionJob[]>): HttpResponse<ICompressionJob[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
