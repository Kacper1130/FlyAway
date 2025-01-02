import {Component, inject, OnInit} from '@angular/core';
import {AsyncPipe, JsonPipe, NgForOf, NgIf} from "@angular/common";
import {EmployeeNavbarComponent} from "../../components/employee-navbar/employee-navbar.component";
import {MatAutocomplete, MatAutocompleteTrigger, MatOption} from "@angular/material/autocomplete";
import {MatButton} from "@angular/material/button";
import {
  MatDatepicker,
  MatDatepickerActions,
  MatDatepickerApply,
  MatDatepickerCancel,
  MatDatepickerInput,
  MatDatepickerModule,
  MatDatepickerToggle,
  MatDateRangeInput,
  MatDateRangePicker
} from "@angular/material/datepicker";
import {MatDialogActions, MatDialogContent, MatDialogTitle} from "@angular/material/dialog";
import {MatError, MatFormField, MatFormFieldModule, MatHint, MatLabel} from "@angular/material/form-field";
import {MatIcon} from "@angular/material/icon";
import {MatInput} from "@angular/material/input";
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {Observable, startWith} from "rxjs";
import {CountryService} from "../../../../services/services/country.service";
import {AirportService} from "../../../../services/services/airport.service";
import {FlightService} from "../../../../services/services/flight.service";
import {AircraftService} from "../../../../services/services/aircraft.service";
import {Aircraft} from "../../../../services/models/aircraft";
import {AirportDto} from "../../../../services/models/airport-dto";
import {map} from "rxjs/operators";
import {FlightDto} from "../../../../services/models/flight-dto";
import {MatSnackBar, MatSnackBarHorizontalPosition, MatSnackBarVerticalPosition} from "@angular/material/snack-bar";

@Component({
  selector: 'app-employee-flights-add',
  standalone: true,
  imports: [
    AsyncPipe,
    EmployeeNavbarComponent,
    MatAutocomplete,
    MatAutocompleteTrigger,
    MatButton,
    MatDatepicker,
    MatDatepickerInput,
    MatDatepickerToggle,
    MatDialogActions,
    MatDialogContent,
    MatDialogTitle,
    MatError,
    MatFormField,
    MatIcon,
    MatInput,
    MatLabel,
    MatOption,
    NgIf,
    ReactiveFormsModule,
    NgForOf,
    MatDatepickerActions,
    MatDatepickerCancel,
    MatDatepickerApply,
    MatHint,
    MatDateRangeInput,
    MatDateRangePicker,
    MatFormFieldModule, MatDatepickerModule, FormsModule, ReactiveFormsModule, JsonPipe
  ],
  templateUrl: './employee-flights-add.component.html',
  styleUrl: './employee-flights-add.component.scss'
})
export class EmployeeFlightsAddComponent implements OnInit {

  private readonly _snackBar = inject(MatSnackBar);
  horizontalPosition: MatSnackBarHorizontalPosition = 'center';
  verticalPosition: MatSnackBarVerticalPosition = 'top';

  flightForm!: FormGroup;
  departureAirports: Observable<Array<AirportDto>> | undefined;
  arrivalAirports: Observable<Array<AirportDto>> | undefined;
  filteredAircraftList: Observable<Array<Aircraft>> | undefined;
  aircraftList: Aircraft[] = [];

  constructor(
    private readonly fb: FormBuilder,
    private readonly countryService: CountryService,
    private readonly airportService: AirportService,
    private readonly aircraftService: AircraftService,
    private readonly flightService: FlightService
  ) {
    this.initializeForm();
  }

  ngOnInit() {
    this.loadData();
    this.filteredAircraftList = this.flightForm.get('aircraft')!.valueChanges.pipe(
      startWith(''),
      map(value => this.filterAircraft(value || ''))
    );
    this.setupAircraftSelection();
  }

  private setupAircraftSelection() {
    this.flightForm.get('aircraft')!.valueChanges.subscribe(selectedAircraft => {
      if (selectedAircraft && typeof selectedAircraft !== 'string') {
        // Get seat class ranges
        const seatClassRanges = selectedAircraft.seatClassRanges || {};

        // Enable or disable fields based on available seat classes
        const cabinClassPricesGroup = this.flightForm.get('cabinClassPrices');

        if (cabinClassPricesGroup) {
          if (seatClassRanges['ECONOMY']) {
            cabinClassPricesGroup.get('ECONOMY')!.enable();
          } else {
            cabinClassPricesGroup.get('ECONOMY')!.disable();
          }

          if (seatClassRanges['BUSINESS']) {
            cabinClassPricesGroup.get('BUSINESS')!.enable();
          } else {
            cabinClassPricesGroup.get('BUSINESS')!.disable();
          }

          if (seatClassRanges['FIRST']) {
            cabinClassPricesGroup.get('FIRST')!.enable();
          } else {
            cabinClassPricesGroup.get('FIRST')!.disable();
          }
        }
      }
    });
  }

