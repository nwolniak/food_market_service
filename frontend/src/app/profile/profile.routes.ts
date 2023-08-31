import {Routes} from "@angular/router";
import {PasswordEditComponent, ProfileComponent} from "@app/profile"

export const PROFILE_ROUTES: Routes = [
  {path: "", component: ProfileComponent},
  {path: "password/change", component: PasswordEditComponent},
];
