import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IWatermarkJob, NewWatermarkJob } from '../watermark-job.model';

export type PartialUpdateWatermarkJob = Partial<IWatermarkJob> & Pick<IWatermarkJob, 'id'>;

type RestOf<T extends IWatermarkJob | NewWatermarkJob> = Omit<T, 'startDate' | 'endDate' | 'createdDate'> & {
  startDate?: string | null;
  endDate?: string | null;
  createdDate?: string | null;
};

export type RestWatermarkJob = RestOf<IWatermarkJob>;

export type NewRestWatermarkJob = RestOf<NewWatermarkJob>;

export type PartialUpdateRestWatermarkJob = RestOf<PartialUpdateWatermarkJob>;

export type EntityResponseType = HttpResponse<IWatermarkJob>;
export type EntityArrayResponseType = HttpResponse<IWatermarkJob[]>;

@Injectable({ providedIn: 'root' })
export class WatermarkJobService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/watermark-jobs', 'transformservice');

  create(watermarkJob: NewWatermarkJob): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(watermarkJob);
    return this.http
      .post<RestWatermarkJob>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(watermarkJob: IWatermarkJob): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(watermarkJob);
    return this.http
      .put<RestWatermarkJob>(`${this.resourceUrl}/${this.getWatermarkJobIdentifier(watermarkJob)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(watermarkJob: PartialUpdateWatermarkJob): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(watermarkJob);
    return this.http
      .patch<RestWatermarkJob>(`${this.resourceUrl}/${this.getWatermarkJobIdentifier(watermarkJob)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestWatermarkJob>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestWatermarkJob[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getWatermarkJobIdentifier(watermarkJob: Pick<IWatermarkJob, 'id'>): number {
    return watermarkJob.id;
  }

  compareWatermarkJob(o1: Pick<IWatermarkJob, 'id'> | null, o2: Pick<IWatermarkJob, 'id'> | null): boolean {
    return o1 && o2 ? this.getWatermarkJobIdentifier(o1) === this.getWatermarkJobIdentifier(o2) : o1 === o2;
  }

  addWatermarkJobToCollectionIfMissing<Type extends Pick<IWatermarkJob, 'id'>>(
    watermarkJobCollection: Type[],
    ...watermarkJobsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const watermarkJobs: Type[] = watermarkJobsToCheck.filter(isPresent);
    if (watermarkJobs.length > 0) {
      const watermarkJobCollectionIdentifiers = watermarkJobCollection.map(watermarkJobItem =>
        this.getWatermarkJobIdentifier(watermarkJobItem),
      );
      const watermarkJobsToAdd = watermarkJobs.filter(watermarkJobItem => {
        const watermarkJobIdentifier = this.getWatermarkJobIdentifier(watermarkJobItem);
        if (watermarkJobCollectionIdentifiers.includes(watermarkJobIdentifier)) {
          return false;
        }
        watermarkJobCollectionIdentifiers.push(watermarkJobIdentifier);
        return true;
      });
      return [...watermarkJobsToAdd, ...watermarkJobCollection];
    }
    return watermarkJobCollection;
  }

  protected convertDateFromClient<T extends IWatermarkJob | NewWatermarkJob | PartialUpdateWatermarkJob>(watermarkJob: T): RestOf<T> {
    return {
      ...watermarkJob,
      startDate: watermarkJob.startDate?.toJSON() ?? null,
      endDate: watermarkJob.endDate?.toJSON() ?? null,
      createdDate: watermarkJob.createdDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restWatermarkJob: RestWatermarkJob): IWatermarkJob {
    return {
      ...restWatermarkJob,
      startDate: restWatermarkJob.startDate ? dayjs(restWatermarkJob.startDate) : undefined,
      endDate: restWatermarkJob.endDate ? dayjs(restWatermarkJob.endDate) : undefined,
      createdDate: restWatermarkJob.createdDate ? dayjs(restWatermarkJob.createdDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestWatermarkJob>): HttpResponse<IWatermarkJob> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestWatermarkJob[]>): HttpResponse<IWatermarkJob[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
