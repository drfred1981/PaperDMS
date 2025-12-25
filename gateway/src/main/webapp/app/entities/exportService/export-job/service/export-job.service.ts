import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IExportJob, NewExportJob } from '../export-job.model';

export type PartialUpdateExportJob = Partial<IExportJob> & Pick<IExportJob, 'id'>;

type RestOf<T extends IExportJob | NewExportJob> = Omit<T, 'startDate' | 'endDate' | 'createdDate'> & {
  startDate?: string | null;
  endDate?: string | null;
  createdDate?: string | null;
};

export type RestExportJob = RestOf<IExportJob>;

export type NewRestExportJob = RestOf<NewExportJob>;

export type PartialUpdateRestExportJob = RestOf<PartialUpdateExportJob>;

export type EntityResponseType = HttpResponse<IExportJob>;
export type EntityArrayResponseType = HttpResponse<IExportJob[]>;

@Injectable({ providedIn: 'root' })
export class ExportJobService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/export-jobs', 'exportservice');

  create(exportJob: NewExportJob): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(exportJob);
    return this.http
      .post<RestExportJob>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(exportJob: IExportJob): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(exportJob);
    return this.http
      .put<RestExportJob>(`${this.resourceUrl}/${this.getExportJobIdentifier(exportJob)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(exportJob: PartialUpdateExportJob): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(exportJob);
    return this.http
      .patch<RestExportJob>(`${this.resourceUrl}/${this.getExportJobIdentifier(exportJob)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestExportJob>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestExportJob[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getExportJobIdentifier(exportJob: Pick<IExportJob, 'id'>): number {
    return exportJob.id;
  }

  compareExportJob(o1: Pick<IExportJob, 'id'> | null, o2: Pick<IExportJob, 'id'> | null): boolean {
    return o1 && o2 ? this.getExportJobIdentifier(o1) === this.getExportJobIdentifier(o2) : o1 === o2;
  }

  addExportJobToCollectionIfMissing<Type extends Pick<IExportJob, 'id'>>(
    exportJobCollection: Type[],
    ...exportJobsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const exportJobs: Type[] = exportJobsToCheck.filter(isPresent);
    if (exportJobs.length > 0) {
      const exportJobCollectionIdentifiers = exportJobCollection.map(exportJobItem => this.getExportJobIdentifier(exportJobItem));
      const exportJobsToAdd = exportJobs.filter(exportJobItem => {
        const exportJobIdentifier = this.getExportJobIdentifier(exportJobItem);
        if (exportJobCollectionIdentifiers.includes(exportJobIdentifier)) {
          return false;
        }
        exportJobCollectionIdentifiers.push(exportJobIdentifier);
        return true;
      });
      return [...exportJobsToAdd, ...exportJobCollection];
    }
    return exportJobCollection;
  }

  protected convertDateFromClient<T extends IExportJob | NewExportJob | PartialUpdateExportJob>(exportJob: T): RestOf<T> {
    return {
      ...exportJob,
      startDate: exportJob.startDate?.toJSON() ?? null,
      endDate: exportJob.endDate?.toJSON() ?? null,
      createdDate: exportJob.createdDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restExportJob: RestExportJob): IExportJob {
    return {
      ...restExportJob,
      startDate: restExportJob.startDate ? dayjs(restExportJob.startDate) : undefined,
      endDate: restExportJob.endDate ? dayjs(restExportJob.endDate) : undefined,
      createdDate: restExportJob.createdDate ? dayjs(restExportJob.createdDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestExportJob>): HttpResponse<IExportJob> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestExportJob[]>): HttpResponse<IExportJob[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
