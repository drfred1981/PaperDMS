import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IImageConversionHistory } from '../image-conversion-history.model';
import { ImageConversionHistoryService } from '../service/image-conversion-history.service';

const imageConversionHistoryResolve = (route: ActivatedRouteSnapshot): Observable<null | IImageConversionHistory> => {
  const id = route.params.id;
  if (id) {
    return inject(ImageConversionHistoryService)
      .find(id)
      .pipe(
        mergeMap((imageConversionHistory: HttpResponse<IImageConversionHistory>) => {
          if (imageConversionHistory.body) {
            return of(imageConversionHistory.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default imageConversionHistoryResolve;
