"use client";
import { useEffect, useRef, useState } from 'react';
import axios from 'axios';
import {Video} from './video'

function FrontPage() {
    const [videos, setVideos] = useState<Video[]>([]);

    const openProfile = () => {
        alert("Profile page is under construction.");
    }

    useEffect(() => {
        axios.get('http://localhost:8080/post')
        .then(function (response: any) {
          // handle success
          setVideos(response.data)
          //alert(response.data[0].thumbnailPath)
        })
        .catch(function (error: any) {
          // handle error
          console.log(error);
      })

        
    }, []);

    const videoFeed = videos.map(video =>
        <li key={video.id}>
            <a href={`/videoDetails/${video.id}`}>
            <div>
  <img src={`http://localhost:8080/${video.thumbnailPath.replaceAll('\\\\', '/')}`} alt="Video thumbnail" />
            <p>{video.title}</p>
            <button onClick={openProfile}>{video.author.name}</button>
</div>
            </a>          
        </li>
    );

    return(
        <div>
            <ul style={{ width: '100%' }}>{videoFeed}</ul>
        </div>
    );
}

export default FrontPage;