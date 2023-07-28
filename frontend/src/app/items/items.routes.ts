import {Routes} from "@angular/router";
import {ListComponent} from "@app/items/list.component";
import {AddComponent} from "@app/items/add.component";
import {EditComponent} from "@app/items/edit.component";

export const ITEMS_ROUTES: Routes = [
  {path: "", component: ListComponent},
  {path: "add", component: AddComponent},
  {path: "edit/:id", component: EditComponent}
]
