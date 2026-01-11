"use client";

import React, { useState, ChangeEvent } from "react";
import axios, { AxiosError } from "axios";

export default function UploadPostPage(): JSX.Element {
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [tags, setTags] = useState("");
  const [location, setLocation] = useState("");

  const [video, setVideo] = useState<File | null>(null);
  const [thumbnail, setThumbnail] = useState<File | null>(null);

  const [loading, setLoading] = useState(false);

  const handleVideoChange = (e: ChangeEvent<HTMLInputElement>) => {
    setVideo(e.target.files?.[0] ?? null);
  };

  const handleThumbnailChange = (e: ChangeEvent<HTMLInputElement>) => {
    setThumbnail(e.target.files?.[0] ?? null);
  };

  const submit = async (): Promise<void> => {
    // Read token directly from localStorage
    const storedAuth = localStorage.getItem("auth");
    if (!storedAuth) {
      alert("You must be logged in to upload.");
      return;
    }

    let uploadToken: string | null = null;
    try {
      const parsed = JSON.parse(storedAuth);
      uploadToken = parsed?.accessToken ?? null;
    } catch {
      alert("Invalid auth data.");
      return;
    }

    if (!uploadToken) {
      alert("Token missing. Please log in again.");
      return;
    }

    // Validate inputs
    if (!video || !thumbnail) {
      alert("Video and thumbnail are required.");
      return;
    }
    if (!title.trim() || !description.trim()) {
      alert("Title and description are required.");
      return;
    }

    // Build form data
    const formData = new FormData();
    formData.append("video", video);
    formData.append("thumbnail", thumbnail);
    formData.append("title", title);
    formData.append("description", description);
    formData.append("geographicalLocation", location);

    tags
      .split(",")
      .map((tag) => tag.trim())
      .forEach((tag) => {
        if (tag) formData.append("tags", tag);
      });

    try {
      setLoading(true);

      await axios.post("http://localhost:8080/post/upload", formData, {
        headers: {
          Authorization: `Bearer ${uploadToken}`, // guaranteed valid
        },
      });

      alert("Video uploaded successfully 🎬");

      // Reset form
      setTitle("");
      setDescription("");
      setTags("");
      setLocation("");
      setVideo(null);
      setThumbnail(null);
    } catch (error) {
      const err = error as AxiosError;
      console.error("Upload failed:", err.response?.status, err.message);
      alert("Upload failed");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-100 p-6">
      <div className="w-full max-w-xl bg-white rounded-xl shadow-lg p-8 space-y-4">
        <h1 className="text-2xl font-semibold">Upload video</h1>

        <input
          className="w-full border rounded p-2"
          placeholder="Title"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
        />

        <textarea
          className="w-full border rounded p-2"
          placeholder="Description"
          rows={4}
          value={description}
          onChange={(e) => setDescription(e.target.value)}
        />

        <input
          className="w-full border rounded p-2"
          placeholder="Tags (comma separated)"
          value={tags}
          onChange={(e) => setTags(e.target.value)}
        />

        <input
          className="w-full border rounded p-2"
          placeholder="Geographical location"
          value={location}
          onChange={(e) => setLocation(e.target.value)}
        />

        <div>
          <label className="block mb-1 font-medium">Video *</label>
          <input type="file" accept="video/*" onChange={handleVideoChange} />
        </div>

        <div>
          <label className="block mb-1 font-medium">Thumbnail *</label>
          <input type="file" accept="image/*" onChange={handleThumbnailChange} />
        </div>

        <button
          onClick={submit}
          className="w-full bg-black text-white rounded py-2 hover:opacity-90 disabled:opacity-50"
        >
          {loading ? "Uploading..." : "Upload"}
        </button>
      </div>
    </div>
  );
}
