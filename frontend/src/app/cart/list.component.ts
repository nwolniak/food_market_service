import {Component, OnInit, QueryList, ViewChildren} from "@angular/core";
import {NgForOf, NgIf} from "@angular/common";
import {ActivatedRoute, RouterLink} from "@angular/router";
import {AlertService, AuthService, CartService, ItemService, OrderService} from "@app/_services";
import {Cart} from "@app/_models/_domain/cart";
import {delay} from "rxjs";
import {Item, ItemQuantity} from "@app/_models";
import {SortableHeader, SortEvent} from "@app/_helpers";

const compare = (v1: any, v2: any) => (v1 < v2 ? -1 : v1 > v2 ? 1 : 0);

@Component({
  templateUrl: "list.component.html",
  styleUrls: ["list.component.scss"],
  imports: [
    NgForOf,
    NgIf,
    RouterLink,
    SortableHeader
  ],
  standalone: true
})
export class ListComponent implements OnInit {
  cart?: Cart;

  @ViewChildren(SortableHeader)
  headers?: QueryList<SortableHeader>;

  constructor(private cartService: CartService,
              private itemService: ItemService,
              private orderService: OrderService,
              private alertService: AlertService,
              private auth: AuthService,
              private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.cartService.cart
      .subscribe(cart => this.cart = cart);
    this.cartService.getByUserId(this.auth.userValue!.id!)
      .subscribe();
  }

  onSort({column, direction}: SortEvent) {
    if (!this.cart || column === "" || !this.headers) {
      return;
    }
    console.log("Column: " + column);
    this.headers
      .filter(header => header.sortable !== column)
      .forEach(header => header.direction = "");

    this.cart.cartItems = this.cart.cartItems
      .sort((itemQuantity1, itemQuantity2) => {
      let res = compare(itemQuantity1[column as keyof ItemQuantity], itemQuantity2[column as keyof ItemQuantity]);
      if (res == 0) {
        res = compare(itemQuantity1.item[column as keyof Item], itemQuantity2.item[column as keyof Item]);
      }
      return direction === "asc" ? res : -res;
    });
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
