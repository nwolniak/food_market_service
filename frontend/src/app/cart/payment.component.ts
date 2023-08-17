import {Component, OnInit} from "@angular/core";
import {ActivatedRoute, Router, RouterLink, RouterLinkActive} from "@angular/router";
import {AlertService, CartService, OrderService, PaymentService} from "@app/_services";
import {NgClass, NgForOf, NgIf} from "@angular/common";
import {Cart, PaymentDto} from "@app/_models";
import {concatMap} from "rxjs";

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
  cart?: Cart;

  protected readonly parseFloat = parseFloat;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private cartService: CartService,
              private orderService: OrderService,
              private paymentService: PaymentService,
              private alertService: AlertService
  ) {
  }

  ngOnInit(): void {
    this.cartService.cart
      .subscribe(cart => this.cart = cart);
  }

  order(cart: Cart): void {
    cart.isAction = true;
    this.orderService.postOrder(cart.id)
      .pipe(
        concatMap(payment => {
          return this.cartService.deleteCart(cart.id);
        }))
      .subscribe({
        next: () => {
          this.alertService.success("Order has been created", true);
          this.router.navigate(["orders"]);
        },
        error: err => {
          cart.isAction = false;
          this.alertService.error(err);
        }
      });
  }

  orderAndPay(cart: Cart): void {
    cart.isAction = true;
    this.orderService.postOrder(cart.id)
      .pipe(
        concatMap(order => {
          let paymentDto = new PaymentDto();
          paymentDto.amount = order.price;
          paymentDto.orderId = order.orderId;
          return this.paymentService.addPayment(paymentDto);
        }),
        concatMap(payment => {
          return this.cartService.deleteCart(cart.id);
        }))
      .subscribe({
        next: () => {
          this.alertService.success("Order has been created and paid", true);
          this.router.navigate(["orders"]);
        },
        error: err => {
          cart.isAction = false;
          this.alertService.error(err);
        }
      });
  }
}
