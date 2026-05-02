package com.tribal.crafts.service;

import com.tribal.crafts.dto.CartRequest;
import com.tribal.crafts.dto.CartResponse;
import com.tribal.crafts.entity.Cart;
import com.tribal.crafts.entity.Product;
import com.tribal.crafts.entity.User;
import com.tribal.crafts.exception.ResourceNotFoundException;
import com.tribal.crafts.repository.CartRepository;
import com.tribal.crafts.repository.ProductRepository;
import com.tribal.crafts.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<CartResponse> getCartByCustomerEmail(String email) {
        User customer = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        return cartRepository.findByCustomerId(customer.getId())
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public CartResponse addToCart(CartRequest request, String customerEmail) {
        User customer = userRepository.findByEmail(customerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        // Check if item already exists in cart
        List<Cart> existingItems = cartRepository.findByCustomerIdAndProductId(customer.getId(), product.getId());
        
        Cart cart;
        if (!existingItems.isEmpty()) {
            // Update quantity if item already exists
            cart = existingItems.get(0);
            cart.setQuantity(cart.getQuantity() + request.getQuantity());
        } else {
            // Add new item if it doesn't exist
            cart = new Cart();
            cart.setCustomer(customer);
            cart.setProduct(product);
            cart.setQuantity(request.getQuantity());
        }

        cart = cartRepository.save(cart);
        return convertToResponse(cart);
    }

    public void removeFromCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));
        cartRepository.delete(cart);
    }

    @Transactional
    public CartResponse updateQuantity(Long cartId, Integer newQuantity) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));
        
        if (newQuantity <= 0) {
            cartRepository.delete(cart);
            return null;
        }
        
        cart.setQuantity(newQuantity);
        cart = cartRepository.save(cart);
        return convertToResponse(cart);
    }

    public void clearCart(String customerEmail) {
        User customer = userRepository.findByEmail(customerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        List<Cart> cartItems = cartRepository.findByCustomerId(customer.getId());
        cartRepository.deleteAll(cartItems);
    }

    private CartResponse convertToResponse(Cart cart) {
        Product product = cart.getProduct();
        return new CartResponse(
                cart.getId(),
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImage(),
                product.getCategory(),
                cart.getQuantity()
        );
    }
}
