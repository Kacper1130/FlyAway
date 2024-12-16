import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AvailableFlightComponent } from './available-flight.component';

describe('AvailableFlightComponent', () => {
  let component: AvailableFlightComponent;
  let fixture: ComponentFixture<AvailableFlightComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AvailableFlightComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AvailableFlightComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
