export class ItemQuantityDto {
  itemId: string;
  quantity: number;

  constructor(itemId: string, quantity: number) {
    this.itemId = itemId;
    this.quantity = quantity;
  }

}
