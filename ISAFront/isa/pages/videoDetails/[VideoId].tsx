import { useRouter } from "next/router";
import { useEffect, useRef, useState } from 'react';
import {Video} from '../video'
import axios from 'axios';

const VideoPage = () => {
  const router = useRouter();
  const [video, setVideo] = useState<Video>();
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
      Your browser does not support the video tag.
    </video>
    <p>{video?.title}</p>
    <p>{video?.descrition}</p>
    <p>{video?.timeOfUpload.toString()}</p>
    </div>


  );
};

export default VideoPage;
