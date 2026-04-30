import { CreateChatRequest, CreateChatResponse, FindDmChatResponse, GetUserChatsResponse, MessageHistoryResponse, RegisterRequest, RegisterResponse, SearchUserResponse, SendMessageRequest, SendMessageResponse } from "@/types/api";

const BASE_URL = "http://localhost:8080/api/v1";

export class ApiError extends Error {
  public status: number;

  constructor(message: string, status: number) {
    super(message);
    this.name = "ApiError";
    this.status = status;
  }
}

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
    throw new ApiError(errorText || `HTTP error! status: ${response.status}`, response.status);
  }

  // Safely parse successful responses in case the backend may returns no content.
  // e.g., 204 No Content.
  const textResponse = await response.text();
  
  // If the backend sent a body, parse it. Otherwise, return an empty object to prevent c-
  // rashes that may come from destructuring or other operations.
  return textResponse ? JSON.parse(textResponse) : ({} as T);
}

export const identityApi = {
  register: (data: RegisterRequest) => 
    fetchClient<RegisterResponse>("/users/register", "POST", data),

  searchByPhone: (phoneNumber: string) =>
    fetchClient<SearchUserResponse>(`/users/search?phoneNumber=${encodeURIComponent(phoneNumber)}`, "GET"),
};

export const authApi = {
  login: (data: { phoneNumber: string }) => 
    fetchClient<RegisterResponse>("/users/login", "POST", data),
};

export const messagingApi = {
  findDmChat: (userA: string, userB: string) =>
    fetchClient<FindDmChatResponse>(`/chats/dm?userA=${userA}&userB=${userB}`, "GET"),

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