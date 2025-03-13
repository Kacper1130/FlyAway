import {Component, inject, OnInit} from '@angular/core';
import {AdminNavbarComponent} from "../../components/admin-navbar/admin-navbar.component";
import {AircraftService} from "../../../../services/services/aircraft.service";
import {Aircraft} from "../../../../services/models/aircraft";
import {NgForOf} from "@angular/common";
import {SingleAircraftComponent} from "./single-aircraft/single-aircraft.component";
import {MatIcon} from "@angular/material/icon";
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {MatButton, MatIconButton} from "@angular/material/button";
import {MatAccordion, MatExpansionModule} from '@angular/material/expansion';
import {FormArray, FormBuilder, FormGroup, ReactiveFormsModule} from "@angular/forms";
import {MatOption, MatSelect} from "@angular/material/select";
import {MatSnackBar, MatSnackBarHorizontalPosition, MatSnackBarVerticalPosition} from "@angular/material/snack-bar";

@Component({
  selector: 'app-aircraft',
  standalone: true,
  imports: [
    AdminNavbarComponent,
    NgForOf,
    SingleAircraftComponent,
    MatAccordion,
    MatExpansionModule,
    MatIcon,
    MatFormField,
    MatInput,
    MatButton,
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

  private readonly _snackBar = inject(MatSnackBar);
  horizontalPosition: MatSnackBarHorizontalPosition = 'center';
  verticalPosition: MatSnackBarVerticalPosition = 'top';
  errorMsg: Array<string> = [];

  aircraftList: Aircraft[] = [];
  aircraftForm: FormGroup;
  classes = ['FIRST', 'BUSINESS', 'ECONOMY'];

  constructor(private readonly fb: FormBuilder, private readonly aircraftService: AircraftService) {
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
    this.errorMsg = [];
    const formValues = this.aircraftForm.value;

    const newAircraft: Aircraft = {
      model: formValues.model,
      productionYear: formValues.productionYear,
      registration: formValues.registration,
      totalSeats: formValues.totalSeats,
      seatClassRanges: formValues.seatClassRanges.reduce((acc: any, seatClassRange: any) => {
        if(seatClassRange.class !== '') {
          acc[seatClassRange.class.toUpperCase()] = {
            startSeat: seatClassRange.startSeat,
            endSeat: seatClassRange.endSeat
          };
          return acc;
        }
      }, {})
    };

    this.aircraftService.createAircraft({body: newAircraft}).subscribe({
      next: () => {
        const message = `Successfully added ${newAircraft.model}(${newAircraft.registration})`
        this._snackBar.open(message, 'close', {
          horizontalPosition: this.horizontalPosition,
          verticalPosition: this.verticalPosition,
          panelClass: ['snackbar-success'],
          duration: 9000
        });
        this.aircraftForm.reset();
        this.ngOnInit();
      },
      error: (err) => {
        if (err.error.errors) {
          const errorMessages = Object.values(err.error.errors).join('\n');
          this.errorMsg.push(errorMessages);
          this._snackBar.open(this.errorMsg.toString(), 'close', {
            horizontalPosition: this.horizontalPosition,
            verticalPosition: this.verticalPosition,
            panelClass: ['snackbar-error'],
            duration: 5000
          });
        } else if (err.error.message) {
          this._snackBar.open(err.error.message.toString(), 'close', {
            horizontalPosition: this.horizontalPosition,
            verticalPosition: this.verticalPosition,
            panelClass: ['snackbar-error'],
            duration: 5000
          });
        }
      }
    });
  }
}
