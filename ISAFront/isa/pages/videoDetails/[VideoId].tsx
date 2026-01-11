import { useRouter } from "next/router";
import { useEffect, useRef, useState } from 'react';
import {Video} from '../video'
import axios from 'axios';

const VideoPage = () => {
  const router = useRouter();
  const [video, setVideo] = useState<Video>();
  const [commentText, setCommentText] = useState('');
  const { VideoId } = router.query;
  axios.defaults.withCredentials = true;
  useEffect(() => {
    if (!router.isReady) return;
        axios.get('http://localhost:8080/post/'+VideoId)
        .then(function (response: any) {
          // handle success
          setVideo(response.data)
          //alert(response.data[0].thumbnailPath)
        })
        .catch(function (error: any) {
          // handle error
          console.log(error);
      })

    }, [VideoId]);

    if (!video) {
    return <div>Loading...</div>;
  }
  
  const Like = () => {
    axios.get('http://localhost:8080/post/like/'+VideoId)
        .then(function (response: any) {
          // handle success
          setVideo(response.data)
          //alert(response.data[0].thumbnailPath)
        })
        .catch(function (error: any) {
          // handle error
          console.log(error);
      })
  }

  const submitComment = () => {
    axios.post('http://localhost:8080/post/comment', {
      text: commentText,
      postId: VideoId
      }).then(function (response: any) {
        // handle success
        alert("Comment submitted successfully.");
        setCommentText('');
        }
      )
        .catch(function (error: any) {
          // handle error
          alert(error);
      })
  }

  return( 
    <div>
     <video width="320" height="240" controls preload="none">
      <source src={`http://localhost:8080/${video?.videoPath.replaceAll('\\\\', '/')}`} type="video/mp4" />
      <track
        src="/path/to/captions.vtt"
        kind="subtitles"
        srcLang="en"
        label="English"
      />
    </video>
    <p>{video?.title}</p>
    <p>{video?.description}</p>
    <p>{video?.timeOfUpload.toString()}</p>
    <p>{video?.likes}</p>
    <button onClick={Like}>Like</button>

      <div>
        <h1>Leave a comment</h1>
        <input
          className="w-full border rounded p-2"
          placeholder="Comment on this video"
          value={commentText}
          onChange={(e) => setCommentText(e.target.value)}
        />
        <button onClick={submitComment}>Submit</button>
      </div>
    </div>
  );
};

export default VideoPage;
