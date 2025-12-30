import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IOrcExtractedText, NewOrcExtractedText } from '../orc-extracted-text.model';

export type PartialUpdateOrcExtractedText = Partial<IOrcExtractedText> & Pick<IOrcExtractedText, 'id'>;

type RestOf<T extends IOrcExtractedText | NewOrcExtractedText> = Omit<T, 'extractedDate'> & {
  extractedDate?: string | null;
};

export type RestOrcExtractedText = RestOf<IOrcExtractedText>;

export type NewRestOrcExtractedText = RestOf<NewOrcExtractedText>;

export type PartialUpdateRestOrcExtractedText = RestOf<PartialUpdateOrcExtractedText>;

export type EntityResponseType = HttpResponse<IOrcExtractedText>;
export type EntityArrayResponseType = HttpResponse<IOrcExtractedText[]>;

@Injectable({ providedIn: 'root' })
export class OrcExtractedTextService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/orc-extracted-texts', 'ocrservice');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/orc-extracted-texts/_search', 'ocrservice');

  create(orcExtractedText: NewOrcExtractedText): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(orcExtractedText);
    return this.http
      .post<RestOrcExtractedText>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(orcExtractedText: IOrcExtractedText): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(orcExtractedText);
    return this.http
      .put<RestOrcExtractedText>(`${this.resourceUrl}/${this.getOrcExtractedTextIdentifier(orcExtractedText)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(orcExtractedText: PartialUpdateOrcExtractedText): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(orcExtractedText);
    return this.http
      .patch<RestOrcExtractedText>(`${this.resourceUrl}/${this.getOrcExtractedTextIdentifier(orcExtractedText)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestOrcExtractedText>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestOrcExtractedText[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestOrcExtractedText[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IOrcExtractedText[]>()], asapScheduler)),
    );
  }

  getOrcExtractedTextIdentifier(orcExtractedText: Pick<IOrcExtractedText, 'id'>): number {
    return orcExtractedText.id;
  }

  compareOrcExtractedText(o1: Pick<IOrcExtractedText, 'id'> | null, o2: Pick<IOrcExtractedText, 'id'> | null): boolean {
    return o1 && o2 ? this.getOrcExtractedTextIdentifier(o1) === this.getOrcExtractedTextIdentifier(o2) : o1 === o2;
  }

  addOrcExtractedTextToCollectionIfMissing<Type extends Pick<IOrcExtractedText, 'id'>>(
    orcExtractedTextCollection: Type[],
    ...orcExtractedTextsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const orcExtractedTexts: Type[] = orcExtractedTextsToCheck.filter(isPresent);
    if (orcExtractedTexts.length > 0) {
      const orcExtractedTextCollectionIdentifiers = orcExtractedTextCollection.map(orcExtractedTextItem =>
        this.getOrcExtractedTextIdentifier(orcExtractedTextItem),
      );
      const orcExtractedTextsToAdd = orcExtractedTexts.filter(orcExtractedTextItem => {
        const orcExtractedTextIdentifier = this.getOrcExtractedTextIdentifier(orcExtractedTextItem);
        if (orcExtractedTextCollectionIdentifiers.includes(orcExtractedTextIdentifier)) {
          return false;
        }
        orcExtractedTextCollectionIdentifiers.push(orcExtractedTextIdentifier);
        return true;
      });
      return [...orcExtractedTextsToAdd, ...orcExtractedTextCollection];
    }
    return orcExtractedTextCollection;
  }

  protected convertDateFromClient<T extends IOrcExtractedText | NewOrcExtractedText | PartialUpdateOrcExtractedText>(
    orcExtractedText: T,
  ): RestOf<T> {
    return {
      ...orcExtractedText,
      extractedDate: orcExtractedText.extractedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restOrcExtractedText: RestOrcExtractedText): IOrcExtractedText {
    return {
      ...restOrcExtractedText,
      extractedDate: restOrcExtractedText.extractedDate ? dayjs(restOrcExtractedText.extractedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestOrcExtractedText>): HttpResponse<IOrcExtractedText> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestOrcExtractedText[]>): HttpResponse<IOrcExtractedText[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
