"use client";
import { useEffect, useRef, useState } from 'react';
import axios from 'axios';
import {Video} from './video'
import {User} from './user'

function FrontPage() {
    const [videos, setVideos] = useState<Video[]>([]);
    const [user, setUser] = useState<User>();
    axios.defaults.withCredentials = true;

    const openProfile = () => {
        alert("Profile page is under construction.");
    }

    const logout = async () => {
        const response = await axios.post('http://localhost:8080/logout');
        window.location.reload();
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

      axios.get('http://localhost:8080/auth/me')
        .then(function (response: any) {
          // handle success
          setUser(response.data)
          //if(response.data)
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
                <p className='text-green-500'>{video.title}</p>
            </div>
            </a>    
            <a href={`/userDetails/${video.author.id}`}>
                <p className='text-green-500'>By: {video.author.username}</p>
            </a>      
        </li>
    );

    return(
        <div>
            <ul style={{ width: '100%' }}>{videoFeed}</ul>
            {user != null ? (
                <button onClick={logout}>logout</button>
            ) : (
                <div></div>
            )}
        </div>
    );
}

export default FrontPage;