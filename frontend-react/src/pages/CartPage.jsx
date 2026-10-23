import { useEffect, useState } from 'react';
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
  LoadingOverlay,
  Badge,
  ActionIcon,
} from '@mantine/core';
import { IconAlertCircle, IconTrash } from '@tabler/icons-react';
import useCartStore from '../store/cartStore';
import useAuthStore from '../store/authStore';

const CartPage = () => {
  const navigate = useNavigate();
  const { cart, cartId, loadCart, removeProduct, isLoading } = useCartStore();
  const { isAuthenticated } = useAuthStore();
  const [error, setError] = useState(null);

  useEffect(() => {
    if (!isAuthenticated) {
      navigate('/login');
      return;
    }
    if (cartId) {
      loadCart(cartId);
    }
  }, [cartId, isAuthenticated]);

  const handleRemove = async (productId) => {
    if (!cartId) return;
    await removeProduct(cartId, productId);
  };

  if (!cart || !cart.items || cart.items.length === 0) {
    return (
      <Stack align="center" gap="xl">
        <Title order={2}>Your Cart is Empty</Title>
        <Text c="dimmed">Add some products to get started!</Text>
        <Button size="lg" onClick={() => navigate('/catalog')}>
          Go to Catalog
        </Button>
      </Stack>
    );
  }

  return (
    <Stack gap="xl">
      <Title order={2}>Shopping Cart</Title>

      {error && (
        <Alert icon={<IconAlertCircle size={16} />} color="red">
          {error}
        </Alert>
      )}

      {/* Cart Items */}
      <Stack gap="md">
        {cart.items.map((item) => (
          <Paper key={item.id} withBorder p="lg" radius="md">
            <Flex justify="space-between" align="center">
              <Flex gap="lg" align="center">
                <img
                  src={item.productImageUrl || 'https://via.placeholder.com/80x80'}
                  alt={item.productName}
                  style={{ width: 80, height: 80, objectFit: 'contain', borderRadius: 8 }}
                />
                <Stack gap="xs">
                  <Text fw={600} size="lg">{item.productName}</Text>
                  <Text c="dimmed" size="sm">Qty: {item.productQuantity}</Text>
                </Stack>
              </Flex>
              <Group gap="lg">
                <Text fw={700} size="lg">
                  ₽{item.totalPrice?.toFixed(2) || '0.00'}
                </Text>
                <ActionIcon
                  color="red"
                  variant="subtle"
                  onClick={() => handleRemove(item.productId)}
                >
                  <IconTrash size={18} />
                </ActionIcon>
              </Group>
            </Flex>
          </Paper>
        ))}
      </Stack>

      <Divider />

      {/* Cart Summary */}
      <Paper withBorder p="xl" radius="md">
        <Stack gap="md">
          <Flex justify="space-between">
            <Text fw={600}>Items ({cart.itemCount || cart.items.length})</Text>
            <Text>₽{cart.items.reduce((sum, i) => sum + (i.totalPrice || 0), 0).toFixed(2)}</Text>
          </Flex>
          <Divider />
          <Flex justify="space-between">
            <Text fw={700} size="lg">Total</Text>
            <Text fw={700} size="xl" c="blue">
              ₽{cart.totalPrice?.toFixed(2) || '0.00'}
            </Text>
          </Flex>
          <Button size="lg" onClick={() => navigate('/checkout')}>
            Proceed to Checkout
          </Button>
        </Stack>
      </Paper>
    </Stack>
  );
};

export default CartPage;
