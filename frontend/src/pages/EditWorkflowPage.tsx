import React, { useState, useEffect, useCallback } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import axiosInstance from '../api/axiosInstance';
import WorkflowForm, { WorkflowFormData } from '../components/WorkflowForm';

// We need the Workflow DTO interface here too, or define it globally
interface WorkflowDto {
    id: number;
    name: string;
    description?: string;
    enabled: boolean;
    triggerConfig: string;
    actionConfig: string;
    // userId, createdAt, updatedAt might not be needed in the form itself
}

const EditWorkflowPage: React.FC = () => {
    const { workflowId } = useParams<{ workflowId: string }>(); // Get ID from URL param
    const navigate = useNavigate();
    const [initialData, setInitialData] = useState<Partial<WorkflowFormData> | null>(null);
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    // Fetch existing workflow data
    const fetchWorkflow = useCallback(async () => {
        if (!workflowId) return;
        setLoading(true);
        setError(null);
        try {
            const response = await axiosInstance.get<WorkflowDto>(`/v1/workflows/${workflowId}`);
            // Prepare data for the form (map DTO to FormData)
            setInitialData({
                name: response.data.name,
                description: response.data.description ?? '',
                enabled: response.data.enabled,
                triggerConfig: response.data.triggerConfig,
                actionConfig: response.data.actionConfig
            });
        } catch (err: any) {
            console.error(`Error fetching workflow ${workflowId}:`, err);
            setError("Failed to load workflow data. It might not exist or you don't have permission.");
        } finally {
            setLoading(false);
        }
    }, [workflowId]);

    useEffect(() => {
        fetchWorkflow();
    }, [fetchWorkflow]);

    const handleUpdateSubmit = async (data: WorkflowFormData) => {
        if (!workflowId) return; // Should not happen

        setIsSubmitting(true);
        setError(null);
        try {
            // Use the backend PUT endpoint
            await axiosInstance.put(`/v1/workflows/${workflowId}`, data);
            navigate('/'); // Navigate back to the list on success
        } catch (err: any) {
            console.error(`Error updating workflow ${workflowId}:`, err);
            setError("Failed to update workflow. Please check your input and ensure the backend is running.");
            setIsSubmitting(false);
        }
    };

    const handleCancel = () => {
        navigate('/'); // Navigate back to the list page
    };

    if (loading) {
        return <p className="text-center mt-8">Loading workflow data...</p>;
    }

    if (error && !initialData) {
        // Show error only if loading failed AND we have no data
        return (
             <div className="container mx-auto p-4">
                <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative max-w-lg mx-auto text-center" role="alert">
                    <strong className="font-bold">Error:</strong>
                    <span className="block sm:inline"> {error}</span>
                    <button onClick={() => navigate('/')} className="mt-4 bg-gray-300 hover:bg-gray-400 text-gray-800 font-bold py-1 px-3 rounded">Back to List</button>
                </div>
             </div>
        );
    }
    
    if (!initialData) { 
        // Should ideally not happen if loading is finished and no error, but good practice
        return <p className="text-center mt-8">Workflow data not available.</p>;
    }

    return (
        <div className="container mx-auto p-4">
            <h2 className="text-2xl font-bold mb-6 text-center">Edit Workflow</h2>
            {/* Display submission errors if they occur after loading */}
             {error && (
                <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative mb-4 max-w-lg mx-auto" role="alert">
                    <strong className="font-bold">Error:</strong>
                    <span className="block sm:inline"> {error}</span>
                </div>
            )}
            <WorkflowForm 
                initialData={initialData} 
                onSubmit={handleUpdateSubmit}
                onCancel={handleCancel}
                isSubmitting={isSubmitting}
                submitButtonText="Update Workflow"
            />
        </div>
    );
};

export default EditWorkflowPage; 