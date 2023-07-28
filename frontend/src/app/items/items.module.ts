import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {ReactiveFormsModule} from "@angular/forms";
import {ItemsRoutingModule} from "@app/items/items-routing.module";
import {ListComponent} from "@app/items/list.component";
import {AddComponent} from "@app/items/add.component";
import {EditComponent} from "@app/items/edit.component";


@NgModule({
  imports: [
    CommonModule,
    ReactiveFormsModule,
    ItemsRoutingModule
  ],
  declarations: [
    ListComponent,
    AddComponent,
    EditComponent
  ]
})
export class ItemsModule {
}
