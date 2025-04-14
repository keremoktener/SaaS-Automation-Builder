import React, { useState, useEffect } from 'react';
// Use the configured axios instance
import axiosInstance from '../api/axiosInstance'; 
import { useAuth } from '../context/AuthContext'; // Import useAuth

// Define an interface for the Workflow DTO (matching backend DTO)
interface WorkflowDto {
    id: number;
    name: string;
    description?: string;
    enabled: boolean;
    triggerConfig: string;
    actionConfig: string;
    userId: number;
    createdAt: string; // Represent Instant as string for simplicity
    updatedAt: string;
}

const WorkflowListPage: React.FC = () => {
    const { currentUser } = useAuth(); // Get the current user
    const [workflows, setWorkflows] = useState<WorkflowDto[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);

    // Remove temporary user ID
    // const TEMP_USER_ID = 1;

    useEffect(() => {
        if (!currentUser) {
            // Should not happen if ProtectedRoute works, but good practice
            setError("Not authenticated");
            setLoading(false);
            return;
        }

        const fetchWorkflows = async () => {
            setLoading(true);
            setError(null);
            try {
                // TODO: Backend endpoint needs to be updated to use authenticated user
                //       instead of user ID in path. For now, we still need a placeholder ID.
                //       Ideally, the backend gets the user from the validated token.
                //       Let's assume user 1 for now, but fetch will include token.
                const TEMP_USER_ID_FOR_API = 1;
                const response = await axiosInstance.get<WorkflowDto[]>(`/v1/workflows/user/${TEMP_USER_ID_FOR_API}`);
                setWorkflows(response.data);
            } catch (err: any) {
                console.error("Error fetching workflows:", err);
                // Check for specific error types if needed
                if (err.response && err.response.status === 403) {
                    setError("Authentication error. Please try logging in again.");
                } else {
                    setError("Failed to fetch workflows. Please ensure the backend is running and the user exists.");
                }
            } finally {
                setLoading(false);
            }
        };

        fetchWorkflows();
    // Depend on currentUser to refetch if user changes (e.g., logs in/out)
    }, [currentUser]); 

    return (
        <div className="container mx-auto">
            <h2 className="text-2xl font-bold mb-4">My Workflows</h2>
            {currentUser && <p className="mb-2 text-sm text-gray-600">Logged in as: {currentUser.email}</p>}

            {/* TODO: Add a button to create a new workflow */}

            {loading && <p>Loading workflows...</p>}
            {error && <p className="text-red-500">{error}</p>}

            {!loading && !error && (
                <ul className="space-y-4">
                    {workflows.length === 0 ? (
                        <p>No workflows found.</p>
                    ) : (
                        workflows.map((workflow) => (
                            <li key={workflow.id} className="p-4 bg-white rounded shadow">
                                <h3 className="text-lg font-semibold">{workflow.name}</h3>
                                {workflow.description && <p className="text-gray-600">{workflow.description}</p>}
                                <p className="text-sm text-gray-500">Status: {workflow.enabled ? 'Enabled' : 'Disabled'}</p>
                                {/* TODO: Add buttons for edit/delete/toggle */} 
                            </li>
                        ))
                    )}
                </ul>
            )}
        </div>
    );
};

export default WorkflowListPage; 