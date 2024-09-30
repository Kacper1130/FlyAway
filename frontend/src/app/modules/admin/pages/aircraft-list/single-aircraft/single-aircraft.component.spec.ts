import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SingleAircraftComponent } from './single-aircraft.component';

describe('SingleAircraftComponent', () => {
  let component: SingleAircraftComponent;
  let fixture: ComponentFixture<SingleAircraftComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SingleAircraftComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SingleAircraftComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
