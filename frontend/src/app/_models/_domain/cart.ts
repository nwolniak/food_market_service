import {ItemQuantity} from "@app/_models";

export class Cart {
  id: string;
  cartItems: ItemQuantity[];
  price: number;
  isAction?: boolean;

  constructor(id: string, cartItems: ItemQuantity[], price: number) {
    this.id = id;
    this.price = price;
    this.cartItems = cartItems;
  }

}


