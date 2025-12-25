import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IWatermarkJob } from '../watermark-job.model';
import { WatermarkJobService } from '../service/watermark-job.service';

const watermarkJobResolve = (route: ActivatedRouteSnapshot): Observable<null | IWatermarkJob> => {
  const id = route.params.id;
  if (id) {
    return inject(WatermarkJobService)
      .find(id)
      .pipe(
        mergeMap((watermarkJob: HttpResponse<IWatermarkJob>) => {
          if (watermarkJob.body) {
            return of(watermarkJob.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default watermarkJobResolve;
