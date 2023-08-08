import {Routes} from "@angular/router";
import {HomeComponent} from "@app/home";
import {authGuard} from "@app/_helpers";
import {LoginComponent, RegisterComponent} from "@app/account";

const itemsModuleLazy = () => import("@app/items/items.routes").then(routes => routes.ITEMS_ROUTES);
const cartModuleLazy = () => import("@app/cart/cart.routes").then(routes => routes.CART_ROUTES);

const orderModuleLazy = () => import("@app/order/order.routes").then(routes => routes.ORDER_ROUTES);

export const APP_ROUTES: Routes = [
  {path: "", component: HomeComponent, canActivate: [authGuard]},
  {path: "account/login", component: LoginComponent},
  {path: "account/register", component: RegisterComponent},
  {path: "items", loadChildren: itemsModuleLazy, canActivate: [authGuard]},
  {path: "cart/:cartId", loadChildren: cartModuleLazy, canActivate: [authGuard]},
  {path: "orders/:userId", loadChildren: orderModuleLazy, canActivate: [authGuard]},
  {path: "**", redirectTo: ""}
];
