import { Routes, Route } from 'react-router-dom';
import WorkflowListPage from './pages/WorkflowListPage';
import LoginPage from './pages/LoginPage'; // Import LoginPage
import ProtectedRoute from './components/ProtectedRoute'; // Import ProtectedRoute
import { useAuth } from './context/AuthContext'; // Import useAuth
import { auth } from './firebaseConfig'; // Import auth for signout
import { signOut } from 'firebase/auth'; // Import signOut

function App() {
  const { currentUser } = useAuth();

  const handleSignOut = async () => {
    try {
      await signOut(auth);
      // Redirect to login or handle post-signout logic if needed
      // navigate('/login'); // Requires importing useNavigate
    } catch (error) {
      console.error("Error signing out:", error);
    }
  };

  return (
    <div className="min-h-screen bg-gray-100">
      <nav className="bg-blue-600 p-4 text-white flex justify-between items-center">
        <h1 className="text-xl font-semibold">SaaS Automation Builder</h1>
        {currentUser && (
          <button 
            onClick={handleSignOut}
            className="bg-red-500 hover:bg-red-700 text-white font-bold py-1 px-3 rounded text-sm"
          >
            Sign Out
          </button>
        )}
      </nav>

      <main className="p-4">
        <Routes>
          <Route path="/login" element={<LoginPage />} />
          <Route path="/" element={<ProtectedRoute />}> {/* Wrap protected routes */} 
            <Route index element={<WorkflowListPage />} /> {/* Default protected route */} 
             {/* Add other protected routes here inside ProtectedRoute */}
          </Route>
        </Routes>
      </main>
    </div>
  );
}

export default App;
