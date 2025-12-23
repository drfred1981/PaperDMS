import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { ISimilarityCluster, NewSimilarityCluster } from '../similarity-cluster.model';

export type PartialUpdateSimilarityCluster = Partial<ISimilarityCluster> & Pick<ISimilarityCluster, 'id'>;

type RestOf<T extends ISimilarityCluster | NewSimilarityCluster> = Omit<T, 'createdDate' | 'lastUpdated'> & {
  createdDate?: string | null;
  lastUpdated?: string | null;
};

export type RestSimilarityCluster = RestOf<ISimilarityCluster>;

export type NewRestSimilarityCluster = RestOf<NewSimilarityCluster>;

export type PartialUpdateRestSimilarityCluster = RestOf<PartialUpdateSimilarityCluster>;

export type EntityResponseType = HttpResponse<ISimilarityCluster>;
export type EntityArrayResponseType = HttpResponse<ISimilarityCluster[]>;

@Injectable({ providedIn: 'root' })
export class SimilarityClusterService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/similarity-clusters', 'similarityservice');

  create(similarityCluster: NewSimilarityCluster): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(similarityCluster);
    return this.http
      .post<RestSimilarityCluster>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(similarityCluster: ISimilarityCluster): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(similarityCluster);
    return this.http
      .put<RestSimilarityCluster>(
        `${this.resourceUrl}/${encodeURIComponent(this.getSimilarityClusterIdentifier(similarityCluster))}`,
        copy,
        { observe: 'response' },
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(similarityCluster: PartialUpdateSimilarityCluster): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(similarityCluster);
    return this.http
      .patch<RestSimilarityCluster>(
        `${this.resourceUrl}/${encodeURIComponent(this.getSimilarityClusterIdentifier(similarityCluster))}`,
        copy,
        { observe: 'response' },
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestSimilarityCluster>(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestSimilarityCluster[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' });
  }

  getSimilarityClusterIdentifier(similarityCluster: Pick<ISimilarityCluster, 'id'>): number {
    return similarityCluster.id;
  }

  compareSimilarityCluster(o1: Pick<ISimilarityCluster, 'id'> | null, o2: Pick<ISimilarityCluster, 'id'> | null): boolean {
    return o1 && o2 ? this.getSimilarityClusterIdentifier(o1) === this.getSimilarityClusterIdentifier(o2) : o1 === o2;
  }

  addSimilarityClusterToCollectionIfMissing<Type extends Pick<ISimilarityCluster, 'id'>>(
    similarityClusterCollection: Type[],
    ...similarityClustersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const similarityClusters: Type[] = similarityClustersToCheck.filter(isPresent);
    if (similarityClusters.length > 0) {
      const similarityClusterCollectionIdentifiers = similarityClusterCollection.map(similarityClusterItem =>
        this.getSimilarityClusterIdentifier(similarityClusterItem),
      );
      const similarityClustersToAdd = similarityClusters.filter(similarityClusterItem => {
        const similarityClusterIdentifier = this.getSimilarityClusterIdentifier(similarityClusterItem);
        if (similarityClusterCollectionIdentifiers.includes(similarityClusterIdentifier)) {
          return false;
        }
        similarityClusterCollectionIdentifiers.push(similarityClusterIdentifier);
        return true;
      });
      return [...similarityClustersToAdd, ...similarityClusterCollection];
    }
    return similarityClusterCollection;
  }

  protected convertDateFromClient<T extends ISimilarityCluster | NewSimilarityCluster | PartialUpdateSimilarityCluster>(
    similarityCluster: T,
  ): RestOf<T> {
    return {
      ...similarityCluster,
      createdDate: similarityCluster.createdDate?.toJSON() ?? null,
      lastUpdated: similarityCluster.lastUpdated?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restSimilarityCluster: RestSimilarityCluster): ISimilarityCluster {
    return {
      ...restSimilarityCluster,
      createdDate: restSimilarityCluster.createdDate ? dayjs(restSimilarityCluster.createdDate) : undefined,
      lastUpdated: restSimilarityCluster.lastUpdated ? dayjs(restSimilarityCluster.lastUpdated) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestSimilarityCluster>): HttpResponse<ISimilarityCluster> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestSimilarityCluster[]>): HttpResponse<ISimilarityCluster[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
