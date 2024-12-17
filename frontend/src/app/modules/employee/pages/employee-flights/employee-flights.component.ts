import { Component } from '@angular/core';
import {EmployeeNavbarComponent} from "../../components/employee-navbar/employee-navbar.component";
import {EmployeeFlightCardComponent} from "./employee-flight-card/employee-flight-card.component";
import {FlightDto} from "../../../../services/models/flight-dto";
import {FlightService} from "../../../../services/services/flight.service";
import {Router} from "@angular/router";
import {NgForOf} from "@angular/common";
import {Flight} from "../../../../services/models/flight";
import {FlightDetailsDto} from "../../../../services/models/flight-details-dto";

@Component({
  selector: 'app-employee-flights',
  standalone: true,
  imports: [
    EmployeeNavbarComponent,
    EmployeeFlightCardComponent,
    NgForOf
  ],
  templateUrl: './employee-flights.component.html',
  styleUrl: './employee-flights.component.scss'
})
export class EmployeeFlightsComponent {
  flights: FlightDetailsDto[] = [];

  constructor(
    private readonly flightService: FlightService,
    private readonly router: Router
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
