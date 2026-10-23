import { Links, Outlet, useNavigate, useLocation } from 'react-router-dom';
import {
  MantineProvider,
  Navbar,
  Header,
  Footer,
  Container,
  Group,
  Button,
  Text,
  Badge,
  ActionIcon,
  Avatar,
  Menu,
  AppShell,
  Flex,
  ThemeIcon,
} from '@mantine/core';
import { useDisclosure } from '@mantine/hooks';
import {
  IconShoppingCart,
  IconBox,
  IconUser,
  IconLogout,
  IconList,
  IconHome,
  IconMenu2,
  IconX,
} from '@tabler/icons-react';
import { useState, useEffect } from 'react';
import useAuthStore from '../store/authStore';

const MainLayout = () => {
  const [navbarOpened, { toggle: toggleNavbar }] = useDisclosure();
  const navigate = useNavigate();
  const location = useLocation();
  const { isAuthenticated, user, logout } = useAuthStore();

  const navItems = [
    { label: 'Home', path: '/', icon: <IconHome size={18} /> },
    { label: 'Catalog', path: '/catalog', icon: <IconBox size={18} /> },
    { label: 'Cart', path: '/cart', icon: <IconShoppingCart size={18} /> },
  ];

  if (isAuthenticated) {
    navItems.push({ label: 'Profile', path: '/profile', icon: <IconUser size={18} /> });
    navItems.push({ label: 'Orders', path: '/orders', icon: <IconList size={18} /> });
  }

  const isActive = (path) => location.pathname === path;

  return (
    <MantineProvider>
      <AppShell
        header={{ height: 60 }}
        navbar={{ width: 200, breakpoint: 'sm', collapsed: { mobile: !navbarOpened } }}
        padding="md"
      >
        {/* Header */}
        <AppShell.Header>
          <Container size="xl" h="100%" px="md">
            <Flex h="100%" align="center" justify="space-between">
              <Group gap="md">
                <ActionIcon onClick={toggleNavbar} variant="default" size="md" display={{ sm: 'none' }}>
                  {navbarOpened ? <IconX size={18} /> : <IconMenu2 size={18} />}
                </ActionIcon>
                <Text fw={700} size="xl" c="blue" onClick={() => navigate('/')}>
                  🛒 FastMart
                </Text>
              </Group>

              <Group>
                {isAuthenticated ? (
                  <Menu>
                    <Menu.Target>
                      <Button variant="subtle" leftSection={<Avatar size="sm" color="blue" radius="xl">
                        {(user?.firstName || user?.username || 'U')[0]}
                      </Avatar>}>
                        {user?.firstName || user?.username || 'User'}
                      </Button>
                    </Menu.Target>
                    <Menu.Dropdown>
                      <Menu.Item onClick={() => navigate('/profile')}>Profile</Menu.Item>
                      <Menu.Item onClick={() => navigate('/orders')}>Orders</Menu.Item>
                      <Menu.Item color="red" leftSection={<IconLogout size={14} />} onClick={logout}>
                        Logout
                      </Menu.Item>
                    </Menu.Dropdown>
                  </Menu>
                ) : (
                  <Button onClick={() => navigate('/login')}>Login</Button>
                )}
              </Group>
            </Flex>
          </Container>
        </AppShell.Header>

        {/* Navbar */}
        <AppShell.Navbar p="md">
          <Flex direction="column" gap="xs" h="100%" justify="space-between">
            <Flex direction="column" gap="xs">
              {navItems.map((item) => (
                <Button
                  key={item.path}
                  variant={isActive(item.path) ? 'light' : 'subtle'}
                  leftSection={item.icon}
                  onClick={() => {
                    navigate(item.path);
                    if (window.innerWidth < 640) toggleNavbar();
                  }}
                  fullWidth
                  size="md"
                >
                  {item.label}
                </Button>
              ))}
            </Flex>

            <Text size="xs" c="dimmed" ta="center">
              FastMart © 2025
            </Text>
          </Flex>
        </AppShell.Navbar>

        {/* Main Content */}
        <AppShell.Main>
          <Container size="xl" py="xl">
            <Outlet />
          </Container>
        </AppShell.Main>
      </AppShell>
    </MantineProvider>
  );
};

export default MainLayout;
