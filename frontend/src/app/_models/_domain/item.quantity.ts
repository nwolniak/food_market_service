import {Item} from "@app/_models";

export class ItemQuantity {
  item: Item;
  quantity: number;

  constructor(item: Item, quantity: number) {
    this.item = item;
    this.quantity = quantity;
  }
}
