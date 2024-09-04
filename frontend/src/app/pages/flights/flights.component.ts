import { Component } from '@angular/core';
import {NavbarComponent} from "../../components/navbar/navbar.component";
import {NewNavbarComponent} from "../../components/new-navbar/new-navbar.component";

@Component({
  selector: 'app-flights',
  standalone: true,
    imports: [
        NavbarComponent,
        NewNavbarComponent
    ],
  templateUrl: './flights.component.html',
  styleUrl: './flights.component.scss'
})
export class FlightsComponent {

}
