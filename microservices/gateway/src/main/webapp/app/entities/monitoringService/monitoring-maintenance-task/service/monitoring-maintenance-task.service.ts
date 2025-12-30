import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IMonitoringMaintenanceTask, NewMonitoringMaintenanceTask } from '../monitoring-maintenance-task.model';

export type PartialUpdateMonitoringMaintenanceTask = Partial<IMonitoringMaintenanceTask> & Pick<IMonitoringMaintenanceTask, 'id'>;

type RestOf<T extends IMonitoringMaintenanceTask | NewMonitoringMaintenanceTask> = Omit<T, 'lastRun' | 'nextRun' | 'createdDate'> & {
  lastRun?: string | null;
  nextRun?: string | null;
  createdDate?: string | null;
};

export type RestMonitoringMaintenanceTask = RestOf<IMonitoringMaintenanceTask>;

export type NewRestMonitoringMaintenanceTask = RestOf<NewMonitoringMaintenanceTask>;

export type PartialUpdateRestMonitoringMaintenanceTask = RestOf<PartialUpdateMonitoringMaintenanceTask>;

export type EntityResponseType = HttpResponse<IMonitoringMaintenanceTask>;
export type EntityArrayResponseType = HttpResponse<IMonitoringMaintenanceTask[]>;

@Injectable({ providedIn: 'root' })
export class MonitoringMaintenanceTaskService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/monitoring-maintenance-tasks', 'monitoringservice');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor(
    'api/monitoring-maintenance-tasks/_search',
    'monitoringservice',
  );

  create(monitoringMaintenanceTask: NewMonitoringMaintenanceTask): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(monitoringMaintenanceTask);
    return this.http
      .post<RestMonitoringMaintenanceTask>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(monitoringMaintenanceTask: IMonitoringMaintenanceTask): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(monitoringMaintenanceTask);
    return this.http
      .put<RestMonitoringMaintenanceTask>(
        `${this.resourceUrl}/${this.getMonitoringMaintenanceTaskIdentifier(monitoringMaintenanceTask)}`,
        copy,
        { observe: 'response' },
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(monitoringMaintenanceTask: PartialUpdateMonitoringMaintenanceTask): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(monitoringMaintenanceTask);
    return this.http
      .patch<RestMonitoringMaintenanceTask>(
        `${this.resourceUrl}/${this.getMonitoringMaintenanceTaskIdentifier(monitoringMaintenanceTask)}`,
        copy,
        { observe: 'response' },
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestMonitoringMaintenanceTask>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestMonitoringMaintenanceTask[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestMonitoringMaintenanceTask[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IMonitoringMaintenanceTask[]>()], asapScheduler)),
    );
  }

  getMonitoringMaintenanceTaskIdentifier(monitoringMaintenanceTask: Pick<IMonitoringMaintenanceTask, 'id'>): number {
    return monitoringMaintenanceTask.id;
  }

  compareMonitoringMaintenanceTask(
    o1: Pick<IMonitoringMaintenanceTask, 'id'> | null,
    o2: Pick<IMonitoringMaintenanceTask, 'id'> | null,
  ): boolean {
    return o1 && o2 ? this.getMonitoringMaintenanceTaskIdentifier(o1) === this.getMonitoringMaintenanceTaskIdentifier(o2) : o1 === o2;
  }

  addMonitoringMaintenanceTaskToCollectionIfMissing<Type extends Pick<IMonitoringMaintenanceTask, 'id'>>(
    monitoringMaintenanceTaskCollection: Type[],
    ...monitoringMaintenanceTasksToCheck: (Type | null | undefined)[]
  ): Type[] {
    const monitoringMaintenanceTasks: Type[] = monitoringMaintenanceTasksToCheck.filter(isPresent);
    if (monitoringMaintenanceTasks.length > 0) {
      const monitoringMaintenanceTaskCollectionIdentifiers = monitoringMaintenanceTaskCollection.map(monitoringMaintenanceTaskItem =>
        this.getMonitoringMaintenanceTaskIdentifier(monitoringMaintenanceTaskItem),
      );
      const monitoringMaintenanceTasksToAdd = monitoringMaintenanceTasks.filter(monitoringMaintenanceTaskItem => {
        const monitoringMaintenanceTaskIdentifier = this.getMonitoringMaintenanceTaskIdentifier(monitoringMaintenanceTaskItem);
        if (monitoringMaintenanceTaskCollectionIdentifiers.includes(monitoringMaintenanceTaskIdentifier)) {
          return false;
        }
        monitoringMaintenanceTaskCollectionIdentifiers.push(monitoringMaintenanceTaskIdentifier);
        return true;
      });
      return [...monitoringMaintenanceTasksToAdd, ...monitoringMaintenanceTaskCollection];
    }
    return monitoringMaintenanceTaskCollection;
  }

  protected convertDateFromClient<
    T extends IMonitoringMaintenanceTask | NewMonitoringMaintenanceTask | PartialUpdateMonitoringMaintenanceTask,
  >(monitoringMaintenanceTask: T): RestOf<T> {
    return {
      ...monitoringMaintenanceTask,
      lastRun: monitoringMaintenanceTask.lastRun?.toJSON() ?? null,
      nextRun: monitoringMaintenanceTask.nextRun?.toJSON() ?? null,
      createdDate: monitoringMaintenanceTask.createdDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restMonitoringMaintenanceTask: RestMonitoringMaintenanceTask): IMonitoringMaintenanceTask {
    return {
      ...restMonitoringMaintenanceTask,
      lastRun: restMonitoringMaintenanceTask.lastRun ? dayjs(restMonitoringMaintenanceTask.lastRun) : undefined,
      nextRun: restMonitoringMaintenanceTask.nextRun ? dayjs(restMonitoringMaintenanceTask.nextRun) : undefined,
      createdDate: restMonitoringMaintenanceTask.createdDate ? dayjs(restMonitoringMaintenanceTask.createdDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestMonitoringMaintenanceTask>): HttpResponse<IMonitoringMaintenanceTask> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestMonitoringMaintenanceTask[]>): HttpResponse<IMonitoringMaintenanceTask[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
