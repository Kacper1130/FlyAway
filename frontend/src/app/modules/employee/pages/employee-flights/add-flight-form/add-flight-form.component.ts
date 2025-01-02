import {Component, OnInit} from '@angular/core';
import {MatDialogActions, MatDialogContent, MatDialogRef, MatDialogTitle} from "@angular/material/dialog";
import {AsyncPipe, NgIf} from "@angular/common";
import {MatAutocomplete, MatAutocompleteTrigger, MatOption} from "@angular/material/autocomplete";
import {MatButton} from "@angular/material/button";
import {MatError, MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {MatSlideToggle} from "@angular/material/slide-toggle";
import {PaginatorModule} from "primeng/paginator";
import {AbstractControl, FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {CountryService} from "../../../../../services/services/country.service";
import {AirportService} from "../../../../../services/services/airport.service";
import {MatDatepicker, MatDatepickerInput, MatDatepickerToggle} from "@angular/material/datepicker";
import {Observable} from "rxjs";
import {MatIcon} from "@angular/material/icon";
import {FlightService} from "../../../../../services/services/flight.service";
import {EmployeeNavbarComponent} from "../../../components/employee-navbar/employee-navbar.component";

@Component({
  selector: 'app-add-flight-form',
  standalone: true,
  imports: [
    MatDialogContent,
    MatDialogTitle,
    AsyncPipe,
    MatAutocomplete,
    MatAutocompleteTrigger,
    MatButton,
    MatDialogActions,
    MatError,
    MatFormField,
    MatInput,
    MatLabel,
    MatOption,
    MatSlideToggle,
    NgIf,
    PaginatorModule,
    ReactiveFormsModule,
    MatDatepickerInput,
    MatDatepickerToggle,
    MatDatepicker,
    MatIcon,
    EmployeeNavbarComponent
  ],
  templateUrl: './add-flight-form.component.html',
  styleUrl: './add-flight-form.component.scss'
})
export class AddFlightFormComponent implements OnInit{

  flightForm!: FormGroup;
  departureAirports!: Observable<any[]>;
  arrivalAirports!: Observable<any[]>;
  aircraftList!: Observable<any[]>;
  isSubmitting = false;
  minDate = new Date();

  constructor(
    private fb: FormBuilder,
    public dialogRef: MatDialogRef<AddFlightFormComponent>,
    private countryService: CountryService,
    private airportService: AirportService,
    private flightService: FlightService
  ) {
    this.initializeForm();
  }

  ngOnInit() {
    this.loadData();
    this.setupFormValidation();
  }

  private initializeForm() {
    this.flightForm = this.fb.group({
      departureAirport: ['', Validators.required],
      departureDate: ['', Validators.required],
      arrivalAirport: ['', Validators.required],
      arrivalDate: ['', Validators.required],
      aircraft: ['', Validators.required],
      cabinClassPrices: this.fb.group({
        economy: ['', [Validators.required, Validators.min(0)]],
        business: ['', [Validators.required, Validators.min(0)]],
        firstClass: ['', [Validators.required, Validators.min(0)]]
      })
    });
  }

  private loadData() {
    this.departureAirports = this.airportService.getAllEnabledAirports();
    this.arrivalAirports = this.airportService.getAllEnabledAirports();
    // Assuming you have an aircraft service
    // this.aircrafts = this.aircraftService.getAircrafts();
  }

  private setupFormValidation() {
    const departureControl = this.flightForm.get('departureDate') as AbstractControl;
    const arrivalControl = this.flightForm.get('arrivalDate') as AbstractControl;

    if (departureControl && arrivalControl) {
      departureControl.valueChanges.subscribe({
        next: (value) => {
          this.validateDates();
        }
      });

      arrivalControl.valueChanges.subscribe({
        next: (value) => {
          this.validateDates();
        }
      });
    }
  }


  private validateDates() {
    const departureDate = this.flightForm.get('departureDate')?.value;
    const arrivalDate = this.flightForm.get('arrivalDate')?.value;

    if (departureDate && arrivalDate && new Date(departureDate) > new Date(arrivalDate)) {
      this.flightForm.get('arrivalDate')?.setErrors({ invalidDate: true });
    } else {
      const currentErrors = this.flightForm.get('arrivalDate')?.errors || {};
      delete currentErrors['invalidDate'];
      const newErrors = Object.keys(currentErrors).length ? currentErrors : null;
      this.flightForm.get('arrivalDate')?.setErrors(newErrors);
    }
  }

  validateSameAirport() {
    const departure = this.flightForm.get('departureAirport')?.value;
    const arrival = this.flightForm.get('arrivalAirport')?.value;

    if (departure && arrival && departure.IATACode === arrival.IATACode) {
      this.flightForm.get('arrivalAirport')?.setErrors({ sameAirport: true });
    }
  }

  async onSubmit() {
    if (this.flightForm.valid && !this.isSubmitting) {
      try {
        this.isSubmitting = true;

        const formValue = this.flightForm.value;
        const flightData = {
          departureAirportId: formValue.departureAirport.id,
          arrivalAirportId: formValue.arrivalAirport.id,
          departureDate: formValue.departureDate,
          arrivalDate: formValue.arrivalDate,
          aircraftId: formValue.aircraft.id,
          cabinClassPrices: formValue.cabinClassPrices
        };

        // Add loading state to button
        // await this.flightService.add(flightData);
        this.dialogRef.close(flightData);
      } catch (error) {
        console.error('Error creating flight:', error);
        // You might want to show an error message to the user here
      } finally {
        this.isSubmitting = false;
      }
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }

  // Helper methods for form validation messages
  getErrorMessage(controlName: string): string {
    const control = this.flightForm.get(controlName);
    if (control?.hasError('required')) {
      return 'This field is required';
    }
    if (control?.hasError('invalidDate')) {
      return 'Arrival date must be after departure date';
    }
    if (control?.hasError('sameAirport')) {
      return 'Departure and arrival airports cannot be the same';
    }
    return '';
  }

  // Format display values for autocomplete
  displayAirport(airport: any): string {
    return airport ? `${airport.IATACode} - ${airport.name}` : '';
  }

  displayAircraft(aircraft: any): string {
    return aircraft ? aircraft.name : '';
  }

}
