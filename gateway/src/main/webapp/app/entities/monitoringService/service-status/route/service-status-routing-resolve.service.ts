import { HttpResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ServiceStatusService } from '../service/service-status.service';
import { IServiceStatus } from '../service-status.model';

const serviceStatusResolve = (route: ActivatedRouteSnapshot): Observable<null | IServiceStatus> => {
  const id = route.params.id;
  if (id) {
    return inject(ServiceStatusService)
      .find(id)
      .pipe(
        mergeMap((serviceStatus: HttpResponse<IServiceStatus>) => {
          if (serviceStatus.body) {
            return of(serviceStatus.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default serviceStatusResolve;
