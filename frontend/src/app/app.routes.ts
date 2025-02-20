import {Routes} from '@angular/router';
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
import {EmployeePanelComponent} from "./modules/employee/pages/employee-panel/employee-panel.component";
import {CountriesComponent} from "./modules/admin/pages/countries/countries.component";
import {AircraftListComponent} from "./modules/admin/pages/aircraft-list/aircraft-list.component";
import {AirportsComponent} from "./modules/admin/pages/airports/airports.component";
import {
  EmployeeReservationsComponent
} from "./modules/employee/pages/employee-reservations/employee-reservations.component";
import {EmployeeIssuesComponent} from "./modules/employee/pages/employee-issues/employee-issues.component";
import {EmployeeFlightsComponent} from "./modules/employee/pages/employee-flights/employee-flights.component";
import {
  EmployeeFlightDetailsComponent
} from "./modules/employee/pages/employee-flights/employee-flight-details/employee-flight-details.component";
import {
  EmployeeFlightsAddComponent
} from "./modules/employee/pages/employee-flights-add/employee-flights-add.component";
import {PaymentStatusComponent} from "./components/payment-status/payment-status.component";
import {ReservationDetailsComponent} from "./modules/reservations/reservation-details/reservation-details.component";
import {
  EmployeeReservationDetailsComponent
} from "./modules/employee/pages/employee-reservations/employee-reservation-details/employee-reservation-details.component";
import {SupportComponent} from "./modules/support/support.component";
import {CreateTicketComponent} from "./modules/support/components/create-ticket/create-ticket.component";
import {
  EmployeeSupportChatComponent
} from "./modules/employee/pages/employee-issues/employee-support-chat/employee-support-chat.component";
import {SupportChatComponent} from "./modules/support/components/support-chat/support-chat.component";

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
    path: 'support',
    component: SupportComponent,
    canActivate: [authGuard]
  },
  {
    path: 'support/create-ticket',
    component: CreateTicketComponent,
    canActivate: [authGuard]
  },
  {
    path: 'support/:id',
    component: SupportChatComponent,
    canActivate: [authGuard]
  },
  {
    path: 'reservations/:id',
    component: ReservationDetailsComponent,
    canActivate: [authGuard]
  },
  {
    path: 'payment-success',
    component: PaymentStatusComponent,
    data: { status: 'success' }
  },
  {
    path: 'payment-cancel',
    component: PaymentStatusComponent,
    data: { status: 'cancel' }
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
      },
      {
        path: 'countries',
        component: CountriesComponent
      },
      {
        path: 'aircraft',
        component: AircraftListComponent
      },
      {
        path: 'airports',
        component: AirportsComponent
      }
    ]
  },
  {
    path: 'employee',
    canActivate: [authGuard],
    data: {
      role: 'ROLE_EMPLOYEE'
    },
    children: [
      {
        path: '',
        component: EmployeePanelComponent
      },
      {
        path: 'flights',
        children: [
          {
            path: '',
            component: EmployeeFlightsComponent
          },
          {
            path: 'add',
            component: EmployeeFlightsAddComponent
          },
          {
            path: ':id',
            component: EmployeeFlightDetailsComponent
          }
        ]
      },
      {
        path: 'reservations',
        children: [
          {
            path: '',
            component: EmployeeReservationsComponent
          },
          {
            path: ":id",
            component: EmployeeReservationDetailsComponent
          }
        ]
      },
      {
        path: 'issues',
        children: [
          {
            path: '',
            component: EmployeeIssuesComponent
          },
          {
            path: ':id',
            component: EmployeeSupportChatComponent
          }
        ]
      }
    ]
  }
];
