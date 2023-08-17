import {Routes} from "@angular/router";
import {ListComponent, PaymentComponent} from "@app/cart";

export const CART_ROUTES: Routes = [
  {path: "", component: ListComponent},
  {path: "payment", component: PaymentComponent}
]
