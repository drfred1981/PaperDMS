import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IImageConversionStatistics } from '../image-conversion-statistics.model';
import { ImageConversionStatisticsService } from '../service/image-conversion-statistics.service';

const imageConversionStatisticsResolve = (route: ActivatedRouteSnapshot): Observable<null | IImageConversionStatistics> => {
  const id = route.params.id;
  if (id) {
    return inject(ImageConversionStatisticsService)
      .find(id)
      .pipe(
        mergeMap((imageConversionStatistics: HttpResponse<IImageConversionStatistics>) => {
          if (imageConversionStatistics.body) {
            return of(imageConversionStatistics.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default imageConversionStatisticsResolve;
