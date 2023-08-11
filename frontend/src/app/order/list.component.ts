import {Component, OnInit} from "@angular/core";
import {Order} from "@app/_models";
import {AlertService, AuthService, OrderService} from "@app/_services";
import {ActivatedRoute, RouterLink} from "@angular/router";
import {NgClass, NgForOf, NgIf} from "@angular/common";
import {CollapseModule} from "ngx-bootstrap/collapse";

@Component({
  templateUrl: "list.component.html",
  imports: [
    NgForOf,
    RouterLink,
    NgIf,
    NgClass,
    CollapseModule,
  ],
  standalone: true
})
export class ListComponent implements OnInit {

  protected readonly parseFloat = parseFloat;

  userId?: string;
  orders?: Order[];

  constructor(private route: ActivatedRoute,
              private auth: AuthService,
              private orderService: OrderService,
              private alertService: AlertService
  ) {
  }

  ngOnInit(): void {
    this.userId = this.auth.userValue?.id;
    this.orderService.getAll().subscribe();
    this.orderService.orders
      .subscribe(orders => this.orders = orders);
  }

  delete(order: Order) {
    order.isAction = true;
    this.orderService.deleteOrder(order.orderId)
      .subscribe({
        next: () => {
          this.alertService.success("Order deleted");
        },
        error: err => {
          order.isAction = false;
          this.alertService.error(err);
        }
      });
  }

  paidOrdersSorted(): Order[] | undefined {
    return this.orders
      ?.filter(order => order.isPaid)
      .sort((o1, o2) => o2.createdDate.localeCompare(o1.createdDate));
  }

  unPaidOrdersSorted(): Order[] | undefined {
    return this.orders
      ?.filter(order => !order.isPaid)
      .sort((o1, o2) => o2.createdDate.localeCompare(o1.createdDate));
  }

}
