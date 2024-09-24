import { Component } from '@angular/core';
import {AdminNavbarComponent} from "../../components/admin-navbar/admin-navbar.component";
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-admin-employees',
  standalone: true,
  imports: [
    AdminNavbarComponent,
    RouterLink
  ],
  templateUrl: './admin-employees.component.html',
  styleUrl: './admin-employees.component.scss'
})
export class AdminEmployeesComponent {

}
