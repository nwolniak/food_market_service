import {Component, OnInit} from "@angular/core";
import {AlertService, CartService, ItemService} from "@app/_services";
import {delay, first} from "rxjs";
import {NgForOf, NgIf} from "@angular/common";
import {RouterLink} from "@angular/router";
import {Item} from "@app/_models";

@Component({
  templateUrl: "list.component.html",
  imports: [
    NgIf,
    NgForOf,
    RouterLink
  ],
  standalone: true
})
export class ListComponent implements OnInit {

  items?: Item[];

  constructor(private itemsService: ItemService,
              private cartService: CartService,
              private alertService: AlertService) {
  }

  ngOnInit(): void {
    this.itemsService.getAll()
      .pipe(first())
      .subscribe(itemDtoList => this.items = itemDtoList.map(itemDto => new Item(itemDto)));
  }

  addItemToCart(item: Item) {
    item.isBuying = true;
    this.cartService.addItemToCart(item.id)
      .pipe(delay(250))
      .subscribe({
        next: () => {
          item.isBuying = false;
          this.alertService.success("Item added to cart");
        },
        error: err => {
          item.isBuying = false;
          this.alertService.error(err);
        }
      });
  }

  deleteItem(item: Item) {
    item.isDeleting = true;
    this.itemsService.deleteItem(item.id)
      .pipe(delay(250))
      .subscribe({
        next: () => {
          item.isDeleting = false;
          this.items = this.items!.filter(item => item.id !== item.id);
          this.alertService.success("Item deleted");
        },
        error: err => {
          item.isDeleting = false;
          this.alertService.error(err);
        }
      });
  }

}
