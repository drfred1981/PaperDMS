import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { IMaintenanceTask, NewMaintenanceTask } from '../maintenance-task.model';

export type PartialUpdateMaintenanceTask = Partial<IMaintenanceTask> & Pick<IMaintenanceTask, 'id'>;

type RestOf<T extends IMaintenanceTask | NewMaintenanceTask> = Omit<T, 'lastRun' | 'nextRun' | 'createdDate'> & {
  lastRun?: string | null;
  nextRun?: string | null;
  createdDate?: string | null;
};

export type RestMaintenanceTask = RestOf<IMaintenanceTask>;

export type NewRestMaintenanceTask = RestOf<NewMaintenanceTask>;

export type PartialUpdateRestMaintenanceTask = RestOf<PartialUpdateMaintenanceTask>;

export type EntityResponseType = HttpResponse<IMaintenanceTask>;
export type EntityArrayResponseType = HttpResponse<IMaintenanceTask[]>;

@Injectable({ providedIn: 'root' })
export class MaintenanceTaskService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/maintenance-tasks', 'monitoringservice');

  create(maintenanceTask: NewMaintenanceTask): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(maintenanceTask);
    return this.http
      .post<RestMaintenanceTask>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(maintenanceTask: IMaintenanceTask): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(maintenanceTask);
    return this.http
      .put<RestMaintenanceTask>(`${this.resourceUrl}/${encodeURIComponent(this.getMaintenanceTaskIdentifier(maintenanceTask))}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(maintenanceTask: PartialUpdateMaintenanceTask): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(maintenanceTask);
    return this.http
      .patch<RestMaintenanceTask>(`${this.resourceUrl}/${encodeURIComponent(this.getMaintenanceTaskIdentifier(maintenanceTask))}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestMaintenanceTask>(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestMaintenanceTask[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' });
  }

  getMaintenanceTaskIdentifier(maintenanceTask: Pick<IMaintenanceTask, 'id'>): number {
    return maintenanceTask.id;
  }

  compareMaintenanceTask(o1: Pick<IMaintenanceTask, 'id'> | null, o2: Pick<IMaintenanceTask, 'id'> | null): boolean {
    return o1 && o2 ? this.getMaintenanceTaskIdentifier(o1) === this.getMaintenanceTaskIdentifier(o2) : o1 === o2;
  }

  addMaintenanceTaskToCollectionIfMissing<Type extends Pick<IMaintenanceTask, 'id'>>(
    maintenanceTaskCollection: Type[],
    ...maintenanceTasksToCheck: (Type | null | undefined)[]
  ): Type[] {
    const maintenanceTasks: Type[] = maintenanceTasksToCheck.filter(isPresent);
    if (maintenanceTasks.length > 0) {
      const maintenanceTaskCollectionIdentifiers = maintenanceTaskCollection.map(maintenanceTaskItem =>
        this.getMaintenanceTaskIdentifier(maintenanceTaskItem),
      );
      const maintenanceTasksToAdd = maintenanceTasks.filter(maintenanceTaskItem => {
        const maintenanceTaskIdentifier = this.getMaintenanceTaskIdentifier(maintenanceTaskItem);
        if (maintenanceTaskCollectionIdentifiers.includes(maintenanceTaskIdentifier)) {
          return false;
        }
        maintenanceTaskCollectionIdentifiers.push(maintenanceTaskIdentifier);
        return true;
      });
      return [...maintenanceTasksToAdd, ...maintenanceTaskCollection];
    }
    return maintenanceTaskCollection;
  }

  protected convertDateFromClient<T extends IMaintenanceTask | NewMaintenanceTask | PartialUpdateMaintenanceTask>(
    maintenanceTask: T,
  ): RestOf<T> {
    return {
      ...maintenanceTask,
      lastRun: maintenanceTask.lastRun?.toJSON() ?? null,
      nextRun: maintenanceTask.nextRun?.toJSON() ?? null,
      createdDate: maintenanceTask.createdDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restMaintenanceTask: RestMaintenanceTask): IMaintenanceTask {
    return {
      ...restMaintenanceTask,
      lastRun: restMaintenanceTask.lastRun ? dayjs(restMaintenanceTask.lastRun) : undefined,
      nextRun: restMaintenanceTask.nextRun ? dayjs(restMaintenanceTask.nextRun) : undefined,
      createdDate: restMaintenanceTask.createdDate ? dayjs(restMaintenanceTask.createdDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestMaintenanceTask>): HttpResponse<IMaintenanceTask> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestMaintenanceTask[]>): HttpResponse<IMaintenanceTask[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
