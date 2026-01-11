import { useRouter } from "next/router";
import { useEffect, useRef, useState } from 'react';
import {Video} from '../video'
import {User} from '../user'
import {Comment} from '../comment'
import axios from 'axios';
import ReactPaginate from "react-paginate";

const VideoPage = () => {
  const router = useRouter();
  const [video, setVideo] = useState<Video>();
  const [user, setUser] = useState<User>();
  const [comments, setComments] = useState<Comment[]>([]);
  const [commentText, setCommentText] = useState('');
  const [page, setPage] = useState(0);
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

      axios.get('http://localhost:8080/comment/post/'+VideoId)
        .then(function (response: any) {
          // handle success
          setComments(response.data.content)
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
    {user != null ? (
        <button onClick={Like}>Like</button>
      ) : (
        <button>Not allowed to like, go log in</button>
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
    <ul>{shownComments && shownComments.map((item, index) => <li>{item.text}</li>)}</ul>
  )
}

export default VideoPage;
