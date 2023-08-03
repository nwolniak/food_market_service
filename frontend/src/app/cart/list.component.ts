import {Component, OnInit} from "@angular/core";
import {NgForOf, NgIf} from "@angular/common";
import {ActivatedRoute, RouterLink} from "@angular/router";
import {CartService, ItemService} from "@app/_services";
import {Cart} from "@app/_models/_domain/cart";

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
              private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.cartId = this.route.snapshot.params['cartId'];
    this.cartService.cart
      .subscribe(cart => {
        console.log("subscribed cart from cart service");
        this.cart = cart;
      })
    this.cartService.getById(this.cartId!)
      .subscribe();
  }

  deleteItem(itemId: string) {
    this.cartService.deleteItem(itemId)
      ?.subscribe();
  }

  deleteCart(cartId: string) {
    this.cartService.deleteCart(cartId)
      .subscribe();
  }

}
