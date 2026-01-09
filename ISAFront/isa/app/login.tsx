"use client";
import React, { useState } from 'react';
import axios from 'axios';

function LoginPage() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const login = async () => {
        try {
            // Check for empty fields
            if (!password || !username) {
                alert('Please fill in all fields.');
                return;
            }


            const response = await axios.post('http://localhost:8080/auth/login', {
                password,
                username
            },{ headers: { 'Accept': 'application/json',
      'Content-Type': 'application/json' } });
            console.log(response.data);
        } catch (error) {
            // Handle signup error
            alert(error)
        }
    };

    return (
        <div className='text-green-500'>
            <p>username</p>
            <input type='text' value={username} onChange={(e) => setUsername(e.target.value)}></input>
            <p>password</p>
            <input type='password' value={password} onChange={(e) => setPassword(e.target.value)}></input>
            <button onClick={login}>Log in</button>
        </div>
    );
}

export default LoginPage;