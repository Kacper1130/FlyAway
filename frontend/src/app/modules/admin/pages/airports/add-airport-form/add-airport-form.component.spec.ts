import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddAirportFormComponent } from './add-airport-form.component';

describe('AddAirportFormComponent', () => {
  let component: AddAirportFormComponent;
  let fixture: ComponentFixture<AddAirportFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddAirportFormComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddAirportFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
