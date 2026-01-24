"use client";
import React, { useState } from 'react';
import axios from 'axios';

function SignupPage() {
    const [username, setUsername] = useState('');
    const [name, setName] = useState('');
    const [surname, setSurname] = useState('');
    const [address, setAddress] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');

    const signup = async () => {
        try {
            // Check for empty fields
            if (!surname || !name || !email || !password || !confirmPassword || !address) {
                alert('Please fill in all fields.');
                return;
            }

            if (password !== confirmPassword) {
                alert("Passwords do not match");
            }

            const response = await axios.post('http://localhost:8080/auth/signup', {
                name,
                surname,
                email,
                password,
                address,
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
            <form onSubmit={signup}>
            <p>name</p>
            <input type='text' value={name} onChange={(e) => setName(e.target.value)} required></input>
            <p>surname</p>
            <input type='text' value={surname} onChange={(e) => setSurname(e.target.value)} required></input>
            <p>username</p>
            <input type='text' value={username} onChange={(e) => setUsername(e.target.value)} required></input>
            <p>address</p>
            <input type='text' value={address} onChange={(e) => setAddress(e.target.value)} required></input>
            <p>email</p>
            <input type='email' value={email} onChange={(e) => setEmail(e.target.value)} required></input>
            <p>password</p>
            <input type='password' value={password} onChange={(e) => setPassword(e.target.value)} required></input>
            <p>confirm password</p>
            <input type='password' value={confirmPassword} onChange={(e) => setConfirmPassword(e.target.value)} required></input>
            <button>Sign up</button>
            </form>
        </div>
    );
}

export default SignupPage;
