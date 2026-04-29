"use client"; // Required because we are using client-side hooks (useState)

import { identityApi, authApi } from "@/services/apiClient";
import { useState } from "react";
import { useRouter } from "next/navigation";

export default function AuthPage() {
  const router = useRouter();
  
  // UI State
  const [isLoginView, setIsLoginView] = useState(true);
  const [error, setError] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [successMsg, setSuccessMsg] = useState("");

  // Form State
  const [phoneNumber, setPhoneNumber] = useState("");
  const [displayName, setDisplayName] = useState("");

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setError("");
    setSuccessMsg("");
    setIsLoading(true);

    try {
      if (isLoginView) {
        // --- LOGIN FLOW ---
        const response = await authApi.login({ phoneNumber });
        
        // Save session
        localStorage.setItem("zalord_user_id", response.id);
        localStorage.setItem("zalord_display_name", response.displayName);
        
        // Route to dashboard
        router.push("/chat");
      } else {
        // --- REGISTER FLOW ---
        const response = await identityApi.register({ phoneNumber, displayName });
        
        // Clear form and switch to login view
        setSuccessMsg(`User ${response.displayName} created! You can now log in.`);
        setIsLoginView(true);
        setDisplayName("");
      }
    } catch (err: any) {
      setError(err.message || "An error occurred. Please try again.");
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <main className="flex min-h-screen items-center justify-center bg-gray-100 text-black">
      <div className="w-full max-w-md bg-white p-8 rounded-lg shadow-md">
        
        {/* Header Section */}
        <div className="text-center mb-6">
          <h1 className="text-3xl font-bold text-gray-800">Zalord</h1>
          <p className="text-gray-500 mt-2">
            {isLoginView ? "Sign in to your account" : "Create a new account"}
          </p>
        </div>
        
        {/* Notifications */}
        {error && (
          <div className="mb-4 p-3 bg-red-100 text-red-700 rounded-md text-sm border border-red-200">
            {error}
          </div>
        )}
        {successMsg && (
          <div className="mb-4 p-3 bg-green-100 text-green-800 rounded-md text-sm border border-green-200">
            {successMsg}
          </div>
        )}

        {/* The Form */}
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700">Phone Number</label>
            <input
              type="text"
              value={phoneNumber}
              onChange={(e) => setPhoneNumber(e.target.value)}
              placeholder="+84..."
              className="mt-1 block w-full rounded-md border-gray-300 shadow-sm border p-2 focus:ring-blue-500 focus:border-blue-500 outline-none"
              required
            />
          </div>

          {/* Only show Display Name if we are registering */}
          {!isLoginView && (
            <div className="animate-fade-in-down">
              <label className="block text-sm font-medium text-gray-700">Display Name</label>
              <input
                type="text"
                value={displayName}
                onChange={(e) => setDisplayName(e.target.value)}
                placeholder="John Doe"
                className="mt-1 block w-full rounded-md border-gray-300 shadow-sm border p-2 focus:ring-blue-500 focus:border-blue-500 outline-none"
                required={!isLoginView}
              />
            </div>
          )}

          <button
            type="submit"
            disabled={isLoading || !phoneNumber.trim()}
            className="w-full bg-blue-600 text-white p-2 rounded-md hover:bg-blue-700 transition disabled:opacity-50 mt-2 font-medium"
          >
            {isLoading 
              ? "Processing..." 
              : isLoginView ? "Login" : "Register"
            }
          </button>
        </form>

        {/* View Toggle */}
        <div className="mt-6 text-center text-sm">
          <button 
            onClick={() => {
              setIsLoginView(!isLoginView);
              setError("");
              setSuccessMsg("");
            }}
            className="text-blue-600 hover:underline font-medium"
          >
            {isLoginView 
              ? "Don't have an account? Register here." 
              : "Already have an account? Log in."
            }
          </button>
        </div>
        
      </div>
    </main>
  );
}