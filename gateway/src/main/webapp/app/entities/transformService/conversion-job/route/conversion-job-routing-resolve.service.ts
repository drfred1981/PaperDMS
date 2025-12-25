import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IConversionJob } from '../conversion-job.model';
import { ConversionJobService } from '../service/conversion-job.service';

const conversionJobResolve = (route: ActivatedRouteSnapshot): Observable<null | IConversionJob> => {
  const id = route.params.id;
  if (id) {
    return inject(ConversionJobService)
      .find(id)
      .pipe(
        mergeMap((conversionJob: HttpResponse<IConversionJob>) => {
          if (conversionJob.body) {
            return of(conversionJob.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default conversionJobResolve;
