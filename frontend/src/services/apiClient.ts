import { RegisterRequest, RegisterResponse } from "@/types/api";

const BASE_URL = "http://localhost:8080/api/v1";

export const identityApi = {
  register: async (data: RegisterRequest): Promise<RegisterResponse> => {
    const response = await fetch(`${BASE_URL}/users/register`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(data)
    });

    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(errorText || "Failed to register");
    }

    return response.json();
  },
};