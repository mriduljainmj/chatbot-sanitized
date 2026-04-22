"use client";

import { useState } from "react";

export default function PromptPage() {
  const [prompt, setPrompt] = useState("");
  const [response, setResponse] = useState("");

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-50 to-slate-100">
      {/* Header */}
      <header className="sticky top-0 z-10 flex items-center justify-between bg-white/80 backdrop-blur border-b px-6 py-4">
        <h1 className="text-lg font-semibold text-gray-900">
          Secure AI Workspace
        </h1>
        <button className="text-sm text-red-600 hover:underline">
          Logout
        </button>
      </header>

      {/* Main Container */}
      <main className="px-6 py-10">
        <div className="mx-auto max-w-4xl space-y-8">
          
          {/* Intro */}
          <div className="text-center">
            <h2 className="text-3xl font-bold text-gray-900">
              Ask the AI anything
            </h2>
            <p className="mt-2 text-gray-500">
              Your prompt is sanitized and processed securely.
            </p>
          </div>

          {/* Response Card */}
          {response && (
            <div className="rounded-2xl border bg-white p-6 shadow-sm">
              <div className="flex items-start gap-3">
                <div className="h-8 w-8 rounded-full bg-indigo-600 flex items-center justify-center text-white text-sm font-bold">
                  AI
                </div>
                <p className="text-gray-800 leading-relaxed">
                  {response}
                </p>
              </div>
            </div>
          )}

          {/* Prompt Composer */}
          <div className="rounded-2xl bg-white shadow-lg border border-gray-200">
            <div className="p-4 border-b text-sm text-gray-500">
              Prompt
            </div>

            <div className="p-4">
              <textarea
                rows={5}
                value={prompt}
                onChange={(e) => setPrompt(e.target.value)}
                placeholder="Write your prompt here..."
                className="
                  w-full resize-none rounded-lg
                  border border-gray-300
                  px-4 py-3
                  text-gray-800
                  focus:outline-none
                  focus:ring-2 focus:ring-indigo-600
                "
              />

              <div className="mt-4 flex justify-between items-center">
                <span className="text-xs text-gray-400">
                  Press submit to send the prompt securely
                </span>

                <button
                  onClick={() => setResponse(
                    "This is a sample AI response. Once backend is connected, this will be dynamic."
                  )}
                  className="
                    rounded-lg bg-indigo-600
                    px-6 py-2.5
                    text-white font-medium
                    hover:bg-indigo-700
                    transition
                  "
                >
                  Submit
                </button>
              </div>
            </div>
          </div>

        </div>
      </main>
    </div>
  );
}