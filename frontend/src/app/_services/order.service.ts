import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {AuthService} from "@app/_services/auth.service";
import {Item, ItemQuantity, Order, OrderDto} from "@app/_models";
import {environment} from "@environments/environment";
import {concatAll, concatMap, from, map, Observable, toArray} from "rxjs";
import {ItemService} from "@app/_services/item.service";

@Injectable({providedIn: "root"})
export class OrderService {


  constructor(private http: HttpClient,
              private auth: AuthService,
              private itemService: ItemService) {
  }

  getById(orderId: string): Observable<Order> {
    return this.http.get<OrderDto>(`${environment.apiUrl}/orders/${orderId}`)
      .pipe(
        concatMap(orderDto => this.mapDtoToOrder(orderDto)),
        map(order => {
          console.log(`Get order ${order}`)
          return order;
        })
      );
  }

  getAll(): Observable<Order[]> {
    return this.http.get<OrderDto[]>(`${environment.apiUrl}/orders`)
      .pipe(
        concatMap((orderDtoList: OrderDto[]) => orderDtoList.map(orderDto => this.mapDtoToOrder(orderDto))),
        concatAll(),
        toArray()
      );
  }

  postOrder(cartId: string): Observable<Order> {
    return this.http.post<OrderDto>(`${environment.apiUrl}/orders`, {cartId})
      .pipe(
        concatMap(orderDto => this.mapDtoToOrder(orderDto)),
        map(order => {
          console.log(`Get order ${order}`)
          return order;
        })
      );
  }

  deleteOrder(userId: string, orderId: string) {
    return this.http.delete(`${environment.apiUrl}/orders/${userId}/${orderId}`);
  }

  mapDtoToOrder(orderDto: OrderDto): Observable<Order> {
    return this.mapCartItems(orderDto)
      .pipe(
        toArray(),
        map(itemQuantities => {
          return {
            orderId: orderDto.orderId!,
            orderItems: itemQuantities
          }
        })
      )
  }

  mapCartItems(orderDto: OrderDto): Observable<ItemQuantity> {
    return from(orderDto.orderItems!)
      .pipe(concatMap(orderItem =>
        from(this.itemService.getById(orderItem.itemId))
          .pipe(map(itemDto => {
            return {
              item: new Item(itemDto),
              quantity: orderItem.quantity
            }
          }))))
  }

}
