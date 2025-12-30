import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IEmailImportEmailAttachment } from '../email-import-email-attachment.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../email-import-email-attachment.test-samples';

import { EmailImportEmailAttachmentService } from './email-import-email-attachment.service';

const requireRestSample: IEmailImportEmailAttachment = {
  ...sampleWithRequiredData,
};

describe('EmailImportEmailAttachment Service', () => {
  let service: EmailImportEmailAttachmentService;
  let httpMock: HttpTestingController;
  let expectedResult: IEmailImportEmailAttachment | IEmailImportEmailAttachment[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(EmailImportEmailAttachmentService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a EmailImportEmailAttachment', () => {
      const emailImportEmailAttachment = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(emailImportEmailAttachment).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a EmailImportEmailAttachment', () => {
      const emailImportEmailAttachment = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(emailImportEmailAttachment).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a EmailImportEmailAttachment', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of EmailImportEmailAttachment', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a EmailImportEmailAttachment', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a EmailImportEmailAttachment', () => {
      const queryObject: any = {
        page: 0,
        size: 20,
        query: '',
        sort: [],
      };
      service.search(queryObject).subscribe(() => expectedResult);

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(null, { status: 500, statusText: 'Internal Server Error' });
      expect(expectedResult).toBe(null);
    });

    describe('addEmailImportEmailAttachmentToCollectionIfMissing', () => {
      it('should add a EmailImportEmailAttachment to an empty array', () => {
        const emailImportEmailAttachment: IEmailImportEmailAttachment = sampleWithRequiredData;
        expectedResult = service.addEmailImportEmailAttachmentToCollectionIfMissing([], emailImportEmailAttachment);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(emailImportEmailAttachment);
      });

      it('should not add a EmailImportEmailAttachment to an array that contains it', () => {
        const emailImportEmailAttachment: IEmailImportEmailAttachment = sampleWithRequiredData;
        const emailImportEmailAttachmentCollection: IEmailImportEmailAttachment[] = [
          {
            ...emailImportEmailAttachment,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEmailImportEmailAttachmentToCollectionIfMissing(
          emailImportEmailAttachmentCollection,
          emailImportEmailAttachment,
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a EmailImportEmailAttachment to an array that doesn't contain it", () => {
        const emailImportEmailAttachment: IEmailImportEmailAttachment = sampleWithRequiredData;
        const emailImportEmailAttachmentCollection: IEmailImportEmailAttachment[] = [sampleWithPartialData];
        expectedResult = service.addEmailImportEmailAttachmentToCollectionIfMissing(
          emailImportEmailAttachmentCollection,
          emailImportEmailAttachment,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(emailImportEmailAttachment);
      });

      it('should add only unique EmailImportEmailAttachment to an array', () => {
        const emailImportEmailAttachmentArray: IEmailImportEmailAttachment[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const emailImportEmailAttachmentCollection: IEmailImportEmailAttachment[] = [sampleWithRequiredData];
        expectedResult = service.addEmailImportEmailAttachmentToCollectionIfMissing(
          emailImportEmailAttachmentCollection,
          ...emailImportEmailAttachmentArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const emailImportEmailAttachment: IEmailImportEmailAttachment = sampleWithRequiredData;
        const emailImportEmailAttachment2: IEmailImportEmailAttachment = sampleWithPartialData;
        expectedResult = service.addEmailImportEmailAttachmentToCollectionIfMissing(
          [],
          emailImportEmailAttachment,
          emailImportEmailAttachment2,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(emailImportEmailAttachment);
        expect(expectedResult).toContain(emailImportEmailAttachment2);
      });

      it('should accept null and undefined values', () => {
        const emailImportEmailAttachment: IEmailImportEmailAttachment = sampleWithRequiredData;
        expectedResult = service.addEmailImportEmailAttachmentToCollectionIfMissing([], null, emailImportEmailAttachment, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(emailImportEmailAttachment);
      });

      it('should return initial array if no EmailImportEmailAttachment is added', () => {
        const emailImportEmailAttachmentCollection: IEmailImportEmailAttachment[] = [sampleWithRequiredData];
        expectedResult = service.addEmailImportEmailAttachmentToCollectionIfMissing(emailImportEmailAttachmentCollection, undefined, null);
        expect(expectedResult).toEqual(emailImportEmailAttachmentCollection);
      });
    });

    describe('compareEmailImportEmailAttachment', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEmailImportEmailAttachment(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 32731 };
        const entity2 = null;

        const compareResult1 = service.compareEmailImportEmailAttachment(entity1, entity2);
        const compareResult2 = service.compareEmailImportEmailAttachment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 32731 };
        const entity2 = { id: 8308 };

        const compareResult1 = service.compareEmailImportEmailAttachment(entity1, entity2);
        const compareResult2 = service.compareEmailImportEmailAttachment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 32731 };
        const entity2 = { id: 32731 };

        const compareResult1 = service.compareEmailImportEmailAttachment(entity1, entity2);
        const compareResult2 = service.compareEmailImportEmailAttachment(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
