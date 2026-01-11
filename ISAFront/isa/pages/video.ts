import { User } from "./user";

export type Video = {
    id: string;
    title: string;
    description: string;
    tags: string[];
    thumbnailPath: string;
    videoPath: string;
    timeOfUpload: Date;
    geographicalLocation: string;
    likes: Int32Array;
    author: User;
  }