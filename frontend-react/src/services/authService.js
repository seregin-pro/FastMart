import api from './api';

export const authService = {
  signup: async (data) => {
    const res = await api.post('/auth/signup', data);
    return res.data;
  },
  signin: async (data) => {
    const res = await api.post('/auth/signin', data);
    return res.data;
  },
  getUserInfo: async (username) => {
    const res = await api.get('/users/me', {
      headers: { 'X-User-Name': username },
    });
    return res.data;
  },
  updateProfile: async (username, data) => {
    const res = await api.put('/users/me', data, {
      headers: { 'X-User-Name': username },
    });
    return res.data;
  },
  getAddresses: async (username) => {
    const res = await api.get('/users/me/addresses', {
      headers: { 'X-User-Name': username },
    });
    return res.data;
  },
  updateAddress: async (username, addressId, data) => {
    const res = await api.put(`/users/me/addresses/${addressId}`, data, {
      headers: { 'X-User-Name': username },
    });
    return res.data;
  },
  deleteAddress: async (username, addressId) => {
    const res = await api.delete(`/users/me/addresses/${addressId}`, {
      headers: { 'X-User-Name': username },
    });
    return res.data;
  },
};

export default authService;
