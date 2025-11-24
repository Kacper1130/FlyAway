import {ActivatedRouteSnapshot, CanActivateFn, Router} from '@angular/router';
import {inject} from "@angular/core";
import {LocalStorageService} from "../token/local-storage.service";

export const authGuard: CanActivateFn = (route: ActivatedRouteSnapshot) => {
  const localStorageService = inject(LocalStorageService);
  const router = inject(Router);
  if (!localStorageService.isLogged()) {
    router.navigate(['login'])
    return false;
  }
  const userRole = localStorageService.role;
  console.log('z auth guarda' + userRole);
  console.log(route.data['role']);
  if (route.data['role'] && route.data['role'].indexOf(userRole) === -1) {
    router.navigate([''])
    return false;
  }
  return true;
};
