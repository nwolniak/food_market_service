import {Component, OnInit} from "@angular/core";
import {ItemsService} from "@app/_services";
import {first} from "rxjs";
import {Item} from "@app/items/item";

@Component({templateUrl: "list.component.html"})
export class ListComponent implements OnInit {

  items?: Array<Item>;

  constructor(private itemsService: ItemsService) {
  }

  ngOnInit(): void {
    this.itemsService.getAll()
      .pipe(first())
      .subscribe(itemDtoList => this.items = itemDtoList.map(itemDto => new Item(itemDto)));
  }

  deleteItem(id: string) {
    const item = this.items!.find(item => item.id === id);
    item!.isDeleting = true;
    this.itemsService.deleteItem(id)
      .pipe(first())
      .subscribe(() => this.items = this.items!.filter(item => item.id !== id));
  }

}