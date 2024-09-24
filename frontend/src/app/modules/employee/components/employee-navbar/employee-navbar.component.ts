import { Component } from '@angular/core';
import {FormsModule} from "@angular/forms";
import {MatButton, MatIconButton} from "@angular/material/button";
import {MatIcon} from "@angular/material/icon";
import {MatToolbar} from "@angular/material/toolbar";
import {PaginatorModule} from "primeng/paginator";
import {RouterLink, RouterLinkActive} from "@angular/router";
import {TokenService} from "../../../../services/token/token.service";

@Component({
  selector: 'app-employee-navbar',
  standalone: true,
    imports: [
        FormsModule,
        MatButton,
        MatIcon,
        MatIconButton,
        MatToolbar,
        PaginatorModule,
        RouterLink,
        RouterLinkActive
    ],
  templateUrl: './employee-navbar.component.html',
  styleUrl: './employee-navbar.component.scss'
})
export class EmployeeNavbarComponent {

  constructor(
    private tokenService: TokenService
  ) {
  }

  get Email(): string {
    return this.tokenService.getEmail();
  }

  logout() {
    localStorage.removeItem('token');
    window.location.reload();
  }

}
