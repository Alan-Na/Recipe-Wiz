import axios from 'axios';

const BASE_URL = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080';

export const apiClient = axios.create({
  baseURL: BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
  // 90 s — matches the backend OkHttp read timeout so Edamam / DeepSeek
  // responses are never cut off on the frontend side first.
  timeout: 90_000,
});
