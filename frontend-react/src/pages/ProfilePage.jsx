import { useState, useEffect } from 'react';
import {
  Paper,
  Title,
  Text,
  TextInput,
  Button,
  Stack,
  Divider,
  Group,
  Alert,
  Badge,
} from '@mantine/core';
import { IconAlertCircle } from '@tabler/icons-react';
import { useForm } from '@mantine/form';
import useAuthStore from '../store/authStore';
import authService from '../services/authService';

const ProfilePage = () => {
  const { user, fetchUser } = useAuthStore();
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(false);

  const form = useForm({
    initialValues: {
      firstName: user?.firstName || '',
      lastName: user?.lastName || '',
      email: user?.email || '',
    },
  });

  useEffect(() => {
    fetchUser();
  }, []);

  useEffect(() => {
    if (user) {
      form.setValues({
        firstName: user.firstName || '',
        lastName: user.lastName || '',
        email: user.email || '',
      });
      setLoading(false);
    }
  }, [user]);

  const handleUpdate = async () => {
    try {
      await authService.updateProfile(user?.username, form.values);
      setSuccess(true);
      setTimeout(() => setSuccess(false), 3000);
      fetchUser();
    } catch (err) {
      setError(err.message);
    }
  };

  if (loading) return <Text>Loading...</Text>;

  return (
    <Stack gap="xl">
      <Title order={2}>Profile</Title>

      {error && (
        <Alert icon={<IconAlertCircle size={16} />} color="red">
          {error}
        </Alert>
      )}
      {success && (
        <Alert color="green">Profile updated successfully!</Alert>
      )}

      <Paper withBorder p="xl" radius="md">
        <Title order={3}>Personal Information</Title>
        <Divider my="md" />
        <form onSubmit={form.onSubmit(handleUpdate)}>
          <Stack gap="md">
            <TextInput
              label="Username"
              value={user?.username || ''}
              disabled
            />
            <TextInput
              label="Email"
              {...form.getInputProps('email')}
            />
            <Group grow>
              <TextInput
                label="First Name"
                {...form.getInputProps('firstName')}
              />
              <TextInput
                label="Last Name"
                {...form.getInputProps('lastName')}
              />
            </Group>
            <Button type="submit">Update Profile</Button>
          </Stack>
        </form>
      </Paper>

      <Paper withBorder p="xl" radius="md">
        <Title order={3}>Account</Title>
        <Divider my="md" />
        <Stack gap="md">
          <Group justify="space-between">
            <Text>Account Status</Text>
            <Badge color="green">Active</Badge>
          </Group>
          <Group justify="space-between">
            <Text>Member Since</Text>
            <Text>{user?.createdAt ? new Date(user.createdAt).toLocaleDateString() : 'N/A'}</Text>
          </Group>
        </Stack>
      </Paper>
    </Stack>
  );
};

export default ProfilePage;
