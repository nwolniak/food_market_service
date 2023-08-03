import {ItemQuantityDto} from "@app/_models";

export class CartDto {
  cartId?: string;
  cartItems?: Array<ItemQuantityDto>;

  constructor(cartId?: string, cartItems?: Array<ItemQuantityDto>) {
    this.cartId = cartId;
    this.cartItems = cartItems;
  }

}


