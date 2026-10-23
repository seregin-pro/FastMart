import { Title, Text, Button, Paper, Stack, Flex, Grid, Container } from '@mantine/core';
import { useNavigate } from 'react-router-dom';
import productService from '../services/productService';
import { useEffect, useState } from 'react';

const HomePage = () => {
  const navigate = useNavigate();
  const [featuredProducts, setFeaturedProducts] = useState([]);

  useEffect(() => {
    const loadFeatured = async () => {
      try {
        const data = await productService.getProducts({ pageNumber: 0, pageSize: 4, sortBy: 'id', sortOrder: 'desc' });
        setFeaturedProducts(data.content || []);
      } catch (err) {
        console.error('Failed to load featured products:', err);
      }
    };
    loadFeatured();
  }, []);

  return (
    <Stack gap="xl">
      {/* Hero Section */}
      <Paper
        style={{
          background: 'linear-gradient(135deg, #1a73e8 0%, #0d47a1 100%)',
          padding: '60px 40px',
          borderRadius: 16,
          color: 'white',
        }}
      >
        <Stack gap="md" align="center" ta="center">
          <Title order={1} size="h1" fw={800}>
            Welcome to FastMart
          </Title>
          <Text size="xl" c="white" opacity={0.9}>
            Your one-stop shop for everything you need
          </Text>
          <Button
            size="lg"
            color="white"
            radius="md"
            onClick={() => navigate('/catalog')}
            style={{ color: '#1a73e8' }}
          >
            Browse Catalog
          </Button>
        </Stack>
      </Paper>

      {/* Features */}
      <Grid>
        {[
          { title: 'Fast Delivery', desc: 'Get your orders delivered in no time', icon: '🚚' },
          { title: 'Best Prices', desc: 'Competitive prices on all products', icon: '💰' },
          { title: 'Secure Payment', desc: 'Safe and encrypted transactions', icon: '🔒' },
          { title: '24/7 Support', desc: 'We are here to help anytime', icon: '💬' },
        ].map((feature, idx) => (
          <Grid.Col key={idx} span={{ base: 12, sm: 6, md: 3 }}>
            <Paper withBorder p="lg" radius="md" ta="center">
              <Text size="4xl">{feature.icon}</Text>
              <Title order={3} mt="md">{feature.title}</Title>
              <Text c="dimmed">{feature.desc}</Text>
            </Paper>
          </Grid.Col>
        ))}
      </Grid>

      {/* Featured Products */}
      <Stack gap="md">
        <Title order={2}>Featured Products</Title>
        <Grid>
          {featuredProducts.map((product) => (
            <Grid.Col key={product.id} span={{ base: 12, sm: 6, md: 3 }}>
              <Paper
                withBorder
                p="md"
                radius="md"
                style={{ cursor: 'pointer' }}
                onClick={() => navigate(`/product/${product.id}`)}
              >
                <img
                  src={product.image || 'https://via.placeholder.com/200x150'}
                  alt={product.name}
                  style={{ width: '100%', height: 150, objectFit: 'contain' }}
                />
                <Stack gap="xs" mt="md">
                  <Text fw={600}>{product.name}</Text>
                  <Text fw={700} c="blue">
                    ₽{product.price?.toFixed(2) || '0.00'}
                  </Text>
                </Stack>
              </Paper>
            </Grid.Col>
          ))}
        </Grid>
      </Stack>
    </Stack>
  );
};

export default HomePage;
