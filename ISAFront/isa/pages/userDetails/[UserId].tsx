import { useRouter } from "next/router";
import { useEffect, useRef, useState } from 'react';
import axios from 'axios';
import {User} from '../user'
import VideoPage from "../videoDetails/[VideoId]";

const UserPage = () => {
  const router = useRouter();
  const [user, setUser] = useState<User>();
  const { UserId } = router.query;
  axios.defaults.withCredentials = true;
  useEffect(() => {
    if (!router.isReady) return;
        axios.get('http://localhost:8080/user/'+UserId)
        .then(function (response: any) {
          // handle success
          setUser(response.data)
        })
        .catch(function (error: any) {
          // handle error
          console.log(error);
      })

    }, [UserId]);

    if (!user) {
    return <div>Loading...</div>;
  }

  return( 
    <div>
        <p>Username: {user?.username}</p>
        <p>Name: {user?.name}</p>
        <p>Surname: {user?.surname}</p>
    </div>
  );
}

export default UserPage;
