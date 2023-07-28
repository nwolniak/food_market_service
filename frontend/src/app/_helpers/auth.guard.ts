import {inject} from '@angular/core';
import {ActivatedRouteSnapshot, Router, RouterStateSnapshot} from '@angular/router';

import {AuthService} from '@app/_services';

export function authGuard(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
  const router: Router = inject(Router);
  const auth: AuthService = inject(AuthService);

  if (!auth.userValue) {
    router.navigate(['/account/login'], {queryParams: {returnUrl: state.url}});
    return false;
  }
  return true;
}
