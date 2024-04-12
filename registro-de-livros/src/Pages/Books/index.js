import React, {useState, useEffect} from "react";
import { useNavigate } from "react-router-dom";
import { Link } from "react-router-dom";

import api from "../../Services/api"; 

import "./style.css"

export default function Books() {

    const [books, setBooks] = useState([]);

    const username = localStorage.getItem('username');
    const acessToken = localStorage.getItem('acessToken');

    const navigate = useNavigate();

    async function logout() {
        localStorage.clear();
        navigate('/Authenticate')
    }

    async function editBook(id) {
        try {
            navigate(`/Books/New/${id}`)
        } catch (error) {
            alert('Edit book failed, try again!')
        }
    }

    async function deleteBook(id) {
        try {
           await api.delete(`api/books/${id}`, {
            headers: {
                Authorization: `Bearer ${acessToken}`
            }
           })
           setBooks(books.filter(book =>
            book.id !== id))
        } catch (error) {
            alert('Delete failed! Try again.')
        }
    }

    useEffect( () => {
        api.get("api/books", {
            headers: {
                Authorization: `Bearer ${acessToken}`
            }
        }).then(response =>{
            setBooks(response.data)
        })
    })

    return (
        <div className="container-books">
            <header>
                <span>Welcome!, {username.toUpperCase()}</span>
                <Link className="button" to={"New/0"}>Add new Book</Link>
                <button onClick={logout} className="button"><strong>Logout</strong></button>
            </header>
        
        <h1>Registred Books</h1>
        <ul>
            {books.map(book => (
                <li key={book.id}>
                <strong>Title</strong>
                <p>{book.title}</p>
                <strong>Author</strong>
                <p>{book.author}</p>
                
                <button onClick={() => editBook(book.id)} type="button">Editar</button>
                <button onClick={() => deleteBook(book.id)} type="button">Deletar</button>
            </li>
            ))}
        </ul>

        </div>
    );
}