import api from './api';

export const cartService = {
  createCart: async (customerId) => {
    const res = await api.post('/cart', { customerId });
    return res.data;
  },
  getCart: async (cartId) => {
    const res = await api.get(`/cart/${cartId}`);
    return res.data;
  },
  addProduct: async (cartId, productItem) => {
    const res = await api.post(`/cart/${cartId}/items`, productItem);
    return res.data;
  },
  removeProduct: async (cartId, productId) => {
    const res = await api.delete(`/cart/${cartId}/items/${productId}`);
    return res.data;
  },
  decreaseProduct: async (cartId, productId) => {
    const res = await api.post(`/cart/${cartId}/decrease`, { productId });
    return res.data;
  },
  cleanCart: async (cartId) => {
    const res = await api.post(`/cart/${cartId}/clean`, { cartId, productIds: [] });
    return res.data;
  },
};

export default cartService;
