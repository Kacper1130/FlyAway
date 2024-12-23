import {Component, OnInit} from '@angular/core';
import {NavbarComponent} from "../../../../components/navbar/navbar.component";
import {NewNavbarComponent} from "../../../../components/new-navbar/new-navbar.component";
import {FlightService} from "../../../../services/services/flight.service";
import {Router} from "@angular/router";
import {FlightDto} from "../../../../services/models/flight-dto";
import {NgForOf} from "@angular/common";
import {FlightComponent} from "../../components/available-flight/flight.component";
import {AvailableFlightComponent} from "../../components/available-flight/available-flight.component";

@Component({
  selector: 'app-flights',
  standalone: true,
  imports: [
    NavbarComponent,
    NewNavbarComponent,
    NgForOf,
    FlightComponent,
    AvailableFlightComponent
  ],
  templateUrl: './flights.component.html',
  styleUrl: './flights.component.scss'
})
export class FlightsComponent implements OnInit{
  flights: FlightDto[] = [];

  constructor(
    private readonly flightService: FlightService,
    private readonly router: Router
  ) {
  }

  ngOnInit(): void {
    this.findAllFlights();
  }

  private findAllFlights() {
    this.flightService.getAll().subscribe({
      next: (flights) => {
       this.flights = flights;
      }
    });
  }
}
