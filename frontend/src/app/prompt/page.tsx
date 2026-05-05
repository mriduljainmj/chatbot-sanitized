"use client";

import { useEffect, useRef, useState } from "react";
import { useRouter } from "next/navigation";

type Message = {
  role: "USER" | "AI";
  content: string;
};

type Chat = {
  chatId: string;
  title: string;
  model: string;
};

export default function PromptPage() {
  const router = useRouter();
  const bottomRef = useRef<HTMLDivElement>(null);
  const [deletingChatId, setDeletingChatId] = useState<string | null>(null);
  const [selectedModel, setSelectedModel] = useState("gpt-4o");

  const [sidebarOpen, setSidebarOpen] = useState(true);
  const [isSending, setIsSending] = useState(false);
  const [chats, setChats] = useState<Chat[]>([]);
  const [activeChat, setActiveChat] = useState<string | null>(null);
  const [messages, setMessages] = useState<Message[]>([]);
  const [input, setInput] = useState("");

  const token =
    typeof window !== "undefined" ? localStorage.getItem("token") : null;

  /* =======================
     AUTH GUARD
     ======================= */
  useEffect(() => {
    if (!token) {
      router.replace("/login");
    }
  }, [router, token]);

  /* =======================
     LOAD CHATS
     ======================= */

  useEffect(() => {
    const t = localStorage.getItem("token");
    if (!t) {
      router.replace("/login");
      return;
    }
    fetchChats();
  }, []);


  /* =======================
     AUTO SCROLL
     ======================= */
  useEffect(() => {
    bottomRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages]);

  async function fetchChats() {
    try {
      const res = await fetch("http://localhost:8080/chats", {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (!res.ok) return; // silent fail or redirect

      setChats(await res.json());
    } catch (err) {
      console.error("Fetch chats failed:", err);
    }
  }


  async function createNewChat() {
    try {
      const res = await fetch("http://localhost:8080/chats", {
        method: "POST",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (!res.ok) throw new Error("Failed to create chat");

      const chatId = await res.text();
      setActiveChat(chatId);
      setMessages([]);
      fetchChats();
    } catch (err) {
      console.error(err);
    }
  }

  async function loadMessages(chat: Chat) {
    setActiveChat(chat.chatId);

    setSelectedModel(chat.model ?? "gpt-4o");

    const res = await fetch(
      `http://localhost:8080/chats/${chat.chatId}/messages`,
      {
        headers: { Authorization: `Bearer ${token}` },
      }
    );

    setMessages(await res.json());
  }

  async function deleteChat(chatId: string) {
    if (deletingChatId) return;

    try {
      setDeletingChatId(chatId);

      await fetch(`http://localhost:8080/chats/${chatId}`, {
        method: "DELETE",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (chatId === activeChat) {
        setActiveChat(null);
        setMessages([]);
      }

      fetchChats();
    } catch (err) {
      console.error("Delete failed", err);
    } finally {
      setDeletingChatId(null);
    }
  }

  async function sendPrompt() {
    if (!input.trim() || isSending) return;

    try {
      setIsSending(true);

      const userMessage = input;
      setMessages(prev => [...prev, { role: "USER", content: userMessage }]);
      setInput("");

      const res = await fetch("http://localhost:8080/prompt", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },

        body: JSON.stringify({
          chatId: messages.length === 0 ? null : activeChat,
          prompt: userMessage,
          model: selectedModel,   // ✅ ADD THIS
        }),

      });

      if (!res.ok) throw new Error("Prompt failed");

      const data = await res.json();

      setMessages(prev => [...prev, { role: "AI", content: data.response }]);
      fetchChats(); // sidebar updates with new title
    } finally {
      setIsSending(false);
    }
  }


  return (
    <div className="flex h-screen bg-gray-50">
      {/* =======================
          SIDEBAR
          ======================= */}
      <aside
        className={`bg-white border-r transition-all duration-300 ease-in-out
          ${sidebarOpen ? "w-72" : "w-0 overflow-hidden"}`}
      >
        {sidebarOpen && (
          <div className="flex h-full flex-col">
            <div className="p-4 border-b">
              <button
                onClick={createNewChat}
                disabled={messages.length === 0}
                className="disabled:opacity-50 disabled:cursor-not-allowed"
              >
                + New Chat
              </button>

            </div>

            <div className="flex-1 overflow-y-auto p-3 space-y-1">
              {chats.map(chat => (
                <div
                  key={chat.chatId}
                  onClick={() => loadMessages(chat)}
                  className={`flex items-center justify-between rounded-md p-2 cursor-pointer
                    ${chat.chatId === activeChat
                      ? "bg-indigo-100"
                      : "hover:bg-gray-100"
                    }`}
                >
                  <span className="text-sm truncate">
                    {chat.title}
                  </span>
                  <button
                    onClick={e => {
                      e.stopPropagation();
                      deleteChat(chat.chatId);
                    }}
                    disabled={deletingChatId === chat.chatId}
                    className="
    text-gray-400 hover:text-red-500
    disabled:opacity-50 disabled:cursor-not-allowed
  "
                    title="Delete chat"
                  >
                    {deletingChatId === chat.chatId ? "…" : "🗑"}
                  </button>
                </div>
              ))}
            </div>
          </div>
        )}
      </aside>

      {/* =======================
          MAIN CONTENT
          ======================= */}
      <main className="flex flex-1 flex-col">
        {/* HEADER */}
        <header className="flex items-center justify-between border-b bg-white px-6 py-4">
          <div className="flex items-center gap-3">
            {/* BURGER ICON */}
            <button
              onClick={() => setSidebarOpen(prev => !prev)}
              className="rounded-md p-2 hover:bg-gray-100"
              aria-label="Toggle sidebar"
            >
              <svg
                xmlns="http://www.w3.org/2000/svg"
                fill="none"
                viewBox="0 0 24 24"
                strokeWidth={2}
                stroke="currentColor"
                className="h-6 w-6"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  d="M3 6h18M3 12h18M3 18h18"
                />
              </svg>
            </button>

            <h1 className="font-semibold text-gray-900">
              Secure AI Agent
            </h1>


            <select
              value={selectedModel}
              onChange={(e) => setSelectedModel(e.target.value)}
              disabled={messages.length > 0}
              className="
    rounded-md border px-3 py-1 text-sm
    disabled:bg-gray-100 disabled:cursor-not-allowed
  "
            >
              <option value="gpt-4o">GPT‑4o (Best)</option>
              <option value="gpt-4.1">GPT‑4.1</option>
              <option value="gpt-3.5-turbo">GPT‑3.5 (Fast)</option>
            </select>

          </div>


          <button
            onClick={() => {
              localStorage.removeItem("token");
              router.push("/login");
            }}
            className="rounded-md bg-red-600 px-4 py-2 text-sm text-white hover:bg-red-700"
          >
            Logout
          </button>
        </header>

        {/* CHAT AREA */}
        <div className="flex-1 overflow-y-auto p-6 space-y-4">
          {messages.map((m, i) => (
            <div
              key={i}
              className={`max-w-xl rounded-lg px-4 py-3 text-sm
                ${m.role === "USER"
                  ? "ml-auto bg-indigo-600 text-white"
                  : "bg-white border"
                }`}
            >
              {m.content}
            </div>
          ))}

          {isSending && (
            <div className="max-w-xs rounded-lg px-4 py-2 text-sm bg-gray-200 animate-pulse">
              AI is thinking…
            </div>
          )}

          <div ref={bottomRef} />
        </div>

        {/* INPUT */}
        <div className="sticky bottom-0 border-t bg-white p-4 flex gap-3">
          <textarea
            rows={2}
            value={input}
            onChange={e => setInput(e.target.value)}
            placeholder={isSending ? "AI is thinking..." : "Type your message..."}
            disabled={isSending}
            className="
      flex-1 rounded-md border px-3 py-2
      focus:ring-2 focus:ring-indigo-500
      outline-none resize-none
      disabled:bg-gray-100
    "
            onKeyDown={e => {
              if (e.key === "Enter" && !e.shiftKey && !isSending) {
                e.preventDefault();
                sendPrompt();
              }
            }}
          />

          <button
            onClick={sendPrompt}
            disabled={isSending || !input.trim()}
            className="
      rounded-md bg-indigo-600 px-6 text-white font-medium
      hover:bg-indigo-700
      disabled:opacity-50 disabled:cursor-not-allowed
    "
          >
            {isSending ? "..." : "Send"}
          </button>
        </div>
      </main>
    </div>
  );
}
