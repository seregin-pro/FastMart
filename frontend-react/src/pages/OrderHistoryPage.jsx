import { useState, useEffect } from 'react';
import {
  Paper,
  Title,
  Text,
  Stack,
  Group,
  Badge,
  Divider,
  Flex,
  Alert,
  LoadingOverlay,
  Button,
} from '@mantine/core';
import { IconAlertCircle } from '@tabler/icons-react';
import { useNavigate } from 'react-router-dom';
import orderService from '../services/orderService';
import useAuthStore from '../store/authStore';

const OrderHistoryPage = () => {
  const navigate = useNavigate();
  const { user } = useAuthStore();
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const loadOrders = async () => {
      try {
        // Using cartId=0 as placeholder — in real app, orders would be user-based
        const data = await orderService.getOrders(0);
        if (Array.isArray(data)) {
          setOrders(data);
        } else if (data?.orders) {
          setOrders(data.orders);
        }
      } catch (err) {
        setError('Failed to load orders');
      } finally {
        setLoading(false);
      }
    };
    loadOrders();
  }, []);

  const getStatusColor = (status) => {
    switch (status?.toUpperCase()) {
      case 'PENDING': return 'yellow';
      case 'CONFIRMED': return 'blue';
      case 'SHIPPED': return 'cyan';
      case 'DELIVERED': return 'green';
      case 'CANCELLED': return 'red';
      case 'PAID': return 'teal';
      default: return 'gray';
    }
  };

  if (loading) return <Text>Loading...</Text>;

  return (
    <Stack gap="xl">
      <Title order={2}>Order History</Title>

      {error && (
        <Alert icon={<IconAlertCircle size={16} />} color="red">
          {error}
        </Alert>
      )}

      {orders.length === 0 ? (
        <Stack align="center" gap="xl">
          <Title order={3}>No Orders Yet</Title>
          <Text c="dimmed">Start shopping to see your orders here!</Text>
          <Button onClick={() => navigate('/catalog')}>Go to Catalog</Button>
        </Stack>
      ) : (
        <Stack gap="md">
          {orders.map((order) => (
            <Paper key={order.orderId} withBorder p="lg" radius="md">
              <Stack gap="md">
                <Flex justify="space-between" align="center">
                  <Group>
                    <Text fw={700}>Order #{order.orderId}</Text>
                    <Badge color={getStatusColor(order.orderStatus)} size="lg">
                      {order.orderStatus}
                    </Badge>
                  </Group>
                  <Text c="dimmed" size="sm">
                    {order.createdAt ? new Date(order.createdAt).toLocaleDateString() : 'N/A'}
                  </Text>
                </Flex>

                <Divider />

                <Stack gap="xs">
                  {order.orderItems?.map((item, idx) => (
                    <Flex key={idx} justify="space-between">
                      <Text size="sm">
                        {item.productName} x{item.productQuantity}
                      </Text>
                      <Text size="sm" fw={600}>
                        ₽{item.totalPrice?.toFixed(2) || '0.00'}
                      </Text>
                    </Flex>
                  ))}
                </Stack>

                <Divider />

                <Flex justify="space-between">
                  <Text fw={700}>Total</Text>
                  <Text fw={700} size="xl" c="blue">
                    ₽{order.totalOrderPrice?.toFixed(2) || '0.00'}
                  </Text>
                </Flex>
              </Stack>
            </Paper>
          ))}
        </Stack>
      )}
    </Stack>
  );
};

export default OrderHistoryPage;
