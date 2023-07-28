import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {ListComponent} from "@app/items/list.component";
import {AddComponent} from "@app/items/add.component";
import {EditComponent} from "@app/items/edit.component";

const routes: Routes = [
  {path: "", component: ListComponent},
  {path: "add", component: AddComponent},
  {path: "edit/:id", component: EditComponent}
]

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ItemsRoutingModule {
}
