"use client";

import { messagingApi } from "@/services/apiClient";
import { MessageHistoryResponse, GetUserChatsResponse } from "@/types/api";
import { Client } from "@stomp/stompjs";
import { useEffect, useState } from "react";
import SockJS from "sockjs-client";
import { useRouter } from "next/navigation";

export default function ChatPage() {
  const router = useRouter();
  
  // Auth States
  const [userId, setUserId] = useState("");
  const [displayName, setDisplayName] = useState("");

  // Sidebar Chat List State
  const [chatList, setChatList] = useState<GetUserChatsResponse[]>([]);

  // Separated states to prevent the Keystroke Trap
  const [chatIdInput, setChatIdInput] = useState(""); 
  const [activeChatId, setActiveChatId] = useState(""); 
  
  const [messages, setMessages] = useState<MessageHistoryResponse[]>([]);
  const [newMessage, setNewMessage] = useState("");
  const [error, setError] = useState("");

  // 1. Initial Load: Check Auth and Fetch Chat List
  useEffect(() => {
    const storedUserId = localStorage.getItem("zalord_user_id");
    const storedName = localStorage.getItem("zalord_display_name");

    if (!storedUserId) {
      router.push("/"); // Kick unauthenticated users back to login
      return;
    }

    setUserId(storedUserId);
    setDisplayName(storedName || "Unknown");

    messagingApi.getUserChats(storedUserId)
      .then(setChatList)
      .catch((err) => console.error("Failed to load chat list", err));
  }, [router]);

  // 2. WebSocket Connection
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

  // Shared connect logic for both manual input and sidebar clicks
  const handleConnect = async (targetChatId: string) => {
    if (!targetChatId.trim() || !userId.trim()) return;
    
    try {
      setError("");
      // Locks in the active room, triggering the WebSocket connection safely
      setActiveChatId(targetChatId); 
      setChatIdInput(targetChatId); // Sync the input box if clicked from sidebar
      
      const history = await messagingApi.getHistory(targetChatId, userId);
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

  const handleLogout = () => {
    localStorage.clear();
    router.push("/");
  };

  return (
    <div className="flex h-screen bg-gray-100 font-sans text-black">
      
      {/* LEFT SIDEBAR: User Info & Chat List */}
      <div className="w-1/3 max-w-sm bg-gray-800 text-white flex flex-col shadow-lg z-10">
        
        {/* Header Profile */}
        <div className="p-4 bg-gray-900 border-b border-gray-700 flex justify-between items-center">
          <div className="truncate pr-2">
            <h2 className="font-bold text-lg truncate">{displayName}</h2>
            <p className="text-xs text-gray-400 font-mono mt-1">ID: {userId.substring(0, 8)}...</p>
          </div>
          <button onClick={handleLogout} className="text-xs bg-red-600 hover:bg-red-500 px-3 py-1.5 rounded transition">
            Logout
          </button>
        </div>

        {/* Manual Connect (Preserved Logic) */}
        <div className="p-4 border-b border-gray-700 bg-gray-800">
          <label className="block text-xs text-gray-400 mb-1 uppercase tracking-wider">Join Room via ID</label>
          <div className="flex gap-2">
            <input 
              type="text" 
              value={chatIdInput} 
              onChange={e => setChatIdInput(e.target.value)}
              className="flex-1 p-2 bg-gray-700 rounded text-sm outline-none focus:ring-1 focus:ring-blue-500" 
              placeholder="Paste UUID..."
            />
            <button 
              onClick={() => handleConnect(chatIdInput)} 
              className="bg-blue-600 hover:bg-blue-500 px-3 py-2 rounded text-sm font-semibold transition"
            >
              Go
            </button>
          </div>
        </div>

        {/* Chat List */}
        <div className="flex-1 overflow-y-auto p-2 space-y-1">
          <p className="px-2 pt-2 pb-1 text-xs text-gray-400 uppercase tracking-wider font-semibold">Your Chats</p>
          {chatList.length === 0 ? (
            <p className="text-gray-500 text-sm text-center mt-4 italic">No chats found.</p>
          ) : (
            chatList.map((chat) => (
              <button
                key={chat.chatId}
                onClick={() => handleConnect(chat.chatId)}
                className={`w-full text-left p-3 rounded transition flex flex-col gap-1 ${
                  activeChatId === chat.chatId 
                    ? "bg-blue-600 text-white shadow-md" 
                    : "hover:bg-gray-700 bg-transparent text-gray-300"
                }`}
              >
                <p className="font-semibold text-sm truncate">
                  {chat.chatName || "Unknown User"}
                </p>
                <p className={`text-[10px] font-mono opacity-60 ${activeChatId === chat.chatId ? "text-blue-200" : "text-gray-500"}`}>
                  #{chat.chatId.substring(0, 8)}
                </p>
              </button>
            ))
          )}
        </div>
      </div>

      {/* RIGHT SIDE: Active Chat Area */}
      <div className="flex-1 flex flex-col bg-white">
        {error && <div className="p-3 text-sm bg-red-100 text-red-700 border-b">{error}</div>}

        {!activeChatId ? (
          <div className="flex-1 flex items-center justify-center bg-gray-50 text-gray-400 flex-col gap-3">
            <svg className="w-16 h-16 opacity-20" fill="currentColor" viewBox="0 0 20 20"><path fillRule="evenodd" d="M18 10c0 3.866-3.582 7-8 7a8.841 8.841 0 01-4.083-.98L2 17l1.338-3.123C2.493 12.767 2 11.434 2 10c0-3.866 3.582-7 8-7s8 3.134 8 7zM7 9H5v2h2V9zm8 0h-2v2h2V9zM9 9h2v2H9V9z" clipRule="evenodd"></path></svg>
            <p>Select a chat or join via ID to start messaging.</p>
          </div>
        ) : (
          <>
            {/* Chat Header */}
            <div className="p-4 border-b bg-gray-50 shadow-sm flex items-center justify-between">
              <div>
                <p className="text-xs text-gray-500 uppercase tracking-wider font-bold">Active Chat</p>
                <p className="text-sm font-mono text-gray-700">{activeChatId}</p>
              </div>
            </div>

            {/* Message History */}
            <div className="flex-1 p-4 overflow-y-auto bg-gray-50 space-y-4">
              {messages.length === 0 ? (
                <p className="text-gray-400 text-center mt-10">No messages yet.</p>
              ) : (
                messages.map((msg) => (
                  <div key={msg.id} className={`flex ${msg.senderId === userId ? "justify-end" : "justify-start"}`}>
                    <div className={`max-w-md p-3 rounded-lg ${msg.senderId === userId ? "bg-blue-600 text-white" : "bg-gray-200 text-gray-800"}`}>
                      <p>{msg.content}</p>
                      <p className="text-[10px] opacity-70 mt-1 text-right">
                        {msg.createdAt ? new Date(msg.createdAt).toLocaleTimeString() : "Sending..."}
                      </p>
                    </div>
                  </div>
                ))
              )}
            </div>

            {/* Input Form */}
            <form onSubmit={handleSend} className="p-4 bg-white border-t flex gap-2 shadow-inner">
              <input
                type="text"
                value={newMessage}
                onChange={(e) => setNewMessage(e.target.value)}
                placeholder="Type a message..."
                className="flex-1 border border-gray-300 rounded-full px-4 py-2 outline-none focus:border-blue-500"
              />
              <button 
                type="submit" 
                disabled={!newMessage.trim()}
                className="bg-blue-600 text-white px-6 py-2 rounded-full hover:bg-blue-700 transition disabled:opacity-50"
              >
                Send
              </button>
            </form>
          </>
        )}
      </div>
    </div>
  );
}