import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMetaTag } from '../meta-tag.model';
import { MetaTagService } from '../service/meta-tag.service';

const metaTagResolve = (route: ActivatedRouteSnapshot): Observable<null | IMetaTag> => {
  const id = route.params.id;
  if (id) {
    return inject(MetaTagService)
      .find(id)
      .pipe(
        mergeMap((metaTag: HttpResponse<IMetaTag>) => {
          if (metaTag.body) {
            return of(metaTag.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default metaTagResolve;
