import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IScannerConfiguration } from '../scanner-configuration.model';
import { ScannerConfigurationService } from '../service/scanner-configuration.service';

const scannerConfigurationResolve = (route: ActivatedRouteSnapshot): Observable<null | IScannerConfiguration> => {
  const id = route.params.id;
  if (id) {
    return inject(ScannerConfigurationService)
      .find(id)
      .pipe(
        mergeMap((scannerConfiguration: HttpResponse<IScannerConfiguration>) => {
          if (scannerConfiguration.body) {
            return of(scannerConfiguration.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default scannerConfigurationResolve;
