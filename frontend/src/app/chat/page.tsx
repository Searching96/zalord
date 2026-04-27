"use client";

import { messagingApi } from "@/services/apiClient";
import { MessageHistoryResponse } from "@/types/api";
import { useState } from "react";

export default function ChatPage() {
  const [userId, setUserId] = useState("");
  const [chatId, setChatId] = useState("");
  const [messages, setMessages] = useState<MessageHistoryResponse[]>([]);
  const [newMessage, setNewMessage] = useState("");
  const [error, setError] = useState("");

  const loadChat = async () => {
    try {
      setError("");
      const history = await messagingApi.getHistory(chatId, userId);
      setMessages(history);
    } catch (err: any) {
      setError(err.message || "Failed to load chat history");
    }
  };

  const handleSend = async (e: React.SubmitEvent<HTMLFormElement>) => {
    e.preventDefault();
    if (!newMessage.trim()) return; // Don't send empty messages

    try {
      setError("");
      await messagingApi.sendMessage(chatId, { senderId: userId, content: newMessage });
      setNewMessage("");
      await loadChat(); // Refresh chat history after sending a message
    } catch (err: any) {
      setError(err.message || "Failed to send message");
    }
  };

  return (
    <div className="flex h-screen bg-gray-100 p-4 font-sans text-black">
      <div className="w-full max-w-3xl mx-auto bg-white rounded-lg shadow-lg flex flex-col overflow-hidden">
        
        {/* Header: Connection Settings */}
        <div className="bg-gray-800 text-white p-4 flex gap-4 items-end">
          <div className="flex-1">
            <label className="block text-xs text-gray-400 mb-1">Your User ID</label>
            <input 
              type="text" value={userId} onChange={e => setUserId(e.target.value)}
              className="w-full p-2 bg-gray-700 rounded text-sm outline-none" 
              placeholder="Paste UUID..."
            />
          </div>
          <div className="flex-1">
            <label className="block text-xs text-gray-400 mb-1">Chat Room ID</label>
            <input 
              type="text" value={chatId} onChange={e => setChatId(e.target.value)}
              className="w-full p-2 bg-gray-700 rounded text-sm outline-none" 
              placeholder="Paste UUID..."
            />
          </div>
          <button onClick={loadChat} className="bg-blue-600 hover:bg-blue-500 px-4 py-2 rounded text-sm font-semibold transition">
            Connect
          </button>
        </div>

        {error && <div className="p-3 text-sm bg-red-100 text-red-700">{error}</div>}

        {/* Message History Window */}
        <div className="flex-1 p-4 overflow-y-auto bg-gray-50 space-y-4">
          {messages.length === 0 ? (
            <p className="text-gray-400 text-center mt-10">No messages yet, or chat not connected.</p>
          ) : (
            messages.map((msg) => (
              <div key={msg.id} className={`flex ${msg.senderId === userId ? "justify-end" : "justify-start"}`}>
                <div className={`max-w-xs md:max-w-md p-3 rounded-lg ${msg.senderId === userId ? "bg-blue-600 text-white" : "bg-gray-200 text-gray-800"}`}>
                  <p>{msg.content}</p>
                  <p className="text-[10px] opacity-70 mt-1 text-right">
                    {new Date(msg.createdAt).toLocaleTimeString()}
                  </p>
                </div>
              </div>
            ))
          )}
        </div>

        {/* Input Area */}
        <form onSubmit={handleSend} className="p-4 bg-white border-t flex gap-2">
          <input
            type="text"
            value={newMessage}
            onChange={(e) => setNewMessage(e.target.value)}
            placeholder="Type a message..."
            className="flex-1 border rounded-full px-4 py-2 outline-none focus:border-blue-500"
            disabled={!chatId || !userId}
          />
          <button 
            type="submit" 
            disabled={!chatId || !userId}
            className="bg-blue-600 text-white px-6 py-2 rounded-full hover:bg-blue-700 transition disabled:opacity-50"
          >
            Send
          </button>
        </form>

      </div>
    </div>
  );
}