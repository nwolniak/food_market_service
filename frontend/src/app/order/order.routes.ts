import {Routes} from "@angular/router";
import {ListComponent, PaymentComponent} from "@app/order";

export const ORDER_ROUTES: Routes = [
  {path: "", component: ListComponent},
  {path: "payment/:orderId", component: PaymentComponent}
]
