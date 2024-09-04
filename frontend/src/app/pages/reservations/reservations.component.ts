import { Component } from '@angular/core';
import {NavbarComponent} from "../../components/navbar/navbar.component";
import {NewNavbarComponent} from "../../components/new-navbar/new-navbar.component";

@Component({
  selector: 'app-reservations',
  standalone: true,
    imports: [
        NavbarComponent,
        NewNavbarComponent
    ],
  templateUrl: './reservations.component.html',
  styleUrl: './reservations.component.scss'
})
export class ReservationsComponent {

}
