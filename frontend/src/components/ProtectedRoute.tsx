import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const ProtectedRoute: React.FC = () => {
    const { currentUser } = useAuth();

    if (!currentUser) {
        // User not logged in, redirect to login page
        return <Navigate to="/login" replace />;
    }

    // User is logged in, render the child route component
    return <Outlet />;
};

export default ProtectedRoute; 