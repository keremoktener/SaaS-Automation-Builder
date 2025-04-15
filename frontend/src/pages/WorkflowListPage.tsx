import React, { useState, useEffect, useCallback } from 'react';
// Use the configured axios instance
import axiosInstance from '../api/axiosInstance'; 
import { useAuth } from '../context/AuthContext'; // Import useAuth
import { useNavigate } from 'react-router-dom'; // Import for navigation (optional for edit)

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
    const navigate = useNavigate(); // For navigating to create/edit pages later
    const [workflows, setWorkflows] = useState<WorkflowDto[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);

    // Remove temporary user ID
    // const TEMP_USER_ID = 1;

    // Fetch workflows function using useCallback for stability
    const fetchWorkflows = useCallback(async () => {
        if (!currentUser) return; // Should be handled by ProtectedRoute

        setLoading(true);
        setError(null);
        try {
            // Use the new endpoint that relies on the authenticated user token
            const response = await axiosInstance.get<WorkflowDto[]>('/v1/workflows');
            setWorkflows(response.data);
        } catch (err: any) {
            console.error("Error fetching workflows:", err);
            if (err.response && (err.response.status === 403 || err.response.status === 401)) {
                setError("Authentication error. Please try logging in again.");
            } else {
                setError("Failed to fetch workflows. Please ensure the backend is running.");
            }
        } finally {
            setLoading(false);
        }
    }, [currentUser]);

    useEffect(() => {
        fetchWorkflows();
    }, [fetchWorkflows]); // Run fetchWorkflows when component mounts or function changes

    // --- Action Handlers ---

    const handleCreateWorkflow = () => {
        // TODO: Navigate to a dedicated create workflow page or open a modal
        console.log("Navigate to Create Workflow page...");
        // navigate('/workflows/new'); // Example navigation
        alert('Create workflow functionality not yet implemented.');
    };

    const handleEditWorkflow = (workflowId: number) => {
        // TODO: Navigate to an edit page for this workflow
        console.log(`Navigate to Edit Workflow page for ID: ${workflowId}`);
        // navigate(`/workflows/${workflowId}/edit`); // Example navigation
        alert(`Edit workflow ${workflowId} functionality not yet implemented.`);
    };

    const handleDeleteWorkflow = async (workflowId: number) => {
        // Simple confirmation dialog
        if (!window.confirm("Are you sure you want to delete this workflow?")) {
            return;
        }

        setError(null); // Clear previous errors
        try {
            await axiosInstance.delete(`/v1/workflows/${workflowId}`);
            // Refresh the list after successful deletion
            // Option 1: Refetch all workflows
            fetchWorkflows(); 
            // Option 2: Filter out the deleted workflow locally (faster UI update)
            // setWorkflows(prevWorkflows => prevWorkflows.filter(wf => wf.id !== workflowId));
            
            alert(`Workflow ${workflowId} deleted successfully.`);
        } catch (err: any) {
            console.error(`Error deleting workflow ${workflowId}:`, err);
            setError(`Failed to delete workflow ${workflowId}.`);
        }
    };

    // --- Rendering ---

    return (
        <div className="container mx-auto p-4">
            <div className="flex justify-between items-center mb-6">
                <h2 className="text-2xl font-bold">My Workflows</h2>
                <button
                    onClick={handleCreateWorkflow}
                    className="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded"
                >
                    + Create New Workflow
                </button>
            </div>

            {currentUser && <p className="mb-4 text-sm text-gray-600">Logged in as: {currentUser.email}</p>}

            {loading && <p>Loading workflows...</p>}
            
            {error && (
                <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative mb-4" role="alert">
                    <strong className="font-bold">Error:</strong>
                    <span className="block sm:inline"> {error}</span>
                </div>
            )}

            {!loading && (
                <div className="bg-white shadow-md rounded-lg overflow-hidden">
                    {workflows.length === 0 && !error ? (
                        <p className="p-4 text-gray-500">No workflows found. Get started by creating one!</p>
                    ) : (
                        <ul className="divide-y divide-gray-200">
                            {workflows.map((workflow) => (
                                <li key={workflow.id} className="p-4 hover:bg-gray-50 flex flex-col sm:flex-row justify-between items-start sm:items-center">
                                    <div className="flex-grow mb-2 sm:mb-0">
                                        <h3 className="text-lg font-semibold text-gray-800">{workflow.name}</h3>
                                        {workflow.description && <p className="text-sm text-gray-600 mt-1">{workflow.description}</p>}
                                        <p className={`text-xs mt-2 font-medium ${workflow.enabled ? 'text-green-600' : 'text-gray-500'}`}>
                                            Status: {workflow.enabled ? 'Enabled' : 'Disabled'}
                                        </p>
                                    </div>
                                    <div className="flex-shrink-0 flex space-x-2 mt-2 sm:mt-0">
                                        <button
                                            onClick={() => handleEditWorkflow(workflow.id)}
                                            className="text-sm bg-yellow-500 hover:bg-yellow-600 text-white font-semibold py-1 px-3 rounded"
                                        >
                                            Edit
                                        </button>
                                        <button
                                            onClick={() => handleDeleteWorkflow(workflow.id)}
                                            className="text-sm bg-red-500 hover:bg-red-600 text-white font-semibold py-1 px-3 rounded"
                                        >
                                            Delete
                                        </button>
                                    </div>
                                </li>
                            ))}
                        </ul>
                    )}
                </div>
            )}
        </div>
    );
};

export default WorkflowListPage; 