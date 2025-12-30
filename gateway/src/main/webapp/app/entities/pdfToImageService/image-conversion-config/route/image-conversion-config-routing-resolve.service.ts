import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IImageConversionConfig } from '../image-conversion-config.model';
import { ImageConversionConfigService } from '../service/image-conversion-config.service';

const imageConversionConfigResolve = (route: ActivatedRouteSnapshot): Observable<null | IImageConversionConfig> => {
  const id = route.params.id;
  if (id) {
    return inject(ImageConversionConfigService)
      .find(id)
      .pipe(
        mergeMap((imageConversionConfig: HttpResponse<IImageConversionConfig>) => {
          if (imageConversionConfig.body) {
            return of(imageConversionConfig.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default imageConversionConfigResolve;
