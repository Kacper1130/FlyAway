import {Component} from '@angular/core';
import {MatToolbar, MatToolbarModule} from "@angular/material/toolbar";
import {MatIcon} from "@angular/material/icon";
import {MatAnchor, MatButton, MatIconButton} from "@angular/material/button";
import {RouterLink, RouterLinkActive} from "@angular/router";
import {FormsModule} from "@angular/forms";
import {NgIf} from "@angular/common";
import {LocalStorageService} from "../../services/token/local-storage.service";
import {MatTooltip} from "@angular/material/tooltip";


@Component({
  selector: 'app-new-navbar',
  standalone: true,
  imports: [
    MatToolbar,
    MatIcon,
    MatToolbarModule,
    MatIconButton,
    RouterLink,
    MatButton,
    FormsModule,
    NgIf,
    MatAnchor,
    RouterLinkActive,
    MatTooltip
  ],
  templateUrl: './new-navbar.component.html',
  styleUrl: './new-navbar.component.scss'
})
export class NewNavbarComponent {

  constructor(
    private readonly localStorageService: LocalStorageService
  ) {
  }

  get Firstname(): string {
    return this.localStorageService.firstname;
  }

  logout() {
    localStorage.clear();
    window.location.reload();
  }

  protected readonly localStorage = localStorage;
}
