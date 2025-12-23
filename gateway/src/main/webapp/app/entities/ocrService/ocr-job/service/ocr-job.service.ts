import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { IOcrJob, NewOcrJob } from '../ocr-job.model';

export type PartialUpdateOcrJob = Partial<IOcrJob> & Pick<IOcrJob, 'id'>;

type RestOf<T extends IOcrJob | NewOcrJob> = Omit<T, 'startDate' | 'endDate' | 'createdDate'> & {
  startDate?: string | null;
  endDate?: string | null;
  createdDate?: string | null;
};

export type RestOcrJob = RestOf<IOcrJob>;

export type NewRestOcrJob = RestOf<NewOcrJob>;

export type PartialUpdateRestOcrJob = RestOf<PartialUpdateOcrJob>;

export type EntityResponseType = HttpResponse<IOcrJob>;
export type EntityArrayResponseType = HttpResponse<IOcrJob[]>;

@Injectable({ providedIn: 'root' })
export class OcrJobService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ocr-jobs', 'ocrservice');

  create(ocrJob: NewOcrJob): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ocrJob);
    return this.http
      .post<RestOcrJob>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(ocrJob: IOcrJob): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ocrJob);
    return this.http
      .put<RestOcrJob>(`${this.resourceUrl}/${encodeURIComponent(this.getOcrJobIdentifier(ocrJob))}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(ocrJob: PartialUpdateOcrJob): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ocrJob);
    return this.http
      .patch<RestOcrJob>(`${this.resourceUrl}/${encodeURIComponent(this.getOcrJobIdentifier(ocrJob))}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestOcrJob>(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestOcrJob[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' });
  }

  getOcrJobIdentifier(ocrJob: Pick<IOcrJob, 'id'>): number {
    return ocrJob.id;
  }

  compareOcrJob(o1: Pick<IOcrJob, 'id'> | null, o2: Pick<IOcrJob, 'id'> | null): boolean {
    return o1 && o2 ? this.getOcrJobIdentifier(o1) === this.getOcrJobIdentifier(o2) : o1 === o2;
  }

  addOcrJobToCollectionIfMissing<Type extends Pick<IOcrJob, 'id'>>(
    ocrJobCollection: Type[],
    ...ocrJobsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const ocrJobs: Type[] = ocrJobsToCheck.filter(isPresent);
    if (ocrJobs.length > 0) {
      const ocrJobCollectionIdentifiers = ocrJobCollection.map(ocrJobItem => this.getOcrJobIdentifier(ocrJobItem));
      const ocrJobsToAdd = ocrJobs.filter(ocrJobItem => {
        const ocrJobIdentifier = this.getOcrJobIdentifier(ocrJobItem);
        if (ocrJobCollectionIdentifiers.includes(ocrJobIdentifier)) {
          return false;
        }
        ocrJobCollectionIdentifiers.push(ocrJobIdentifier);
        return true;
      });
      return [...ocrJobsToAdd, ...ocrJobCollection];
    }
    return ocrJobCollection;
  }

  protected convertDateFromClient<T extends IOcrJob | NewOcrJob | PartialUpdateOcrJob>(ocrJob: T): RestOf<T> {
    return {
      ...ocrJob,
      startDate: ocrJob.startDate?.toJSON() ?? null,
      endDate: ocrJob.endDate?.toJSON() ?? null,
      createdDate: ocrJob.createdDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restOcrJob: RestOcrJob): IOcrJob {
    return {
      ...restOcrJob,
      startDate: restOcrJob.startDate ? dayjs(restOcrJob.startDate) : undefined,
      endDate: restOcrJob.endDate ? dayjs(restOcrJob.endDate) : undefined,
      createdDate: restOcrJob.createdDate ? dayjs(restOcrJob.createdDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestOcrJob>): HttpResponse<IOcrJob> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestOcrJob[]>): HttpResponse<IOcrJob[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
