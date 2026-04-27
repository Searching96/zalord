// --- Identity Context ---
export interface RegisterRequest {
  phoneNumber: string;
  displayName: string;
}

export interface RegisterResponse {
  id: string; // UUIDs map to strings in TypeScript 
  displayName: string;
}

// --- Messaging Context ---
export interface CreateChatRequest {
  participantIds: string[];
}

export interface CreateChatResponse {
  chatId: string;
  type: string;
}