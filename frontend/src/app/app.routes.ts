import {Routes} from "@angular/router";
import {HomeComponent} from "@app/home";
import {authGuard} from "@app/_helpers";
import {LoginComponent, RegisterComponent} from "@app/account";

const itemsModuleLazy = () => import("@app/items/items.routes").then(routes => routes.ITEMS_ROUTES);

export const APP_ROUTES: Routes = [
  {path: "", component: HomeComponent, canActivate: [authGuard]},
  {path: "account/login", component: LoginComponent},
  {path: "account/register", component: RegisterComponent},
  {path: "items", loadChildren: itemsModuleLazy, canActivate: [authGuard]},
  {path: "**", redirectTo: ""}
];
