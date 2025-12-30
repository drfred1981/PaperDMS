import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMetaMetaTagCategory } from '../meta-meta-tag-category.model';
import { MetaMetaTagCategoryService } from '../service/meta-meta-tag-category.service';

const metaMetaTagCategoryResolve = (route: ActivatedRouteSnapshot): Observable<null | IMetaMetaTagCategory> => {
  const id = route.params.id;
  if (id) {
    return inject(MetaMetaTagCategoryService)
      .find(id)
      .pipe(
        mergeMap((metaMetaTagCategory: HttpResponse<IMetaMetaTagCategory>) => {
          if (metaMetaTagCategory.body) {
            return of(metaMetaTagCategory.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default metaMetaTagCategoryResolve;
