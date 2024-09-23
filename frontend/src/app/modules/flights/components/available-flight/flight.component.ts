import {Component, Input} from '@angular/core';
import {FlightDto} from "../../../../services/models/flight-dto";
import {DatePipe} from "@angular/common";

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
  @Input() flight!: FlightDto;
}
