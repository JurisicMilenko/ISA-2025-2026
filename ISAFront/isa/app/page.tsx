import LoginPage from "./login";
import SignupPage from "./registration"
export default function Home() {
  return (
    <div className="min-h-screen bg-gray-100 flex items-center justify-center p-4">
      <main className="w-full max-w-3xl bg-white/80 backdrop-blur rounded-xl shadow-lg p-8">
        <h1>HI</h1>
        <SignupPage/>
        <LoginPage/>
      </main>
    </div>
  );
}
