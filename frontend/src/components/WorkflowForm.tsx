import React from 'react';

// Define the shape of the data the form handles (matches create/update DTOs)
export interface WorkflowFormData {
    name: string;
    description: string; // Use empty string for null/undefined
    enabled: boolean;
    triggerConfig: string;
    actionConfig: string;
}

interface WorkflowFormProps {
    initialData?: Partial<WorkflowFormData>; // Optional initial data for editing
    onSubmit: (data: WorkflowFormData) => void;
    onCancel: () => void;
    isSubmitting: boolean;
    submitButtonText?: string;
}

const WorkflowForm: React.FC<WorkflowFormProps> = ({
    initialData, // Receive initialData directly
    onSubmit,
    onCancel,
    isSubmitting,
    submitButtonText = 'Save Workflow'
}) => {
    // Initialize state directly from initialData, providing defaults
    const [formData, setFormData] = React.useState<WorkflowFormData>(() => ({
        name: initialData?.name ?? '',
        description: initialData?.description ?? '',
        enabled: initialData?.enabled ?? false,
        triggerConfig: initialData?.triggerConfig ?? '',
        actionConfig: initialData?.actionConfig ?? ''
    }));

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
        const { name, value, type } = e.target;
        const target = e.target; 

        if (type === 'checkbox' && target instanceof HTMLInputElement) {
            setFormData(prev => ({ ...prev, [name]: target.checked })); 
        } else {
            setFormData(prev => ({ ...prev, [name]: value }));
        }
    };

    const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        onSubmit(formData);
    };

    return (
        <form onSubmit={handleSubmit} className="space-y-4 max-w-lg mx-auto p-6 bg-white rounded shadow-md">
            <div>
                <label htmlFor="name" className="block text-sm font-medium text-gray-700 mb-1">Name <span className="text-red-500">*</span></label>
                <input
                    type="text"
                    id="name"
                    name="name"
                    value={formData.name}
                    onChange={handleChange}
                    required
                    className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                />
            </div>

            <div>
                <label htmlFor="description" className="block text-sm font-medium text-gray-700 mb-1">Description</label>
                <textarea
                    id="description"
                    name="description"
                    rows={3}
                    value={formData.description}
                    onChange={handleChange}
                    className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                />
            </div>

            <div className="flex items-center">
                <input
                    id="enabled"
                    name="enabled"
                    type="checkbox"
                    checked={formData.enabled}
                    onChange={handleChange}
                    className="h-4 w-4 text-indigo-600 focus:ring-indigo-500 border-gray-300 rounded mr-2"
                />
                <label htmlFor="enabled" className="text-sm font-medium text-gray-700">Enabled</label>
            </div>

            {/* TODO: Replace textareas with more structured editors (e.g., JSON editor) */}
            <div>
                <label htmlFor="triggerConfig" className="block text-sm font-medium text-gray-700 mb-1">Trigger Configuration (JSON) <span className="text-red-500">*</span></label>
                <textarea
                    id="triggerConfig"
                    name="triggerConfig"
                    rows={5}
                    value={formData.triggerConfig}
                    onChange={handleChange}
                    required
                    placeholder='{
  "type": "schedule",
  "cron": "0 * * * *"
}'
                    className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm font-mono"
                />
            </div>

            <div>
                <label htmlFor="actionConfig" className="block text-sm font-medium text-gray-700 mb-1">Action Configuration (JSON) <span className="text-red-500">*</span></label>
                <textarea
                    id="actionConfig"
                    name="actionConfig"
                    rows={5}
                    value={formData.actionConfig}
                    onChange={handleChange}
                    required
                    placeholder='[
  {
    "type": "log",
    "message": "Workflow triggered!"
  }
]'
                    className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm font-mono"
                />
            </div>

            <div className="flex justify-end space-x-3 pt-4">
                <button
                    type="button"
                    onClick={onCancel}
                    disabled={isSubmitting}
                    className="bg-gray-200 hover:bg-gray-300 text-gray-800 font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline disabled:opacity-50"
                >
                    Cancel
                </button>
                <button
                    type="submit"
                    disabled={isSubmitting}
                    className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline disabled:opacity-50"
                >
                    {isSubmitting ? 'Saving...' : submitButtonText}
                </button>
            </div>
        </form>
    );
};

export default WorkflowForm; 