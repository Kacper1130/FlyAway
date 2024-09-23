import { Component } from '@angular/core';
import {NewNavbarComponent} from "../../../../components/new-navbar/new-navbar.component";
import {AdminNavbarComponent} from "../../components/admin-navbar/admin-navbar.component";

@Component({
  selector: 'app-admin-panel',
  standalone: true,
  imports: [
    NewNavbarComponent,
    AdminNavbarComponent
  ],
  templateUrl: './admin-panel.component.html',
  styleUrl: './admin-panel.component.scss'
})
export class AdminPanelComponent {

}
