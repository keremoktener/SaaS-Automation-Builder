import { initializeApp } from "firebase/app";
import { getAuth } from "firebase/auth";

// TODO: Replace with your actual Firebase project configuration
// IMPORTANT: Use environment variables for sensitive keys in a real application
const firebaseConfig = {
  apiKey: "AIzaSyDDjKQogkrL_FTpBapXEdZWVwqaAhb4wZ4",
  authDomain: "saas-automation-builder-c9bb7.firebaseapp.com",
  projectId: "saas-automation-builder-c9bb7",
  storageBucket: "saas-automation-builder-c9bb7.firebasestorage.app",
  messagingSenderId: "575361638064",
  appId: "1:575361638064:web:ce8b344a170b509f42a575",
    measurementId: "G-2RL4PWWEWC"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);

// Initialize Firebase Authentication and get a reference to the service
export const auth = getAuth(app); 