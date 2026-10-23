import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {
  Paper,
  Image,
  Title,
  Text,
  Button,
  Stack,
  Group,
  Divider,
  Badge,
  Rating,
  TextInput,
  NumberInput,
  Alert,
  LoadingOverlay,
  Flex,
  Tabs,
} from '@mantine/core';
import { IconAlertCircle, IconShoppingCart } from '@tabler/icons-react';
import useAuthStore from '../store/authStore';
import useCartStore from '../store/cartStore';
import productService from '../services/productService';

const ProductDetailPage = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const { isAuthenticated } = useAuthStore();
  const { cartId, addProduct, isLoading: cartLoading } = useCartStore();
  const [product, setProduct] = useState(null);
  const [reviews, setReviews] = useState([]);
  const [summary, setSummary] = useState(null);
  const [loading, setLoading] = useState(true);
  const [quantity, setQuantity] = useState(1);
  const [error, setError] = useState(null);

  useEffect(() => {
    const loadProduct = async () => {
      try {
        const data = await productService.getProduct(id);
        setProduct(data);
      } catch (err) {
        setError('Product not found');
      } finally {
        setLoading(false);
      }
    };
    loadProduct();
  }, [id]);

  useEffect(() => {
    const loadReviews = async () => {
      try {
        const [reviewsData, summaryData] = await Promise.all([
          productService.getReviews(id),
          productService.getReviewSummary(id),
        ]);
        setReviews(reviewsData?.content || []);
        setSummary(summaryData);
      } catch (err) {
        console.error('Failed to load reviews:', err);
      }
    };
    loadReviews();
  }, [id]);

  const handleAddToCart = async () => {
    if (!isAuthenticated) {
      navigate('/login');
      return;
    }
    if (!cartId) {
      navigate('/cart');
      return;
    }

    const result = await addProduct(cartId, {
      id: product.id,
      name: product.name,
      price: product.price,
      image: product.image,
    });

    if (!result.success) {
      setError(result.error);
    }
  };

  if (loading) return <Text>Loading...</Text>;
  if (error || !product) return <Alert color="red">{error || 'Product not found'}</Alert>;

  return (
    <Stack gap="xl">
      <Flex gap="xl" wrap="wrap">
        <Paper radius="md" p="lg" style={{ flex: '1 1 400px' }}>
          <Image
            src={product.image || 'https://via.placeholder.com/400x300?text=No+Image'}
            height={350}
            fit="contain"
            alt={product.name}
            radius="md"
          />
        </Paper>

        <Paper withBorder p="xl" radius="md" style={{ flex: '1 1 400px' }}>
          <Stack gap="lg">
            <Title order={2}>{product.name}</Title>
            <Text c="dimmed" size="sm">SKU: {product.sku || 'N/A'}</Text>

            <Divider />

            <Group justify="space-between">
              <Text fw={700} size="3xl" c="blue">
                ₽{product.price?.toFixed(2) || '0.00'}
              </Text>
              <Badge size="lg" color={product.quantity > 0 ? 'green' : 'red'}>
                {product.quantity > 0 ? 'In Stock' : 'Out of Stock'}
              </Badge>
            </Group>

            <Text c="dimmed" size="sm">
              {product.description || 'No description available'}
            </Text>

            <Divider />

            {summary && (
              <Stack gap="xs">
                <Group>
                  <Rating value={summary.averageRating || 0} readOnly />
                  <Text size="sm" c="dimmed">
                    ({summary.reviewCount || 0} reviews)
                  </Text>
                </Group>
              </Stack>
            )}

            <Divider />

            <Group>
              <NumberInput
                value={quantity}
                onChange={setQuantity}
                min={1}
                max={product.quantity}
                w={120}
              />
              <Button
                size="lg"
                leftSection={<IconShoppingCart size={20} />}
                onClick={handleAddToCart}
                loading={cartLoading}
                disabled={product.quantity === 0}
                style={{ flex: 1 }}
              >
                Add to Cart
              </Button>
            </Group>
          </Stack>
        </Paper>
      </Flex>

      {/* Reviews */}
      <Paper withBorder p="xl" radius="md">
        <Title order={3}>Reviews ({reviews.length})</Title>
        <Divider my="md" />
        {reviews.length === 0 ? (
          <Text c="dimmed">No reviews yet. Be the first to review!</Text>
        ) : (
          <Stack gap="md">
            {reviews.map((review) => (
              <Paper key={review.id} withBorder p="md" radius="md">
                <Group justify="space-between">
                  <Text fw={600}>{review.userName}</Text>
                  <Rating value={review.rating} readOnly />
                </Group>
                <Text size="sm" c="dimmed" mt="xs">
                  {review.comment}
                </Text>
              </Paper>
            ))}
          </Stack>
        )}
      </Paper>
    </Stack>
  );
};

export default ProductDetailPage;
