import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IImageConversionBatch } from '../image-conversion-batch.model';
import { ImageConversionBatchService } from '../service/image-conversion-batch.service';

const imageConversionBatchResolve = (route: ActivatedRouteSnapshot): Observable<null | IImageConversionBatch> => {
  const id = route.params.id;
  if (id) {
    return inject(ImageConversionBatchService)
      .find(id)
      .pipe(
        mergeMap((imageConversionBatch: HttpResponse<IImageConversionBatch>) => {
          if (imageConversionBatch.body) {
            return of(imageConversionBatch.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default imageConversionBatchResolve;
