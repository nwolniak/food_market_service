import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {HomeComponent} from "./home";
import {LoginComponent, RegisterComponent} from "./account";
import {AuthGuard} from "@app/_helpers";

const itemsModuleLazy = () => import("@app/items/items.module").then(value => value.ItemsModule);

const routes: Routes = [
  {path: "", component: HomeComponent, canActivate: [AuthGuard]},
  {path: "account/login", component: LoginComponent},
  {path: "account/register", component: RegisterComponent},
  {path: "items", loadChildren: itemsModuleLazy, canActivate: [AuthGuard]},
  {path: "**", redirectTo: ""}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
