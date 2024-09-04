import { Component } from '@angular/core';
import {NavbarComponent} from "../../../components/navbar/navbar.component";
import {NewNavbarComponent} from "../../../components/new-navbar/new-navbar.component";

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    NavbarComponent,
    NewNavbarComponent
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {

}
