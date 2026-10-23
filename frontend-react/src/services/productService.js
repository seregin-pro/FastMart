import api from './api';

export const productService = {
  getProducts: async (params = {}) => {
    const res = await api.get('/products', { params });
    return res.data;
  },
  getProduct: async (id) => {
    const res = await api.get(`/products/${id}`);
    return res.data;
  },
  searchProducts: async (query, page = 0, size = 10) => {
    const res = await api.get('/products/search', {
      params: { query, pageNumber: page, pageSize: size },
    });
    return res.data;
  },
  getCategories: async () => {
    const res = await api.get('/categories/all');
    return res.data;
  },
  getManufacturers: async () => {
    const res = await api.get('/manufacturers');
    return res.data;
  },
  getReviews: async (productId, page = 0, size = 10) => {
    const res = await api.get(`/products/${productId}/reviews`, {
      params: { page, size },
    });
    return res.data;
  },
  getReviewSummary: async (productId) => {
    const res = await api.get(`/products/${productId}/reviews/summary`);
    return res.data;
  },
  addReview: async (productId, review) => {
    const res = await api.post(`/products/${productId}/reviews`, review);
    return res.data;
  },
};

export default productService;
