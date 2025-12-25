import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IExtractedText, NewExtractedText } from '../extracted-text.model';

export type PartialUpdateExtractedText = Partial<IExtractedText> & Pick<IExtractedText, 'id'>;

type RestOf<T extends IExtractedText | NewExtractedText> = Omit<T, 'extractedDate'> & {
  extractedDate?: string | null;
};

export type RestExtractedText = RestOf<IExtractedText>;

export type NewRestExtractedText = RestOf<NewExtractedText>;

export type PartialUpdateRestExtractedText = RestOf<PartialUpdateExtractedText>;

export type EntityResponseType = HttpResponse<IExtractedText>;
export type EntityArrayResponseType = HttpResponse<IExtractedText[]>;

@Injectable({ providedIn: 'root' })
export class ExtractedTextService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/extracted-texts', 'ocrservice');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/extracted-texts/_search', 'ocrservice');

  create(extractedText: NewExtractedText): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(extractedText);
    return this.http
      .post<RestExtractedText>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(extractedText: IExtractedText): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(extractedText);
    return this.http
      .put<RestExtractedText>(`${this.resourceUrl}/${this.getExtractedTextIdentifier(extractedText)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(extractedText: PartialUpdateExtractedText): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(extractedText);
    return this.http
      .patch<RestExtractedText>(`${this.resourceUrl}/${this.getExtractedTextIdentifier(extractedText)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestExtractedText>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestExtractedText[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestExtractedText[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IExtractedText[]>()], asapScheduler)),
    );
  }

  getExtractedTextIdentifier(extractedText: Pick<IExtractedText, 'id'>): number {
    return extractedText.id;
  }

  compareExtractedText(o1: Pick<IExtractedText, 'id'> | null, o2: Pick<IExtractedText, 'id'> | null): boolean {
    return o1 && o2 ? this.getExtractedTextIdentifier(o1) === this.getExtractedTextIdentifier(o2) : o1 === o2;
  }

  addExtractedTextToCollectionIfMissing<Type extends Pick<IExtractedText, 'id'>>(
    extractedTextCollection: Type[],
    ...extractedTextsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const extractedTexts: Type[] = extractedTextsToCheck.filter(isPresent);
    if (extractedTexts.length > 0) {
      const extractedTextCollectionIdentifiers = extractedTextCollection.map(extractedTextItem =>
        this.getExtractedTextIdentifier(extractedTextItem),
      );
      const extractedTextsToAdd = extractedTexts.filter(extractedTextItem => {
        const extractedTextIdentifier = this.getExtractedTextIdentifier(extractedTextItem);
        if (extractedTextCollectionIdentifiers.includes(extractedTextIdentifier)) {
          return false;
        }
        extractedTextCollectionIdentifiers.push(extractedTextIdentifier);
        return true;
      });
      return [...extractedTextsToAdd, ...extractedTextCollection];
    }
    return extractedTextCollection;
  }

  protected convertDateFromClient<T extends IExtractedText | NewExtractedText | PartialUpdateExtractedText>(extractedText: T): RestOf<T> {
    return {
      ...extractedText,
      extractedDate: extractedText.extractedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restExtractedText: RestExtractedText): IExtractedText {
    return {
      ...restExtractedText,
      extractedDate: restExtractedText.extractedDate ? dayjs(restExtractedText.extractedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestExtractedText>): HttpResponse<IExtractedText> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestExtractedText[]>): HttpResponse<IExtractedText[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
