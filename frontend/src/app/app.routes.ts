import { Routes } from '@angular/router';
import {LoginComponent} from "./pages/login/login.component";
import {RegisterComponent} from "./pages/register/register.component";
import {HomeComponent} from "./pages/home/home/home.component";
import {authGuard} from "./services/guard/auth.guard";
import {FlightsComponent} from "./pages/flights/flights.component";
import {ReservationsComponent} from "./pages/reservations/reservations.component";
import {ActivateAccountComponent} from "./pages/activate-account/activate-account.component";

export const routes: Routes = [
  {
    path: '',
    component: HomeComponent
  },
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'register',
    component: RegisterComponent
  },
  {
    path: 'activate-account',
    component: ActivateAccountComponent
  },
  {
    path: 'flights',
    component: FlightsComponent,
    canActivate: [authGuard]
  },
  {
    path: 'reservations',
    component: ReservationsComponent,
    canActivate: [authGuard]
  }
];
