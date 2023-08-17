import {Component, OnInit} from "@angular/core";
import {ActivatedRoute, Router, RouterLink, RouterLinkActive} from "@angular/router";
import {AlertService, OrderService, PaymentService} from "@app/_services";
import {NgClass, NgForOf, NgIf} from "@angular/common";
import {Order, PaymentDto} from "@app/_models";

@Component({
  templateUrl: "payment.component.html",
  imports: [
    NgIf,
    RouterLink,
    NgClass,
    NgForOf,
    RouterLinkActive
  ],
  standalone: true
})
export class PaymentComponent implements OnInit {
  orderId?: string;
  order?: Order;

  protected readonly parseFloat = parseFloat;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private orderService: OrderService,
              private paymentService: PaymentService,
              private alertService: AlertService
  ) {
  }

  ngOnInit(): void {
    this.orderId = this.route.snapshot.params['orderId'];
    if (!this.orderId) {
      return;
    }
    this.orderService.getById(this.orderId)
      .subscribe(order => this.order = order);
  }

  pay(order: Order): void {
    order.isAction = true;
    let paymentDto = new PaymentDto();
    paymentDto.amount = order.price;
    paymentDto.orderId = order.orderId;
    this.paymentService.addPayment(paymentDto)
      .subscribe({
        next: payment => {
          console.log("Payment: " + payment);
          this.alertService.success("Order has been paid", true);
          this.router.navigate(["../.."], {relativeTo: this.route});
        },
        error: err => {
          console.error("Payment: " + err);
          order.isAction = false;
          this.alertService.error(err);
        }
      });
  }
}
