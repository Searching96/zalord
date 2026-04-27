"use client";

import { messagingApi } from "@/services/apiClient";
import { MessageHistoryResponse } from "@/types/api";
import { Client } from "@stomp/stompjs";
import { useEffect, useState } from "react";
import SockJS from "sockjs-client";

export default function ChatPage() {
  const [userId, setUserId] = useState("");
  
  // Separated states to prevent the Keystroke Trap
  const [chatIdInput, setChatIdInput] = useState(""); 
  const [activeChatId, setActiveChatId] = useState(""); 
  
  const [messages, setMessages] = useState<MessageHistoryResponse[]>([]);
  const [newMessage, setNewMessage] = useState("");
  const [error, setError] = useState("");

  useEffect(() => {
    if (!activeChatId) return;

    const stompClient = new Client({
      // Creates a fresh socket every time it tries to connect
      webSocketFactory: () => new SockJS("http://localhost:8080/api/v1/ws"),
      
      onConnect: () => {
        stompClient.subscribe(`/topic/chats/${activeChatId}`, (message) => {
          const incomingMessage = JSON.parse(message.body);
          setMessages((prev) => [...prev, incomingMessage]);
        });
      },
      onStompError: (frame) => {
        console.error("Broker reported error: " + frame.headers["message"]);
      },
    });

    stompClient.activate();

    return () => {
      stompClient.deactivate();
    };
  }, [activeChatId]);

  const handleConnect = async () => {
    if (!chatIdInput.trim() || !userId.trim()) return;
    
    try {
      setError("");
      // Locks in the active room, triggering the WebSocket connection safely
      setActiveChatId(chatIdInput); 
      
      const history = await messagingApi.getHistory(chatIdInput, userId);
      setMessages(history);
    } catch (err: any) {
      setError(err.message || "Failed to load chat history");
    }
  };

  const handleSend = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    if (!newMessage.trim()) return;

    try {
      setError("");
      // Sends the message to the active room
      await messagingApi.sendMessage(activeChatId, { senderId: userId, content: newMessage });
      setNewMessage("");
    } catch (err: any) {
      setError(err.message || "Failed to send message");
    }
  };

  return (
    <div className="flex h-screen bg-gray-100 p-4 font-sans text-black">
      <div className="w-full max-w-3xl mx-auto bg-white rounded-lg shadow-lg flex flex-col overflow-hidden">
        
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
              type="text" value={chatIdInput} onChange={e => setChatIdInput(e.target.value)}
              className="w-full p-2 bg-gray-700 rounded text-sm outline-none" 
              placeholder="Paste UUID..."
            />
          </div>
          <button onClick={handleConnect} className="bg-blue-600 hover:bg-blue-500 px-4 py-2 rounded text-sm font-semibold transition">
            Connect
          </button>
        </div>

        {error && <div className="p-3 text-sm bg-red-100 text-red-700">{error}</div>}

        <div className="flex-1 p-4 overflow-y-auto bg-gray-50 space-y-4">
          {messages.length === 0 ? (
            <p className="text-gray-400 text-center mt-10">No messages yet, or chat not connected.</p>
          ) : (
            messages.map((msg) => (
              <div key={msg.id} className={`flex ${msg.senderId === userId ? "justify-end" : "justify-start"}`}>
                <div className={`max-w-xs md:max-w-md p-3 rounded-lg ${msg.senderId === userId ? "bg-blue-600 text-white" : "bg-gray-200 text-gray-800"}`}>
                  <p>{msg.content}</p>
                  <p className="text-[10px] opacity-70 mt-1 text-right">
                    {msg.createdAt ? new Date(msg.createdAt).toLocaleTimeString() : "Sending..."}
                  </p>
                </div>
              </div>
            ))
          )}
        </div>

        <form onSubmit={handleSend} className="p-4 bg-white border-t flex gap-2">
          <input
            type="text"
            value={newMessage}
            onChange={(e) => setNewMessage(e.target.value)}
            placeholder="Type a message..."
            className="flex-1 border rounded-full px-4 py-2 outline-none focus:border-blue-500"
            disabled={!activeChatId || !userId}
          />
          <button 
            type="submit" 
            disabled={!activeChatId || !userId}
            className="bg-blue-600 text-white px-6 py-2 rounded-full hover:bg-blue-700 transition disabled:opacity-50"
          >
            Send
          </button>
        </form>

      </div>
    </div>
  );
}