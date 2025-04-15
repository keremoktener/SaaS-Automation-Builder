import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axiosInstance from '../api/axiosInstance';
import WorkflowForm, { WorkflowFormData } from '../components/WorkflowForm';

const CreateWorkflowPage: React.FC = () => {
    const navigate = useNavigate();
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const handleCreateSubmit = async (data: WorkflowFormData) => {
        setIsSubmitting(true);
        setError(null);
        try {
            // Use the backend POST endpoint
            await axiosInstance.post('/v1/workflows', data);
            navigate('/'); // Navigate back to the list on success
            // Optional: Add success message/toast
        } catch (err: any) {
            console.error("Error creating workflow:", err);
            setError("Failed to create workflow. Please check your input and ensure the backend is running.");
            setIsSubmitting(false);
        }
        // No need to set isSubmitting to false on success due to navigation
    };

    const handleCancel = () => {
        navigate('/'); // Navigate back to the list page
    };

    return (
        <div className="container mx-auto p-4">
            <h2 className="text-2xl font-bold mb-6 text-center">Create New Workflow</h2>
            {error && (
                <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative mb-4 max-w-lg mx-auto" role="alert">
                    <strong className="font-bold">Error:</strong>
                    <span className="block sm:inline"> {error}</span>
                </div>
            )}
            <WorkflowForm 
                onSubmit={handleCreateSubmit}
                onCancel={handleCancel}
                isSubmitting={isSubmitting}
                submitButtonText="Create Workflow"
            />
        </div>
    );
};

export default CreateWorkflowPage; 