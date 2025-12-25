import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IScanJob, NewScanJob } from '../scan-job.model';

export type PartialUpdateScanJob = Partial<IScanJob> & Pick<IScanJob, 'id'>;

type RestOf<T extends IScanJob | NewScanJob> = Omit<T, 'startDate' | 'endDate' | 'createdDate'> & {
  startDate?: string | null;
  endDate?: string | null;
  createdDate?: string | null;
};

export type RestScanJob = RestOf<IScanJob>;

export type NewRestScanJob = RestOf<NewScanJob>;

export type PartialUpdateRestScanJob = RestOf<PartialUpdateScanJob>;

export type EntityResponseType = HttpResponse<IScanJob>;
export type EntityArrayResponseType = HttpResponse<IScanJob[]>;

@Injectable({ providedIn: 'root' })
export class ScanJobService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/scan-jobs', 'scanservice');

  create(scanJob: NewScanJob): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(scanJob);
    return this.http
      .post<RestScanJob>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(scanJob: IScanJob): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(scanJob);
    return this.http
      .put<RestScanJob>(`${this.resourceUrl}/${this.getScanJobIdentifier(scanJob)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(scanJob: PartialUpdateScanJob): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(scanJob);
    return this.http
      .patch<RestScanJob>(`${this.resourceUrl}/${this.getScanJobIdentifier(scanJob)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestScanJob>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestScanJob[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getScanJobIdentifier(scanJob: Pick<IScanJob, 'id'>): number {
    return scanJob.id;
  }

  compareScanJob(o1: Pick<IScanJob, 'id'> | null, o2: Pick<IScanJob, 'id'> | null): boolean {
    return o1 && o2 ? this.getScanJobIdentifier(o1) === this.getScanJobIdentifier(o2) : o1 === o2;
  }

  addScanJobToCollectionIfMissing<Type extends Pick<IScanJob, 'id'>>(
    scanJobCollection: Type[],
    ...scanJobsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const scanJobs: Type[] = scanJobsToCheck.filter(isPresent);
    if (scanJobs.length > 0) {
      const scanJobCollectionIdentifiers = scanJobCollection.map(scanJobItem => this.getScanJobIdentifier(scanJobItem));
      const scanJobsToAdd = scanJobs.filter(scanJobItem => {
        const scanJobIdentifier = this.getScanJobIdentifier(scanJobItem);
        if (scanJobCollectionIdentifiers.includes(scanJobIdentifier)) {
          return false;
        }
        scanJobCollectionIdentifiers.push(scanJobIdentifier);
        return true;
      });
      return [...scanJobsToAdd, ...scanJobCollection];
    }
    return scanJobCollection;
  }

  protected convertDateFromClient<T extends IScanJob | NewScanJob | PartialUpdateScanJob>(scanJob: T): RestOf<T> {
    return {
      ...scanJob,
      startDate: scanJob.startDate?.toJSON() ?? null,
      endDate: scanJob.endDate?.toJSON() ?? null,
      createdDate: scanJob.createdDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restScanJob: RestScanJob): IScanJob {
    return {
      ...restScanJob,
      startDate: restScanJob.startDate ? dayjs(restScanJob.startDate) : undefined,
      endDate: restScanJob.endDate ? dayjs(restScanJob.endDate) : undefined,
      createdDate: restScanJob.createdDate ? dayjs(restScanJob.createdDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestScanJob>): HttpResponse<IScanJob> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestScanJob[]>): HttpResponse<IScanJob[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
