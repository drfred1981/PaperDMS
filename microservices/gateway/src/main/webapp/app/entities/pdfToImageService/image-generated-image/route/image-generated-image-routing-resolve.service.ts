import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IImageGeneratedImage } from '../image-generated-image.model';
import { ImageGeneratedImageService } from '../service/image-generated-image.service';

const imageGeneratedImageResolve = (route: ActivatedRouteSnapshot): Observable<null | IImageGeneratedImage> => {
  const id = route.params.id;
  if (id) {
    return inject(ImageGeneratedImageService)
      .find(id)
      .pipe(
        mergeMap((imageGeneratedImage: HttpResponse<IImageGeneratedImage>) => {
          if (imageGeneratedImage.body) {
            return of(imageGeneratedImage.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default imageGeneratedImageResolve;
