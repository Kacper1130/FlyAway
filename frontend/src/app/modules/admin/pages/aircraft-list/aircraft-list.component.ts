import {Component, OnInit, viewChild} from '@angular/core';
import {AdminNavbarComponent} from "../../components/admin-navbar/admin-navbar.component";
import {AircraftService} from "../../../../services/services/aircraft.service";
import {Aircraft} from "../../../../services/models/aircraft";
import {NgForOf, NgIf} from "@angular/common";
import {FlightComponent} from "../../../flights/components/available-flight/flight.component";
import {SingleAircraftComponent} from "./single-aircraft/single-aircraft.component";
import {MatIcon} from "@angular/material/icon";
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {MatButton, MatIconButton} from "@angular/material/button";
import {MatDatepicker} from "@angular/material/datepicker";
import {MatAccordion, MatExpansionModule} from '@angular/material/expansion';
import {FormArray, FormBuilder, FormGroup, ReactiveFormsModule} from "@angular/forms";
import {MatOption, MatSelect} from "@angular/material/select";

@Component({
  selector: 'app-aircraft',
  standalone: true,
  imports: [
    AdminNavbarComponent,
    NgForOf,
    NgIf,
    FlightComponent,
    SingleAircraftComponent,
    MatAccordion,
    MatExpansionModule,
    MatIcon,
    MatFormField,
    MatInput,
    MatButton,
    MatDatepicker,
    MatAccordion,
    MatLabel,
    ReactiveFormsModule,
    MatIconButton,
    MatSelect,
    MatOption
  ],
  templateUrl: './aircraft-list.component.html',
  styleUrl: './aircraft-list.component.scss'
})
export class AircraftListComponent implements OnInit {

  aircraftList: Aircraft[] = [];

  accordion = viewChild.required(MatAccordion);

  constructor(private fb: FormBuilder, private aircraftService: AircraftService) {
    this.aircraftForm = this.fb.group({
      model: [''],
      productionYear: [''],
      registration: [''],
      totalSeats: [''],
      seatClassRanges: this.fb.array([])
    });
  }

  ngOnInit(): void {
    this.findAllAircraft();
  }

  private findAllAircraft() {
    this.aircraftService.getAllAircraft().subscribe({
      next: (res) => {
        this.aircraftList = res;
      }
    })
  }

  aircraftForm: FormGroup;

  get seatClassRanges(): FormArray {
    return this.aircraftForm.get('seatClassRanges') as FormArray;
  }

  addSeatClass() {
    const seatClassGroup = this.fb.group({
      class: [''],
      startSeat: [''],
      endSeat: ['']
    });
    this.seatClassRanges.push(seatClassGroup);
  }

  removeSeatClass(index: number) {
    this.seatClassRanges.removeAt(index);
  }

  onSubmit() {
    // Pobranie danych z formularza
    const formValues = this.aircraftForm.value;

    // Tworzenie obiektu samolotu na podstawie wartości formularza
    const newAircraft: Aircraft = {
      model: formValues.model,
      productionYear: formValues.productionYear,
      registration: formValues.registration,
      totalSeats: formValues.totalSeats,
      seatClassRanges: formValues.seatClassRanges.reduce((acc: any, seatClassRange: any) => {
        acc[seatClassRange.class.toUpperCase()] = {
          startSeat: seatClassRange.startSeat,
          endSeat: seatClassRange.endSeat
        };
        return acc;
      }, {})
    };

    this.aircraftService.addAircraft({body: newAircraft}).subscribe({
      next: () => {
        this.aircraftForm.reset();
        this.ngOnInit();
      },
      error: () => {

      }
    });
  }
}
