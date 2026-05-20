import React from 'react';
import ReactDOM from 'react-dom/client';
import { RouterProvider } from 'react-router-dom';
import { ChakraProvider, extendTheme } from '@chakra-ui/react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { ReactQueryDevtools } from '@tanstack/react-query-devtools';
import './index.css';
import './i18n';
import { router } from './router';

const queryClient = new QueryClient({
  defaultOptions: {
    queries: { refetchOnWindowFocus: false, retry: 1 },
  },
});

const theme = extendTheme({
  styles: {
    global: {
      body: { bg: '#fffaf5', color: 'gray.800' },
    },
  },
  colors: {
    brand: {
      50:  '#e6fffa',
      100: '#b2f5ea',
      200: '#81e6d9',
      300: '#4fd1c5',
      400: '#38b2ac',
      500: '#2C7A7B',
      600: '#285E61',
      700: '#234E52',
      800: '#1D4044',
      900: '#133b3c',
    },
  },
  fonts: {
    heading: `'Inter', system-ui, sans-serif`,
    body:    `'Inter', system-ui, sans-serif`,
  },
  components: {
    Button: {
      defaultProps: { colorScheme: 'teal' },
      baseStyle: { fontWeight: 600, borderRadius: 'xl' },
    },
    Card: {
      baseStyle: {
        container: { borderRadius: '2xl', overflow: 'hidden' },
      },
    },
  },
});

ReactDOM.createRoot(document.getElementById('root') as HTMLElement).render(
  <React.StrictMode>
    <ChakraProvider theme={theme}>
      <QueryClientProvider client={queryClient}>
        <RouterProvider router={router} />
        <ReactQueryDevtools initialIsOpen={false} />
      </QueryClientProvider>
    </ChakraProvider>
  </React.StrictMode>,
);
