export class ItemQuantityDto {
  id: string;
  quantity: number;

  constructor(id: string, quantity: number) {
    this.id = id;
    this.quantity = quantity;
  }

}
