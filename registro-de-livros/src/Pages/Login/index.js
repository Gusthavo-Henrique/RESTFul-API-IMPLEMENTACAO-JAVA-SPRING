import React, {useState} from "react";
import "./style.css";
import { useNavigate } from "react-router-dom";

import api from '../../Services/api';

export default function Login() {

    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const navigate = useNavigate();

    async function login(e) {
        e.preventDefault();
        const data = {
            username,
            password
        };

        try {
            const response = await api.post('auth/login', data);

            localStorage.setItem('username', username);
            localStorage.setItem('acessToken', response.data.token);

            navigate('/Books')
        } catch (err) {
            alert('Login failed, try again');
        }
    }

    return (
        <div className="container-login">
            <section className="form">
                <form onSubmit={login}>
                    <h1>Menu de acesso</h1>
                    <input 
                        placeholder="Username"
                        value={username}
                        onChange={e => setUsername(e.target.value)}
                    />
                    <input type="password" placeholder="Password"
                        value={password}
                        onChange={e => setPassword(e.target.value)}
                    />
                    <button className="button" type="submit">Login</button>
                </form>
            </section>
        </div>
    );
}