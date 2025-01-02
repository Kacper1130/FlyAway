import { Component } from '@angular/core';
import {EmployeeNavbarComponent} from "../../components/employee-navbar/employee-navbar.component";
import {EmployeeFlightCardComponent} from "./employee-flight-card/employee-flight-card.component";
import {FlightDto} from "../../../../services/models/flight-dto";
import {FlightService} from "../../../../services/services/flight.service";
import {Router, RouterLink} from "@angular/router";
import {NgForOf} from "@angular/common";
import {Flight} from "../../../../services/models/flight";
import {FlightDetailsDto} from "../../../../services/models/flight-details-dto";
import {addAirport} from "../../../../services/fn/airport/add-airport";
import {MatButton} from "@angular/material/button";
import {AddAirportFormComponent} from "../../../admin/pages/airports/add-airport-form/add-airport-form.component";
import {MatDialog} from "@angular/material/dialog";
import {AddFlightFormComponent} from "./add-flight-form/add-flight-form.component";

@Component({
  selector: 'app-employee-flights',
  standalone: true,
  imports: [
    EmployeeNavbarComponent,
    EmployeeFlightCardComponent,
    NgForOf,
    MatButton,
    RouterLink
  ],
  templateUrl: './employee-flights.component.html',
  styleUrl: './employee-flights.component.scss'
})
export class EmployeeFlightsComponent {
  flights: FlightDetailsDto[] = [];

  constructor(
    private readonly flightService: FlightService,
    private readonly router: Router,
    private dialog: MatDialog
  ) {
  }

  ngOnInit(): void {
    this.findAllFlights();
  }

  private findAllFlights() {
    this.flightService.getAllFullFlights().subscribe({
      next: (flights) => {
        this.flights = flights;
        // flights.forEach(value => console.log(value.cabinClassPrices));
      }
    });
  }
}
