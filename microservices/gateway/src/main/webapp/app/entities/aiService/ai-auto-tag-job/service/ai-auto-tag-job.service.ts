import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IAIAutoTagJob, NewAIAutoTagJob } from '../ai-auto-tag-job.model';

export type PartialUpdateAIAutoTagJob = Partial<IAIAutoTagJob> & Pick<IAIAutoTagJob, 'id'>;

type RestOf<T extends IAIAutoTagJob | NewAIAutoTagJob> = Omit<T, 'startDate' | 'endDate' | 'createdDate'> & {
  startDate?: string | null;
  endDate?: string | null;
  createdDate?: string | null;
};

export type RestAIAutoTagJob = RestOf<IAIAutoTagJob>;

export type NewRestAIAutoTagJob = RestOf<NewAIAutoTagJob>;

export type PartialUpdateRestAIAutoTagJob = RestOf<PartialUpdateAIAutoTagJob>;

export type EntityResponseType = HttpResponse<IAIAutoTagJob>;
export type EntityArrayResponseType = HttpResponse<IAIAutoTagJob[]>;

@Injectable({ providedIn: 'root' })
export class AIAutoTagJobService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ai-auto-tag-jobs', 'aiservice');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/ai-auto-tag-jobs/_search', 'aiservice');

  create(aIAutoTagJob: NewAIAutoTagJob): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(aIAutoTagJob);
    return this.http
      .post<RestAIAutoTagJob>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(aIAutoTagJob: IAIAutoTagJob): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(aIAutoTagJob);
    return this.http
      .put<RestAIAutoTagJob>(`${this.resourceUrl}/${this.getAIAutoTagJobIdentifier(aIAutoTagJob)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(aIAutoTagJob: PartialUpdateAIAutoTagJob): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(aIAutoTagJob);
    return this.http
      .patch<RestAIAutoTagJob>(`${this.resourceUrl}/${this.getAIAutoTagJobIdentifier(aIAutoTagJob)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestAIAutoTagJob>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestAIAutoTagJob[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestAIAutoTagJob[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IAIAutoTagJob[]>()], asapScheduler)),
    );
  }

  getAIAutoTagJobIdentifier(aIAutoTagJob: Pick<IAIAutoTagJob, 'id'>): number {
    return aIAutoTagJob.id;
  }

  compareAIAutoTagJob(o1: Pick<IAIAutoTagJob, 'id'> | null, o2: Pick<IAIAutoTagJob, 'id'> | null): boolean {
    return o1 && o2 ? this.getAIAutoTagJobIdentifier(o1) === this.getAIAutoTagJobIdentifier(o2) : o1 === o2;
  }

  addAIAutoTagJobToCollectionIfMissing<Type extends Pick<IAIAutoTagJob, 'id'>>(
    aIAutoTagJobCollection: Type[],
    ...aIAutoTagJobsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const aIAutoTagJobs: Type[] = aIAutoTagJobsToCheck.filter(isPresent);
    if (aIAutoTagJobs.length > 0) {
      const aIAutoTagJobCollectionIdentifiers = aIAutoTagJobCollection.map(aIAutoTagJobItem =>
        this.getAIAutoTagJobIdentifier(aIAutoTagJobItem),
      );
      const aIAutoTagJobsToAdd = aIAutoTagJobs.filter(aIAutoTagJobItem => {
        const aIAutoTagJobIdentifier = this.getAIAutoTagJobIdentifier(aIAutoTagJobItem);
        if (aIAutoTagJobCollectionIdentifiers.includes(aIAutoTagJobIdentifier)) {
          return false;
        }
        aIAutoTagJobCollectionIdentifiers.push(aIAutoTagJobIdentifier);
        return true;
      });
      return [...aIAutoTagJobsToAdd, ...aIAutoTagJobCollection];
    }
    return aIAutoTagJobCollection;
  }

  protected convertDateFromClient<T extends IAIAutoTagJob | NewAIAutoTagJob | PartialUpdateAIAutoTagJob>(aIAutoTagJob: T): RestOf<T> {
    return {
      ...aIAutoTagJob,
      startDate: aIAutoTagJob.startDate?.toJSON() ?? null,
      endDate: aIAutoTagJob.endDate?.toJSON() ?? null,
      createdDate: aIAutoTagJob.createdDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restAIAutoTagJob: RestAIAutoTagJob): IAIAutoTagJob {
    return {
      ...restAIAutoTagJob,
      startDate: restAIAutoTagJob.startDate ? dayjs(restAIAutoTagJob.startDate) : undefined,
      endDate: restAIAutoTagJob.endDate ? dayjs(restAIAutoTagJob.endDate) : undefined,
      createdDate: restAIAutoTagJob.createdDate ? dayjs(restAIAutoTagJob.createdDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestAIAutoTagJob>): HttpResponse<IAIAutoTagJob> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestAIAutoTagJob[]>): HttpResponse<IAIAutoTagJob[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
