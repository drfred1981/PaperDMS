import { HttpResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { TagCategoryService } from '../service/tag-category.service';
import { ITagCategory } from '../tag-category.model';

const tagCategoryResolve = (route: ActivatedRouteSnapshot): Observable<null | ITagCategory> => {
  const id = route.params.id;
  if (id) {
    return inject(TagCategoryService)
      .find(id)
      .pipe(
        mergeMap((tagCategory: HttpResponse<ITagCategory>) => {
          if (tagCategory.body) {
            return of(tagCategory.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default tagCategoryResolve;
