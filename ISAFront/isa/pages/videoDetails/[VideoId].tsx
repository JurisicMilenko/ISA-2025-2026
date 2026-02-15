import { useRouter } from "next/router";
import { useEffect, useRef, useState } from 'react';
import {Video} from '../video'
import {User} from '../user'
import {Comment} from '../comment'
//websocketjank 
import { Client, Message } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
//websocketjank 
import axios from 'axios';
import ReactPaginate from "react-paginate";

const VideoPage = () => {
  const router = useRouter();
  const [video, setVideo] = useState<Video>();
  const [user, setUser] = useState<User>();
  const [comments, setComments] = useState<Comment[]>([]);
  const [commentText, setCommentText] = useState('');
  const [page, setPage] = useState(0);
  const [duration, setDuration] = useState(0);
  const { VideoId } = router.query;
  const videoRef = useRef<HTMLVideoElement>(null);
  const [isPlaying, setIsPlaying] = useState(false);
  const [isStreaming, setIsStreaming] = useState(false);
  const [started, setStarted] = useState(true);
   //websocketjank 
  const [chatMessages, setChatMessages] = useState<{sender: string, content: string}[]>([]);
  const [chatInput, setChatInput] = useState('');
  const stompClientRef = useRef<Client | null>(null);
   //websocketjank 
  axios.defaults.withCredentials = true;
  useEffect(() => {
    if (!router.isReady) return;
        const replicas = ["http://localhost:8081", "http://localhost:8082"];
        const replicaIndex = Math.floor(Math.random() * replicas.length);

        axios.get('http://localhost:8080/post/'+VideoId)
        .then(function (response: any) {
          // handle success
          setVideo(response.data)

          axios.get(replicas[replicaIndex]+'/post/viewsFrom/'+VideoId)
            .then(function (response: any) {
              // handle success
              setVideo(prev => prev ? {...prev, views: response.data} : prev);
            })
            .catch(function (error: any) {
              // handle error
              console.log(error);
          })
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

      axios.get('http://localhost:8080/comment/postDTO/'+VideoId)
        .then(function (response: any) {
          // handle success
          setComments(response.data.content)
          //alert(response.data[0].thumbnailPath)
        })
        .catch(function (error: any) {
          // handle error
          console.log(error);
      })

      axios.get(replicas[replicaIndex]+'/post/view/'+VideoId)
        .then(function (response: any) {
          // handle success

        })
        .catch(function (error: any) {
          // handle error
          console.log(error);
      })
      

    }, [VideoId]);

    //websocketjank 
    useEffect(() => {
    if (!isStreaming || !user || !video) return;

    const socket = new SockJS('http://localhost:8080/ws-chat');
    const stompClient = new Client({
      webSocketFactory: () => socket,
      reconnectDelay: 5000,
      debug: (str: string) => console.log(str)
    });

    stompClient.onConnect = () => {
      console.log('Connected to WS');
      stompClient.subscribe(`/topic/chat/${video.id}`, (message: Message) => {
        const chatMessage = JSON.parse(message.body);
        setChatMessages(prev => [...prev, chatMessage]);
      });
    };

    stompClient.activate();
    stompClientRef.current = stompClient;

    return () => {
      stompClient.deactivate();
    };
    }, [isStreaming, user, video]);
    //websocketjank 

    if (!video) {
    return <div>Loading...</div>;
  }

  const Like = () => {
    axios.get('http://localhost:8080/post/like/'+VideoId)
        .then(function (response: any) {
          // handle success
          setVideo(prev => prev ? {...prev, likes: response.data.likes} : prev);
          //setVideo(response.data)
          //alert(response.data[0].thumbnailPath)
        })
        .catch(function (error: any) {
          // handle error
          console.log(error);
      })
      
  }
//websocketjank
  const sendChatMessage = () => {
  if (!chatInput.trim()) return;
  if (!user) return;
  if (!stompClientRef.current) return;

  const message = {
    sender: user.username || "Anonymous",
    content: chatInput
  };

  stompClientRef.current.publish({
    destination: `/app/chat/${video?.id}`,
    body: JSON.stringify(message)
  });

  setChatInput('');
  };
//websocketjank
  const submitComment = () => {
    axios.post('http://localhost:8080/comment', {
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

  const Play = () => {
    const thisVideo = videoRef.current;
    if (!thisVideo) return;

    if (thisVideo.paused) {
      thisVideo.play();
      setIsPlaying(true);
      const now = new Date();
      const premiereTime = new Date(video.premiereTime)
      thisVideo.currentTime = (now.getTime() - premiereTime.getTime())/1000;
    } else {
      thisVideo.pause();
      setIsPlaying(false);
    }
  };

  const Fullscreen = () => {
    const thisVideo = videoRef.current;
    if (!thisVideo) return;

    if (thisVideo.requestFullscreen) {
      thisVideo.requestFullscreen();
    }
  };

  return( 
    <div>
      {!started && <p>Video Hasn't started yet</p>}
     {started && <video ref={videoRef} width="320" height="240" controls
     onLoadedMetadata={(e) => {
          if(video.premiereTime == null)
            return
          const thisVideo = e.currentTarget;
          setDuration(thisVideo.duration);
          const now = new Date();
          const premiereTime = new Date(video.premiereTime)
          if((now.getTime() - premiereTime.getTime())/1000 < thisVideo.duration && now.getTime() - premiereTime.getTime() > 0){
            thisVideo.currentTime = (now.getTime() - premiereTime.getTime())/1000;
            thisVideo.controls = false;
            setIsStreaming(true);
          }
          if(now.getTime() - premiereTime.getTime() < 0){
            setStarted(false);
          }
          
        }}>
      <source id="video" src={`http://localhost:8080/${video?.videoPath.replaceAll('\\\\', '/')}`} type="video/mp4" />
      <track
        src="/path/to/captions.vtt"
        kind="subtitles"
        srcLang="en"
        label="English"
      />
    </video>}
    {isStreaming && 
    <button onClick={Play}>
      {isPlaying ? "Pause" : "Play"}
    </button>
    }
    <p>{video?.title}</p>
    <p>{video?.description}</p>
    <p>{video?.timeOfUpload.toString()}</p>
    <p>Views: {video?.views}</p>
    <p>{video?.likes}</p>
    {user != null ? (
        <button onClick={Like}>Like</button>
      ) : (
        <button>Not allowed to like, go log in</button>
      )}

      {isStreaming && user && (
        <div style={{ marginTop: '20px', border: '1px solid gray', padding: '10px', borderRadius: '8px' }}>
          <h3>Live Chat</h3>
          <div style={{ maxHeight: '200px', overflowY: 'auto', marginBottom: '10px' }}>
            {chatMessages.map((msg, idx) => (
              <div key={idx} style={{ marginBottom: '5px' }}>
                <strong>{msg.sender}:</strong> {msg.content}
              </div>
            ))}
          </div>
          <input
            type="text"
            value={chatInput}
            onChange={(e) => setChatInput(e.target.value)}
            placeholder="Type a message..."
            style={{ width: '70%', marginRight: '10px' }}
          />
          <button onClick={sendChatMessage}>Send</button>
        </div>
      )}

      <div>
        <h1>Leave a comment</h1>
        <input
          className="w-full border rounded p-2"
          placeholder="Comment on this video"
          value={commentText}
          onChange={(e) => setCommentText(e.target.value)}
        />
        {user != null ? (
        <button onClick={submitComment}>Submit</button>
      ) : (
        <button>Not allowed to comment, go log in</button>
      )}
        <PaginateComments comments={comments} page={page} />
        <ReactPaginate
          onPageChange={(event) => setPage(event.selected)}
        pageCount={Math.ceil(comments?.length / 3)}
        breakLabel="..."
        previousLabel="next" 
        nextLabel = "previous"
/>
      </div>
    </div>
  );
};

function PaginateComments({comments, page}: {comments: Comment[], page: number}){
  const [shownComments, setShownComments] = useState<Comment[]>([]);

  useEffect(() => {
   /* const response = await axios.get('http://localhost:8080/user/',{withCredentials: true, headers: { 'Accept': 'application/json',
      'Content-Type': 'application/json' } });
            console.log(response.data);
            localStorage.setItem("auth", JSON.stringify(response.data));
            alert("Login successful!");
        } catch (error) {
            // Handle signup error
            alert(error)
        }
*/
    setShownComments(
      comments.filter((item, index) => {
        return (index >= page * 3) && (index < (page + 1) * 3);
      })
    );
}, [page, comments]);
  return(
    <ul>{shownComments && shownComments.map((comment, index) => <div style={{
                    background: "lightblue",
                    padding: '12px',
                    margin: '12px',
                    borderRadius: '12px',
                }}><li>{comment.text}</li><li>User:{comment.authorName}</li></div>)}</ul>
  )
}

export default VideoPage;
