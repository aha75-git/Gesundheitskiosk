import './App.css'
import {BrowserRouter, Navigate, Route, Routes} from "react-router-dom";
import {AuthProvider, useAuth} from "./api/AuthContext.tsx";
import Login from "./components/Login.tsx";
import Register from "./components/Register.tsx";
import OAuthRedirect from "./components/OAuthRedirect.tsx";
import Dashboard from "./components/Dashboard.tsx";

const ProtectedRoute: React.FC<{ children: React.ReactNode }> = ({ children }) => {
    const { user, isLoading } = useAuth();

    if (isLoading) {
        return <div>Loading...</div>;
    }

    return user ? <>{children}</> : <Navigate to="/login" />;
};

function App() {

  return (
      <AuthProvider>
          <BrowserRouter>
              <div className="App">
                  <Routes>
                      <Route path="/login" element={<Login />} />
                      <Route path="/register" element={<Register />} />
                      <Route path="/oauth2/redirect" element={<OAuthRedirect />} />
                      <Route
                          path="/dashboard"
                          element={
                              <ProtectedRoute>
                                  <Dashboard />
                              </ProtectedRoute>
                          }
                      />
                      <Route path="/" element={<Navigate to="/dashboard" />} />
                  </Routes>
              </div>
          </BrowserRouter>
      </AuthProvider>
  )
}

export default App
