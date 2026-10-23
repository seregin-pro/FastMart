import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  TextInput,
  NumberInput,
  Select,
  Button,
  Grid,
  Paper,
  Image,
  Text,
  Group,
  Badge,
  Stack,
  Title,
  Divider,
  LoadingOverlay,
  Pagination,
  Flex,
  rem,
} from '@mantine/core';
import { IconSearch } from '@tabler/icons-react';
import productService from '../services/productService';

const CatalogPage = () => {
  const navigate = useNavigate();
  const [products, setProducts] = useState([]);
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState('');
  const [minPrice, setMinPrice] = useState(null);
  const [maxPrice, setMaxPrice] = useState(null);
  const [selectedCategory, setSelectedCategory] = useState(null);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);

  const PAGE_SIZE = 12;

  const loadProducts = async (page = 0, query = '') => {
    setLoading(true);
    try {
      const params = {
        pageNumber: page,
        pageSize: PAGE_SIZE,
        sortBy: 'id',
        sortOrder: 'desc',
      };
      if (query) params.query = query;
      if (minPrice) params.minPrice = minPrice;
      if (maxPrice) params.maxPrice = maxPrice;
      if (selectedCategory) params.categoryId = selectedCategory;

      const data = query
        ? await productService.searchProducts(query, page, PAGE_SIZE)
        : await productService.getProducts(params);

      setProducts(data.content || []);
      setTotalPages(data.totalPages || 0);
      setTotalElements(data.totalElements || 0);
    } catch (error) {
      console.error('Failed to load products:', error);
    } finally {
      setLoading(false);
    }
  };

  const loadCategories = async () => {
    try {
      const data = await productService.getCategories();
      setCategories(Array.isArray(data) ? data : []);
    } catch (error) {
      console.error('Failed to load categories:', error);
    }
  };

  useEffect(() => {
    loadCategories();
  }, []);

  useEffect(() => {
    loadProducts(currentPage, searchQuery);
  }, [currentPage, selectedCategory, minPrice, maxPrice]);

  const handleSearch = (e) => {
    e.preventDefault();
    setCurrentPage(0);
    loadProducts(0, searchQuery);
  };

  return (
    <Stack gap="xl">
      <Title order={2}>Catalog</Title>

      {/* Search & Filters */}
      <Paper withBorder p="lg" radius="md">
        <Grid>
          <Grid.Col span={{ base: 12, md: 6 }}>
            <form onSubmit={handleSearch}>
              <TextInput
                placeholder="Search products..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                leftSection={<IconSearch size={16} />}
                rightSection={
                  <Button size="sm" onClick={handleSearch} ml="sm">
                    Search
                  </Button>
                }
              />
            </form>
          </Grid.Col>
          <Grid.Col span={{ base: 12, md: 2 }}>
            <Select
              placeholder="Category"
              data={categories.map((c) => ({ value: String(c.id), label: c.name }))}
              value={selectedCategory}
              onChange={(v) => { setSelectedCategory(v); setCurrentPage(0); }}
            />
          </Grid.Col>
          <Grid.Col span={{ base: 6, md: 2 }}>
            <NumberInput
              placeholder="Min price"
              value={minPrice}
              onChange={setMinPrice}
              min={0}
            />
          </Grid.Col>
          <Grid.Col span={{ base: 6, md: 2 }}>
            <NumberInput
              placeholder="Max price"
              value={maxPrice}
              onChange={setMaxPrice}
              min={0}
            />
          </Grid.Col>
        </Grid>
      </Paper>

      <Text c="dimmed" size="sm">
        Showing {products.length} of {totalElements} products
      </Text>

      {/* Products Grid */}
      <Grid>
        {products.map((product) => (
          <Grid.Col key={product.id} span={{ base: 12, sm: 6, md: 4, lg: 3 }}>
            <Paper
              withBorder
              radius="md"
              p="lg"
              style={{ cursor: 'pointer' }}
              onClick={() => navigate(`/product/${product.id}`)}
              hover
            >
              <Image
                src={product.image || 'https://via.placeholder.com/300x200?text=No+Image'}
                height={160}
                fit="contain"
                alt={product.name}
                mb="md"
              />
              <Stack gap="xs">
                <Title order={4}>{product.name}</Title>
                <Text c="dimmed" size="sm" lineClamp={2}>
                  {product.description || 'No description available'}
                </Text>
                <Divider />
                <Group justify="space-between">
                  <Text fw={700} size="lg">
                    ₽{product.price?.toFixed(2) || '0.00'}
                  </Text>
                  <Badge variant="light">
                    {product.quantity || 0} in stock
                  </Badge>
                </Group>
                <Button fullWidth onClick={(e) => { e.stopPropagation(); }}>
                  View Details
                </Button>
              </Stack>
            </Paper>
          </Grid.Col>
        ))}
      </Grid>

      {/* Pagination */}
      {totalPages > 1 && (
        <Flex justify="center">
          <Pagination
            total={totalPages}
            value={currentPage + 1}
            onChange={(p) => setCurrentPage(p - 1)}
            siblings={2}
            boundaries={1}
          />
        </Flex>
      )}
    </Stack>
  );
};

export default CatalogPage;
