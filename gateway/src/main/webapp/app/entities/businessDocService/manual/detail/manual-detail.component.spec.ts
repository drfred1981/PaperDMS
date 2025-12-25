import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ManualDetailComponent } from './manual-detail.component';

describe('Manual Management Detail Component', () => {
  let comp: ManualDetailComponent;
  let fixture: ComponentFixture<ManualDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ManualDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./manual-detail.component').then(m => m.ManualDetailComponent),
              resolve: { manual: () => of({ id: 16259 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ManualDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ManualDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load manual on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ManualDetailComponent);

      // THEN
      expect(instance.manual()).toEqual(expect.objectContaining({ id: 16259 }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
