import {Component} from '@angular/core';
import {FormsModule} from "@angular/forms";
import {MatAnchor, MatIconButton} from "@angular/material/button";
import {MatIcon} from "@angular/material/icon";
import {MatToolbar} from "@angular/material/toolbar";
import {PaginatorModule} from "primeng/paginator";
import {RouterLink, RouterLinkActive} from "@angular/router";
import {LocalStorageService} from "../../../../services/token/local-storage.service";

@Component({
  selector: 'app-employee-navbar',
  standalone: true,
  imports: [
    FormsModule,
    MatIcon,
    MatIconButton,
    MatToolbar,
    PaginatorModule,
    RouterLink,
    RouterLinkActive,
    MatAnchor
  ],
  templateUrl: './employee-navbar.component.html',
  styleUrl: './employee-navbar.component.scss'
})
export class EmployeeNavbarComponent {

  constructor(
    private readonly localStorageService: LocalStorageService,
  ) {
  }

  get Email(): string {
    return this.localStorageService.email;
  }

  logout() {
    localStorage.clear();
    window.location.reload();
  }

}
