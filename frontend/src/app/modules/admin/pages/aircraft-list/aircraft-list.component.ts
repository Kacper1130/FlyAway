import {Component, OnInit} from '@angular/core';
import {AdminNavbarComponent} from "../../components/admin-navbar/admin-navbar.component";
import {AircraftService} from "../../../../services/services/aircraft.service";
import {Aircraft} from "../../../../services/models/aircraft";
import {NgForOf, NgIf} from "@angular/common";
import {FlightComponent} from "../../../flights/components/available-flight/flight.component";
import {SingleAircraftComponent} from "./single-aircraft/single-aircraft.component";

@Component({
  selector: 'app-aircraft',
  standalone: true,
  imports: [
    AdminNavbarComponent,
    NgForOf,
    NgIf,
    FlightComponent,
    SingleAircraftComponent
  ],
  templateUrl: './aircraft-list.component.html',
  styleUrl: './aircraft-list.component.scss'
})
export class AircraftListComponent implements OnInit {

  aircraftList: Aircraft[] = [];

  constructor(
    private aircraftService: AircraftService
  ) {
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

}
