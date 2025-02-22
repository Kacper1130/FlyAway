import {Component, OnInit} from '@angular/core';
import {EmployeeNavbarComponent} from "../../components/employee-navbar/employee-navbar.component";
import {EmployeeFlightCardComponent} from "./employee-flight-card/employee-flight-card.component";
import {FlightService} from "../../../../services/services/flight.service";
import {RouterLink} from "@angular/router";
import {NgForOf} from "@angular/common";
import {MatButton} from "@angular/material/button";
import {MatIcon} from "@angular/material/icon";
import {FlightSummaryEmployeeDto} from "../../../../services/models/flight-summary-employee-dto";

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
export class EmployeeFlightsComponent implements OnInit{
  flights: FlightSummaryEmployeeDto[] = [];

  constructor(
    private readonly flightService: FlightService,
  ) {
  }

  ngOnInit(): void {
    this.findAllFlights();
  }

  private findAllFlights() {
    this.flightService.getAllFullFlights().subscribe({
      next: (flights) => {
        this.flights = flights;
      }
    });
  }
}
