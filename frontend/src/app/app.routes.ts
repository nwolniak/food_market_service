import {Routes} from "@angular/router";
import {HomeComponent} from "@app/home";
import {authGuard} from "@app/_helpers";
import {LoginComponent, RegisterComponent} from "@app/account";

const itemsModuleLazy = () => import("@app/items/items.routes").then(routes => routes.ITEMS_ROUTES);
const cartModuleLazy = () => import("@app/cart/cart.routes").then(routes => routes.CART_ROUTES);

const orderModuleLazy = () => import("@app/order/order.routes").then(routes => routes.ORDER_ROUTES);

const paymentsModuleLazy = () => import("@app/payments/payments.routes").then(routes => routes.PAYMENTS_ROUTES);

const profileModuleLazy = () => import("@app/profile/profile.routes").then(routes => routes.PROFILE_ROUTES);

export const APP_ROUTES: Routes = [
  {path: "", component: HomeComponent, canActivate: [authGuard]},
  {path: "account/login", component: LoginComponent},
  {path: "account/register", component: RegisterComponent},
  {path: "items", loadChildren: itemsModuleLazy, canActivate: [authGuard]},
  {path: "cart", loadChildren: cartModuleLazy, canActivate: [authGuard]},
  {path: "orders", loadChildren: orderModuleLazy, canActivate: [authGuard]},
  {path: "payments", loadChildren: paymentsModuleLazy, canActivate: [authGuard]},
  {path: "profile", loadChildren: profileModuleLazy, canActivate: [authGuard]},
  {path: "**", redirectTo: ""}
];
