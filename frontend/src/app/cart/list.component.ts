import {Component, OnInit} from "@angular/core";
import {NgForOf, NgIf} from "@angular/common";
import {ActivatedRoute, RouterLink} from "@angular/router";
import {AlertService, CartService, ItemService} from "@app/_services";
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
              private alertService: AlertService,
              private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.cartId = this.route.snapshot.params['cartId'];
    this.cartService.cart
      .subscribe(cart => {
        this.cart = cart;
      })
  }

  deleteItem(item: Item) {
    item.isDeleting = true;
    this.cartService.deleteItem(item.id)
      .pipe(delay(250))
      .subscribe({
        next: () => {
          item.isDeleting = false;
          this.alertService.success("Item deleted from cart");
        },
        error: err => {
          item.isDeleting = false;
          this.alertService.error(err);
        }
      });
  }

  deleteCart(cart: Cart) {
    cart.isDeleting = true;
    this.cartService.deleteCart(cart.id)
      .pipe(delay(250))
      .subscribe({
        next: () => {
          cart.isDeleting = false;
          this.alertService.success("Cart deleted");
        },
        error: err => {
          cart.isDeleting = false;
          this.alertService.error(err);
        }
      });
  }

}
