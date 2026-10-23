import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Paper,
  Title,
  Text,
  Button,
  Group,
  Stack,
  Divider,
  Flex,
  Alert,
  Radio,
  Select,
  LoadingOverlay,
} from '@mantine/core';
import { IconAlertCircle, IconCheck } from '@tabler/icons-react';
import useCartStore from '../store/cartStore';
import useAuthStore from '../store/authStore';
import orderService from '../services/orderService';

const CheckoutPage = () => {
  const navigate = useNavigate();
  const { cart, cartId, clearCart } = useCartStore();
  const { user } = useAuthStore();
  const [orderPlaced, setOrderPlaced] = useState(false);
  const [error, setError] = useState(null);
  const [selectedAddress, setSelectedAddress] = useState(null);
  const [paymentMethod, setPaymentMethod] = useState('yookassa');

  useEffect(() => {
    if (!isAuthenticated) {
      navigate('/login');
    }
    if (!cartId) {
      navigate('/cart');
    }
  }, [isAuthenticated, cartId]);

  if (!cart || !cart.items || cart.items.length === 0) {
    return (
      <Stack align="center" gap="xl">
        <Title order={2}>Nothing to Checkout</Title>
        <Button onClick={() => navigate('/cart')}>Go to Cart</Button>
      </Stack>
    );
  }

  const handlePlaceOrder = async () => {
    try {
      const orderItems = cart.items.map((item) => ({
        productId: item.productId,
        productName: item.productName,
        productQuantity: item.productQuantity,
        pricePerUnit: item.pricePerUnit,
        totalPrice: item.totalPrice,
        productImageUrl: item.productImageUrl,
      }));

      const result = await orderService.createOrder({
        cartId: cartId,
        totalOrderPrice: cart.totalPrice,
        orderItems: orderItems,
      });

      if (result.code === '200') {
        setOrderPlaced(true);
        clearCart();
      } else {
        setError(result.message || 'Failed to place order');
      }
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to place order');
    }
  };

  return (
    <Stack gap="xl">
      <Title order={2}>Checkout</Title>

      {error && (
        <Alert icon={<IconAlertCircle size={16} />} color="red">
          {error}
        </Alert>
      )}

      {orderPlaced && (
        <Alert icon={<IconCheck size={16} />} color="green">
          Order placed successfully!
        </Alert>
      )}

      <Flex gap="xl" wrap="wrap">
        {/* Order Summary */}
        <Paper withBorder p="xl" radius="md" style={{ flex: '1 1 400px' }}>
          <Title order={3}>Order Summary</Title>
          <Divider my="md" />
          <Stack gap="md">
            {cart.items.map((item) => (
              <Flex key={item.id} justify="space-between">
                <Text size="sm">{item.productName} x{item.productQuantity}</Text>
                <Text size="sm" fw={600}>
                  ₽{item.totalPrice?.toFixed(2) || '0.00'}
                </Text>
              </Flex>
            ))}
            <Divider />
            <Flex justify="space-between">
              <Text fw={700}>Total</Text>
              <Text fw={700} size="xl" c="blue">
                ₽{cart.totalPrice?.toFixed(2) || '0.00'}
              </Text>
            </Flex>
          </Stack>
        </Paper>

        {/* Delivery & Payment */}
        <Paper withBorder p="xl" radius="md" style={{ flex: '1 1 400px' }}>
          <Title order={3}>Delivery</Title>
          <Divider my="md" />
          <Stack gap="md">
            <Text size="sm">
              {user?.firstName} {user?.lastName}
            </Text>
            <Text c="dimmed" size="sm">
              {user?.email}
            </Text>

            <Select
              label="Address"
              placeholder="Select delivery address"
              data={[
                { value: 'default', label: 'Default address' },
                { value: 'work', label: 'Work address' },
              ]}
              value={selectedAddress}
              onChange={setSelectedAddress}
            />

            <Title order={4} mt="md">Payment Method</Title>
            <Radio.Group value={paymentMethod} onChange={setPaymentMethod}>
              <Stack gap="xs">
                <Radio value="yookassa" label="YooKassa (Online)" />
                <Radio value="card" label="Bank Card" />
                <Radio value="sbp" label="SBP" />
              </Stack>
            </Radio.Group>

            <Button
              size="lg"
              onClick={handlePlaceOrder}
              disabled={orderPlaced}
              fullWidth
              mt="lg"
            >
              Place Order — ₽{cart.totalPrice?.toFixed(2) || '0.00'}
            </Button>
          </Stack>
        </Paper>
      </Flex>
    </Stack>
  );
};

export default CheckoutPage;
