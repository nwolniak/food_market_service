import {Injectable} from "@angular/core";
import {HttpClient, HttpParams} from "@angular/common/http";
import {BehaviorSubject, concatAll, concatMap, from, map, Observable, toArray} from "rxjs";
import {environment} from "@environments/environment";
import {Cart, CartDto, Item, ItemQuantity, ItemQuantityDto} from "@app/_models";
import {ItemService} from "@app/_services/item.service";
import {AuthService} from "@app/_services/auth.service";

@Injectable({
  providedIn: "root"
})
export class CartService {

  private cartSubject: BehaviorSubject<Cart | undefined>;
  private _cart: Observable<Cart | undefined>;

  constructor(private http: HttpClient,
              private auth: AuthService,
              private itemService: ItemService) {
    this.cartSubject = new BehaviorSubject<Cart | undefined>(undefined);
    this._cart = this.cartSubject.asObservable();
    this.getByUserId(this.auth.userValue!.id!)
      .subscribe();
  }

  getById(id: string): Observable<Cart> {
    return this.http.get<CartDto>(`${environment.apiUrl}/carts/${id}`)
      .pipe(
        concatMap(cartDto => this.mapDtoToCart(cartDto)),
        map(cart => {
          this.cartSubject.next(cart);
          console.log(`Get cart: ${cart}`);
          return cart;
        }));
  }

  getByUserId(userId: string) {
    let httpParams = new HttpParams();
    httpParams = httpParams.append("findBy", "user");
    return this.http.get<CartDto>(`${environment.apiUrl}/carts/${userId}`, {params: httpParams})
      .pipe(
        concatMap(cartDto => this.mapDtoToCart(cartDto)),
        map(cart => {
          this.cartSubject.next(cart);
          console.log(`Get cart: ${cart}`);
          return cart;
        }));
  }

  getAll(): Observable<Cart[]> {
    return this.http.get<Array<CartDto>>(`${environment.apiUrl}/carts`)
      .pipe(
        concatMap(cartDtoList => cartDtoList.map(cartDto => this.mapDtoToCart(cartDto))),
        concatAll(),
        toArray()
      );
  }

  postCart(cartDto: CartDto) {
    return this.http.post<CartDto>(`${environment.apiUrl}/carts`, cartDto)
      .pipe(
        concatMap(cartDto => this.mapDtoToCart(cartDto)),
        map(cart => {
          this.cartSubject.next(cart);
          console.log(`Post cart: ${cart}`);
          return cart;
        }));
  }

  addItemToCart(itemId: string) {
    if (!this.cartValue) {
      console.log("Cart is null, creating new cart");
      return this.postCart({cartItems: []})
        .pipe(concatMap(cart => this.createOrIncrementItemInCart(cart, itemId)));
    } else {
      return this.createOrIncrementItemInCart(this.cartValue, itemId);
    }
  }

  createOrIncrementItemInCart(cart: Cart, itemId: string): Observable<Cart> {
    let currentItem = cart.cartItems.find(itemQuantity => itemQuantity.item.id === itemId);
    let newQuantity: number = currentItem?.quantity ? currentItem.quantity + 1 : 1;
    return this.patchCart(cart.id, [new ItemQuantityDto(itemId, newQuantity)]);
  }

  patchCart(id: string, itemQuantities: Array<ItemQuantityDto>): Observable<Cart> {
    return this.http.patch<CartDto>(`${environment.apiUrl}/carts/${id}`, itemQuantities)
      .pipe(
        concatMap(cartDto => this.mapDtoToCart(cartDto)),
        map(cart => {
          this.cartSubject.next(cart);
          console.log(`Patch cart: ${cart}`);
          return cart;
        }));
  }

  deleteItem(itemId: string) {
    return this.http.delete<CartDto>(`${environment.apiUrl}/carts/${this.cartValue?.id}/${itemId}`)
      .pipe(
        concatMap(cartDto => this.mapDtoToCart(cartDto)),
        map(cart => {
          this.cartSubject.next(cart);
          console.log(`Deleted cart item: ${itemId}`);
          return cart;
        }));
  }

  deleteCart(id: string): Observable<Object> {
    return this.http.delete(`${environment.apiUrl}/carts/${id}`)
      .pipe(response => {
        this.cartSubject.next(undefined);
        console.log(`Deleted cart: ${id}`);
        return response;
      })
  }

  mapCartToDto(cart: Cart): CartDto {
    return {
      cartId: cart.id,
      cartItems: cart.cartItems.map(itemQuantity => new ItemQuantityDto(
        itemQuantity.item.id,
        itemQuantity.quantity
      ))
    }
  }

  mapDtoToCart(cartDto: CartDto): Observable<Cart> {
    return this.mapCartItems(cartDto)
      .pipe(
        toArray(),
        map(itemQuantities => {
          return {
            id: cartDto.cartId!,
            cartItems: itemQuantities,
            price: this.cartPrice(itemQuantities)
          }
        })
      )
  }

  mapCartItems(cartDto: CartDto): Observable<ItemQuantity> {
    return from(cartDto.cartItems!)
      .pipe(concatMap(cartItem =>
        from(this.itemService.getById(cartItem.itemId))
          .pipe(map(itemDto => {
            return {
              item: new Item(itemDto),
              quantity: cartItem.quantity
            }
          }))))
  }

  private cartPrice(cartItems: ItemQuantity[]): number {
    return cartItems
      .map(cartItem => cartItem.quantity * parseFloat(cartItem.item.unitPrice))
      .reduce((previousValue, currentValue) => previousValue + currentValue, 0);
  }

  get cart(): Observable<Cart | undefined> {
    return this._cart;
  }

  get cartValue(): Cart | undefined {
    return this.cartSubject.value;
  }

}
