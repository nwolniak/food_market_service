import {Component, OnInit} from "@angular/core";
import {NgForOf, NgIf} from "@angular/common";
import {ActivatedRoute, RouterLink} from "@angular/router";
import {AlertService, AuthService, CartService, ItemService, OrderService} from "@app/_services";
import {Cart} from "@app/_models/_domain/cart";
import {delay} from "rxjs";
import {Item} from "@app/_models";

@Component({
  templateUrl: "list.component.html",
  imports: [
    NgForOf,
    NgIf,
    RouterLink
  ],
  standalone: true
})
export class ListComponent implements OnInit {
  cartId?: string;
  cart?: Cart;

  constructor(private cartService: CartService,
              private itemService: ItemService,
              private orderService: OrderService,
              private alertService: AlertService,
              private auth: AuthService,
              private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.cartId = this.auth.userValue?.id;
    this.cartService.cart
      .subscribe(cart => this.cart = cart)
  }

  createOrder(cart: Cart) {
    cart.isAction = true;
    this.orderService.postOrder(cart.id)
      .subscribe({
        next: () => {
          this.alertService.success("Order created");
          this.deleteCart(cart);
        },
        error: err => {
          cart.isAction = false;
          this.alertService.error(err);
        }
      })
  }

  deleteItem(item: Item) {
    item.isDeleting = true;
    this.cartService.deleteItem(item.id)
      .pipe(delay(250))
      .subscribe({
        next: () => {
          this.alertService.success("Item deleted from cart");
        },
        error: err => {
          item.isDeleting = false;
          this.alertService.error(err);
        }
      });
  }

  deleteCart(cart: Cart) {
    cart.isAction = true;
    this.cartService.deleteCart(cart.id)
      .pipe(delay(250))
      .subscribe({
        next: () => {
          this.alertService.success("Cart deleted");
        },
        error: err => {
          cart.isAction = false;
          this.alertService.error(err);
        }
      });
  }

}
