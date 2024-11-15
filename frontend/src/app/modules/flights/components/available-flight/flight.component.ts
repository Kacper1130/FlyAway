import {Component, Input} from '@angular/core';
import {DatePipe} from "@angular/common";
import {Flight} from "../../../../services/models/flight";

@Component({
  selector: 'app-flight',
  standalone: true,
  imports: [
    DatePipe
  ],
  templateUrl: './flight.component.html',
  styleUrl: './flight.component.scss'
})
export class FlightComponent {
  @Input() flight!: Flight;
}
