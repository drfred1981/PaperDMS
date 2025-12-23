import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { ISimilarityJob, NewSimilarityJob } from '../similarity-job.model';

export type PartialUpdateSimilarityJob = Partial<ISimilarityJob> & Pick<ISimilarityJob, 'id'>;

type RestOf<T extends ISimilarityJob | NewSimilarityJob> = Omit<T, 'startDate' | 'endDate' | 'createdDate'> & {
  startDate?: string | null;
  endDate?: string | null;
  createdDate?: string | null;
};

export type RestSimilarityJob = RestOf<ISimilarityJob>;

export type NewRestSimilarityJob = RestOf<NewSimilarityJob>;

export type PartialUpdateRestSimilarityJob = RestOf<PartialUpdateSimilarityJob>;

export type EntityResponseType = HttpResponse<ISimilarityJob>;
export type EntityArrayResponseType = HttpResponse<ISimilarityJob[]>;

@Injectable({ providedIn: 'root' })
export class SimilarityJobService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/similarity-jobs', 'similarityservice');

  create(similarityJob: NewSimilarityJob): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(similarityJob);
    return this.http
      .post<RestSimilarityJob>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(similarityJob: ISimilarityJob): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(similarityJob);
    return this.http
      .put<RestSimilarityJob>(`${this.resourceUrl}/${encodeURIComponent(this.getSimilarityJobIdentifier(similarityJob))}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(similarityJob: PartialUpdateSimilarityJob): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(similarityJob);
    return this.http
      .patch<RestSimilarityJob>(`${this.resourceUrl}/${encodeURIComponent(this.getSimilarityJobIdentifier(similarityJob))}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestSimilarityJob>(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestSimilarityJob[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' });
  }

  getSimilarityJobIdentifier(similarityJob: Pick<ISimilarityJob, 'id'>): number {
    return similarityJob.id;
  }

  compareSimilarityJob(o1: Pick<ISimilarityJob, 'id'> | null, o2: Pick<ISimilarityJob, 'id'> | null): boolean {
    return o1 && o2 ? this.getSimilarityJobIdentifier(o1) === this.getSimilarityJobIdentifier(o2) : o1 === o2;
  }

  addSimilarityJobToCollectionIfMissing<Type extends Pick<ISimilarityJob, 'id'>>(
    similarityJobCollection: Type[],
    ...similarityJobsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const similarityJobs: Type[] = similarityJobsToCheck.filter(isPresent);
    if (similarityJobs.length > 0) {
      const similarityJobCollectionIdentifiers = similarityJobCollection.map(similarityJobItem =>
        this.getSimilarityJobIdentifier(similarityJobItem),
      );
      const similarityJobsToAdd = similarityJobs.filter(similarityJobItem => {
        const similarityJobIdentifier = this.getSimilarityJobIdentifier(similarityJobItem);
        if (similarityJobCollectionIdentifiers.includes(similarityJobIdentifier)) {
          return false;
        }
        similarityJobCollectionIdentifiers.push(similarityJobIdentifier);
        return true;
      });
      return [...similarityJobsToAdd, ...similarityJobCollection];
    }
    return similarityJobCollection;
  }

  protected convertDateFromClient<T extends ISimilarityJob | NewSimilarityJob | PartialUpdateSimilarityJob>(similarityJob: T): RestOf<T> {
    return {
      ...similarityJob,
      startDate: similarityJob.startDate?.toJSON() ?? null,
      endDate: similarityJob.endDate?.toJSON() ?? null,
      createdDate: similarityJob.createdDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restSimilarityJob: RestSimilarityJob): ISimilarityJob {
    return {
      ...restSimilarityJob,
      startDate: restSimilarityJob.startDate ? dayjs(restSimilarityJob.startDate) : undefined,
      endDate: restSimilarityJob.endDate ? dayjs(restSimilarityJob.endDate) : undefined,
      createdDate: restSimilarityJob.createdDate ? dayjs(restSimilarityJob.createdDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestSimilarityJob>): HttpResponse<ISimilarityJob> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestSimilarityJob[]>): HttpResponse<ISimilarityJob[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
