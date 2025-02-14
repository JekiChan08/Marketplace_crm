package com.example.marketplace_crm.controller;

import com.example.marketplace_crm.Model.Cart;
import com.example.marketplace_crm.Model.CartItem;
import com.example.marketplace_crm.Service.Impl.CartService;
import com.example.marketplace_crm.Service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/cart")

public class CartController {

    private final CartService cartService;

    private final ProductService productService;

    public CartController(CartService cartService, ProductService productService) {
        this.cartService = cartService;
        this.productService = productService;
    }

    @GetMapping("/{userId}/items")
    public ResponseEntity<List<CartItem>> getCartItems(@PathVariable String userId) {
        return cartService.getCartItemsByUserId(userId);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Cart> getCart(@PathVariable String userId) {
        Cart cart = cartService.getCart(userId);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/add")
    public ResponseEntity<Cart> addItem(@RequestParam("userId") String userId,
                        @RequestParam("productId") String productId,
                        @RequestParam("quantity") int quantity) {
        double price = productService.findById(productId).getPrice();
        Cart cart = cartService.addItemToCart(userId, productId, quantity, price);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> removeItem(@RequestParam String userId, @RequestParam String productId) {
        cartService.removeItemFromCart(userId, productId);
        return ResponseEntity.ok("Removed " + productId);
    }

    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<String> clearCart(@PathVariable String userId) {
        cartService.clearCart(userId);
        return ResponseEntity.ok("Cart cleared");
    }
}
