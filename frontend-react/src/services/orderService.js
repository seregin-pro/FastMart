import api from './api';

export const orderService = {
  createOrder: async (data) => {
    const res = await api.post('/orders', data);
    return res.data;
  },
  cancelOrder: async (orderId) => {
    const res = await api.post(`/orders/${orderId}/cancel`);
    return res.data;
  },
  getOrders: async (cartId) => {
    const res = await api.get('/orders', { params: { cartId } });
    return res.data;
  },
  getOrder: async (orderId) => {
    const res = await api.get(`/orders/${orderId}`);
    return res.data;
  },
};

export default orderService;
