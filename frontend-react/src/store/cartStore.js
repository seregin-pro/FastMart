import { create } from 'zustand';
import { cartService } from '../services/cartService';

const useCartStore = create((set) => ({
  cart: null,
  cartId: null,
  isLoading: false,
  error: null,

  createCart: async (customerId) => {
    set({ isLoading: true, error: null });
    try {
      const response = await cartService.createCart(customerId);
      const cartId = response.cartId || response.data?.cartId;
      set({ cartId, cart: response, isLoading: false });
      return { success: true, cartId };
    } catch (error) {
      set({ error: error.message, isLoading: false });
      return { success: false, error: error.message };
    }
  },

  loadCart: async (cartId) => {
    set({ isLoading: true, error: null });
    try {
      const response = await cartService.getCart(cartId);
      set({ cart: response, isLoading: false });
      return response;
    } catch (error) {
      set({ error: error.message, isLoading: false });
      return null;
    }
  },

  addProduct: async (cartId, product) => {
    set({ isLoading: true, error: null });
    try {
      await cartService.addProduct(cartId, {
        productId: product.id,
        productName: product.name,
        productQuantity: 1,
        pricePerUnit: product.price,
        imageUrl: product.image,
      });
      const updated = await cartService.getCart(cartId);
      set({ cart: updated, isLoading: false });
      return { success: true };
    } catch (error) {
      set({ error: error.message, isLoading: false });
      return { success: false, error: error.message };
    }
  },

  removeProduct: async (cartId, productId) => {
    set({ isLoading: true, error: null });
    try {
      await cartService.removeProduct(cartId, productId);
      const updated = await cartService.getCart(cartId);
      set({ cart: updated, isLoading: false });
      return { success: true };
    } catch (error) {
      set({ error: error.message, isLoading: false });
      return { success: false, error: error.message };
    }
  },

  clearCart: async () => {
    set({ cart: null, cartId: null });
  },
}));

export default useCartStore;
