import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { IArchiveJob, NewArchiveJob } from '../archive-job.model';

export type PartialUpdateArchiveJob = Partial<IArchiveJob> & Pick<IArchiveJob, 'id'>;

type RestOf<T extends IArchiveJob | NewArchiveJob> = Omit<T, 'startDate' | 'endDate' | 'createdDate'> & {
  startDate?: string | null;
  endDate?: string | null;
  createdDate?: string | null;
};

export type RestArchiveJob = RestOf<IArchiveJob>;

export type NewRestArchiveJob = RestOf<NewArchiveJob>;

export type PartialUpdateRestArchiveJob = RestOf<PartialUpdateArchiveJob>;

export type EntityResponseType = HttpResponse<IArchiveJob>;
export type EntityArrayResponseType = HttpResponse<IArchiveJob[]>;

@Injectable({ providedIn: 'root' })
export class ArchiveJobService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/archive-jobs', 'archiveservice');

  create(archiveJob: NewArchiveJob): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(archiveJob);
    return this.http
      .post<RestArchiveJob>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(archiveJob: IArchiveJob): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(archiveJob);
    return this.http
      .put<RestArchiveJob>(`${this.resourceUrl}/${encodeURIComponent(this.getArchiveJobIdentifier(archiveJob))}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(archiveJob: PartialUpdateArchiveJob): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(archiveJob);
    return this.http
      .patch<RestArchiveJob>(`${this.resourceUrl}/${encodeURIComponent(this.getArchiveJobIdentifier(archiveJob))}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestArchiveJob>(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestArchiveJob[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' });
  }

  getArchiveJobIdentifier(archiveJob: Pick<IArchiveJob, 'id'>): number {
    return archiveJob.id;
  }

  compareArchiveJob(o1: Pick<IArchiveJob, 'id'> | null, o2: Pick<IArchiveJob, 'id'> | null): boolean {
    return o1 && o2 ? this.getArchiveJobIdentifier(o1) === this.getArchiveJobIdentifier(o2) : o1 === o2;
  }

  addArchiveJobToCollectionIfMissing<Type extends Pick<IArchiveJob, 'id'>>(
    archiveJobCollection: Type[],
    ...archiveJobsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const archiveJobs: Type[] = archiveJobsToCheck.filter(isPresent);
    if (archiveJobs.length > 0) {
      const archiveJobCollectionIdentifiers = archiveJobCollection.map(archiveJobItem => this.getArchiveJobIdentifier(archiveJobItem));
      const archiveJobsToAdd = archiveJobs.filter(archiveJobItem => {
        const archiveJobIdentifier = this.getArchiveJobIdentifier(archiveJobItem);
        if (archiveJobCollectionIdentifiers.includes(archiveJobIdentifier)) {
          return false;
        }
        archiveJobCollectionIdentifiers.push(archiveJobIdentifier);
        return true;
      });
      return [...archiveJobsToAdd, ...archiveJobCollection];
    }
    return archiveJobCollection;
  }

  protected convertDateFromClient<T extends IArchiveJob | NewArchiveJob | PartialUpdateArchiveJob>(archiveJob: T): RestOf<T> {
    return {
      ...archiveJob,
      startDate: archiveJob.startDate?.toJSON() ?? null,
      endDate: archiveJob.endDate?.toJSON() ?? null,
      createdDate: archiveJob.createdDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restArchiveJob: RestArchiveJob): IArchiveJob {
    return {
      ...restArchiveJob,
      startDate: restArchiveJob.startDate ? dayjs(restArchiveJob.startDate) : undefined,
      endDate: restArchiveJob.endDate ? dayjs(restArchiveJob.endDate) : undefined,
      createdDate: restArchiveJob.createdDate ? dayjs(restArchiveJob.createdDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestArchiveJob>): HttpResponse<IArchiveJob> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestArchiveJob[]>): HttpResponse<IArchiveJob[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
