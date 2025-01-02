import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddFlightFormComponent } from './add-flight-form.component';

describe('AddFlightFormComponent', () => {
  let component: AddFlightFormComponent;
  let fixture: ComponentFixture<AddFlightFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddFlightFormComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddFlightFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
