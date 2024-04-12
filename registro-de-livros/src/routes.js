import React from "react";
import {BrowserRouter, Route, Routes} from "react-router-dom";

import Login from "./Pages/Login";
import Books from "./Pages/Books";
import NewBook from "./Pages/NewBook";

export default function AppRoutes() {
    return (

        <BrowserRouter>
            <Routes>
                <Route path="/Authenticate" exact element={<Login/>}/>
                <Route path="/Books" element={<Books/>}/>
                <Route path="/Books/New/:bookId" element={<NewBook/>}/>
            </Routes>
        </BrowserRouter>

    );
}