import React, {useState, useEffect} from "react";
import { useNavigate, useParams } from "react-router-dom";

import api from "../../Services/api"
import { Link } from "react-router-dom";

import "./style.css"

export default function NewBook() {

    const username = localStorage.getItem('username');
    const acessToken = localStorage.getItem('acessToken');

    const [id, setId] = useState(null);
    const [title, setTitle] = useState('');
    const [author, setAuthor] = useState('');

    const navigate = useNavigate();
    const {bookId} = useParams();

    async function loadBook() {
        try {
            const response = await api.get(`api/books/${bookId}`, {
                headers: {
                    Authorization: `Bearer ${acessToken}`
                }
            })
            setId(response.data.id);
            setTitle(response.data.title);
            setAuthor(response.data.author);
        } catch (error) {
            alert('error recovering book');
            navigate("/Books")
        }
    }

    useEffect( () => {
        if(bookId === '0') return;
        else loadBook();
    }, [bookId])

    async function createNewBook(e) {
        e.preventDefault();
        const data = {
            title,
            author
        }
        try {
            await api.post('/api/books', data, {
                headers: {
                    Authorization: `Bearer ${acessToken}`
                }
            });
            navigate('/Books')
        } catch (err) {
            alert('Error while recording book!')
        }
    }
    return (
        <div className="newbook-container">
            <div className="content">
                <section>
                    <h1>Add new book</h1>
                    <p1>Enter the book information add click on "Add" ### {bookId}. </p1>
                    <Link className="home-link" to={"/Books"}>
                        <span>home</span>
                    </Link>
                </section>
                <form onSubmit={createNewBook}>
                    <input 
                        placeholder="Title"
                        value={title}
                        onChange={e => setTitle(e.target.value)}
                    />
                    <input 
                        placeholder="Author"
                        value={author}
                        onChange={e => setAuthor(e.target.value)}
                    />
                    <button className="button" type="submit">Add</button>
                </form>
            </div>
        </div>
    )
}