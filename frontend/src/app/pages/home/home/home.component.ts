import { Component } from '@angular/core';
import {NewNavbarComponent} from "../../../components/new-navbar/new-navbar.component";

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    NewNavbarComponent,
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {

}
