import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MakePromocodePageComponent } from './make-promocode-page.component';

describe('MakePromocodePageComponent', () => {
  let component: MakePromocodePageComponent;
  let fixture: ComponentFixture<MakePromocodePageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MakePromocodePageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MakePromocodePageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
