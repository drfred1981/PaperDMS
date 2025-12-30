import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMonitoringDocumentWatch } from '../monitoring-document-watch.model';
import { MonitoringDocumentWatchService } from '../service/monitoring-document-watch.service';

const monitoringDocumentWatchResolve = (route: ActivatedRouteSnapshot): Observable<null | IMonitoringDocumentWatch> => {
  const id = route.params.id;
  if (id) {
    return inject(MonitoringDocumentWatchService)
      .find(id)
      .pipe(
        mergeMap((monitoringDocumentWatch: HttpResponse<IMonitoringDocumentWatch>) => {
          if (monitoringDocumentWatch.body) {
            return of(monitoringDocumentWatch.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default monitoringDocumentWatchResolve;
