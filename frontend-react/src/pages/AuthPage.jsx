import { useState } from 'react';
import { useNavigate, Links } from 'react-router-dom';
import {
  Paper,
  Title,
  Text,
  TextInput,
  PasswordInput,
  Button,
  Group,
  Stack,
  Divider,
  Tabs,
  Alert,
  Flex,
  Container,
} from '@mantine/core';
import { IconAlertCircle } from '@tabler/icons-react';
import { useForm } from '@mantine/form';
import useAuthStore from '../store/authStore';

const AuthPage = () => {
  const navigate = useNavigate();
  const { signin, signup, error, isLoading } = useAuthStore();
  const [activeTab, setActiveTab] = useState('login');

  const loginForm = useForm({
    initialValues: { username: '', password: '' },
    validate: {
      username: (val) => (val.length >= 3 ? null : 'Username must be at least 3 characters'),
      password: (val) => (val.length >= 6 ? null : 'Password must be at least 6 characters'),
    },
  });

  const registerForm = useForm({
    initialValues: {
      username: '',
      email: '',
      firstName: '',
      lastName: '',
      password: '',
      confirmPassword: '',
    },
    validate: {
      username: (val) => (val.length >= 3 ? null : 'Username must be at least 3 characters'),
      email: (val) => (/^\S+@\S+$/.test(val) ? null : 'Invalid email'),
      password: (val) => (val.length >= 6 ? null : 'Password must be at least 6 characters'),
      confirmPassword: (val, values) =>
        val === values.password ? null : 'Passwords do not match',
    },
  });

  const handleLogin = async (values) => {
    const result = await signin(values);
    if (result.success) {
      navigate('/');
    }
  };

  const handleRegister = async (values) => {
    const result = await signup(values);
    if (result.success) {
      navigate('/');
    }
  };

  return (
    <Container size={420} py="xl">
      <Paper withBorder p="xl" radius="md">
        <Title ta="center" mb="md">
          Welcome to FastMart
        </Title>

        {error && (
          <Alert icon={<IconAlertCircle size={16} />} color="red" mb="md" radius="md">
            {error}
          </Alert>
        )}

        <Tabs value={activeTab} onChange={setActiveTab}>
          <Tabs.List justify="center" mb="lg">
            <Tabs.Tab value="login">Login</Tabs.Tab>
            <Tabs.Tab value="register">Register</Tabs.Tab>
          </Tabs.List>

          <Tabs.Panel value="login">
            <form onSubmit={loginForm.onSubmit(handleLogin)}>
              <Stack gap="md">
                <TextInput
                  label="Username"
                  placeholder="Enter your username"
                  {...loginForm.getInputProps('username')}
                  required
                />
                <PasswordInput
                  label="Password"
                  placeholder="Enter your password"
                  {...loginForm.getInputProps('password')}
                  required
                />
                <Button type="submit" loading={isLoading} fullWidth>
                  Sign In
                </Button>
              </Stack>
            </form>
          </Tabs.Panel>

          <Tabs.Panel value="register">
            <form onSubmit={registerForm.onSubmit(handleRegister)}>
              <Stack gap="md">
                <TextInput
                  label="Username"
                  placeholder="Choose a username"
                  {...registerForm.getInputProps('username')}
                  required
                />
                <TextInput
                  label="Email"
                  placeholder="your@email.com"
                  {...registerForm.getInputProps('email')}
                  required
                />
                <Group grow>
                  <TextInput
                    label="First Name"
                    {...registerForm.getInputProps('firstName')}
                    required
                  />
                  <TextInput
                    label="Last Name"
                    {...registerForm.getInputProps('lastName')}
                    required
                  />
                </Group>
                <PasswordInput
                  label="Password"
                  placeholder="Create a password"
                  {...registerForm.getInputProps('password')}
                  required
                />
                <PasswordInput
                  label="Confirm Password"
                  placeholder="Confirm your password"
                  {...registerForm.getInputProps('confirmPassword')}
                  required
                />
                <Button type="submit" loading={isLoading} fullWidth>
                  Create Account
                </Button>
              </Stack>
            </form>
          </Tabs.Panel>
        </Tabs>
      </Paper>
    </Container>
  );
};

export default AuthPage;
