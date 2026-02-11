import { User } from "./user";

export type Video = {
    id: string;
    title: string;
    description: string;
    tags: string[];
    thumbnailPath: string;
    videoPath: string;
    timeOfUpload: Date;
    premiereTime: Date;
    geographicalLocation: string;
    views: Int32Array;
    likes: Int32Array;
    author: User;
  }