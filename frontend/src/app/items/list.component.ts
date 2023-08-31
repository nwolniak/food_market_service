import {Component, OnInit, QueryList, ViewChildren} from "@angular/core";
import {AlertService, CartService, ItemService} from "@app/_services";
import {delay, first} from "rxjs";
import {NgForOf, NgIf} from "@angular/common";
import {RouterLink} from "@angular/router";
import {Item} from "@app/_models";
import {SortableHeader, SortEvent} from "@app/_helpers";

const compare = (v1: any, v2: any) => (v1 < v2 ? -1 : v1 > v2 ? 1 : 0);

@Component({
  templateUrl: "list.component.html",
  styleUrls: ["list.component.scss"],
  imports: [
    NgIf,
    NgForOf,
    RouterLink,
    SortableHeader
  ],
  standalone: true
})
export class ListComponent implements OnInit {

  items?: Item[];

  @ViewChildren(SortableHeader)
  headers?: QueryList<SortableHeader>;

  constructor(private itemsService: ItemService,
              private cartService: CartService,
              private alertService: AlertService) {
  }

  ngOnInit(): void {
    this.itemsService.getAll()
      .pipe(first())
      .subscribe(itemDtoList => this.items = itemDtoList.map(itemDto => new Item(itemDto)));
  }

  onSort({column, direction}: SortEvent) {
    if (!this.items || column === "" || !this.headers) {
      return;
    }

    this.headers
      .filter(header => header.sortable !== column)
      .forEach(header => header.direction = "");

    this.items = this.items.sort((p1, p2) => {
      const res = compare(p1[column as keyof Item], p2[column as keyof Item]);
      return direction === "asc" ? res : -res;
    });
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
