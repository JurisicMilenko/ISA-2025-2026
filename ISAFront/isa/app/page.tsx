import LoginPage from "../pages/login";
import SignupPage from "../pages/registration";
import UploadPostPage from "../pages/uploadVideo";
import FrontPage from '../pages/frontPage'
import VideoPage from '../pages/videoDetails/[VideoId]'
import PopularPage from "@/pages/popular";

function Home() {
  return (
    <div className="min-h-screen bg-gray-100 flex items-center justify-center p-4">
      <main className="w-full max-w-3xl bg-white/80 backdrop-blur rounded-xl shadow-lg p-8">
        <h1>HI</h1>
        <SignupPage/>
        <LoginPage/>
        <UploadPostPage/>
        <FrontPage/>
        <PopularPage/>
      </main>
    </div>
  );
}
export default Home;