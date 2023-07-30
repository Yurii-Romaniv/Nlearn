import './App.css';
import {BrowserRouter, Route, Switch} from 'react-router-dom';
import TestEdit from "./TestEdit";
import TestList from "./TestList";
import TestPassing from "./TestPassing";
import StudentsHome from "./StudentsHome";
import 'bootstrap/dist/css/bootstrap.css';
import {useQuery} from "react-query";
import {checkAuth} from "./checkAuth";
import {useState} from "react";

function App() {
    const [user, setUser] = useState({role: null});

    useQuery('users', () => fetch('/whoami', {mode: "no-cors"}).then(checkAuth), {
        onSuccess: (data) => setUser(data),
    });

    if (user.role === null) {
        return 'Loading...';
    }

    return (
        <BrowserRouter>
            <Switch>
                <Route path='/' exact={true} component={user.role=="STUDENT"? StudentsHome : TestList}/>
                <Route path='/tests/:id' component={TestEdit}/>
                <Route path='/:id/start' component={TestPassing}/>
            </Switch>
        </BrowserRouter>
    )
}

export default App;