  private filterAircraft(value: string | Aircraft): Aircraft[] {
    const filterValue = typeof value === 'string' ? value.toLowerCase() : '';

    return this.aircraftList.filter(aircraft =>
      aircraft.model.toLowerCase().includes(filterValue) ||
      aircraft.registration.toLowerCase().includes(filterValue)
    );
  }

  private initializeForm() {
    this.flightForm = this.fb.group({
      departureAirport: ['', Validators.required],
      departureDate: ['', Validators.required],
      departureTime: ['', Validators.required],
      arrivalAirport: ['', Validators.required],
      arrivalDate: ['', Validators.required],
      arrivalTime: ['', Validators.required],
      aircraft: ['', Validators.required],
      cabinClassPrices: this.fb.group({
        ECONOMY: [{value: '', disabled: true}, [Validators.required, Validators.min(0)]],
        BUSINESS: [{value: '', disabled: true}, [Validators.required, Validators.min(0)]],
        FIRST: [{value: '', disabled: true}, [Validators.required, Validators.min(0)]]
      })
    });
  }


  private loadData() {
    this.departureAirports = this.airportService.getAllEnabledAirports();
    this.arrivalAirports = this.airportService.getAllEnabledAirports();

    this.aircraftService.getAllAircraft().subscribe(aircrafts => {
      this.aircraftList = aircrafts;

      this.filteredAircraftList = this.flightForm.get('aircraft')!.valueChanges.pipe(
        startWith(''),
        map(value => this.filterAircraft(value || ''))
      );
    });
  }

  onSubmit() {
    if (this.flightForm.valid) {
      // const newFlight: FlightDto = this.flightForm.value;

      const formValue = this.flightForm.value;
      const departureDateTime = this.combineDateAndTime(
        formValue.departureDate,
        formValue.departureTime
      );

      const arrivalDateTime = this.combineDateAndTime(
        formValue.arrivalDate,
        formValue.arrivalTime
      );

      const newFlight: FlightDto = {
        aircraft: formValue.aircraft,
        arrivalAirportDto: formValue.arrivalAirport,
        arrivalDate: arrivalDateTime,
        cabinClassPrices: formValue.cabinClassPrices,
        departureAirportDto: formValue.departureAirport,
        departureDate: departureDateTime
      };
      this.flightService.add({body: newFlight}).subscribe({
        next: () => {
          const message = `Successfully created flight from ${newFlight.departureAirportDto.city}(${newFlight.departureAirportDto.country.name})
          to ${newFlight.arrivalAirportDto.city}(${newFlight.arrivalAirportDto.country.name})`;
          this._snackBar.open(message, 'close', {
            horizontalPosition: this.horizontalPosition,
            verticalPosition: this.verticalPosition,
            panelClass: ['snackbar-success'],
            duration: 9000
          });
        }
      })
    }
  }

  combineDateAndTime(date: string, time: string): string {
    console.log(date)
    console.log(time)
    const [hours, minutes] = time.split(':').map(Number);
    const result = new Date(date);
    result.setHours(hours, minutes, 0, 0);
    // return result.toISOString()

    const year = result.getFullYear();
    const month = String(result.getMonth() + 1).padStart(2, '0'); // Months are zero-based
    const day = String(result.getDate()).padStart(2, '0');
    const hour = String(result.getHours()).padStart(2, '0');
    const minute = String(result.getMinutes()).padStart(2, '0');
    const second = String(result.getSeconds()).padStart(2, '0');
    return `${year}-${month}-${day}T${hour}:${minute}:${second}`;
  }


  displayAirport(airport: any): string {
    return airport ? `${airport.name} (${airport.city}, ${airport.country.name})` : '';
  }

  displayAircraft(aircraft: any): string {
    return aircraft
      ? `${aircraft.model} (${aircraft.registration}) - Capacity: ${aircraft.totalSeats}` : '';
  }

}

