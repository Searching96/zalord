"use client"; // Required because we are using client-side hooks (useState)
import { identityApi } from "@/services/apiClient";
import { RegisterResponse } from "@/types/api";
import { useState } from "react";


export default function Home() {
  const [phoneNumber, setPhoneNumber] = useState("");
  const [displayName, setDisplayName] = useState("");
  const [result, setResult] = useState<RegisterResponse | null>(null);
  const [error, setError] = useState("");

  const handleRegister = async (e: React.SubmitEvent<HTMLFormElement>) => {
    e.preventDefault()
    setError("");
    setResult(null);

    try {
      const response = await identityApi.register({ phoneNumber, displayName });
      setResult(response);
    } catch (err: any) {
      setError(err.message);
    }
  };

  return (
    <main className="flex min-h-screen items-center justify-center bg-gray-100">
      <div className="w-full max-w-md bg-white p-8 rounded-lg shadow-md">
        <h1 className="text-2xl font-bold mb-6 text-gray-800">Zalord Registration</h1>
        
        <form onSubmit={handleRegister} className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700">Phone Number</label>
            <input
              type="text"
              value={phoneNumber}
              onChange={(e) => setPhoneNumber(e.target.value)}
              className="mt-1 block w-full rounded-md border-gray-300 shadow-sm border p-2 focus:ring-blue-500 focus:border-blue-500 text-black"
              required
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700">Display Name</label>
            <input
              type="text"
              value={displayName}
              onChange={(e) => setDisplayName(e.target.value)}
              className="mt-1 block w-full rounded-md border-gray-300 shadow-sm border p-2 focus:ring-blue-500 focus:border-blue-500 text-black"
              required
            />
          </div>

          <button
            type="submit"
            className="w-full bg-blue-600 text-white p-2 rounded-md hover:bg-blue-700 transition"
          >
            Register User
          </button>
        </form>

        {error && (
          <div className="mt-4 p-3 bg-red-100 text-red-700 rounded-md text-sm">
            {error}
          </div>
        )}

        {result && (
          <div className="mt-4 p-3 bg-green-100 text-green-800 rounded-md text-sm">
            <p><strong>Success!</strong></p>
            <p>ID: <span className="font-mono text-xs">{result.id}</span></p>
            <p>Name: {result.displayName}</p>
          </div>
        )}
      </div>
    </main>
  );
}