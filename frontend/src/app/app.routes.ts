import { Routes } from '@angular/router';
import {LoginComponent} from "./pages/login/login.component";
import {RegisterComponent} from "./pages/register/register.component";
import {HomeComponent} from "./pages/home/home/home.component";
import {authGuard} from "./services/guard/auth.guard";
import {FlightsComponent} from "./modules/flights/pages/available-flights/flights.component";
import {ReservationsComponent} from "./modules/reservations/reservations/reservations.component";
import {ActivateAccountComponent} from "./pages/activate-account/activate-account.component";
import {ClientDetailsComponent} from "./pages/client-details/client-details.component";
import {AdminPanelComponent} from "./modules/admin/pages/admin-panel/admin-panel.component";
import {AdminEmployeesComponent} from "./modules/admin/pages/admin-employees/admin-employees.component";
import {AddEmployeeComponent} from "./modules/admin/pages/add-employee/add-employee.component";

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
    path: 'profile',
    component: ClientDetailsComponent,
    canActivate: [authGuard]
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
  },
  {
    path: 'admin',
    canActivate: [authGuard],
    data: {
      role: 'ROLE_ADMIN'
    },
    children: [
      {
        path: '',
        component: AdminPanelComponent
      },
      {
        path: 'employees',
        children: [
          {
            path: '',
            component: AdminEmployeesComponent
          },
          {
            path: 'add',
            component: AddEmployeeComponent
          }
        ]
      }
    ]
  },
];
