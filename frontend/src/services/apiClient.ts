import { CreateChatRequest, CreateChatResponse, GetUserChatsResponse, MessageHistoryResponse, RegisterRequest, RegisterResponse, SendMessageRequest, SendMessageResponse } from "@/types/api";

const BASE_URL = "http://localhost:8080/api/v1";

async function fetchClient<T>(endpoint: string, method: string = "GET", body?: any): Promise<T> {
  const options: RequestInit = {
    method,
    headers: { "Content-Type": "application/json" },
  };

  if (body) {
    options.body = JSON.stringify(body);
  }

  const response = await fetch(`${BASE_URL}${endpoint}`, options);

  if (!response.ok) {
    const errorText = await response.text();
    throw new Error(errorText || `API request failed with status ${response.status}`);
  }

  return response.json();
}

export const identityApi = {
  register: (data: RegisterRequest) => 
    fetchClient<RegisterResponse>("/users/register", "POST", data),
};

export const authApi = {
  login: (data: { phoneNumber: string }) => 
    fetchClient<RegisterResponse>("/users/login", "POST", data),
};

export const messagingApi = {
  createChat: (data: CreateChatRequest) => 
    fetchClient<CreateChatResponse>("/chats", "POST", data),

  sendMessage: (chatId: string, data: SendMessageRequest) => 
    fetchClient<SendMessageResponse>(`/chats/${chatId}/messages`, "POST", data),

  // Notice that userId is passed as a query parameter (?userId=...) just like the backend design
  getHistory: (chatId: string, userId: string) => 
    fetchClient<MessageHistoryResponse[]>(`/chats/${chatId}/messages?userId=${userId}`, "GET"),

  getUserChats: (userId: string) =>
    fetchClient<GetUserChatsResponse[]>(`/users/${userId}/chats`, "GET"),
};