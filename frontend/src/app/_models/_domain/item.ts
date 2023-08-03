import {ItemDto} from "@app/_models";

export class Item {
  id: string;
  name: string;
  category: string;
  unitType: string;
  unitPrice: string;
  description: string;
  isDeleting: boolean = false;
  isBuying: boolean = false;

  constructor(itemDto: ItemDto) {
    this.id = itemDto.id || "";
    this.name = itemDto.name || "";
    this.category = itemDto.category || "";
    this.unitType = itemDto.unitType || "";
    this.unitPrice = itemDto.unitPrice || "";
    this.description = itemDto.description || "";
  }

}
