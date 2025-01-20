import {Component} from '@angular/core';
import {EmployeeNavbarComponent} from "../../components/employee-navbar/employee-navbar.component";
import {EmployeeFlightCardComponent} from "./employee-flight-card/employee-flight-card.component";
import {FlightService} from "../../../../services/services/flight.service";
import {Router, RouterLink} from "@angular/router";
import {NgForOf} from "@angular/common";
import {FlightDetailsDto} from "../../../../services/models/flight-details-dto";
import {MatButton} from "@angular/material/button";
import {MatDialog} from "@angular/material/dialog";
import {MatIcon} from "@angular/material/icon";

@Component({
  selector: 'app-employee-flights',
  standalone: true,
  imports: [
    EmployeeNavbarComponent,
    EmployeeFlightCardComponent,
    NgForOf,
    MatButton,
    RouterLink,
    MatIcon
  ],
  templateUrl: './employee-flights.component.html',
  styleUrl: './employee-flights.component.scss'
})
export class EmployeeFlightsComponent {
  flights: FlightDetailsDto[] = [];

  constructor(
    private readonly flightService: FlightService,
    private readonly router: Router,
    private readonly dialog: MatDialog
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
