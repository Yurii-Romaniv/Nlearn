import './App.css';
import {BrowserRouter, Route, Routes} from 'react-router-dom';
import TestEdit from "./TestEdit";
import TestPassing from "./TestPassing";
import StudentsHome from "./StudentsHome";
import 'bootstrap/dist/css/bootstrap.css';
import {useQuery} from "react-query";
import {checkAuth} from "./checkAuth";
import React, {useState} from "react";
import TestResults from "./TestResults";
import EntityList from "./EntityList";
import UserEdit from "./UserEdit";
import AdminsHome from "./AdminsHome";
import AppNavbar from "./AppNavbar";

function App() {
    const [user, setUser] = useState({role: null});
    let home;

    useQuery('authUsers', () => fetch('/whoami', {mode: "no-cors"}).then(checkAuth), {
        onSuccess: (data) => setUser(data),
    });

    if (user.role === null) {
        return 'Loading...';
    }

    switch (user.role) {
        case "STUDENT" :
            home = <StudentsHome/>;
            break;
        case "TEACHER":
            home = <EntityList fetchUrl={"teachers-home/tests/"} entityName={"test"}/>;
            break;
        case "ADMIN":
            home = <AdminsHome/>;
            break;
    }

    return (<BrowserRouter>
        <AppNavbar/>
        <Routes>
            <Route path='/' exact={true} element={home}/>
            <Route path='/tests/:id/results' element={<TestResults/>}/>
            <Route path='/tests/:id' element={<TestEdit/>}/>
            <Route path='/users/:id' element={<UserEdit/>}/>
            <Route path='/:id/start' element={<TestPassing/>}/>
        </Routes>
    </BrowserRouter>)
}

export default App;