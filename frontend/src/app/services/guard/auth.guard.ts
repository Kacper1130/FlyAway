import {ActivatedRouteSnapshot, CanActivateFn, Router} from '@angular/router';
import {inject} from "@angular/core";
import {TokenService} from "../token/token.service";

export const authGuard: CanActivateFn = (route: ActivatedRouteSnapshot) => {
  const tokenService = inject(TokenService);
  const router = inject(Router);
  if (tokenService.isTokenNotValid()) {
    router.navigate(['login'])
    return false;
  }
  const userRole = tokenService.getRole();
  console.log(userRole);
  console.log(route.data['role']);
  if (route.data['role'] && route.data['role'].indexOf(userRole) === -1) {
    router.navigate([''])
    return false;
  }
  return true;
};
