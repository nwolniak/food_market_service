import {Routes} from "@angular/router";
import {ProfileComponent} from "@app/profile";
import {PasswordEditComponent} from "@app/profile/password.edit.component";

export const PROFILE_ROUTES: Routes = [
  {path: "", component: ProfileComponent},
  {path: "password/change", component: PasswordEditComponent},
]
