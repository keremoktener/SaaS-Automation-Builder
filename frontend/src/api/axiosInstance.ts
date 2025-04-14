import axios from 'axios';
import { auth } from '../firebaseConfig'; // Your Firebase auth instance

const axiosInstance = axios.create({
  baseURL: 'http://localhost:8080/api' // Base URL for your backend API
});

// Add a request interceptor to include the Firebase ID token
axiosInstance.interceptors.request.use(async (config) => {
  const currentUser = auth.currentUser;
  if (currentUser) {
    try {
      const token = await currentUser.getIdToken(true); // Force refresh token if needed
      config.headers.Authorization = `Bearer ${token}`;
    } catch (error) {
      console.error("Error getting Firebase ID token:", error);
      // Handle error appropriately - maybe redirect to login?
    }
  }
  return config;
}, (error) => {
  return Promise.reject(error);
});

export default axiosInstance; 