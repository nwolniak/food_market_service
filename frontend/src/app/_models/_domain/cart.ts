import {ItemQuantity} from "@app/_models";

export class Cart {
  id: string;
  cartItems: ItemQuantity[];
  isAction?: boolean;

  constructor(id: string, cartItems: ItemQuantity[]) {
    this.id = id;
    this.cartItems = cartItems;
  }

}


