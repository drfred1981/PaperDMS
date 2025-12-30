import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IImagePdfConversionRequest } from '../image-pdf-conversion-request.model';
import { ImagePdfConversionRequestService } from '../service/image-pdf-conversion-request.service';

const imagePdfConversionRequestResolve = (route: ActivatedRouteSnapshot): Observable<null | IImagePdfConversionRequest> => {
  const id = route.params.id;
  if (id) {
    return inject(ImagePdfConversionRequestService)
      .find(id)
      .pipe(
        mergeMap((imagePdfConversionRequest: HttpResponse<IImagePdfConversionRequest>) => {
          if (imagePdfConversionRequest.body) {
            return of(imagePdfConversionRequest.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default imagePdfConversionRequestResolve;
