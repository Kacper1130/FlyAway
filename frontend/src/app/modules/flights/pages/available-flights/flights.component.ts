import {Component, OnInit} from '@angular/core';
import {NavbarComponent} from "../../../../components/navbar/navbar.component";
import {NewNavbarComponent} from "../../../../components/new-navbar/new-navbar.component";
import {FlightService} from "../../../../services/services/flight.service";
import {Router} from "@angular/router";
import {FlightDto} from "../../../../services/models/flight-dto";
import {NgForOf} from "@angular/common";
import {FlightComponent} from "../../components/available-flight/flight.component";
import {Flight} from "../../../../services/models/flight";

@Component({
  selector: 'app-flights',
  standalone: true,
  imports: [
    NavbarComponent,
    NewNavbarComponent,
    NgForOf,
    FlightComponent
  ],
  templateUrl: './flights.component.html',
  styleUrl: './flights.component.scss'
})
export class FlightsComponent implements OnInit{
  flights: Flight[] = [];

  constructor(
    private flightService: FlightService,
    private router: Router
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
